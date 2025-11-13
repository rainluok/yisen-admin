<template>
  <div class="dashboard-container">
    <!-- 数据统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :xs="24" :sm="12" :lg="6">
        <el-card shadow="hover" class="stats-card">
          <div class="stats-content">
            <div class="stats-icon" style="background-color: #409eff">
              <el-icon><Document /></el-icon>
            </div>
            <div class="stats-info">
              <div class="stats-value">{{ statistics.totalTasks }}</div>
              <div class="stats-label">总任务数</div>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :xs="24" :sm="12" :lg="6">
        <el-card shadow="hover" class="stats-card">
          <div class="stats-content">
            <div class="stats-icon" style="background-color: #67c23a">
              <el-icon><CircleCheck /></el-icon>
            </div>
            <div class="stats-info">
              <div class="stats-value">{{ statistics.completedTasks }}</div>
              <div class="stats-label">已完成</div>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :xs="24" :sm="12" :lg="6">
        <el-card shadow="hover" class="stats-card">
          <div class="stats-content">
            <div class="stats-icon" style="background-color: #e6a23c">
              <el-icon><Clock /></el-icon>
            </div>
            <div class="stats-info">
              <div class="stats-value">{{ statistics.pendingTasks }}</div>
              <div class="stats-label">进行中</div>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :xs="24" :sm="12" :lg="6">
        <el-card shadow="hover" class="stats-card">
          <div class="stats-content">
            <div class="stats-icon" style="background-color: #f56c6c">
              <el-icon><WarningFilled /></el-icon>
            </div>
            <div class="stats-info">
              <div class="stats-value">{{ statistics.overdueTasks }}</div>
              <div class="stats-label">已逾期</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表和面板 -->
    <el-row :gutter="20" class="chart-row">
      <el-col :xs="24" :lg="16">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>任务趋势</span>
            </div>
          </template>
          <div class="chart-container">
            <div id="taskTrendChart" style="width: 100%; height: 300px"></div>
          </div>
        </el-card>
      </el-col>

      <el-col :xs="24" :lg="8">
        <score-panel
          title="积分统计"
          :total-score="scoreData.total"
          :score-items="scoreData.items"
          :progress="scoreData.progress"
          :trend-data="scoreData.trend"
          :show-trend="true"
          @refresh="fetchScoreData"
        />
      </el-col>
    </el-row>

    <!-- 最近任务 -->
    <el-row :gutter="20">
      <el-col :span="24">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>最近任务</span>
              <el-button type="primary" size="small" @click="gotoTaskList">
                查看全部
              </el-button>
            </div>
          </template>
          <el-table :data="recentTasks" style="width: 100%">
            <el-table-column prop="title" label="任务名称" min-width="200" />
            <el-table-column prop="priority" label="优先级" width="100">
              <template #default="{ row }">
                <el-tag :type="getPriorityType(row.priority)">
                  {{ getPriorityText(row.priority) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="status" label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="getStatusType(row.status)">
                  {{ getStatusText(row.status) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="createTime" label="创建时间" width="180" />
            <el-table-column label="操作" width="150">
              <template #default="{ row }">
                <el-button type="primary" size="small" @click="viewTask(row)">
                  查看
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import {
  Document,
  CircleCheck,
  Clock,
  WarningFilled
} from '@element-plus/icons-vue'
import ScorePanel from '@/components/business/ScorePanel.vue'
import { getTaskStatistics } from '@/api/task'
import {
  TASK_STATUS,
  TASK_STATUS_TEXT,
  TASK_PRIORITY,
  TASK_PRIORITY_TEXT
} from '@/constants/status'

const router = useRouter()

// 统计数据
const statistics = ref({
  totalTasks: 0,
  completedTasks: 0,
  pendingTasks: 0,
  overdueTasks: 0
})

// 积分数据
const scoreData = ref({
  total: 850,
  items: [
    { label: '任务完成', value: 500, color: '#67c23a' },
    { label: '按时完成', value: 200, color: '#409eff' },
    { label: '质量奖励', value: 150, color: '#e6a23c' }
  ],
  progress: 75,
  trend: [20, 40, 35, 60, 45, 70, 80]
})

// 最近任务
const recentTasks = ref([])

// 获取统计数据
const fetchStatistics = async () => {
  try {
    const data = await getTaskStatistics()
    statistics.value = data
  } catch (error) {
    console.error('获取统计数据失败:', error)
  }
}

// 获取积分数据
const fetchScoreData = async () => {
  // 实际项目中应该调用 API
  console.log('刷新积分数据')
}

// 获取最近任务
const fetchRecentTasks = async () => {
  // 实际项目中应该调用 API
  recentTasks.value = [
    {
      id: 1,
      title: '完成首页设计稿',
      priority: TASK_PRIORITY.HIGH,
      status: TASK_STATUS.IN_PROGRESS,
      createTime: '2024-01-15 10:30:00'
    },
    {
      id: 2,
      title: '优化数据库查询性能',
      priority: TASK_PRIORITY.URGENT,
      status: TASK_STATUS.PENDING,
      createTime: '2024-01-15 09:15:00'
    },
    {
      id: 3,
      title: '编写API文档',
      priority: TASK_PRIORITY.NORMAL,
      status: TASK_STATUS.DONE,
      createTime: '2024-01-14 16:20:00'
    }
  ]
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

// 查看任务
const viewTask = (task) => {
  router.push(`/task/detail/${task.id}`)
}

// 跳转到任务列表
const gotoTaskList = () => {
  router.push('/task/list')
}

// 初始化图表
const initChart = () => {
  // 实际项目中应该使用 ECharts 等图表库
  console.log('初始化图表')
}

onMounted(() => {
  fetchStatistics()
  fetchScoreData()
  fetchRecentTasks()
  initChart()
})
</script>

<style scoped lang="scss">
.dashboard-container {
  padding: 20px;

  .stats-row {
    margin-bottom: 20px;
  }

  .stats-card {
    margin-bottom: 20px;

    .stats-content {
      display: flex;
      align-items: center;
      gap: 20px;

      .stats-icon {
        display: flex;
        align-items: center;
        justify-content: center;
        width: 60px;
        height: 60px;
        border-radius: 8px;
        color: #fff;
        font-size: 24px;
      }

      .stats-info {
        flex: 1;

        .stats-value {
          font-size: 28px;
          font-weight: bold;
          color: #303133;
          line-height: 1;
          margin-bottom: 8px;
        }

        .stats-label {
          font-size: 14px;
          color: #909399;
        }
      }
    }
  }

  .chart-row {
    margin-bottom: 20px;
  }

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-weight: bold;
  }

  .chart-container {
    display: flex;
    align-items: center;
    justify-content: center;
  }
}
</style>

