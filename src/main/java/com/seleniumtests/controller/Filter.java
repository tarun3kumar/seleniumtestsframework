package com.seleniumtests.controller;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Filter provides a convenient way of retrieving records from spreadsheets
 * as well as emails based on conditions.
 *
 *
 */

public class Filter {

    // //////////////// Operator Enumeration /////////////
    static enum Operator {
        Equals, EqualsIgnoreCase, Lt, Gt, Between, In, IsNull, Not, Contains, ContainsIgnoreCase, StartsWith, StartsWithIgnoreCase, EndsWith, EndsWithIgnoreCase, Or, And;
    }

    public static Filter and(Filter left, Filter right) {
        return new Filter(left, right, Operator.And);
    }

    public static Filter between(String name, Date value1, Date value2) {
        return new Filter(name, new Object[] { value1, value2 },
                Operator.Between);
    }

    public static Filter between(String name, Number value1, Number value2) {
        return new Filter(name, new Object[] { value1, value2 },
                Operator.Between);
    }

    public static Filter contains(String name, String value) {
        return new Filter(name, value, Operator.Contains);
    }

    public static Filter containsIgnoreCase(String name, String value) {
        return new Filter(name, value, Operator.ContainsIgnoreCase);
    }

    public static Filter endsWith(String name, String value) {
        return new Filter(name, value, Operator.EndsWith);
    }

    public static Filter endsWithIgnoreCase(String name, String value) {
        return new Filter(name, value, Operator.EndsWithIgnoreCase);
    }

    public static Filter equals(String name, Object value) {
        return new Filter(name, value, Operator.Equals);
    }

    public static Filter equalsIgnoreCase(String name, String value) {
        return new Filter(name, value, Operator.EqualsIgnoreCase);
    }

    public static Filter gt(String name, Date value) {
        return new Filter(name, value, Operator.Gt);
    }

    public static Filter gt(String name, Number value) {
        return new Filter(name, value, Operator.Gt);
    }

    public static Filter in(String name, Object[] values) {
        return new Filter(name, values, Operator.In);
    }

    public static Filter isNull(String name) {
        return new Filter(name, (String[]) null, Operator.IsNull);
    }

    public static Filter lt(String name, Date value) {
        return new Filter(name, value, Operator.Lt);
    }

    public static Filter lt(String name, Number value) {
        return new Filter(name, value, Operator.Lt);
    }

    @SuppressWarnings("deprecation")
    public static void main(String[] st) {
        Filter f = Filter.between("mydate", new Date("01-JAN-2008"),
                new Date("01-JAN-2011"));

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("mydate", new Date("01-JAN-2010"));
        System.out.println(f.match(map));

        map.put("mydate", new Date("01-JAN-2007"));
        System.out.println(f.match(map));

        map.put("mydate", new Date("01-JAN-2012"));
        System.out.println(f.match(map));

        // Date[] mydates = {new Date("01-JAN-2008"),new Date("01-JAN-2010"),new
        // Date("01-JAN-2011")};
    }

