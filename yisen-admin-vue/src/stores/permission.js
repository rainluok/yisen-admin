import { defineStore } from 'pinia';
import router from '@/router';
import { useMenuStore } from './system/menu.js';
import { useUserStore } from './system/user.js';

/**
 * 动态路由和权限管理 Store
 */
export const usePermissionStore = defineStore('permission', {
  state: () => ({
    // 所有路由（包括静态和动态）
    routes: [],
    // 动态添加的路由
    addRoutes: [],
    // 是否已生成路由
    routesGenerated: false,
  }),

  getters: {
    /**
     * 获取侧边栏菜单
     */
    sidebarRoutes: (state) => {
      return state.routes.filter((route) => !route.meta?.hidden);
    },
  },

  actions: {
    /**
     * 生成路由
     * @param {Array} roles - 用户角色
     */
    async generateRoutes(roles) {
      try {
        const menuStore = useMenuStore();
        const userStore = useUserStore();

        // 获取用户菜单树
        const userMenuTree = await menuStore.fetchUserMenuTree(userStore.id);

        menuStore.userMenuTree = userMenuTree;
        console.log(22, menuStore.userMenuTree);
        // 生成动态路由
        const accessedRoutes = this.generateRoutesFromMenus(userMenuTree);

        // 设置路由
        this.addRoutes = accessedRoutes;
        this.routes = [...constantRoutes, ...accessedRoutes];
        this.routesGenerated = true;

        return accessedRoutes;
      } catch (error) {
        console.error('生成路由失败:', error);
        return Promise.reject(error);
      }
    },

    /**
     * 从菜单生成路由
     * @param {Array} menus - 菜单树
     * @param {String} parentPath - 父路径
     */
    generateRoutesFromMenus(menus, parentPath = '') {
      const routes = [];

      menus.forEach((menu) => {
        // 只处理菜单类型（type === 1）
        if (menu.type !== 1) {
          return;
        }

        const route = {
          path: menu.path,
          name: menu.menuCode,
          component: this.loadComponent(menu.component),
          meta: {
            title: menu.menuName,
            icon: menu.icon,
            hidden: menu.hidden === 1,
            noCache: menu.noCache === 1,
            affix: menu.affix === 1,
            permission: menu.permission,
            layout: menu.layout || 'default',
          },
        };

        // 递归处理子菜单
        if (menu.children && menu.children.length > 0) {
          route.children = this.generateRoutesFromMenus(menu.children, route.path);
        }

        routes.push(route);
      });

      return routes;
    },

    /**
     * 动态加载组件
     * @param {String} componentPath - 组件路径
     */
    loadComponent(componentPath) {
      if (!componentPath) {
        return () => import('@/views/error/404.vue');
      }

      // 处理布局组件
      if (componentPath === 'Layout') {
        return () => import('@/components/layout/Layout.vue');
      }

      // 处理其他组件
      // 支持 views/xxx/xxx 或 /views/xxx/xxx 格式
      const path = componentPath.startsWith('/') ? componentPath.slice(1) : componentPath;

      return () =>
        import(`@/${path}.vue`).catch((err) => {
          console.error(`加载组件失败: ${path}`, err);
          return import('@/views/error/404.vue');
        });
    },

    /**
     * 添加路由到 router
     */
    addRoutesToRouter() {
      this.addRoutes.forEach((route) => {
        router.addRoute(route);
      });
    },

    /**
     * 重置路由
     */
    resetRoutes() {
      this.routes = [];
      this.addRoutes = [];
      this.routesGenerated = false;
    },
  },
});

/**
 * 静态路由（不需要权限）
 */
export const constantRoutes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index.vue'),
    meta: { title: '登录', hidden: true },
  },
  {
    path: '/404',
    name: '404',
    component: () => import('@/views/error/404.vue'),
    meta: { title: '404', hidden: true },
  },
  {
    path: '/403',
    name: '403',
    component: () => import('@/views/error/403.vue'),
    meta: { title: '403', hidden: true },
  },
  {
    path: '/',
    redirect: '/dashboard',
    meta: { hidden: true },
  },
];
