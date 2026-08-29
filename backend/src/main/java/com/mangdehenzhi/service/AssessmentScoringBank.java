package com.mangdehenzhi.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 测评题库与服务端权威计分引擎（North Star M1）。
 *
 * 核心原则：前端只提交作答（题目 id -> 选项值），维度分由后端在此处重算，
 * 客户端传来的分数一律不信任（服务端权威计分，对应 P0 根因修复）。
 */
public class AssessmentScoringBank {

    public record OptionSpec(int value, int score) {}

    public record QuestionSpec(String id, String dimension, List<OptionSpec> options) {}

    public record DimensionSpec(String dimension, String label, List<QuestionSpec> questions) {}

    private final Map<String, DimensionSpec> dimensions = new LinkedHashMap<>();
    private final Map<String, QuestionSpec> questionsById = new LinkedHashMap<>();

    public AssessmentScoringBank() {
        buildBank();
    }

    private void registerDimension(String dimension, String label) {
        dimensions.put(dimension, new DimensionSpec(dimension, label, new ArrayList<>()));
    }

    /** 注册题目：opts 为 (value, score) 成对出现，例如 q("communication","comm-1", 4,90, 3,65, 2,35, 1,15) */
    private void q(String dimension, String id, int... opts) {
        DimensionSpec d = dimensions.get(dimension);
        List<  OptionSpec> options = new ArrayList<>();
        for (int i = 0; i + 1 < opts.length; i += 2) {
            options.add(new OptionSpec(opts[i], opts[i + 1]));
        }
        QuestionSpec qs = new QuestionSpec(id, dimension, options);
        d.questions().add(qs);
        questionsById.put(id, qs);
    }

    private void buildBank() {
        registerDimension("communication", "沟通能力");
        registerDimension("collaboration", "协作能力");
        registerDimension("problem_solving", "问题解决能力");
        registerDimension("leadership", "领导力");
        registerDimension("adaptability", "适应力");

        // 题库：每维度 11 题，每题 4 档（value 4/3/2/1 -> 分值）。
        // 注：分值权重为从编译产物还原的代表性样例，可按业务校准；引擎本身为权威计分。
        String[][] dims = {
                {"communication", "comm"},
                {"collaboration", "collab"},
                {"problem_solving", "prob"},
                {"leadership", "lead"},
                {"adaptability", "adapt"}
        };
        for (String[] d : dims) {
            for (int i = 1; i <= 11; i++) {
                q(d[0], d[1] + "-" + i, 4, 90, 3, 65, 2, 35, 1, 15);
            }
        }
    }

    /** 服务端权威计分：仅依据题库与作答重算各维度得分（维度 -> 原始累加分）。 */
    public Map<String, Integer> computeDimensionScores(Map<String, Integer> answers) {
        Map<String, Integer> result = new LinkedHashMap<>();
        for (DimensionSpec d : dimensions.values()) {
            int total = 0;
            for (QuestionSpec qs : d.questions()) {
                Integer chosen = answers.get(qs.id());
                if (chosen == null) {
                    continue;
                }
                for (OptionSpec o : qs.options()) {
                    if (o.value() == chosen) {
                        total += o.score();
                        break;
                    }
                }
            }
            result.put(d.dimension(), total);
        }
        return result;
    }

    public java.util.Set<String> getAllQuestionIds() {
        return questionsById.keySet();
    }

    public List<DimensionSpec> getDimensions() {
        return new ArrayList<>(dimensions.values());
    }
}
