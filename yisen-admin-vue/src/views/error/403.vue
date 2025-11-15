<template>
  <div class="error-container">
    <div class="error-content">
      <div class="error-code">403</div>
      <div class="error-title">无权限访问</div>
      <div class="error-description">抱歉，您没有权限访问此页面</div>
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
    background: linear-gradient(135deg, #fc466b 0%, #3f5efb 100%);
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
