import { defineStore } from 'pinia';
import {
  pageDictList,
  getDictList,
  addDict,
  getDictDetail,
  updateDict,
  deleteDict,
  pageDictItemList,
  getDictItemsByCode,
  getDictItemsByDictId,
  addDictItem,
  getDictItemDetail,
  updateDictItem,
  deleteDictItem,
} from '@/api/dict';

export const useDictStore = defineStore('dict', {
  state: () => ({
    // 字典列表
    dictList: [],
    // 字典项列表
    dictItemList: [],
    // 当前字典
    currentDict: null,
    // 当前字典项
    currentDictItem: null,
    // 字典分页信息
    dictPagination: {
      pageNum: 1,
      pageSize: 10,
      total: 0,
    },
    // 字典项分页信息
    dictItemPagination: {
      pageNum: 1,
      pageSize: 10,
      total: 0,
    },
    // 字典缓存（根据字典编码缓存字典项）
    dictCache: {},
    // 加载状态
    loading: false,
  }),

  getters: {
    /**
     * 是否有字典数据
     */
    hasDicts: (state) => state.dictList.length > 0,

    /**
     * 字典总数
     */
    totalDicts: (state) => state.dictPagination.total,

    /**
     * 根据字典编码获取字典项
     */
    getDictItemsByCode: (state) => (code) => {
      return state.dictCache[code] || [];
    },
  },

  actions: {
    /**
     * 分页查询字典列表
     * @param {Object} params - 查询参数
     */
    async getDictListPage(params = {}) {
      this.loading = true;
      try {
        const { pageNum = 1, pageSize = 10, ...queryParams } = params;
        const data = await pageDictList({
          pageNum,
          pageSize,
          params: queryParams,
        });

        this.dictList = data.records || [];
        this.dictPagination = {
          pageNum: data.pageNum,
          pageSize: data.pageSize,
          total: data.total,
        };

        return data;
      } catch (error) {
        return Promise.reject(error);
      } finally {
        this.loading = false;
      }
    },

    /**
     * 查询所有字典列表
     * @param {Object} params - 查询参数
     */
    async fetchDictList(params = {}) {
      this.loading = true;
      try {
        const data = await getDictList(params);
        this.dictList = data || [];
        return data;
      } catch (error) {
        return Promise.reject(error);
      } finally {
        this.loading = false;
      }
    },

    /**
     * 新增字典
     * @param {Object} dictData - 字典数据
     */
    async createDict(dictData) {
      try {
        const data = await addDict(dictData);
        await this.fetchDictList();
        return data;
      } catch (error) {
        return Promise.reject(error);
      }
    },

    /**
     * 获取字典详情
     * @param {string} id - 字典ID
     */
    async fetchDictDetail(id) {
      try {
        const data = await getDictDetail(id);
        this.currentDict = data;
        return data;
      } catch (error) {
        return Promise.reject(error);
      }
    },

    /**
     * 更新字典
     * @param {Object} dictData - 字典数据
     */
    async modifyDict(dictData) {
      try {
        await updateDict(dictData);
        await this.fetchDictList();
        if (this.currentDict && this.currentDict.id === dictData.id) {
          await this.fetchDictDetail(dictData.id);
        }
      } catch (error) {
        return Promise.reject(error);
      }
    },

    /**
     * 删除字典
     * @param {string} id - 字典ID
     */
    async removeDict(id) {
      try {
        await deleteDict(id);
        await this.fetchDictList();
        if (this.currentDict && this.currentDict.id === id) {
          this.currentDict = null;
        }
      } catch (error) {
        return Promise.reject(error);
      }
    },

    // ==================== 字典项相关 ====================

    /**
     * 分页查询字典项列表
     * @param {Object} params - 查询参数
     */
    async getDictItemListPage(params = {}) {
      this.loading = true;
      try {
        const { pageNum = 1, pageSize = 10, ...queryParams } = params;
        const data = await pageDictItemList({
          pageNum,
          pageSize,
          params: queryParams,
        });

        this.dictItemList = data.records || [];
        this.dictItemPagination = {
          pageNum: data.pageNum,
          pageSize: data.pageSize,
          total: data.total,
        };

        return data;
      } catch (error) {
        return Promise.reject(error);
      } finally {
        this.loading = false;
      }
    },

    /**
     * 根据字典编码获取字典项
     * @param {string} dictCode - 字典编码
     * @param {boolean} useCache - 是否使用缓存
     */
    async fetchDictItemsByCode(dictCode, useCache = true) {
      // 如果使用缓存且缓存中有数据，直接返回
      if (useCache && this.dictCache[dictCode]) {
        return this.dictCache[dictCode];
      }

      try {
        const data = await getDictItemsByCode(dictCode);
        // 缓存数据
        this.dictCache[dictCode] = data || [];
        return data;
      } catch (error) {
        return Promise.reject(error);
      }
    },

    /**
     * 根据字典ID获取字典项
     * @param {string} dictId - 字典ID
     */
    async fetchDictItemsByDictId(dictId) {
      try {
        const data = await getDictItemsByDictId(dictId);
        this.dictItemList = data || [];
        return data;
      } catch (error) {
        return Promise.reject(error);
      }
    },

    /**
     * 新增字典项
     * @param {Object} dictItemData - 字典项数据
     */
    async createDictItem(dictItemData) {
      try {
        const data = await addDictItem(dictItemData);
        // 清除相关缓存
        this.clearDictCache(dictItemData.dictId);
        return data;
      } catch (error) {
        return Promise.reject(error);
      }
    },

    /**
     * 获取字典项详情
     * @param {string} id - 字典项ID
     */
    async fetchDictItemDetail(id) {
      try {
        const data = await getDictItemDetail(id);
        this.currentDictItem = data;
        return data;
      } catch (error) {
        return Promise.reject(error);
      }
    },

    /**
     * 更新字典项
     * @param {Object} dictItemData - 字典项数据
     */
    async modifyDictItem(dictItemData) {
      try {
        await updateDictItem(dictItemData);
        // 清除相关缓存
        this.clearDictCache(dictItemData.dictId);
        if (this.currentDictItem && this.currentDictItem.id === dictItemData.id) {
          await this.fetchDictItemDetail(dictItemData.id);
        }
      } catch (error) {
        return Promise.reject(error);
      }
    },

    /**
     * 删除字典项
     * @param {string} id - 字典项ID
     * @param {string} dictId - 字典ID（用于清除缓存）
     */
    async removeDictItem(id, dictId) {
      try {
        await deleteDictItem(id);
        // 清除相关缓存
        if (dictId) {
          this.clearDictCache(dictId);
        }
        if (this.currentDictItem && this.currentDictItem.id === id) {
          this.currentDictItem = null;
        }
      } catch (error) {
        return Promise.reject(error);
      }
    },

    /**
     * 清除字典缓存
     * @param {string} dictId - 字典ID（可选，不传则清除所有）
     */
    clearDictCache(dictId) {
      if (dictId) {
        // 清除指定字典的缓存
        const dict = this.dictList.find((d) => d.id === dictId);
        if (dict && dict.dictCode) {
          delete this.dictCache[dict.dictCode];
        }
      } else {
        // 清除所有缓存
        this.dictCache = {};
      }
    },

    /**
     * 清空当前字典
     */
    clearCurrentDict() {
      this.currentDict = null;
    },

    /**
     * 清空当前字典项
     */
    clearCurrentDictItem() {
      this.currentDictItem = null;
    },

    /**
     * 重置状态
     */
    resetState() {
      this.dictList = [];
      this.dictItemList = [];
      this.currentDict = null;
      this.currentDictItem = null;
      this.dictPagination = {
        pageNum: 1,
        pageSize: 10,
        total: 0,
      };
      this.dictItemPagination = {
        pageNum: 1,
        pageSize: 10,
        total: 0,
      };
      this.dictCache = {};
      this.loading = false;
    },
  },
});

