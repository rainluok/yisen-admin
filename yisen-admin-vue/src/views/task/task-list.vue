<template>
  <div class="task-list-container">
    <el-card shadow="never">
      <!-- 搜索表单 -->
      <el-form :model="queryForm" class="search-form" inline>
        <el-form-item label="任务名称">
          <el-input
            v-model="queryForm.title"
            placeholder="请输入任务名称"
            clearable
          />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryForm.status" placeholder="请选择状态" clearable>
            <el-option
              v-for="(text, value) in TASK_STATUS_TEXT"
              :key="value"
              :label="text"
              :value="Number(value)"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="优先级">
          <el-select v-model="queryForm.priority" placeholder="请选择优先级" clearable>
            <el-option
              v-for="(text, value) in TASK_PRIORITY_TEXT"
              :key="value"
              :label="text"
              :value="Number(value)"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>
            搜索
          </el-button>
          <el-button @click="handleReset">
            <el-icon><Refresh /></el-icon>
            重置
          </el-button>
        </el-form-item>
      </el-form>

      <!-- 操作按钮 -->
      <div class="toolbar">
        <el-button type="primary" @click="handleCreate">
          <el-icon><Plus /></el-icon>
          新建任务
        </el-button>
        <el-button @click="handleExport">
          <el-icon><Download /></el-icon>
          导出
        </el-button>
      </div>

      <!-- 表格 -->
      <el-table
        v-loading="loading"
        :data="tableData"
        style="width: 100%"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="id" label="ID" width="80" />
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
        <el-table-column prop="assignee" label="负责人" width="120" />
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleView(row)">
              查看
            </el-button>
            <el-button type="warning" size="small" @click="handleEdit(row)">
              编辑
            </el-button>
            <el-button type="danger" size="small" @click="handleDelete(row)">
              删除
            </el-button>
          </template>
        </el-table-column>
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

    <!-- 新建/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      @close="handleDialogClose"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="100px"
      >
        <el-form-item label="任务名称" prop="title">
          <el-input v-model="formData.title" placeholder="请输入任务名称" />
        </el-form-item>
        <el-form-item label="优先级" prop="priority">
          <el-select v-model="formData.priority" placeholder="请选择优先级">
            <el-option
              v-for="(text, value) in TASK_PRIORITY_TEXT"
              :key="value"
              :label="text"
              :value="Number(value)"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="负责人" prop="assignee">
          <el-input v-model="formData.assignee" placeholder="请输入负责人" />
        </el-form-item>
        <el-form-item label="截止日期" prop="deadline">
          <el-date-picker
            v-model="formData.deadline"
            type="date"
            placeholder="请选择截止日期"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="任务描述" prop="description">
          <el-input
            v-model="formData.description"
            type="textarea"
            :rows="4"
            placeholder="请输入任务描述"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  Search,
  Refresh,
  Plus,
  Download
} from '@element-plus/icons-vue'
import {
  getTaskList,
  createTask,
  updateTask,
  deleteTask
} from '@/api/task'
import {
  TASK_STATUS,
  TASK_STATUS_TEXT,
  TASK_PRIORITY,
  TASK_PRIORITY_TEXT
} from '@/constants/status'

const router = useRouter()

// 查询表单
const queryForm = reactive({
  title: '',
  status: null,
  priority: null
})

// 表格数据
const tableData = ref([])
const loading = ref(false)
const selectedRows = ref([])

// 分页
const pagination = reactive({
  page: 1,
  pageSize: 10,
  total: 0
})

// 对话框
const dialogVisible = ref(false)
const dialogTitle = ref('新建任务')
const formRef = ref(null)
const formData = reactive({
  id: null,
  title: '',
  priority: TASK_PRIORITY.NORMAL,
  assignee: '',
  deadline: '',
  description: ''
})

// 表单验证规则
const formRules = {
  title: [
    { required: true, message: '请输入任务名称', trigger: 'blur' }
  ],
  priority: [
    { required: true, message: '请选择优先级', trigger: 'change' }
  ],
  assignee: [
    { required: true, message: '请输入负责人', trigger: 'blur' }
  ]
}

// 获取任务列表
const fetchTaskList = async () => {
  loading.value = true
  try {
    const params = {
      ...queryForm,
      page: pagination.page,
      pageSize: pagination.pageSize
    }
    const data = await getTaskList(params)
    tableData.value = data.list || []
    pagination.total = data.total || 0
  } catch (error) {
    ElMessage.error('获取任务列表失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  pagination.page = 1
  fetchTaskList()
}

// 重置
const handleReset = () => {
  Object.assign(queryForm, {
    title: '',
    status: null,
    priority: null
  })
  handleSearch()
}

// 新建
const handleCreate = () => {
  dialogTitle.value = '新建任务'
  dialogVisible.value = true
}

// 查看
const handleView = (row) => {
  router.push(`/task/detail/${row.id}`)
}

// 编辑
const handleEdit = (row) => {
  dialogTitle.value = '编辑任务'
  Object.assign(formData, row)
  dialogVisible.value = true
}

// 删除
const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该任务吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
    .then(async () => {
      try {
        await deleteTask(row.id)
        ElMessage.success('删除成功')
        fetchTaskList()
      } catch (error) {
        ElMessage.error('删除失败')
      }
    })
    .catch(() => {})
}

// 导出
const handleExport = () => {
  ElMessage.info('导出功能开发中...')
}

// 表格选择变化
const handleSelectionChange = (val) => {
  selectedRows.value = val
}

// 分页大小变化
const handleSizeChange = () => {
  fetchTaskList()
}

// 当前页变化
const handleCurrentChange = () => {
  fetchTaskList()
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (formData.id) {
          await updateTask(formData.id, formData)
          ElMessage.success('更新成功')
        } else {
          await createTask(formData)
          ElMessage.success('创建成功')
        }
        dialogVisible.value = false
        fetchTaskList()
      } catch (error) {
        ElMessage.error('操作失败')
      }
    }
  })
}

// 关闭对话框
const handleDialogClose = () => {
  formRef.value?.resetFields()
  Object.assign(formData, {
    id: null,
    title: '',
    priority: TASK_PRIORITY.NORMAL,
    assignee: '',
    deadline: '',
    description: ''
  })
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
  fetchTaskList()
})
</script>

<style scoped lang="scss">
.task-list-container {
  padding: 20px;

  .search-form {
    margin-bottom: 20px;
  }

  .toolbar {
    margin-bottom: 20px;
  }

  .pagination-container {
    margin-top: 20px;
    display: flex;
    justify-content: flex-end;
  }
}
</style>

