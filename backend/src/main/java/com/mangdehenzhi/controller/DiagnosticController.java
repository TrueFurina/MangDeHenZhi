package com.mangdehenzhi.controller;

import com.mangdehenzhi.dto.ApiResponse;
import com.mangdehenzhi.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * AI 学情诊断系统 — 20道诊断题 → 8维度画像 → 学习路径推荐
 */
@RestController
@RequestMapping("/api/diagnostic")
@RequiredArgsConstructor
public class DiagnosticController {

    private static final List<Map<String, Object>> QUESTIONS = buildQuestions();

    @GetMapping("/start")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> start() {
        return ResponseEntity.ok(ApiResponse.success(QUESTIONS));
    }

    @PostMapping("/submit")
    public ResponseEntity<ApiResponse<Map<String, Object>>> submit(
            @AuthenticationPrincipal User user,
            @RequestBody Map<String, Object> body) {
        @SuppressWarnings("unchecked")
        Map<String, String> answers = (Map<String, String>) body.get("answers");
        if (answers == null) answers = Map.of();

        // 计算各科得分
        Map<String, SubjectResultBuilder> subjectResults = new LinkedHashMap<>();
        int totalCorrect = 0, totalQ = 0;

        for (Map<String, Object> q : QUESTIONS) {
            String qId = (String) q.get("id");
            String subject = (String) q.get("subject");
            String subjectName = (String) q.get("subject_name");
            String topic = (String) q.get("topic");
            String correct = (String) q.get("answer");
            String userAns = answers != null ? answers.get(qId) : null;

            boolean isCorrect = userAns != null && userAns.equalsIgnoreCase(correct);
            if (isCorrect) totalCorrect++;
            totalQ++;

            SubjectResultBuilder sr = subjectResults.computeIfAbsent(subject, k ->
                    new SubjectResultBuilder(subject, subjectName));
            sr.total++;
            if (isCorrect) sr.correct++;
            if (!isCorrect && userAns != null) {
                sr.weakTopics.add(topic);
            }
        }

        // 构建结果
        double overallAccuracy = totalQ > 0 ? (double) totalCorrect / totalQ : 0;
        List<Map<String, Object>> results = new ArrayList<>();
        List<String> allWeakTopics = new ArrayList<>();

        for (SubjectResultBuilder srb : subjectResults.values()) {
            SubjectResult sr = srb.build();
            double acc = sr.total > 0 ? (double) sr.correct / sr.total : 0;
            allWeakTopics.addAll(sr.weakTopics);

            Map<String, Object> r = new LinkedHashMap<>();
            r.put("subject", sr.subject);
            r.put("subject_name", sr.subjectName);
            r.put("accuracy", Math.round(acc * 100.0) / 100.0);
            r.put("correct", sr.correct);
            r.put("total", sr.total);
            r.put("weak_topics", sr.weakTopics);
            results.add(r);
        }

        // 生成推荐
        String recommendation = overallAccuracy >= 0.8
                ? "🎉 基础扎实！建议直接进入进阶学习，重点攻克薄弱知识点"
                : overallAccuracy >= 0.6
                ? "👍 基础良好！建议系统复习薄弱环节，巩固后再进入下一阶段"
                : "📚 需要加强基础！建议从核心概念开始系统学习，打好基础再进阶";

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("overall_accuracy", Math.round(overallAccuracy * 100.0) / 100.0);
        result.put("total_correct", totalCorrect);
        result.put("total_questions", totalQ);
        result.put("results", results);
        result.put("weak_topics", allWeakTopics.stream().distinct().toList());
        result.put("overall_recommendation", recommendation);

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    private static List<Map<String, Object>> buildQuestions() {
        List<Map<String, Object>> qs = new ArrayList<>();

        // 计算机基础
        qs.add(q("d_cs_1", "computer_basics", "计算机基础", "计算机的核心部件CPU的功能是？",
                List.of("A: 存储数据", "B: 执行指令", "C: 显示图像", "D: 连接网络"), "B", "easy", "CPU"));
        qs.add(q("d_cs_2", "computer_basics", "计算机基础", "以下哪个是操作系统的核心功能？",
                List.of("A: 文字处理", "B: 进程管理", "C: 网页浏览", "D: 数据统计"), "B", "easy", "操作系统"));
        qs.add(q("d_cs_3", "computer_basics", "计算机基础", "二进制数1010转换为十进制是？",
                List.of("A: 8", "B: 9", "C: 10", "D: 12"), "C", "easy", "数制转换"));
        qs.add(q("d_cs_4", "computer_basics", "计算机基础", "以下哪个是输入设备？",
                List.of("A: 显示器", "B: 打印机", "C: 键盘", "D: 音箱"), "C", "easy", "硬件基础"));
        qs.add(q("d_cs_5", "computer_basics", "计算机基础", "软件生命周期中，哪个阶段最耗时？",
                List.of("A: 需求分析", "B: 设计", "C: 编码", "D: 维护"), "D", "medium", "软件工程"));

        // 沟通能力
        qs.add(q("d_comm_1", "communication", "沟通能力", "有效沟通的核心要素是？",
                List.of("A: 说得越多越好", "B: 双向理解与反馈", "C: 使用专业术语", "D: 加快语速"), "B", "easy", "沟通基础"));
        qs.add(q("d_comm_2", "communication", "沟通能力", "团队协作中遇到分歧时，最佳做法是？",
                List.of("A: 坚持己见", "B: 寻求共识与妥协", "C: 回避问题", "D: 让领导决定"), "B", "easy", "团队协作"));
        qs.add(q("d_comm_3", "communication", "沟通能力", "非语言沟通包括哪些？",
                List.of("A: 仅文字", "B: 仅语音", "C: 肢体语言与表情", "D: 仅书面"), "C", "medium", "非语言沟通"));
        qs.add(q("d_comm_4", "communication", "沟通能力", "汇报工作时应该优先？",
                List.of("A: 详细描述过程", "B: 先说结论再讲依据", "C: 先讲困难", "D: 只讲成绩"), "B", "medium", "工作汇报"));
        qs.add(q("d_comm_5", "communication", "沟通能力", "倾听时最重要的态度是？",
                List.of("A: 边听边想怎么回应", "B: 专注于理解对方", "C: 打断补充观点", "D: 做其他事"), "B", "easy", "倾听技巧"));

        // 问题解决
        qs.add(q("d_ps_1", "problem_solving", "问题解决能力", "解决问题的第一步是？",
                List.of("A: 寻找解决方案", "B: 明确定义问题", "C: 收集数据", "D: 咨询他人"), "B", "easy", "问题定义"));
        qs.add(q("d_ps_2", "problem_solving", "问题解决能力", "结构化思维的核心是？",
                List.of("A: 想到什么写什么", "B: 分类分层分析", "C: 依赖直觉", "D: 复制他人方案"), "B", "easy", "结构化思维"));
        qs.add(q("d_ps_3", "problem_solving", "问题解决能力", "根因分析常用的工具是？",
                List.of("A: SWOT分析", "B: 5Why分析法", "C: PDCA循环", "D: 甘特图"), "B", "medium", "根因分析"));
        qs.add(q("d_ps_4", "problem_solving", "问题解决能力", "决策时面对多个方案应？",
                List.of("A: 随便选一个", "B: 用评估矩阵对比", "C: 选最简单的", "D: 等别人决定"), "B", "medium", "决策方法"));
        qs.add(q("d_ps_5", "problem_solving", "问题解决能力", "创新解决问题时需要？",
                List.of("A: 按部就班", "B: 打破思维定势", "C: 遵循惯例", "D: 避免风险"), "B", "medium", "创新思维"));

        // 数据分析
        qs.add(q("d_da_1", "data_analysis", "数据分析", "数据分析中，均值反映的是？",
                List.of("A: 数据的离散程度", "B: 数据的集中趋势", "C: 数据分布形态", "D: 数据相关性"), "B", "easy", "统计基础"));
        qs.add(q("d_da_2", "data_analysis", "数据分析", "数据可视化的主要目的是？",
                List.of("A: 让图表好看", "B: 直观传递信息", "C: 占用空间", "D: 替代数据"), "B", "easy", "可视化"));
        qs.add(q("d_da_3", "data_analysis", "数据分析", "A/B测试中，什么是实验组？",
                List.of("A: 接受新方案的用户", "B: 接受旧方案的用户", "C: 随机样本", "D: 全部用户"), "A", "medium", "A/B测试"));
        qs.add(q("d_da_4", "data_analysis", "数据分析", "SQL中用于筛选数据的语句是？",
                List.of("A: SELECT", "B: WHERE", "C: JOIN", "D: GROUP BY"), "B", "medium", "SQL"));
        qs.add(q("d_da_5", "data_analysis", "数据分析", "相关系数接近1表示？",
                List.of("A: 强负相关", "B: 强正相关", "C: 无相关", "D: 弱相关"), "B", "hard", "相关性"));

        // 团队协作能力
        qs.add(q("d_tc_1", "team_collaboration", "团队协作", "敏捷开发中Scrum Master的职责是？",
                List.of("A: 分配任务", "B: 确保流程顺畅", "C: 编写代码", "D: 测试验收"), "B", "medium", "敏捷方法"));
        qs.add(q("d_tc_2", "team_collaboration", "团队协作", "团队中有人拖慢进度应该？",
                List.of("A: 指责", "B: 主动询问是否需要帮助", "C: 报告领导", "D: 忽略"), "B", "easy", "团队互助"));
        qs.add(q("d_tc_3", "team_collaboration", "团队协作", "代码评审的主要目的是？",
                List.of("A: 找茬", "B: 提升代码质量", "C: 增加工作量", "D: 监控进度"), "B", "medium", "代码评审"));
        qs.add(q("d_tc_4", "team_collaboration", "团队协作", "远程团队协作的最佳工具是？",
                List.of("A: 仅邮件", "B: 综合协作平台", "C: 仅电话", "D: 仅面对面"), "B", "easy", "远程协作"));
        qs.add(q("d_tc_5", "team_collaboration", "团队协作", "知识共享的最佳方式是？",
                List.of("A: 口口相传", "B: 文档化+定期分享", "C: 不共享", "D: 仅限核心成员"), "B", "medium", "知识管理"));

        return qs;
    }

    private static Map<String, Object> q(String id, String subject, String subjectName,
                                          String question, List<String> options,
                                          String answer, String difficulty, String topic) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", id);
        map.put("subject", subject);
        map.put("subject_name", subjectName);
        map.put("question", question);
        map.put("options", options);
        map.put("answer", answer);
        map.put("difficulty", difficulty);
        map.put("topic", topic);
        return map;
    }

    private record SubjectResult(String subject, String subjectName, int correct, int total, List<String> weakTopics) {}

    private static class SubjectResultBuilder {
        final String subject;
        final String subjectName;
        int correct;
        int total;
        List<String> weakTopics = new ArrayList<>();

        SubjectResultBuilder(String subject, String subjectName) {
            this.subject = subject;
            this.subjectName = subjectName;
        }

        SubjectResult build() {
            return new SubjectResult(subject, subjectName, correct, total, weakTopics);
        }
    }
}