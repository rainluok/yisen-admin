/**
 * 任务状态常量
 */
export const TASK_STATUS = {
  PENDING: 0, // 待处理
  IN_PROGRESS: 1, // 进行中
  DONE: 2, // 已完成
  CANCELLED: 3, // 已取消
};

/**
 * 任务状态文本映射
 */
export const TASK_STATUS_TEXT = {
  [TASK_STATUS.PENDING]: "待处理",
  [TASK_STATUS.IN_PROGRESS]: "进行中",
  [TASK_STATUS.DONE]: "已完成",
  [TASK_STATUS.CANCELLED]: "已取消",
};

/**
 * 任务状态颜色映射
 */
export const TASK_STATUS_COLOR = {
  [TASK_STATUS.PENDING]: "#909399",
  [TASK_STATUS.IN_PROGRESS]: "#409EFF",
  [TASK_STATUS.DONE]: "#67C23A",
  [TASK_STATUS.CANCELLED]: "#F56C6C",
};

/**
 * 任务优先级
 */
export const TASK_PRIORITY = {
  LOW: 0, // 低
  NORMAL: 1, // 普通
  HIGH: 2, // 高
  URGENT: 3, // 紧急
};

/**
 * 任务优先级文本映射
 */
export const TASK_PRIORITY_TEXT = {
  [TASK_PRIORITY.LOW]: "低",
  [TASK_PRIORITY.NORMAL]: "普通",
  [TASK_PRIORITY.HIGH]: "高",
  [TASK_PRIORITY.URGENT]: "紧急",
};

/**
 * 用户状态
 */
export const USER_STATUS = {
  INACTIVE: 0, // 未激活
  ACTIVE: 1, // 已激活
  DISABLED: 2, // 已禁用
};

/**
 * 用户状态文本映射
 */
export const USER_STATUS_TEXT = {
  [USER_STATUS.INACTIVE]: "未激活",
  [USER_STATUS.ACTIVE]: "已激活",
  [USER_STATUS.DISABLED]: "已禁用",
};

/**
 * 请求状态码
 */
export const HTTP_STATUS = {
  SUCCESS: 200,
  CREATED: 201,
  NO_CONTENT: 204,
  BAD_REQUEST: 400,
  UNAUTHORIZED: 401,
  FORBIDDEN: 403,
  NOT_FOUND: 404,
  SERVER_ERROR: 500,
};
