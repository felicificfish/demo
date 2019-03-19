package com.example.demo.utils;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Bean映射工具类
 *
 * @author zhou.xy
 * @since 2019/3/19
 */
public class BeanMapperUtil {
    private static Mapper MAPPER = DozerBeanMapperBuilder.buildDefault();

    /**
     * 普通对象转换 比如: ADO -> AVO
     *
     * @param: [source 源对象, destinationClass 目标对象class]
     * @return: T
     */
    public static <T> T map(Object source, Class<T> destinationClass) {
        if (source == null) {
            return null;
        }
        return MAPPER.map(source, destinationClass);
    }

    /**
     * List转换 比如: List<ADO> -> List<AVO>
     *
     * @param: [sourceList 源对象List, destinationClass 目标对象class]
     * @return: java.util.List<T>
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <T> List<T> mapList(Collection sourceList, Class<T> destinationClass) {
        List<T> destinationList = new ArrayList();
        if (sourceList == null) {
            return destinationList;
        }
        for (Object sourceObject : sourceList) {
            if (sourceObject != null) {
                T destinationObject = MAPPER.map(sourceObject, destinationClass);
                destinationList.add(destinationObject);
            }
        }
        return destinationList;
    }
}
