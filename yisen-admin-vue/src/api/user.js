import request from '@/utils/request';

/**
 * 用户登录
 * @param {Object} data - 登录数据
 * @param {string} data.username - 用户名
 * @param {string} data.password - 密码
 */
export function login(data) {
  return request({
    url: '/login',
    method: 'post',
    data,
  });
}

/**
 * 获取当前登录用户信息
 */
export function getUserInfo() {
  return request({
    url: '/userInfo',
    method: 'get',
  });
}

/**
 * 用户登出
 */
export function logout() {
  return request({
    url: '/logout',
    method: 'post',
  });
}

/**
 * 修改用户信息
 * @param {Object} data - 用户信息
 */
export function updateUserInfo(data) {
  return request({
    url: '/user/update',
    method: 'put',
    data,
  });
}

/**
 * 修改密码
 * @param {Object} data - 密码数据
 * @param {string} data.oldPassword - 旧密码
 * @param {string} data.newPassword - 新密码
 */
export function updatePassword(data) {
  return request({
    url: '/user/password',
    method: 'put',
    data,
  });
}

/**
 * 分页查询用户列表
 * @param {Object} data - 分页查询参数
 */
export function pageUserList(data) {
  return request({
    url: '/sys/user/page',
    method: 'post',
    data,
  });
}

/**
 * 新增用户
 * @param {Object} data - 用户数据
 */
export function addUser(data) {
  return request({
    url: '/sys/user/add',
    method: 'post',
    data,
  });
}

/**
 * 获取用户详情
 * @param {string} id - 用户ID
 */
export function getUserDetail(id) {
  return request({
    url: `/sys/user/${id}`,
    method: 'get',
  });
}

/**
 * 修改用户信息
 * @param {Object} data - 用户数据
 */
export function updateUser(data) {
  return request({
    url: '/sys/user/update',
    method: 'put',
    data,
  });
}

/**
 * 删除用户
 * @param {string} id - 用户ID
 */
export function deleteUser(id) {
  return request({
    url: `/sys/user/${id}`,
    method: 'delete',
  });
}

/**
 * 启用/禁用用户
 * @param {string} id - 用户ID
 * @param {number} status - 状态：0-禁用，1-启用
 */
export function updateUserStatus(id, status) {
  return request({
    url: '/sys/user/status',
    method: 'put',
    params: { id, status },
  });
}

/**
 * 重置用户密码
 * @param {Object} data - 密码数据
 * @param {string} data.userId - 用户ID
 * @param {string} data.newPassword - 新密码
 */
export function resetPassword(data) {
  return request({
    url: '/sys/user/reset-password',
    method: 'put',
    data,
  });
}
