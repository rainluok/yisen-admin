<template>
  <div class="header-container">
    <div class="header-left">
      <div class="toggle-btn" @click="toggleSidebar">
        <el-icon><Fold v-if="sidebarOpened" /><Expand v-else /></el-icon>
      </div>
      <div class="logo">
        <span>益森管理系统</span>
      </div>
    </div>

    <div class="header-right">
      <!-- 全屏按钮 -->
      <div class="header-icon" @click="toggleFullscreen">
        <el-icon><FullScreen /></el-icon>
      </div>

      <!-- 用户信息 -->
      <el-dropdown trigger="click" @command="handleCommand">
        <div class="user-info">
          <el-avatar :size="32" :src="userStore.avatar">
            {{ userStore.name?.charAt(0) }}
          </el-avatar>
          <span class="user-name">{{ userStore.name }}</span>
          <el-icon><ArrowDown /></el-icon>
        </div>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="profile">
              <el-icon><User /></el-icon>
              个人中心
            </el-dropdown-item>
            <el-dropdown-item command="settings">
              <el-icon><Setting /></el-icon>
              设置
            </el-dropdown-item>
            <el-dropdown-item divided command="logout">
              <el-icon><SwitchButton /></el-icon>
              退出登录
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script setup>
import { computed } from "vue";
import { useRouter } from "vue-router";
import { ElMessage, ElMessageBox } from "element-plus";
import {
  Fold,
  Expand,
  FullScreen,
  ArrowDown,
  User,
  Setting,
  SwitchButton,
} from "@element-plus/icons-vue";
import { useAppStore } from "@/stores/appStore";
import { useUserStore } from "@/stores/userStore";

const router = useRouter();
const appStore = useAppStore();
const userStore = useUserStore();

// 侧边栏状态
const sidebarOpened = computed(() => appStore.sidebarOpened);

// 切换侧边栏
const toggleSidebar = () => {
  appStore.toggleSidebar();
};

// 全屏切换
const toggleFullscreen = () => {
  if (!document.fullscreenElement) {
    document.documentElement.requestFullscreen();
  } else {
    if (document.exitFullscreen) {
      document.exitFullscreen();
    }
  }
};

// 下拉菜单命令处理
const handleCommand = (command) => {
  switch (command) {
    case "profile":
      router.push("/user/profile");
      break;
    case "settings":
      router.push("/settings");
      break;
    case "logout":
      handleLogout();
      break;
  }
};

// 退出登录
const handleLogout = () => {
  ElMessageBox.confirm("确定要退出登录吗？", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  })
    .then(async () => {
      await userStore.logout();
      ElMessage.success("退出成功");
      router.push("/login");
    })
    .catch(() => {});
};
</script>

<style scoped lang="scss">
.header-container {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 60px;
  padding: 0 20px;
  background-color: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  z-index: 1000;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 20px;
}

.toggle-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  cursor: pointer;
  border-radius: 4px;
  transition: background-color 0.3s;

  &:hover {
    background-color: #f5f7fa;
  }

  .el-icon {
    font-size: 20px;
  }
}

.logo {
  font-size: 18px;
  font-weight: bold;
  color: #303133;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 20px;
}

.header-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  cursor: pointer;
  border-radius: 4px;
  transition: background-color 0.3s;

  &:hover {
    background-color: #f5f7fa;
  }

  .el-icon {
    font-size: 20px;
  }
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 4px 12px;
  cursor: pointer;
  border-radius: 4px;
  transition: background-color 0.3s;

  &:hover {
    background-color: #f5f7fa;
  }
}

.user-name {
  font-size: 14px;
  color: #303133;
}
</style>
