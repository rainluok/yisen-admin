/**
 * SM4 加密解密工具
 * 注意：这里使用示例实现，实际项目中请使用成熟的 SM4 加密库，如 sm-crypto
 */

// SM4 密钥（实际使用时应从配置中获取）
const SM4_KEY = "0123456789abcdeffedcba9876543210";

/**
 * SM4 加密
 * @param {string} data - 需要加密的数据
 * @param {string} key - 加密密钥
 * @returns {string} 加密后的数据
 */
export function sm4Encrypt(data, key = SM4_KEY) {
  // 实际项目中应使用 sm-crypto 等库进行加密
  // 示例: import { sm4 } from 'sm-crypto'
  // return sm4.encrypt(data, key)

  console.warn("SM4 加密功能需要集成 sm-crypto 库");
  return data;
}

/**
 * SM4 解密
 * @param {string} encryptedData - 加密的数据
 * @param {string} key - 解密密钥
 * @returns {string} 解密后的数据
 */
export function sm4Decrypt(encryptedData, key = SM4_KEY) {
  // 实际项目中应使用 sm-crypto 等库进行解密
  // 示例: import { sm4 } from 'sm-crypto'
  // return sm4.decrypt(encryptedData, key)

  console.warn("SM4 解密功能需要集成 sm-crypto 库");
  return encryptedData;
}

/**
 * 加密敏感字段
 * @param {Object} obj - 包含敏感字段的对象
 * @param {Array} fields - 需要加密的字段列表
 * @returns {Object} 加密后的对象
 */
export function encryptFields(obj, fields = ["password"]) {
  const result = { ...obj };
  fields.forEach((field) => {
    if (result[field]) {
      result[field] = sm4Encrypt(result[field]);
    }
  });
  return result;
}

/**
 * 解密敏感字段
 * @param {Object} obj - 包含加密字段的对象
 * @param {Array} fields - 需要解密的字段列表
 * @returns {Object} 解密后的对象
 */
export function decryptFields(obj, fields = ["password"]) {
  const result = { ...obj };
  fields.forEach((field) => {
    if (result[field]) {
      result[field] = sm4Decrypt(result[field]);
    }
  });
  return result;
}

/**
 * 使用说明：
 *
 * 1. 安装 sm-crypto：
 *    pnpm add sm-crypto
 *
 * 2. 导入并使用：
 *    import { sm4 } from 'sm-crypto'
 *    const key = '0123456789abcdeffedcba9876543210' // 32位16进制字符串
 *    const encrypted = sm4.encrypt('hello world', key)
 *    const decrypted = sm4.decrypt(encrypted, key)
 */
