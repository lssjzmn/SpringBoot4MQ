package application.utils;


import application.model.ArrayFormat;
import application.model.DateFormat;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

/**
 * @author Lance Chen
 */
public class ReflectionUtil {
    private static final String[] DEFAULT_DATE_PATTERNS = new String[]{"yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd hh:mm:ss"};
    private static final String DEFAULT_ARRAY_PATTERN = "\\s*[;]\\s*";
    private static final Map<Class, String> BASIC_TYPE_MAP = new HashMap<Class, String>();

    static {
        BASIC_TYPE_MAP.put(boolean.class, boolean.class.getSimpleName());
        BASIC_TYPE_MAP.put(Boolean.class, Boolean.class.getSimpleName());
        BASIC_TYPE_MAP.put(int.class, int.class.getSimpleName());
        BASIC_TYPE_MAP.put(Integer.class, Integer.class.getSimpleName());
        BASIC_TYPE_MAP.put(float.class, float.class.getSimpleName());
        BASIC_TYPE_MAP.put(Float.class, Float.class.getSimpleName());
        BASIC_TYPE_MAP.put(long.class, long.class.getSimpleName());
        BASIC_TYPE_MAP.put(Long.class, Long.class.getSimpleName());
        BASIC_TYPE_MAP.put(byte.class, byte.class.getSimpleName());
        BASIC_TYPE_MAP.put(Byte.class, Byte.class.getSimpleName());
        BASIC_TYPE_MAP.put(short.class, short.class.getSimpleName());
        BASIC_TYPE_MAP.put(Short.class, Short.class.getSimpleName());
        BASIC_TYPE_MAP.put(String.class, String.class.getSimpleName());
        BASIC_TYPE_MAP.put(Date.class, Date.class.getSimpleName());
        BASIC_TYPE_MAP.put(BigDecimal.class, BigDecimal.class.getSimpleName());
    }

    private ReflectionUtil() {
    }

