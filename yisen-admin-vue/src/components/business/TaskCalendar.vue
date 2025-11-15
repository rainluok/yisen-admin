<template>
  <div class="task-calendar">
    <el-calendar v-model="currentDate">
      <template #header="{ date }">
        <div class="calendar-header">
          <el-button-group>
            <el-button size="small" @click="selectDate('prev-month')"> 上个月 </el-button>
            <el-button size="small" @click="selectDate('today')"> 今天 </el-button>
            <el-button size="small" @click="selectDate('next-month')"> 下个月 </el-button>
          </el-button-group>
          <span class="calendar-title">{{ date }}</span>
        </div>
      </template>

      <template #date-cell="{ data }">
        <div class="calendar-day" :class="{ 'is-selected': isSelected(data.day) }" @click="handleDateClick(data)">
          <div class="day-number">{{ data.day.split('-').slice(-1)[0] }}</div>
          <div v-if="getTasksForDate(data.day).length > 0" class="task-count">{{ getTasksForDate(data.day).length }} 个任务</div>
          <div class="task-list">
            <div v-for="task in getTasksForDate(data.day).slice(0, 3)" :key="task.id" class="task-item" :class="`task-status-${task.status}`">
              {{ task.title }}
            </div>
          </div>
        </div>
      </template>
    </el-calendar>

    <!-- 任务详情对话框 -->
    <el-dialog v-model="dialogVisible" :title="`${selectedDate} 的任务`" width="600px">
      <el-table :data="selectedTasks" style="width: 100%">
        <el-table-column prop="title" label="任务名称" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="viewTask(row)"> 查看 </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>

<script setup>
  import { computed, ref } from 'vue';
  import { useRouter } from 'vue-router';
  import { TASK_STATUS, TASK_STATUS_TEXT } from '@/constants/status';

  const router = useRouter();

  const props = defineProps({
    tasks: {
      type: Array,
      default: () => [],
    },
  });

  const currentDate = ref(new Date());
  const selectedDate = ref('');
  const dialogVisible = ref(false);

  // 选中日期的任务
  const selectedTasks = computed(() => {
    return getTasksForDate(selectedDate.value);
  });

  // 获取指定日期的任务
  const getTasksForDate = (date) => {
    return props.tasks.filter((task) => {
      const taskDate = new Date(task.date).toISOString().split('T')[0];
      return taskDate === date;
    });
  };

  // 判断日期是否被选中
  const isSelected = (date) => {
    return date === selectedDate.value;
  };

  // 选择日期
  const selectDate = (type) => {
    const date = new Date(currentDate.value);
    switch (type) {
      case 'prev-month':
        date.setMonth(date.getMonth() - 1);
        break;
      case 'next-month':
        date.setMonth(date.getMonth() + 1);
        break;
      case 'today':
        currentDate.value = new Date();
        return;
    }
    currentDate.value = date;
  };

  // 点击日期
  const handleDateClick = (data) => {
    selectedDate.value = data.day;
    const tasks = getTasksForDate(data.day);
    if (tasks.length > 0) {
      dialogVisible.value = true;
    }
  };

  // 查看任务详情
  const viewTask = (task) => {
    router.push(`/task/detail/${task.id}`);
  };

  // 获取状态类型
  const getStatusType = (status) => {
    const typeMap = {
      [TASK_STATUS.PENDING]: '',
      [TASK_STATUS.IN_PROGRESS]: 'primary',
      [TASK_STATUS.DONE]: 'success',
      [TASK_STATUS.CANCELLED]: 'danger',
    };
    return typeMap[status] || '';
  };

  // 获取状态文本
  const getStatusText = (status) => {
    return TASK_STATUS_TEXT[status] || '未知';
  };
</script>

<style scoped lang="scss">
  .task-calendar {
    :deep(.el-calendar) {
      background-color: #fff;
      border-radius: 4px;
    }

    .calendar-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 12px;
    }

    .calendar-title {
      font-size: 16px;
      font-weight: bold;
      color: #303133;
    }

    .calendar-day {
      min-height: 100px;
      padding: 8px;
      cursor: pointer;
      transition: background-color 0.3s;

      &:hover {
        background-color: #f5f7fa;
      }

      &.is-selected {
        background-color: #ecf5ff;
      }
    }

    .day-number {
      font-size: 14px;
      font-weight: bold;
      color: #303133;
      margin-bottom: 4px;
    }

    .task-count {
      font-size: 12px;
      color: #909399;
      margin-bottom: 8px;
    }

    .task-list {
      display: flex;
      flex-direction: column;
      gap: 4px;
    }

    .task-item {
      padding: 4px 8px;
      font-size: 12px;
      border-radius: 2px;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;

      &.task-status-0 {
        background-color: #f4f4f5;
        color: #909399;
      }

      &.task-status-1 {
        background-color: #ecf5ff;
        color: #409eff;
      }

      &.task-status-2 {
        background-color: #f0f9ff;
        color: #67c23a;
      }

      &.task-status-3 {
        background-color: #fef0f0;
        color: #f56c6c;
      }
    }
  }
</style>
