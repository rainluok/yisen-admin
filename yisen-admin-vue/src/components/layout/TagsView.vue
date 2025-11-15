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
        <span class="tag-title">{{ tag.title }}</span>
        <el-icon
          v-if="!isAffix(tag)"
          class="close-icon"
          @click.prevent.stop="closeSelectedTag(tag)"
        >
          <Close />
        </el-icon>
      </router-link>
    </scroll-pane>

    <!-- 右键菜单 -->
    <ul
      v-show="visible"
      :style="{ left: left + 'px', top: top + 'px' }"
      class="contextmenu"
    >
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
import { computed, nextTick, onMounted, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useTagsViewStore } from '@/stores/tagsView'
import {
  Close,
  Refresh,
  CircleClose,
  Back,
  Right,
  CircleCloseFilled
} from '@element-plus/icons-vue'
import ScrollPane from './ScrollPane.vue'

const route = useRoute()
const router = useRouter()
const tagsViewStore = useTagsViewStore()

const visible = ref(false)
const top = ref(0)
const left = ref(0)
const selectedTag = ref({})
const scrollPaneRef = ref(null)

const visitedViews = computed(() => tagsViewStore.visitedViews)

/**
 * 判断是否是当前激活的标签
 */
const isActive = (tag) => {
  return tag.path === route.path
}

/**
 * 判断是否是固定标签
 */
const isAffix = (tag) => {
  return tag.affix
}

/**
 * 添加标签
 */
const addTags = () => {
  const { name } = route
  if (name && route.meta?.title) {
    tagsViewStore.addView(route)
  }
  return false
}

/**
 * 移动到当前标签
 */
const moveToCurrentTag = () => {
  nextTick(() => {
    const tags = visitedViews.value
    for (const tag of tags) {
      if (tag.path === route.path) {
        scrollPaneRef.value?.moveToTarget(tag)
        break
      }
    }
  })
}

/**
 * 刷新选中的标签
 */
const refreshSelectedTag = (view) => {
  tagsViewStore.delCachedView(view).then(() => {
    const { fullPath } = view
    nextTick(() => {
      router.replace({
        path: '/redirect' + fullPath,
      })
    })
  })
}

/**
 * 关闭选中的标签
 */
const closeSelectedTag = (view) => {
  tagsViewStore.delView(view).then(({ visitedViews }) => {
    if (isActive(view)) {
      toLastView(visitedViews, view)
    }
  })
}

/**
 * 关闭其他标签
 */
const closeOthersTags = () => {
  router.push(selectedTag.value)
  tagsViewStore.delOthersViews(selectedTag.value).then(() => {
    moveToCurrentTag()
  })
}

/**
 * 关闭左侧标签
 */
const closeLeftTags = () => {
  tagsViewStore.delLeftViews(selectedTag.value).then(() => {
    if (!visitedViews.value.find((v) => v.path === route.path)) {
      toLastView(visitedViews.value)
    }
  })
}

/**
 * 关闭右侧标签
 */
const closeRightTags = () => {
  tagsViewStore.delRightViews(selectedTag.value).then(() => {
    if (!visitedViews.value.find((v) => v.path === route.path)) {
      toLastView(visitedViews.value)
    }
  })
}

/**
 * 关闭所有标签
 */
const closeAllTags = (view) => {
  tagsViewStore.delAllViews().then(({ visitedViews }) => {
    if (isAffix(view)) {
      return
    }
    toLastView(visitedViews, view)
  })
}

/**
 * 跳转到最后一个标签
 */
const toLastView = (visitedViews, view) => {
  const latestView = visitedViews.slice(-1)[0]
  if (latestView) {
    router.push(latestView.fullPath)
  } else {
    // 如果没有标签了，跳转到首页
    if (view.name === 'Dashboard') {
      // 重新加载
      router.replace({ path: '/redirect' + view.fullPath })
    } else {
      router.push('/')
    }
  }
}

/**
 * 打开右键菜单
 */
const openMenu = (tag, e) => {
  const menuMinWidth = 105
  const offsetLeft = e.currentTarget.getBoundingClientRect().left
  const offsetWidth = e.currentTarget.offsetWidth
  const maxLeft = window.innerWidth - menuMinWidth
  const leftValue = offsetLeft + offsetWidth + 15

  if (leftValue > maxLeft) {
    left.value = maxLeft
  } else {
    left.value = leftValue
  }

  top.value = e.currentTarget.getBoundingClientRect().bottom
  visible.value = true
  selectedTag.value = tag
}

/**
 * 关闭右键菜单
 */
const closeMenu = () => {
  visible.value = false
}

/**
 * 滚动处理
 */
const handleScroll = () => {
  closeMenu()
}

// 监听路由变化
watch(
  route,
  () => {
    addTags()
    moveToCurrentTag()
  },
  { immediate: true }
)

// 监听点击事件，关闭右键菜单
watch(visible, (value) => {
  if (value) {
    document.body.addEventListener('click', closeMenu)
  } else {
    document.body.removeEventListener('click', closeMenu)
  }
})

onMounted(() => {
  addTags()
})
</script>

<style lang="scss" scoped>
@import '@/styles/variables.scss';

.tags-view-container {
  height: 34px;
  width: 100%;
  background: #fff;
  border-bottom: 1px solid $border-light;
  box-shadow: $box-shadow-light;
  position: relative;
  z-index: $z-index-header - 100;

  .tags-view-wrapper {
    .tags-view-item {
      display: inline-flex;
      align-items: center;
      position: relative;
      cursor: pointer;
      height: 26px;
      line-height: 26px;
      border: 1px solid $border-light;
      color: $text-regular;
      background: #fff;
      padding: 0 $spacing-sm;
      font-size: $font-size-sm;
      margin-left: 5px;
      margin-top: 4px;
      text-decoration: none;
      transition: $transition-base;
      border-radius: $border-radius-sm;

      &:first-of-type {
        margin-left: $spacing-md;
      }

      &:last-of-type {
        margin-right: $spacing-md;
      }

      &.active {
        background-color: $primary-color;
        color: #fff;
        border-color: $primary-color;

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
        font-size: $font-size-xs;
      }

      .tag-title {
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
      }

      .close-icon {
        width: 16px;
        height: 16px;
        vertical-align: middle;
        border-radius: 50%;
        margin-left: $spacing-xs;
        transition: $transition-base;
        font-size: $font-size-xs;
        display: flex;
        align-items: center;
        justify-content: center;

        &:hover {
          background-color: #b4bccc;
          color: #fff;
        }
      }

      &:hover {
        background-color: #f5f7fa;
        border-color: $border-base;

        &.active {
          background-color: $primary-color;
          border-color: $primary-color;
        }
      }
    }
  }

  .contextmenu {
    margin: 0;
    background: #fff;
    z-index: $z-index-modal;
    position: absolute;
    list-style-type: none;
    padding: $spacing-xs 0;
    border-radius: $border-radius-base;
    font-size: $font-size-sm;
    font-weight: $font-weight-normal;
    color: $text-primary;
    box-shadow: 2px 2px 3px 0 rgba(0, 0, 0, 0.3);

    li {
      margin: 0;
      padding: $spacing-sm $spacing-md;
      cursor: pointer;
      display: flex;
      align-items: center;

      .el-icon {
        margin-right: $spacing-xs;
        font-size: $font-size-sm;
      }

      &:hover {
        background: #f5f7fa;
      }
    }
  }
}
</style>
