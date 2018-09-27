package com.example.demo.configs.mapper.helper;

import java.util.ArrayList;
import java.util.List;

public class Sqls {
    private Sqls.Criteria criteria = new Sqls.Criteria();

    private Sqls() {
    }

    public static Sqls custom() {
        return new Sqls();
    }

    public Sqls.Criteria getCriteria() {
        return this.criteria;
    }

    public Sqls andIsNull(String property) {
        this.criteria.criterions.add(new Sqls.Criterion(property, "is null", "and"));
        return this;
    }

    public Sqls andIsNotNull(String property) {
        this.criteria.criterions.add(new Sqls.Criterion(property, "is not null", "and"));
        return this;
    }

    public Sqls andEqualTo(String property, Object value) {
        this.criteria.criterions.add(new Sqls.Criterion(property, value, "=", "and"));
        return this;
    }

    public Sqls andNotEqualTo(String property, Object value) {
        this.criteria.criterions.add(new Sqls.Criterion(property, value, "<>", "and"));
        return this;
    }

    public Sqls andGreaterThan(String property, Object value) {
        this.criteria.criterions.add(new Sqls.Criterion(property, value, ">", "and"));
        return this;
    }

    public Sqls andGreaterThanOrEqualTo(String property, Object value) {
        this.criteria.criterions.add(new Sqls.Criterion(property, value, ">=", "and"));
        return this;
    }

    public Sqls andLessThan(String property, Object value) {
        this.criteria.criterions.add(new Sqls.Criterion(property, value, "<", "and"));
        return this;
    }

    public Sqls andLessThanOrEqualTo(String property, Object value) {
        this.criteria.criterions.add(new Sqls.Criterion(property, value, "<=", "and"));
        return this;
    }

    public Sqls andIn(String property, Iterable<?> values) {
        this.criteria.criterions.add(new Sqls.Criterion(property, values, "in", "and"));
        return this;
    }

    public Sqls andNotIn(String property, Iterable<?> values) {
        this.criteria.criterions.add(new Sqls.Criterion(property, values, "not in", "and"));
        return this;
    }

    public Sqls andBetween(String property, Object value1, Object value2) {
        this.criteria.criterions.add(new Sqls.Criterion(property, value1, value2, "between", "and"));
        return this;
    }

    public Sqls andNotBetween(String property, Object value1, Object value2) {
        this.criteria.criterions.add(new Sqls.Criterion(property, value1, value2, "not between", "and"));
        return this;
    }

    public Sqls andLike(String property, String value) {
        this.criteria.criterions.add(new Sqls.Criterion(property, value, "like", "and"));
        return this;
    }

    public Sqls andNotLike(String property, String value) {
        this.criteria.criterions.add(new Sqls.Criterion(property, value, "not like", "and"));
        return this;
    }

    public Sqls orIsNull(String property) {
        this.criteria.criterions.add(new Sqls.Criterion(property, "is null", "or"));
        return this;
    }

    public Sqls orIsNotNull(String property) {
        this.criteria.criterions.add(new Sqls.Criterion(property, "is not null", "or"));
        return this;
    }

    public Sqls orEqualTo(String property, Object value) {
        this.criteria.criterions.add(new Sqls.Criterion(property, value, "=", "or"));
        return this;
    }

    public Sqls orNotEqualTo(String property, Object value) {
        this.criteria.criterions.add(new Sqls.Criterion(property, value, "<>", "or"));
        return this;
    }

    public Sqls orGreaterThan(String property, Object value) {
        this.criteria.criterions.add(new Sqls.Criterion(property, value, ">", "or"));
        return this;
    }

    public Sqls orGreaterThanOrEqualTo(String property, Object value) {
        this.criteria.criterions.add(new Sqls.Criterion(property, value, ">=", "or"));
        return this;
    }

    public Sqls orLessThan(String property, Object value) {
        this.criteria.criterions.add(new Sqls.Criterion(property, value, "<", "or"));
        return this;
    }

    public Sqls orLessThanOrEqualTo(String property, Object value) {
        this.criteria.criterions.add(new Sqls.Criterion(property, value, "<=", "or"));
        return this;
    }

    public Sqls orIn(String property, Iterable<?> values) {
        this.criteria.criterions.add(new Sqls.Criterion(property, values, "in", "or"));
        return this;
    }

    public Sqls orNotIn(String property, Iterable<?> values) {
        this.criteria.criterions.add(new Sqls.Criterion(property, values, "not in", "or"));
        return this;
    }

    public Sqls orBetween(String property, Object value1, Object value2) {
        this.criteria.criterions.add(new Sqls.Criterion(property, value1, value2, "between", "or"));
        return this;
    }

    public Sqls orNotBetween(String property, Object value1, Object value2) {
        this.criteria.criterions.add(new Sqls.Criterion(property, value1, value2, "not between", "or"));
        return this;
    }

    public Sqls orLike(String property, String value) {
        this.criteria.criterions.add(new Sqls.Criterion(property, value, "like", "or"));
        return this;
    }

    public Sqls orNotLike(String property, String value) {
        this.criteria.criterions.add(new Sqls.Criterion(property, value, "not like", "or"));
        return this;
    }

    public static class Criterion {
        private String property;
        private Object value;
        private Object secondValue;
        private String condition;
        private String andOr;

        public Criterion(String property, String condition, String andOr) {
            this.property = property;
            this.condition = condition;
            this.andOr = andOr;
        }

        public Criterion(String property, Object value, String condition, String andOr) {
            this.property = property;
            this.value = value;
            this.condition = condition;
            this.andOr = andOr;
        }

        public Criterion(String property, Object value1, Object value2, String condition, String andOr) {
            this.property = property;
            this.value = value1;
            this.secondValue = value2;
            this.condition = condition;
            this.andOr = andOr;
        }

        public String getProperty() {
            return this.property;
        }

        public Object getValue() {
            return this.value;
        }

        public Object getSecondValue() {
            return this.secondValue;
        }

        public Object[] getValues() {
            if (this.value != null) {
                return this.secondValue != null ? new Object[]{this.value, this.secondValue} : new Object[]{this.value};
            } else {
                return new Object[0];
            }
        }

        public String getCondition() {
            return this.condition;
        }

        public String getAndOr() {
            return this.andOr;
        }
    }

    public static class Criteria {
        private String andOr;
        private List<Criterion> criterions = new ArrayList(2);

        public Criteria() {
        }

        public List<Sqls.Criterion> getCriterions() {
            return this.criterions;
        }

        public String getAndOr() {
            return this.andOr;
        }

        public void setAndOr(String andOr) {
            this.andOr = andOr;
        }
    }
}

