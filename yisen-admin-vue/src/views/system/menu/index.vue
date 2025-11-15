<template>
  <div class="menu-container">
    <el-card shadow="never">
      <!-- 查询条件 -->
      <el-form :model="queryParams" :inline="true" class="search-form">
        <el-form-item label="菜单名称">
          <el-input v-model="queryParams.menuName" placeholder="请输入菜单名称" clearable />
        </el-form-item>
        <el-form-item label="菜单名称">
          <el-input v-model="queryParams.menuName" placeholder="请输入菜单名称" clearable />
        </el-form-item>
        <el-form-item label="菜单名称">
          <el-input v-model="queryParams.menuName" placeholder="请输入菜单名称" clearable />
        </el-form-item>

        <el-form-item label="菜单编码">
          <el-input v-model="queryParams.menuCode" placeholder="请输入菜单编码" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select style="width: 120px" v-model="queryParams.status" placeholder="请选择状态" clearable>
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">搜索</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 操作按钮 -->
      <div class="table-actions">
        <el-button type="primary" @click="handleAdd">新增</el-button>
        <el-button type="danger" @click="handleBatchDelete" :disabled="selectedRows.length === 0"> 批量删除 </el-button>
        <el-button @click="handleExpandAll">展开/折叠</el-button>
      </div>

      <!-- 菜单表格 -->
      <el-table
        ref="menuTableRef"
        v-loading="loading"
        :data="menuTableData"
        style="width: 100%"
        row-key="id"
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
        default-expand-all
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column type="index" label="#" width="50" />

        <el-table-column prop="menuName" label="菜单名称" >
          <template #default="{ row }">
            <el-icon v-if="row?.icon">
              <component :is="row.icon" />
            </el-icon>
            <span style="margin-left: 8px">{{ row.menuName }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="menuCode" label="菜单编码" width="150" />
        <el-table-column prop="path" label="路由地址" width="150" />
        <el-table-column prop="component" label="组件路径" width="200" />
        <el-table-column prop="type" label="菜单类型" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.type === 1">菜单</el-tag>
            <el-tag v-else-if="row.type === 2" type="success">按钮</el-tag>
            <el-tag v-else type="info">未知</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="permission" label="权限标识" width="150" />
        <el-table-column prop="sort" label="排序" width="80" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.status === 1" type="success">启用</el-tag>
            <el-tag v-else type="danger">禁用</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" prop="createTime" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleAddChild(row)">新增</el-button>
            <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 菜单编辑对话框 -->
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="600px" @close="handleDialogClose">
      <el-form ref="menuFormRef" :model="menuForm" :rules="menuRules" label-width="100px">
        <el-form-item label="上级菜单" prop="parentId">
          <el-tree-select
            v-model="menuForm.parentId"
            :data="menuTreeOptions"
            node-key="id"
            :props="{ label: 'menuName', value: 'id' }"
            check-strictly
            clearable
            placeholder="请选择上级菜单"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="菜单类型" prop="type">
          <el-radio-group v-model="menuForm.type">
            <el-radio :label="1">菜单</el-radio>
            <el-radio :label="2">按钮</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="菜单名称" prop="menuName">
          <el-input v-model="menuForm.menuName" placeholder="请输入菜单名称" />
        </el-form-item>
        <el-form-item label="菜单编码" prop="menuCode">
          <el-input v-model="menuForm.menuCode" placeholder="请输入菜单编码" />
        </el-form-item>
        <el-form-item label="路由地址" prop="path" v-if="menuForm.type === 1">
          <el-input v-model="menuForm.path" placeholder="请输入路由地址" />
        </el-form-item>
        <el-form-item label="组件路径" prop="component" v-if="menuForm.type === 1">
          <el-input v-model="menuForm.component" placeholder="请输入组件路径" />
        </el-form-item>
        <el-form-item label="权限标识" prop="permission" v-if="menuForm.type === 2">
          <el-input v-model="menuForm.permission" placeholder="请输入权限标识" />
        </el-form-item>
        <el-form-item label="图标" prop="icon" v-if="menuForm.type === 1">
          <el-input v-model="menuForm.icon" placeholder="请输入图标名称" />
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input-number v-model="menuForm.sort" :min="0" controls-position="right" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="menuForm.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitMenuForm">确 定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
  import { onMounted, reactive, ref } from 'vue';
  import { ElMessage, ElMessageBox } from 'element-plus';
  import { useMenuStore, useUserStore } from '@/stores/index';

  const menuStore = useMenuStore();
  const userStore = useUserStore();

  // 数据相关
  const loading = ref(false);
  const menuTableData = ref([]);
  const menuTreeOptions = ref([]);
  const menuTableRef = ref();

  // 对话框相关
  const dialogVisible = ref(false);
  const dialogTitle = ref('');
  const isEdit = ref(false);

  // 查询参数
  const queryParams = reactive({
    menuName: '',
    menuCode: '',
    status: undefined,
  });

  // 表单相关
  const menuFormRef = ref();
  const menuForm = reactive({
    id: undefined,
    parentId: null,
    menuName: '',
    menuCode: '',
    path: '',
    component: '',
    type: 1,
    permission: '',
    icon: '',
    sort: 0,
    status: 1,
  });

  // 表单校验规则
  const menuRules = {
    menuName: [{ required: true, message: '请输入菜单名称', trigger: 'blur' }],
    menuCode: [{ required: true, message: '请输入菜单编码', trigger: 'blur' }],
    path: [{ required: true, message: '请输入路由地址', trigger: 'blur' }],
    component: [{ required: true, message: '请输入组件路径', trigger: 'blur' }],
    permission: [{ required: true, message: '请输入权限标识', trigger: 'blur' }],
  };

  // 添加选中行数据
  const selectedRows = ref([]);

  // 处理选择变化
  const handleSelectionChange = (rows) => {
    selectedRows.value = rows;
  };

  // 展开/折叠所有节点
  const expandAll = ref(true);
  const handleExpandAll = () => {
    expandAll.value = !expandAll.value;
    toggleExpansion(menuTableData.value, expandAll.value);
  };

  const toggleExpansion = (data, expanded) => {
    data.forEach((item) => {
      menuTableRef.value.toggleRowExpansion(item, expanded);
      if (item.children && item.children.length > 0) {
        toggleExpansion(item.children, expanded);
      }
    });
  };

  // 获取菜单列表
  const fetchMenuList = async () => {
    loading.value = true;
    try {
      const params = {
        ...queryParams,
      };
      const res = await menuStore.fetchUserMenuTree(userStore.id, params);
      menuTableData.value = res;

      // 构造菜单树选项（排除按钮类型）
      menuTreeOptions.value = [
        {
          id: 0,
          menuName: '顶级菜单',
          children: buildMenuTreeOptions(res),
        },
      ];
    } catch (error) {
      ElMessage.error('获取菜单列表失败');
    } finally {
      loading.value = false;
    }
  };

  // 构建菜单树选项（仅包含菜单类型）
  const buildMenuTreeOptions = (menus) => {
    return menus
      .filter((menu) => menu.type === 1)
      .map((menu) => {
        const menuItem = {
          id: menu.id,
          menuName: menu.menuName,
        };
        if (menu.children && menu.children.length > 0) {
          menuItem.children = buildMenuTreeOptions(menu.children);
        }
        return menuItem;
      });
  };

  // 搜索
  const handleQuery = () => {
    fetchMenuList();
  };

  // 重置查询
  const resetQuery = () => {
    Object.assign(queryParams, {
      menuName: '',
      menuCode: '',
      status: undefined,
    });
    fetchMenuList();
  };

  // 新增菜单
  const handleAdd = () => {
    dialogTitle.value = '新增菜单';
    isEdit.value = false;
    dialogVisible.value = true;
    resetMenuForm();
  };

  // 新增子菜单
  const handleAddChild = (row) => {
    dialogTitle.value = '新增子菜单';
    isEdit.value = false;
    dialogVisible.value = true;
    resetMenuForm();
    menuForm.parentId = row.id;
  };

  // 编辑菜单
  const handleEdit = (row) => {
    dialogTitle.value = '编辑菜单';
    isEdit.value = true;
    dialogVisible.value = true;

    // 填充表单数据
    Object.keys(menuForm).forEach((key) => {
      menuForm[key] = row[key];
    });
  };

  // 提交表单
  const submitMenuForm = async () => {
    try {
      await menuFormRef.value.validate();
      const res = isEdit.value ? await menuStore.updateMenu(menuForm) : await menuStore.createMenu(menuForm);

      if (res.success) {
        ElMessage.success(`${isEdit.value ? '编辑' : '新增'}菜单成功`);
        dialogVisible.value = false;
        fetchMenuList();
      } else {
        ElMessage.error(res.message || `${isEdit.value ? '编辑' : '新增'}菜单失败`);
      }
    } catch (error) {
      console.error(error);
      ElMessage.error(`${isEdit.value ? '编辑' : '新增'}菜单失败`);
    }
  };

  // 删除菜单
  const handleDelete = (row) => {
    ElMessageBox.confirm(`确定删除菜单"${row.menuName}"吗？`, '提示', {
      type: 'warning',
    })
      .then(async () => {
        try {
          const res = await menuStore.removeMenu(row.id);
          if (res.success) {
            ElMessage.success('删除成功');
            fetchMenuList();
          } else {
            ElMessage.error(res.message || '删除失败');
          }
        } catch (error) {
          ElMessage.error('删除失败');
        }
      })
      .catch(() => {});
  };

  // 批量删除方法
  const handleBatchDelete = () => {
    ElMessageBox.confirm(`确定删除选中的 ${selectedRows.value.length} 个菜单吗？`, '提示', {
      type: 'warning',
    })
      .then(async () => {
        try {
          const ids = selectedRows.value.map((item) => item.id);
          const res = await menuStore.batchRemoveMenu(ids);
          if (res.success) {
            ElMessage.success('批量删除成功');
            fetchMenuList();
          } else {
            ElMessage.error(res.message || '批量删除失败');
          }
        } catch (error) {
          ElMessage.error('批量删除失败');
        }
      })
      .catch(() => {});
  };

  // 重置表单
  const resetMenuForm = () => {
    Object.keys(menuForm).forEach((key) => {
      if (key === 'type') {
        menuForm[key] = 1;
      } else if (key === 'status') {
        menuForm[key] = 1;
      } else if (key === 'sort') {
        menuForm[key] = 0;
      } else {
        menuForm[key] = undefined;
      }
    });
  };

  // 关闭对话框
  const handleDialogClose = () => {
    menuFormRef.value.resetFields();
  };

  // 初始化
  onMounted(() => {
    fetchMenuList();
  });
</script>

<style scoped lang="scss">
  .menu-container {
    height: auto;
    overflow: visible;
    .search-form {
      margin-bottom: 20px;
    }

    .table-actions {
      margin-bottom: 20px;
    }

    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }
  }

</style>
