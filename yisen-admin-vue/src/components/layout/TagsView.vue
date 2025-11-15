<template>
  <div class="tags-view-container">
    <scroll-pane ref="scrollPaneRef" class="tags-view-wrapper" @scroll="handleScroll">
      <router-link
        v-for="tag in visitedViews"
        :key="tag.path"
        :class="isActive(tag) ? 'active' : ''"
        :to="{ path: tag.path, query: tag.query, fullPath: tag.fullPath }"
        class="tags-view-item"
        @click.middle="!isAffix(tag) ? closeSelectedTag(tag) : ''"
        @contextmenu.prevent="openMenu(tag, $event)"
      >
        <el-icon v-if="tag.icon" class="tag-icon">
          <component :is="tag.icon" />
        </el-icon>
        {{ tag.title }}
        <el-icon v-if="!isAffix(tag)" class="close-icon" @click.prevent.stop="closeSelectedTag(tag)">
          <Close />
        </el-icon>
      </router-link>
    </scroll-pane>

    <!-- 右键菜单 -->
    <ul v-show="visible" :style="{ left: left + 'px', top: top + 'px' }" class="contextmenu">
      <li @click="refreshSelectedTag(selectedTag)">
        <el-icon><Refresh /></el-icon>
        刷新
      </li>
      <li v-if="!isAffix(selectedTag)" @click="closeSelectedTag(selectedTag)">
        <el-icon><Close /></el-icon>
        关闭
      </li>
      <li @click="closeOthersTags">
        <el-icon><CircleClose /></el-icon>
        关闭其他
      </li>
      <li @click="closeLeftTags">
        <el-icon><Back /></el-icon>
        关闭左侧
      </li>
      <li @click="closeRightTags">
        <el-icon><Right /></el-icon>
        关闭右侧
      </li>
      <li @click="closeAllTags(selectedTag)">
        <el-icon><CircleCloseFilled /></el-icon>
        关闭所有
      </li>
    </ul>
  </div>
</template>

<script setup>
  import { computed, nextTick, onMounted, ref, watch } from 'vue';
  import { useRoute, useRouter } from 'vue-router';
  import { useTagsViewStore } from '@/stores/tagsView';
  import { Close, Refresh, CircleClose, Back, Right, CircleCloseFilled } from '@element-plus/icons-vue';
  import ScrollPane from './ScrollPane.vue';

  const route = useRoute();
  const router = useRouter();
  const tagsViewStore = useTagsViewStore();

  const visible = ref(false);
  const top = ref(0);
  const left = ref(0);
  const selectedTag = ref({});
  const scrollPaneRef = ref(null);

  const visitedViews = computed(() => tagsViewStore.visitedViews);

  /**
   * 判断是否是当前激活的标签
   */
  const isActive = (tag) => {
    return tag.path === route.path;
  };

  /**
   * 判断是否是固定标签
   */
  const isAffix = (tag) => {
    return tag.affix;
  };

  /**
   * 添加标签
   */
  const addTags = () => {
    const { name } = route;
    if (name && route.meta?.title) {
      tagsViewStore.addView(route);
    }
    return false;
  };

  /**
   * 移动到当前标签
   */
  const moveToCurrentTag = () => {
    nextTick(() => {
      const tags = visitedViews.value;
      for (const tag of tags) {
        if (tag.path === route.path) {
          scrollPaneRef.value?.moveToTarget(tag);
          break;
        }
      }
    });
  };

  /**
   * 刷新选中的标签
   */
  const refreshSelectedTag = (view) => {
    tagsViewStore.delCachedView(view).then(() => {
      const { fullPath } = view;
      nextTick(() => {
        router.replace({
          path: '/redirect' + fullPath,
        });
      });
    });
  };

  /**
   * 关闭选中的标签
   */
  const closeSelectedTag = (view) => {
    tagsViewStore.delView(view).then(({ visitedViews }) => {
      if (isActive(view)) {
        toLastView(visitedViews, view);
      }
    });
  };

  /**
   * 关闭其他标签
   */
  const closeOthersTags = () => {
    router.push(selectedTag.value);
    tagsViewStore.delOthersViews(selectedTag.value).then(() => {
      moveToCurrentTag();
    });
  };

  /**
   * 关闭左侧标签
   */
  const closeLeftTags = () => {
    tagsViewStore.delLeftViews(selectedTag.value).then(() => {
      if (!visitedViews.value.find((v) => v.path === route.path)) {
        toLastView(visitedViews.value);
      }
    });
  };

  /**
   * 关闭右侧标签
   */
  const closeRightTags = () => {
    tagsViewStore.delRightViews(selectedTag.value).then(() => {
      if (!visitedViews.value.find((v) => v.path === route.path)) {
        toLastView(visitedViews.value);
      }
    });
  };

  /**
   * 关闭所有标签
   */
  const closeAllTags = (view) => {
    tagsViewStore.delAllViews().then(({ visitedViews }) => {
      if (isAffix(view)) {
        return;
      }
      toLastView(visitedViews, view);
    });
  };

  /**
   * 跳转到最后一个标签
   */
  const toLastView = (visitedViews, view) => {
    const latestView = visitedViews.slice(-1)[0];
    if (latestView) {
      router.push(latestView.fullPath);
    } else {
      // 如果没有标签了，跳转到首页
      if (view.name === 'Dashboard') {
        // 重新加载
        router.replace({ path: '/redirect' + view.fullPath });
      } else {
        router.push('/');
      }
    }
  };

  /**
   * 打开右键菜单
   */
  const openMenu = (tag, e) => {
    const menuMinWidth = 105;
    const offsetLeft = e.currentTarget.getBoundingClientRect().left;
    const offsetWidth = e.currentTarget.offsetWidth;
    const maxLeft = window.innerWidth - menuMinWidth;
    const leftValue = offsetLeft + offsetWidth + 15;

    if (leftValue > maxLeft) {
      left.value = maxLeft;
    } else {
      left.value = leftValue;
    }

    top.value = e.currentTarget.getBoundingClientRect().bottom;
    visible.value = true;
    selectedTag.value = tag;
  };

  /**
   * 关闭右键菜单
   */
  const closeMenu = () => {
    visible.value = false;
  };

  /**
   * 滚动处理
   */
  const handleScroll = () => {
    closeMenu();
  };

  // 监听路由变化
  watch(
    route,
    () => {
      addTags();
      moveToCurrentTag();
    },
    { immediate: true }
  );

  // 监听点击事件，关闭右键菜单
  watch(visible, (value) => {
    if (value) {
      document.body.addEventListener('click', closeMenu);
    } else {
      document.body.removeEventListener('click', closeMenu);
    }
  });

  onMounted(() => {
    addTags();
  });
