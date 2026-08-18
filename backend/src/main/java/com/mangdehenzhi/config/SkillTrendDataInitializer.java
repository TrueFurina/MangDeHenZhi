package com.mangdehenzhi.config;

import com.mangdehenzhi.recruitment.SkillTrend;
import com.mangdehenzhi.recruitment.SkillTrendRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 技能趋势数据初始化器
 * 数据基于 Kaggle Skill Demand Index / Skill Scarcity Index / AI Requirements Index 数据集提炼
 */
@Slf4j
@Component
@Order(3)
@RequiredArgsConstructor
public class SkillTrendDataInitializer implements CommandLineRunner {

    private final SkillTrendRepository repository;

    @Override
    public void run(String... args) {
        if (repository.count() > 0) {
            log.info("技能趋势数据已存在 ({} 条)", repository.count());
            return;
        }

        log.info("========== 初始化技能趋势数据 ==========");
        initSkills();
        log.info("========== 技能趋势数据初始化完成 ==========");
    }

    private void initSkills() {
        // ===== AI / 技术类 =====
        save("深度学习", "AI", 98.5, 92.0, 45.2, "UP",
                "深度神经网络、Transformer架构、模型训练与调优", "['AI 与机器学习入门','Python高级编程']");
        save("大语言模型(LLM)", "AI", 97.2, 95.0, 52.8, "UP",
                "GPT、BERT等大模型的应用、微调与Prompt Engineering", "['AI 与机器学习入门','自然语言处理基础']");
        save("自然语言处理", "AI", 92.0, 88.5, 38.6, "UP",
                "文本分类、情感分析、机器翻译、对话系统", "['AI 与机器学习入门','Python数据科学']");
        save("计算机视觉", "AI", 88.5, 85.0, 30.4, "UP",
                "图像识别、目标检测、图像生成、视频分析", "['AI 与机器学习入门','Python数据科学']");
        save("Python", "TECHNOLOGY", 95.0, 80.0, 25.0, "UP",
                "Python编程、数据分析、自动化脚本、Web开发", "['Python数据科学','AI 与机器学习入门']");
        save("Java 企业级开发", "TECHNOLOGY", 88.0, 72.0, 12.5, "STABLE",
                "Spring Boot、微服务架构、分布式系统", "['Java 企业级开发实战','Web 前端进阶教程']");
        save("云原生技术", "TECHNOLOGY", 85.0, 82.0, 35.0, "UP",
                "Docker、Kubernetes、服务网格、Serverless", "['Java 企业级开发实战']");
        save("数据科学与分析", "TECHNOLOGY", 90.0, 78.0, 22.0, "UP",
                "SQL、数据可视化、统计分析、商业智能", "['Python数据科学','数据分析实战']");
        save("前端开发", "TECHNOLOGY", 82.0, 65.0, 8.5, "STABLE",
                "Vue.js、React、TypeScript、小程序开发", "['Web 前端进阶教程']");
        save("网络安全", "TECHNOLOGY", 80.0, 85.0, 28.0, "UP",
                "渗透测试、安全审计、漏洞挖掘、安全架构", "['Java 企业级开发实战']");

        // ===== 软技能类 =====
        save("沟通能力", "SOFT_SKILLS", 92.0, 70.0, 15.0, "STABLE",
                "有效表达、倾听理解、跨部门沟通、演讲汇报", "['沟通与协作技巧','商业思维与创新']");
        save("团队协作", "SOFT_SKILLS", 90.0, 68.0, 12.0, "STABLE",
                "敏捷协作、跨职能团队、冲突管理、远程协作", "['沟通与协作技巧','商业思维与创新']");
        save("问题解决能力", "SOFT_SKILLS", 88.0, 75.0, 18.0, "UP",
                "结构化思维、根因分析、创新方案设计、决策能力", "['沟通与协作技巧','商业思维与创新']");
        save("批判性思维", "SOFT_SKILLS", 85.0, 72.0, 20.0, "UP",
                "逻辑推理、论证分析、偏见识别、独立思考", "['商业思维与创新']");
        save("学习能力", "SOFT_SKILLS", 82.0, 65.0, 10.0, "STABLE",
                "快速学习新技术、知识迁移、自我提升、信息检索", "['AI 与机器学习入门','Python数据科学']");

        // ===== 商业类 =====
        save("数据分析思维", "BUSINESS", 88.0, 76.0, 22.0, "UP",
                "数据驱动决策、指标体系搭建、A/B测试、业务分析", "['商业思维与创新','Python数据科学']");
        save("项目管理", "BUSINESS", 85.0, 70.0, 15.0, "STABLE",
                "敏捷管理、Scrum、需求管理、风险管理", "['商业思维与创新','沟通与协作技巧']");
        save("产品思维", "BUSINESS", 82.0, 72.0, 18.0, "UP",
                "用户需求分析、产品规划、MVP设计、迭代优化", "['商业思维与创新','Web 前端进阶教程']");

        // ===== 新增：AI 技能扩展 =====
        save("机器学习", "AI", 94.0, 88.0, 32.0, "UP",
                "监督学习、无监督学习、强化学习、模型评估与选择", "['AI 与机器学习入门','Python数据科学']");
        save("Prompt Engineering", "AI", 93.0, 90.0, 60.0, "UP",
                "提示词设计、上下文管理、链式推理、工具调用", "['AI 与机器学习入门']");
        save("AI Agent开发", "AI", 91.0, 93.0, 55.0, "UP",
                "自主Agent设计、工具使用、多Agent协作、记忆管理", "['AI 与机器学习入门','Python高级编程']");
        save("模型微调(LLM)", "AI", 90.0, 91.0, 50.0, "UP",
                "LoRA/QLoRA微调、数据准备、评估与部署", "['AI 与机器学习入门','Python数据科学']");
        save("强化学习", "AI", 78.0, 82.0, 22.0, "UP",
                "Q-Learning、策略梯度、深度强化学习、环境建模", "['AI 与机器学习入门']");

        // ===== 新增：技术技能扩展 =====
        save("Go语言", "TECHNOLOGY", 78.0, 76.0, 28.0, "UP",
                "Go编程、并发模型、微服务、云原生开发", "['Java 企业级开发实战']");
        save("Rust语言", "TECHNOLOGY", 72.0, 80.0, 35.0, "UP",
                "系统编程、内存安全、WebAssembly、嵌入式开发", "['Java 企业级开发实战']");
        save("TypeScript", "TECHNOLOGY", 85.0, 68.0, 18.0, "UP",
                "类型系统、泛型、装饰器、前端框架集成", "['Web 前端进阶教程']");
        save("React Native", "TECHNOLOGY", 76.0, 70.0, 15.0, "STABLE",
                "跨平台移动开发、组件化、性能优化", "['Web 前端进阶教程']");
        save("微服务架构", "TECHNOLOGY", 86.0, 78.0, 20.0, "UP",
                "服务拆分、通信协议、服务治理、分布式事务", "['Java 企业级开发实战']");
        save("API设计与开发", "TECHNOLOGY", 84.0, 72.0, 15.0, "STABLE",
                "RESTful API、GraphQL、API网关、接口文档", "['Java 企业级开发实战','Web 前端进阶教程']");
        save("数据库管理", "TECHNOLOGY", 82.0, 74.0, 12.0, "STABLE",
                "MySQL、PostgreSQL、MongoDB、Redis、调优", "['Java 企业级开发实战','Python数据科学']");
        save("Linux系统管理", "TECHNOLOGY", 78.0, 70.0, 8.0, "STABLE",
                "Shell编程、系统运维、网络配置、安全加固", "['Java 企业级开发实战']");
        save("Git版本控制", "TECHNOLOGY", 80.0, 60.0, 5.0, "STABLE",
                "分支管理、协作流程、CI/CD集成、代码审查", "['Java 企业级开发实战']");

        // ===== 新增：软技能扩展 =====
        save("领导力", "SOFT_SKILLS", 85.0, 78.0, 15.0, "UP",
                "团队激励、决策能力、战略思维、教练辅导", "['沟通与协作技巧','商业思维与创新']");
        save("时间管理", "SOFT_SKILLS", 80.0, 65.0, 8.0, "STABLE",
                "优先级排序、GTD方法、番茄工作法、效率工具", "['沟通与协作技巧']");
        save("情绪智力", "SOFT_SKILLS", 82.0, 70.0, 12.0, "UP",
                "自我认知、情绪管理、同理心、人际关系", "['沟通与协作技巧']");
        save("跨文化沟通", "SOFT_SKILLS", 75.0, 72.0, 18.0, "UP",
                "跨文化意识、英语沟通、全球化协作", "['沟通与协作技巧']");
        save("演讲与表达", "SOFT_SKILLS", 80.0, 68.0, 10.0, "STABLE",
                "PPT设计、公众演讲、故事叙述、即兴表达", "['沟通与协作技巧','商业思维与创新']");
        save("谈判技巧", "SOFT_SKILLS", 76.0, 72.0, 12.0, "UP",
                "利益分析、议价策略、共识建立、冲突解决", "['商业思维与创新','沟通与协作技巧']");

        // ===== 新增：商业技能扩展 =====
        save("商业模式设计", "BUSINESS", 80.0, 78.0, 18.0, "UP",
                "商业模式画布、价值主张、收入模型、成本结构", "['商业思维与创新']");
        save("市场营销", "BUSINESS", 82.0, 72.0, 14.0, "STABLE",
                "数字营销、内容营销、品牌策略、用户增长", "['商业思维与创新']");
        save("财务管理", "BUSINESS", 78.0, 72.0, 10.0, "STABLE",
                "财务报表分析、预算管理、成本控制、投资评估", "['商业思维与创新']");
        save("运营管理", "BUSINESS", 76.0, 68.0, 8.0, "STABLE",
                "流程优化、供应链管理、质量控制、效率提升", "['商业思维与创新','沟通与协作技巧']");
        save("创业思维", "BUSINESS", 80.0, 76.0, 22.0, "UP",
                "机会识别、MVP开发、融资策略、团队组建", "['商业思维与创新']");

        // ===== 新增：设计与创意技能 =====
        save("用户研究", "DESIGN", 78.0, 74.0, 16.0, "UP",
                "用户访谈、可用性测试、问卷调研、用户画像", "['Web 前端进阶教程','商业思维与创新']");
        save("交互设计", "DESIGN", 80.0, 72.0, 12.0, "STABLE",
                "信息架构、交互流程、原型设计、动效设计", "['Web 前端进阶教程']");
        save("数据可视化", "DESIGN", 82.0, 70.0, 20.0, "UP",
                "图表设计、Dashboard设计、故事叙述、可视化工具", "['Python数据科学','商业思维与创新']");

        // ===== 新增：语言/专业类 =====
        save("英语(商务)", "LANGUAGE", 85.0, 65.0, 8.0, "STABLE",
                "商务邮件、会议英语、技术文档阅读、口语表达", "['沟通与协作技巧']");

        long count = repository.count();
        log.info("✅ 技能趋势数据初始化完成: {} 条", count);
    }

    private void save(String name, String category, double demand, double scarcity,
                       double growth, String trend, String desc, String courses) {
        repository.save(SkillTrend.builder()
                .skillName(name)
                .category(category)
                .demandIndex(demand)
                .scarcityIndex(scarcity)
                .growthRate(growth)
                .trend(trend)
                .description(desc)
                .relatedCourses(courses)
                .build());
    }
}