package com.example.demo.configs.mapper.helper;

import com.example.demo.configs.exception.ValidateException;
import com.example.demo.configs.mapper.annotation.RegisterMapper;
import com.example.demo.configs.mapper.entity.Config;
import com.example.demo.configs.mapper.provider.EmptyProvider;
import com.example.demo.configs.mapper.utils.MsUtils;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.builder.annotation.ProviderSqlSource;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MapperHelper {
    private List<Class<?>> registerClass;
    private Map<Class<?>, MapperTemplate> registerMapper;
    private Config config;

    public MapperHelper() {
        this.registerClass = new ArrayList();
        this.registerMapper = new ConcurrentHashMap();
        this.config = new Config();
    }

    public MapperHelper(Properties properties) {
        this();
        this.setProperties(properties);
    }

    private MapperTemplate fromMapperClass(Class<?> mapperClass) {
        Method[] methods = mapperClass.getDeclaredMethods();
        Class<?> templateClass = null;
        Class<?> tempClass = null;
        Set<String> methodSet = new HashSet();
        Method[] var6 = methods;
        int var7 = methods.length;

        for (int var8 = 0; var8 < var7; ++var8) {
            Method method = var6[var8];
            if (method.isAnnotationPresent(SelectProvider.class)) {
                SelectProvider provider = (SelectProvider) method.getAnnotation(SelectProvider.class);
                tempClass = provider.type();
                methodSet.add(method.getName());
            } else if (method.isAnnotationPresent(InsertProvider.class)) {
                InsertProvider provider = (InsertProvider) method.getAnnotation(InsertProvider.class);
                tempClass = provider.type();
                methodSet.add(method.getName());
            } else if (method.isAnnotationPresent(DeleteProvider.class)) {
                DeleteProvider provider = (DeleteProvider) method.getAnnotation(DeleteProvider.class);
                tempClass = provider.type();
                methodSet.add(method.getName());
            } else if (method.isAnnotationPresent(UpdateProvider.class)) {
                UpdateProvider provider = (UpdateProvider) method.getAnnotation(UpdateProvider.class);
                tempClass = provider.type();
                methodSet.add(method.getName());
            }

            if (templateClass == null) {
                templateClass = tempClass;
            } else if (templateClass != tempClass) {
                throw new ValidateException("一个通用Mapper中只允许存在一个MapperTemplate子类!");
            }
        }

        if (templateClass == null || !MapperTemplate.class.isAssignableFrom(templateClass)) {
            templateClass = EmptyProvider.class;
        }

        var6 = null;

        MapperTemplate mapperTemplate;
        try {
            mapperTemplate = (MapperTemplate) templateClass.getConstructor(Class.class, MapperHelper.class).newInstance(mapperClass, this);
        } catch (Exception var12) {
            throw new ValidateException("实例化MapperTemplate对象失败:" + var12.getMessage());
        }

        Iterator var14 = methodSet.iterator();

        while (var14.hasNext()) {
            String methodName = (String) var14.next();

            try {
                mapperTemplate.addMethodMap(methodName, templateClass.getMethod(methodName, MappedStatement.class));
            } catch (NoSuchMethodException var11) {
                throw new ValidateException(templateClass.getCanonicalName() + "中缺少" + methodName + "方法!");
            }
        }

        return mapperTemplate;
    }

    public void registerMapper(Class<?> mapperClass) {
        if (!this.registerMapper.containsKey(mapperClass)) {
            this.registerClass.add(mapperClass);
            this.registerMapper.put(mapperClass, this.fromMapperClass(mapperClass));
        }

        Class<?>[] interfaces = mapperClass.getInterfaces();
        if (interfaces != null && interfaces.length > 0) {
            Class[] var3 = interfaces;
            int var4 = interfaces.length;

            for (int var5 = 0; var5 < var4; ++var5) {
                Class<?> anInterface = var3[var5];
                this.registerMapper(anInterface);
            }
        }

    }

    public void registerMapper(String mapperClass) {
        try {
            this.registerMapper(Class.forName(mapperClass));
        } catch (ClassNotFoundException var3) {
            throw new ValidateException("注册通用Mapper[" + mapperClass + "]失败，找不到该通用Mapper!");
        }
    }

    public MapperTemplate isMapperMethod(String msId) {
        MapperTemplate mapperTemplate = this.getMapperTemplateByMsId(msId);
        if (mapperTemplate == null) {
            try {
                Class<?> mapperClass = MsUtils.getMapperClass(msId);
                if (mapperClass.isInterface() && this.hasRegisterMapper(mapperClass)) {
                    mapperTemplate = this.getMapperTemplateByMsId(msId);
                }
            } catch (Exception var4) {
                ;
            }
        }

        return mapperTemplate;
    }

    public MapperTemplate getMapperTemplateByMsId(String msId) {
        Iterator var2 = this.registerMapper.entrySet().iterator();

        Map.Entry entry;
        do {
            if (!var2.hasNext()) {
                return null;
            }

            entry = (Map.Entry) var2.next();
        } while (!((MapperTemplate) entry.getValue()).supportMethod(msId));

        return (MapperTemplate) entry.getValue();
    }

    public boolean isExtendCommonMapper(Class<?> mapperInterface) {
        Iterator var2 = this.registerClass.iterator();

        Class mapperClass;
        do {
            if (!var2.hasNext()) {
                return this.hasRegisterMapper(mapperInterface);
            }

            mapperClass = (Class) var2.next();
        } while (!mapperClass.isAssignableFrom(mapperInterface));

        return true;
    }

    private boolean hasRegisterMapper(Class<?> mapperInterface) {
        Class<?>[] interfaces = mapperInterface.getInterfaces();
        boolean hasRegisterMapper = false;
        if (interfaces != null && interfaces.length > 0) {
            Class[] var4 = interfaces;
            int var5 = interfaces.length;

            for (int var6 = 0; var6 < var5; ++var6) {
                Class<?> anInterface = var4[var6];
                if (anInterface.isAnnotationPresent(RegisterMapper.class)) {
                    hasRegisterMapper = true;
                    if (!this.registerMapper.containsKey(anInterface)) {
                        this.registerMapper(anInterface);
                    }
                } else if (this.hasRegisterMapper(anInterface)) {
                    hasRegisterMapper = true;
                }
            }
        }

        return hasRegisterMapper;
    }

    public void processConfiguration(Configuration configuration) {
        this.processConfiguration(configuration, (Class) null);
    }

    public void processConfiguration(Configuration configuration, Class<?> mapperInterface) {
        String prefix;
        if (mapperInterface != null) {
            prefix = mapperInterface.getCanonicalName();
        } else {
            prefix = "";
        }

        Iterator var4 = (new ArrayList(configuration.getMappedStatements())).iterator();

        while (var4.hasNext()) {
            Object object = var4.next();
            if (object instanceof MappedStatement) {
                MappedStatement ms = (MappedStatement) object;
                if (ms.getId().startsWith(prefix)) {
                    this.processMappedStatement(ms);
                }
            }
        }

    }

    public void processMappedStatement(MappedStatement ms) {
        MapperTemplate mapperTemplate = this.isMapperMethod(ms.getId());
        if (mapperTemplate != null && ms.getSqlSource() instanceof ProviderSqlSource) {
            this.setSqlSource(ms, mapperTemplate);
        }

    }

    public Config getConfig() {
        return this.config;
    }

    public void setConfig(Config config) {
        this.config = config;
        if (config.getResolveClass() != null) {
            try {
                EntityHelper.setResolve((EntityResolve) config.getResolveClass().newInstance());
            } catch (Exception var4) {
                throw new ValidateException("创建 " + config.getResolveClass().getCanonicalName() + " 实例失败，请保证该类有默认的构造方法!");
            }
        }

        if (config.getMappers() != null && config.getMappers().size() > 0) {
            Iterator var2 = config.getMappers().iterator();

            while (var2.hasNext()) {
                Class mapperClass = (Class) var2.next();
                this.registerMapper(mapperClass);
            }
        }

    }

    public void setProperties(Properties properties) {
        this.config.setProperties(properties);
        String mapper;
        if (properties != null) {
            mapper = properties.getProperty("resolveClass");
            if (!StringUtils.isEmpty(mapper)) {
                try {
                    EntityHelper.setResolve((EntityResolve) Class.forName(mapper).newInstance());
                } catch (Exception var8) {
                    throw new ValidateException("创建 " + mapper + " 实例失败!");
                }
            }
        }

        if (properties != null) {
            mapper = properties.getProperty("mappers");
            if (!StringUtils.isEmpty(mapper)) {
                String[] mappers = mapper.split(",");
                String[] var4 = mappers;
                int var5 = mappers.length;

                for (int var6 = 0; var6 < var5; ++var6) {
                    String mapperClass = var4[var6];
                    if (mapperClass.length() > 0) {
                        this.registerMapper(mapperClass);
                    }
                }
            }
        }

    }

    public void setSqlSource(MappedStatement ms, MapperTemplate mapperTemplate) {
        try {
            if (mapperTemplate != null) {
                mapperTemplate.setSqlSource(ms);
            }

        } catch (Exception var4) {
            throw new ValidateException(var4.getMessage());
        }
    }
}
