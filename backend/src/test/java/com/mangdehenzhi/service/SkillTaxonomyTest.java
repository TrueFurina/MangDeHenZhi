package com.mangdehenzhi.service;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SkillTaxonomyTest {

    @Test
    void normalize_ShouldLowercaseAndTrim() {
        assertEquals("javascript", SkillTaxonomy.normalize("  JavaScript "));
        assertEquals("three.js", SkillTaxonomy.normalize("ThreeJS"));
    }

    @Test
    void matchScore_ExactMatch_ShouldReturn1() {
        assertEquals(1.0, SkillTaxonomy.matchScore("Java", "java"));
    }

    @Test
    void matchScore_AliasMatch_ShouldReturn1() {
        // "js" 是 "javascript" 的别名
        assertEquals(1.0, SkillTaxonomy.matchScore("js", "javascript"));
    }

    @Test
    void matchScore_AdjacentMatch_ShouldReturn06() {
        // vue 与 react 相邻
        assertEquals(0.6, SkillTaxonomy.matchScore("vue", "react"));
    }

    @Test
    void matchScore_ParentCategory_ShouldReturn03() {
        // mysql 与 go 同属 backend 分类，但互不相邻、不同义
        assertEquals(0.3, SkillTaxonomy.matchScore("mysql", "go"));
    }

    @Test
    void matchScore_NoMatch_ShouldReturn0() {
        assertEquals(0.0, SkillTaxonomy.matchScore("java", "vue"));
    }

    @Test
    void matchRate_ShouldCalculatePercentage() {
        // 完全匹配：100%
        assertEquals(100.0, SkillTaxonomy.matchRate(
                List.of("java", "mysql"),
                List.of("Java", "MySQL")));

        // 部分匹配：相邻技能 0.6 → 60%
        assertEquals(60.0, SkillTaxonomy.matchRate(
                List.of("vue"),
                List.of("react")));
    }

    @Test
    void categoryOf_ShouldMapKnownSkills() {
        assertEquals("backend", SkillTaxonomy.categoryOf("spring"));
        assertEquals("frontend", SkillTaxonomy.categoryOf("vue"));
        assertEquals("other", SkillTaxonomy.categoryOf("unknown_skill"));
    }
}