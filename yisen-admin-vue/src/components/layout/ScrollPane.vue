<template>
  <el-scrollbar ref="scrollContainer" :vertical="false" class="scroll-container" @wheel.prevent="handleScroll">
    <slot />
  </el-scrollbar>
</template>

<script setup>
  import { getCurrentInstance, nextTick, onBeforeUnmount, onMounted, ref } from 'vue';

  const { proxy } = getCurrentInstance();

  const scrollContainer = ref(null);
  const tagSpacing = 4; // 标签间距

  defineExpose({
    moveToTarget,
  });

  /**
   * 滚动到指定标签
   */
  function moveToTarget(currentTag) {
    const container = scrollContainer.value.$el.querySelector('.el-scrollbar__wrap');
    const containerWidth = container.offsetWidth;
    const tagList = proxy.$parent.$refs.scrollPaneRef.$el.querySelectorAll('.tags-view-item');

    let firstTag = null;
    let lastTag = null;
    let prevTag = null;
    let nextTag = null;

    // 找到第一个、最后一个、前一个和后一个标签
    for (const [i, tag] of tagList.entries()) {
      if (i === 0) firstTag = tag;
      if (i === tagList.length - 1) lastTag = tag;

      if (tag.classList.contains('active')) {
        if (i > 0) prevTag = tagList[i - 1];
        if (i < tagList.length - 1) nextTag = tagList[i + 1];
      }
    }

    if (firstTag === currentTag) {
      container.scrollLeft = 0;
    } else if (lastTag === currentTag) {
      container.scrollLeft = container.scrollWidth - containerWidth;
    } else {
      // 标签在中间
      const tagListDom = proxy.$parent.$refs.scrollPaneRef.$el;
      const currentTagElement = tagListDom.querySelector('.active');
      const currentTagOffsetLeft = currentTagElement.offsetLeft;
      const currentTagOffsetWidth = currentTagElement.offsetWidth;

      if (currentTagOffsetLeft < container.scrollLeft) {
        // 标签在视口左侧
        if (prevTag) {
          const prevTagOffsetLeft = prevTag.offsetLeft;
          container.scrollLeft = prevTagOffsetLeft - tagSpacing;
        }
      } else if (currentTagOffsetLeft + currentTagOffsetWidth > container.scrollLeft + containerWidth) {
        // 标签在视口右侧
        if (nextTag) {
          const nextTagOffsetLeft = nextTag.offsetLeft;
          const nextTagOffsetWidth = nextTag.offsetWidth;
          container.scrollLeft = nextTagOffsetLeft + nextTagOffsetWidth - containerWidth + tagSpacing;
        }
      }
    }
  }

  /**
   * 鼠标滚轮处理
   */
  function handleScroll(e) {
    const eventDelta = e.wheelDelta || -e.deltaY * 40;
    const container = scrollContainer.value.$el.querySelector('.el-scrollbar__wrap');
    container.scrollLeft = container.scrollLeft + eventDelta / 4;
  }
</script>

<style lang="scss" scoped>
  .scroll-container {
    white-space: nowrap;
    position: relative;
    overflow: hidden;
    width: 100%;

    :deep(.el-scrollbar__bar) {
      bottom: 0px;
    }

    :deep(.el-scrollbar__wrap) {
      height: 49px;
    }
  }
</style>
