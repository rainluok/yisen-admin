/**
 * 响应状态码枚举
 * 对应后端 ResponseCodeEnum
 */
export const ResponseCode = {
  // 成功
  SUCCESS: 200,
  FAIL: 500,

  // 客户端错误 4xx
  BAD_REQUEST: 400,
  UNAUTHORIZED: 401,
  FORBIDDEN: 403,
  NOT_FOUND: 404,
  METHOD_NOT_ALLOWED: 405,
  REQUEST_TIMEOUT: 408,
  CONFLICT: 409,
  TOO_MANY_REQUESTS: 429,

  // 服务器错误 5xx
  INTERNAL_SERVER_ERROR: 500,
  BAD_GATEWAY: 502,
  SERVICE_UNAVAILABLE: 503,
  GATEWAY_TIMEOUT: 504,

  // 业务错误 1xxx
  BUSINESS_ERROR: 1000,
  VALIDATION_ERROR: 1001,
  DUPLICATE_KEY_ERROR: 1002,
  DATA_NOT_FOUND: 1003,
  DATA_ALREADY_EXISTS: 1004,

  // 用户相关错误 2xxx
  USER_NOT_FOUND: 2001,
  USER_DISABLED: 2002,
  USER_LOCKED: 2003,
  USERNAME_PASSWORD_ERROR: 2004,
  USERNAME_EXISTS: 2005,
  USER_NOT_LOGIN: 2006,
  PERMISSION_DENIED: 2009,
  OLD_PASSWORD_ERROR: 2010,
  PASSWORD_ERROR: 2011,
  USER_ALREADY_EXISTS: 2012,
  USER_DELETED: 2013,
  INVALID_STATUS: 2014,
  EMAIL_ALREADY_EXISTS: 2015,

  // 角色相关错误 21xx
  ROLE_NOT_FOUND: 2101,
  ROLE_ALREADY_EXISTS: 2102,
  ROLE_DELETED: 2103,

  // 菜单相关错误 22xx
  MENU_NOT_FOUND: 2201,
  MENU_IDS_EMPTY: 2202,
  MENU_CODE_EXISTS: 2203,
  MENU_DELETED: 2204,
  MENU_HAS_CHILDREN: 2205,

  // 部门相关错误 23xx
  DEPART_NOT_FOUND: 2301,
  DEPART_CODE_EXISTS: 2302,
  DEPART_DELETED: 2303,
  DEPART_HAS_CHILDREN: 2304,
  DEPART_HAS_USERS: 2305,
  DEPART_HAS_USERS_NOT_IN_THIS_DEPART: 2306,
  DEPART_HAS_USERS_NOT_IN_THIS_DEPART_OR_CHILDREN_DEPART: 2307,
  PARENT_DEPART_NOT_EXISTS: 2308,
  DEPART_PARENT_CANNOT_BE_SELF: 2309,

  // 字典相关错误 24xx
  DICT_NOT_EXISTS: 24001,
  DICT_ITEM_VALUE_EXISTS: 2402,
  DICT_ITEM_NOT_EXISTS: 2403,
  DICT_CODE_EXISTS: 2404,

  // 日志相关错误 25xx
  LOG_NOT_EXISTS: 2501,

  // 安全类错误码 4xxx
  TOKEN_INVALID: 4001,
  TOKEN_EXPIRED: 4002,
  TOKEN_KICKED_OUT: 4003,
  IP_BLACKLISTED: 4004,
  REPEAT_SUBMIT: 4005,
  RATE_LIMIT_EXCEEDED: 4006,

  // 文件相关错误 3xxx
  FILE_UPLOAD_ERROR: 3001,
  FILE_SIZE_EXCEEDED: 3002,
  FILE_TYPE_NOT_ALLOWED: 3003,
  FILE_NOT_FOUND: 3004,
  FILE_DOWNLOAD_ERROR: 3005,

  // 数据库相关错误
  DATABASE_ERROR: 4001,
  SQL_SYNTAX_ERROR: 4002,
  CONSTRAINT_VIOLATION: 4003,

  // 第三方服务错误 5xxx
  THIRD_PARTY_SERVICE_ERROR: 5001,
  SMS_SEND_ERROR: 5002,
  EMAIL_SEND_ERROR: 5003,
};

/**
 * 响应消息映射
 */
