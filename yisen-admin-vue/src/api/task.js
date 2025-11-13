import request from "@/utils/request";

/**
 * 获取任务列表
 * @param {Object} params - 查询参数
 * @param {number} params.page - 页码
 * @param {number} params.pageSize - 每页数量
 * @param {number} params.status - 任务状态
 */
export function getTaskList(params) {
  return request({
    url: "/task/list",
    method: "get",
    params,
  });
}

/**
 * 获取任务详情
 * @param {number|string} id - 任务ID
 */
export function getTaskDetail(id) {
  return request({
    url: `/task/${id}`,
    method: "get",
  });
}

/**
 * 创建任务
 * @param {Object} data - 任务数据
 */
export function createTask(data) {
  return request({
    url: "/task/create",
    method: "post",
    data,
  });
}

/**
 * 更新任务
 * @param {number|string} id - 任务ID
 * @param {Object} data - 任务数据
 */
export function updateTask(id, data) {
  return request({
    url: `/task/${id}`,
    method: "put",
    data,
  });
}

/**
 * 删除任务
 * @param {number|string} id - 任务ID
 */
export function deleteTask(id) {
  return request({
    url: `/task/${id}`,
    method: "delete",
  });
}

/**
 * 更新任务状态
 * @param {number|string} id - 任务ID
 * @param {number} status - 任务状态
 */
export function updateTaskStatus(id, status) {
  return request({
    url: `/task/${id}/status`,
    method: "put",
    data: { status },
  });
}

/**
 * 获取任务统计
 */
export function getTaskStatistics() {
  return request({
    url: "/task/statistics",
    method: "get",
  });
}
