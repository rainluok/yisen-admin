import { defineStore } from 'pinia';

export const useTagsViewStore = defineStore('tagsView', {
  state: () => ({
    // 已访问的标签页
    visitedViews: [],
    // 缓存的页面（用于 keep-alive）
    cachedViews: [],
  }),

  getters: {
    /**
     * 是否有标签页
     */
    hasViews: (state) => state.visitedViews.length > 0,

    /**
     * 标签页数量
     */
    viewsCount: (state) => state.visitedViews.length,
  },

  actions: {
    /**
     * 添加已访问的标签页
     * @param {Object} view - 路由对象
     */
    addView(view) {
      this.addVisitedView(view);
      this.addCachedView(view);
    },

    /**
     * 添加已访问的标签页（不缓存）
     * @param {Object} view - 路由对象
     */
    addVisitedView(view) {
      // 如果已存在，更新
      const existingIndex = this.visitedViews.findIndex((v) => v.path === view.path);
      if (existingIndex !== -1) {
        // 更新已存在的标签页
        this.visitedViews[existingIndex] = Object.assign({}, this.visitedViews[existingIndex], {
          fullPath: view.fullPath,
          query: view.query,
          params: view.params,
        });
        return;
      }

      // 添加新标签页
      this.visitedViews.push({
        name: view.name,
        path: view.path,
        fullPath: view.fullPath,
        title: view.meta?.title || 'no-name',
        icon: view.meta?.icon,
        affix: view.meta?.affix, // 是否固定标签页
        query: view.query,
        params: view.params,
      });
    },

    /**
     * 添加缓存的页面
     * @param {Object} view - 路由对象
     */
    addCachedView(view) {
      // 如果设置了不缓存，则不添加
      if (view.meta?.noCache) {
        return;
      }

      // 如果已缓存，返回
      if (this.cachedViews.includes(view.name)) {
        return;
      }

      this.cachedViews.push(view.name);
    },

    /**
     * 删除标签页
     * @param {Object} view - 路由对象
     */
    delView(view) {
      return new Promise((resolve) => {
        this.delVisitedView(view);
        this.delCachedView(view);
        resolve({
          visitedViews: [...this.visitedViews],
          cachedViews: [...this.cachedViews],
        });
      });
    },

    /**
     * 删除已访问的标签页
     * @param {Object} view - 路由对象
     */
    delVisitedView(view) {
      return new Promise((resolve) => {
        const index = this.visitedViews.findIndex((v) => v.path === view.path);
        if (index !== -1) {
          this.visitedViews.splice(index, 1);
        }
        resolve([...this.visitedViews]);
      });
    },

    /**
     * 删除缓存的页面
     * @param {Object} view - 路由对象
     */
    delCachedView(view) {
      return new Promise((resolve) => {
        const index = this.cachedViews.indexOf(view.name);
        if (index !== -1) {
          this.cachedViews.splice(index, 1);
        }
        resolve([...this.cachedViews]);
      });
    },

    /**
     * 删除其他标签页
     * @param {Object} view - 路由对象
     */
    delOthersViews(view) {
      return new Promise((resolve) => {
        this.delOthersVisitedViews(view);
        this.delOthersCachedViews(view);
        resolve({
          visitedViews: [...this.visitedViews],
          cachedViews: [...this.cachedViews],
        });
      });
    },

    /**
     * 删除其他已访问的标签页
     * @param {Object} view - 路由对象
     */
    delOthersVisitedViews(view) {
      return new Promise((resolve) => {
        this.visitedViews = this.visitedViews.filter((v) => {
          return v.meta?.affix || v.path === view.path;
        });
        resolve([...this.visitedViews]);
      });
    },

    /**
     * 删除其他缓存的页面
     * @param {Object} view - 路由对象
     */
    delOthersCachedViews(view) {
      return new Promise((resolve) => {
        const index = this.cachedViews.indexOf(view.name);
        if (index > -1) {
          this.cachedViews = this.cachedViews.slice(index, index + 1);
        } else {
          this.cachedViews = [];
        }
        resolve([...this.cachedViews]);
      });
    },

    /**
     * 删除所有标签页
     */
    delAllViews() {
      return new Promise((resolve) => {
        this.delAllVisitedViews();
        this.delAllCachedViews();
        resolve({
          visitedViews: [...this.visitedViews],
          cachedViews: [...this.cachedViews],
        });
      });
    },

    /**
     * 删除所有已访问的标签页
     */
    delAllVisitedViews() {
      return new Promise((resolve) => {
        // 保留固定的标签页
        this.visitedViews = this.visitedViews.filter((v) => v.affix);
        resolve([...this.visitedViews]);
      });
    },

    /**
     * 删除所有缓存的页面
     */
    delAllCachedViews() {
      return new Promise((resolve) => {
        this.cachedViews = [];
        resolve([...this.cachedViews]);
      });
    },

    /**
     * 删除左侧标签页
     * @param {Object} view - 路由对象
     */
    delLeftViews(view) {
      return new Promise((resolve) => {
        const currentIndex = this.visitedViews.findIndex((v) => v.path === view.path);
        if (currentIndex === -1) {
          return;
        }
        this.visitedViews = this.visitedViews.filter((v, index) => {
          return v.affix || index >= currentIndex;
        });
        resolve([...this.visitedViews]);
      });
    },

    /**
     * 删除右侧标签页
     * @param {Object} view - 路由对象
     */
    delRightViews(view) {
      return new Promise((resolve) => {
        const currentIndex = this.visitedViews.findIndex((v) => v.path === view.path);
        if (currentIndex === -1) {
          return;
        }
        this.visitedViews = this.visitedViews.filter((v, index) => {
          return v.affix || index <= currentIndex;
        });
        resolve([...this.visitedViews]);
      });
    },

    /**
     * 更新标签页
     * @param {Object} view - 路由对象
     */
    updateVisitedView(view) {
      const index = this.visitedViews.findIndex((v) => v.path === view.path);
      if (index !== -1) {
        this.visitedViews[index] = Object.assign({}, this.visitedViews[index], view);
      }
    },
  },

  // 持久化配置
  persist: {
    enabled: true,
    strategies: [
      {
        key: 'yisen_admin_tags_view',
        storage: sessionStorage,
        paths: ['visitedViews', 'cachedViews'],
      },
    ],
  },
});
