import request from '@/utils/request';

/**
 * 分页查询角色列表
 * @param {Object} data - 分页查询参数
 */
export function pageRoleList(data) {
  return request({
    url: '/sys/role/page',
    method: 'post',
    data,
  });
}

/**
 * 新增角色
 * @param {Object} data - 角色数据
 */
export function addRole(data) {
  return request({
    url: '/sys/role/add',
    method: 'post',
    data,
  });
}

/**
 * 获取角色详情
 * @param {string} id - 角色ID
 */
export function getRoleDetail(id) {
  return request({
    url: `/sys/role/${id}`,
    method: 'get',
  });
}

/**
 * 修改角色信息
 * @param {Object} data - 角色数据
 */
export function updateRole(data) {
  return request({
    url: '/sys/role/update',
    method: 'put',
    data,
  });
}

/**
 * 删除角色
 * @param {string} id - 角色ID
 */
export function deleteRole(id) {
  return request({
    url: `/sys/role/${id}`,
    method: 'delete',
  });
}

/**
 * 分配角色菜单权限
 * @param {Object} data - 角色菜单数据
 * @param {string} data.roleId - 角色ID
 * @param {Array} data.menuIds - 菜单ID列表
 */
export function assignMenus(data) {
  return request({
    url: '/sys/role/assign-menus',
    method: 'post',
    data,
  });
}
