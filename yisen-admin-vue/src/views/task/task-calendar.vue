<template>
  <div class="task-calendar-container">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span>任务日历</span>
          <el-button type="primary" size="small" @click="gotoList">
            列表视图
          </el-button>
        </div>
      </template>

      <task-calendar :tasks="tasks" />
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import TaskCalendar from '@/components/business/TaskCalendar.vue'
import { getTaskList } from '@/api/task'

const router = useRouter()

// 任务数据
const tasks = ref([])

// 获取任务列表
const fetchTasks = async () => {
  try {
    const data = await getTaskList({ page: 1, pageSize: 100 })
    tasks.value = data.list || []
  } catch (error) {
    console.error('获取任务列表失败:', error)
  }
}

// 跳转到列表视图
const gotoList = () => {
  router.push('/task/list')
}

onMounted(() => {
  fetchTasks()
})
</script>

<style scoped lang="scss">
.task-calendar-container {
  padding: 20px;

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-weight: bold;
  }
}
</style>

