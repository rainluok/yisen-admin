import { createRouter, createWebHistory } from 'vue-router';
import { useUserStore } from '@/stores/system/user.js';
import { usePermissionStore } from '@/stores/permission.js';
import { getToken } from '@/utils/auth';
import Layout from '@/components/layout/Layout.vue';
import nProgress from 'nprogress';

/**
 * 公共路由
 * 不需要权限验证的路由
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
    path: '/redirect',
    component: Layout,
    meta: { hidden: true },
    children: [
      {
        path: '/redirect/:path(.*)',
        component: () => import('@/views/redirect/index.vue'),
      },
    ],
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/index.vue'),
        meta: { title: '仪表盘', icon: 'dashboard', affix: true },
      },
    ],
  },
];

// 创建路由实例
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: constantRoutes,
  scrollBehavior(to, from, savedPosition) {
    if (savedPosition) {
      return savedPosition;
    } else {
      return { top: 0 };
    }
  },
});

// 白名单，不需要登录的页面
const whiteList = ['/login', '/404', '/403'];

// 路由守卫
router.beforeEach(async (to, from, next) => {
  nProgress.start();
  // 设置页面标题
  // eslint-disable-next-line no-undef
  document.title = to.meta.title ? `${to.meta.title} - 益森管理系统` : '益森管理系统';

  const token = getToken();

  if (token) {
    if (to.path === '/login') {
      // 已登录，跳转到首页
      next({ path: '/' });
      nProgress.done();
    } else {
      const userStore = useUserStore();
      const permissionStore = usePermissionStore();

      // 判断是否已获取用户信息
      if (userStore.username) {
        // 判断是否已生成路由
        if (permissionStore.routesGenerated) {
          next();
        } else {
          try {
            // 生成动态路由
            const accessRoutes = await permissionStore.generateRoutes(userStore.roles);

            // 动态添加路由
            accessRoutes.forEach((route) => {
              router.addRoute(route);
            });

            // 添加 404 页面（必须在最后）
            router.addRoute({
              path: '/:pathMatch(.*)*',
              redirect: '/404',
              meta: { hidden: true },
            });

            // 重新导航到目标路由
            next({ ...to, replace: true });
          } catch (error) {
            console.error('生成路由失败:', error);
            // 重置状态
            await userStore.logout();
            permissionStore.resetRoutes();
            next(`/login?redirect=${to.path}`);
            nProgress.done();
          }
        }
      } else {
        try {
          // 获取用户信息
          await userStore.getUserInfo();

          // 生成动态路由
          const accessRoutes = await permissionStore.generateRoutes(userStore.roles);

          // 动态添加路由
          accessRoutes.forEach((route) => {
            router.addRoute(route);
          });

          // 添加 404 页面（必须在最后）
          router.addRoute({
            path: '/:pathMatch(.*)*',
            redirect: '/404',
            meta: { hidden: true },
          });

          // 重新导航到目标路由
          next({ ...to, replace: true });
        } catch (error) {
          console.error('获取用户信息失败:', error);
          // 获取用户信息失败，清除 token 并跳转到登录页
          await userStore.logout();
          permissionStore.resetRoutes();
          next(`/login?redirect=${to.path}`);
          nProgress.done();
        }
      }
    }
  } else {
    // 未登录
    if (whiteList.includes(to.path)) {
      next();
    } else {
      next(`/login?redirect=${to.path}`);
      nProgress.done();
    }
  }
});

router.afterEach(() => {
  nProgress.done();
});

export default router;
