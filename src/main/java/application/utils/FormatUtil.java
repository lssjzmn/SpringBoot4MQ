package application.utils;

import java.math.BigDecimal;
import java.text.*;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author lancec
 */
public class FormatUtil {

    private FormatUtil() {
    }

    /**
     * Format currency.
     *
     * @param pattern The pattern
     * @param value   The format value
     * @return Return formated string
     */
    public static String formatCurrency(String pattern, double value) {
        NumberFormat format = new DecimalFormat(pattern);
        return format.format(value);
    }

    /**
     * Format a currency by default pattern $#,##0.00.
     *
     * @param value The format value
     * @return Return the String of currency
     */
    public static String formatCurrency(BigDecimal value) {
        NumberFormat format = new DecimalFormat("$#,##0.00");
        format.setMaximumFractionDigits(2);
        return format.format(value);
    }

    /**
     * Format a currency by default pattern $#,##0.00.
     *
     * @param value The format value
     * @return Return the String of currency
     */
    public static String formatCurrency(String pattern, BigDecimal value) {
        NumberFormat format = new DecimalFormat(pattern);
        format.setMaximumFractionDigits(2);
        return format.format(value);
    }

    /**
     * format Currency, eg. $100.00
     *
     * @param money
     * @param locale
     * @return
     */
    public static String formatCurrency(BigDecimal money, Locale locale) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(locale);
        currencyFormat.setMaximumFractionDigits(2);
        return currencyFormat.format(money);
    }

    /**
     * Format date.
     *
     * @param pattern The pattern
     * @param date    The date
     * @return Return the formated date
     */
    public static String formatDate(String pattern, Date date) {
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }

    /**
     * format date, eg.	'5/6/13'
     *
     * @param date
     * @param locale
     * @return
     */
    public static String formatDate(Date date, Locale locale) {
        DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT, locale);
        format.setTimeZone(TimeZone.getDefault());
        return format.format(date);
    }

    /**
     * format datetime, eg. '05/07/13 10:29 AM'
     *
     * @param date
     * @param locale
     * @return
     */
    public static String formatDateTime(Date date, Locale locale) {
        DateFormat format = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, locale);
        format.setTimeZone(TimeZone.getDefault());
        return format.format(date);
    }

    /**
     * Format date.
     *
     * @param pattern The pattern
     * @param date    The date
     * @param locale  The locale
     * @return Return the formated date
     */
    public static String formatDate(String pattern, Date date, Locale locale) {
        DateFormat dateFormat = new SimpleDateFormat(pattern, locale);
        return dateFormat.format(date);
    }

    /**
     * Format number.
     *
     * @param pattern The pattern
     * @param number  The number
     * @return Return the formated number
     */
    public static String formatNumber(String pattern, double number) {
        NumberFormat format = new DecimalFormat(pattern);
        return format.format(number);
    }

    /**
     * Parse a string to date.
     *
     * @param pattern The parse pattern
     * @param source  The string
     * @return Return Date
     * @throws ParseException
     */
    public static Date parseDate(String pattern, String source) throws ParseException {
        DateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.parse(source);
    }

    /**
     * Parse a string to date.
     *
     * @param pattern The parse pattern
     * @param source  The string
     * @return Return Date
     * @throws ParseException
     */
    public static Date parseDate(String pattern, String source, Locale locale) throws ParseException {
        DateFormat simpleDateFormat = new SimpleDateFormat(pattern, locale);
        return simpleDateFormat.parse(source);
    }
}
