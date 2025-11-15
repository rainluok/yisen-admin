import { defineStore } from 'pinia';
import {
  pageLogList,
  getLogDetail,
  deleteLog,
  deleteBatchLog,
  clearLogs,
  pageLoginLogList,
  getLoginLogDetail,
  deleteLoginLog,
  deleteBatchLoginLog,
  clearLoginLogs,
} from '@/api/log.js';

export const useLogStore = defineStore('log', {
  state: () => ({
    // 操作日志列表
    logList: [],
    // 登录日志列表
    loginLogList: [],
    // 当前操作日志
    currentLog: null,
    // 当前登录日志
    currentLoginLog: null,
    // 操作日志分页信息
    logPagination: {
      pageNum: 1,
      pageSize: 10,
      total: 0,
    },
    // 登录日志分页信息
    loginLogPagination: {
      pageNum: 1,
      pageSize: 10,
      total: 0,
    },
    // 加载状态
    loading: false,
  }),

  getters: {
    /**
     * 操作日志总数
     */
    totalLogs: (state) => state.logPagination.total,

    /**
     * 登录日志总数
     */
    totalLoginLogs: (state) => state.loginLogPagination.total,
  },

  actions: {
    // ==================== 操作日志相关 ====================

    /**
     * 分页查询操作日志列表
     * @param {Object} params - 查询参数
     */
    async getLogListPage(params = {}) {
      this.loading = true;
      try {
        const { pageNum = 1, pageSize = 10, ...queryParams } = params;
        const data = await pageLogList({
          pageNum,
          pageSize,
          params: queryParams,
        });

        this.logList = data.records || [];
        this.logPagination = {
          pageNum: data.pageNum,
          pageSize: data.pageSize,
          total: data.total,
        };

        return data;
      } catch (error) {
        return Promise.reject(error);
      } finally {
        this.loading = false;
      }
    },

    /**
     * 获取操作日志详情
     * @param {string} id - 日志ID
     */
    async fetchLogDetail(id) {
      try {
        const data = await getLogDetail(id);
        this.currentLog = data;
        return data;
      } catch (error) {
        return Promise.reject(error);
      }
    },

    /**
     * 删除操作日志
     * @param {string} id - 日志ID
     */
    async removeLog(id) {
      try {
        await deleteLog(id);
        await this.getLogListPage({ pageNum: this.logPagination.pageNum });
        if (this.currentLog && this.currentLog.id === id) {
          this.currentLog = null;
        }
      } catch (error) {
        return Promise.reject(error);
      }
    },

    /**
     * 批量删除操作日志
     * @param {Array} ids - 日志ID数组
     */
    async removeBatchLog(ids) {
      try {
        await deleteBatchLog(ids);
        await this.getLogListPage({ pageNum: this.logPagination.pageNum });
      } catch (error) {
        return Promise.reject(error);
      }
    },

    /**
     * 清空操作日志
     */
    async clearAllLogs() {
      try {
        await clearLogs();
        this.logList = [];
        this.logPagination = {
          pageNum: 1,
          pageSize: 10,
          total: 0,
        };
        this.currentLog = null;
      } catch (error) {
        return Promise.reject(error);
      }
    },

    // ==================== 登录日志相关 ====================

    /**
     * 分页查询登录日志列表
     * @param {Object} params - 查询参数
     */
    async getLoginLogListPage(params = {}) {
      this.loading = true;
      try {
        const { pageNum = 1, pageSize = 10, ...queryParams } = params;
        const data = await pageLoginLogList({
          pageNum,
          pageSize,
          params: queryParams,
        });

        this.loginLogList = data.records || [];
        this.loginLogPagination = {
          pageNum: data.pageNum,
          pageSize: data.pageSize,
          total: data.total,
        };

        return data;
      } catch (error) {
        return Promise.reject(error);
      } finally {
        this.loading = false;
      }
    },

    /**
     * 获取登录日志详情
     * @param {string} id - 日志ID
     */
    async fetchLoginLogDetail(id) {
      try {
        const data = await getLoginLogDetail(id);
        this.currentLoginLog = data;
        return data;
      } catch (error) {
        return Promise.reject(error);
      }
    },

    /**
     * 删除登录日志
     * @param {string} id - 日志ID
     */
    async removeLoginLog(id) {
      try {
        await deleteLoginLog(id);
        await this.getLoginLogListPage({ pageNum: this.loginLogPagination.pageNum });
        if (this.currentLoginLog && this.currentLoginLog.id === id) {
          this.currentLoginLog = null;
        }
      } catch (error) {
        return Promise.reject(error);
      }
    },

    /**
     * 批量删除登录日志
     * @param {Array} ids - 日志ID数组
     */
    async removeBatchLoginLog(ids) {
      try {
        await deleteBatchLoginLog(ids);
        await this.getLoginLogListPage({ pageNum: this.loginLogPagination.pageNum });
      } catch (error) {
        return Promise.reject(error);
      }
    },

    /**
     * 清空登录日志
     */
    async clearAllLoginLogs() {
      try {
        await clearLoginLogs();
        this.loginLogList = [];
        this.loginLogPagination = {
          pageNum: 1,
          pageSize: 10,
          total: 0,
        };
        this.currentLoginLog = null;
      } catch (error) {
        return Promise.reject(error);
      }
    },

    /**
     * 清空当前操作日志
     */
    clearCurrentLog() {
      this.currentLog = null;
    },

    /**
     * 清空当前登录日志
     */
    clearCurrentLoginLog() {
      this.currentLoginLog = null;
    },

    /**
     * 重置状态
     */
    resetState() {
      this.logList = [];
      this.loginLogList = [];
      this.currentLog = null;
      this.currentLoginLog = null;
      this.logPagination = {
        pageNum: 1,
        pageSize: 10,
        total: 0,
      };
      this.loginLogPagination = {
        pageNum: 1,
        pageSize: 10,
        total: 0,
      };
      this.loading = false;
    },
  },
});
