import { ref } from 'vue';

/**
 * 认证相关的组合式函数
 */
export function useAuth() {
  const isAuthenticated = ref(false);

  const checkAuth = () => {
    const token = localStorage.getItem('yisen_admin_token');
    isAuthenticated.value = !!token;
    return isAuthenticated.value;
  };

  const logout = () => {
    localStorage.removeItem('yisen_admin_token');
    localStorage.removeItem('yisen_admin_user_info');
    localStorage.removeItem('yisen_admin_permissions');
    isAuthenticated.value = false;
  };

  return {
    isAuthenticated,
    checkAuth,
    logout,
  };
}
