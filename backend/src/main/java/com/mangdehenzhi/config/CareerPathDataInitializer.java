package com.mangdehenzhi.config;

import com.mangdehenzhi.recruitment.CareerPath;
import com.mangdehenzhi.recruitment.CareerPathRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 职业路径数据初始化器
 * 数据基于 Kaggle College Student Career Selection / AI Job Postings 2026 数据集提炼
 */
@Slf4j
@Component
@Order(4)
@RequiredArgsConstructor
public class CareerPathDataInitializer implements CommandLineRunner {

    private final CareerPathRepository repository;

    @Override
    public void run(String... args) {
        if (repository.count() > 0) {
            log.info("职业路径数据已存在 ({} 条)", repository.count());
            return;
        }
        log.info("========== 初始化职业路径数据 ==========");
        initCareerPaths();
        log.info("========== 职业路径数据初始化完成 ==========");
    }

    private void initCareerPaths() {
        // ===== AI / 数据科学类 =====
        save("AI工程师", "AI", "设计、训练和部署AI模型，包括大语言模型、计算机视觉和自然语言处理系统",
              "['Python','深度学习','大语言模型(LLM)','自然语言处理','计算机视觉','PyTorch/TensorFlow']",
              "['AI 与机器学习入门','Python数据科学','深度学习实战']",
              "20K-45K", 98.0, 95.0, "HARD", "['模型训练与调优','数据预处理','模型部署','性能评估']", "['计算机科学与技术','人工智能','数学']");

        save("数据科学家", "AI", "利用统计分析和机器学习方法从海量数据中提取洞察，支持业务决策",
              "['Python','SQL','统计学','机器学习','数据可视化','A/B测试']",
              "['AI 与机器学习入门','Python数据科学','商业思维与创新']",
              "18K-35K", 95.0, 90.0, "HARD", "['数据分析','建模','报告撰写','指标设计']", "['统计学','数学','计算机']");

        save("AI产品经理", "AI", "负责AI产品的需求分析、产品规划和迭代管理，协调算法、工程和业务团队",
              "['产品思维','数据分析','AI基础','项目管理','用户研究','沟通能力']",
              "['商业思维与创新','AI 与机器学习入门','沟通与协作技巧']",
              "18K-38K", 92.0, 88.0, "MEDIUM", "['需求分析','PRD撰写','跨部门协作','产品迭代']", "['计算机','经管','心理学']");

        // ===== 技术开发类 =====
        save("Java后端工程师", "TECH", "负责企业级应用的后端服务开发，构建高并发、高可用的分布式系统",
              "['Java','Spring Boot','微服务架构','MySQL','Redis','消息队列','Docker']",
              "['Java 企业级开发实战','沟通与协作技巧']",
              "12K-28K", 90.0, 82.0, "MEDIUM", "['接口开发','系统设计','性能优化','代码审查']", "['计算机科学与技术','软件工程']");

        save("全栈工程师", "TECH", "同时掌握前后端技术，能独立完成Web应用的完整开发",
              "['Vue.js/React','Java/Python','Node.js','数据库设计','DevOps','TypeScript']",
              "['Java 企业级开发实战','Web 前端进阶教程','Python数据科学']",
              "14K-30K", 92.0, 85.0, "MEDIUM", "['全栈开发','架构设计','技术选型','部署运维']", "['计算机','软件工程']");

        save("前端架构师", "TECH", "负责前端技术架构设计，制定前端规范，提升开发效率和用户体验",
              "['Vue.js','React','TypeScript','Webpack/Vite','性能优化','组件库设计']",
              "['Web 前端进阶教程','Java 企业级开发实战']",
              "15K-32K", 85.0, 80.0, "MEDIUM", "['架构设计','组件开发','性能优化','技术规范']", "['计算机','软件工程','数字媒体']");

        save("云原生工程师", "TECH", "基于云原生技术栈构建和管理可弹性伸缩的应用系统",
              "['Docker','Kubernetes','微服务','CI/CD','云服务(AWS/阿里云)','Go/Python']",
              "['Java 企业级开发实战','Python数据科学']",
              "16K-35K", 88.0, 88.0, "HARD", "['容器化','编排管理','监控告警','自动化运维']", "['计算机','软件工程']");

        save("网络安全工程师", "TECH", "负责企业安全体系建设，安全漏洞检测与防护",
              "['网络安全','渗透测试','密码学','安全架构','Python','Linux']",
              "['Java 企业级开发实战']",
              "15K-35K", 85.0, 90.0, "HARD", "['安全审计','漏洞挖掘','安全加固','应急响应']", "['信息安全','网络空间安全','计算机']");

        save("测试开发工程师", "TECH", "开发自动化测试框架和工具，保障软件质量",
              "['Python/Java','自动化测试','CI/CD','性能测试','测试框架设计']",
              "['Java 企业级开发实战','Python数据科学']",
              "10K-22K", 80.0, 75.0, "MEDIUM", "['测试框架开发','用例设计','质量保障','自动化']", "['计算机','软件工程']");

        // ===== 数据类 =====
        save("数据分析师", "TECH", "负责业务数据分析，为运营和决策提供数据支持",
              "['SQL','Python','Excel','数据可视化','统计学','业务理解']",
              "['Python数据科学','商业思维与创新','AI 与机器学习入门']",
              "8K-18K", 90.0, 78.0, "EASY", "['数据提取','报表开发','数据分析','洞察报告']", "['统计学','数学','经管','计算机']");

        save("数据工程师", "TECH", "构建和维护数据管道，确保数据的高效采集、存储和处理",
              "['SQL','Python','Spark','Hadoop','数据仓库','ETL开发','大数据平台']",
              "['Python数据科学','Java 企业级开发实战']",
              "15K-30K", 88.0, 85.0, "HARD", "['数据管道开发','数仓建设','ETL','性能优化']", "['计算机','软件工程','数学']");

        // ===== 软技能类 =====
        save("项目经理", "SOFT_SKILLS", "负责项目全生命周期管理，协调团队资源，确保项目按时交付",
              "['项目管理','敏捷方法','沟通能力','团队协作','风险管理','领导力']",
              "['商业思维与创新','沟通与协作技巧']",
              "12K-25K", 88.0, 75.0, "MEDIUM", "['项目规划','进度跟踪','风险管理','团队协调']", "['经管','工程管理','计算机']");

        save("产品经理", "SOFT_SKILLS", "负责产品全生命周期管理，从用户需求调研到产品上线迭代",
              "['产品思维','用户研究','数据分析','沟通能力','项目管理','商业洞察']",
              "['商业思维与创新','沟通与协作技巧','AI 与机器学习入门']",
              "14K-30K", 90.0, 80.0, "MEDIUM", "['需求分析','产品规划','数据分析','用户调研']", "['经管','心理学','计算机']");

        save("产品运营", "SOFT_SKILLS", "负责产品日常运营，通过数据驱动手段提升用户活跃和留存",
              "['数据分析','用户运营','内容运营','增长策略','A/B测试','沟通能力']",
              "['商业思维与创新','沟通与协作技巧','Python数据科学']",
              "8K-18K", 82.0, 72.0, "EASY", "['运营策略','数据分析','用户增长','活动策划']", "['经管','市场营销','心理学']");

        save("HRBP", "SOFT_SKILLS", "深入业务部门，提供人力资源战略支持，推动组织发展",
              "['人力资源管理','沟通能力','团队协作','数据分析','组织发展','心理学']",
              "['沟通与协作技巧','商业思维与创新']",
              "8K-18K", 78.0, 70.0, "EASY", "['人才招聘','员工关系','绩效管理','组织发展']", "['人力资源管理','心理学','经管']");

        // ===== 商业类 =====
        save("商业分析师", "BUSINESS", "通过数据分析和商业洞察，为企业战略决策提供支持",
              "['数据分析','商业洞察','SQL','Excel','沟通能力','PPT汇报']",
              "['商业思维与创新','Python数据科学','沟通与协作技巧']",
              "10K-22K", 85.0, 78.0, "MEDIUM", "['行业研究','竞品分析','数据建模','战略建议']", "['经管','统计学','金融']");

        save("咨询顾问", "BUSINESS", "为企业和机构提供专业的管理咨询服务，解决复杂业务问题",
              "['问题解决','数据分析','沟通能力','PPT汇报','行业知识','批判性思维']",
              "['商业思维与创新','沟通与协作技巧','Python数据科学']",
              "12K-28K", 82.0, 80.0, "MEDIUM", "['客户调研','方案设计','报告撰写','落地辅导']", "['经管','金融','理工科']");

        // ===== 设计类 =====
        save("UX/UI设计师", "DESIGN", "负责产品界面设计和用户体验优化，提升产品的易用性和美观度",
              "['Figma/Sketch','用户研究','交互设计','视觉设计','设计系统','设计思维']",
              "['Web 前端进阶教程','商业思维与创新']",
              "10K-22K", 82.0, 75.0, "MEDIUM", "['界面设计','交互原型','用户测试','设计规范']", "['数字媒体艺术','视觉传达','工业设计']");

        long count = repository.count();
        log.info("✅ 职业路径数据初始化完成: {} 条", count);
    }

    private void save(String title, String category, String desc, String skills,
                       String courses, String salary, double demand, double growth,
                       String difficulty, String tasks, String majors) {
        repository.save(CareerPath.builder()
                .title(title)
                .category(category)
                .description(desc)
                .requiredSkills(skills)
                .recommendedCourses(courses)
                .salaryRange(salary)
                .demandScore(demand)
                .growthPotential(growth)
                .difficulty(difficulty)
                .typicalTasks(tasks)
                .relatedMajors(majors)
                .build());
    }
}