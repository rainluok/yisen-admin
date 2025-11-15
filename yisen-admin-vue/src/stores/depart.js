import { defineStore } from 'pinia';
import {
  pageDepartList,
  getDepartList,
  getDepartTree,
  addDepart,
  getDepartDetail,
  updateDepart,
  deleteDepart,
  updateDepartStatus,
} from '@/api/depart';

export const useDepartStore = defineStore('depart', {
  state: () => ({
    // 部门列表
    departList: [],
    // 部门树
    departTree: [],
    // 当前部门
    currentDepart: null,
    // 分页信息
    pagination: {
      pageNum: 1,
      pageSize: 10,
      total: 0,
    },
    // 加载状态
    loading: false,
  }),

  getters: {
    /**
     * 是否有部门数据
     */
    hasDeparts: (state) => state.departList.length > 0,

    /**
     * 部门总数
     */
    totalDeparts: (state) => state.pagination.total,

    /**
     * 当前页码
     */
    currentPage: (state) => state.pagination.pageNum,

    /**
     * 一级部门
     */
    topDeparts: (state) => state.departList.filter((depart) => depart.parentId === '0'),
  },

  actions: {
    /**
     * 分页查询部门列表
     * @param {Object} params - 查询参数
     */
    async getDepartListPage(params = {}) {
      this.loading = true;
      try {
        const { pageNum = 1, pageSize = 10, ...queryParams } = params;
        const data = await pageDepartList({
          pageNum,
          pageSize,
          params: queryParams,
        });

        this.departList = data.records || [];
        this.pagination = {
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
     * 查询所有部门列表
     * @param {Object} params - 查询参数
     */
    async fetchDepartList(params = {}) {
      this.loading = true;
      try {
        const data = await getDepartList(params);
        this.departList = data || [];
        return data;
      } catch (error) {
        return Promise.reject(error);
      } finally {
        this.loading = false;
      }
    },

    /**
     * 获取部门树
     */
    async fetchDepartTree() {
      this.loading = true;
      try {
        const data = await getDepartTree();
        this.departTree = data || [];
        return data;
      } catch (error) {
        return Promise.reject(error);
      } finally {
        this.loading = false;
      }
    },

    /**
     * 新增部门
     * @param {Object} departData - 部门数据
     */
    async createDepart(departData) {
      try {
        const data = await addDepart(departData);
        // 重新加载列表和树
        await this.fetchDepartList();
        await this.fetchDepartTree();
        return data;
      } catch (error) {
        return Promise.reject(error);
      }
    },

    /**
     * 获取部门详情
     * @param {string} id - 部门ID
     */
    async fetchDepartDetail(id) {
      try {
        const data = await getDepartDetail(id);
        this.currentDepart = data;
        return data;
      } catch (error) {
        return Promise.reject(error);
      }
    },

    /**
     * 更新部门
     * @param {Object} departData - 部门数据
     */
    async modifyDepart(departData) {
      try {
        await updateDepart(departData);
        // 重新加载列表和树
        await this.fetchDepartList();
        await this.fetchDepartTree();
        // 如果是当前部门，更新当前部门信息
        if (this.currentDepart && this.currentDepart.id === departData.id) {
          await this.fetchDepartDetail(departData.id);
        }
      } catch (error) {
        return Promise.reject(error);
      }
    },

    /**
     * 删除部门
     * @param {string} id - 部门ID
     */
    async removeDepart(id) {
      try {
        await deleteDepart(id);
        // 重新加载列表和树
        await this.fetchDepartList();
        await this.fetchDepartTree();
        // 如果删除的是当前部门，清空当前部门
        if (this.currentDepart && this.currentDepart.id === id) {
          this.currentDepart = null;
        }
      } catch (error) {
        return Promise.reject(error);
      }
    },

    /**
     * 启用/禁用部门
     * @param {string} id - 部门ID
     * @param {number} status - 状态
     */
    async changeDepartStatus(id, status) {
      try {
        await updateDepartStatus(id, status);
        // 重新加载列表
        await this.fetchDepartList();
        await this.fetchDepartTree();
      } catch (error) {
        return Promise.reject(error);
      }
    },

    /**
     * 设置分页信息
     * @param {Object} pagination - 分页信息
     */
    setPagination(pagination) {
      this.pagination = { ...this.pagination, ...pagination };
    },

    /**
     * 清空当前部门
     */
    clearCurrentDepart() {
      this.currentDepart = null;
    },

    /**
     * 重置状态
     */
    resetState() {
      this.departList = [];
      this.departTree = [];
      this.currentDepart = null;
      this.pagination = {
        pageNum: 1,
        pageSize: 10,
        total: 0,
      };
      this.loading = false;
    },
  },
});

