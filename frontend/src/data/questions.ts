import type { QuestionGroup } from '@/types'

/**
 * 测评题库 — 5个维度，共55道题
 * 每个维度10-12道选择题，覆盖不同场景
 */
export const questionBank: QuestionGroup[] = [
  // ===== 1. 沟通能力 (11题) =====
  {
    dimension: 'communication',
    label: '沟通能力',
    icon: '💬',
    questions: [
      {
        id: 'comm-1',
        dimension: 'communication',
        text: '在团队讨论中，你的同事提出了一个与你不同的观点，你会？',
        type: 'single',
        options: [
          { value: 4, label: '认真倾听，分析他观点的合理性，再回应', score: 90 },
          { value: 3, label: '礼貌地听完，但坚持自己的想法', score: 65 },
          { value: 2, label: '直接反驳，指出他错在哪里', score: 35 },
          { value: 1, label: '不回应，心里觉得他不对', score: 15 },
        ],
      },
      {
        id: 'comm-2',
        dimension: 'communication',
        text: '你需要向非技术背景的领导汇报技术方案，你会？',
        type: 'single',
        options: [
          { value: 4, label: '用比喻和类比，避免技术术语，聚焦业务价值', score: 90 },
          { value: 3, label: '准备详细的PPT，列出技术要点', score: 65 },
          { value: 2, label: '直接展示技术架构图和数据', score: 35 },
          { value: 1, label: '口头简单说一下思路', score: 15 },
        ],
      },
      {
        id: 'comm-3',
        dimension: 'communication',
        text: '你发现项目进度可能延期，会怎么做？',
        type: 'single',
        options: [
          { value: 4, label: '提前预警，同步风险和应对方案', score: 95 },
          { value: 3, label: '自己加班赶工，希望能赶上', score: 55 },
          { value: 2, label: '等到了截止日期再说', score: 20 },
          { value: 1, label: '隐瞒情况，不告诉任何人', score: 5 },
        ],
      },
      {
        id: 'comm-4',
        dimension: 'communication',
        text: '收到一封措辞严厉的批评邮件，你的第一反应是？',
        type: 'single',
        options: [
          { value: 4, label: '冷静分析批评内容，约对方当面沟通', score: 90 },
          { value: 3, label: '回复邮件解释自己的立场', score: 60 },
          { value: 2, label: '感到委屈，暂时不回复', score: 30 },
          { value: 1, label: '同样严厉地回复回去', score: 10 },
        ],
      },
      {
        id: 'comm-5',
        dimension: 'communication',
        text: '跨部门协作时，对方部门不配合，你会？',
        type: 'single',
        options: [
          { value: 4, label: '主动了解对方困难，寻找共同利益点', score: 90 },
          { value: 3, label: '通过上级协调推动', score: 65 },
          { value: 2, label: '反复发邮件催促', score: 30 },
          { value: 1, label: '绕过对方自己做', score: 15 },
        ],
      },
      {
        id: 'comm-6',
        dimension: 'communication',
        text: '会议中你发现了方案的重大漏洞，但方案是领导提出的，你会？',
        type: 'single',
        options: [
          { value: 4, label: '用数据和事实委婉地指出风险，提出替代方案', score: 90 },
          { value: 3, label: '会后私下找领导沟通', score: 70 },
          { value: 2, label: '不做声，按方案执行', score: 25 },
          { value: 1, label: '当场直接否定方案', score: 15 },
        ],
      },
      {
        id: 'comm-7',
        dimension: 'communication',
        text: '你如何向客户解释一个复杂的产品功能？',
        type: 'single',
        options: [
          { value: 4, label: '从客户痛点出发，用生活化类比解释', score: 90 },
          { value: 3, label: '准备详细的说明书和演示视频', score: 65 },
          { value: 2, label: '直接展示功能列表和技术参数', score: 35 },
          { value: 1, label: '让客户自己试用', score: 15 },
        ],
      },
      {
        id: 'comm-8',
        dimension: 'communication',
        text: '团队中有成员在会议上沉默寡言，但有好的想法，你会？',
        type: 'single',
        options: [
          { value: 4, label: '单独邀请他分享，创造安全的表达环境', score: 90 },
          { value: 3, label: '在会议上点名让他发言', score: 55 },
          { value: 2, label: '不管他，愿意说自然会说', score: 25 },
          { value: 1, label: '认为他不够积极', score: 10 },
        ],
      },
      {
        id: 'comm-9',
        dimension: 'communication',
        text: '你需要在短时间内说服团队接受一个新的工作流程，你会？',
        type: 'single',
        options: [
          { value: 4, label: '先试点展示效果，用数据说服大家', score: 90 },
          { value: 3, label: '开大会宣讲新流程的好处', score: 60 },
          { value: 2, label: '直接发布新流程要求执行', score: 25 },
          { value: 1, label: '等着别人先提出来', score: 10 },
        ],
      },
      {
        id: 'comm-10',
        dimension: 'communication',
        text: '你收到模糊不清的任务需求，会怎么做？',
        type: 'single',
        options: [
          { value: 4, label: '主动追问确认，输出需求文档请对方确认', score: 90 },
          { value: 3, label: '按自己的理解先做一部分', score: 50 },
          { value: 2, label: '等着需求明确了再开始', score: 25 },
          { value: 1, label: '抱怨需求不清晰但不动手', score: 10 },
        ],
      },
      {
        id: 'comm-11',
        dimension: 'communication',
        text: '公开演讲时你更倾向于哪种方式？',
        type: 'single',
        options: [
          { value: 4, label: '准备故事线，用案例和互动吸引听众', score: 90 },
          { value: 3, label: '准备详细的PPT，按页讲解', score: 65 },
          { value: 2, label: '即兴发挥，凭感觉讲', score: 30 },
          { value: 1, label: '尽量避免公开演讲', score: 10 },
        ],
      },
    ],
  },

  // ===== 2. 协作能力 (11题) =====
  {
    dimension: 'collaboration',
    label: '协作能力',
    icon: '🤝',
    questions: [
      {
        id: 'collab-1',
        dimension: 'collaboration',
        text: '团队中有人拖慢了整体进度，你会？',
        type: 'single',
        options: [
          { value: 4, label: '主动询问是否需要帮助，共同解决问题', score: 90 },
          { value: 3, label: '向组长反映情况，让组长协调', score: 65 },
          { value: 2, label: '自己多做一点，弥补他的部分', score: 40 },
          { value: 1, label: '抱怨几句但不管', score: 10 },
        ],
      },
      {
        id: 'collab-2',
        dimension: 'collaboration',
        text: '分配任务时，你更倾向于？',
        type: 'single',
        options: [
          { value: 4, label: '根据每个人的特长合理分配，明确分工', score: 90 },
          { value: 3, label: '平均分配，每人负责一部分', score: 60 },
          { value: 2, label: '谁有空谁做', score: 30 },
          { value: 1, label: '自己全包了', score: 15 },
        ],
      },
      {
        id: 'collab-3',
        dimension: 'collaboration',
        text: '与同事发生意见分歧时，你通常？',
        type: 'single',
        options: [
          { value: 4, label: '先找共识，再讨论分歧，寻求双赢方案', score: 95 },
          { value: 3, label: '各自陈述理由，投票决定', score: 65 },
          { value: 2, label: '听职位高的人的意见', score: 30 },
          { value: 1, label: '坚持己见，不轻易让步', score: 15 },
        ],
      },
      {
        id: 'collab-4',
        dimension: 'collaboration',
        text: '你如何评价自己在团队中的角色？',
        type: 'single',
        options: [
          { value: 4, label: '主动协调者，促进团队协作', score: 90 },
          { value: 3, label: '可靠执行者，按时完成任务', score: 65 },
          { value: 2, label: '独立思考者，专注自己的部分', score: 35 },
          { value: 1, label: '跟随者，听安排做事', score: 15 },
        ],
      },
      {
        id: 'collab-5',
        dimension: 'collaboration',
        text: '新成员加入团队，你会？',
        type: 'single',
        options: [
          { value: 4, label: '主动介绍团队情况，帮助他快速融入', score: 90 },
          { value: 3, label: '等他来问问题时再帮助', score: 55 },
          { value: 2, label: '各做各的，互不干扰', score: 25 },
          { value: 1, label: '觉得他可能会拖累团队', score: 5 },
        ],
      },
      {
        id: 'collab-6',
        dimension: 'collaboration',
        text: '团队目标与个人目标冲突时，你会？',
        type: 'single',
        options: [
          { value: 4, label: '优先团队目标，同时寻找个人成长的结合点', score: 90 },
          { value: 3, label: '与团队协商调整分工', score: 70 },
          { value: 2, label: '先完成个人目标再看团队', score: 30 },
          { value: 1, label: '只关注自己的任务', score: 10 },
        ],
      },
      {
        id: 'collab-7',
        dimension: 'collaboration',
        text: '你发现同事的工作方法效率很低，你会？',
        type: 'single',
        options: [
          { value: 4, label: '分享自己的高效方法，并询问是否需要帮助', score: 90 },
          { value: 3, label: '在团队分享会上提出讨论', score: 65 },
          { value: 2, label: '不管他，做好自己的事', score: 25 },
          { value: 1, label: '嘲笑他的效率', score: 5 },
        ],
      },
      {
        id: 'collab-8',
        dimension: 'collaboration',
        text: '项目需要跨团队协作，但对方团队优先级不同，你会？',
        type: 'single',
        options: [
          { value: 4, label: '主动对齐双方目标，找到互利共赢的合作方式', score: 90 },
          { value: 3, label: '通过双方领导协调优先级', score: 65 },
          { value: 2, label: '等对方有空了再推进', score: 25 },
          { value: 1, label: '绕过对方自己做', score: 10 },
        ],
      },
      {
        id: 'collab-9',
        dimension: 'collaboration',
        text: '团队决策时你不同意多数人的意见，你会？',
        type: 'single',
        options: [
          { value: 4, label: '表达自己的担忧，但执行团队决定', score: 85 },
          { value: 3, label: '保留意见，勉强执行', score: 55 },
          { value: 2, label: '消极执行，证明自己是对的', score: 20 },
          { value: 1, label: '拒绝执行，坚持己见', score: 5 },
        ],
      },
      {
        id: 'collab-10',
        dimension: 'collaboration',
        text: '你更倾向于哪种工作方式？',
        type: 'single',
        options: [
          { value: 4, label: '与团队紧密协作，实时沟通', score: 90 },
          { value: 3, label: '定期同步，大部分时间独立工作', score: 60 },
          { value: 2, label: '完全独立工作，只交结果', score: 25 },
          { value: 1, label: '不喜欢团队合作', score: 5 },
        ],
      },
      {
        id: 'collab-11',
        dimension: 'collaboration',
        text: '团队取得成就时，你如何反应？',
        type: 'single',
        options: [
          { value: 4, label: '肯定每个成员的贡献，庆祝团队成功', score: 90 },
          { value: 3, label: '为自己参与其中感到高兴', score: 65 },
          { value: 2, label: '觉得是自己功劳最大', score: 25 },
          { value: 1, label: '无所谓，做完了就行', score: 10 },
        ],
      },
    ],
  },

  // ===== 3. 问题解决能力 (11题) =====
  {
    dimension: 'problem_solving',
    label: '问题解决能力',
    icon: '🧩',
    questions: [
      {
        id: 'prob-1',
        dimension: 'problem_solving',
        text: '遇到一个从未见过的技术难题，你会？',
        type: 'single',
        options: [
          { value: 4, label: '查阅资料、拆解问题、分步验证解决', score: 95 },
          { value: 3, label: '问有经验的同事怎么处理', score: 60 },
          { value: 2, label: '尝试几种常见方法，不行就放弃', score: 30 },
          { value: 1, label: '直接找领导换人来做', score: 10 },
        ],
      },
      {
        id: 'prob-2',
        dimension: 'problem_solving',
        text: '项目上线前发现一个严重BUG，距离截止还有2小时，你会？',
        type: 'single',
        options: [
          { value: 4, label: '评估影响范围，紧急修复并准备回滚方案', score: 95 },
          { value: 3, label: '立即修复，争取按时上线', score: 60 },
          { value: 2, label: '申请延期上线', score: 30 },
          { value: 1, label: '先上线再说，后面再修', score: 5 },
        ],
      },
      {
        id: 'prob-3',
        dimension: 'problem_solving',
        text: '面对一个复杂问题，你通常如何分析？',
        type: 'single',
        options: [
          { value: 4, label: '先定义问题边界，再拆解为子问题逐一解决', score: 90 },
          { value: 3, label: '凭经验直接尝试解决方案', score: 55 },
          { value: 2, label: '搜索类似案例，照搬方案', score: 30 },
          { value: 1, label: '等问题变得更明确再处理', score: 10 },
        ],
      },
      {
        id: 'prob-4',
        dimension: 'problem_solving',
        text: '你提出的方案被否决了，你会？',
        type: 'single',
        options: [
          { value: 4, label: '虚心接受反馈，分析不足，改进后再次提报', score: 90 },
          { value: 3, label: '询问否决的具体原因，记下来', score: 65 },
          { value: 2, label: '觉得对方不理解，但不再争取', score: 30 },
          { value: 1, label: '很失落，以后不再提建议', score: 10 },
        ],
      },
      {
        id: 'prob-5',
        dimension: 'problem_solving',
        text: '多个紧急任务同时到达，你会？',
        type: 'single',
        options: [
          { value: 4, label: '按紧急性和重要性排序，逐一处理并同步进度', score: 90 },
          { value: 3, label: '先做最紧急的，其他的往后排', score: 60 },
          { value: 2, label: '随机选一个开始做', score: 25 },
          { value: 1, label: '感到焦虑，不知从何下手', score: 10 },
        ],
      },
      {
        id: 'prob-6',
        dimension: 'problem_solving',
        text: '你尝试了所有已知方法都无法解决问题，你会？',
        type: 'single',
        options: [
          { value: 4, label: '跳出框架，从第一性原理重新思考问题', score: 90 },
          { value: 3, label: '寻求外部专家或社区的帮助', score: 70 },
          { value: 2, label: '暂时放一放，过段时间再试', score: 35 },
          { value: 1, label: '放弃这个问题', score: 5 },
        ],
      },
      {
        id: 'prob-7',
        dimension: 'problem_solving',
        text: '在处理问题时，你会如何记录和复盘？',
        type: 'single',
        options: [
          { value: 4, label: '详细记录问题原因、解决过程和经验教训', score: 90 },
          { value: 3, label: '在心里记住，下次注意', score: 55 },
          { value: 2, label: '解决了就不管了', score: 25 },
          { value: 1, label: '从不记录', score: 5 },
        ],
      },
      {
        id: 'prob-8',
        dimension: 'problem_solving',
        text: '面对一个需要创新方案的问题，你会？',
        type: 'single',
        options: [
          { value: 4, label: '头脑风暴多种方案，快速验证可行性', score: 90 },
          { value: 3, label: '参考行业最佳实践', score: 65 },
          { value: 2, label: '用之前用过的方法', score: 30 },
          { value: 1, label: '等别人想出方案', score: 10 },
        ],
      },
      {
        id: 'prob-9',
        dimension: 'problem_solving',
        text: '你的方案实施后效果不佳，你会？',
        type: 'single',
        options: [
          { value: 4, label: '快速复盘，调整方案，迭代优化', score: 90 },
          { value: 3, label: '查找原因，小范围调整', score: 65 },
          { value: 2, label: '觉得方案没问题，是执行的问题', score: 25 },
          { value: 1, label: '放弃该方案', score: 10 },
        ],
      },
      {
        id: 'prob-10',
        dimension: 'problem_solving',
        text: '你如何判断一个解决方案是否可行？',
        type: 'single',
        options: [
          { value: 4, label: '最小可行产品验证，用数据说话', score: 90 },
          { value: 3, label: '凭借经验和直觉判断', score: 55 },
          { value: 2, label: '参考别人的做法', score: 30 },
          { value: 1, label: '试了才知道', score: 15 },
        ],
      },
      {
        id: 'prob-11',
        dimension: 'problem_solving',
        text: '遇到反复出现的同类问题，你会？',
        type: 'single',
        options: [
          { value: 4, label: '从根本上分析原因，制定长效机制防止再发', score: 90 },
          { value: 3, label: '每次出现时快速处理', score: 50 },
          { value: 2, label: '忍受它，觉得这是常态', score: 20 },
          { value: 1, label: '让别人去处理', score: 5 },
        ],
      },
    ],
  },

  // ===== 4. 领导力 (11题) =====
  {
    dimension: 'leadership',
    label: '领导力',
    icon: '🎯',
    questions: [
      {
        id: 'lead-1',
        dimension: 'leadership',
        text: '当团队缺乏方向时，你会？',
        type: 'single',
        options: [
          { value: 4, label: '主动制定清晰的目标和计划，带领团队前进', score: 90 },
          { value: 3, label: '建议团队一起讨论确定方向', score: 65 },
          { value: 2, label: '等待领导给出明确指示', score: 30 },
          { value: 1, label: '做自己的事，不管方向', score: 10 },
        ],
      },
      {
        id: 'lead-2',
        dimension: 'leadership',
        text: '团队成员犯错时，你会？',
        type: 'single',
        options: [
          { value: 4, label: '私下沟通，帮助分析原因，共同改进', score: 90 },
          { value: 3, label: '指出错误，让他自己改正', score: 60 },
          { value: 2, label: '公开批评，以儆效尤', score: 25 },
          { value: 1, label: '不管，让他自己承担后果', score: 10 },
        ],
      },
      {
        id: 'lead-3',
        dimension: 'leadership',
        text: '你如何激励团队成员？',
        type: 'single',
        options: [
          { value: 4, label: '了解每个人的动机，给予个性化的认可和成长机会', score: 90 },
          { value: 3, label: '设定明确的目标和奖励机制', score: 65 },
          { value: 2, label: '用权威推动执行', score: 25 },
          { value: 1, label: '不需要激励，做好自己的事就行', score: 10 },
        ],
      },
      {
        id: 'lead-4',
        dimension: 'leadership',
        text: '面对重大决策，你如何做？',
        type: 'single',
        options: [
          { value: 4, label: '收集多方信息，权衡利弊，果断决策', score: 90 },
          { value: 3, label: '咨询专家意见后再决定', score: 65 },
          { value: 2, label: '拖延决策，等待更多信息', score: 25 },
          { value: 1, label: '让别人做决定', score: 10 },
        ],
      },
      {
        id: 'lead-5',
        dimension: 'leadership',
        text: '你如何培养团队成员的成长？',
        type: 'single',
        options: [
          { value: 4, label: '定期一对一沟通，制定个人发展计划，提供挑战机会', score: 90 },
          { value: 3, label: '安排培训，让团队成员自学', score: 55 },
          { value: 2, label: '让他们在工作中自己摸索', score: 25 },
          { value: 1, label: '那是他们自己的事', score: 5 },
        ],
      },
      {
        id: 'lead-6',
        dimension: 'leadership',
        text: '团队内部出现冲突，你会？',
        type: 'single',
        options: [
          { value: 4, label: '及时介入，倾听双方，找到公平的解决方案', score: 90 },
          { value: 3, label: '让双方自行沟通解决', score: 50 },
          { value: 2, label: '偏向自己支持的一方', score: 20 },
          { value: 1, label: '忽视冲突，希望它自行消失', score: 5 },
        ],
      },
      {
        id: 'lead-7',
        dimension: 'leadership',
        text: '你如何应对外部变化对团队的影响？',
        type: 'single',
        options: [
          { value: 4, label: '快速调整计划，透明沟通，带领团队适应变化', score: 90 },
          { value: 3, label: '评估影响后再决定是否调整', score: 65 },
          { value: 2, label: '坚持原计划不变', score: 20 },
          { value: 1, label: '感到焦虑，不知所措', score: 10 },
        ],
      },
      {
        id: 'lead-8',
        dimension: 'leadership',
        text: '你如何看待授权？',
        type: 'single',
        options: [
          { value: 4, label: '充分授权，给予信任，同时明确边界和支持', score: 90 },
          { value: 3, label: '授权部分工作，关键事项自己把控', score: 65 },
          { value: 2, label: '不想授权，担心别人做不好', score: 20 },
          { value: 1, label: '所有事情自己亲力亲为', score: 5 },
        ],
      },
      {
        id: 'lead-9',
        dimension: 'leadership',
        text: '团队面临高压和截止日期，你会？',
        type: 'single',
        options: [
          { value: 4, label: '带头冲锋，合理分配资源，关注团队士气', score: 90 },
          { value: 3, label: '督促大家加班赶工', score: 50 },
          { value: 2, label: '把压力转嫁给团队', score: 15 },
          { value: 1, label: '自己也感到压力，无法有效应对', score: 10 },
        ],
      },
      {
        id: 'lead-10',
        dimension: 'leadership',
        text: '你如何为团队设定愿景？',
        type: 'single',
        options: [
          { value: 4, label: '描绘清晰的愿景，让每个人看到自己的价值', score: 90 },
          { value: 3, label: '设定具体的KPI和目标', score: 65 },
          { value: 2, label: '告诉大家做好本职工作', score: 25 },
          { value: 1, label: '不需要愿景，完成任务就行', score: 10 },
        ],
      },
      {
        id: 'lead-11',
        dimension: 'leadership',
        text: '当你不在时，团队的表现如何？',
        type: 'single',
        options: [
          { value: 4, label: '团队能自主运转，高效完成任务', score: 90 },
          { value: 3, label: '部分成员能承担更多责任', score: 65 },
          { value: 2, label: '团队效率明显下降', score: 30 },
          { value: 1, label: '团队几乎停滞', score: 10 },
        ],
      },
    ],
  },

  // ===== 5. 适应力 (11题) =====
  {
    dimension: 'adaptability',
    label: '适应力',
    icon: '🔄',
    questions: [
      {
        id: 'adapt-1',
        dimension: 'adaptability',
        text: '公司突然推行全新的工作流程，你会？',
        type: 'single',
        options: [
          { value: 4, label: '积极学习新流程，思考如何利用它提升效率', score: 90 },
          { value: 3, label: '按要求执行，但保留旧习惯', score: 55 },
          { value: 2, label: '抵触变化，觉得旧流程更好', score: 20 },
          { value: 1, label: '消极应对，不配合', score: 5 },
        ],
      },
      {
        id: 'adapt-2',
        dimension: 'adaptability',
        text: '你被分配到一个完全陌生的领域工作，你会？',
        type: 'single',
        options: [
          { value: 4, label: '兴奋地迎接挑战，快速学习新领域知识', score: 90 },
          { value: 3, label: '接受安排，边做边学', score: 65 },
          { value: 2, label: '感到不安，希望回到熟悉的工作', score: 25 },
          { value: 1, label: '拒绝调动', score: 5 },
        ],
      },
      {
        id: 'adapt-3',
        dimension: 'adaptability',
        text: '你使用的核心工具突然被替换了，你会？',
        type: 'single',
        options: [
          { value: 4, label: '尽快学习新工具，并帮助团队过渡', score: 90 },
          { value: 3, label: '参加培训，逐步适应', score: 65 },
          { value: 2, label: '抱怨新工具不好用', score: 20 },
          { value: 1, label: '继续用旧工具，直到被迫切换', score: 5 },
        ],
      },
      {
        id: 'adapt-4',
        dimension: 'adaptability',
        text: '项目进行到一半，客户要求完全改变方向，你会？',
        type: 'single',
        options: [
          { value: 4, label: '快速调整计划，重新评估资源和时间', score: 90 },
          { value: 3, label: '感到沮丧，但按要求执行', score: 55 },
          { value: 2, label: '试图说服客户保持原计划', score: 30 },
          { value: 1, label: '消极怠工，觉得之前的努力白费了', score: 5 },
        ],
      },
      {
        id: 'adapt-5',
        dimension: 'adaptability',
        text: '你如何保持自己的技能不过时？',
        type: 'single',
        options: [
          { value: 4, label: '持续学习，跟踪行业趋势，定期更新技能', score: 90 },
          { value: 3, label: '参加公司安排的培训', score: 55 },
          { value: 2, label: '等到需要时再学', score: 20 },
          { value: 1, label: '觉得现有的技能就够了', score: 5 },
        ],
      },
      {
        id: 'adapt-6',
        dimension: 'adaptability',
        text: '你的工作计划被突发事件打乱，你会？',
        type: 'single',
        options: [
          { value: 4, label: '快速重新规划优先级，灵活调整日程', score: 90 },
          { value: 3, label: '先处理突发事件，再恢复原计划', score: 65 },
          { value: 2, label: '感到焦虑，不知所措', score: 25 },
          { value: 1, label: '抱怨计划被打乱', score: 10 },
        ],
      },
      {
        id: 'adapt-7',
        dimension: 'adaptability',
        text: '你如何看待失败？',
        type: 'single',
        options: [
          { value: 4, label: '失败是学习的机会，分析原因，下次改进', score: 90 },
          { value: 3, label: '感到失望，但会继续前进', score: 60 },
          { value: 2, label: '尽量避免失败，失败了就归咎于外部因素', score: 20 },
          { value: 1, label: '无法接受失败，影响自信心', score: 5 },
        ],
      },
      {
        id: 'adapt-8',
        dimension: 'adaptability',
        text: '工作环境从线下转为远程办公，你会？',
        type: 'single',
        options: [
          { value: 4, label: '主动学习远程协作工具，建立新的工作习惯', score: 90 },
          { value: 3, label: '能适应，但还是更喜欢线下', score: 65 },
          { value: 2, label: '觉得远程效率很低', score: 25 },
          { value: 1, label: '无法适应远程工作', score: 5 },
        ],
      },
      {
        id: 'adapt-9',
        dimension: 'adaptability',
        text: '你如何应对工作中的不确定性？',
        type: 'single',
        options: [
          { value: 4, label: '接受不确定性，制定灵活的应对策略', score: 90 },
          { value: 3, label: '尽量收集信息来降低不确定性', score: 65 },
          { value: 2, label: '对不确定性感到焦虑', score: 25 },
          { value: 1, label: '需要完全明确的计划才能行动', score: 10 },
        ],
      },
      {
        id: 'adapt-10',
        dimension: 'adaptability',
        text: '你同时接到多项不同领域的学习任务，你会？',
        type: 'single',
        options: [
          { value: 4, label: '制定学习计划，分阶段高效学习', score: 90 },
          { value: 3, label: '一次学一个，学完再学下一个', score: 55 },
          { value: 2, label: '感到压力大，不知从何开始', score: 20 },
          { value: 1, label: '拖延学习', score: 5 },
        ],
      },
      {
        id: 'adapt-11',
        dimension: 'adaptability',
        text: '你更倾向于哪种工作节奏？',
        type: 'single',
        options: [
          { value: 4, label: '灵活适应不同类型的工作节奏', score: 90 },
          { value: 3, label: '喜欢稳定的、可预测的节奏', score: 55 },
          { value: 2, label: '只能在高压下高效工作', score: 25 },
          { value: 1, label: '需要固定的工作流程', score: 10 },
        ],
      },
    ],
  },
]

/** 根据选项计算维度得分（0-100） */
export function calculateDimensionScore(
  questions: QuestionGroup['questions'],
  answers: Record<string, number>,
): number {
  let total = 0
  let count = 0
  for (const q of questions) {
    const selected = answers[q.id]
    if (selected !== undefined) {
      const option = q.options.find(o => o.value === selected)
      if (option) {
        total += option.score
        count++
      }
    }
  }
  return count > 0 ? Math.round(total / count) : 0
}

/** 计算所有维度得分 */
export function calculateAllScores(
  groups: QuestionGroup[],
  answers: Record<string, number>,
): Record<string, number> {
  const scores: Record<string, number> = {}
  for (const group of groups) {
    scores[group.dimension] = calculateDimensionScore(group.questions, answers)
  }
  return scores
}

/** 计算总分 */
export function calculateTotalScore(dimensionScores: Record<string, number>): number {
  return Object.values(dimensionScores).reduce((sum, s) => sum + s, 0)
}