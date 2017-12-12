package application.utils;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by lancec on 2014/6/24.
 */
public class SearchUtil {
    public static String generateFilterUrl(String requestUrl, Map<String, String[]> parameterMap, String paramName, String paramValue) {
        Map<String, String[]> parameters = new LinkedHashMap<String, String[]>();
        parameters.putAll(parameterMap);
        parameters.put(paramName, new String[]{paramValue});
        StringBuilder url = buildRequestUrlAndQuery(requestUrl, parameters);
        return url.toString();
    }

    public static String generateFilterUrl(String requestUrl, Map<String, String[]> parameterMap, Map<String, String> params) {
        Map<String, String[]> parameters = new LinkedHashMap<String, String[]>();
        parameters.putAll(parameterMap);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            parameters.put(entry.getKey(), new String[]{entry.getValue()});
        }

        StringBuilder url = buildRequestUrlAndQuery(requestUrl, parameters);
        return url.toString();
    }


    public static String generateExcludeUrl(String requestUrl, Map<String, String[]> parameterMap, String excludeParameter) {
        Map<String, String[]> parameters = new LinkedHashMap<String, String[]>();
        parameters.putAll(parameterMap);
        Iterator<String> keyIterator = parameters.keySet().iterator();
        while (keyIterator.hasNext()) {
            String paramName = keyIterator.next();
            if (paramName.equals(excludeParameter)) {
                keyIterator.remove();
            }
        }

        StringBuilder url = buildRequestUrlAndQuery(requestUrl, parameters);
        return url.toString();
    }

    protected static StringBuilder buildRequestUrlAndQuery(String requestUrl, Map<String, String[]> parameterMap) {
        StringBuilder url = new StringBuilder();
        url.append(requestUrl);
        int index = 0;
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            if (entry.getKey().equals("pageNo") || entry.getKey().equals("pageSize")) {
                continue;
            }
            if (index++ == 0) {
                url.append("?");
            } else {
                url.append("&");
            }
            String value = "";
            if (entry.getValue() != null && entry.getValue().length > 0) {
                value = entry.getValue()[0];
            }
            value = URLUtil.URLEncoding(value);
            url.append(entry.getKey()).append("=").append(value);
        }
        return url;
    }

}
