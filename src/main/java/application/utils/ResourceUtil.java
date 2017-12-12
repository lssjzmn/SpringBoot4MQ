package application.utils;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * The resource Util
 *
 * @author Lance Chen
 */
public class ResourceUtil {

    /**
     * Get message from resource bundle by parameters.
     *
     * @param bundle  The bundle
     * @param msgCode The message key
     * @param params  The parameters, could be empty
     * @return Return Null if message not found.
     */
    public static String getMessage(ResourceBundle bundle, String msgCode, Object... params) {
        if (msgCode == null) {
            return null;
        }
        try {
            String message = bundle.getString(msgCode);
            if (params != null) {
                int index = 0;
                for (Object param : params) {
                    if (param == null) {
                        index++;
                        continue;
                    }
                    message = message.replace("{" + index + "}", param.toString());
                    index++;
                }
            }
            return message;
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Get message by key and Locale from base name i18n/messages.
     *
     * @param key    The message key
     * @param locale The Locale
     * @param params The message parameters
     * @return Return the value
     */
    public static String getMessage(String key, Locale locale, Object... params) {
        ResourceBundle bundle = ResourceBundle.getBundle("i18n/messages", locale);
        return getMessage(bundle, key, params);
    }

    /**
     * Get validation message by key and Locale from base name i18n/validations.
     *
     * @param key    The validation message key
     * @param locale The Locale
     * @param params The message parameters
     * @return Return the value
     */
    public static String getValidationMessage(String key, Locale locale, Object... params) {
        ResourceBundle bundle = ResourceBundle.getBundle("i18n/validations", locale);
        return getMessage(bundle, key, params);
    }
}
