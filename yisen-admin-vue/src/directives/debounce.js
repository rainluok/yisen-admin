/**
 * 防抖指令
 * 用法: v-debounce="handleClick" 或 v-debounce:500="handleClick"
 */
export const debounce = {
  mounted(el, binding) {
    const delay = binding.arg ? parseInt(binding.arg) : 300;
    let timer = null;

    el.addEventListener('click', () => {
      if (timer) {
        clearTimeout(timer);
      }
      timer = setTimeout(() => {
        binding.value();
      }, delay);
    });
  },
};

export default debounce;
