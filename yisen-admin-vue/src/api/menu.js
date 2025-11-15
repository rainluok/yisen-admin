import request from '@/utils/request';

/**
 * 查询所有菜单列表（平铺）
 * @param {Object} data - 查询参数
 */
export function getMenuList(data) {
  return request({
    url: '/sys/menu/list',
    method: 'post',
    data,
  });
}

/**
 * 获取菜单树
 */
export function getMenuTree() {
  return request({
    url: '/sys/menu/tree',
    method: 'get',
  });
}

/**
 * 获取用户菜单树
 * @param {string} userId - 用户ID
 */
export function getUserMenuTree(userId) {
  return request({
    url: `/sys/menu/user-tree/${userId}`,
    method: 'get',
  });
}

/**
 * 新增菜单
 * @param {Object} data - 菜单数据
 */
export function addMenu(data) {
  return request({
    url: '/sys/menu/add',
    method: 'post',
    data,
  });
}

/**
 * 获取菜单详情
 * @param {string} id - 菜单ID
 */
export function getMenuDetail(id) {
  return request({
    url: `/sys/menu/${id}`,
    method: 'get',
  });
}

/**
 * 修改菜单信息
 * @param {Object} data - 菜单数据
 */
export function updateMenu(data) {
  return request({
    url: '/sys/menu/update',
    method: 'put',
    data,
  });
}

/**
 * 删除菜单
 * @param {string} id - 菜单ID
 */
export function deleteMenu(id) {
  return request({
    url: `/sys/menu/${id}`,
    method: 'delete',
  });
}
