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
 * EasyFilter provides a convenient way of retrieving records from spreadsheets
 * as well as emails based on conditions.
 *
 *
 */

public class EasyFilter {

    // //////////////// Operator Enumeration /////////////
    static enum Operator {
        Equals, EqualsIgnoreCase, Lt, Gt, Between, In, IsNull, Not, Contains, ContainsIgnoreCase, StartsWith, StartsWithIgnoreCase, EndsWith, EndsWithIgnoreCase, Or, And;
    }

    public static EasyFilter and(EasyFilter left, EasyFilter right) {
        return new EasyFilter(left, right, Operator.And);
    }

    public static EasyFilter between(String name, Date value1, Date value2) {
        return new EasyFilter(name, new Object[] { value1, value2 },
                Operator.Between);
    }

    public static EasyFilter between(String name, Number value1, Number value2) {
        return new EasyFilter(name, new Object[] { value1, value2 },
                Operator.Between);
    }

    public static EasyFilter contains(String name, String value) {
        return new EasyFilter(name, value, Operator.Contains);
    }

    public static EasyFilter containsIgnoreCase(String name, String value) {
        return new EasyFilter(name, value, Operator.ContainsIgnoreCase);
    }

    public static EasyFilter endsWith(String name, String value) {
        return new EasyFilter(name, value, Operator.EndsWith);
    }

    public static EasyFilter endsWithIgnoreCase(String name, String value) {
        return new EasyFilter(name, value, Operator.EndsWithIgnoreCase);
    }

    public static EasyFilter equals(String name, Object value) {
        return new EasyFilter(name, value, Operator.Equals);
    }

    public static EasyFilter equalsIgnoreCase(String name, String value) {
        return new EasyFilter(name, value, Operator.EqualsIgnoreCase);
    }

    public static EasyFilter gt(String name, Date value) {
        return new EasyFilter(name, value, Operator.Gt);
    }

    public static EasyFilter gt(String name, Number value) {
        return new EasyFilter(name, value, Operator.Gt);
    }

    public static EasyFilter in(String name, Object[] values) {
        return new EasyFilter(name, values, Operator.In);
    }

    public static EasyFilter isNull(String name) {
        return new EasyFilter(name, (String[]) null, Operator.IsNull);
    }

    public static EasyFilter lt(String name, Date value) {
        return new EasyFilter(name, value, Operator.Lt);
    }

    public static EasyFilter lt(String name, Number value) {
        return new EasyFilter(name, value, Operator.Lt);
    }

    @SuppressWarnings("deprecation")
    public static void main(String[] st) {
        EasyFilter f = EasyFilter.between("mydate", new Date("01-JAN-2008"),
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
    private static boolean match(EasyFilter filter,
                                 Map<String, Object> parameters) {
        String name = (filter.name != null ? filter.name.toUpperCase() : null);
        Object values[] = filter.values;
        Operator operator = filter.operator;
        EasyFilter left = filter.left;
        EasyFilter right = filter.right;

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

    public static EasyFilter not(EasyFilter exp) {
        return new EasyFilter((EasyFilter) null, exp, Operator.Not);
    }

    public static EasyFilter or(EasyFilter left, EasyFilter right) {
        return new EasyFilter(left, right, Operator.Or);
    }

    public static EasyFilter startsWith(String name, String value) {
        return new EasyFilter(name, value, Operator.StartsWith);
    }

    public static EasyFilter startsWithIgnoreCase(String name, String value) {
        return new EasyFilter(name, value, Operator.StartsWithIgnoreCase);
    }

    private String name;

    private Object[] values;

    private Operator operator;

    private EasyFilter left;

    private EasyFilter right;

    private EasyFilter(EasyFilter left, EasyFilter right, Operator condition) {
        this.left = left;
        this.right = right;
        this.operator = condition;
    }

    public EasyFilter(String name, Object value, Operator condition) {
        this(name, new Object[] { value }, condition);
    }

    public EasyFilter(String name, Object[] values, Operator condition) {
        super();
        this.name = name;
        this.values = values;
        this.operator = condition;
    }

    public EasyFilter getLeft() {
        return left;
    }

    public String getName() {
        return name;
    }

    public Operator getOperator() {
        return operator;
    }

    public EasyFilter getRight() {
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

    public void setLeft(EasyFilter left) {
        this.left = left;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOperator(Operator condition) {
        this.operator = condition;
    }

    public void setRight(EasyFilter right) {
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
