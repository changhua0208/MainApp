package com.jch.utils;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * 文件操作
 *
 * @author changhua.jiang
 * @since 2017/11/23 下午3:38
 */

public class FileHelper {

    public static void copyFile(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        copyFileFrom(in, dst);
        in.close();
    }

    public static void copyFileFrom(InputStream in, File dst) throws IOException {
        int maxBuffSize = 10 * 1024;
        OutputStream out = new FileOutputStream(dst);
        int len = 0;
        byte[] buff = new byte[maxBuffSize];//10k
        while ((len = in.read(buff)) >= 0) {
            out.write(buff,0,len);
        }
        out.close();
    }

    public static void copyFileFrom(InputStream in, String dst) throws IOException {
        File file = new File(dst);
        copyFileFrom(in, file);
    }

    public static String read(File file, String charset) throws IOException {
        if (!file.exists()) {
            return null;
        }
        FileInputStream in = null;
        in = new FileInputStream(file);
        int len = 0;
        byte[] buff = new byte[10 * 1024];//10k
        while ((len = in.read(buff)) >= 0) {

        }
        in.close();
        return new String(buff,charset);
    }

    public static String read(File file) throws IOException {

        return read(file,"utf-8");
    }

    public static String read(String filePath, String charset) throws IOException {
        File file = new File(filePath);
        return read(file, charset);
    }

    public static String read(String filePath) throws IOException {
        File file = new File(filePath);
        return read(file);
    }

    //深度优先，删除文件或文件夹
    public static void delete(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (File f : file.listFiles()) {
                    delete(f);
                }
            } else {
                file.delete();
            }
        } else if (file.isFile()) {
            file.delete();
        }
    }

    public static void delete(String filePath) {
        File file = new File(filePath);
        delete(file);
    }

    //删除suffix后缀的文件
    public static void delete(File file, String suffix) {
        if (file.isFile()) {
            if (file.getName().endsWith("." + suffix)) {
                file.delete();
            }
        } else if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (File f : file.listFiles()) {
                    delete(f, suffix);
                }
            }
        }
    }

    public static void unzip(File zipFile, String location) throws IOException {
        if (!zipFile.isDirectory()) {
            zipFile.mkdirs();
        }
        if(!location.endsWith("/")){
            location += "/";
        }
        ZipInputStream zin = new ZipInputStream(new FileInputStream(zipFile));
        try {
            ZipEntry ze = null;
            while ((ze = zin.getNextEntry()) != null) {
                String path = location + ze.getName();

                if (ze.isDirectory()) {
                    File unzipFile = new File(path);
                    if (!unzipFile.isDirectory()) {
                        unzipFile.mkdirs();
                    }
                } else {
//                    FileOutputStream fout = new FileOutputStream(path, false);
                    File dstFile = new File(path);
                    try {
                        copyFileFrom(zin,dstFile);
                        zin.closeEntry();
                    } finally {
                        //fout.close();
                    }
                }
            }
        } finally {
            zin.close();
        }

    }

    public static void unzip(String zipFile, String location) throws IOException {
        File f = new File(zipFile);
        unzip(f,location);
    }

}
