import axios from 'axios';
import { ElMessage, ElMessageBox } from 'element-plus';
import { useUserStore } from '@/stores/system/user.js';
import { getToken } from './auth';
import { HTTP_STATUS } from '@/constants/status';
import { getApiBaseUrl } from './env';
import { ResponseCode, getResponseMessage } from '@/enums/response-code';

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

// 需要重新登录的错误码
const NEED_LOGIN_CODES = [
  ResponseCode.UNAUTHORIZED,
  ResponseCode.USER_NOT_LOGIN,
  ResponseCode.TOKEN_INVALID,
  ResponseCode.TOKEN_EXPIRED,
  ResponseCode.TOKEN_KICKED_OUT,
];

// 不显示错误提示的错误码（某些业务场景需要自行处理）
const SILENT_ERROR_CODES = [];

/**
 * 处理登录过期
 */
const handleLoginExpired = (code) => {
  let message = '您的登录状态已过期，请重新登录';
  
  if (code === ResponseCode.TOKEN_KICKED_OUT) {
    message = '账号已在其他设备登录，您已被踢下线';
  }

  ElMessageBox.confirm(message, '提示', {
    confirmButtonText: '重新登录',
    cancelButtonText: '取消',
    type: 'warning',
    closeOnClickModal: false,
    closeOnPressEscape: false,
  })
    .then(() => {
      const userStore = useUserStore();
      userStore.logout();
      location.reload();
    })
    .catch(() => {
      // 用户取消，也执行登出
      const userStore = useUserStore();
      userStore.logout();
      location.reload();
    });
};

/**
 * 处理业务错误
 */
const handleBusinessError = (code, message) => {
  // 需要重新登录的错误
  if (NEED_LOGIN_CODES.includes(code)) {
    handleLoginExpired(code);
    return;
  }

  // 静默错误，不显示提示
  if (SILENT_ERROR_CODES.includes(code)) {
    return;
  }

  // 显示错误提示
  const errorMessage = message || getResponseMessage(code);
  ElMessage.error(errorMessage);
};

// 响应拦截器
service.interceptors.response.use(
  (response) => {
    const res = response.data;
    const { code, message, data } = res;

    // 如果返回的状态码不是 200，则视为错误
    if (code !== ResponseCode.SUCCESS) {
      // 统一处理业务错误
      handleBusinessError(code, message);
      return Promise.reject(new Error(message || getResponseMessage(code)));
    }

    return data;
  },
  (error) => {
    console.error('响应错误:', error);

    let message = '请求失败';

    if (error.response) {
      const { status, data } = error.response;
      
      // 优先使用后端返回的错误信息
      if (data && data.message) {
        message = data.message;
      } else if (data && data.code) {
        message = getResponseMessage(data.code);
      } else {
        // 根据 HTTP 状态码返回错误信息
        switch (status) {
          case HTTP_STATUS.BAD_REQUEST:
            message = getResponseMessage(ResponseCode.BAD_REQUEST);
            break;
          case HTTP_STATUS.UNAUTHORIZED:
            message = getResponseMessage(ResponseCode.UNAUTHORIZED);
            handleLoginExpired(ResponseCode.UNAUTHORIZED);
            break;
          case HTTP_STATUS.FORBIDDEN:
            message = getResponseMessage(ResponseCode.FORBIDDEN);
            break;
          case HTTP_STATUS.NOT_FOUND:
            message = getResponseMessage(ResponseCode.NOT_FOUND);
            break;
          case HTTP_STATUS.SERVER_ERROR:
            message = getResponseMessage(ResponseCode.INTERNAL_SERVER_ERROR);
            break;
          default:
            message = `请求失败: ${status}`;
        }
      }
    } else if (error.code === 'ECONNABORTED') {
      message = getResponseMessage(ResponseCode.REQUEST_TIMEOUT);
    } else if (error.message.includes('Network Error')) {
      message = '网络错误，请检查网络连接';
    } else if (error.message) {
      message = error.message;
    }

    // 只在非登录过期的情况下显示错误提示
    if (!NEED_LOGIN_CODES.includes(error.response?.data?.code)) {
      ElMessage.error(message);
    }

    return Promise.reject(error);
  }
);

export default service;
