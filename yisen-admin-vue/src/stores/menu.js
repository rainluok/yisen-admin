import { defineStore } from 'pinia';
import { getMenuList, getMenuTree, getUserMenuTree, addMenu, getMenuDetail, updateMenu, deleteMenu } from '@/api/menu';

export const useMenuStore = defineStore('menu', {
  state: () => ({
    // 菜单列表（平铺）
    menuList: [],
    // 菜单树
    menuTree: [],
    // 用户菜单树
    userMenuTree: [],
    // 当前菜单
    currentMenu: null,
    // 路由菜单（用于生成动态路由）
    routes: [],
    // 加载状态
    loading: false,
  }),

  getters: {
    /**
     * 是否有菜单数据
     */
    hasMenus: (state) => state.menuList.length > 0,

    /**
     * 菜单总数
     */
    totalMenus: (state) => state.menuList.length,

    /**
     * 一级菜单
     */
    topMenus: (state) => state.menuList.filter((menu) => menu.parentId === '0'),

    /**
     * 按钮权限列表
     */
    buttonPermissions: (state) => state.menuList.filter((menu) => menu.type === 2).map((menu) => menu.permission),
  },

  actions: {
    /**
     * 查询所有菜单列表（平铺）
     * @param {Object} params - 查询参数
     */
    async fetchMenuList(params = {}) {
      this.loading = true;
      try {
        const data = await getMenuList(params);
        this.menuList = data || [];
        return data;
      } catch (error) {
        return Promise.reject(error);
      } finally {
        this.loading = false;
      }
    },

    /**
     * 获取菜单树
     */
    async fetchMenuTree() {
      this.loading = true;
      try {
        const data = await getMenuTree();
        this.menuTree = data || [];
        return data;
      } catch (error) {
        return Promise.reject(error);
      } finally {
        this.loading = false;
      }
    },

    /**
     * 获取用户菜单树
     * @param {string} userId - 用户ID
     */
    async fetchUserMenuTree(userId) {
      this.loading = true;
      try {
        const data = await getUserMenuTree(userId);
        this.userMenuTree = data || [];
        // 生成路由
        this.routes = this.generateRoutes(data);
        return data;
      } catch (error) {
        return Promise.reject(error);
      } finally {
        this.loading = false;
      }
    },

    /**
     * 新增菜单
     * @param {Object} menuData - 菜单数据
     */
    async createMenu(menuData) {
      try {
        const data = await addMenu(menuData);
        // 重新加载列表
        await this.fetchMenuList();
        await this.fetchMenuTree();
        return data;
      } catch (error) {
        return Promise.reject(error);
      }
    },

    /**
     * 获取菜单详情
     * @param {string} id - 菜单ID
     */
    async fetchMenuDetail(id) {
      try {
        const data = await getMenuDetail(id);
        this.currentMenu = data;
        return data;
      } catch (error) {
        return Promise.reject(error);
      }
    },

    /**
     * 更新菜单
     * @param {Object} menuData - 菜单数据
     */
    async modifyMenu(menuData) {
      try {
        await updateMenu(menuData);
        // 重新加载列表
        await this.fetchMenuList();
        await this.fetchMenuTree();
        // 如果是当前菜单，更新当前菜单信息
        if (this.currentMenu && this.currentMenu.id === menuData.id) {
          await this.fetchMenuDetail(menuData.id);
        }
      } catch (error) {
        return Promise.reject(error);
      }
    },

    /**
     * 删除菜单
     * @param {string} id - 菜单ID
     */
    async removeMenu(id) {
      try {
        await deleteMenu(id);
        // 重新加载列表
        await this.fetchMenuList();
        await this.fetchMenuTree();
        // 如果删除的是当前菜单，清空当前菜单
        if (this.currentMenu && this.currentMenu.id === id) {
          this.currentMenu = null;
        }
      } catch (error) {
        return Promise.reject(error);
      }
    },

    /**
     * 生成路由
     * @param {Array} menus - 菜单树
     */
    generateRoutes(menus) {
      const routes = [];

      const traverse = (menuList, parentPath = '') => {
        menuList.forEach((menu) => {
          if (menu.type === 1 && menu.path) {
            // 只处理菜单类型
            const route = {
              path: menu.path,
              name: menu.menuCode,
              component: menu.component,
              meta: {
                title: menu.menuName,
                icon: menu.icon,
                hidden: menu.hidden === 1,
                permission: menu.permission,
              },
            };

            if (menu.children && menu.children.length > 0) {
              route.children = [];
              traverse(menu.children, route.path);
            }

            routes.push(route);
          }
        });
      };

      traverse(menus);
      return routes;
    },

    /**
     * 清空当前菜单
     */
    clearCurrentMenu() {
      this.currentMenu = null;
    },

    /**
     * 重置状态
     */
    resetState() {
      this.menuList = [];
      this.menuTree = [];
      this.userMenuTree = [];
      this.currentMenu = null;
      this.routes = [];
      this.loading = false;
    },
  },
});

