package com.seu.platform.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

@Slf4j
@UtilityClass
public class BeanUtil {
    /**
     * bean互换
     *
     * @param entity entity
     * @param tClass class
     * @param <T>    vo类
     * @return 转换后的bean
     */
    public static <T> T convertBean(Object entity, Class<T> tClass) {
        T t = null;
        try {
            t = tClass.newInstance();
            if(entity==null){
                return t;
            }
            BeanUtils.copyProperties(entity, t);
        } catch (InstantiationException | IllegalAccessException e) {
            log.error("entity转vo异常，entity:{}", entity, e);
        }
        return t;
    }
}
