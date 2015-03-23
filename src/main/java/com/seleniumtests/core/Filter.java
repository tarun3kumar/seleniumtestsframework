/*
 * Copyright 2015 www.seleniumtests.com
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.seleniumtests.core;

import java.math.BigDecimal;

import java.text.DateFormat;
import java.text.ParseException;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Provides mechanism to retrieve records from spreadsheets.
 */
public class Filter {

    static enum Operator {
        EQUALS,
        EQUALS_IGNORE_CASE,
        LESS_THAN,
        GREATER_THAN,
        BETWEEN,
        IN,
        IS_NULL,
        NOT,
        CONTAINS,
        CONTAINS_IGNORE_CASE,
        STARTS_WITH,
        STARTS_WITH_IGNORE_CASE,
        ENDS_WITH,
        ENDS_WITH_IGNORE_CASE,
        OR,
        AND;
    }

    public static Filter and(final Filter left, final Filter right) {
        return new Filter(left, right, Operator.AND);
    }

    public static Filter contains(final String name, final String value) {
        return new Filter(name, value, Operator.CONTAINS);
    }

    public static Filter containsIgnoreCase(final String name, final String value) {
        return new Filter(name, value, Operator.CONTAINS_IGNORE_CASE);
    }

    public static Filter equals(final String name, final Object value) {
        return new Filter(name, value, Operator.EQUALS);
    }

    public static Filter equalsIgnoreCase(final String name, final String value) {
        return new Filter(name, value, Operator.EQUALS_IGNORE_CASE);
    }

    public static Filter greaterThan(final String name, final Number value) {
        return new Filter(name, value, Operator.GREATER_THAN);
    }

    public static Filter in(final String name, final Object[] values) {
        return new Filter(name, values, Operator.IN);
    }

    public static Filter lt(final String name, final Date value) {
        return new Filter(name, value, Operator.LESS_THAN);
    }

    public static Filter lt(final String name, final Number value) {
        return new Filter(name, value, Operator.LESS_THAN);
    }

    private static boolean match(final Filter filter, final Map<String, Object> parameters) {
        String name = (filter.name != null ? filter.name.toUpperCase() : null);
        Object[] values = filter.values;
        Operator operator = filter.operator;
        Filter left = filter.left;
        Filter right = filter.right;

        if (Operator.AND.equals(operator)) {
            return left.match(parameters) && right.match(parameters);
        } else if (Operator.OR.equals(operator)) {
            return left.match(parameters) || right.match(parameters);
        } else if (Operator.NOT.equals(operator)) {
            return !right.match(parameters);
        } else if (!parameters.containsKey(name)) {
            return false;
        } else if (Operator.IS_NULL.equals(operator)) {
            return (parameters.get(name) == null);
        } else if ((Operator.EQUALS.equals(operator) || Operator.EQUALS_IGNORE_CASE.equals(operator))
                && (values == null || (values.length == 1 && values[0] == null))) {
            return (parameters.get(name) == null);
        } else if (Operator.EQUALS.equals(operator)) {
            return parameters.get(name).equals(values[0]);
        } else if (Operator.EQUALS_IGNORE_CASE.equals(operator)) {
            return parameters.get(name).toString().toLowerCase().equals(values[0].toString().toLowerCase());
        } else if (Operator.IN.equals(operator)) {
            boolean found = false;
            for (Object value : values) {
                if (parameters.get(name).equals(value)) {
                    found = true;
                    break;
                }
            }

            return found;
        } else if (values == null || values[0] == null) {
            throw new RuntimeException("Filter Operation does not support Null values: " + operator);
        } else {
            if (values[0] instanceof String) {
                switch (operator) {

                    case CONTAINS :
                        return parameters.get(name).toString().contains(values[0].toString());

                    case CONTAINS_IGNORE_CASE :
                        return parameters.get(name).toString().toLowerCase().contains(values[0].toString()
                                    .toLowerCase());

                    case STARTS_WITH :
                        return parameters.get(name).toString().startsWith(values[0].toString());

                    case STARTS_WITH_IGNORE_CASE :
                        return parameters.get(name).toString().toLowerCase().startsWith(values[0].toString()
                                    .toLowerCase());

                    case ENDS_WITH :
                        return parameters.get(name).toString().endsWith(values[0].toString());

                    case ENDS_WITH_IGNORE_CASE :
                        return parameters.get(name).toString().toLowerCase().endsWith(values[0].toString()
                                    .toLowerCase());
                }

            } else if (values[0] instanceof Number) {
                BigDecimal val = new BigDecimal(parameters.get(name).toString());
                BigDecimal leftValue = new BigDecimal(values[0].toString());
                BigDecimal rightValue;

                switch (operator) {

                    case BETWEEN :
                        rightValue = new BigDecimal(values[1].toString());
                        return leftValue.compareTo(val) < 1 && rightValue.compareTo(val) > -1;

                    case LESS_THAN :
                        return val.compareTo(leftValue) == -1;

                    case GREATER_THAN :
                        return val.compareTo(leftValue) == 1;

                    default :
                        break;
                }
            } else if (values[0] instanceof Date) {
                Date date;
                try {
                    date = DateFormat.getDateInstance().parse(parameters.get(name).toString());
                } catch (ParseException e) {
                    date = (Date) parameters.get(name);
                }

                Date dateLeft = (Date) values[0];
                Date dateRight;

                switch (operator) {

                    case BETWEEN :
                        dateRight = (Date) values[1];
                        return (dateLeft.before(date) || dateLeft.equals(date))
                                && (date.before(dateRight) || date.equals(dateRight));

                    case LESS_THAN :
                        return date.before(dateLeft);

                    case GREATER_THAN :
                        return date.after(dateLeft);

                    default :
                        break;
                }
            }
        }

        throw new RuntimeException("NOT Implemented Yet" + "\n" + filter + "\n" + parameters);
    }

    public static Filter not(final Filter exp) {
        return new Filter((Filter) null, exp, Operator.NOT);
    }

    public static Filter or(final Filter left, final Filter right) {
        return new Filter(left, right, Operator.OR);
    }

    private String name;

    private Object[] values;

    private Operator operator;

    private Filter left;

    private Filter right;

    private Filter(final Filter left, final Filter right, final Operator condition) {
        this.left = left;
        this.right = right;
        this.operator = condition;
    }

    public Filter(final String name, final Object value, final Operator condition) {
        this(name, new Object[] {value}, condition);
    }

    public Filter(final String name, final Object[] values, final Operator condition) {
        super();
        this.name = name;
        this.values = values;
        this.operator = condition;
    }

    public String getName() {
        return name;
    }

    public boolean match(final Map<String, Object> parameters) {
        Map<String, Object> parameters2 = new HashMap<String, Object>();
        for (Entry<String, Object> entry : parameters.entrySet()) {
            parameters2.put(entry.getKey().toUpperCase(), entry.getValue());
        }

        return match(this, parameters2);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        if (name != null) {
            sb.append(name + " " + operator.toString() + " " + Arrays.toString(values));
        } else {
            sb.append((left != null ? left.toString() : "") + " " + operator.toString() + " " + right.toString());
        }

        return "(" + sb.toString() + ")";
    }
}
