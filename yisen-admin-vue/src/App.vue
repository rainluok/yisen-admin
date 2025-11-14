<script setup>
  import { ref, onMounted, onUnmounted } from 'vue';
  import { ElConfigProvider, ElLoading } from 'element-plus';
  import zhCn from 'element-plus/es/locale/lang/zh-cn';
  import { useRouter } from 'vue-router';

  const loading = ref(false);
  let loadingInstance = null;

  const router = useRouter();

  // 路由全局 loading 处理
  const startLoading = () => {
    if (!loadingInstance) {
      loading.value = true;
      loadingInstance = ElLoading.service({
        fullscreen: true,
        text: '加载中...',
        background: 'rgba(255, 255, 255, 0.5)',
      });
    }
  };

  const endLoading = () => {
    loading.value = false;
    if (loadingInstance) {
      loadingInstance.close();
      loadingInstance = null;
    }
  };

  onMounted(() => {
    router.beforeEach((to, from, next) => {
      startLoading();
      next();
    });
    router.afterEach(() => {
      endLoading();
    });
  });

  onUnmounted(() => {
    endLoading();
  });
</script>

<template>
  <el-config-provider :locale="zhCn">
    <router-view />
  </el-config-provider>
</template>

<style scoped></style>
