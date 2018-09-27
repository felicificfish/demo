package com.example.demo.configs.mapper.entity;

import com.example.demo.configs.mapper.code.ORDER;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.springframework.util.StringUtils;

public class EntityColumn {
    private EntityTable table;
    private String property;
    private String column;
    private Class<?> javaType;
    private JdbcType jdbcType;
    private Class<? extends TypeHandler<?>> typeHandler;
    private boolean id = false;
    private boolean identity = false;
    private boolean blob;
    private String generator;
    private String orderBy;
    private boolean insertable = true;
    private boolean updatable = true;
    private ORDER order;
    private boolean useJavaType;
    private EntityField entityField;

    public EntityColumn() {
        this.order = ORDER.DEFAULT;
    }

    public EntityColumn(EntityTable table) {
        this.order = ORDER.DEFAULT;
        this.table = table;
    }

    public String getColumnEqualsHolder(String entityName) {
        return this.column + " = " + this.getColumnHolder(entityName);
    }

    public String getColumnHolder(String entityName) {
        return this.getColumnHolder(entityName, (String) null);
    }

    public String getColumnHolder(String entityName, String suffix) {
        return this.getColumnHolder(entityName, (String) null, (String) null);
    }

    public String getColumnHolderWithComma(String entityName, String suffix) {
        return this.getColumnHolder(entityName, suffix, ",");
    }

