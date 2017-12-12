package application.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.*;

/**
 * @author lancec
 */
public class URLUtil {

    private static final Logger logger = LoggerFactory.getLogger(URLUtil.class);

    /**
     * Post data to url.
     *
     * @param urlString  The request url
     * @param parameters The parameters
     * @return Return the response string
     */
    public static String postData(String urlString, String parameters) {
        InputStream is = null;
        PrintWriter writer = null;
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            writer = new PrintWriter(connection.getOutputStream());
            writer.print(parameters);

            is = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder data = new StringBuilder();
            String currentLine = null;
            int index = 0;
            String line = System.getProperty("line.separator");
            while ((currentLine = reader.readLine()) != null) {
                if (index++ != 0) {
                    data.append(line);
                }
                data.append(currentLine);
            }
            return data.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ex) {
                }
            }
            if (writer != null) {
                writer.close();
            }
        }
    }

    /**
     * Get data from url.
     *
     * @param urlString The request url
     * @return Return the response string
     */
    public static String getData(String urlString) {
        InputStream is = null;
        try {
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(2 * 60 * 1000);
            is = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder data = new StringBuilder();
            String currentLine = null;
            int index = 0;
            String line = System.getProperty("line.separator");
            while ((currentLine = reader.readLine()) != null) {
                if (index++ != 0) {
                    data.append(line);
                }
                data.append(currentLine);
            }
            return data.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ex) {
                }
            }
        }
    }

    public static String getData(String urlString, String enc) {
        InputStream is = null;
        try {
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(2 * 60 * 1000);
            is = connection.getInputStream();
            StringBuilder data = new StringBuilder();
            byte[] bytes = new byte[1024];
            int read = -1;
            while ((read = is.read(bytes)) != -1) {
                data.append(new String(bytes, 0, read, enc));
            }
            return data.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ex) {
                }
            }
        }
    }

    /**
     * URL Encoding with UTF-8
     *
     * @param str The string
     * @return Return the encoding string
     */
    public static String URLEncoding(String str) {
        try {
            return URLEncoder.encode(str == null ? "" : str.trim(), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * URL Decoding with UTF-8
     *
     * @param str The string
     * @return Return the decoding string
     */
    public static String URLDecoding(String str) {
        try {
            return URLDecoder.decode(str == null ? "" : str.trim(), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Combine the url and query string.
     *
     * @param url        The url
     * @param parameters The parameters
     * @return Return the url which contains query string
     */
    public static String createURLwithParameter(String url, String parameters) {
        StringBuilder result = new StringBuilder();
        result.append(url);
        if (url.contains("?")) {
            result.append("&");
        } else {
            result.append("?");
        }
        result.append(parameters);
        return result.toString();
    }

    /**
     * Combine the url and  parameters.
     *
     * @param url        The url
     * @param param      The parameter
     * @param paramValue The  parameter value
     * @return Return the url which contains  parameter and value
     */
    public static String addParameter(String url, String param, String paramValue) {
        StringBuilder result = new StringBuilder();
        result.append(url);
        if (url.contains("?")) {
            result.append("&");
        } else {
            result.append("?");
        }
        result.append(param);
        result.append("=");
        result.append(paramValue);
        return result.toString();
    }

    /**
     * Combine the url and  parameters.
     *
     * @param url        The url
     * @param param      The parameter
     * @param paramValue The  parameter value
     * @return Return the url which contains  parameter and value
     */
    public static String addParameter(String url, String param, Integer paramValue) {
        StringBuilder result = new StringBuilder();
        result.append(url);
        if (url.contains("?")) {
            result.append("&");
        } else {
            result.append("?");
        }
        result.append(param);
        result.append("=");
        result.append(paramValue.intValue());
        return result.toString();
    }

    /**
     * Save a URL resource to file.
     *
     * @param urlString The URL
     * @param filePath  The save file path, if not exist the path, will be created.
     */
    public static void saveToFile(String urlString, String filePath) {
        InputStream is = null;
        try {
            URL url = new URL(urlString);
            is = url.openStream();
            FileUtil.save(is, filePath);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    /**
     * Get inputstream from url.
     *
     * @param urlString The request url
     * @return Return the response inputstream
     */
    public static InputStream getInputStream(String urlString) {
        InputStream is = null;
        try {
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(2 * 60 * 1000);
            is = connection.getInputStream();
        } catch (Exception ex) {
            logger.error("Get InputStream Error: ", ex);
        }
        return is;
    }

    public static OutputStream getOutputStream(String urlString) {
        URL url = null;
        try {
            url = new URL(urlString);
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(10 * 60 * 1000);
            return connection.getOutputStream();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get inputstream from url.
     *
     * @param url The request url
     * @return Return the response inputstream
     */
    public static InputStream getInputStream(URL url) {
        InputStream is = null;
        try {
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(2 * 60 * 1000);
            is = connection.getInputStream();
        } catch (Exception ex) {
            logger.error("Get InputStream Error: ", ex);
        }
        return is;
    }
}
