package application.utils;

import java.io.*;

/**
 * @author Lance Chen
 */
public class ObjectUtil {

    /**
     * Copy the same properties source object to target class.
     *
     * @param source The source object
     * @param target The target object
     * @return Return the target object
     */
/*    public static <T> T copyProperties(Object source, Class<T> target) {
        try {
            T to = target.newInstance();
            return copyProperties(source, to);
        } catch (InstantiationException ex) {
            throw new RuntimeException(ex);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
    }*/

    /**
     * Copy the same properties from source to target.
     *
     * @param source The source object
     * @param target The target object
     * @return Return the target object
     */
/*    public static <T> T copyProperties(Object source, T target) {
        Class fromClass = source.getClass();
        Class toClass = target.getClass();
        Field[] fromFields = ReflectionUtil.getNonStaticFields(fromClass);
        for (Field fromField : fromFields) {
            try {
                Field toField = ReflectionUtil.getField(toClass, fromField.getName());
                if (toField != null && toField.getType().equals(fromField.getType())) {
                    Object fromValue = ReflectionUtil.invokeGetter(source, fromField.getName());
                    ReflectionUtil.invokeSetter(target, fromField.getName(), fromValue);
                }
            } catch (Exception ex) {
                continue;
            }
        }
        return target;
    }*/

    /**
     * Copy the same properties from source to target.
     *
     * @param source                 The source object
     * @param ignoreSourceProperties The ignore source properties
     * @param target                 The target object
     * @return Return the target object
     */
//    public static <T> T copyProperties(Object source, String[] ignoreSourceProperties, T target) {
//        Class fromClass = source.getClass();
//        Class toClass = target.getClass();
//        Field[] fromFields = ReflectionUtil.getNonStaticFields(fromClass);
//        for (Field fromField : fromFields) {
//            try {
//                boolean ignored = false;
//                for (String ignoreSource : ignoreSourceProperties) {
//                    if (fromField.getName().equals(ignoreSource)) {
//                        ignored = true;
//                        break;
//                    }
//                }
//                if (ignored) {
//                    continue;
//                }
//                Field toField = ReflectionUtil.getField(toClass, fromField.getName());
//                if (toField != null && toField.getType().equals(fromField.getType())) {
//                    Object fromValue = ReflectionUtil.invokeGetter(source, fromField.getName());
//                    ReflectionUtil.invokeSetter(target, fromField.getName(), fromValue);
//                }
//            } catch (Exception ex) {
//                continue;
//            }
//        }
//        return target;
//    }

    public static byte[] toByteArray(Object object) {
        ByteArrayOutputStream bas = new ByteArrayOutputStream();
        try {
            ObjectOutputStream os = new ObjectOutputStream(bas);
            os.writeObject(object);
            return bas.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object fromByteArray(byte[] byteArray) {
        ByteArrayInputStream bai = new ByteArrayInputStream(byteArray);
        try {
            ObjectInputStream is = new ObjectInputStream(bai);
            try {
                return is.readObject();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
