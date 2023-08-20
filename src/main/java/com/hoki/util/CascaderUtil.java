package com.hoki.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReflectUtil;
import com.hoki.entity.CascaderNode;
import com.hoki.enums.Pay_PLATFORM_TYPE;
import com.hoki.entity.User;

import java.lang.reflect.Field;
import java.util.*;


/**
 * 二级菜单处理工具类
 *
 * @author Hoki_Lin
 * @date 2023-08-20
 */
public class CascaderUtil {

    /**
     * 通过字段名称获取指定对象的字段值
     *
     * @param fieldName
     * @param obj
     * @return
     */
    public static Object getValueByFieldName(String fieldName, Object obj) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("通过 " + fieldName + " 字段获取对应值失败！" + e.getMessage());
        }
    }


    /**
     * 将枚举类型转为Map
     *
     * @param array
     * @return
     */
    public static Map<String, String> convertEnumToMap(Enum[] array) {
        Map<String, String> map = new LinkedHashMap<>();

        Class<?> componentType = array.getClass().getComponentType();
        List<String> fieldNames = ModifierUtil.getPrivateFieldName(componentType);

        for (Enum t : array) {
            map.put(getValueByFieldName(fieldNames.get(0), t).toString(), getValueByFieldName(fieldNames.get(fieldNames.size() - 1), t).toString());
        }
        return map;
    }

    /**
     * 处理二级菜单列表
     *
     * @param list  待处理的列表
     * @param array 枚举数组
     * @param t     结果类型
     * @param <T>   结果泛型
     * @param <R>   参数泛型
     * @return 处理后的二级菜单列表
     */
    public static <T, R> List<T> handleList(List<R> list, Enum[] array, Class<T> t) {
        return handleList(list, convertEnumToMap(array), t);
    }

    /**
     * 处理二级菜单列表
     *
     * @param list    待处理的列表
     * @param dictMap 字典枚举映射
     * @param t       结果类型
     * @param <T>     结果泛型
     * @param <R>     参数泛型
     * @return 处理后的二级菜单列表
     */
    public static <T, R> List<T> handleList(List<R> list, Map<String, String> dictMap, Class<T> t) {
        // 二级菜单
        List<T> tree = new LinkedList<>();
        // 存放存在的类型
        Map<String, List<T>> typeMap = new HashMap<>();
        // 子节点
        T childrenNode;
        // 叶子节点
        T leafNode;
        // 子节点的后继
        List<T> children;

        // 获取节点字段名称
        Field[] fields = ReflectUtil.getFields(t);
        Field userIdField = fields[0];
        Field nickNameField = fields[1];
        Field typeField = fields[2];


        // 设置前缀和名称
        if (CollUtil.isNotEmpty(list)) {
            for (R vo : list) {
                Object[] fieldsValue = ReflectUtil.getFieldsValue(vo);
                String userId = (String) fieldsValue[0];
                String nickName = (String) fieldsValue[1];
                String type = (String) fieldsValue[2];

                childrenNode = ReflectUtil.newInstance(t);
                leafNode = ReflectUtil.newInstance(t);
                ReflectUtil.setFieldValue(leafNode, userIdField, userId);
                ReflectUtil.setFieldValue(leafNode, nickNameField, nickName);

                // 如果类型不存在，则对菜单进行增加操作
                if (!typeMap.containsKey(type)) {
                    ReflectUtil.setFieldValue(childrenNode, userIdField, type);
                    ReflectUtil.setFieldValue(childrenNode, nickNameField, dictMap.get(type));


                    children = new LinkedList<>();
                    children.add(leafNode);
                    ReflectUtil.setFieldValue(childrenNode, typeField, children);

                    tree.add(childrenNode);

                    typeMap.put(type, children);
                    continue;
                }
                // 如果当前类型存在，则对当前节点进行追加操作
                children = typeMap.get(type);
                children.add(leafNode);
            }
        }

        // 补全其他类型
        for (Map.Entry<String, String> entry : dictMap.entrySet()) {
            if (!typeMap.containsKey(entry.getKey())) {
                childrenNode = ReflectUtil.newInstance(t);
                ReflectUtil.setFieldValue(childrenNode, userIdField, entry.getKey());
                ReflectUtil.setFieldValue(childrenNode, nickNameField, entry.getValue());

                children = new ArrayList<>();
                ReflectUtil.setFieldValue(childrenNode, typeField, children);
                tree.add(childrenNode);
            }
        }

        return tree;
    }


    public static void main(String[] args) {
        List<User> list = new ArrayList<>();
        list.add(new User("1", "小明", "1"));
        list.add(new User("2", "小红", "2"));
        list.add(new User("3", "小蓝", "3"));
        list.add(new User("4", "小白", "4"));

        List<CascaderNode> cascaderNodeList = CascaderUtil.handleList(list, Pay_PLATFORM_TYPE.values(), CascaderNode.class);
        System.out.println(cascaderNodeList);
    }
}