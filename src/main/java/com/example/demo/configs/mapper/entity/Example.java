package com.example.demo.configs.mapper.entity;

import com.example.demo.configs.exception.ValidateException;
import com.example.demo.configs.mapper.helper.EntityHelper;
import com.example.demo.configs.mapper.helper.Sqls;
import com.github.pagehelper.util.MetaObjectUtil;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.util.StringUtils;

import java.util.*;

public class Example implements IDynamicTableName {
    protected String orderByClause;
    protected boolean distinct;
    protected boolean exists;
    protected boolean notNull;
    protected boolean forUpdate;
    protected Set<String> selectColumns;
    protected Set<String> excludeColumns;
    protected String countColumn;
    protected List<Criteria> oredCriteria;
    protected Class<?> entityClass;
    protected EntityTable table;
    protected Map<String, EntityColumn> propertyMap;
    protected String tableName;
    protected Example.OrderBy ORDERBY;

    public Example(Class<?> entityClass) {
        this(entityClass, true);
    }

    public Example(Class<?> entityClass, boolean exists) {
        this(entityClass, exists, false);
    }

    public Example(Class<?> entityClass, boolean exists, boolean notNull) {
        this.exists = exists;
        this.notNull = notNull;
        this.oredCriteria = new ArrayList();
        this.entityClass = entityClass;
        this.table = EntityHelper.getEntityTable(entityClass);
        this.propertyMap = this.table.getPropertyMap();
        this.ORDERBY = new Example.OrderBy(this, this.propertyMap);
    }

    private Example(Example.Builder builder) {
        this.exists = builder.exists;
        this.notNull = builder.notNull;
        this.distinct = builder.distinct;
        this.entityClass = builder.entityClass;
        this.propertyMap = builder.propertyMap;
        this.selectColumns = builder.selectColumns;
        this.excludeColumns = builder.excludeColumns;
        this.oredCriteria = builder.exampleCriterias;
        this.forUpdate = builder.forUpdate;
        this.tableName = builder.tableName;
        if (!StringUtils.isEmpty(builder.orderByClause.toString())) {
            this.orderByClause = builder.orderByClause.toString();
        }

    }

    public static Example.Builder builder(Class<?> entityClass) {
        return new Example.Builder(entityClass);
    }

    public Example.OrderBy orderBy(String property) {
        this.ORDERBY.orderBy(property);
        return this.ORDERBY;
    }

    public Example excludeProperties(String... properties) {
        if (properties != null && properties.length > 0) {
            if (this.excludeColumns == null) {
                this.excludeColumns = new LinkedHashSet();
            }

            String[] var2 = properties;
            int var3 = properties.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                String property = var2[var4];
                if (!this.propertyMap.containsKey(property)) {
                    throw new ValidateException("类 " + this.entityClass.getSimpleName() + " 不包含属性 '" + property + "'，或该属性被@Transient注释！");
                }

                this.excludeColumns.add(((EntityColumn)this.propertyMap.get(property)).getColumn());
            }
        }

