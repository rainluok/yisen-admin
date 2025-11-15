import { useUserStore } from '@/stores/user.js';

/**
 * 权限指令
 * 用法: v-permission="'user:view'" 或 v-permission="['user:view', 'user:edit']"
 */
export const permission = {
  mounted(el, binding) {
    const { value } = binding;
    const userStore = useUserStore();

    if (value) {
      const hasPermission = userStore.hasPermission(value);
      if (!hasPermission) {
        // 移除元素
        el.parentNode && el.parentNode.removeChild(el);
      }
    } else {
      throw new Error('需要提供权限标识，如 v-permission="\'user:view\'"');
    }
  },
};

export default permission;
