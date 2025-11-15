import { defineStore } from 'pinia';
import { getUserInfo as getUserInfoApi, login, logout } from '@/api/user';
import { clearAuth, setPermissions, setToken, setUserInfo } from '@/utils/auth';

export const useUserStore = defineStore('user', {
  state: () => ({
    token: '',
    name: '',
    avatar: '',
    email: '',
    roles: [],
    permissions: [],
  }),

  getters: {
    /**
     * 是否已登录
     */
    isLoggedIn: (state) => !!state.token,

    /**
     * 用户信息
     */
    userInfo: (state) => ({
      name: state.name,
      avatar: state.avatar,
      email: state.email,
    }),
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

        this.token = data.token;
        setToken(data.token);

        return data;
      } catch (error) {
        return Promise.reject(error);
      }
    },

    /**
     * 获取用户信息
     */
    async getUserInfo() {
      try {
        const data = await getUserInfoApi();

        if (!data) {
          throw new Error('获取用户信息失败，请重新登录');
        }

        const { name, avatar, email, roles, permissions } = data;

        // 验证返回的数据
        if (!roles || roles.length <= 0) {
          throw new Error('用户角色不能为空');
        }

        this.name = name;
        this.avatar = avatar;
        this.email = email;
        this.roles = roles;
        this.permissions = permissions;

        // 保存到本地存储
        setUserInfo({ name, avatar, email, roles });
        setPermissions(permissions);

        return data;
      } catch (error) {
        return Promise.reject(error);
      }
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
      this.name = '';
      this.avatar = '';
      this.email = '';
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
  },
});
