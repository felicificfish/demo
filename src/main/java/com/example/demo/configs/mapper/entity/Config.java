package com.example.demo.configs.mapper.entity;

import com.example.demo.configs.exception.ValidateException;
import com.example.demo.configs.mapper.code.IdentityDialect;
import com.example.demo.configs.mapper.code.Style;
import com.example.demo.configs.mapper.helper.EntityResolve;
import com.example.demo.configs.mapper.utils.SimpleTypeUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Config {
    public static final String PREFIX = "mapper";
    private List<Class> mappers = new ArrayList();
    private String IDENTITY;
    private boolean BEFORE;
    private String seqFormat;
    private String catalog;
    private String schema;
    private boolean checkExampleEntityClass;
    private boolean useSimpleType = true;
    private boolean enumAsSimpleType;
    private boolean enableMethodAnnotation;
    private boolean notEmpty;
    private Style style;
    private String wrapKeyword = "";
    private Class<? extends EntityResolve> resolveClass;
    private boolean safeDelete;
    private boolean safeUpdate;
    private boolean useJavaType;

    public Config() {
    }

    public String getCatalog() {
        return this.catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getIDENTITY() {
        return !StringUtils.isEmpty(this.IDENTITY) ? this.IDENTITY : IdentityDialect.MYSQL.getIdentityRetrievalStatement();
    }

    public void setIDENTITY(String IDENTITY) {
        IdentityDialect identityDialect = IdentityDialect.getDatabaseDialect(IDENTITY);
        if (identityDialect != null) {
            this.IDENTITY = identityDialect.getIdentityRetrievalStatement();
        } else {
            this.IDENTITY = IDENTITY;
        }

    }

    public String getPrefix() {
        if (!StringUtils.isEmpty(this.catalog)) {
            return this.catalog;
        } else {
            return !StringUtils.isEmpty(this.schema) ? this.schema : "";
        }
    }

    public String getSchema() {
        return this.schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getSeqFormat() {
        return !StringUtils.isEmpty(this.seqFormat) ? this.seqFormat : "{0}.nextval";
    }

    public void setSeqFormat(String seqFormat) {
        this.seqFormat = seqFormat;
    }

    public Style getStyle() {
        return this.style == null ? Style.CAMELHUMP_SKIP_DO : this.style;
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    public String getWrapKeyword() {
        return this.wrapKeyword;
    }

    public void setWrapKeyword(String wrapKeyword) {
        this.wrapKeyword = wrapKeyword;
    }

    public boolean isBEFORE() {
        return this.BEFORE;
    }

    public void setBEFORE(boolean BEFORE) {
        this.BEFORE = BEFORE;
    }

    public boolean isCheckExampleEntityClass() {
        return this.checkExampleEntityClass;
    }

    public void setCheckExampleEntityClass(boolean checkExampleEntityClass) {
        this.checkExampleEntityClass = checkExampleEntityClass;
    }

    public boolean isEnableMethodAnnotation() {
        return this.enableMethodAnnotation;
    }

    public void setEnableMethodAnnotation(boolean enableMethodAnnotation) {
        this.enableMethodAnnotation = enableMethodAnnotation;
    }

    public boolean isEnumAsSimpleType() {
        return this.enumAsSimpleType;
    }

    public void setEnumAsSimpleType(boolean enumAsSimpleType) {
        this.enumAsSimpleType = enumAsSimpleType;
    }

    public boolean isNotEmpty() {
        return this.notEmpty;
    }

    public void setNotEmpty(boolean notEmpty) {
        this.notEmpty = notEmpty;
    }

    public boolean isUseSimpleType() {
        return this.useSimpleType;
    }

    public void setUseSimpleType(boolean useSimpleType) {
        this.useSimpleType = useSimpleType;
    }

    public void setOrder(String order) {
        this.BEFORE = "BEFORE".equalsIgnoreCase(order);
    }

    public String getIdentity() {
        return this.getIDENTITY();
    }

    public void setIdentity(String identity) {
        this.setIDENTITY(identity);
    }

    public List<Class> getMappers() {
        return this.mappers;
    }

    public void setMappers(List<Class> mappers) {
        this.mappers = mappers;
    }

    public boolean isBefore() {
        return this.isBEFORE();
    }

    public void setBefore(boolean before) {
        this.setBEFORE(before);
    }

    public Class<? extends EntityResolve> getResolveClass() {
        return this.resolveClass;
    }

    public void setResolveClass(Class<? extends EntityResolve> resolveClass) {
        this.resolveClass = resolveClass;
    }

    public boolean isSafeDelete() {
        return this.safeDelete;
    }

    public void setSafeDelete(boolean safeDelete) {
        this.safeDelete = safeDelete;
    }

    public boolean isSafeUpdate() {
        return this.safeUpdate;
    }

    public void setSafeUpdate(boolean safeUpdate) {
        this.safeUpdate = safeUpdate;
    }

    public boolean isUseJavaType() {
        return this.useJavaType;
    }

    public void setUseJavaType(boolean useJavaType) {
        this.useJavaType = useJavaType;
    }

    public void setProperties(Properties properties) {
        if (properties == null) {
            this.style = Style.CAMELHUMP_SKIP_DO;
        } else {
            String IDENTITY = properties.getProperty("IDENTITY");
            if (!StringUtils.isEmpty(IDENTITY)) {
                this.setIDENTITY(IDENTITY);
            }

            String seqFormat = properties.getProperty("seqFormat");
            if (!StringUtils.isEmpty(seqFormat)) {
                this.setSeqFormat(seqFormat);
            }

            String catalog = properties.getProperty("catalog");
            if (!StringUtils.isEmpty(catalog)) {
                this.setCatalog(catalog);
            }

            String schema = properties.getProperty("schema");
            if (!StringUtils.isEmpty(schema)) {
                this.setSchema(schema);
            }

            String ORDER = properties.getProperty("ORDER");
            if (!StringUtils.isEmpty(ORDER)) {
                this.setOrder(ORDER);
            }

            ORDER = properties.getProperty("order");
            if (!StringUtils.isEmpty(ORDER)) {
                this.setOrder(ORDER);
            }

            ORDER = properties.getProperty("before");
            if (!StringUtils.isEmpty(ORDER)) {
                this.setBefore(Boolean.valueOf(ORDER));
            }

            this.notEmpty = Boolean.valueOf(properties.getProperty("notEmpty"));
            this.enableMethodAnnotation = Boolean.valueOf(properties.getProperty("enableMethodAnnotation"));
            this.checkExampleEntityClass = Boolean.valueOf(properties.getProperty("checkExampleEntityClass"));
            String useSimpleTypeStr = properties.getProperty("useSimpleType");
            if (!StringUtils.isEmpty(useSimpleTypeStr)) {
                this.useSimpleType = Boolean.valueOf(useSimpleTypeStr);
            }

            this.enumAsSimpleType = Boolean.valueOf(properties.getProperty("enumAsSimpleType"));
            String simpleTypes = properties.getProperty("simpleTypes");
            if (!StringUtils.isEmpty(simpleTypes)) {
                SimpleTypeUtils.registerSimpleType(simpleTypes);
            }

            if (Boolean.valueOf(properties.getProperty("usePrimitiveType"))) {
                SimpleTypeUtils.registerPrimitiveTypes();
            }

            String styleStr = properties.getProperty("style");
            if (!StringUtils.isEmpty(styleStr)) {
                try {
                    this.style = Style.valueOf(styleStr);
                } catch (IllegalArgumentException var11) {
                    throw new ValidateException(styleStr + "不是合法的Style值!");
                }
            } else {
                this.style = Style.CAMELHUMP_SKIP_DO;
            }

            String wrapKeyword = properties.getProperty("wrapKeyword");
            if (!StringUtils.isEmpty(wrapKeyword)) {
                this.wrapKeyword = wrapKeyword;
            }

            this.safeDelete = Boolean.valueOf(properties.getProperty("safeDelete"));
            this.safeUpdate = Boolean.valueOf(properties.getProperty("safeUpdate"));
            this.useJavaType = Boolean.valueOf(properties.getProperty("useJavaType"));
        }
    }
}
