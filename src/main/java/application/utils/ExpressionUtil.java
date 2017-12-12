package application.utils;


import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lancec
 *         Date: 13-9-6
 */
public class ExpressionUtil {
    private static final String EXP = "\\$\\{(.+?)\\}";
    private static final String ARRAY_EXP = "(\\w+)\\[(.+)\\]";

    /**
     * Eval the value of given expression.
     *
     * @param obj  The object which will be eval, for example, it's an instance of SearchCriteria.
     * @param text The give expression, for example: ${sorters[0].name}
     * @return Return the evaled value, if failed, return Null.
     */
    public static Object eval(Object obj, String text) {
        if (text == null) {
            return null;
        }

        Object result = text;
        Pattern pattern = Pattern.compile(EXP, Pattern.MULTILINE);
        Pattern arrayPattern = Pattern.compile(ARRAY_EXP, Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            String group = matcher.group(1);
            String[] groups = group.split("\\.");
            Object current = obj;

            for (String exp : groups) {
                if (current == null) {
                    break;
                }
                Matcher arrayMather = arrayPattern.matcher(exp);
                String fieldName = null;
                String arraySufix = null;
                if (arrayMather.find()) {
                    fieldName = arrayMather.group(1);
                    arraySufix = arrayMather.group(2);
                } else {
                    fieldName = exp;
                }
                Object invokeResult = invokeGetterMethod(current, fieldName);
                if (invokeResult == null) {
                    current = null;
                    break;
                } else {
                    if (arraySufix != null) {
                        if (arraySufix.matches("\\d+")) {
                            int arrayIndex = Integer.parseInt(arraySufix);
                            if (invokeResult instanceof Iterable) {
                                if (invokeResult instanceof List) {
                                    try {
                                        current = ((List) invokeResult).get(arrayIndex);
                                    } catch (IndexOutOfBoundsException ex) {
                                        current = null;
                                        break;
                                    }
                                } else {
                                    int index = 0;
                                    boolean found = false;
                                    Iterator iterator = ((Iterable) invokeResult).iterator();
                                    while (iterator.hasNext()) {
                                        if (index == arrayIndex) {
                                            current = iterator.next();
                                            found = true;
                                            break;
                                        }
                                    }
                                    if (!found) {
                                        current = null;
                                        break;
                                    }
                                }
                            } else if (invokeResult.getClass().isArray()) {
                                try {
                                    current = Array.get(invokeResult, arrayIndex);
                                } catch (IndexOutOfBoundsException ex) {
                                    current = null;
                                    break;
                                }
                            } else {
                                current = null;
                            }
                        } else {
                            arraySufix = arraySufix.replaceAll("[\"']+", "");
                            if (invokeResult instanceof Map) {
                                current = ((Map) invokeResult).get(arraySufix);
                            } else {
                                current = null;
                            }
                        }
                    } else {
                        current = invokeResult;
                    }
                }
            }
            result = current;
        }
        return result;
    }

    private static Object invokeGetterMethod(Object obj, String field) {
        String getterMethod = "get" + field.substring(0, 1).toUpperCase() + field.substring(1);
        try {
            Object result = obj.getClass().getMethod(getterMethod).invoke(obj);
            return result;
        } catch (IllegalAccessException e) {
            return null;
        } catch (InvocationTargetException e) {
            return null;
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

//    public static void view(String[] args) {
////        SearchCriteria criteria = SearchCriteria.createInstance();
////        criteria.addSorter("test");
////        System.out.println("Begin: "+eval(criteria, "${begin}"));
////        System.out.println("Size: "+eval(criteria, "${size}"));
////        System.out.println("Sorter 0: "+eval(criteria, "${sorters[0].name}"));
//
//        String content = "\"30101\": {\n" +
//                "        \"name\": \"北京\",\n" +
//                "        \"nextLevelIds\": [30101],\n" +
//                "        \"firstPinyin\": \"B\"\n" +
//                "    },\n" +
//                "    \"30201\": {\n" +
//                "        \"name\": \"上海\",\n" +
//                "        \"nextLevelIds\": [30201],\n" +
//                "        \"firstPinyin\": \"S\"\n" +
//                "    }";
////        String regEx = "\"(\\d+)\":\\s*\\{\\s*\"name\":\\s*\"(.+?)\",\\s*\"nextLevelIds\":\\s*\\[(.+?)],.+?}";
//        String regEx = "\"(\\d+)\":\\s*\\{\\s*\"name\":\\s*\"(.+?)\",\\s*\"nextLevelIds\":\\s*\\[(.+?)],\\s*\"firstPinyin\":\\s*\"(\\w+)\"\\s*}";
//        Pattern pattern = Pattern.compile(regEx);
//        Matcher matcher = pattern.matcher(content);
//        while (matcher.find()) {
//            String id = matcher.group(1);
//            String name = matcher.group(2);
//            String childIds = matcher.group(3);
//            String firstPinyin = matcher.group(4);
//            System.out.println("ID: " + id + ", Name: " + name + ", ChildIds: " + childIds + ", First Pinyin: " + firstPinyin);
////            System.out.println("ID: " + id + ", Name: " + name);
//        }
//    }
}
