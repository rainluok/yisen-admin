import axios from 'axios';
import { ElMessage, ElMessageBox } from 'element-plus';
import { useUserStore } from '@/stores/userStore';
import { getToken } from './auth';
import { HTTP_STATUS } from '@/constants/status';
import { getApiBaseUrl } from './env';

// 创建 axios 实例
const service = axios.create({
  baseURL: getApiBaseUrl(),
  timeout: 15000, // 请求超时时间
  headers: {
    'Content-Type': 'application/json;charset=utf-8',
  },
});

// 请求拦截器
service.interceptors.request.use(
  (config) => {
    // 获取 token
    const token = getToken();
    if (token) {
      // 让每个请求携带 token
      config.headers['Authorization'] = `Bearer ${token}`;
    }

    return config;
  },
  (error) => {
    console.error('请求错误:', error);
    return Promise.reject(error);
  }
);

// 响应拦截器
service.interceptors.response.use(
  (response) => {
    const res = response.data;
    const { code, message, data } = res;

    // 如果返回的状态码不是 200，则视为错误
    if (code !== HTTP_STATUS.SUCCESS) {
      ElMessage.error(message || '请求失败');

      // 401: 未授权，token 失效
      if (code === HTTP_STATUS.UNAUTHORIZED) {
        ElMessageBox.confirm('您的登录状态已过期，请重新登录', '提示', {
          confirmButtonText: '重新登录',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(() => {
          const userStore = useUserStore();
          userStore.logout();
          location.reload();
        });
      }

      return Promise.reject(new Error(message || '请求失败'));
    }

    return data;
  },
  (error) => {
    console.error('响应错误:', error);

    let message = error.message || '请求失败';

    if (error.response) {
      switch (error.response.status) {
        case HTTP_STATUS.BAD_REQUEST:
          message = '请求参数错误';
          break;
        case HTTP_STATUS.UNAUTHORIZED:
          message = '未授权，请重新登录';
          break;
        case HTTP_STATUS.FORBIDDEN:
          message = '拒绝访问';
          break;
        case HTTP_STATUS.NOT_FOUND:
          message = '请求的资源不存在';
          break;
        case HTTP_STATUS.SERVER_ERROR:
          message = '服务器错误';
          break;
        default:
          message = `请求失败: ${error.response.status}`;
      }
    } else if (error.code === 'ECONNABORTED') {
      message = '请求超时';
    } else if (error.message.includes('Network Error')) {
      message = '网络错误';
    }

    ElMessage.error(message);
    return Promise.reject(error);
  }
);

export default service;
