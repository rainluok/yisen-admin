package com.yisen.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.yisen.common.util.UUIDUtil;
import com.yisen.core.context.LoginUserContext;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

/**
 * @author rainluo
 * @version 1.1
 * @description mybatis plus 配置类，自动填充ID、创建/更新时间、创建人/修改人
 * @date 2025/11/14 10:10
 */
@Configuration
public class MybatisPlusConfig implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        String currentUserId = LoginUserContext.getUserId();
        // 填充ID（如果字段名为"id"，且为null）
        if (metaObject.hasSetter("id") && getFieldValByName("id", metaObject) == null) {
            setFieldValByName("id", UUIDUtil.generateUUID(), metaObject);
        }
        // 创建时间
        if (metaObject.hasSetter("createTime") && getFieldValByName("createTime", metaObject) == null) {
            setFieldValByName("createTime", new Date(), metaObject);
        }
        // 更新时间
        if (metaObject.hasSetter("updateTime") && getFieldValByName("updateTime", metaObject) == null) {
            setFieldValByName("updateTime", new Date(), metaObject);
        }
        // 创建人
        if (metaObject.hasSetter("createBy") && getFieldValByName("createBy", metaObject) == null) {
            setFieldValByName("createBy", currentUserId, metaObject);
        }
        // 修改人
        if (metaObject.hasSetter("updateBy") && getFieldValByName("updateBy", metaObject) == null) {
            setFieldValByName("updateBy", currentUserId, metaObject);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        // 更新时间
        if (metaObject.hasSetter("updateTime")) {
            setFieldValByName("updateTime", new Date(), metaObject);
        }
        // 修改人
        if (metaObject.hasSetter("updateBy")) {
            setFieldValByName("updateBy", LoginUserContext.getUserId(), metaObject);
        }
    }
}
