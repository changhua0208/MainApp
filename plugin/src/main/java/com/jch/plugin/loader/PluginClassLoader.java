package com.jch.plugin.loader;

import com.jch.plugin.cpu.CPUFrameworkHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import dalvik.system.DexClassLoader;

/**
 * @author changhua.jiang
 * @since 2017/9/22 下午6:07
 */

class PluginClassLoader extends DexClassLoader {


    public PluginClassLoader(String dexPath, String optimizedDirectory, String libraryPath, ClassLoader parent) {
        super(dexPath, optimizedDirectory, libraryPath, parent);
        releaseLib(dexPath,libraryPath);
    }

    /**
     * 可以在这里搞事情
     * @param name
     * @return
     * @throws ClassNotFoundException
     */
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return super.findClass(name);
    }

    @Override
    public String findLibrary(String name) {
        String ret =  super.findLibrary(name);
        return ret;
    }

    private void releaseLib(String dexPath,String libPath){
        try {
            ZipFile zip = new ZipFile(dexPath);
            InputStream in = null;
            List<String> abiList = CPUFrameworkHelper.getSupportABI();
            boolean findit = false;
            for(String abi : abiList) {
                for (Enumeration<? extends ZipEntry> entries = zip.entries(); entries.hasMoreElements(); ) {
                    ZipEntry entry = entries.nextElement();
                    String prefix = "lib/" + abi +"/";
                    String name = entry.getName();
                    if (name.startsWith(prefix)) {
                        findit = true;
                        int index = name.lastIndexOf("/");
                        String libFullName = libPath + name.substring(index);
                        File libFile = new File(libFullName);
                        if (!libFile.exists()) {
                            in = zip.getInputStream(entry);
                            copyFileFrom(in, libFile);
                            in.close();
                        }
                    }
                }
                if(findit)
                    break;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
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
}
