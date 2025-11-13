/**
 * 认证相关工具函数
 */

const TOKEN_KEY = "yisen_admin_token";
const USER_INFO_KEY = "yisen_admin_user_info";
const PERMISSIONS_KEY = "yisen_admin_permissions";

/**
 * 获取 Token
 */
export function getToken() {
  return localStorage.getItem(TOKEN_KEY);
}

/**
 * 设置 Token
 * @param {string} token
 */
export function setToken(token) {
  return localStorage.setItem(TOKEN_KEY, token);
}

/**
 * 移除 Token
 */
export function removeToken() {
  return localStorage.removeItem(TOKEN_KEY);
}

/**
 * 获取用户信息
 */
export function getUserInfo() {
  const userInfo = localStorage.getItem(USER_INFO_KEY);
  return userInfo ? JSON.parse(userInfo) : null;
}

/**
 * 设置用户信息
 * @param {Object} userInfo
 */
export function setUserInfo(userInfo) {
  return localStorage.setItem(USER_INFO_KEY, JSON.stringify(userInfo));
}

/**
 * 移除用户信息
 */
export function removeUserInfo() {
  return localStorage.removeItem(USER_INFO_KEY);
}

/**
 * 获取用户权限
 */
export function getPermissions() {
  const permissions = localStorage.getItem(PERMISSIONS_KEY);
  return permissions ? JSON.parse(permissions) : [];
}

/**
 * 设置用户权限
 * @param {Array} permissions
 */
export function setPermissions(permissions) {
  return localStorage.setItem(PERMISSIONS_KEY, JSON.stringify(permissions));
}

/**
 * 移除用户权限
 */
export function removePermissions() {
  return localStorage.removeItem(PERMISSIONS_KEY);
}

/**
 * 清除所有认证信息
 */
export function clearAuth() {
  removeToken();
  removeUserInfo();
  removePermissions();
}

/**
 * 检查是否有权限
 * @param {string|Array} permission - 权限标识
 */
export function hasPermission(permission) {
  const permissions = getPermissions();
  if (!permissions || permissions.length === 0) {
    return false;
  }

  if (typeof permission === "string") {
    return permissions.includes(permission);
  }

  if (Array.isArray(permission)) {
    return permission.some((p) => permissions.includes(p));
  }

  return false;
}

/**
 * 检查是否有所有权限
 * @param {Array} permissions - 权限标识数组
 */
export function hasAllPermissions(permissions) {
  const userPermissions = getPermissions();
  if (!userPermissions || userPermissions.length === 0) {
    return false;
  }

  return permissions.every((p) => userPermissions.includes(p));
}
