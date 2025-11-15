<template>
  <div class="depart-list-container">
    <el-card shadow="never">
      <!-- 搜索表单 -->
      <el-form :model="queryForm" class="search-form" inline>
        <el-form-item label="部门名称">
          <el-input v-model="queryForm.name" placeholder="请输入部门名称" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select style="width: 150px" v-model="queryForm.status" placeholder="请选择状态" clearable>
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 操作按钮 -->
      <div class="button-group">
        <el-button type="primary" @click="handleAdd">新增</el-button>
        <el-button type="danger" @click="handleBatchDelete" :disabled="selectedRows.length === 0">批量删除</el-button>
      </div>

      <!-- 部门表格 -->
      <el-table
        v-loading="loading"
        :data="tableData"
        style="width: 100%"
        row-key="id"
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="name" label="部门名称" width="200" />
        <el-table-column prop="code" label="部门编码" width="150" />
        <el-table-column prop="sort" label="排序" width="80" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="描述" min-width="200" />
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
            <el-button type="primary" link @click="handleViewSub(row)">查看子部门</el-button>
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

    <!-- 部门编辑对话框 -->
    <el-dialog
      :title="dialogTitle"
      v-model="dialogVisible"
      width="500px"
      @close="handleDialogClose"
    >
      <el-form
        :model="form"
        :rules="rules"
        ref="formRef"
        label-width="100px"
      >
        <el-form-item label="上级部门" prop="parentId">
          <el-tree-select
            v-model="form.parentId"
            :data="departTree"
            node-key="id"
            :props="{ label: 'name', value: 'id' }"
            check-strictly
            clearable
            placeholder="请选择上级部门"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="部门名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入部门名称" />
        </el-form-item>
        <el-form-item label="部门编码" prop="code">
          <el-input v-model="form.code" placeholder="请输入部门编码" />
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input-number v-model="form.sort" :min="0" controls-position="right" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="form.description" type="textarea" placeholder="请输入描述" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleSubmit">确 定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

// 查询表单
const queryForm = reactive({
  name: '',
  status: null
})

// 表格数据
const tableData = ref([])
const loading = ref(false)
const selectedRows = ref([])

// 分页参数
const pagination = reactive({
  page: 1,
  pageSize: 10,
  total: 0
})

// 对话框相关
const dialogVisible = ref(false)
const dialogTitle = ref('')
const isEdit = ref(false)
const formRef = ref()

// 表单数据
const form = reactive({
  id: undefined,
  parentId: null,
  name: '',
  code: '',
  sort: 0,
  status: 1,
  description: ''
})

// 部门树结构
const departTree = ref([])

// 表单校验规则
const rules = {
  name: [{ required: true, message: '请输入部门名称', trigger: 'blur' }],
  code: [{ required: true, message: '请输入部门编码', trigger: 'blur' }]
}

// 获取部门列表
const fetchDepartList = async () => {
  loading.value = true
  try {
    // 这里调用实际的API接口
    // const params = {
    //   ...queryForm,
    //   page: pagination.page,
    //   pageSize: pagination.pageSize
    // }
    // const data = await getDepartList(params)
    // tableData.value = data.list || []
    // pagination.total = data.total || 0

    // 模拟数据
    tableData.value = [
      {
        id: 1,
        parentId: 0,
        name: '技术部',
        code: 'DEV',
        sort: 1,
        status: 1,
        description: '技术研发部门',
        createTime: '2023-01-01 12:00:00',
        children: [
          {
            id: 3,
            parentId: 1,
            name: '前端组',
            code: 'FE',
            sort: 1,
            status: 1,
            description: '前端开发组',
            createTime: '2023-01-01 12:00:00'
          }
        ]
      },
      {
        id: 2,
        parentId: 0,
        name: '产品部',
        code: 'PM',
        sort: 2,
        status: 1,
        description: '产品设计部门',
        createTime: '2023-01-01 12:00:00'
      }
    ]
    pagination.total = 2
  } catch (error) {
    ElMessage.error('获取部门列表失败')
  } finally {
    loading.value = false
  }
}

// 获取部门树结构
const fetchDepartTree = async () => {
  try {
    // 这里调用实际的API接口获取部门树
    // const data = await getDepartTree()
    // departTree.value = data

    // 模拟数据
    departTree.value = [
      {
        id: 0,
        name: '顶级部门',
        children: [
          {
            id: 1,
            name: '技术部',
            children: [
              {
                id: 3,
                name: '前端组'
              }
            ]
          },
          {
            id: 2,
            name: '产品部'
          }
        ]
      }
    ]
  } catch (error) {
    ElMessage.error('获取部门树失败')
  }
}

// 搜索
const handleSearch = () => {
  pagination.page = 1
  fetchDepartList()
}

// 重置
const handleReset = () => {
  Object.assign(queryForm, {
    name: '',
    status: null
  })
  handleSearch()
}

// 分页变化
const handleSizeChange = () => {
  fetchDepartList()
}

const handleCurrentChange = () => {
  fetchDepartList()
}

// 表格选择变化
const handleSelectionChange = (rows) => {
  selectedRows.value = rows
}

// 新增部门
const handleAdd = () => {
  dialogTitle.value = '新增部门'
  isEdit.value = false
  dialogVisible.value = true
  // 重置表单
  Object.assign(form, {
    id: undefined,
    parentId: null,
    name: '',
    code: '',
    sort: 0,
    status: 1,
    description: ''
  })
}

// 编辑部门
const handleEdit = (row) => {
  dialogTitle.value = '编辑部门'
  isEdit.value = true
  dialogVisible.value = true
  // 填充表单数据
  Object.assign(form, row)
}

// 查看子部门
const handleViewSub = (row) => {
  queryForm.name = ''
  queryForm.status = null
  // 如果需要跳转到子部门列表，可以在这里实现
  ElMessage.info(`查看 ${row.name} 的子部门`)
}

// 提交表单
const handleSubmit = async () => {
  try {
    await formRef.value.validate()

    // 调用保存接口
    if (isEdit.value) {
      // await updateDepart(form)
      ElMessage.success('更新成功')
    } else {
      // await addDepart(form)
      ElMessage.success('新增成功')
    }

    dialogVisible.value = false
    fetchDepartList()
  } catch (error) {
    console.error(error)
  }
}

// 删除部门
const handleDelete = (row) => {
  ElMessageBox.confirm(`确定删除部门 "${row.name}" 吗？`, '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      // await deleteDepart(row.id)
      ElMessage.success('删除成功')
      fetchDepartList()
    } catch (error) {
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}

// 批量删除
const handleBatchDelete = () => {
  ElMessageBox.confirm(
    `确定删除选中的 ${selectedRows.value.length} 个部门吗？`,
    '提示',
    {
      type: 'warning'
    }
  ).then(async () => {
    try {
      const ids = selectedRows.value.map(item => item.id)
      // await batchDeleteDeparts(ids)
      ElMessage.success('删除成功')
      fetchDepartList()
    } catch (error) {
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}

// 关闭对话框
const handleDialogClose = () => {
  formRef.value.resetFields()
}

// 初始化
onMounted(() => {
  fetchDepartList()
  fetchDepartTree()
})
</script>

<style scoped lang="scss">
.depart-list-container {

  .search-form {
    margin-bottom: 20px;
  }

  .button-group {
    margin-bottom: 20px;
  }

  .pagination-container {
    margin-top: 20px;
    display: flex;
    justify-content: flex-end;
  }
}
</style>
