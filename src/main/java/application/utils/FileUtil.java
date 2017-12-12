package application.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.zip.Adler32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author Lance Chen
 */
public class FileUtil {

    private static final int BSIZE = 1024;
    public static final String CHARSET_STR = "UTF-8";
    private static final Charset CHARSET = Charset.forName(System.getProperty("file.encoding"));

    public static boolean deleteDirectory(File dir) {
        if ((dir == null) || !dir.isDirectory()) {
            throw new IllegalArgumentException("Argument " + dir
                    + " is not a directory. ");
        }

        File[] entries = dir.listFiles();
        int sz = entries.length;

        for (int i = 0; i < sz; i++) {
            if (entries[i].isDirectory()) {
                if (!deleteDirectory(entries[i])) {
                    return false;
                }
            } else {
                if (!entries[i].delete()) {
                    return false;
                }
            }
        }

        if (!dir.delete()) {
            return false;
        }
        return true;
    }

    /**
     * Read a file to string by default char set.
     *
     * @param fileName The absolute file name
     * @return Return the file string
     */
    public static String readFile(String fileName) {
        return readFile(fileName, CHARSET.name());
    }

    /**
     * Read a file to string by given char set name.
     *
     * @param fileName    The absolute file name
     * @param charsetName The char set name
     * @return Return the file string
     */
    public static String readFile(String fileName, String charsetName) {
        FileInputStream in = null;
        try {
            StringBuilder result = new StringBuilder();
            in = new FileInputStream(fileName);
            FileChannel fc = in.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(BSIZE);
            Charset charset = Charset.forName(charsetName);
            try {
                while (fc.read(buffer) != -1) {
                    buffer.flip();
                    result.append(charset.decode(buffer).toString());
                    buffer.clear();
                }
            } finally {
                fc.close();
            }
            return result.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    /**
     * Write a string to given absolute file name by default file system char
     * set.
     *
     * @param fileName The saving file name
     * @param content  The string content
     */
    public static void writeFile(String fileName, String content) {
        writeFile(fileName, content, CHARSET.name());
    }

    /**
     * Write a string to given absolute file name by given char set.
     *
     * @param fileName    The absolute saving file name
     * @param content     The string content
     * @param charsetName The char set name
     */
    public static void writeFile(String fileName, String content, String charsetName) {
        FileOutputStream out = null;
        try {
            File file = new File(fileName);
            makeParentDir(file);
            out = new FileOutputStream(file);
            FileChannel fc = out.getChannel();
            ByteBuffer buffer = ByteBuffer.wrap(content.getBytes(charsetName));
            try {
                fc.write(buffer);
            } finally {
                fc.close();
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    /**
     * Make parent dir for file while parent dir not exist.
     *
     * @param file The absolute file
     */
    public static void makeParentDir(File file) {
        File parent = file.getParentFile();
        if (!parent.exists()) {
            parent.mkdirs();
        }
    }

    /**
     * Make parent dir for file name while parent dir not exist.
     *
     * @param fileName The absolute file name
     */
    public static void makeParentDir(String fileName) {
        File file = new File(fileName);
        makeParentDir(file);
    }

    /**
     * Copy src file to dest file.
     *
     * @param srcFile  The absolute src file name
     * @param destFile the absolute dest file name
     */
    public static void copy(String srcFile, String destFile) {
        FileChannel in = null;
        FileChannel out = null;
        try {
            in = new FileInputStream(srcFile).getChannel();

            makeParentDir(destFile);
            out = new FileOutputStream(destFile).getChannel();
            in.transferTo(0, in.size(), out);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    /**
     * Saving file channel to dest file.
     *
     * @param in       The fileChannel
     * @param destFile The absolute dest file name
     */
    public static void save(FileChannel in, String destFile) {
        FileChannel out = null;
        try {
            makeParentDir(destFile);
            out = new FileOutputStream(destFile).getChannel();
            in.transferTo(0, in.size(), out);

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                out.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    /**
     * Saving inputstream to dest file.
     *
     * @param in       The inputstream
     * @param destFile The absolute dest file name
     */
    public static void save(InputStream in, String destFile) {
        OutputStream out = null;
        makeParentDir(destFile);
        try {
            out = new BufferedOutputStream(new FileOutputStream(destFile));
            byte[] buffer = new byte[in.available()];
            int readbytes = -1;
            while ((readbytes = in.read(buffer)) != -1) {
                out.write(buffer, 0, readbytes);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    /**
     * Compress multiply files to one file.
     *
     * @param files       The files
     * @param outFilePath The out put file name
     * @param comment     The comment for compress file
     */
    public static void compress(File[] files, String outFilePath, String comment) {
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(outFilePath);
            CheckedOutputStream cos = new CheckedOutputStream(outputStream, new Adler32());
            ZipOutputStream zos = new ZipOutputStream(cos);
            BufferedOutputStream out = new BufferedOutputStream(zos);
            zos.setComment(comment);
            for (File file : files) {
                BufferedInputStream in = null;
                try {
                    in = new BufferedInputStream(new FileInputStream(file));
                    zos.putNextEntry(new ZipEntry(file.getName()));
                    int c;
                    while ((c = in.read()) != -1) {
                        out.write(c);
                    }
                    out.flush();
                } finally {
                    if (in != null) {
                        in.close();
                    }
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                outputStream.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    /**
     * Compress one file or one dir(include sub dir) to an zip file.
     *
     * @param file        The file or dir
     * @param outFilePath The output file path
     * @param comment     The comment for zip file
     */
    public static void compress(File file, String outFilePath, String comment) {
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(outFilePath);
            CheckedOutputStream cos = new CheckedOutputStream(outputStream, new Adler32());
            ZipOutputStream zos = new ZipOutputStream(cos);
            zos.setComment(comment);
            compressZip(file.getAbsolutePath(), file.getName(), file, zos);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    /**
     * Compress a file dir to zip file.
     *
     * @param rootPath The root dir
     * @param rootName The root file name
     * @param file     The current file
     * @param zos      The zip output stream
     * @throws FileNotFoundException
     * @throws IOException
     */
    private static void compressZip(String rootPath, String rootName, File file, ZipOutputStream zos) throws FileNotFoundException, IOException {
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                compressZip(rootPath, rootName, f, zos);
            }
        } else {
            BufferedInputStream in = null;
            try {
                in = new BufferedInputStream(new FileInputStream(file));
                String absolutePath = file.getAbsolutePath();
                String entryName = rootName + absolutePath.substring(absolutePath.indexOf(rootPath) + rootPath.length());
                ZipEntry entry = new ZipEntry(entryName);
                zos.putNextEntry(entry);

                BufferedOutputStream out = new BufferedOutputStream(zos);
                int c;
                while ((c = in.read()) != -1) {
                    out.write(c);
                }
                out.flush();
            } finally {
                if (in != null) {
                    in.close();
                }
            }
        }
    }

    /**
     * Get the file suffix by file name.
     *
     * @param fileName The file name
     * @return Return the suffix of file name, after "." text
     */
    public static String getFilePrefix(String fileName) {
        if (fileName != null) {
            int index = fileName.lastIndexOf(".");
            if (index > 0 && index < fileName.length() - 1) {
                return fileName.substring(0, index);
            } else {
                return fileName;
            }
        } else {
            return "";
        }
    }

    /**
     * Get the file suffix by file name.
     *
     * @param fileName The file name
     * @return Return the suffix of file name, after "." text
     */
    public static String getFileSuffix(String fileName) {
        if (fileName != null) {
            int index = fileName.lastIndexOf(".");
            if (index > 0 && index < fileName.length() - 1) {
                return fileName.substring(index + 1);
            } else {
                return "";
            }
        } else {
            return "";
        }
    }

    public static BufferedImage readImage(InputStream in) {
        try {
            return ImageIO.read(in);
        } catch (IOException ex) {
            throw new RuntimeException("Can't read image.");
        }
    }

    public static boolean isImage(String fileType) {
        return "png".equalsIgnoreCase(fileType) || "jpeg".equalsIgnoreCase(fileType) || "bmp".equalsIgnoreCase(fileType) || "jpg".equalsIgnoreCase(fileType) || "gif".equalsIgnoreCase(fileType);
    }
}
