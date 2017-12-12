package application.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.Random;
import java.util.UUID;

/**
 * @author Lance Chen
 */
public class StringUtil {
    public static final String EMAIL_PATTERN = "^[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+$";
    public static final String IP_PATTERN = "^(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])$";
    public static final String MOBILE_PHONE_PATTERN = "^1\\d{10}$";


    private StringUtil() {
    }

    /**
     * Determine a string is an integer.
     *
     * @param str The string
     * @return Return true if is integer
     */
    public static boolean isInteger(String str) {
        try {
            Integer.parseInt(str.trim());
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean isFloat(String str) {
        try {
            Float.parseFloat(str);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean isBigDecimal(String str) {
        try {
            new BigDecimal(str);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * Determine a string is empty.
     *
     * @param str The given string
     * @return Return true if is empty
     */
    public static boolean isEmpty(String str) {
        if (str == null || str.trim().length() <= 0 || "null".equalsIgnoreCase(str)) {
            return true;
        }
        return false;
    }

    /**
     * Determine a string is not empty.
     *
     * @param str
     * @return Return true if is not empty
     * @author haleyg
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * Determine a string is email.
     *
     * @param email The given string
     * @return Return true if is email
     */
    public static boolean isEmail(String email) {
        if (StringUtil.isEmpty(email)) {
            return false;
        }
        return email.matches(EMAIL_PATTERN);
    }

    /**
     * Convert string to lower and the first character to upper.
     *
     * @param str The string
     * @return Return string
     */
    public static String firstToUpper(String str) {
        if (isEmpty(str)) {
            return str;
        }
        str = str.trim().toLowerCase();
        str = str.substring(0, 1).toUpperCase() + str.substring(1);
        return str;
    }

    /**
     * Determine whether the given String is an IP.
     *
     * @param ip The give string
     * @return Return true if it's IP formatting.
     */
    public static boolean isIP(String ip) {
        if (isEmpty(ip)) {
            return false;
        }
        return ip.matches(IP_PATTERN);
    }

    public static boolean isMobilePhone(String mobilePhone) {
        if (isEmpty(mobilePhone)) {
            return false;
        }
        return mobilePhone.matches(MOBILE_PHONE_PATTERN);
    }

    /**
     * Create a random string by length, this string contains numbers and words(Upper and Lower).
     *
     * @param length The specified string length, if length <=0, default to 6.
     * @return Return random string
     */
    public static String createRandomString(int length) {
        String codes = "1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        if (length <= 0) {
            length = 6;
        }
        for (int i = 0; i < length; i++) {
            sb.append(codes.charAt(random.nextInt(codes.length())));
        }
        return sb.toString();
    }

    public static String createNumberRandomString(int length) {
        String codes = "1234567890";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        if (length <= 0) {
            length = 6;
        }
        for (int i = 0; i < length; i++) {
            sb.append(codes.charAt(random.nextInt(codes.length())));
        }
        return sb.toString();
    }

    /**
     * Split a String to an array.
     *
     * @param source    The source string
     * @param delimiter The delimiter, eg. , ; .
     * @return Return the string arry
     */
    public static String[] split(String source, String delimiter) {
        if (source == null) {
            return new String[0];
        }
        String[] result = source.trim().split("\\s*" + delimiter.trim() + "\\s*");
        return result;
    }

    /**
     * First Character UpCase
     *
     * @param str
     * @return
     * @author haleyg
     */
    public static String capitalize(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return str;
        }
        return new StringBuilder(strLen)
                .append(Character.toTitleCase(str.charAt(0)))
                .append(str.substring(1))
                .toString();
    }


    public static Integer convertToInteger(String value) {
        try {
            if (value == null) {
                return null;
            }
            return Integer.valueOf(value);
        } catch (Exception ex) {
            return null;
        }
    }

    public static Long convertToLong(String value) {
        try {
            return Long.valueOf(value);
        } catch (Exception ex) {
            return null;
        }
    }

    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }


    public static String getExceptionStackTrace(Throwable ex) {
        StringWriter writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        return writer.toString();
    }
//    public static void view(String[] args){
//        String uid=generateUUID();
//        System.out.println(uid.length()+", "+uid);
//    }
}
