import request from '@/utils/request';

/**
 * 分页查询字典列表
 * @param {Object} data - 分页查询参数
 */
export function pageDictList(data) {
  return request({
    url: '/sys/dict/page',
    method: 'post',
    data,
  });
}

/**
 * 查询所有字典列表
 * @param {Object} data - 查询参数
 */
export function getDictList(data) {
  return request({
    url: '/sys/dict/list',
    method: 'post',
    data,
  });
}

/**
 * 新增字典
 * @param {Object} data - 字典数据
 */
export function addDict(data) {
  return request({
    url: '/sys/dict/add',
    method: 'post',
    data,
  });
}

/**
 * 获取字典详情
 * @param {string} id - 字典ID
 */
export function getDictDetail(id) {
  return request({
    url: `/sys/dict/${id}`,
    method: 'get',
  });
}

/**
 * 修改字典信息
 * @param {Object} data - 字典数据
 */
export function updateDict(data) {
  return request({
    url: '/sys/dict/update',
    method: 'put',
    data,
  });
}

/**
 * 删除字典
 * @param {string} id - 字典ID
 */
export function deleteDict(id) {
  return request({
    url: `/sys/dict/${id}`,
    method: 'delete',
  });
}

// ==================== 字典项相关 ====================

/**
 * 分页查询字典项列表
 * @param {Object} data - 分页查询参数
 */
export function pageDictItemList(data) {
  return request({
    url: '/sys/dict-item/page',
    method: 'post',
    data,
  });
}

/**
 * 根据字典编码查询字典项
 * @param {string} dictCode - 字典编码
 */
export function getDictItemsByCode(dictCode) {
  return request({
    url: `/sys/dict-item/list/${dictCode}`,
    method: 'get',
  });
}

/**
 * 根据字典ID查询字典项
 * @param {string} dictId - 字典ID
 */
export function getDictItemsByDictId(dictId) {
  return request({
    url: `/sys/dict-item/list-by-id/${dictId}`,
    method: 'get',
  });
}

/**
 * 新增字典项
 * @param {Object} data - 字典项数据
 */
export function addDictItem(data) {
  return request({
    url: '/sys/dict-item/add',
    method: 'post',
    data,
  });
}

/**
 * 获取字典项详情
 * @param {string} id - 字典项ID
 */
export function getDictItemDetail(id) {
  return request({
    url: `/sys/dict-item/${id}`,
    method: 'get',
  });
}

/**
 * 修改字典项信息
 * @param {Object} data - 字典项数据
 */
export function updateDictItem(data) {
  return request({
    url: '/sys/dict-item/update',
    method: 'put',
    data,
  });
}

/**
 * 删除字典项
 * @param {string} id - 字典项ID
 */
export function deleteDictItem(id) {
  return request({
    url: `/sys/dict-item/${id}`,
    method: 'delete',
  });
}
