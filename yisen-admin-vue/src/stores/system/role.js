import { defineStore } from 'pinia';
import { pageRoleList, addRole, getRoleDetail, updateRole, deleteRole, assignMenus } from '@/api/role.js';

export const useRoleStore = defineStore('role', {
  state: () => ({
    // 角色列表
    roleList: [],
    // 当前角色
    currentRole: null,
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
     * 是否有角色数据
     */
    hasRoles: (state) => state.roleList.length > 0,

    /**
     * 角色总数
     */
    totalRoles: (state) => state.pagination.total,

    /**
     * 当前页码
     */
    currentPage: (state) => state.pagination.pageNum,
  },

  actions: {
    /**
     * 分页查询角色列表
     * @param {Object} params - 查询参数
     */
    async getRoleList(params = {}) {
      this.loading = true;
      try {
        const { pageNum = 1, pageSize = 10, ...queryParams } = params;
        const data = await pageRoleList({
          pageNum,
          pageSize,
          params: queryParams,
        });

        this.roleList = data.records || [];
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
     * 新增角色
     * @param {Object} roleData - 角色数据
     */
    async createRole(roleData) {
      try {
        const data = await addRole(roleData);
        // 重新加载列表
        await this.getRoleList({ pageNum: this.pagination.pageNum });
        return data;
      } catch (error) {
        return Promise.reject(error);
      }
    },

    /**
     * 获取角色详情
     * @param {string} id - 角色ID
     */
    async fetchRoleDetail(id) {
      try {
        const data = await getRoleDetail(id);
        this.currentRole = data;
        return data;
      } catch (error) {
        return Promise.reject(error);
      }
    },

    /**
     * 更新角色
     * @param {Object} roleData - 角色数据
     */
    async modifyRole(roleData) {
      try {
        await updateRole(roleData);
        // 重新加载列表
        await this.getRoleList({ pageNum: this.pagination.pageNum });
        // 如果是当前角色，更新当前角色信息
        if (this.currentRole && this.currentRole.id === roleData.id) {
          await this.fetchRoleDetail(roleData.id);
        }
      } catch (error) {
        return Promise.reject(error);
      }
    },

    /**
     * 删除角色
     * @param {string} id - 角色ID
     */
    async removeRole(id) {
      try {
        await deleteRole(id);
        // 重新加载列表
        await this.getRoleList({ pageNum: this.pagination.pageNum });
        // 如果删除的是当前角色，清空当前角色
        if (this.currentRole && this.currentRole.id === id) {
          this.currentRole = null;
        }
      } catch (error) {
        return Promise.reject(error);
      }
    },

    /**
     * 分配角色菜单权限
     * @param {Object} assignData - 分配数据
     */
    async assignRoleMenus(assignData) {
      try {
        await assignMenus(assignData);
        // 重新加载角色详情
        if (assignData.roleId) {
          await this.fetchRoleDetail(assignData.roleId);
        }
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
     * 清空当前角色
     */
    clearCurrentRole() {
      this.currentRole = null;
    },

    /**
     * 重置状态
     */
    resetState() {
      this.roleList = [];
      this.currentRole = null;
      this.pagination = {
        pageNum: 1,
        pageSize: 10,
        total: 0,
      };
      this.loading = false;
    },
  },
});
