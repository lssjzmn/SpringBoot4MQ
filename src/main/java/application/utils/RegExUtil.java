package application.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 212466128 on 2016/5/7.
 */
public class RegExUtil {
    public static String getJsonFieldValue(String fieldName, String content) {
        String regx = "\""+fieldName+"\":\\s*\"(.+?)\"";
        Pattern p = Pattern.compile(regx);
        Matcher m = p.matcher(content);
        String value = null;
        while (m.find()) {
            value = m.group(1);
        }
        return value;
    }

    public static int getJsonIntFieldValue(String fieldName, String content) {
        String regx = "\""+fieldName+"\":\\s*(\\d+)";
        Pattern p = Pattern.compile(regx);
        Matcher m = p.matcher(content);
        String value = null;
        while (m.find()) {
            value = m.group(1);
        }
        return Integer.parseInt(value);
    }

    public static String[] getNumberInString(String str){
        String regEx = "(\\d+\\.*\\d*\\s*-\\s*\\d+\\.*\\d*)";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        while (m.find()) {
            String found = m.group(1);
            return found.split("\\s*-\\s*");
        }

        return new String[0];
    }
    public static List<String> getNumbersInStr(String str){
        List<String> numbers=new ArrayList<>();
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        String tmp1=m.replaceAll("-");
        String[] strs=tmp1.split("-");
        for (int i = 0; i < strs.length; i++) {
            if(!StringUtil.isEmpty(strs[i])){
                numbers.add(strs[i]);
            }
        }
        return numbers;
    }

//    public static void view(String[] args) {
//         String[] result = getNumberInString("fdsafdas3.7  -  9.3fdafda");
//        for (String s : result) {
//            int min=(int)(Float.valueOf(s).floatValue()*10000);
//            System.out.println(min);
//        }
//        Integer s=new Integer(200);
//        Integer r=new Integer(25);
//        float salary=s;
//        float reward=r;
//        float percent=reward/salary;
//        System.out.println(percent);
//    }
}
