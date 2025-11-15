<template>
  <div class="sidebar-container" :class="{ collapsed: !sidebarOpened }">
    <div class="web-title">
      <img class="web-logo" :src="LOGO" alt="logo"/>
      <span>益森管理系统</span>
    </div>
    <el-scrollbar>
      <el-menu :default-active="activeMenu" :collapse="!sidebarOpened" :collapse-transition="false" :unique-opened="true" router>
        <sidebar-item v-for="route in menuRoutes" :key="route.path" :item="route" :base-path="route.path" />
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<script setup>
  import { computed } from 'vue';
  import { useRoute, useRouter } from 'vue-router';
  import { useAppStore } from '@/stores/app.js';
  import { useUserStore } from '@/stores/system/user.js';
  import SidebarItem from './SidebarItem.vue';
  import { useMenuStore } from '@/stores/index.js';
  import LOGO from '@/assets/images/logo.jpg';
  const route = useRoute();
  const router = useRouter();
  const appStore = useAppStore();
  const userStore = useUserStore();
  const menuStore = useMenuStore();
  // 侧边栏状态
  const sidebarOpened = computed(() => appStore.sidebarOpened);

  // 当前激活菜单
  const activeMenu = computed(() => {
    const { meta, path } = route;
    if (meta.activeMenu) {
      return meta.activeMenu;
    }
    return path;
  });

  // 菜单路由列表
  const menuRoutes = computed(() => {
    const userMenuTree = menuStore.userMenuTree;
    return userMenuTree.filter((menuItem) => {
      if (menuItem?.type !== 1) {
        return false;
      }

      // 过滤隐藏的路由
      if (menuItem?.hidden) {
        return false;
      }

      // 检查权限
      if (menuItem?.permission) {
        return userStore.hasPermission(menuItem.permission);
      }

      return true;
    });
  });
</script>

<style scoped lang="scss">
  .sidebar-container {
    width: 200px;
    height: 100%;
    //background-color: #304156;
    transition: width 0.28s;
    overflow: hidden;

    &.collapsed {
      width: 64px;
    }

    :deep(.el-scrollbar) {
      height: 100%;
    }

    .web-title{
      display: flex;
      align-items: center;
      justify-content: center;
      border-bottom: 1px solid #e6e6e6;
      width: 200px;
      height: 60px;
      margin:0 auto;

      .web-logo{
        width: 40px;
        height: 40px;
      }
    }
    //:deep(.el-menu) {
    //  border-right: none;
    //  background-color: #304156;
    //}

    //:deep(.el-menu-item),
    //:deep(.el-submenu__title) {
    //  color: #bfcbd9;
    //
    //  &:hover {
    //    background-color: #263445 !important;
    //  }
  }

  //:deep(.el-menu-item.is-active) {
  //  background-color: #409eff !important;
  //  color: #fff;
  //}
  //
  //:deep(.el-submenu.is-active > .el-submenu__title) {
  //  color: #409eff;
  //}
  //}
</style>
