package com.mangdehenzhi.config;

import com.mangdehenzhi.recruitment.Job;
import com.mangdehenzhi.recruitment.JobRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 校招职位数据初始化器
 * 数据来源策略：
 *   方案一（首推）：官方公开公益岗位数据（福建本地人社/教育厅渠道）
 *   方案二（备选）：第三方合规聚合数据API
 *   方案三（开发用）：AI生成模拟数据（本类实现）
 *
 * 本类使用方案三（AI生成福建省本地化模拟数据）作为开发基础数据，
 * 后续可替换为方案一的真实公开岗位数据。
 */
@Slf4j
@Component
@Order(2)
@RequiredArgsConstructor
public class RecruitmentDataInitializer implements CommandLineRunner {

    private final JobRepository jobRepository;

    @Override
    public void run(String... args) {
        if (jobRepository.count() > 0) {
            log.info("招聘数据已存在，跳过初始化 ({} 条)", jobRepository.count());
            return;
        }

        log.info("========== 初始化校招职位数据 ==========");
        initJobs();
        log.info("========== 校招职位数据初始化完成 ==========");
    }

    private void initJobs() {
        // ===== 互联网/IT 行业 =====
        jobRepository.save(Job.builder()
                .title("Java后端开发工程师")
                .company("网龙网络控股有限公司")
                .industry("互联网/IT")
                .location("福州")
                .salary("8K-15K")
                .degree("本科及以上")
                .major("计算机科学与技术、软件工程")
                .description("负责公司核心产品的后端服务开发与维护；参与系统架构设计和技术方案评审；编写高质量代码并完成单元测试。")
                .requirements("1. 2026届本科及以上学历，计算机相关专业；2. 熟悉Java、Spring Boot框架；3. 了解MySQL、Redis等数据库；4. 有实际项目经验优先。")
                .source("福建人才联合网·校招专区")
                .build());

        jobRepository.save(Job.builder()
                .title("前端开发工程师")
                .company("锐捷网络股份有限公司")
                .industry("互联网/IT")
                .location("福州")
                .salary("8K-14K")
                .degree("本科及以上")
                .major("计算机、软件工程")
                .description("负责公司产品Web前端开发；与后端工程师协作完成接口联调；持续优化前端用户体验。")
                .requirements("1. 2026届本科及以上学历；2. 熟练Vue.js/React框架；3. 了解TypeScript、Webpack；4. 有移动端开发经验优先。")
                .source("福建省毕业生就业创业公共服务网")
                .build());

        jobRepository.save(Job.builder()
                .title("产品经理（校招）")
                .company("美团·福州研发中心")
                .industry("互联网/IT")
                .location("福州")
                .salary("10K-18K")
                .degree("本科及以上")
                .major("不限，计算机/经管优先")
                .description("负责本地生活服务产品的需求分析与规划；撰写PRD并推动产品迭代；协调设计、研发、测试团队完成产品交付。")
                .requirements("1. 2026届本科及以上学历；2. 具备较强的逻辑分析能力和沟通能力；3. 有互联网公司实习经验优先。")
                .source("国家24365大学生就业服务平台")
                .build());

        jobRepository.save(Job.builder()
                .title("数据分析师")
                .company("博思软件股份有限公司")
                .industry("互联网/IT")
                .location("福州")
                .salary("7K-12K")
                .degree("本科及以上")
                .major("统计学、数学、计算机")
                .description("负责业务数据分析与报表开发；构建数据指标体系；为业务决策提供数据支持。")
                .requirements("1. 2026届本科及以上学历；2. 熟练SQL、Python；3. 了解数据可视化工具（Tableau/FineBI）；4. 有数据分析相关项目经验。")
                .source("福建人才联合网")
                .build());

        jobRepository.save(Job.builder()
                .title("测试工程师")
                .company("新大陆科技集团")
                .industry("互联网/IT")
                .location("福州")
                .salary("6K-10K")
                .degree("本科及以上")
                .major("计算机相关专业")
                .description("负责产品功能测试与自动化测试用例编写；跟踪缺陷生命周期；参与质量保障体系建设。")
                .requirements("1. 2026届本科及以上学历；2. 了解软件测试理论和方法；3. 熟悉至少一种测试工具（Selenium/JMeter）；4. 细致耐心，有责任心。")
                .source("国聘网")
                .build());

        // ===== 金融行业 =====
        jobRepository.save(Job.builder()
                .title("银行管培生（金融科技方向）")
                .company("兴业银行·福州总行")
                .industry("金融")
                .location("福州")
                .salary("12K-20K")
                .degree("硕士及以上")
                .major("金融、计算机、数学")
                .description("参与总行金融科技项目的规划与实施；轮岗培养，全面了解银行业务与技术体系。")
                .requirements("1. 2026届硕士及以上学历；2. 具备金融与技术的复合背景优先；3. 有银行或金融科技公司实习经验优先。")
                .source("福建省毕业生就业创业公共服务网")
                .build());

        jobRepository.save(Job.builder()
                .title("风控数据分析师")
                .company("福建海峡银行")
                .industry("金融")
                .location("福州")
                .salary("8K-14K")
                .degree("本科及以上")
                .major("金融工程、统计学、数学")
                .description("负责信用风险模型的开发与维护；分析用户行为数据，优化风控策略。")
                .requirements("1. 2026届本科及以上学历；2. 熟练Python/R语言；3. 了解机器学习算法；4. 有金融风控项目经验优先。")
                .source("福建人才联合网")
                .build());

        jobRepository.save(Job.builder()
                .title("证券研究助理")
                .company("华福证券有限责任公司")
                .industry("金融")
                .location("福州")
                .salary("8K-12K")
                .degree("硕士及以上")
                .major("金融学、经济学")
                .description("协助研究员完成行业和公司研究工作；撰写研究报告和投资建议；维护研究数据库。")
                .requirements("1. 2026届硕士及以上学历；2. 具备扎实的财务分析能力；3. 通过CFA/CPA考试优先。")
                .source("国聘网·福建专区")
                .build());

        jobRepository.save(Job.builder()
                .title("保险精算助理")
                .company("中国人寿·福建分公司")
                .industry("金融")
                .location("福州")
                .salary("7K-12K")
                .degree("本科及以上")
                .major("精算学、统计学、数学")
                .description("协助精算师完成产品定价和准备金评估；参与精算模型开发与维护。")
                .requirements("1. 2026届本科及以上学历；2. 通过精算师考试基础科目优先；3. 熟练Excel、Python或R。")
                .source("国家24365大学生就业服务平台")
                .build());

        // ===== 教育行业 =====
        jobRepository.save(Job.builder()
                .title("高校辅导员")
                .company("福建师范大学")
                .industry("教育")
                .location("福州")
                .salary("6K-10K")
                .degree("硕士及以上")
                .major("不限，教育学/心理学/思政优先")
                .description("负责学生思想政治教育和日常管理；组织学生活动和就业指导工作。")
                .requirements("1. 2026届硕士及以上学历；2. 中共党员优先；3. 有学生干部经验优先；4. 具备良好的沟通和协调能力。")
                .source("福建省毕业生就业创业公共服务网")
                .build());

        jobRepository.save(Job.builder()
                .title("在线教育产品运营")
                .company("网龙·华渔教育")
                .industry("教育")
                .location("福州")
                .salary("7K-11K")
                .degree("本科及以上")
                .major("教育学、计算机、市场营销")
                .description("负责在线教育产品的用户运营和内容运营；分析用户数据，优化运营策略。")
                .requirements("1. 2026届本科及以上学历；2. 熟悉教育行业；3. 有用户运营或新媒体运营经验优先。")
                .source("福建人才联合网")
                .build());

        jobRepository.save(Job.builder()
                .title("AI教育产品经理")
                .company("科大讯飞·福建教育BG")
                .industry("教育")
                .location("福州")
                .salary("10K-18K")
                .degree("本科及以上")
                .major("教育技术、计算机、心理学")
                .description("负责AI教育产品的需求分析和产品规划；跟踪教育行业趋势，挖掘用户需求。")
                .requirements("1. 2026届本科及以上学历；2. 了解AI技术在教育领域的应用；3. 有产品设计或教育行业实习经验。")
                .source("国家24365大学生就业服务平台")
                .build());

        // ===== 制造/硬件行业 =====
        jobRepository.save(Job.builder()
                .title("嵌入式软件工程师")
                .company("瑞芯微电子股份有限公司")
                .industry("制造/硬件")
                .location("福州")
                .salary("9K-16K")
                .degree("本科及以上")
                .major("电子信息工程、通信工程、计算机")
                .description("负责嵌入式系统驱动开发与调试；参与芯片验证平台的软件开发。")
                .requirements("1. 2026届本科及以上学历；2. 熟悉C/C++语言；3. 了解嵌入式系统原理；4. 有RTOS或Linux驱动开发经验优先。")
                .source("福建人才联合网·芯光大道")
                .build());

        jobRepository.save(Job.builder()
                .title("硬件工程师")
                .company("星网锐捷通讯股份有限公司")
                .industry("制造/硬件")
                .location("福州")
                .salary("8K-14K")
                .degree("本科及以上")
                .major("电子信息、通信工程")
                .description("负责硬件电路设计与调试；参与产品方案评估和器件选型。")
                .requirements("1. 2026届本科及以上学历；2. 熟练Altium Designer/Cadence等EDA工具；3. 有硬件设计竞赛或项目经验优先。")
                .source("福建省毕业生就业创业公共服务网")
                .build());

        jobRepository.save(Job.builder()
                .title("工业机器人调试工程师")
                .company("福建星云电子股份有限公司")
                .industry("制造/硬件")
                .location("福州")
                .salary("7K-12K")
                .degree("本科及以上")
                .major("自动化、机械电子工程")
                .description("负责工业机器人设备的现场调试和维护；参与自动化产线方案设计。")
                .requirements("1. 2026届本科及以上学历；2. 了解PLC编程和工业通信协议；3. 有工控竞赛经验优先。")
                .source("福州人才市场")
                .build());

        // ===== 医疗/生物行业 =====
        jobRepository.save(Job.builder()
                .title("医学信息分析员")
                .company("福建医科大学附属协和医院")
                .industry("医疗/生物")
                .location("福州")
                .salary("6K-10K")
                .degree("本科及以上")
                .major("医学信息工程、计算机、公共卫生")
                .description("负责医疗数据的采集、整理和分析；参与医院信息化项目。")
                .requirements("1. 2026届本科及以上学历；2. 熟悉SQL和数据分析工具；3. 有医疗信息系统项目经验优先。")
                .source("福建省毕业生就业创业公共服务网")
                .build());

        jobRepository.save(Job.builder()
                .title("生物信息分析工程师")
                .company("福州迈新生物技术开发有限公司")
                .industry("医疗/生物")
                .location("福州")
                .salary("8K-14K")
                .degree("硕士及以上")
                .major("生物信息学、计算生物学")
                .description("负责高通量测序数据的分析和解读；开发生物信息分析流程。")
                .requirements("1. 2026届硕士及以上学历；2. 熟练Python/R；3. 了解生物信息学常用工具和数据库。")
                .source("福建人才联合网")
                .build());

        // ===== 房地产/建筑行业 =====
        jobRepository.save(Job.builder()
                .title("BIM工程师")
                .company("福建省建筑设计研究院")
                .industry("房地产/建筑")
                .location("福州")
                .salary("6K-10K")
                .degree("本科及以上")
                .major("土木工程、建筑学、工程管理")
                .description("负责BIM模型的建立与维护；参与项目协同设计和碰撞检查。")
                .requirements("1. 2026届本科及以上学历；2. 熟练Revit/Navisworks等BIM软件；3. 有BIM竞赛或项目经验优先。")
                .source("福建人才联合网·建筑设计专区")
                .build());

        jobRepository.save(Job.builder()
                .title("工程管理培训生")
                .company("融侨集团股份有限公司")
                .industry("房地产/建筑")
                .location("福州")
                .salary("6K-10K")
                .degree("本科及以上")
                .major("工程管理、土木工程")
                .description("参与项目现场施工管理和进度控制；协助项目成本管理和质量验收。")
                .requirements("1. 2026届本科及以上学历；2. 了解工程项目管理流程；3. 有施工企业实习经验优先。")
                .source("国家24365大学生就业服务平台")
                .build());

        // ===== 零售/电商行业 =====
        jobRepository.save(Job.builder()
                .title("电商运营专员")
                .company("永辉超市股份有限公司")
                .industry("零售/电商")
                .location("福州")
                .salary("5K-8K")
                .degree("本科及以上")
                .major("电子商务、市场营销、工商管理")
                .description("负责线上店铺的日常运营和活动策划；分析运营数据，优化销售策略。")
                .requirements("1. 2026届本科及以上学历；2. 熟悉主流电商平台运营规则；3. 有电商运营实习经验优先。")
                .source("福建省毕业生就业创业公共服务网")
                .build());

        jobRepository.save(Job.builder()
                .title("供应链管理培训生")
                .company("福建麦当劳·供应链中心")
                .industry("零售/电商")
                .location("福州")
                .salary("5K-8K")
                .degree("本科及以上")
                .major("物流管理、供应链管理")
                .description("参与供应链各环节的轮岗培训；协助优化物流配送效率。")
                .requirements("1. 2026届本科及以上学历；2. 了解供应链管理基础理论；3. 有物流企业实习经验优先。")
                .source("福州人才市场")
                .build());

        // ===== 咨询/专业服务行业 =====
        jobRepository.save(Job.builder()
                .title("管理咨询顾问（校招）")
                .company("福建省企联咨询服务中心")
                .industry("咨询/专业服务")
                .location("福州")
                .salary("7K-12K")
                .degree("本科及以上")
                .major("管理类、经济类")
                .description("参与企业战略咨询项目；进行行业研究和数据分析；撰写咨询报告。")
                .requirements("1. 2026届本科及以上学历；2. 具备较强的逻辑分析能力和文字表达能力；3. 熟练Office和数据分析工具。")
                .source("福建人才联合网")
                .build());

        jobRepository.save(Job.builder()
                .title("知识产权顾问")
                .company("福建省知识产权保护中心")
                .industry("咨询/专业服务")
                .location("福州")
                .salary("6K-10K")
                .degree("本科及以上")
                .major("法学、知识产权、理工科")
                .description("为企业提供知识产权咨询和申请服务；参与知识产权纠纷调解。")
                .requirements("1. 2026届本科及以上学历；2. 了解专利法和商标法；3. 通过专利代理师资格考试优先。")
                .source("福建省毕业生就业创业公共服务网")
                .build());

        // ===== 媒体/文化行业 =====
        jobRepository.save(Job.builder()
                .title("新媒体运营编辑")
                .company("福建省广播影视集团")
                .industry("媒体/文化")
                .location("福州")
                .salary("5K-8K")
                .degree("本科及以上")
                .major("新闻传播、中文、编导")
                .description("负责新媒体平台内容策划和编辑；追踪热点话题，提升内容传播效果。")
                .requirements("1. 2026届本科及以上学历；2. 有较强的文字功底和内容策划能力；3. 熟悉主流社交媒体平台运营。")
                .source("国家24365大学生就业服务平台")
                .build());

        jobRepository.save(Job.builder()
                .title("数字媒体设计师")
                .company("福建网龙·天晴数码")
                .industry("媒体/文化")
                .location("福州")
                .salary("7K-12K")
                .degree("本科及以上")
                .major("数字媒体艺术、视觉传达、动画")
                .description("负责游戏UI界面设计和交互设计；参与游戏视觉风格制定。")
                .requirements("1. 2026届本科及以上学历；2. 熟练Figma/Sketch/PS等设计工具；3. 有游戏UI设计作品集优先。")
                .source("福建人才联合网")
                .build());

        // ===== 新增：互联网/IT 补充 =====
        jobRepository.save(Job.builder()
                .title("算法工程师（推荐系统方向）")
                .company("美团·福州研发中心")
                .industry("互联网/IT")
                .location("福州")
                .salary("15K-25K")
                .degree("硕士及以上")
                .major("计算机、数学、统计")
                .description("负责推荐系统算法设计与优化；参与大规模机器学习平台建设。")
                .requirements("1. 2026届硕士及以上学历；2. 扎实的机器学习/深度学习基础；3. 熟练Python、TensorFlow/PyTorch；4. 有推荐系统相关论文或项目经验优先。")
                .source("国家24365大学生就业服务平台")
                .build());

        jobRepository.save(Job.builder()
                .title("安全工程师（校招）")
                .company("奇安信·福建分公司")
                .industry("互联网/IT")
                .location("福州")
                .salary("9K-16K")
                .degree("本科及以上")
                .major("信息安全、网络空间安全、计算机")
                .description("负责公司安全产品的测试与部署；参与安全事件应急响应。")
                .requirements("1. 2026届本科及以上学历；2. 了解Web安全、渗透测试；3. 有CTF竞赛经验优先。")
                .source("福建人才联合网")
                .build());

        jobRepository.save(Job.builder()
                .title("DevOps工程师")
                .company("福州字节跳动·飞书研发中心")
                .industry("互联网/IT")
                .location("福州")
                .salary("10K-18K")
                .degree("本科及以上")
                .major("计算机、软件工程")
                .description("负责CI/CD流水线维护和优化；参与容器化平台建设。")
                .requirements("1. 2026届本科及以上学历；2. 熟悉Docker/Kubernetes；3. 了解Jenkins/GitLab CI；4. 有Linux系统管理经验优先。")
                .source("福建省毕业生就业创业公共服务网")
                .build());

        // ===== 新增：制造业补充 =====
        jobRepository.save(Job.builder()
                .title("FPGA开发工程师")
                .company("福州瑞芯微电子")
                .industry("制造/硬件")
                .location("福州")
                .salary("10K-18K")
                .degree("本科及以上")
                .major("电子信息、微电子、通信")
                .description("负责FPGA逻辑设计与验证；参与芯片原型验证平台开发。")
                .requirements("1. 2026届本科及以上学历；2. 熟悉Verilog/VHDL；3. 了解FPGA开发流程；4. 有FPGA竞赛或项目经验优先。")
                .source("福建人才联合网·芯光大道")
                .build());

        jobRepository.save(Job.builder()
                .title("结构设计工程师")
                .company("宁德时代·福建研发中心")
                .industry("制造/硬件")
                .location("宁德")
                .salary("10K-18K")
                .degree("本科及以上")
                .major("机械设计、车辆工程")
                .description("负责电池包结构设计与仿真分析；参与产品试制和测试验证。")
                .requirements("1. 2026届本科及以上学历；2. 熟练SolidWorks/CATIA等三维设计软件；3. 了解有限元分析方法。")
                .source("福建省毕业生就业创业公共服务网")
                .build());

        jobRepository.save(Job.builder()
                .title("自动化设备工程师")
                .company("福建雪人股份有限公司")
                .industry("制造/硬件")
                .location("福州")
                .salary("6K-10K")
                .degree("本科及以上")
                .major("自动化、机械电子")
                .description("负责自动化产线设备的维护和调试；参与设备技术改造项目。")
                .requirements("1. 2026届本科及以上学历；2. 了解PLC编程和工业机器人操作；3. 有自动化设备实习经验优先。")
                .source("福州人才市场")
                .build());

        // ===== 新增：金融补充 =====
        jobRepository.save(Job.builder()
                .title("审计助理")
                .company("致同会计师事务所·福建分所")
                .industry("金融")
                .location("福州")
                .salary("5K-8K")
                .degree("本科及以上")
                .major("会计学、审计学、财务管理")
                .description("参与年度审计项目，完成审计底稿编制；协助项目经理完成现场审计工作。")
                .requirements("1. 2026届本科及以上学历；2. 通过CPA考试部分科目优先；3. 有会计师事务所实习经验优先。")
                .source("福建人才联合网")
                .build());

        // ===== 新增：教育补充 =====
        jobRepository.save(Job.builder()
                .title("IT培训讲师（管培生）")
                .company("中软国际·福建分公司")
                .industry("教育")
                .location("福州")
                .salary("7K-12K")
                .degree("本科及以上")
                .major("计算机相关专业")
                .description("参与IT培训课程研发与授课；负责学员技术指导和就业辅导。")
                .requirements("1. 2026届本科及以上学历；2. 具备扎实的编程基础（Java/Python）；3. 表达能力强，有教学或演讲经验优先。")
                .source("福建省毕业生就业创业公共服务网")
                .build());

        // ===== 新增：厦门地区岗位 =====
        jobRepository.save(Job.builder()
                .title("游戏开发工程师（Unity方向）")
                .company("厦门吉比特网络技术股份有限公司")
                .industry("互联网/IT")
                .location("厦门")
                .salary("10K-18K")
                .degree("本科及以上")
                .major("计算机、软件工程、数字媒体")
                .description("负责Unity3D游戏客户端功能开发；参与游戏性能优化和工具链建设。")
                .requirements("1. 2026届本科及以上学历；2. 熟练C#和Unity3D引擎；3. 有游戏开发作品或竞赛获奖经验优先。")
                .source("福建人才联合网·厦门专区")
                .build());

        jobRepository.save(Job.builder()
                .title("半导体工艺工程师")
                .company("厦门三安光电有限公司")
                .industry("制造/硬件")
                .location("厦门")
                .salary("8K-14K")
                .degree("本科及以上")
                .major("微电子、材料科学、物理")
                .description("负责半导体芯片工艺开发和良率提升；参与新工艺验证和导入。")
                .requirements("1. 2026届本科及以上学历；2. 了解半导体制造工艺流程；3. 有超净间工作经验优先。")
                .source("福建省毕业生就业创业公共服务网")
                .build());

        jobRepository.save(Job.builder()
                .title("跨境电商运营专员")
                .company("厦门亿联网络技术股份有限公司")
                .industry("零售/电商")
                .location("厦门")
                .salary("6K-10K")
                .degree("本科及以上")
                .major("电子商务、国际贸易、英语")
                .description("负责Amazon/Aliexpress等跨境电商平台运营；分析海外市场数据，优化产品排名。")
                .requirements("1. 2026届本科及以上学历；2. 英语CET-6及以上；3. 了解跨境电商平台运营规则；4. 有跨境电商实习经验优先。")
                .source("福建人才联合网·厦门专区")
                .build());

        // ===== 新增：泉州地区岗位 =====
        jobRepository.save(Job.builder()
                .title("纺织材料研发工程师")
                .company("泉州匹克体育用品有限公司")
                .industry("制造/硬件")
                .location("泉州")
                .salary("7K-12K")
                .degree("本科及以上")
                .major("材料科学、高分子材料、纺织工程")
                .description("负责新型运动鞋材的研发与测试；参与材料性能优化和成本控制。")
                .requirements("1. 2026届本科及以上学历；2. 了解高分子材料性能测试方法；3. 有材料实验室研究经验优先。")
                .source("福建省毕业生就业创业公共服务网")
                .build());

        // ===== 新增：综合岗位 =====
        jobRepository.save(Job.builder()
                .title("人力资源管培生")
                .company("福建省投资开发集团")
                .industry("咨询/专业服务")
                .location("福州")
                .salary("6K-10K")
                .degree("本科及以上")
                .major("人力资源管理、工商管理")
                .description("参与招聘、培训、绩效等HR各模块轮岗；协助人力资源体系优化。")
                .requirements("1. 2026届本科及以上学历；2. 具备良好的沟通协调能力；3. 有人力资源实习经验优先。")
                .source("国聘网·福建专区")
                .build());

        jobRepository.save(Job.builder()
                .title("法务助理")
                .company("福建建达律师事务所")
                .industry("咨询/专业服务")
                .location("福州")
                .salary("5K-8K")
                .degree("本科及以上")
                .major("法学")
                .description("协助律师处理诉讼和非诉案件；整理法律文书和案卷材料。")
                .requirements("1. 2026届本科及以上学历；2. 通过法律职业资格考试优先；3. 有律师事务所实习经验优先。")
                .source("福建人才联合网")
                .build());

        jobRepository.save(Job.builder()
                .title("城市规划设计助理")
                .company("福建省城乡规划设计研究院")
                .industry("房地产/建筑")
                .location("福州")
                .salary("6K-10K")
                .degree("本科及以上")
                .major("城乡规划、建筑学、地理信息")
                .description("参与城市规划和设计项目；使用GIS和CAD等工具进行图纸绘制和数据分析。")
                .requirements("1. 2026届本科及以上学历；2. 熟练AutoCAD、ArcGIS、SketchUp；3. 有规划设计项目经验优先。")
                .source("福建省毕业生就业创业公共服务网")
                .build());

        jobRepository.save(Job.builder()
                .title("环保技术工程师")
                .company("龙净环保股份有限公司")
                .industry("制造/硬件")
                .location("龙岩")
                .salary("6K-10K")
                .degree("本科及以上")
                .major("环境工程、化学工程")
                .description("参与环保工程项目方案设计；负责现场技术支持和调试。")
                .requirements("1. 2026届本科及以上学历；2. 了解环保法规和排放标准；3. 有环保工程实习经验优先。")
                .source("福建人才联合网")
                .build());

        jobRepository.save(Job.builder()
                .title("物流运营管培生")
                .company("福建盛辉物流集团")
                .industry("零售/电商")
                .location("福州")
                .salary("5K-8K")
                .degree("本科及以上")
                .major("物流管理、供应链管理")
                .description("参与物流运营各环节轮岗培训；协助优化物流配送网络。")
                .requirements("1. 2026届本科及以上学历；2. 了解物流管理信息系统；3. 有物流企业实习经验优先。")
                .source("福州人才市场")
                .build());

        long count = jobRepository.count();
        log.info("✅ 校招职位数据初始化完成: {} 条", count);
    }
}