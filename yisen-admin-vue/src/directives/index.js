import permission from './permission';
import debounce from './debounce';

/**
 * 注册全局指令
 */
export function setupDirectives(app) {
  app.directive('permission', permission);
  app.directive('debounce', debounce);
}

export default {
  permission,
  debounce,
};