    public String getColumnHolder(String entityName, String suffix, String separator) {
        StringBuffer sb = new StringBuffer("#{");
        if (!StringUtils.isEmpty(entityName)) {
            sb.append(entityName);
            sb.append(".");
        }

        sb.append(this.property);
        if (!StringUtils.isEmpty(suffix)) {
            sb.append(suffix);
        }

        if (this.jdbcType != null) {
            sb.append(", jdbcType=");
            sb.append(this.jdbcType.toString());
        }

        if (this.typeHandler != null) {
            sb.append(", typeHandler=");
            sb.append(this.typeHandler.getCanonicalName());
        }

        if (this.useJavaType && !this.javaType.isArray()) {
            sb.append(", javaType=");
            sb.append(this.javaType.getCanonicalName());
        }

        sb.append("}");
        if (!StringUtils.isEmpty(separator)) {
            sb.append(separator);
        }

        return sb.toString();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            EntityColumn that = (EntityColumn) o;
            if (this.id != that.id) {
                return false;
            } else if (this.identity != that.identity) {
                return false;
            } else {
                label104:
                {
                    if (this.table != null) {
                        if (this.table.equals(that.table)) {
                            break label104;
                        }
                    } else if (that.table == null) {
                        break label104;
                    }

                    return false;
                }

                if (this.property != null) {
                    if (!this.property.equals(that.property)) {
                        return false;
                    }
                } else if (that.property != null) {
                    return false;
                }

                label90:
                {
                    if (this.column != null) {
                        if (this.column.equals(that.column)) {
                            break label90;
                        }
                    } else if (that.column == null) {
                        break label90;
                    }

                    return false;
                }

                label83:
                {
                    if (this.javaType != null) {
                        if (this.javaType.equals(that.javaType)) {
                            break label83;
                        }
                    } else if (that.javaType == null) {
                        break label83;
                    }

                    return false;
                }

                if (this.jdbcType != that.jdbcType) {
                    return false;
                } else {
                    if (this.typeHandler != null) {
                        if (!this.typeHandler.equals(that.typeHandler)) {
                            return false;
                        }
                    } else if (that.typeHandler != null) {
                        return false;
                    }

                    if (this.generator != null) {
                        if (!this.generator.equals(that.generator)) {
                            return false;
                        }
                    } else if (that.generator != null) {
                        return false;
                    }

                    boolean var10000;
                    label137:
                    {
                        if (this.orderBy != null) {
                            if (this.orderBy.equals(that.orderBy)) {
                                break label137;
                            }
                        } else if (that.orderBy == null) {
                            break label137;
                        }

                        var10000 = false;
                        return var10000;
                    }

                    var10000 = true;
                    return var10000;
                }
            }
        } else {
            return false;
        }
    }

    public int hashCode() {
        int result = this.table != null ? this.table.hashCode() : 0;
        result = 31 * result + (this.property != null ? this.property.hashCode() : 0);
        result = 31 * result + (this.column != null ? this.column.hashCode() : 0);
        result = 31 * result + (this.javaType != null ? this.javaType.hashCode() : 0);
        result = 31 * result + (this.jdbcType != null ? this.jdbcType.hashCode() : 0);
        result = 31 * result + (this.typeHandler != null ? this.typeHandler.hashCode() : 0);
        result = 31 * result + (this.id ? 1 : 0);
        result = 31 * result + (this.identity ? 1 : 0);
        result = 31 * result + (this.generator != null ? this.generator.hashCode() : 0);
        result = 31 * result + (this.orderBy != null ? this.orderBy.hashCode() : 0);
        return result;
    }

    public String getColumn() {
        return this.column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getColumnEqualsHolder() {
        return this.getColumnEqualsHolder((String) null);
    }

    public String getColumnHolder() {
        return this.getColumnHolder((String) null);
    }

    public EntityField getEntityField() {
        return this.entityField;
    }

    public void setEntityField(EntityField entityField) {
        this.entityField = entityField;
    }

    public String getGenerator() {
        return this.generator;
    }

    public void setGenerator(String generator) {
        this.generator = generator;
    }

    public Class<?> getJavaType() {
        return this.javaType;
    }

    public void setJavaType(Class<?> javaType) {
        this.javaType = javaType;
    }

    public JdbcType getJdbcType() {
        return this.jdbcType;
    }

    public void setJdbcType(JdbcType jdbcType) {
        this.jdbcType = jdbcType;
    }

    public String getOrderBy() {
        return this.orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getProperty() {
        return this.property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public EntityTable getTable() {
        return this.table;
    }

    public void setTable(EntityTable table) {
        this.table = table;
    }

    public Class<? extends TypeHandler<?>> getTypeHandler() {
        return this.typeHandler;
    }

    public void setTypeHandler(Class<? extends TypeHandler<?>> typeHandler) {
        this.typeHandler = typeHandler;
    }

    public boolean isId() {
        return this.id;
    }

    public void setId(boolean id) {
        this.id = id;
    }

    public boolean isIdentity() {
        return this.identity;
    }

    public void setIdentity(boolean identity) {
        this.identity = identity;
    }

    public boolean isInsertable() {
        return this.insertable;
    }

    public void setInsertable(boolean insertable) {
        this.insertable = insertable;
    }

    public boolean isUpdatable() {
        return this.updatable;
    }

    public void setUpdatable(boolean updatable) {
        this.updatable = updatable;
    }

    public ORDER getOrder() {
        return this.order;
    }

    public void setOrder(ORDER order) {
        this.order = order;
    }

    public boolean isBlob() {
        return this.blob;
    }

    public void setBlob(boolean blob) {
        this.blob = blob;
    }

    public boolean isUseJavaType() {
        return this.useJavaType;
    }

    public void setUseJavaType(boolean useJavaType) {
        this.useJavaType = useJavaType;
    }

    public String toString() {
        return "EntityColumn{table=" + this.table.getName() + ", property='" + this.property + '\'' + ", column='" + this.column + '\'' + ", javaType=" + this.javaType + ", jdbcType=" + this.jdbcType + ", typeHandler=" + this.typeHandler + ", id=" + this.id + ", identity=" + this.identity + ", blob=" + this.blob + ", generator='" + this.generator + '\'' + ", orderBy='" + this.orderBy + '\'' + ", insertable=" + this.insertable + ", updatable=" + this.updatable + ", order=" + this.order + '}';
    }
}
