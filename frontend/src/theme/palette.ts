/**
 * 芒得很职 设计令牌（Design Tokens）
 * 统一 light/dark 主题下的颜色、间距、字号，供 Vue 组件与 canvas/SVG 复用。
 * 约定（中国习惯）：涨=红、跌=绿；主品牌色沿用平台主题。
 */
export const palette = {
  // 品牌
  brand: '#2F54EB',
  brandDark: '#1F3AA8',
  brandLight: '#E7ECFF',

  // 语义（金融/股票：涨红跌绿）
  up: '#E83828',     // 涨 / 提升 → 红
  down: '#18A058',   // 跌 / 下降 → 绿
  warn: '#F5A623',
  danger: '#E83828',

  // 中性
  text: '#1F2329',
  textMuted: '#8A9099',
  border: '#E5E6EB',
  surface: '#FFFFFF',
  surfaceAlt: '#F7F8FA',
  bg: '#F2F3F5',

  // 维度评分色板
  cPurple: '#7C5CFC',
  cTeal: '#13C2C2',
  cOrange: '#FA8C16',
  cBlue: '#2F54EB',
  cGreen: '#18A058',
} as const;

export type Palette = typeof palette;
