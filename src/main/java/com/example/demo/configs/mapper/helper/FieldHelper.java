package com.example.demo.configs.mapper.helper;

import com.example.demo.configs.exception.ValidateException;
import com.example.demo.configs.mapper.entity.EntityField;

import javax.persistence.Entity;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.util.*;

public class FieldHelper {
    private static final FieldHelper.IFieldHelper fieldHelper;

    public FieldHelper() {
    }

    public static List<EntityField> getFields(Class<?> entityClass) {
        return fieldHelper.getFields(entityClass);
    }

    public static List<EntityField> getProperties(Class<?> entityClass) {
        return fieldHelper.getProperties(entityClass);
    }

    public static List<EntityField> getAll(Class<?> entityClass) {
        List<EntityField> fields = fieldHelper.getFields(entityClass);
        List<EntityField> properties = fieldHelper.getProperties(entityClass);
        List<EntityField> all = new ArrayList();
        Set<EntityField> usedSet = new HashSet();

        Iterator var5;
        EntityField property;
        for (var5 = fields.iterator(); var5.hasNext(); all.add(property)) {
            property = (EntityField) var5.next();
            Iterator var7 = properties.iterator();

            while (var7.hasNext()) {
                property = (EntityField) var7.next();
                if (!usedSet.contains(property) && property.getName().equals(property.getName())) {
                    property.copyFromPropertyDescriptor(property);
                    usedSet.add(property);
                    break;
                }
            }
        }

        var5 = properties.iterator();

        while (var5.hasNext()) {
            property = (EntityField) var5.next();
            if (!usedSet.contains(property)) {
                all.add(property);
            }
        }

        return all;
    }

    static {
        String version = System.getProperty("java.version");
        if (!version.contains("1.6.") && !version.contains("1.7.")) {
            fieldHelper = new FieldHelper.Jdk8FieldHelper();
        } else {
            fieldHelper = new FieldHelper.Jdk6_7FieldHelper();
        }

    }

    static class Jdk6_7FieldHelper implements FieldHelper.IFieldHelper {
        Jdk6_7FieldHelper() {
        }

        public List<EntityField> getFields(Class<?> entityClass) {
            List<EntityField> fieldList = new ArrayList();
            this._getFields(entityClass, fieldList, this._getGenericTypeMap(entityClass), (Integer) null);
            return fieldList;
        }

        public List<EntityField> getProperties(Class<?> entityClass) {
            Map<String, Class<?>> genericMap = this._getGenericTypeMap(entityClass);
            ArrayList entityFields = new ArrayList();

            BeanInfo beanInfo;
            try {
                beanInfo = Introspector.getBeanInfo(entityClass);
            } catch (IntrospectionException var11) {
                throw new ValidateException(var11.getMessage());
            }

            PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
            PropertyDescriptor[] var6 = descriptors;
            int var7 = descriptors.length;

            for (int var8 = 0; var8 < var7; ++var8) {
                PropertyDescriptor desc = var6[var8];
                if (desc != null && !"class".equals(desc.getName())) {
                    EntityField entityField = new EntityField((Field) null, desc);
                    if (desc.getReadMethod() != null && desc.getReadMethod().getGenericReturnType() != null && desc.getReadMethod().getGenericReturnType() instanceof TypeVariable) {
                        entityField.setJavaType((Class) genericMap.get(((TypeVariable) desc.getReadMethod().getGenericReturnType()).getName()));
                    } else if (desc.getWriteMethod() != null && desc.getWriteMethod().getGenericParameterTypes() != null && desc.getWriteMethod().getGenericParameterTypes().length == 1 && desc.getWriteMethod().getGenericParameterTypes()[0] instanceof TypeVariable) {
                        entityField.setJavaType((Class) genericMap.get(((TypeVariable) desc.getWriteMethod().getGenericParameterTypes()[0]).getName()));
                    }

                    entityFields.add(entityField);
                }
            }

            return entityFields;
        }

        private void _getFields(Class<?> entityClass, List<EntityField> fieldList, Map<String, Class<?>> genericMap, Integer level) {
            if (fieldList == null) {
                throw new NullPointerException("fieldList参数不能为空!");
            } else {
                if (level == null) {
                    level = 0;
                }

                if (entityClass != Object.class) {
                    Field[] fields = entityClass.getDeclaredFields();
                    int index = 0;
                    Field[] var7 = fields;
                    int var8 = fields.length;

                    for (int var9 = 0; var9 < var8; ++var9) {
                        Field field = var7[var9];
                        if (!Modifier.isStatic(field.getModifiers()) && !Modifier.isTransient(field.getModifiers())) {
                            EntityField entityField = new EntityField(field, (PropertyDescriptor) null);
                            if (field.getGenericType() != null && field.getGenericType() instanceof TypeVariable) {
                                if (genericMap == null || !genericMap.containsKey(((TypeVariable) field.getGenericType()).getName())) {
                                    throw new ValidateException(entityClass + "字段" + field.getName() + "的泛型类型无法获取!");
                                }

                                entityField.setJavaType((Class) genericMap.get(((TypeVariable) field.getGenericType()).getName()));
                            } else {
                                entityField.setJavaType(field.getType());
                            }

                            if (level != 0) {
                                fieldList.add(index, entityField);
                                ++index;
                            } else {
                                fieldList.add(entityField);
                            }
                        }
                    }

                    Class<?> superClass = entityClass.getSuperclass();
                    if (superClass != null && !superClass.equals(Object.class) && (superClass.isAnnotationPresent(Entity.class) || !Map.class.isAssignableFrom(superClass) && !Collection.class.isAssignableFrom(superClass))) {
                        level = level + 1;
                        this._getFields(superClass, fieldList, genericMap, level);
                    }

                }
            }
        }