export const ResponseMessage = {
  [ResponseCode.SUCCESS]: '操作成功',
  [ResponseCode.FAIL]: '操作失败',

  // 客户端错误
  [ResponseCode.BAD_REQUEST]: '请求参数错误',
  [ResponseCode.UNAUTHORIZED]: '未授权，请先登录',
  [ResponseCode.FORBIDDEN]: '没有权限访问',
  [ResponseCode.NOT_FOUND]: '请求的资源不存在',
  [ResponseCode.METHOD_NOT_ALLOWED]: '请求方法不允许',
  [ResponseCode.REQUEST_TIMEOUT]: '请求超时',
  [ResponseCode.CONFLICT]: '数据冲突',
  [ResponseCode.TOO_MANY_REQUESTS]: '请求过于频繁，请稍后再试',

  // 服务器错误
  [ResponseCode.INTERNAL_SERVER_ERROR]: '服务器内部错误',
  [ResponseCode.BAD_GATEWAY]: '网关错误',
  [ResponseCode.SERVICE_UNAVAILABLE]: '服务暂时不可用',
  [ResponseCode.GATEWAY_TIMEOUT]: '网关超时',

  // 业务错误
  [ResponseCode.BUSINESS_ERROR]: '业务处理失败',
  [ResponseCode.VALIDATION_ERROR]: '数据校验失败',
  [ResponseCode.DUPLICATE_KEY_ERROR]: '数据重复',
  [ResponseCode.DATA_NOT_FOUND]: '数据不存在',
  [ResponseCode.DATA_ALREADY_EXISTS]: '数据已存在',

  // 用户相关错误
  [ResponseCode.USER_NOT_FOUND]: '用户不存在',
  [ResponseCode.USER_DISABLED]: '用户已被禁用',
  [ResponseCode.USER_LOCKED]: '用户已被锁定',
  [ResponseCode.USERNAME_PASSWORD_ERROR]: '用户名或密码错误',
  [ResponseCode.USERNAME_EXISTS]: '用户名已存在',
  [ResponseCode.USER_NOT_LOGIN]: '用户未登录',
  [ResponseCode.PERMISSION_DENIED]: '没有访问权限',
  [ResponseCode.OLD_PASSWORD_ERROR]: '原密码错误',
  [ResponseCode.PASSWORD_ERROR]: '密码错误',
  [ResponseCode.USER_ALREADY_EXISTS]: '用户已存在',
  [ResponseCode.USER_DELETED]: '用户已被删除',
  [ResponseCode.INVALID_STATUS]: '状态值不合法',
  [ResponseCode.EMAIL_ALREADY_EXISTS]: '邮箱已存在',

  // 角色相关错误
  [ResponseCode.ROLE_NOT_FOUND]: '角色不存在',
  [ResponseCode.ROLE_ALREADY_EXISTS]: '角色已存在',
  [ResponseCode.ROLE_DELETED]: '角色已被删除',

  // 菜单相关错误
  [ResponseCode.MENU_NOT_FOUND]: '菜单不存在',
  [ResponseCode.MENU_IDS_EMPTY]: '菜单ID列表不能为空',
  [ResponseCode.MENU_CODE_EXISTS]: '菜单编码已存在',
  [ResponseCode.MENU_DELETED]: '菜单已被删除',
  [ResponseCode.MENU_HAS_CHILDREN]: '菜单下有子菜单，无法删除',

  // 部门相关错误
  [ResponseCode.DEPART_NOT_FOUND]: '部门不存在',
  [ResponseCode.DEPART_CODE_EXISTS]: '部门编码已存在',
  [ResponseCode.DEPART_DELETED]: '部门已被删除',
  [ResponseCode.DEPART_HAS_CHILDREN]: '部门下有子部门，无法删除',
  [ResponseCode.DEPART_HAS_USERS]: '部门下有用户，无法删除',
  [ResponseCode.DEPART_HAS_USERS_NOT_IN_THIS_DEPART]: '部门下有用户，请先将用户移出部门',
  [ResponseCode.DEPART_HAS_USERS_NOT_IN_THIS_DEPART_OR_CHILDREN_DEPART]: '部门下有用户，请先将用户移出部门或子部门',
  [ResponseCode.PARENT_DEPART_NOT_EXISTS]: '上级部门不存在',
  [ResponseCode.DEPART_PARENT_CANNOT_BE_SELF]: '上级部门不能为自己',

  // 字典相关错误
  [ResponseCode.DICT_NOT_EXISTS]: '字典不存在',
  [ResponseCode.DICT_ITEM_VALUE_EXISTS]: '字典项值已存在',
  [ResponseCode.DICT_ITEM_NOT_EXISTS]: '字典项不存在',
  [ResponseCode.DICT_CODE_EXISTS]: '字典编码已存在',

  // 日志相关错误
  [ResponseCode.LOG_NOT_EXISTS]: '日志不存在',

  // 安全类错误
  [ResponseCode.TOKEN_INVALID]: 'Token无效',
  [ResponseCode.TOKEN_EXPIRED]: 'Token已过期',
  [ResponseCode.TOKEN_KICKED_OUT]: '账号已在其他设备登录，您已被踢下线',
  [ResponseCode.IP_BLACKLISTED]: 'IP已被封禁',
  [ResponseCode.REPEAT_SUBMIT]: '请勿重复提交',
  [ResponseCode.RATE_LIMIT_EXCEEDED]: '请求过于频繁，请稍后再试',

  // 文件相关错误
  [ResponseCode.FILE_UPLOAD_ERROR]: '文件上传失败',
  [ResponseCode.FILE_SIZE_EXCEEDED]: '文件大小超出限制',
  [ResponseCode.FILE_TYPE_NOT_ALLOWED]: '文件类型不允许',
  [ResponseCode.FILE_NOT_FOUND]: '文件不存在',
  [ResponseCode.FILE_DOWNLOAD_ERROR]: '文件下载失败',

  // 数据库相关错误
  [ResponseCode.DATABASE_ERROR]: '数据库操作失败',
  [ResponseCode.SQL_SYNTAX_ERROR]: 'SQL语法错误',
  [ResponseCode.CONSTRAINT_VIOLATION]: '违反约束条件',

  // 第三方服务错误
  [ResponseCode.THIRD_PARTY_SERVICE_ERROR]: '第三方服务调用失败',
  [ResponseCode.SMS_SEND_ERROR]: '短信发送失败',
  [ResponseCode.EMAIL_SEND_ERROR]: '邮件发送失败',
};

/**
 * 获取响应消息
 * @param {number} code - 响应码
 * @returns {string} 响应消息
 */
export const getResponseMessage = (code) => {
  return ResponseMessage[code] || '未知错误';
};