        return this;
    }

    public Example selectProperties(String... properties) {
        if (properties != null && properties.length > 0) {
            if (this.selectColumns == null) {
                this.selectColumns = new LinkedHashSet();
            }

            String[] var2 = properties;
            int var3 = properties.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                String property = var2[var4];
                if (!this.propertyMap.containsKey(property)) {
                    throw new ValidateException("类 " + this.entityClass.getSimpleName() + " 不包含属性 '" + property + "'，或该属性被@Transient注释！");
                }

                this.selectColumns.add(((EntityColumn)this.propertyMap.get(property)).getColumn());
            }
        }

        return this;
    }

    public void or(Example.Criteria criteria) {
        criteria.setAndOr("or");
        this.oredCriteria.add(criteria);
    }

    public Example.Criteria or() {
        Example.Criteria criteria = this.createCriteriaInternal();
        criteria.setAndOr("or");
        this.oredCriteria.add(criteria);
        return criteria;
    }

    public void and(Example.Criteria criteria) {
        criteria.setAndOr("and");
        this.oredCriteria.add(criteria);
    }

    public Example.Criteria and() {
        Example.Criteria criteria = this.createCriteriaInternal();
        criteria.setAndOr("and");
        this.oredCriteria.add(criteria);
        return criteria;
    }

    public Example.Criteria createCriteria() {
        Example.Criteria criteria = this.createCriteriaInternal();
        if (this.oredCriteria.size() == 0) {
            criteria.setAndOr("and");
            this.oredCriteria.add(criteria);
        }

        return criteria;
    }

    protected Example.Criteria createCriteriaInternal() {
        Example.Criteria criteria = new Example.Criteria(this.propertyMap, this.exists, this.notNull);
        return criteria;
    }

    public void clear() {
        this.oredCriteria.clear();
        this.orderByClause = null;
        this.distinct = false;
    }

    public String getCountColumn() {
        return this.countColumn;
    }

    public String getDynamicTableName() {
        return this.tableName;
    }

    public Class<?> getEntityClass() {
        return this.entityClass;
    }

    public String getOrderByClause() {
        return this.orderByClause;
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public List<Example.Criteria> getOredCriteria() {
        return this.oredCriteria;
    }

    public Set<String> getSelectColumns() {
        if ((this.selectColumns == null || this.selectColumns.size() <= 0) && this.excludeColumns != null && this.excludeColumns.size() > 0) {
            Collection<EntityColumn> entityColumns = this.propertyMap.values();
            this.selectColumns = new LinkedHashSet(entityColumns.size() - this.excludeColumns.size());
            Iterator var2 = entityColumns.iterator();

            while(var2.hasNext()) {
                EntityColumn column = (EntityColumn)var2.next();
                if (!this.excludeColumns.contains(column.getColumn())) {
                    this.selectColumns.add(column.getColumn());
                }
            }
        }

        return this.selectColumns;
    }

    public boolean isDistinct() {
        return this.distinct;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isForUpdate() {
        return this.forUpdate;
    }

    public void setForUpdate(boolean forUpdate) {
        this.forUpdate = forUpdate;
    }

    public void setCountProperty(String property) {
        if (this.propertyMap.containsKey(property)) {
            this.countColumn = ((EntityColumn)this.propertyMap.get(property)).getColumn();
        }

    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public static class Builder {
        private final Class<?> entityClass;
        protected EntityTable table;
        protected Map<String, EntityColumn> propertyMap;
        private StringBuilder orderByClause;
        private boolean distinct;
        private boolean exists;
        private boolean notNull;
        private boolean forUpdate;
        private Set<String> selectColumns;
        private Set<String> excludeColumns;
        private List<Sqls.Criteria> sqlsCriteria;
        private List<Example.Criteria> exampleCriterias;
        private String tableName;

        public Builder(Class<?> entityClass) {
            this(entityClass, true);
        }

        public Builder(Class<?> entityClass, boolean exists) {
            this(entityClass, exists, false);
        }

        public Builder(Class<?> entityClass, boolean exists, boolean notNull) {
            this.entityClass = entityClass;
            this.exists = exists;
            this.notNull = notNull;
            this.orderByClause = new StringBuilder();
            this.table = EntityHelper.getEntityTable(entityClass);
            this.propertyMap = this.table.getPropertyMap();
            this.sqlsCriteria = new ArrayList(2);
        }

        public Example.Builder distinct() {
            return this.setDistinct(true);
        }

        public Example.Builder forUpdate() {
            return this.setForUpdate(true);
        }

        public Example.Builder selectDistinct(String... properties) {
            this.select(properties);
            this.distinct = true;
            return this;
        }

        public Example.Builder select(String... properties) {
            if (properties != null && properties.length > 0) {
                if (this.selectColumns == null) {
                    this.selectColumns = new LinkedHashSet();
                }

                String[] var2 = properties;
                int var3 = properties.length;

                for(int var4 = 0; var4 < var3; ++var4) {
                    String property = var2[var4];
                    if (!this.propertyMap.containsKey(property)) {
                        throw new ValidateException("当前实体类不包含名为" + property + "的属性!");
                    }

                    this.selectColumns.add(((EntityColumn)this.propertyMap.get(property)).getColumn());
                }
            }

            return this;
        }

        public Example.Builder notSelect(String... properties) {
            if (properties != null && properties.length > 0) {
                if (this.excludeColumns == null) {
                    this.excludeColumns = new LinkedHashSet();
                }

                String[] var2 = properties;
                int var3 = properties.length;

                for(int var4 = 0; var4 < var3; ++var4) {
                    String property = var2[var4];
                    if (!this.propertyMap.containsKey(property)) {
                        throw new ValidateException("当前实体类不包含名为" + property + "的属性!");
                    }

                    this.excludeColumns.add(((EntityColumn)this.propertyMap.get(property)).getColumn());
                }
            }

            return this;
        }

        public Example.Builder from(String tableName) {
            return this.setTableName(tableName);
        }

        public Example.Builder where(Sqls sqls) {
            Sqls.Criteria criteria = sqls.getCriteria();
            criteria.setAndOr("and");
            this.sqlsCriteria.add(criteria);
            return this;
        }

        public Example.Builder where(SqlsCriteria sqls) {
            Sqls.Criteria criteria = sqls.getCriteria();
            criteria.setAndOr("and");
            this.sqlsCriteria.add(criteria);
            return this;
        }

        public Example.Builder andWhere(Sqls sqls) {
            Sqls.Criteria criteria = sqls.getCriteria();
            criteria.setAndOr("and");
            this.sqlsCriteria.add(criteria);
            return this;
        }

        public Example.Builder andWhere(SqlsCriteria sqls) {
            Sqls.Criteria criteria = sqls.getCriteria();
            criteria.setAndOr("and");
            this.sqlsCriteria.add(criteria);
            return this;
        }

        public Example.Builder orWhere(Sqls sqls) {
            Sqls.Criteria criteria = sqls.getCriteria();
            criteria.setAndOr("or");
            this.sqlsCriteria.add(criteria);
            return this;
        }

        public Example.Builder orWhere(SqlsCriteria sqls) {
            Sqls.Criteria criteria = sqls.getCriteria();
            criteria.setAndOr("or");
            this.sqlsCriteria.add(criteria);
            return this;
        }

        public Example.Builder orderBy(String... properties) {
            return this.orderByAsc(properties);
        }

        public Example.Builder orderByAsc(String... properties) {
            this.contactOrderByClause(" Asc", properties);
            return this;
        }

        public Example.Builder orderByDesc(String... properties) {
            this.contactOrderByClause(" Desc", properties);
            return this;
        }

        private void contactOrderByClause(String order, String... properties) {
            StringBuilder columns = new StringBuilder();
            String[] var4 = properties;
            int var5 = properties.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                String property = var4[var6];
                String column;
                if ((column = this.propertyforOderBy(property)) != null) {
                    columns.append(",").append(column);
                }
            }

            columns.append(order);
            if (columns.length() > 0) {
                this.orderByClause.append(columns);
            }

        }

        public Example build() {
            this.exampleCriterias = new ArrayList();
            Iterator var1 = this.sqlsCriteria.iterator();

            while(var1.hasNext()) {
                Sqls.Criteria criteria = (Sqls.Criteria)var1.next();
                Example.Criteria exampleCriteria = new Example.Criteria(this.propertyMap, this.exists, this.notNull);
                exampleCriteria.setAndOr(criteria.getAndOr());
                Iterator var4 = criteria.getCriterions().iterator();

                while(var4.hasNext()) {
                    Sqls.Criterion criterion = (Sqls.Criterion)var4.next();
                    String condition = criterion.getCondition();
                    String andOr = criterion.getAndOr();
                    String property = criterion.getProperty();
                    Object[] values = criterion.getValues();
                    this.transformCriterion(exampleCriteria, condition, property, values, andOr);
                }

                this.exampleCriterias.add(exampleCriteria);
            }

            if (this.orderByClause.length() > 0) {
                this.orderByClause = new StringBuilder(this.orderByClause.substring(1, this.orderByClause.length()));
            }

            return new Example(this);
        }

        private void transformCriterion(Example.Criteria exampleCriteria, String condition, String property, Object[] values, String andOr) {
            if (values.length == 0) {
                if ("and".equals(andOr)) {
                    exampleCriteria.addCriterion(this.column(property) + " " + condition);
                } else {
                    exampleCriteria.addOrCriterion(this.column(property) + " " + condition);
                }
            } else if (values.length == 1) {
                if ("and".equals(andOr)) {
                    exampleCriteria.addCriterion(this.column(property) + " " + condition, values[0], this.property(property));
                } else {
                    exampleCriteria.addOrCriterion(this.column(property) + " " + condition, values[0], this.property(property));
                }
            } else if (values.length == 2) {
                if ("and".equals(andOr)) {
                    exampleCriteria.addCriterion(this.column(property) + " " + condition, values[0], values[1], this.property(property));
                } else {
                    exampleCriteria.addOrCriterion(this.column(property) + " " + condition, values[0], values[1], this.property(property));
                }
            }

        }

        private String column(String property) {
            if (this.propertyMap.containsKey(property)) {
                return ((EntityColumn)this.propertyMap.get(property)).getColumn();
            } else if (this.exists) {
                throw new ValidateException("当前实体类不包含名为" + property + "的属性!");
            } else {
                return null;
            }
        }

        private String property(String property) {
            if (this.propertyMap.containsKey(property)) {
                return property;
            } else if (this.exists) {
                throw new ValidateException("当前实体类不包含名为" + property + "的属性!");
            } else {
                return null;
            }
        }

        private String propertyforOderBy(String property) {
            if (!StringUtils.isEmpty(property) && !StringUtils.isEmpty(property.trim())) {
                property = property.trim();
                if (!this.propertyMap.containsKey(property)) {
                    throw new ValidateException("当前实体类不包含名为" + property + "的属性!");
                } else {
                    return ((EntityColumn)this.propertyMap.get(property)).getColumn();
                }
            } else {
                throw new ValidateException("接收的property为空！");
            }
        }

        public Example.Builder setDistinct(boolean distinct) {
            this.distinct = distinct;
            return this;
        }

        public Example.Builder setForUpdate(boolean forUpdate) {
            this.forUpdate = forUpdate;
            return this;
        }

        public Example.Builder setTableName(String tableName) {
            this.tableName = tableName;
            return this;
        }
    }

    public static class Criterion {
        private String condition;
        private Object value;
        private Object secondValue;
        private String andOr;
        private boolean noValue;
        private boolean singleValue;
        private boolean betweenValue;
        private boolean listValue;
        private String typeHandler;

        protected Criterion(String condition) {
            this(condition, false);
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            this(condition, value, typeHandler, false);
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, (String)null, false);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            this(condition, value, secondValue, typeHandler, false);
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, (String)null, false);
        }

        protected Criterion(String condition, boolean isOr) {
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
            this.andOr = isOr ? "or" : "and";
        }

        protected Criterion(String condition, Object value, String typeHandler, boolean isOr) {
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            this.andOr = isOr ? "or" : "and";
            if (value instanceof Collection) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }

        }

        protected Criterion(String condition, Object value, boolean isOr) {
            this(condition, value, (String)null, isOr);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler, boolean isOr) {
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
            this.andOr = isOr ? "or" : "and";
        }

        protected Criterion(String condition, Object value, Object secondValue, boolean isOr) {
            this(condition, value, secondValue, (String)null, isOr);
        }

        public String getAndOr() {
            return this.andOr;
        }

        public void setAndOr(String andOr) {
            this.andOr = andOr;
        }

        public String getCondition() {
            return this.condition;
        }

        public Object getSecondValue() {
            return this.secondValue;
        }

        public String getTypeHandler() {
            return this.typeHandler;
        }

        public Object getValue() {
            return this.value;
        }

        public boolean isBetweenValue() {
            return this.betweenValue;
        }

        public boolean isListValue() {
            return this.listValue;
        }

        public boolean isNoValue() {
            return this.noValue;
        }

        public boolean isSingleValue() {
            return this.singleValue;
        }
    }

    public static class Criteria extends Example.GeneratedCriteria {
        protected Criteria(Map<String, EntityColumn> propertyMap, boolean exists, boolean notNull) {
            super(propertyMap, exists, notNull);
        }
    }

    protected abstract static class GeneratedCriteria {
        protected List<Example.Criterion> criteria;
        protected boolean exists;
        protected boolean notNull;
        protected String andOr;
        protected Map<String, EntityColumn> propertyMap;

        protected GeneratedCriteria(Map<String, EntityColumn> propertyMap, boolean exists, boolean notNull) {
            this.exists = exists;
            this.notNull = notNull;
            this.criteria = new ArrayList();
            this.propertyMap = propertyMap;
        }

        private String column(String property) {
            if (this.propertyMap.containsKey(property)) {
                return ((EntityColumn)this.propertyMap.get(property)).getColumn();
            } else if (this.exists) {
                throw new ValidateException("当前实体类不包含名为" + property + "的属性!");
            } else {
                return null;
            }
        }

        private String property(String property) {
            if (this.propertyMap.containsKey(property)) {
                return property;
            } else if (this.exists) {
                throw new ValidateException("当前实体类不包含名为" + property + "的属性!");
            } else {
                return null;
            }
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new ValidateException("Value for condition cannot be null");
            } else if (!condition.startsWith("null")) {
                this.criteria.add(new Example.Criterion(condition));
            }
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                if (this.notNull) {
                    throw new ValidateException("Value for " + property + " cannot be null");
                }
            } else if (property != null) {
                this.criteria.add(new Example.Criterion(condition, value));
            }
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 != null && value2 != null) {
                if (property != null) {
                    this.criteria.add(new Example.Criterion(condition, value1, value2));
                }
            } else if (this.notNull) {
                throw new ValidateException("Between values for " + property + " cannot be null");
            }
        }

        protected void addOrCriterion(String condition) {
            if (condition == null) {
                throw new ValidateException("Value for condition cannot be null");
            } else if (!condition.startsWith("null")) {
                this.criteria.add(new Example.Criterion(condition, true));
            }
        }

        protected void addOrCriterion(String condition, Object value, String property) {
            if (value == null) {
                if (this.notNull) {
                    throw new ValidateException("Value for " + property + " cannot be null");
                }
            } else if (property != null) {
                this.criteria.add(new Example.Criterion(condition, value, true));
            }
        }

        protected void addOrCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 != null && value2 != null) {
                if (property != null) {
                    this.criteria.add(new Example.Criterion(condition, value1, value2, true));
                }
            } else if (this.notNull) {
                throw new ValidateException("Between values for " + property + " cannot be null");
            }
        }

        public Example.Criteria andIsNull(String property) {
            this.addCriterion(this.column(property) + " is null");
            return (Example.Criteria)this;
        }

        public Example.Criteria andIsNotNull(String property) {
            this.addCriterion(this.column(property) + " is not null");
            return (Example.Criteria)this;
        }

        public Example.Criteria andEqualTo(String property, Object value) {
            this.addCriterion(this.column(property) + " =", value, this.property(property));
            return (Example.Criteria)this;
        }

        public Example.Criteria andNotEqualTo(String property, Object value) {
            this.addCriterion(this.column(property) + " <>", value, this.property(property));
            return (Example.Criteria)this;
        }

        public Example.Criteria andGreaterThan(String property, Object value) {
            this.addCriterion(this.column(property) + " >", value, this.property(property));
            return (Example.Criteria)this;
        }

        public Example.Criteria andGreaterThanOrEqualTo(String property, Object value) {
            this.addCriterion(this.column(property) + " >=", value, this.property(property));
            return (Example.Criteria)this;
        }

        public Example.Criteria andLessThan(String property, Object value) {
            this.addCriterion(this.column(property) + " <", value, this.property(property));
            return (Example.Criteria)this;
        }

        public Example.Criteria andLessThanOrEqualTo(String property, Object value) {
            this.addCriterion(this.column(property) + " <=", value, this.property(property));
            return (Example.Criteria)this;
        }

        public Example.Criteria andIn(String property, Iterable<?> values) {
            this.addCriterion(this.column(property) + " in", values, this.property(property));
            return (Example.Criteria)this;
        }

        public Example.Criteria andNotIn(String property, Iterable<?> values) {
            this.addCriterion(this.column(property) + " not in", values, this.property(property));
            return (Example.Criteria)this;
        }

        public Example.Criteria andBetween(String property, Object value1, Object value2) {
            this.addCriterion(this.column(property) + " between", value1, value2, this.property(property));
            return (Example.Criteria)this;
        }

        public Example.Criteria andNotBetween(String property, Object value1, Object value2) {
            this.addCriterion(this.column(property) + " not between", value1, value2, this.property(property));
            return (Example.Criteria)this;
        }

        public Example.Criteria andLike(String property, String value) {
            this.addCriterion(this.column(property) + "  like", value, this.property(property));
            return (Example.Criteria)this;
        }

        public Example.Criteria andNotLike(String property, String value) {
            this.addCriterion(this.column(property) + "  not like", value, this.property(property));
            return (Example.Criteria)this;
        }

        public Example.Criteria andCondition(String condition) {
            this.addCriterion(condition);
            return (Example.Criteria)this;
        }

        public Example.Criteria andCondition(String condition, Object value) {
            this.criteria.add(new Example.Criterion(condition, value));
            return (Example.Criteria)this;
        }

        public Example.Criteria andEqualTo(Object param) {
            MetaObject metaObject = MetaObjectUtil.forObject(param);
            String[] properties = metaObject.getGetterNames();
            String[] var4 = properties;
            int var5 = properties.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                String property = var4[var6];
                if (this.propertyMap.get(property) != null) {
                    Object value = metaObject.getValue(property);
                    if (value != null) {
                        this.andEqualTo(property, value);
                    }
                }
            }

            return (Example.Criteria)this;
        }

        public Example.Criteria andAllEqualTo(Object param) {
            MetaObject metaObject = MetaObjectUtil.forObject(param);
            String[] properties = metaObject.getGetterNames();
            String[] var4 = properties;
            int var5 = properties.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                String property = var4[var6];
                if (this.propertyMap.get(property) != null) {
                    Object value = metaObject.getValue(property);
                    if (value != null) {
                        this.andEqualTo(property, value);
                    } else {
                        this.andIsNull(property);
                    }
                }
            }

            return (Example.Criteria)this;
        }

        public Example.Criteria orIsNull(String property) {
            this.addOrCriterion(this.column(property) + " is null");
            return (Example.Criteria)this;
        }

        public Example.Criteria orIsNotNull(String property) {
            this.addOrCriterion(this.column(property) + " is not null");
            return (Example.Criteria)this;
        }

        public Example.Criteria orEqualTo(String property, Object value) {
            this.addOrCriterion(this.column(property) + " =", value, this.property(property));
            return (Example.Criteria)this;
        }

        public Example.Criteria orNotEqualTo(String property, Object value) {
            this.addOrCriterion(this.column(property) + " <>", value, this.property(property));
            return (Example.Criteria)this;
        }

        public Example.Criteria orGreaterThan(String property, Object value) {
            this.addOrCriterion(this.column(property) + " >", value, this.property(property));
            return (Example.Criteria)this;
        }

        public Example.Criteria orGreaterThanOrEqualTo(String property, Object value) {
            this.addOrCriterion(this.column(property) + " >=", value, this.property(property));
            return (Example.Criteria)this;
        }

        public Example.Criteria orLessThan(String property, Object value) {
            this.addOrCriterion(this.column(property) + " <", value, this.property(property));
            return (Example.Criteria)this;
        }

        public Example.Criteria orLessThanOrEqualTo(String property, Object value) {
            this.addOrCriterion(this.column(property) + " <=", value, this.property(property));
            return (Example.Criteria)this;
        }

        public Example.Criteria orIn(String property, Iterable<?> values) {
            this.addOrCriterion(this.column(property) + " in", values, this.property(property));
            return (Example.Criteria)this;
        }

        public Example.Criteria orNotIn(String property, Iterable<?> values) {
            this.addOrCriterion(this.column(property) + " not in", values, this.property(property));
            return (Example.Criteria)this;
        }

        public Example.Criteria orBetween(String property, Object value1, Object value2) {
            this.addOrCriterion(this.column(property) + " between", value1, value2, this.property(property));
            return (Example.Criteria)this;
        }

        public Example.Criteria orNotBetween(String property, Object value1, Object value2) {
            this.addOrCriterion(this.column(property) + " not between", value1, value2, this.property(property));
            return (Example.Criteria)this;
        }

        public Example.Criteria orLike(String property, String value) {
            this.addOrCriterion(this.column(property) + "  like", value, this.property(property));
            return (Example.Criteria)this;
        }

        public Example.Criteria orNotLike(String property, String value) {
            this.addOrCriterion(this.column(property) + "  not like", value, this.property(property));
            return (Example.Criteria)this;
        }

        public Example.Criteria orCondition(String condition) {
            this.addOrCriterion(condition);
            return (Example.Criteria)this;
        }

        public Example.Criteria orCondition(String condition, Object value) {
            this.criteria.add(new Example.Criterion(condition, value, true));
            return (Example.Criteria)this;
        }

        public Example.Criteria orEqualTo(Object param) {
            MetaObject metaObject = MetaObjectUtil.forObject(param);
            String[] properties = metaObject.getGetterNames();
            String[] var4 = properties;
            int var5 = properties.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                String property = var4[var6];
                if (this.propertyMap.get(property) != null) {
                    Object value = metaObject.getValue(property);
                    if (value != null) {
                        this.orEqualTo(property, value);
                    }
                }
            }

            return (Example.Criteria)this;
        }

        public Example.Criteria orAllEqualTo(Object param) {
            MetaObject metaObject = MetaObjectUtil.forObject(param);
            String[] properties = metaObject.getGetterNames();
            String[] var4 = properties;
            int var5 = properties.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                String property = var4[var6];
                if (this.propertyMap.get(property) != null) {
                    Object value = metaObject.getValue(property);
                    if (value != null) {
                        this.orEqualTo(property, value);
                    } else {
                        this.orIsNull(property);
                    }
                }
            }

            return (Example.Criteria)this;
        }

        public List<Example.Criterion> getAllCriteria() {
            return this.criteria;
        }

        public String getAndOr() {
            return this.andOr;
        }

        public void setAndOr(String andOr) {
            this.andOr = andOr;
        }

        public List<Example.Criterion> getCriteria() {
            return this.criteria;
        }

        public boolean isValid() {
            return this.criteria.size() > 0;
        }
    }

    public static class OrderBy {
        protected Map<String, EntityColumn> propertyMap;
        private Example example;
        private Boolean isProperty;

        public OrderBy(Example example, Map<String, EntityColumn> propertyMap) {
            this.example = example;
            this.propertyMap = propertyMap;
        }

        private String property(String property) {
            if (!StringUtils.isEmpty(property) && !StringUtils.isEmpty(property.trim())) {
                property = property.trim();
                if (!this.propertyMap.containsKey(property)) {
                    throw new ValidateException("当前实体类不包含名为" + property + "的属性!");
                } else {
                    return ((EntityColumn)this.propertyMap.get(property)).getColumn();
                }
            } else {
                throw new ValidateException("接收的property为空！");
            }
        }

        public Example.OrderBy orderBy(String property) {
            String column = this.property(property);
            if (column == null) {
                this.isProperty = false;
                return this;
            } else {
                if (!StringUtils.isEmpty(this.example.getOrderByClause())) {
                    this.example.setOrderByClause(this.example.getOrderByClause() + "," + column);
                } else {
                    this.example.setOrderByClause(column);
                }

                this.isProperty = true;
                return this;
            }
        }

        public Example.OrderBy desc() {
            if (this.isProperty) {
                this.example.setOrderByClause(this.example.getOrderByClause() + " DESC");
                this.isProperty = false;
            }

            return this;
        }

        public Example.OrderBy asc() {
            if (this.isProperty) {
                this.example.setOrderByClause(this.example.getOrderByClause() + " ASC");
                this.isProperty = false;
            }

            return this;
        }
    }
}
