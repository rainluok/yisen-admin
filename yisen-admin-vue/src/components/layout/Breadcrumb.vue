<template>
  <el-breadcrumb class="app-breadcrumb" separator="/">
    <transition-group name="breadcrumb">
      <el-breadcrumb-item v-for="(item, index) in breadcrumbs" :key="item.path">
        <span v-if="item.redirect === 'noRedirect' || index === breadcrumbs.length - 1" class="no-redirect">
          {{ item.meta.title }}
        </span>
        <a v-else @click.prevent="handleLink(item)">
          {{ item.meta.title }}
        </a>
      </el-breadcrumb-item>
    </transition-group>
  </el-breadcrumb>
</template>

<script setup>
import { ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()
const breadcrumbs = ref([])

/**
 * 获取面包屑
 */
const getBreadcrumb = () => {
  // 过滤掉隐藏的路由和没有标题的路由
  let matched = route.matched.filter((item) => {
    return item.meta && item.meta.title && !item.meta.breadcrumb === false
  })

  // 如果第一个不是首页，添加首页
  const first = matched[0]
  if (!first || first.path !== '/dashboard') {
    matched = [{ path: '/dashboard', meta: { title: '首页' } }].concat(matched)
  }

  breadcrumbs.value = matched.filter((item) => {
    return item.meta && item.meta.title && item.meta.breadcrumb !== false
  })
}

/**
 * 处理面包屑点击
 */
const handleLink = (item) => {
  const { redirect, path } = item
  if (redirect) {
    router.push(redirect)
    return
  }
  router.push(path)
}

// 监听路由变化
watch(
  () => route.path,
  () => {
    // 如果是登录页或重定向页，不显示面包屑
    if (route.path.startsWith('/login') || route.path.startsWith('/redirect')) {
      return
    }
    getBreadcrumb()
  },
  { immediate: true }
)
</script>

<style lang="scss" scoped>
@import '@/styles/variables.scss';

.app-breadcrumb {
  display: inline-block;
  font-size: $font-size-base;
  line-height: 50px;
  margin-left: $spacing-sm;

  .no-redirect {
    color: $text-secondary;
    cursor: text;
  }

  a {
    color: $text-regular;
    font-weight: $font-weight-normal;
    transition: $transition-base;

    &:hover {
      color: $primary-color;
    }
  }
}

// 面包屑动画
.breadcrumb-enter-active,
.breadcrumb-leave-active {
  transition: $transition-base;
}

.breadcrumb-enter-from {
  opacity: 0;
  transform: translateX(20px);
}

.breadcrumb-leave-to {
  opacity: 0;
  transform: translateX(-20px);
}

.breadcrumb-leave-active {
  position: absolute;
}
</style>
