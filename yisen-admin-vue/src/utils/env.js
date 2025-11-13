/**
 * 环境配置工具
 */

/**
 * 获取环境变量
 * @param {string} key - 环境变量键名
 * @param {any} defaultValue - 默认值
 * @returns {string} 环境变量值
 */
export function getEnv(key, defaultValue = '') {
  return import.meta.env[key] || defaultValue;
}

/**
 * 获取 API 基础路径
 * @returns {string} API 基础路径
 */
export function getApiBaseUrl() {
  return getEnv('VITE_API_BASE_URL', '/api');
}

/**
 * 获取应用标题
 * @returns {string} 应用标题
 */
export function getAppTitle() {
  return getEnv('VITE_APP_TITLE', '益森管理系统');
}

/**
 * 是否开启 Mock
 * @returns {boolean} 是否开启 Mock
 */
export function isUseMock() {
  return getEnv('VITE_USE_MOCK', 'false') === 'true';
}

/**
 * 是否显示调试信息
 * @returns {boolean} 是否显示调试信息
 */
export function isShowDebug() {
  return getEnv('VITE_SHOW_DEBUG', 'false') === 'true';
}

/**
 * 获取当前环境
 * @returns {string} 当前环境
 */
export function getMode() {
  return import.meta.env.MODE;
}

/**
 * 是否生产环境
 * @returns {boolean} 是否生产环境
 */
export function isProduction() {
  return import.meta.env.PROD;
}

/**
 * 是否开发环境
 * @returns {boolean} 是否开发环境
 */
export function isDevelopment() {
  return import.meta.env.DEV;
}

/**
 * 获取上传文件大小限制（字节）
 * @returns {number} 文件大小限制
 */
export function getUploadSize() {
  const size = Number(getEnv('VITE_UPLOAD_SIZE', '10'));
  return size * 1024 * 1024; // 转换为字节
}

/**
 * 打印环境信息
 */
export function printEnvInfo() {
  if (!isShowDebug()) return;

  console.log('='.repeat(50));
  console.log('应用信息:');
  console.log(`  应用名称: ${getEnv('VITE_APP_NAME')}`);
  console.log(`  应用标题: ${getAppTitle()}`);
  console.log(`  应用版本: ${getEnv('VITE_APP_VERSION')}`);
  console.log(`  运行环境: ${getMode()}`);
  console.log(`  API地址: ${getApiBaseUrl()}`);
  console.log(`  Mock数据: ${isUseMock() ? '开启' : '关闭'}`);
  console.log(`  调试模式: ${isShowDebug() ? '开启' : '关闭'}`);
  console.log('='.repeat(50));
}

export default {
  getEnv,
  getApiBaseUrl,
  getAppTitle,
  isUseMock,
  isShowDebug,
  getMode,
  isProduction,
  isDevelopment,
  getUploadSize,
  printEnvInfo,
};
