import type { QuestionGroup } from '@/types'

/**
 * 测评题库 — 按维度分组
 * 每个维度下有若干道选择题，每道题各选项对应不同分数
 */
export const questionBank: QuestionGroup[] = [
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
    ],
  },
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
    ],
  },
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