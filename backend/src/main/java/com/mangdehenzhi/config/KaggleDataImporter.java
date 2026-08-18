package com.mangdehenzhi.config;

import com.mangdehenzhi.recruitment.SkillTrend;
import com.mangdehenzhi.recruitment.SkillTrendRepository;
import com.mangdehenzhi.recruitment.CareerPath;
import com.mangdehenzhi.recruitment.CareerPathRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Kaggle 真实 CSV 数据导入器
 * 读取下载的真实数据集文件，导入数据库
 * 数据来源:
 *   - datamatastudios/skill-demand-index
 *   - datamatastudios/skill-scarcity-index
 *   - datamatastudios/ai-requirements-index
 *   - zayn1999/college-student-career-selection-dataset
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class KaggleDataImporter {

    private final SkillTrendRepository skillTrendRepository;
    private final CareerPathRepository careerPathRepository;

    @PostConstruct
    public void importData() {
        if (skillTrendRepository.count() > 50) {
            log.info("Kaggle 数据已存在，跳过导入 (SkillTrend: {} 条)", skillTrendRepository.count());
            return;
        }

        log.info("========== 开始导入 Kaggle 真实数据 ==========");
        try {
            importSkillDemand();
            importSkillScarcity();
            importAiRequirements();
            importCareerSelection();
            log.info("========== Kaggle 真实数据导入完成 ==========");
        } catch (Exception e) {
            log.error("Kaggle 数据导入失败: {}", e.getMessage());
        }
    }

    private void importSkillDemand() throws Exception {
        List<String[]> rows = readCsv("data/skill-demand-index.csv");
        if (rows.isEmpty()) return;

        // 列: snapshot_date, category, skill, skill_group, listing_count, total_listings, demand_pct, required_count
        Map<String, Double> skillScores = new LinkedHashMap<>();
        for (String[] row : rows) {
            String skill = row[2];
            double demandPct = Double.parseDouble(row[6]);
            skillScores.merge(skill, demandPct, Math::max);
        }

        // 取前20条写入 SkillTrend
        int count = 0;
        for (Map.Entry<String, Double> entry : skillScores.entrySet()) {
            if (count++ >= 20) break;
            String skill = entry.getKey();
            double demand = Math.min(100, entry.getValue() * 8);

            if (skillTrendRepository.findBySkillNameContainingIgnoreCase(skill).isEmpty()) {
                skillTrendRepository.save(SkillTrend.builder()
                        .skillName(skill)
                        .category(mapCategory(skill))
                        .demandIndex(demand)
                        .scarcityIndex(Math.min(100, demand * 0.85))
                        .growthRate(Math.min(50, demand * 0.3))
                        .trend(demand > 60 ? "UP" : "STABLE")
                        .description(skill + " 技能需求分析")
                        .relatedCourses("['AI 与机器学习入门']")
                        .build());
            }
        }
        log.info("✅ Skill Demand Index 导入: {} 条", count);
    }

    private void importSkillScarcity() throws Exception {
        List<String[]> rows = readCsv("data/skill-scarcity-index.csv");
        if (rows.isEmpty()) return;

        // 列: snapshot_date, category, skill_name, demand_count, demand_pct, median_days_open, salary_premium_pct, repost_rate_pct, scarcity_score
        int count = 0;
        for (String[] row : rows) {
            if (count++ >= 20) break;
            String skill = row[2];
            double scarcity = Double.parseDouble(row[8]);
            double salaryPremium = Double.parseDouble(row[6]);

            if (skillTrendRepository.findBySkillNameContainingIgnoreCase(skill).isEmpty()) {
                skillTrendRepository.save(SkillTrend.builder()
                        .skillName(skill)
                        .category(mapCategory(skill))
                        .demandIndex(Math.min(100, scarcity * 1.5))
                        .scarcityIndex(Math.min(100, scarcity))
                        .growthRate(Math.min(50, salaryPremium))
                        .trend("UP")
                        .description(skill + " — 薪资溢价 " + String.format("%.1f", salaryPremium) + "%")
                        .relatedCourses("['AI 与机器学习入门']")
                        .build());
            }
        }
        log.info("✅ Skill Scarcity Index 导入: {} 条", count);
    }

    private void importAiRequirements() throws Exception {
        List<String[]> rows = readCsv("data/ai-requirements-index.csv");
        if (rows.isEmpty()) return;

        // 列: snapshot_date, category, seniority, tier, listings_with_ai, total_listings, pct, required_count
        int count = 0;
        for (String[] row : rows) {
            if (count++ >= 15) break;
            String tier = row[3];
            double pct = Double.parseDouble(row[6]);

            String skillName = "AI需求-" + tier;
            if (skillTrendRepository.findBySkillNameContainingIgnoreCase(skillName).isEmpty()) {
                skillTrendRepository.save(SkillTrend.builder()
                        .skillName(skillName)
                        .category("AI")
                        .demandIndex(Math.min(100, pct))
                        .scarcityIndex(Math.min(100, pct * 0.9))
                        .growthRate(35.0)
                        .trend("UP")
                        .description(tier + " AI 技能需求占比 " + String.format("%.1f", pct) + "%")
                        .relatedCourses("['AI 与机器学习入门']")
                        .build());
            }
        }
        log.info("✅ AI Requirements Index 导入: {} 条", count);
    }

    private void importCareerSelection() throws Exception {
        List<String[]> rows = readCsv("data/college_student_career_selection_dataset.csv");
        if (rows.isEmpty()) return;

        // 列: student_id, age, gender, degree_program, cgpa, career_interest_domain, recommended_career, ...
        Map<String, List<String[]>> careerGroups = new LinkedHashMap<>();
        for (String[] row : rows) {
            String career = row[rows.get(0).length - 1]; // 最后一列是 recommended_career
            careerGroups.computeIfAbsent(career, k -> new ArrayList<>()).add(row);
        }

        int count = 0;
        for (Map.Entry<String, List<String[]>> entry : careerGroups.entrySet()) {
            if (count++ >= 15) break;
            String career = entry.getKey();
            List<String[]> students = entry.getValue();

            // 计算平均 CGPA 和技能分数
            double avgCgpa = students.stream()
                    .mapToDouble(s -> Double.parseDouble(s[4]))
                    .average().orElse(0);

            if (careerPathRepository.findByTitleContainingIgnoreCase(career).isEmpty()) {
                careerPathRepository.save(CareerPath.builder()
                        .title(career)
                        .category("TECH")
                        .description("基于 " + students.size() + " 名学生的职业偏好分析，平均GPA " + String.format("%.2f", avgCgpa))
                        .requiredSkills("['Python','数据分析','沟通能力','团队协作']")
                        .recommendedCourses("['AI 与机器学习入门','商业思维与创新']")
                        .salaryRange("12K-25K")
                        .demandScore(Math.min(100.0, 60.0 + students.size() * 2.0))
                        .growthPotential(80.0)
                        .difficulty("MEDIUM")
                        .typicalTasks("['数据分析','报告撰写','项目管理']")
                        .relatedMajors("['计算机科学','数据科学','经管']")
                        .build());
            }
        }
        log.info("✅ Career Selection 导入: {} 个职业方向", count);
    }

    private List<String[]> readCsv(String path) throws Exception {
        List<String[]> rows = new ArrayList<>();
        ClassPathResource resource = new ClassPathResource(path);
        if (!resource.exists()) {
            log.warn("CSV 文件不存在: {}", path);
            return rows;
        }
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) { firstLine = false; continue; } // 跳过标题行
                if (line.isBlank()) continue;
                rows.add(parseCsvLine(line));
            }
        }
        log.info("  读取 {}: {} 行数据", path, rows.size());
        return rows;
    }

    private String[] parseCsvLine(String line) {
        List<String> fields = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder field = new StringBuilder();
        for (char c : line.toCharArray()) {
            if (c == '"') { inQuotes = !inQuotes; continue; }
            if (c == ',' && !inQuotes) {
                fields.add(field.toString().trim());
                field = new StringBuilder();
                continue;
            }
            field.append(c);
        }
        fields.add(field.toString().trim());
        return fields.toArray(new String[0]);
    }

    private String mapCategory(String skill) {
        String s = skill.toLowerCase();
        if (s.contains("python") || s.contains("sql") || s.contains("java") || s.contains("javascript")
            || s.contains("react") || s.contains("docker") || s.contains("kubernetes")
            || s.contains("git") || s.contains("linux") || s.contains("aws")
            || s.contains("cloud") || s.contains("devops") || s.contains("api"))
            return "TECHNOLOGY";
        if (s.contains("ai") || s.contains("machine learning") || s.contains("deep learning")
            || s.contains("nlp") || s.contains("llm") || s.contains("neural")
            || s.contains("tensorflow") || s.contains("pytorch") || s.contains("data science"))
            return "AI";
        if (s.contains("communication") || s.contains("team") || s.contains("leadership")
            || s.contains("management") || s.contains("presentation") || s.contains("writing"))
            return "SOFT_SKILLS";
        if (s.contains("business") || s.contains("marketing") || s.contains("finance")
            || s.contains("strategy") || s.contains("sales"))
            return "BUSINESS";
        if (s.contains("design") || s.contains("ui") || s.contains("ux") || s.contains("figma"))
            return "DESIGN";
        return "TECHNOLOGY";
    }
}