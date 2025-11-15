<template>
  <div class="header-container">
    <div class="header-middle">
      <div class="toggle-btn" @click="toggleSidebar">
        <el-icon><Fold v-if="sidebarOpened" /><Expand v-else /></el-icon>
      </div>
      <Breadcrumb />
    </div>

    <div class="header-right">
      <!-- 全屏按钮 -->
      <div class="header-icon" @click="toggleFullscreen">
        <el-icon><FullScreen /></el-icon>
      </div>

      <!-- 用户信息 -->
      <el-dropdown trigger="click" @command="handleCommand">
        <div class="user-info">
          <el-avatar :size="36" :src="userStore.avatar" class="user-avatar">
            {{ userStore.name?.charAt(0) }}
          </el-avatar>
          <div class="user-details">
            <div class="user-name">{{ userStore.name }}</div>
            <div class="user-role">管理员</div>
          </div>
          <el-icon class="arrow-icon"><ArrowDown /></el-icon>
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
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { ArrowDown, Expand, Fold, FullScreen, Setting, SwitchButton, User } from '@element-plus/icons-vue'
import { useAppStore } from '@/stores/app.js'
import { useUserStore } from '@/stores/system/user.js'
import Breadcrumb from '@components/layout/Breadcrumb.vue'

const router = useRouter()
const appStore = useAppStore()
const userStore = useUserStore()

// 侧边栏状态
const sidebarOpened = computed(() => appStore.sidebarOpened)

// 切换侧边栏
const toggleSidebar = () => {
  appStore.toggleSidebar()
}

// 全屏切换
const toggleFullscreen = () => {
  if (!document.fullscreenElement) {
    document.documentElement.requestFullscreen()
  } else {
    if (document.exitFullscreen) {
      document.exitFullscreen()
    }
  }
}

// 下拉菜单命令处理
const handleCommand = (command) => {
  switch (command) {
    case 'profile':
      router.push('/user/profile')
      break
    case 'settings':
      router.push('/settings')
      break
    case 'logout':
      handleLogout()
      break
  }
}

// 退出登录
const handleLogout = () => {
  ElMessageBox.confirm('确定要退出登录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  })
    .then(async () => {
      await userStore.logout()
      ElMessage.success('退出成功')
      router.push('/login')
    })
    .catch(() => {})
}
</script>

<style scoped lang="scss">
@import '@/styles/variables.scss';

.header-container {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: $header-height;
  padding: 0 $spacing-lg;
  background: linear-gradient(90deg, $primary-color, darken($primary-color, 10%));
  box-shadow: $box-shadow-base;
  z-index: $z-index-header;
  color: #fff;
}

.header-middle {
  display: flex;
  align-items: center;
  justify-content: start;
  gap: $spacing-lg;
  flex: 1;
}

.toggle-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 44px;
  height: 44px;
  cursor: pointer;
  border-radius: 50%;
  transition: $transition-base;
  background-color: rgba(255, 255, 255, 0.15);

  &:hover {
    background-color: rgba(255, 255, 255, 0.25);
    transform: scale(1.05);
  }

  .el-icon {
    font-size: 20px;
    color: #fff;
  }
}

.header-right {
  display: flex;
  align-items: center;
  gap: $spacing-lg;
}

.header-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 44px;
  height: 44px;
  cursor: pointer;
  border-radius: 50%;
  transition: $transition-base;
  background-color: rgba(255, 255, 255, 0.15);

  &:hover {
    background-color: rgba(255, 255, 255, 0.25);
    transform: scale(1.05);
  }

  .el-icon {
    font-size: 20px;
    color: #fff;
  }
}

.user-info {
  display: flex;
  align-items: center;
  gap: $spacing-sm;
  padding: $spacing-xs $spacing-sm;
  cursor: pointer;
  border-radius: 30px;
  transition: $transition-base;
  background-color: rgba(255, 255, 255, 0.15);

  &:hover {
    background-color: rgba(255, 255, 255, 0.25);
  }
}

.user-avatar {
  background-color: #fff;
  color: $primary-color;
}

.user-details {
  display: flex;
  flex-direction: column;
}

.user-name {
  font-size: $font-size-base;
  font-weight: $font-weight-medium;
  color: #fff;
  line-height: 1.2;
}

.user-role {
  font-size: $font-size-xs;
  color: rgba(255, 255, 255, 0.8);
  line-height: 1.2;
}

.arrow-icon {
  font-size: 16px;
  color: #fff;
  transition: transform 0.3s;
}

.user-info:hover .arrow-icon {
  transform: rotate(180deg);
}
</style>
