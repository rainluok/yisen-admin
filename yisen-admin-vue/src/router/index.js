import { createRouter, createWebHistory } from 'vue-router';
import { useUserStore } from '@/stores/userStore';
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

/**
 * 动态路由
 * 需要根据权限动态加载的路由
 */
export const asyncRoutes = [
  {
    path: '/task',
    component: Layout,
    redirect: '/task/list',
    name: 'Task',
    meta: { title: '任务管理', icon: 'task' },
    children: [
      {
        path: 'list',
        name: 'TaskList',
        component: () => import('@/views/task/task-list.vue'),
        meta: { title: '任务列表', icon: 'list' },
      },
      {
        path: 'calendar',
        name: 'TaskCalendar',
        component: () => import('@/views/task/task-calendar.vue'),
        meta: { title: '任务日历', icon: 'calendar' },
      },
      {
        path: 'detail/:id',
        name: 'TaskDetail',
        component: () => import('@/views/task/task-detail.vue'),
        meta: { title: '任务详情', hidden: true },
      },
    ],
  },
  {
    path: '/user',
    component: Layout,
    redirect: '/user/list',
    name: 'User',
    meta: { title: '用户管理', icon: 'user', permission: 'user:view' },
    children: [
      {
        path: 'list',
        name: 'UserList',
        component: () => import('@/views/user/user-list.vue'),
        meta: { title: '用户列表', icon: 'list' },
      },
    ],
  },
  // 404 页面必须放在最后
  {
    path: '/:pathMatch(.*)*',
    redirect: '/404',
    meta: { hidden: true },
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
const whiteList = ['/login', '/404'];

// 路由守卫
router.beforeEach(async (to, from, next) => {
  nProgress.start();
  // 设置页面标题
  document.title = to.meta.title ? `${to.meta.title} - 益森管理系统` : '益森管理系统';

  const token = getToken();

  if (token) {
    if (to.path === '/login') {
      // 已登录，跳转到首页
      next({ path: '/' });
    } else {
      const userStore = useUserStore();

      // 判断是否已获取用户信息
      if (userStore.name) {
        next();
      } else {
        try {
          // 获取用户信息
          await userStore.getUserInfo();

          // 动态添加路由
          asyncRoutes.forEach((route) => {
            router.addRoute(route);
          });

          // 重新导航到目标路由
          next({ ...to, replace: true });
        } catch (error) {
          // 获取用户信息失败，清除 token 并跳转到登录页
          await userStore.logout();
          next(`/login?redirect=${to.path}`);
        }
      }
    }
  } else {
    // 未登录
    if (whiteList.includes(to.path)) {
      next();
    } else {
      next(`/login?redirect=${to.path}`);
    }
  }
});

router.afterEach(() => {
  nProgress.done();
});

export default router;
