<template>
  <div class="app-layout">
    <Header />
    <div class="app-main">
      <Sidebar />
      <div class="content-wrapper">
        <div class="main-container">
          <div class="navbar-container">
            <TagsView />
          </div>
          <div class="view-container">
            <router-view v-slot="{ Component }">
              <transition name="fade-transform" mode="out-in">
                <keep-alive :include="cachedViews">
                  <component :is="Component" />
                </keep-alive>
              </transition>
            </router-view>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
  import { computed } from 'vue';
  import Header from './Header.vue';
  import Sidebar from './Sidebar.vue';
  import Breadcrumb from './Breadcrumb.vue';
  import TagsView from './TagsView.vue';
  import { useTagsViewStore } from '@/stores/tagsView';

  const tagsViewStore = useTagsViewStore();

  // 缓存的页面
  const cachedViews = computed(() => tagsViewStore.cachedViews);
</script>

<style scoped lang="scss">
  .app-layout {
    display: flex;
    flex-direction: column;
    height: 100vh;
    width: 100%;
    overflow: hidden;
  }

  .app-main {
    display: flex;
    flex: 1;
    overflow: hidden;
  }

  .content-wrapper {
    flex: 1;
    display: flex;
    flex-direction: column;
    overflow: hidden;
  }

  .main-container {
    flex: 1;
    display: flex;
    flex-direction: column;
    overflow: hidden;
  }

  .navbar-container {
    background: #fff;
    box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  }

  .view-container {
    flex: 1;
    overflow-y: auto;
    background-color: #f2f3f5;
    padding: 20px;
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
