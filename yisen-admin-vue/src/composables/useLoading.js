import { ref } from 'vue'

/**
 * 加载状态管理
 */
export function useLoading(initialState = false) {
  const loading = ref(initialState)

  const setLoading = (value) => {
    loading.value = value
  }

  const startLoading = () => {
    loading.value = true
  }

  const stopLoading = () => {
    loading.value = false
  }

  const withLoading = async (fn) => {
    startLoading()
    try {
      return await fn()
    } finally {
      stopLoading()
    }
  }

  return {
    loading,
    setLoading,
    startLoading,
    stopLoading,
    withLoading
  }
}

