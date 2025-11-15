import request from '@/utils/request';

// ==================== 操作日志相关 ====================

/**
 * 分页查询操作日志列表
 * @param {Object} data - 分页查询参数
 */
export function pageLogList(data) {
  return request({
    url: '/sys/log/page',
    method: 'post',
    data,
  });
}

/**
 * 获取操作日志详情
 * @param {string} id - 日志ID
 */
export function getLogDetail(id) {
  return request({
    url: `/sys/log/${id}`,
    method: 'get',
  });
}

/**
 * 删除操作日志
 * @param {string} id - 日志ID
 */
export function deleteLog(id) {
  return request({
    url: `/sys/log/${id}`,
    method: 'delete',
  });
}

/**
 * 批量删除操作日志
 * @param {Array} ids - 日志ID数组
 */
export function deleteBatchLog(ids) {
  return request({
    url: '/sys/log/batch',
    method: 'delete',
    data: ids,
  });
}

/**
 * 清空操作日志
 */
export function clearLogs() {
  return request({
    url: '/sys/log/clear',
    method: 'delete',
  });
}

// ==================== 登录日志相关 ====================

/**
 * 分页查询登录日志列表
 * @param {Object} data - 分页查询参数
 */
export function pageLoginLogList(data) {
  return request({
    url: '/sys/login-log/page',
    method: 'post',
    data,
  });
}

/**
 * 获取登录日志详情
 * @param {string} id - 日志ID
 */
export function getLoginLogDetail(id) {
  return request({
    url: `/sys/login-log/${id}`,
    method: 'get',
  });
}

/**
 * 删除登录日志
 * @param {string} id - 日志ID
 */
export function deleteLoginLog(id) {
  return request({
    url: `/sys/login-log/${id}`,
    method: 'delete',
  });
}

/**
 * 批量删除登录日志
 * @param {Array} ids - 日志ID数组
 */
export function deleteBatchLoginLog(ids) {
  return request({
    url: '/sys/login-log/batch',
    method: 'delete',
    data: ids,
  });
}

/**
 * 清空登录日志
 */
export function clearLoginLogs() {
  return request({
    url: '/sys/login-log/clear',
    method: 'delete',
  });
}