</script>

<style lang="scss" scoped>
  .tags-view-container {
    height: 34px;
    width: 100%;
    background: #fff;
    border-bottom: 1px solid #d8dce5;
    box-shadow:
      0 1px 3px 0 rgba(0, 0, 0, 0.12),
      0 0 3px 0 rgba(0, 0, 0, 0.04);

    .tags-view-wrapper {
      .tags-view-item {
        display: inline-block;
        position: relative;
        cursor: pointer;
        height: 26px;
        line-height: 26px;
        border: 1px solid #d8dce5;
        color: #495060;
        background: #fff;
        padding: 0 8px;
        font-size: 12px;
        margin-left: 5px;
        margin-top: 4px;
        text-decoration: none;
        transition: all 0.3s;

        &:first-of-type {
          margin-left: 15px;
        }

        &:last-of-type {
          margin-right: 15px;
        }

        &.active {
          background-color: #409eff;
          color: #fff;
          border-color: #409eff;

          &::before {
            content: '';
            background: #fff;
            display: inline-block;
            width: 8px;
            height: 8px;
            border-radius: 50%;
            position: relative;
            margin-right: 4px;
          }
        }

        .tag-icon {
          margin-right: 2px;
          vertical-align: middle;
        }

        .close-icon {
          width: 16px;
          height: 16px;
          vertical-align: middle;
          border-radius: 50%;
          margin-left: 2px;
          transition: all 0.3s cubic-bezier(0.645, 0.045, 0.355, 1);

          &:hover {
            background-color: #b4bccc;
            color: #fff;
          }
        }
      }
    }

    .contextmenu {
      margin: 0;
      background: #fff;
      z-index: 3000;
      position: absolute;
      list-style-type: none;
      padding: 5px 0;
      border-radius: 4px;
      font-size: 12px;
      font-weight: 400;
      color: #333;
      box-shadow: 2px 2px 3px 0 rgba(0, 0, 0, 0.3);

      li {
        margin: 0;
        padding: 7px 16px;
        cursor: pointer;
        display: flex;
        align-items: center;

        .el-icon {
          margin-right: 5px;
        }

        &:hover {
          background: #eee;
        }
      }
    }
  }
</style>
