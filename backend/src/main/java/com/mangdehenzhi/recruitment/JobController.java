package com.mangdehenzhi.recruitment;

import com.mangdehenzhi.dto.ApiResponse;
import com.mangdehenzhi.dto.PageDTO;
import com.mangdehenzhi.entity.User;
import com.mangdehenzhi.service.DeepSeekService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 校招选岗 & 网申助手 REST 端点
 */
@RestController
@RequestMapping("/api/recruitment")
@RequiredArgsConstructor
public class JobController {

    private final JobRepository jobRepository;
    private final ApplicationRepository applicationRepository;
    private final SavedJobRepository savedJobRepository;
    private final JobMatchingService jobMatchingService;
    private final ApplicationAssistantService assistantService;
    private final DeepSeekService deepSeekService;

    // ===== 职位管理 =====

    @GetMapping("/jobs")
    public ResponseEntity<ApiResponse<?>> getAllJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String industry,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String degree,
            @RequestParam(required = false) String keyword) {
        PageRequest pr = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        if (industry != null || location != null || degree != null || (keyword != null && !keyword.isBlank())) {
            Page<Job> result = jobRepository.findFiltered(industry, location, degree, keyword, pr);
            return ResponseEntity.ok(ApiResponse.success(PageDTO.of(result.getContent(), page, size, result.getTotalElements())));
        }
        Page<Job> result = jobRepository.findByActiveTrue(pr);
        return ResponseEntity.ok(ApiResponse.success(PageDTO.of(result.getContent(), page, size, result.getTotalElements())));
    }

    @GetMapping("/jobs/{id}")
    public ResponseEntity<ApiResponse<Job>> getJob(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(
                jobRepository.findById(id).orElseThrow(() -> new RuntimeException("职位不存在"))));
    }

    @GetMapping("/jobs/search")
    public ResponseEntity<ApiResponse<List<Job>>> searchJobs(@RequestParam String q) {
        return ResponseEntity.ok(ApiResponse.success(jobMatchingService.searchJobs(q)));
    }

    @GetMapping("/industries")
    public ResponseEntity<ApiResponse<List<String>>> getIndustries() {
        List<String> industries = jobRepository.findByActiveTrue().stream()
                .map(Job::getIndustry)
                .distinct()
                .sorted()
                .toList();
        return ResponseEntity.ok(ApiResponse.success(industries));
    }

    // ===== 收藏职位 =====

    @PostMapping("/saved-jobs")
    public ResponseEntity<ApiResponse<SavedJob>> saveJob(
            @AuthenticationPrincipal User user,
            @RequestBody Map<String, Long> body) {
        Job job = jobRepository.findById(body.get("jobId"))
                .orElseThrow(() -> new RuntimeException("职位不存在"));
        if (savedJobRepository.findByUserIdAndJobId(user.getId(), job.getId()).isPresent()) {
            return ResponseEntity.ok(ApiResponse.success("已收藏", null));
        }
        SavedJob saved = SavedJob.builder()
                .user(user)
                .job(job)
                .build();
        return ResponseEntity.ok(ApiResponse.success("收藏成功", savedJobRepository.save(saved)));
    }

    @DeleteMapping("/saved-jobs/{jobId}")
    public ResponseEntity<ApiResponse<Void>> unsaveJob(
            @AuthenticationPrincipal User user,
            @PathVariable Long jobId) {
        savedJobRepository.findByUserIdAndJobId(user.getId(), jobId)
                .ifPresent(savedJobRepository::delete);
        return ResponseEntity.ok(ApiResponse.success("已取消收藏", null));
    }

    @GetMapping("/saved-jobs")
    public ResponseEntity<ApiResponse<List<SavedJob>>> getSavedJobs(
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ApiResponse.success(savedJobRepository.findByUserId(user.getId())));
    }

    // ===== AI 匹配 =====

    @PostMapping("/match")
    public ResponseEntity<ApiResponse<List<JobMatchingService.JobMatchResult>>> matchJobs(
            @AuthenticationPrincipal User user,
            @RequestBody Map<String, Integer> skillScores) {
        return ResponseEntity.ok(ApiResponse.success(
                jobMatchingService.matchJobs(skillScores, 10)));
    }

    // ===== 网申填报 =====

    @PostMapping("/applications")
    public ResponseEntity<ApiResponse<Application>> createApplication(
            @AuthenticationPrincipal User user,
            @RequestBody CreateApplicationRequest request) {
        Job job = jobRepository.findById(request.jobId())
                .orElseThrow(() -> new RuntimeException("职位不存在"));

        Application app = Application.builder()
                .user(user)
                .job(job)
                .companyName(job.getCompany())
                .positionName(job.getTitle())
                .status("DRAFT")
                .build();
        return ResponseEntity.ok(ApiResponse.success("网申创建成功", applicationRepository.save(app)));
    }

    @GetMapping("/applications")
    public ResponseEntity<ApiResponse<List<Application>>> getMyApplications(
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ApiResponse.success(applicationRepository.findByUserId(user.getId())));
    }

    /**
     * 求职仪表盘统计 — 按状态统计投递数
     */
    @GetMapping("/applications/stats")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getApplicationStats(
            @AuthenticationPrincipal User user) {
        List<Application> apps = applicationRepository.findByUserId(user.getId());

        Map<String, Long> byStatus = apps.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        Application::getStatus, java.util.stream.Collectors.counting()));

        long total = apps.size();
        long inProgress = byStatus.getOrDefault("SUBMITTED", 0L) + byStatus.getOrDefault("DRAFT", 0L);
        long interviewed = byStatus.getOrDefault("INTERVIEWED", 0L);
        long accepted = byStatus.getOrDefault("ACCEPTED", 0L);
        long rejected = byStatus.getOrDefault("REJECTED", 0L);

        Map<String, Object> stats = new java.util.LinkedHashMap<>();
        stats.put("total", total);
        stats.put("inProgress", inProgress);
        stats.put("interviewed", interviewed);
        stats.put("accepted", accepted);
        stats.put("rejected", rejected);
        stats.put("byStatus", byStatus);
        return ResponseEntity.ok(ApiResponse.success(stats));
    }

    @PutMapping("/applications/{id}/status")
    public ResponseEntity<ApiResponse<Application>> updateApplicationStatus(
            @AuthenticationPrincipal User user,
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        Application app = applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("申请不存在"));
        if (!app.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("无权操作");
        }
        app.setStatus(body.getOrDefault("status", app.getStatus()));
        return ResponseEntity.ok(ApiResponse.success(applicationRepository.save(app)));
    }

    @PostMapping("/applications/{id}/suggestions")
    public ResponseEntity<ApiResponse<Map<String, String>>> getSuggestions(
            @AuthenticationPrincipal User user,
            @PathVariable Long id,
            @RequestBody SuggestionRequest request) {
        Application app = applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("申请不存在"));

        Map<String, String> suggestions = assistantService.generateSuggestions(
                app.getCompanyName(),
                app.getPositionName(),
                request.jobDescription(),
                request.skillScores(),
                request.formFields()
        );
        return ResponseEntity.ok(ApiResponse.success(suggestions));
    }

    // S-006：单用户简历分析配额（每用户每小时上限），防止匿名/低成本的 LLM 额度滥用
    private static final int RESUME_QUOTA_PER_HOUR = 20;
    private final Cache<Long, AtomicInteger> resumeQuotaCache = Caffeine.<Long, AtomicInteger>newBuilder()
            .expireAfterWrite(Duration.ofHours(1))
            .maximumSize(100_000)
            .build();

    @PostMapping("/analyze-resume")
    public ResponseEntity<ApiResponse<String>> analyzeResume(
            @AuthenticationPrincipal User user,
            @RequestBody AnalyzeResumeRequest request) {
        if (user == null) {
            return ResponseEntity.status(401).body(ApiResponse.error(401, "请先登录后再使用简历分析"));
        }
        if (resumeQuotaCache.get(user.getId(), k -> new AtomicInteger(0)).incrementAndGet() > RESUME_QUOTA_PER_HOUR) {
            return ResponseEntity.status(429).body(ApiResponse.error(429, "本小时简历分析次数已达上限，请稍后再试"));
        }
        String result = assistantService.analyzeResumeFit(request.resumeText(), request.jobDescription());
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    /**
     * JD 翻译官 — 把抽象职位描述翻译成具体的工作日常/能力/薪资
     */
    @PostMapping("/translate-jd")
    public ResponseEntity<ApiResponse<String>> translateJd(
            @AuthenticationPrincipal User user,
            @RequestBody TranslateJdRequest request) {
        if (user == null) {
            return ResponseEntity.status(401).body(ApiResponse.error(401, "请先登录后再使用 JD 翻译"));
        }
        String result = deepSeekService.translateJobDescription(request.jobTitle(), request.jobDescription());
        if (result == null) {
            return ResponseEntity.status(503).body(ApiResponse.error(503, "AI 翻译服务暂时不可用"));
        }
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    // ===== DTOs =====

    public record CreateApplicationRequest(Long jobId) {}
    public record SuggestionRequest(String jobDescription, Map<String, Integer> skillScores, List<String> formFields) {}
    public record AnalyzeResumeRequest(String resumeText, String jobDescription) {}
    public record TranslateJdRequest(String jobTitle, String jobDescription) {}
}