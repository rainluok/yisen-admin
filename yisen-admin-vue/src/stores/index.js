/**
 * Store 统一导出文件
 */

// 应用状态
export { useAppStore } from './app';

// 用户状态
export { useUserStore } from './system/user.js';

// 角色状态
export { useRoleStore } from './system/role.js';

// 菜单状态
export { useMenuStore } from './system/menu.js';

// 部门状态
export { useDepartStore } from './system/depart.js';

// 字典状态
export { useDictStore } from './system/dict.js';

// 日志状态
export { useLogStore } from './system/log.js';

// 标签页状态
export { useTagsViewStore } from './tagsView';

// 权限路由状态
export { usePermissionStore } from './permission';
