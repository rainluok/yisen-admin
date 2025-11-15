<template>
  <template v-if="!item?.hidden">
    <!-- 有子菜单 -->
    <el-sub-menu
      v-if="item.children && item.children.filter((child) => child.type === 1).length > 0"
      :index="resolvePath(item.path)"
      class="sidebar-sub-menu"
    >
      <template #title>
        <el-icon v-if="item?.icon" class="sidebar-icon">
          <component :is="item.icon" />
        </el-icon>
        <span class="sidebar-title">{{ item?.menuName }}</span>
      </template>

      <sidebar-item v-for="child in item.children" :key="child.path" :item="child" :base-path="resolvePath(item.path)" />
    </el-sub-menu>

    <!-- 无子菜单 -->
    <el-menu-item v-else :index="resolvePath(item.path)" class="sidebar-menu-item">
      <el-icon v-if="item?.icon" class="sidebar-icon">
        <component :is="item.icon" />
      </el-icon>
      <template #title>
        <span class="sidebar-title">{{ item?.menuName }}</span>
      </template>
    </el-menu-item>
  </template>
</template>

<script setup>
  const props = defineProps({
    item: {
      type: Object,
      required: true,
    },
    basePath: {
      type: String,
      default: '',
    },
  });

  // 判断是否为外部链接
  const isExternalLink = (path) => {
    return /^(https?:|mailto:|tel:)/.test(path);
  };

  // 简单JS实现路径拼接
  function joinPaths(base, relative) {
    if (!base) return relative || '';
    if (!relative) return base;
    // 如果relative是绝对路径
    if (relative.startsWith('/')) {
      return relative;
    }
    // 去掉base结尾的/
    const b = base.replace(/\/+$/, '');
    // 去掉relative开头的/
    const r = relative.replace(/^\/+/, '');
    return `${b}/${r}`;
  }

  // 解析路径
  const resolvePath = (routePath) => {
    if (isExternalLink(routePath)) {
      return routePath;
    }
    if (isExternalLink(props.basePath)) {
      return props.basePath;
    }
    return joinPaths(props.basePath, routePath);
  };
</script>

<style scoped lang="scss">
  @import '@/styles/variables.scss';

  .sidebar-sub-menu,
  .sidebar-menu-item {
    .sidebar-icon {
      width: 16px;
      height: 16px;
      margin-right: $spacing-sm;
      vertical-align: middle;
    }

    .sidebar-title {
      font-size: $font-size-base;
      transition: opacity 0.28s;
    }
  }
</style>
