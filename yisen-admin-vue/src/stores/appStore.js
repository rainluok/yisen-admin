import { defineStore } from "pinia";

export const useAppStore = defineStore("app", {
  state: () => ({
    // 侧边栏
    sidebar: {
      opened: true,
      withoutAnimation: false,
    },
    // 设备类型
    device: "desktop",
    // 主题
    theme: "light",
    // 语言
    language: "zh-cn",
    // 页面加载状态
    loading: false,
    // 缓存的页面
    cachedViews: [],
  }),

  getters: {
    /**
     * 侧边栏是否打开
     */
    sidebarOpened: (state) => state.sidebar.opened,

    /**
     * 是否移动设备
     */
    isMobile: (state) => state.device === "mobile",
  },

  actions: {
    /**
     * 切换侧边栏
     */
    toggleSidebar() {
      this.sidebar.opened = !this.sidebar.opened;
      this.sidebar.withoutAnimation = false;
    },

    /**
     * 关闭侧边栏
     */
    closeSidebar(withoutAnimation = false) {
      this.sidebar.opened = false;
      this.sidebar.withoutAnimation = withoutAnimation;
    },

    /**
     * 打开侧边栏
     */
    openSidebar() {
      this.sidebar.opened = true;
      this.sidebar.withoutAnimation = false;
    },

    /**
     * 切换设备类型
     * @param {string} device - 设备类型 (desktop/mobile)
     */
    toggleDevice(device) {
      this.device = device;
    },

    /**
     * 切换主题
     * @param {string} theme - 主题名称
     */
    toggleTheme(theme) {
      this.theme = theme;
      // 保存到本地存储
      localStorage.setItem("yisen_admin_theme", theme);
    },

    /**
     * 设置语言
     * @param {string} language - 语言代码
     */
    setLanguage(language) {
      this.language = language;
      // 保存到本地存储
      localStorage.setItem("yisen_admin_language", language);
    },

    /**
     * 设置加载状态
     * @param {boolean} loading - 加载状态
     */
    setLoading(loading) {
      this.loading = loading;
    },

    /**
     * 添加缓存页面
     * @param {string} view - 页面名称
     */
    addCachedView(view) {
      if (this.cachedViews.includes(view)) return;
      this.cachedViews.push(view);
    },

    /**
     * 删除缓存页面
     * @param {string} view - 页面名称
     */
    deleteCachedView(view) {
      const index = this.cachedViews.indexOf(view);
      if (index > -1) {
        this.cachedViews.splice(index, 1);
      }
    },

    /**
     * 清空缓存页面
     */
    clearCachedViews() {
      this.cachedViews = [];
    },

    /**
     * 初始化应用状态
     */
    initApp() {
      // 从本地存储读取主题
      const theme = localStorage.getItem("yisen_admin_theme");
      if (theme) {
        this.theme = theme;
      }

      // 从本地存储读取语言
      const language = localStorage.getItem("yisen_admin_language");
      if (language) {
        this.language = language;
      }

      // 检测设备类型
      this.device = this.isMobileDevice() ? "mobile" : "desktop";
    },

    /**
     * 检测是否为移动设备
     */
    isMobileDevice() {
      const rect = document.body.getBoundingClientRect();
      return rect.width - 1 < 768;
    },
  },
});
