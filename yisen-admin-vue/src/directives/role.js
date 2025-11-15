import { useUserStore } from '@/stores/user.js';

/**
 * 角色指令
 * 用法:
 * 1. 单个角色：v-role="'ROLE_ADMIN'"
 * 2. 多个角色（任一满足）：v-role="['ROLE_ADMIN', 'ROLE_USER']"
 * 3. 禁用而非隐藏：v-role.disable="'ROLE_ADMIN'"
 */
export const role = {
  mounted(el, binding) {
    checkRole(el, binding);
  },
  updated(el, binding) {
    checkRole(el, binding);
  },
};

/**
 * 检查角色
 */
function checkRole(el, binding) {
  const { value, modifiers } = binding;
  const userStore = useUserStore();

  if (!value) {
    throw new Error('需要提供角色标识，如 v-role="\'ROLE_ADMIN\'"');
  }

  const hasRole = userStore.hasRole(value);

  if (!hasRole) {
    // 如果有 disable 修饰符，则禁用元素而不是移除
    if (modifiers.disable) {
      el.disabled = true;
      el.style.cursor = 'not-allowed';
      el.style.opacity = '0.6';
      // 添加提示
      if (!el.title) {
        el.title = '您的角色无权执行此操作';
      }
      // 阻止点击事件
      el.addEventListener('click', preventClick, true);
    } else {
      // 隐藏元素
      el.style.display = 'none';
      // 或者完全移除：el.parentNode && el.parentNode.removeChild(el)
    }
  } else {
    // 有角色时，移除可能存在的禁用状态
    if (modifiers.disable) {
      el.disabled = false;
      el.style.cursor = '';
      el.style.opacity = '';
      el.removeEventListener('click', preventClick, true);
    } else {
      el.style.display = '';
    }
  }
}

/**
 * 阻止点击事件
 */
function preventClick(e) {
  e.preventDefault();
  e.stopPropagation();
  return false;
}

export default role;
