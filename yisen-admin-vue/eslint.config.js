import js from '@eslint/js';
import globals from 'globals';
import pluginVue from 'eslint-plugin-vue';
import { defineConfig } from 'eslint/config';
import eslintPrettierRecommended from 'eslint-config-prettier';

export default defineConfig([
  {
    files: ['**/*.{js,mjs,cjs,vue}'],
    plugins: { js },
    extends: ['js/recommended'],
    languageOptions: { globals: globals.node },
    rules: {
      // 警告打印
      'no-console': 'warn',

      // 禁止未使用的变量，忽略所有变量名，参数不检查
      'no-unused-vars': ['error', { varsIgnorePattern: '.*', args: 'none' }],

      // 关闭函数括号前空格检查
      'space-before-function-paren': 'off',

      // 关闭Vue属性顺序检查
      'vue/attributes-order': 'off',

      // 关闭每个文件只允许一个组件的检查
      'vue/one-component-per-file': 'off',

      // 关闭HTML标签闭合换行检查
      'vue/html-closing-bracket-newline': 'off',

      // 关闭每行最大属性数检查
      'vue/max-attributes-per-line': 'off',

      // 关闭多行HTML元素内容换行检查
      'vue/multiline-html-element-content-newline': 'off',

      // 关闭单行HTML元素内容换行检查
      'vue/singleline-html-element-content-newline': 'off',

      // 关闭属性连字符命名检查
      'vue/attribute-hyphenation': 'off',

      // 关闭必须有默认prop的检查
      'vue/require-default-prop': 'off',

      // 组件名必须为多词，忽略index
      'vue/multi-word-component-names': [
        'error',
        {
          ignores: ['index'], // 需要忽略的组件名
          ignores: ['App'], // 需要忽略的组件名
        },
      ],

      // HTML标签自闭合风格
      'vue/html-self-closing': [
        'error',
        {
          html: {
            void: 'always', // 空元素总是自闭合
            normal: 'never', // 普通元素不自闭合
            component: 'always', // 组件总是自闭合
          },
          svg: 'always', // SVG标签总是自闭合
          math: 'always', // MathML标签总是自闭合
        },
      ],
    },
  },
  pluginVue.configs['flat/essential'],
  eslintPrettierRecommended,
]);
