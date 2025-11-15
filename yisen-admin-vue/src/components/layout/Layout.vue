<template>
  <div class="app-layout">
    <Sidebar />
    <div class="main-container">
      <Header />
      <div class="tags-container">
        <TagsView />
      </div>
      <el-scrollbar class="view-container">
        <router-view v-slot="{ Component }">
          <transition name="fade-transform" mode="out-in">
            <keep-alive :include="cachedViews">
              <component :is="Component" />
            </keep-alive>
          </transition>
        </router-view>
      </el-scrollbar>
    </div>
  </div>
</template>

<script setup>
  import { computed } from 'vue';
  import Header from './Header.vue';
  import Sidebar from './Sidebar.vue';
  import TagsView from './TagsView.vue';
  import { useTagsViewStore } from '@/stores/tagsView';

  const tagsViewStore = useTagsViewStore();

  // 缓存的页面
  const cachedViews = computed(() => tagsViewStore.cachedViews);
</script>

<style scoped lang="scss">
  .app-layout {
    display: flex;
    height: 100vh;
    width: 100%;
    overflow: hidden;

    .main-container {
      flex: 1;
      display: flex;
      flex-direction: column;
      overflow: hidden;

      .tags-container {
        background: #fff;
        box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
      }

      .view-container {
        flex: 1;
        background-color: #f2f3f5;

        // Element Plus 滚动条样式
        :deep(.el-scrollbar__wrap) {
          overflow-x: hidden;
        }

        // 自定义滚动条样式（可选）
        :deep(.el-scrollbar__bar) {
          opacity: 0.3;
          transition: opacity 0.3s;
        }

        &:hover :deep(.el-scrollbar__bar) {
          opacity: 0.7;
        }
      }
    }
  }

  /* 页面过渡动画 */
  .fade-transform-enter-active,
  .fade-transform-leave-active {
    transition: all 0.3s;
  }

  .fade-transform-enter-from {
    opacity: 0;
    transform: translateX(-30px);
  }

  .fade-transform-leave-to {
    opacity: 0;
    transform: translateX(30px);
  }
</style>
