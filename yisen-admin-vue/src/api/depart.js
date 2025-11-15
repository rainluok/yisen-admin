import request from '@/utils/request';

/**
 * 分页查询部门列表
 * @param {Object} data - 分页查询参数
 */
export function pageDepartList(data) {
  return request({
    url: '/sys/depart/page',
    method: 'post',
    data,
  });
}

/**
 * 查询所有部门列表
 * @param {Object} data - 查询参数
 */
export function getDepartList(data) {
  return request({
    url: '/sys/depart/list',
    method: 'post',
    data,
  });
}

/**
 * 获取部门树
 */
export function getDepartTree() {
  return request({
    url: '/sys/depart/tree',
    method: 'get',
  });
}

/**
 * 新增部门
 * @param {Object} data - 部门数据
 */
export function addDepart(data) {
  return request({
    url: '/sys/depart/add',
    method: 'post',
    data,
  });
}

/**
 * 获取部门详情
 * @param {string} id - 部门ID
 */
export function getDepartDetail(id) {
  return request({
    url: `/sys/depart/${id}`,
    method: 'get',
  });
}

/**
 * 修改部门信息
 * @param {Object} data - 部门数据
 */
export function updateDepart(data) {
  return request({
    url: '/sys/depart/update',
    method: 'put',
    data,
  });
}

/**
 * 删除部门
 * @param {string} id - 部门ID
 */
export function deleteDepart(id) {
  return request({
    url: `/sys/depart/${id}`,
    method: 'delete',
  });
}

/**
 * 启用/禁用部门
 * @param {string} id - 部门ID
 * @param {number} status - 状态：0-禁用，1-启用
 */
export function updateDepartStatus(id, status) {
  return request({
    url: '/sys/depart/status',
    method: 'put',
    params: { id, status },
  });
}
