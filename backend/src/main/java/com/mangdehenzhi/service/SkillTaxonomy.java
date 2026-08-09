package com.mangdehenzhi.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Taxonomy 技能分类体系 — 借鉴 placement-ops 的 8 维技能分类思想
 *
 * 将零散技能映射到分类 + 别名/相邻技能，支撑可解释的匹配打分：
 *   - Exact    = 1.0（技能完全匹配）
 *   - Alias    = 1.0（同义技能，如 "JS" ↔ "JavaScript"）
 *   - Adjacent = 0.6（相邻技能，如 "Vue" 相邻 "React"）
 *   - Parent   = 0.3（技能是其分类的子技能）
 *   - None     = 0.0（无匹配）
 */
public final class SkillTaxonomy {

    private SkillTaxonomy() {}

    /** 技能 → 所属分类 */
    private static final Map<String, String> SKILL_CATEGORY = Map.ofEntries(
        Map.entry("java", "backend"),
        Map.entry("spring", "backend"),
        Map.entry("springboot", "backend"),
        Map.entry("mysql", "backend"),
        Map.entry("python", "backend"),
        Map.entry("fastapi", "backend"),
        Map.entry("go", "backend"),
        Map.entry("node", "backend"),
        Map.entry("vue", "frontend"),
        Map.entry("react", "frontend"),
        Map.entry("typescript", "frontend"),
        Map.entry("javascript", "frontend"),
        Map.entry("css", "frontend"),
        Map.entry("html", "frontend"),
        Map.entry("docker", "devops"),
        Map.entry("kubernetes", "devops"),
        Map.entry("ci/cd", "devops"),
        Map.entry("linux", "devops"),
        Map.entry("aws", "cloud"),
        Map.entry("aliyun", "cloud"),
        Map.entry("tensorflow", "ai"),
        Map.entry("pytorch", "ai"),
        Map.entry("llm", "ai"),
        Map.entry("prompt", "ai"),
        Map.entry("three.js", "3d"),
        Map.entry("webgl", "3d"),
        Map.entry("unity", "3d"),
        Map.entry("blockchain", "blockchain"),
        Map.entry("solidity", "blockchain"),
        Map.entry("product", "product"),
        Map.entry("ui/ux", "design"),
        Map.entry("figma", "design")
    );

    /** 别名映射（同义技能） */
    private static final Map<String, Set<String>> ALIASES = Map.of(
        "js", Set.of("javascript"),
        "javascript", Set.of("js"),
        "ts", Set.of("typescript"),
        "typescript", Set.of("ts"),
        "springboot", Set.of("spring boot"),
        "spring boot", Set.of("springboot"),
        "k8s", Set.of("kubernetes"),
        "kubernetes", Set.of("k8s"),
        "threed", Set.of("three.js"),
        "three.js", Set.of("threed")
    );

    /** 相邻技能组（技能之间高度相关） */
    private static final Map<String, Set<String>> ADJACENT = Map.of(
        "vue", Set.of("react", "svelte"),
        "react", Set.of("vue", "svelte"),
        "java", Set.of("kotlin", "go"),
        "python", Set.of("javascript", "java"),
        "mysql", Set.of("postgresql", "redis"),
        "docker", Set.of("kubernetes", "jenkins"),
        "tensorflow", Set.of("pytorch"),
        "pytorch", Set.of("tensorflow")
    );

    /** 规范化技能名（小写去空格） */
    public static String normalize(String skill) {
        if (skill == null) return "";
        return skill.trim().toLowerCase().replaceAll("\\s+", " ").replace(".js", "js").replace("threejs", "three.js");
    }

    /** 获取技能分类 */
    public static String categoryOf(String skill) {
        return SKILL_CATEGORY.getOrDefault(normalize(skill), "other");
    }

    /** 判断两个技能是否同义（别名） */
    public static boolean isAlias(String a, String b) {
        String na = normalize(a), nb = normalize(b);
        if (na.equals(nb)) return true;
        Set<String> aliases = ALIASES.get(na);
        return aliases != null && aliases.contains(nb);
    }

    /** 判断两个技能是否相邻 */
    public static boolean isAdjacent(String a, String b) {
        String na = normalize(a), nb = normalize(b);
        Set<String> adj = ADJACENT.get(na);
        return adj != null && adj.contains(nb);
    }

    /** 计算技能匹配分数（exact=1.0 / alias=1.0 / adjacent=0.6 / parent=0.3 / none=0） */
    public static double matchScore(String skill, String requiredSkill) {
        String ns = normalize(skill), nr = normalize(requiredSkill);
        if (ns.isEmpty() || nr.isEmpty()) return 0.0;

        // Exact
        if (ns.equals(nr)) return 1.0;
        // Alias（同义）
        if (isAlias(ns, nr)) return 1.0;
        // Adjacent（相邻）
        if (isAdjacent(ns, nr)) return 0.6;
        // Parent（技能是其分类子技能）：用户技能分类 == 要求技能分类
        if (categoryOf(ns).equals(categoryOf(nr)) && !categoryOf(ns).equals("other")) return 0.3;

        return 0.0;
    }

    /** 技能与一组要求技能的总体匹配率（0-100） */
    public static double matchRate(List<String> skills, List<String> requiredSkills) {
        if (skills == null || skills.isEmpty() || requiredSkills == null || requiredSkills.isEmpty()) {
            return 0.0;
        }
        double total = 0.0;
        for (String req : requiredSkills) {
            double best = 0.0;
            for (String skill : skills) {
                double s = matchScore(skill, req);
                if (s > best) best = s;
            }
            total += best;
        }
        return Math.round((total / requiredSkills.size()) * 100.0);
    }
}