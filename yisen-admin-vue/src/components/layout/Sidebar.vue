<template>
  <div class="sidebar-container" :class="{ collapsed: !sidebarOpened }">
    <div class="web-title">
      <img class="web-logo" :src="LOGO" alt="logo" />
      <span v-show="sidebarOpened" class="title-text">益森管理系统</span>
    </div>
    <el-scrollbar class="sidebar-scrollbar">
      <el-menu :default-active="activeMenu" :collapse="!sidebarOpened" :collapse-transition="false" :unique-opened="true" router class="sidebar-menu">
        <sidebar-item v-for="route in menuRoutes" :key="route.path" :item="route" :base-path="route.path" />
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<script setup>
  import { computed } from 'vue';
  import { useRoute } from 'vue-router';
  import { useAppStore } from '@/stores/app.js';
  import { useUserStore } from '@/stores/system/user.js';
  import { useMenuStore } from '@/stores/index.js';
  import SidebarItem from './SidebarItem.vue';
  import LOGO from '@/assets/images/logo.jpg';

  const route = useRoute();
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
  @import '@/styles/variables.scss';

  .sidebar-container {
    width: $sidebar-width;
    height: 100%;
    background-color: #304156;
    transition: width 0.28s;
    overflow: hidden;
    box-shadow: 2px 0 8px rgba(0, 0, 0, 0.1);
    position: relative;
    z-index: $z-index-sidebar;

    &.collapsed {
      width: $sidebar-collapsed-width;
    }

    .web-title {
      display: flex;
      align-items: center;
      justify-content: center;
      border-bottom: 1px solid #2a3a4a;
      width: $sidebar-width;
      height: $sidebar-logo-height;
      margin: 0 auto;
      background-color: #2a3a4a;
      transition: all 0.28s;
      overflow: hidden;

      .web-logo {
        width: 32px;
        height: 32px;
        transition: all 0.28s;
      }

      .title-text {
        margin-left: $spacing-sm;
        font-size: $sidebar-logo-font-size;
        font-weight: $sidebar-logo-font-weight;
        color: $sidebar-logo-font-color;
        white-space: nowrap;
        transition: all 0.28s;
        opacity: 1;
      }

      .collapsed & {
        width: $sidebar-collapsed-width;

        .title-text {
          opacity: 0;
          width: 0;
          height: 0;
          overflow: hidden;
        }
      }
    }

    .sidebar-scrollbar {
      height: calc(100% - #{$sidebar-logo-height});
    }
  }
</style>
