<template>
  <div class="user-list-container">
    <el-card shadow="never">
      <template #header>
        <span>用户列表</span>
      </template>

      <!-- 搜索表单 -->
      <el-form :model="queryForm" class="search-form" inline>
        <el-form-item label="用户名">
          <el-input v-model="queryForm.username" placeholder="请输入用户名" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryForm.status" placeholder="请选择状态" clearable>
            <el-option v-for="(text, value) in USER_STATUS_TEXT" :key="value" :label="text" :value="Number(value)" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 表格 -->
      <el-table v-loading="loading" :data="tableData" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="username" label="用户名" width="150" />
        <el-table-column prop="name" label="姓名" width="120" />
        <el-table-column prop="email" label="邮箱" min-width="200" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pagination.page"
          v-model:page-size="pagination.pageSize"
          :total="pagination.total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
  import { ref, reactive, onMounted } from 'vue';
  import { ElMessage } from 'element-plus';
  import { getUserList } from '@/api/user';
  import { USER_STATUS, USER_STATUS_TEXT } from '@/constants/status';

  const queryForm = reactive({
    username: '',
    status: null,
  });

  const tableData = ref([]);
  const loading = ref(false);

  const pagination = reactive({
    page: 1,
    pageSize: 10,
    total: 0,
  });

  const fetchUserList = async () => {
    loading.value = true;
    try {
      const params = {
        ...queryForm,
        page: pagination.page,
        pageSize: pagination.pageSize,
      };
      const data = await getUserList(params);
      tableData.value = data.list || [];
      pagination.total = data.total || 0;
    } catch (error) {
      ElMessage.error('获取用户列表失败');
    } finally {
      loading.value = false;
    }
  };

  const handleSearch = () => {
    pagination.page = 1;
    fetchUserList();
  };

  const handleReset = () => {
    Object.assign(queryForm, {
      username: '',
      status: null,
    });
    handleSearch();
  };

  const handleSizeChange = () => {
    fetchUserList();
  };

  const handleCurrentChange = () => {
    fetchUserList();
  };

  const getStatusType = (status) => {
    const typeMap = {
      [USER_STATUS.INACTIVE]: 'info',
      [USER_STATUS.ACTIVE]: 'success',
      [USER_STATUS.DISABLED]: 'danger',
    };
    return typeMap[status] || '';
  };

  const getStatusText = (status) => {
    return USER_STATUS_TEXT[status] || '未知';
  };

  onMounted(() => {
    fetchUserList();
  });
</script>

<style scoped lang="scss">
  .user-list-container {
    padding: 20px;

    .search-form {
      margin-bottom: 20px;
    }

    .pagination-container {
      margin-top: 20px;
      display: flex;
      justify-content: flex-end;
    }
  }
</style>
