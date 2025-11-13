import { defineConfig, loadEnv } from 'vite';
import vue from '@vitejs/plugin-vue';
import path from 'path';

// 新增 Element Plus 自动导入插件及相关解析器
import AutoImport from 'unplugin-auto-import/vite';
import Components from 'unplugin-vue-components/vite';
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers';

export default defineConfig(({ mode }) => {
  // 加载 .env 文件
  const env = loadEnv(mode, process.cwd(), '');
  const isBuild = mode === 'production';

  console.log('🚀 当前环境:', mode);
  console.log('🌐 API地址:', env.VITE_API_BASE_URL || '未配置');

  return {
    plugins: [
      vue(),
      // 自动导入 Element Plus API
      AutoImport({
        resolvers: [ElementPlusResolver()],
        // 可选补充：自动导入 Vue 相关API
        imports: ['vue', 'vue-router', 'pinia'],
        dts: 'src/auto-imports.d.ts',
      }),
      // 自动导入 Element Plus 组件
      Components({
        resolvers: [ElementPlusResolver()],
        dts: 'src/components.d.ts',
      }),
    ],
    resolve: {
      alias: {
        '@': path.resolve(__dirname, 'src'), // 路径别名
        '@components': path.resolve(__dirname, 'src/components'),
        '@utils': path.resolve(__dirname, 'src/utils'),
        '@config': path.resolve(__dirname, 'src/config'),
        '@types': path.resolve(__dirname, 'src/types'),
      },
      extensions: ['.js', '.vue', '.json', '.ts'],
    },
    server: {
      host: '0.0.0.0', // 允许外部访问
      port: Number(env.VITE_PORT) || 3000,
      open: env.VITE_OPEN === 'true',
      https: env.VITE_HTTPS === 'true',
      proxy: {
        // 代理 API 请求
        [env.VITE_API_PREFIX || '/api']: {
          target: env.VITE_API_BASE_URL || 'http://localhost:8080',
          changeOrigin: true,
          rewrite: (path) => path.replace(new RegExp(`^${env.VITE_API_PREFIX || '/api'}`), ''),
        },
      },
    },
    build: {
      outDir: env.VITE_OUTPUT_DIR || 'dist',
      sourcemap: env.VITE_SOURCEMAP === 'true',
      chunkSizeWarningLimit: 1500,
      rollupOptions: {
        output: {
          // 分包策略
          manualChunks: {
            vue: ['vue', 'vue-router', 'pinia'],
            elementPlus: ['element-plus'],
            axios: ['axios'],
            utils: ['dayjs', 'lodash'],
          },
          // 静态资源分类
          chunkFileNames: 'js/[name]-[hash].js',
          entryFileNames: 'js/[name]-[hash].js',
          assetFileNames: '[ext]/[name]-[hash].[ext]',
        },
      },
      minify: 'esbuild',
      target: 'es2015',
    },
    base: './',
    define: {
      'process.env': env,
      __APP_VERSION__: JSON.stringify(env.VITE_APP_VERSION || '1.0.0'),
      __APP_TITLE__: JSON.stringify(env.VITE_APP_TITLE || '益森管理系统'),
    },
    css: {
      preprocessorOptions: {
        scss: {
          additionalData: `@use "@/styles/variables.scss" as *;`,
        },
      },
    },
    optimizeDeps: {
      include: ['vue', 'vue-router', 'pinia', 'axios', 'element-plus'],
    },
    esbuild: {
      drop: env.VITE_DROP_CONSOLE === 'true' ? ['console', 'debugger'] : [],
    },
  };
});
