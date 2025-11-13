/**
 * 权限标识常量
 */

/**
 * 用户权限
 */
export const USER_PERMISSIONS = {
  VIEW: "user:view",
  CREATE: "user:create",
  UPDATE: "user:update",
  DELETE: "user:delete",
  EXPORT: "user:export",
};

/**
 * 任务权限
 */
export const TASK_PERMISSIONS = {
  VIEW: "task:view",
  CREATE: "task:create",
  UPDATE: "task:update",
  DELETE: "task:delete",
  ASSIGN: "task:assign",
  EXPORT: "task:export",
};

/**
 * 角色权限
 */
export const ROLE_PERMISSIONS = {
  VIEW: "role:view",
  CREATE: "role:create",
  UPDATE: "role:update",
  DELETE: "role:delete",
  ASSIGN: "role:assign",
};

/**
 * 系统权限
 */
export const SYSTEM_PERMISSIONS = {
  SETTINGS: "system:settings",
  LOGS: "system:logs",
  MONITOR: "system:monitor",
};

/**
 * 角色类型
 */
export const ROLES = {
  SUPER_ADMIN: "super_admin",
  ADMIN: "admin",
  USER: "user",
  GUEST: "guest",
};

/**
 * 角色名称映射
 */
export const ROLE_NAMES = {
  [ROLES.SUPER_ADMIN]: "超级管理员",
  [ROLES.ADMIN]: "管理员",
  [ROLES.USER]: "普通用户",
  [ROLES.GUEST]: "访客",
};

/**
 * 所有权限列表
 */
export const ALL_PERMISSIONS = {
  ...USER_PERMISSIONS,
  ...TASK_PERMISSIONS,
  ...ROLE_PERMISSIONS,
  ...SYSTEM_PERMISSIONS,
};
