<template>
  <div class="sidebar-container" :class="{ collapsed: !sidebarOpened }">
    <el-scrollbar>
      <el-menu
        :default-active="activeMenu"
        :collapse="!sidebarOpened"
        :collapse-transition="false"
        :unique-opened="true"
        router
      >
        <sidebar-item
          v-for="route in menuRoutes"
          :key="route.path"
          :item="route"
          :base-path="route.path"
        />
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<script setup>
import { computed } from "vue";
import { useRoute, useRouter } from "vue-router";
import { useAppStore } from "@/stores/appStore";
import { useUserStore } from "@/stores/userStore";
import SidebarItem from "./SidebarItem.vue";

const route = useRoute();
const router = useRouter();
const appStore = useAppStore();
const userStore = useUserStore();

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
  const routes = router.getRoutes();
  return routes.filter((route) => {
    // 过滤隐藏的路由
    if (route.meta?.hidden) {
      return false;
    }

    // 检查权限
    if (route.meta?.permission) {
      return userStore.hasPermission(route.meta.permission);
    }

    return true;
  });
});
</script>

<style scoped lang="scss">
.sidebar-container {
  width: 200px;
  height: 100%;
  background-color: #304156;
  transition: width 0.28s;
  overflow: hidden;

  &.collapsed {
    width: 64px;
  }

  :deep(.el-scrollbar) {
    height: 100%;
  }

  :deep(.el-menu) {
    border-right: none;
    background-color: #304156;
  }

  :deep(.el-menu-item),
  :deep(.el-submenu__title) {
    color: #bfcbd9;

    &:hover {
      background-color: #263445 !important;
    }
  }

  :deep(.el-menu-item.is-active) {
    background-color: #409eff !important;
    color: #fff;
  }

  :deep(.el-submenu.is-active > .el-submenu__title) {
    color: #409eff;
  }
}
</style>
