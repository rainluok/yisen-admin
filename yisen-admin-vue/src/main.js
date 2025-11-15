import { createApp } from 'vue';
import { createPinia } from 'pinia';
import App from './App.vue';
import ElementPlus from 'element-plus';
import 'element-plus/dist/index.css';
import router from './router';
import { setupPlugins } from './plugins';
import { setupDirectives } from './directives';
import { printEnvInfo } from './utils/env';
import 'reset-css';
import * as ElementPlusIconsVue from '@element-plus/icons-vue'


// 打印环境信息
printEnvInfo();

// 创建应用实例
const app = createApp(App);

// 创建 Pinia 实例
const pinia = createPinia();

// 注册插件
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}
app.use(ElementPlus);
setupPlugins(app);

// 注册指令
setupDirectives(app);

// 使用插件
app.use(pinia);
app.use(router);

// 挂载应用
app.mount('#app');
