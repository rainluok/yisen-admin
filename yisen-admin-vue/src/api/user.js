import request from "@/utils/request";

/**
 * 用户登录
 * @param {Object} data - 登录数据
 * @param {string} data.username - 用户名
 * @param {string} data.password - 密码
 */
export function login(data) {
  return request({
    url: "/user/login",
    method: "post",
    data,
  });
}

/**
 * 获取用户信息
 */
export function getUserInfo() {
  return request({
    url: "/user/info",
    method: "get",
  });
}

/**
 * 用户登出
 */
export function logout() {
  return request({
    url: "/user/logout",
    method: "post",
  });
}

/**
 * 修改用户信息
 * @param {Object} data - 用户信息
 */
export function updateUserInfo(data) {
  return request({
    url: "/user/update",
    method: "put",
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
    url: "/user/password",
    method: "put",
    data,
  });
}

/**
 * 获取用户列表
 * @param {Object} params - 查询参数
 */
export function getUserList(params) {
  return request({
    url: "/user/list",
    method: "get",
    params,
  });
}
