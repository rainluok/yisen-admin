import { defineStore } from 'pinia';
import {
  getUserInfo as getUserInfoApi,
  login,
  logout,
  pageUserList,
  addUser,
  getUserDetail,
  updateUser,
  deleteUser,
  updateUserStatus,
  resetPassword,
} from '@/api/user.js';
import { clearAuth, setPermissions, setToken, setUserInfo } from '@utils/auth.js';

export const useUserStore = defineStore('user', {
  state: () => ({
    // 当前登录用户信息
    token: '',
    id: '',
    username: '',
    realName: '',
    gender: null,
    avatar: '',
    email: '',
    departId: '',
    loginIp: '',
    loginTime: null,
    status: null,
    roles: [],
    permissions: [],
    // 用户管理列表
    userList: [],
    // 当前查看的用户详情
    currentUser: null,
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
     * 是否已登录
     */
    isLoggedIn: (state) => !!state.token,

    /**
     * 当前用户信息
     */
    userInfo: (state) => ({
      id: state.id,
      username: state.username,
      realName: state.realName,
      gender: state.gender,
      avatar: state.avatar,
      email: state.email,
      departId: state.departId,
      loginIp: state.loginIp,
      loginTime: state.loginTime,
      status: state.status,
      roles: state.roles,
      permissions: state.permissions,
    }),

    /**
     * 用户显示名称
     */
    displayName: (state) => state.realName || state.username,

    /**
     * 是否有用户数据
     */
    hasUsers: (state) => state.userList.length > 0,

    /**
     * 用户总数
     */
    totalUsers: (state) => state.pagination.total,

    /**
     * 当前页码
     */
    currentPage: (state) => state.pagination.pageNum,

    /**
     * 是否为男性
     */
    isMale: (state) => state.gender === 1,

    /**
     * 是否为女性
     */
    isFemale: (state) => state.gender === 2,

    /**
     * 性别文本
     */
    genderText: (state) => {
      const genderMap = { 0: '未知', 1: '男', 2: '女' };
      return genderMap[state.gender] || '未知';
    },
  },

  actions: {
    /**
     * 用户登录
     * @param {Object} loginForm - 登录表单
     */
    async login(loginForm) {
      try {
        const { username, password } = loginForm;
        const data = await login({ username: username.trim(), password });

        // 设置 token
        this.token = data.token;
        setToken(data.token);

        // 设置用户信息
        if (data.userInfo) {
          this.setUserInfoData(data.userInfo);
        }

        return data;
      } catch (error) {
        return Promise.reject(error);
      }
    },

    /**
     * 获取当前登录用户信息
     */
    async getUserInfo() {
      try {
        const data = await getUserInfoApi();

        if (!data) {
          throw new Error('获取用户信息失败，请重新登录');
        }

        // 验证返回的数据
        if (!data.roles || data.roles.length <= 0) {
          throw new Error('用户角色不能为空');
        }

        this.setUserInfoData(data);

        return data;
      } catch (error) {
        return Promise.reject(error);
      }
    },

    /**
     * 设置用户信息数据
     * @param {Object} userInfo - 用户信息
     */
    setUserInfoData(userInfo) {
      this.id = userInfo.id;
      this.username = userInfo.username;
      this.realName = userInfo.realName;
      this.gender = userInfo.gender;
      this.avatar = userInfo.avatar;
      this.email = userInfo.email;
      this.departId = userInfo.departId;
      this.loginIp = userInfo.loginIp;
      this.loginTime = userInfo.loginTime;
      this.status = userInfo.status;
      this.roles = Array.from(userInfo.roles || []);
      this.permissions = Array.from(userInfo.permissions || []);

      // 保存到本地存储
      setUserInfo({
        id: this.id,
        username: this.username,
        realName: this.realName,
        avatar: this.avatar,
        email: this.email,
        roles: this.roles,
      });
      setPermissions(this.permissions);
    },

    /**
     * 用户登出
     */
    async logout() {
      try {
        await logout();
      } catch (error) {
        console.error('登出失败:', error);
      } finally {
        this.resetState();
      }
    },

    /**
     * 重置状态
     */
    resetState() {
      this.token = '';
      this.id = '';
      this.username = '';
      this.realName = '';
      this.gender = null;
      this.avatar = '';
      this.email = '';
      this.departId = '';
      this.loginIp = '';
      this.loginTime = null;
      this.status = null;
      this.roles = [];
      this.permissions = [];

      clearAuth();
    },

    /**
     * 检查是否有权限
     * @param {string|Array} permission - 权限标识
     */
    hasPermission(permission) {
      if (!permission) return true;

      if (typeof permission === 'string') {
        return this.permissions.includes(permission);
      }

      if (Array.isArray(permission)) {
        return permission.some((p) => this.permissions.includes(p));
      }

      return false;
    },

    /**
     * 检查是否有所有权限
     * @param {Array} permissions - 权限标识数组
     */
    hasAllPermissions(permissions) {
      if (!permissions || permissions.length === 0) return true;
      return permissions.every((p) => this.permissions.includes(p));
    },

    /**
     * 检查是否有角色
     * @param {string|Array} role - 角色标识
     */
    hasRole(role) {
      if (!role) return true;

      if (typeof role === 'string') {
        return this.roles.includes(role);
      }

      if (Array.isArray(role)) {
        return role.some((r) => this.roles.includes(r));
      }

      return false;
    },

    // ==================== 用户管理相关 ====================

    /**
     * 分页查询用户列表
     * @param {Object} params - 查询参数
     */
    async getUserList(params = {}) {
      this.loading = true;
      try {
        const { pageNum = 1, pageSize = 10, ...queryParams } = params;
        const data = await pageUserList({
          pageNum,
          pageSize,
          params: queryParams,
        });

        this.userList = data.records || [];
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
     * 新增用户
     * @param {Object} userData - 用户数据
     */
    async createUser(userData) {
      try {
        const data = await addUser(userData);
        // 重新加载列表
        await this.getUserList({ pageNum: this.pagination.pageNum });
        return data;
      } catch (error) {
        return Promise.reject(error);
      }
    },

    /**
     * 获取用户详情
     * @param {string} id - 用户ID
     */
    async fetchUserDetail(id) {
      try {
        const data = await getUserDetail(id);
        this.currentUser = data;
        return data;
      } catch (error) {
        return Promise.reject(error);
      }
    },

    /**
     * 更新用户信息
     * @param {Object} userData - 用户数据
     */
    async modifyUser(userData) {
      try {
        await updateUser(userData);
        // 重新加载列表
        await this.getUserList({ pageNum: this.pagination.pageNum });
        // 如果是当前用户，更新当前用户信息
        if (this.currentUser && this.currentUser.id === userData.id) {
          await this.fetchUserDetail(userData.id);
        }
      } catch (error) {
        return Promise.reject(error);
      }
    },

    /**
     * 删除用户
     * @param {string} id - 用户ID
     */
    async removeUser(id) {
      try {
        await deleteUser(id);
        // 重新加载列表
        await this.getUserList({ pageNum: this.pagination.pageNum });
        // 如果删除的是当前用户，清空当前用户
        if (this.currentUser && this.currentUser.id === id) {
          this.currentUser = null;
        }
      } catch (error) {
        return Promise.reject(error);
      }
    },

    /**
     * 启用/禁用用户
     * @param {string} id - 用户ID
     * @param {number} status - 状态
     */
    async changeUserStatus(id, status) {
      try {
        await updateUserStatus(id, status);
        // 重新加载列表
        await this.getUserList({ pageNum: this.pagination.pageNum });
      } catch (error) {
        return Promise.reject(error);
      }
    },

    /**
     * 重置用户密码
     * @param {Object} data - 密码数据
     */
    async resetUserPassword(data) {
      try {
        await resetPassword(data);
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
     * 清空当前用户详情
     */
    clearCurrentUser() {
      this.currentUser = null;
    },

    /**
     * 重置用户管理状态
     */
    resetUserManagementState() {
      this.userList = [];
      this.currentUser = null;
      this.pagination = {
        pageNum: 1,
        pageSize: 10,
        total: 0,
      };
      this.loading = false;
    },
  },
});