        private Map<String, Class<?>> _getGenericTypeMap(Class<?> entityClass) {
            Map<String, Class<?>> genericMap = new HashMap();
            if (entityClass == Object.class) {
                return genericMap;
            } else {
                Class<?> superClass = entityClass.getSuperclass();
                if (superClass != null && !superClass.equals(Object.class) && (superClass.isAnnotationPresent(Entity.class) || !Map.class.isAssignableFrom(superClass) && !Collection.class.isAssignableFrom(superClass))) {
                    if (entityClass.getGenericSuperclass() instanceof ParameterizedType) {
                        Type[] types = ((ParameterizedType) entityClass.getGenericSuperclass()).getActualTypeArguments();
                        TypeVariable[] typeVariables = superClass.getTypeParameters();
                        if (typeVariables.length > 0) {
                            for (int i = 0; i < typeVariables.length; ++i) {
                                if (types[i] instanceof Class) {
                                    genericMap.put(typeVariables[i].getName(), (Class) types[i]);
                                }
                            }
                        }
                    }

                    genericMap.putAll(this._getGenericTypeMap(superClass));
                }

                return genericMap;
            }
        }
    }

    static class Jdk8FieldHelper implements FieldHelper.IFieldHelper {
        Jdk8FieldHelper() {
        }

        public List<EntityField> getFields(Class<?> entityClass) {
            List<EntityField> fields = this._getFields(entityClass, (List) null, (Integer) null);
            List<EntityField> properties = this.getProperties(entityClass);
            Set<EntityField> usedSet = new HashSet();
            Iterator var5 = fields.iterator();

            while (true) {
                while (var5.hasNext()) {
                    EntityField field = (EntityField) var5.next();
                    Iterator var7 = properties.iterator();

                    while (var7.hasNext()) {
                        EntityField property = (EntityField) var7.next();
                        if (!usedSet.contains(property) && field.getName().equals(property.getName())) {
                            field.setJavaType(property.getJavaType());
                            break;
                        }
                    }
                }

                return fields;
            }
        }

        private List<EntityField> _getFields(Class<?> entityClass, List<EntityField> fieldList, Integer level) {
            if (fieldList == null) {
                fieldList = new ArrayList();
            }

            if (level == null) {
                level = 0;
            }

            if (entityClass.equals(Object.class)) {
                return (List) fieldList;
            } else {
                Field[] fields = entityClass.getDeclaredFields();
                int index = 0;

                for (int i = 0; i < fields.length; ++i) {
                    Field field = fields[i];
                    if (!Modifier.isStatic(field.getModifiers()) && !Modifier.isTransient(field.getModifiers())) {
                        if (level != 0) {
                            ((List) fieldList).add(index, new EntityField(field, (PropertyDescriptor) null));
                            ++index;
                        } else {
                            ((List) fieldList).add(new EntityField(field, (PropertyDescriptor) null));
                        }
                    }
                }

                Class<?> superClass = entityClass.getSuperclass();
                return (List) (superClass == null || superClass.equals(Object.class) || !superClass.isAnnotationPresent(Entity.class) && (Map.class.isAssignableFrom(superClass) || Collection.class.isAssignableFrom(superClass)) ? fieldList : this._getFields(entityClass.getSuperclass(), (List) fieldList, level + 1));
            }
        }

        public List<EntityField> getProperties(Class<?> entityClass) {
            List<EntityField> entityFields = new ArrayList();
            BeanInfo beanInfo = null;

            try {
                beanInfo = Introspector.getBeanInfo(entityClass);
            } catch (IntrospectionException var9) {
                throw new ValidateException(var9.getMessage());
            }

            PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
            PropertyDescriptor[] var5 = descriptors;
            int var6 = descriptors.length;

            for (int var7 = 0; var7 < var6; ++var7) {
                PropertyDescriptor desc = var5[var7];
                if (!desc.getName().equals("class")) {
                    entityFields.add(new EntityField((Field) null, desc));
                }
            }

            return entityFields;
        }
    }

    interface IFieldHelper {
        List<EntityField> getFields(Class<?> entityClass);

        List<EntityField> getProperties(Class<?> entityClass);
    }
}

