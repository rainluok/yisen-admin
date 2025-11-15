package com.yisen.module.system.user.model.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @author rainluo
 * @version 1.0
 * @description
 * @date 2025/11/14 10:47
 */
@Data
@Builder
public class LoginInfoVO {
    private String token;
    private UserInfoVO userInfo;
}
