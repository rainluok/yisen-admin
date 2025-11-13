<template>
  <div class="task-detail-container">
    <el-card shadow="never" v-loading="loading">
      <template #header>
        <div class="card-header">
          <span>任务详情</span>
          <div>
            <el-button @click="goBack">返回</el-button>
            <el-button type="primary" @click="handleEdit">编辑</el-button>
          </div>
        </div>
      </template>

      <el-descriptions :column="2" border>
        <el-descriptions-item label="任务ID">
          {{ taskDetail.id }}
        </el-descriptions-item>
        <el-descriptions-item label="任务名称">
          {{ taskDetail.title }}
        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusType(taskDetail.status)">
            {{ getStatusText(taskDetail.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="优先级">
          <el-tag :type="getPriorityType(taskDetail.priority)">
            {{ getPriorityText(taskDetail.priority) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="负责人">
          {{ taskDetail.assignee }}
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">
          {{ taskDetail.createTime }}
        </el-descriptions-item>
        <el-descriptions-item label="截止日期">
          {{ taskDetail.deadline }}
        </el-descriptions-item>
        <el-descriptions-item label="完成时间">
          {{ taskDetail.completeTime || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="任务描述" :span="2">
          {{ taskDetail.description || '-' }}
        </el-descriptions-item>
      </el-descriptions>

      <el-divider />

      <!-- 操作按钮 -->
      <div class="action-buttons">
        <el-button
          v-if="taskDetail.status === TASK_STATUS.PENDING"
          type="primary"
          @click="handleUpdateStatus(TASK_STATUS.IN_PROGRESS)"
        >
          开始任务
        </el-button>
        <el-button
          v-if="taskDetail.status === TASK_STATUS.IN_PROGRESS"
          type="success"
          @click="handleUpdateStatus(TASK_STATUS.DONE)"
        >
          完成任务
        </el-button>
        <el-button
          v-if="taskDetail.status !== TASK_STATUS.CANCELLED"
          type="danger"
          @click="handleUpdateStatus(TASK_STATUS.CANCELLED)"
        >
          取消任务
        </el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getTaskDetail, updateTaskStatus } from '@/api/task'
import {
  TASK_STATUS,
  TASK_STATUS_TEXT,
  TASK_PRIORITY,
  TASK_PRIORITY_TEXT
} from '@/constants/status'

const route = useRoute()
const router = useRouter()

const loading = ref(false)
const taskDetail = ref({})

// 获取任务详情
const fetchTaskDetail = async () => {
  loading.value = true
  try {
    const id = route.params.id
    const data = await getTaskDetail(id)
    taskDetail.value = data
  } catch (error) {
    ElMessage.error('获取任务详情失败')
  } finally {
    loading.value = false
  }
}

// 返回
const goBack = () => {
  router.back()
}

// 编辑
const handleEdit = () => {
  router.push(`/task/edit/${taskDetail.value.id}`)
}

// 更新状态
const handleUpdateStatus = async (status) => {
  try {
    await updateTaskStatus(taskDetail.value.id, status)
    ElMessage.success('状态更新成功')
    fetchTaskDetail()
  } catch (error) {
    ElMessage.error('状态更新失败')
  }
}

// 获取优先级类型
const getPriorityType = (priority) => {
  const typeMap = {
    [TASK_PRIORITY.LOW]: 'info',
    [TASK_PRIORITY.NORMAL]: '',
    [TASK_PRIORITY.HIGH]: 'warning',
    [TASK_PRIORITY.URGENT]: 'danger'
  }
  return typeMap[priority] || ''
}

// 获取优先级文本
const getPriorityText = (priority) => {
  return TASK_PRIORITY_TEXT[priority] || '未知'
}

// 获取状态类型
const getStatusType = (status) => {
  const typeMap = {
    [TASK_STATUS.PENDING]: '',
    [TASK_STATUS.IN_PROGRESS]: 'primary',
    [TASK_STATUS.DONE]: 'success',
    [TASK_STATUS.CANCELLED]: 'danger'
  }
  return typeMap[status] || ''
}

// 获取状态文本
const getStatusText = (status) => {
  return TASK_STATUS_TEXT[status] || '未知'
}

onMounted(() => {
  fetchTaskDetail()
})
</script>

<style scoped lang="scss">
.task-detail-container {
  padding: 20px;

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-weight: bold;
  }

  .action-buttons {
    display: flex;
    gap: 12px;
    margin-top: 20px;
  }
}
</style>

