<template>
  <div class="error-container">
    <div class="error-content">
      <div class="error-code">404</div>
      <div class="error-title">页面不存在</div>
      <div class="error-description">抱歉，您访问的页面不存在或已被删除</div>
      <el-button type="primary" @click="goBack">返回上一页</el-button>
      <el-button @click="goHome">返回首页</el-button>
    </div>
  </div>
</template>

<script setup>
  import { useRouter } from 'vue-router';

  const router = useRouter();

  const goBack = () => {
    router.go(-1);
  };

  const goHome = () => {
    router.push('/');
  };
</script>

<style scoped lang="scss">
  .error-container {
    display: flex;
    align-items: center;
    justify-content: center;
    min-height: 100vh;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  }

  .error-content {
    text-align: center;
    color: #fff;
  }

  .error-code {
    font-size: 120px;
    font-weight: bold;
    line-height: 1;
    margin-bottom: 20px;
  }

  .error-title {
    font-size: 32px;
    font-weight: 500;
    margin-bottom: 16px;
  }

  .error-description {
    font-size: 16px;
    margin-bottom: 32px;
    opacity: 0.9;
  }
</style>
