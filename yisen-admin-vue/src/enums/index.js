/**
 * 任务类型枚举
 */
export const TaskType = {
  DAILY: 'daily',
  WEEKLY: 'weekly',
  MONTHLY: 'monthly',
  PROJECT: 'project',
};

/**
 * 用户角色枚举
 */
export const UserRole = {
  SUPER_ADMIN: 'super_admin',
  ADMIN: 'admin',
  USER: 'user',
  GUEST: 'guest',
};

/**
 * 操作类型枚举
 */
export const ActionType = {
  CREATE: 'create',
  UPDATE: 'update',
  DELETE: 'delete',
  VIEW: 'view',
};

export default {
  TaskType,
  UserRole,
  ActionType,
};