    public static Method[] getSetterMethods(Class clazz) {
        List<Method> methods = new ArrayList<Method>();
        Class currentClass = clazz;
        while (currentClass != null && currentClass != Object.class) {
            for (Method method : currentClass.getDeclaredMethods()) {
                int modifiers = method.getModifiers();
                if (!Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers)) {
                    String methodName = method.getName();
                    if (methodName.length() > 3
                            && methodName.startsWith("set")
                            && method.getParameterTypes().length == 1) {
                        methods.add(method);
                    }
                }
            }
            currentClass = currentClass.getSuperclass();
        }
        Method[] methodArray = new Method[methods.size()];
        return methods.toArray(methodArray);
    }

    public static Field getField(String name, Class clazz) {
        Class currentClass = clazz;
        Field result = null;
        while (currentClass != null && currentClass != Object.class) {
            try {
                result = currentClass.getDeclaredField(name);
                break;
            } catch (NoSuchFieldException e) {
                currentClass = currentClass.getSuperclass();
            }
        }
        return result;
    }

    /**
     * New a instance by class.
     *
     * @param clazz The given class
     * @return Return the instance
     */
    public static <T> T newInstance(Class<T> clazz) {
        try {
            T instance = clazz.newInstance();
            return instance;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * New a instance by class.
     *
     * @param className The given class
     * @return Return the instance
     */
    @SuppressWarnings("unchecked")
    public static <T> T newInstance(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            T instance = (T) clazz.newInstance();
            return instance;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static boolean isBasicType(Class clazz) {
        String className = BASIC_TYPE_MAP.get(clazz);
        if (className == null) {
            return false;
        }
        return true;
    }

    public static Object getBasicFieldValue(String srcValue, Class fieldClass, String datePattern) {
        Object fieldValue = null;
        if (srcValue == null || srcValue.equals("null")) {
            return fieldValue;
        }
        try {
            if (fieldClass == boolean.class || fieldClass == Boolean.class) {
                fieldValue = Boolean.valueOf(srcValue);
            } else if (fieldClass == int.class || fieldClass == Integer.class) {
                fieldValue = Integer.valueOf(srcValue);
            } else if (fieldClass == float.class || fieldClass == Float.class) {
                fieldValue = Float.valueOf(srcValue);
            } else if (fieldClass == double.class || fieldClass == Double.class) {
                fieldValue = Double.valueOf(srcValue);
            } else if (fieldClass == long.class || fieldClass == Long.class) {
                fieldValue = Long.valueOf(srcValue);
            } else if (fieldClass == byte.class || fieldClass == Byte.class) {
                fieldValue = Byte.valueOf(srcValue);
            } else if (fieldClass == short.class || fieldClass == Short.class) {
                fieldValue = Short.valueOf(srcValue);
            } else if (fieldClass == String.class) {
                fieldValue = srcValue;
            } else if (fieldClass == Date.class) {
                if (datePattern != null) {
                    fieldValue = FormatUtil.parseDate(datePattern, srcValue);
                } else {
                    for (String defaultDatePattern : DEFAULT_DATE_PATTERNS) {
                        try {
                            fieldValue = FormatUtil.parseDate(defaultDatePattern, srcValue);
                            break;
                        } catch (ParseException e) {
                        }
                    }
                }
            } else if (fieldClass == BigDecimal.class) {
                fieldValue = new BigDecimal(srcValue);
            } else if (fieldClass.isEnum()) {
                fieldValue = Enum.valueOf(fieldClass, srcValue);
            }

        } catch (Exception ex) {
        }
        return fieldValue;
    }

    public static void setInstanceValue(Object instance, Map<String, String> nameValues) {
        Class clazz = instance.getClass();
        Method[] setterMethods = ReflectionUtil.getSetterMethods(clazz);
        for (Method setterMethod : setterMethods) {
            String methodName = setterMethod.getName();
            String fieldName = methodName.substring(3);
            fieldName = fieldName.substring(0, 1).toLowerCase() + fieldName.substring(1);
            Class fieldClass = setterMethod.getParameterTypes()[0];
            Object fieldValue = null;

            String strValue = nameValues.get(fieldName);
            if (strValue == null) {
                continue;
            }

            String datePattern = getDatePattern(setterMethod, fieldName, clazz);
            if (ReflectionUtil.isBasicType(fieldClass)) {
                fieldValue = ReflectionUtil.getBasicFieldValue(strValue, fieldClass, datePattern);
            } else if (fieldClass.isEnum()) {
                try {
                    fieldValue = Enum.valueOf(fieldClass, strValue);
                } catch (Exception e) {
                }
            } else if (fieldClass.isArray()) {
                String arrayPattern = getArrayPattern(setterMethod, fieldName, clazz);
                if (arrayPattern == null) {
                    arrayPattern = DEFAULT_ARRAY_PATTERN;
                }
                String[] arrayValues = strValue.split(arrayPattern);
                Class componentType = fieldClass.getComponentType();
                Object objArray = Array.newInstance(componentType, arrayValues.length);
                if (ReflectionUtil.isBasicType(componentType)) {
                    for (int i = 0; i < arrayValues.length; i++) {
                        String strItemValue = arrayValues[i];
                        Object arrayValue = ReflectionUtil.getBasicFieldValue(strItemValue, componentType, datePattern);
                        Array.set(objArray, i, arrayValue);
                    }
                    fieldValue = objArray;
                } else if (fieldClass.isEnum()) {
                    for (int i = 0; i < arrayValues.length; i++) {
                        String strItemValue = arrayValues[i];
                        Object arrayValue = null;
                        try {
                            arrayValue = Enum.valueOf(fieldClass, strItemValue);
                        } catch (Exception ex) {
                        }
                        Array.set(objArray, i, arrayValue);
                    }
                    fieldValue = objArray;
                } else {
                    fieldValue = null;
                }

            }
            try {
                setterMethod.invoke(instance, fieldValue);
            } catch (Exception e) {
            }
        }
    }

    private static String getDatePattern(Method setterMethod, String fieldName, Class objClass) {
        DateFormat dateFormatAnnotation = null;
        Annotation[] paramAnnotations = setterMethod.getParameterAnnotations()[0];
        if (paramAnnotations != null && paramAnnotations.length > 0) {
            for (Annotation paramAnnotation : paramAnnotations) {
                if (paramAnnotation.getClass() == DateFormat.class) {
                    dateFormatAnnotation = (DateFormat) paramAnnotation;
                    break;
                }
            }
        }
        if (dateFormatAnnotation == null) {
            Field field = ReflectionUtil.getField(fieldName, objClass);
            if (field != null) {
                dateFormatAnnotation = field.getAnnotation(DateFormat.class);
            }
        }
        String datePattern = null;
        if (dateFormatAnnotation != null) {
            datePattern = dateFormatAnnotation.pattern();
        }
        return datePattern;
    }

    private static String getArrayPattern(Method setterMethod, String fieldName, Class objClass) {
        ArrayFormat arrayFormatAnnotation = null;
        Annotation[] paramAnnotations = setterMethod.getParameterAnnotations()[0];
        if (paramAnnotations != null && paramAnnotations.length > 0) {
            for (Annotation paramAnnotation : paramAnnotations) {
                if (paramAnnotation.getClass() == ArrayFormat.class) {
                    arrayFormatAnnotation = (ArrayFormat) paramAnnotation;
                    break;
                }
            }
        }
        if (arrayFormatAnnotation == null) {
            Field field = ReflectionUtil.getField(fieldName, objClass);
            if (field != null) {
                arrayFormatAnnotation = field.getAnnotation(ArrayFormat.class);
            }
        }
        String datePattern = null;
        if (arrayFormatAnnotation != null) {
            datePattern = arrayFormatAnnotation.pattern();
        }
        return datePattern;
    }
}