    @SuppressWarnings("incomplete-switch")
    private static boolean match(Filter filter,
                                 Map<String, Object> parameters) {
        String name = (filter.name != null ? filter.name.toUpperCase() : null);
        Object values[] = filter.values;
        Operator operator = filter.operator;
        Filter left = filter.left;
        Filter right = filter.right;

        if (Operator.And.equals(operator)) {
            return left.match(parameters) && right.match(parameters);
        } else if (Operator.Or.equals(operator)) {
            return left.match(parameters) || right.match(parameters);
        } else if (Operator.Not.equals(operator)) {
            return !right.match(parameters);
        } else if (!parameters.containsKey(name)) {
            return false;
        } else if (Operator.IsNull.equals(operator)) {// Direct Null Check
            return (parameters.get(name) == null);
        } else if ((Operator.Equals.equals(operator) || Operator.EqualsIgnoreCase
                .equals(operator))
                && (values == null || (values.length == 1 && values[0] == null))) {// Indirect
            // Null
            // Check
            return (parameters.get(name) == null);
        } else if (Operator.Equals.equals(operator)) {
            return parameters.get(name).equals(values[0]);
        } else if (Operator.EqualsIgnoreCase.equals(operator)) {
            return parameters.get(name).toString().toLowerCase()
                    .equals(values[0].toString().toLowerCase());
        } else if (Operator.In.equals(operator)) {
            boolean found = false;
            for (int i = 0; i < values.length; i++) {
                if (parameters.get(name).equals(values[i])) {
                    found = true;
                    break;
                }
            }
            return found;
        } else if (values == null || values[0] == null) {
            throw new RuntimeException(
                    "Null values are not supported for Filter Operation: "
                            + operator);
        } else {// By the time the control reaches here values is not null

            // String specific: Contains, ContainsIgnoreCase
            if (values[0] instanceof String) {
                switch (operator) {
                    case Contains:
                        return parameters.get(name).toString()
                                .contains(values[0].toString());
                    case ContainsIgnoreCase:
                        return parameters.get(name).toString().toLowerCase()
                                .contains(values[0].toString().toLowerCase());
                    case StartsWith:
                        return parameters.get(name).toString()
                                .startsWith(values[0].toString());
                    case StartsWithIgnoreCase:
                        return parameters.get(name).toString().toLowerCase()
                                .startsWith(values[0].toString().toLowerCase());
                    case EndsWith:
                        return parameters.get(name).toString()
                                .endsWith(values[0].toString());
                    case EndsWithIgnoreCase:
                        return parameters.get(name).toString().toLowerCase()
                                .endsWith(values[0].toString().toLowerCase());
                }

            } else if (values[0] instanceof Number) {// Number & Date specific:
                // Between, Lt, Gt

                BigDecimal val = new BigDecimal(parameters.get(name).toString());
                BigDecimal leftValue = new BigDecimal(values[0].toString());
                BigDecimal rightValue = null;

                switch (operator) {
                    case Between:
                        rightValue = new BigDecimal(values[1].toString());
                        return leftValue.compareTo(val) < 1
                                && rightValue.compareTo(val) > -1;
                    case Lt:
                        return val.compareTo(leftValue) == -1;
                    case Gt:
                        return val.compareTo(leftValue) == 1;
                    default:
                        break;
                }
            } else if (values[0] instanceof Date) {
                Date date = null;
                try {
                    date = DateFormat.getDateInstance().parse(
                            parameters.get(name).toString());
                } catch (ParseException e) {
                    //throw new RuntimeException(e);
                    date = (Date)parameters.get(name);
                }
                Date dateLeft = (Date) values[0];
                Date dateRight = null;

                switch (operator) {
                    case Between:
                        dateRight = (Date) values[1];
                        return (dateLeft.before(date) || dateLeft.equals(date))
                                && (date.before(dateRight) || date
                                .equals(dateRight));
                    case Lt:
                        return date.before(dateLeft);
                    case Gt:
                        return date.after(dateLeft);
                    default:
                        break;
                }
            }
        }

        throw new RuntimeException("Should not reach here. Not Implemented Yet"
                + "\n" + filter + "\n" + parameters);
    }

    // //////////////// Static Methods //////////////////////

    public static Filter not(Filter exp) {
        return new Filter((Filter) null, exp, Operator.Not);
    }

    public static Filter or(Filter left, Filter right) {
        return new Filter(left, right, Operator.Or);
    }

    public static Filter startsWith(String name, String value) {
        return new Filter(name, value, Operator.StartsWith);
    }

    public static Filter startsWithIgnoreCase(String name, String value) {
        return new Filter(name, value, Operator.StartsWithIgnoreCase);
    }

    private String name;

    private Object[] values;

    private Operator operator;

    private Filter left;

    private Filter right;

    private Filter(Filter left, Filter right, Operator condition) {
        this.left = left;
        this.right = right;
        this.operator = condition;
    }

    public Filter(String name, Object value, Operator condition) {
        this(name, new Object[] { value }, condition);
    }

    public Filter(String name, Object[] values, Operator condition) {
        super();
        this.name = name;
        this.values = values;
        this.operator = condition;
    }

    public Filter getLeft() {
        return left;
    }

    public String getName() {
        return name;
    }

    public Operator getOperator() {
        return operator;
    }

    public Filter getRight() {
        return right;
    }

    public Object[] getValues() {
        return values;
    }

    public boolean match(Map<String, Object> parameters) {
        Map<String, Object> parameters2 = new HashMap<String, Object>();
        for (Entry<String, Object> entry : parameters.entrySet()) {
            parameters2.put(entry.getKey().toUpperCase(), entry.getValue());
        }
        return match(this, parameters2);
    }

    public void setLeft(Filter left) {
        this.left = left;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOperator(Operator condition) {
        this.operator = condition;
    }

    public void setRight(Filter right) {
        this.right = right;
    }

    public void setValues(Object[] values) {
        this.values = values;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        if (name != null) {
            sb.append(name + " " + operator.toString() + " "
                    + Arrays.toString(values));
        } else {
            sb.append((left != null ? left.toString() : "") + " "
                    + operator.toString() + " " + right.toString());
        }

        return "(" + sb.toString() + ")";
    }

    public String toXML() {
        StringBuffer sb = new StringBuffer();

        if (left != null)
            sb.append("\n<crit:left>").append(left.toXML())
                    .append("\n</crit:left>");
        if (name != null)
            sb.append("\n<crit:name>").append(name).append("</crit:name>");
        if (operator != null)
            sb.append("\n<crit:operator>").append(operator)
                    .append("</crit:operator>");
        if (values != null) {
            sb.append("\n<crit:values>");
            for (Object obj : values) {
                sb.append("\n\t<ws:item>").append(obj.toString())
                        .append("</ws:item>");
            }
            sb.append("\n</crit:values>");
        }
        if (right != null)
            sb.append("\n<crit:right>").append(right.toXML())
                    .append("\n</crit:right>");

        return sb.toString();
    }
}
