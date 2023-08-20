package com.hoki.util;

import cn.hutool.core.lang.Console;
import cn.hutool.core.util.ReflectUtil;
import com.hoki.enums.Pay_PLATFORM_TYPE;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 获取指定类的字段名称工具类
 *
 * @author Hoki_Lin
 * @date 2023-08-20
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ModifierUtil {

    /**
     * 修饰符类型
     */
    @Getter
    @AllArgsConstructor
    enum MODIFIER_TYPE {
        PRIVATE("private"),
        PUBLIC("public"),
        PROTECTED("protected");

        private final String desc;
    }


    /**
     * 获取指定类的私有属性名称
     *
     * @param clazz 指定类
     * @return
     */
    public static List<String> getPrivateFieldName(Class<?> clazz) {
        return getFieldName(clazz, MODIFIER_TYPE.PRIVATE.getDesc());
    }

    /**
     * 获取指定类的公共属性名称
     *
     * @param clazz 指定类
     * @return
     */
    public static List<String> getPublicFieldName(Class<?> clazz) {
        return getFieldName(clazz, MODIFIER_TYPE.PUBLIC.getDesc());
    }

    /**
     * 获取指定类的受保护属性名称
     *
     * @param clazz 指定类
     * @return
     */
    public static List<String> getProtectedPublicFieldName(Class<?> clazz) {
        return getFieldName(clazz, MODIFIER_TYPE.PROTECTED.getDesc());
    }

    /**
     * 获取指定类的属性名称
     *
     * @param clazz    指定类
     * @param modifier 修饰符
     * @return
     */
    public static List<String> getFieldName(Class<?> clazz, String modifier) {
        List<String> fieldNameList = new ArrayList<>();
        Map<String, Field> fieldMap = ReflectUtil.getFieldMap(clazz);
        int i = 0;
        int defaultLength = Integer.MAX_VALUE;

        boolean isEnumFlag = clazz.isEnum();
        // 隐藏枚举类底层的私有属性
        if (isEnumFlag && modifier.equals(MODIFIER_TYPE.PRIVATE.getDesc())) {
            defaultLength = 2;
        }

        for (Map.Entry<String, Field> entry : fieldMap.entrySet()) {
            if (entry.getValue().toString().startsWith(modifier) && i < defaultLength && !entry.getKey().startsWith("$")) {
                i++;
                fieldNameList.add(entry.getKey());
            }
        }
        return fieldNameList;
    }

    public static void main(String[] args) {
        List<String> fieldNameList = ModifierUtil.getPrivateFieldName(Pay_PLATFORM_TYPE.class);
        for (String fieldName : fieldNameList) {
            Console.log("{}", fieldName);
        }
    }

}
 