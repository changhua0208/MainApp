package com.jch

import org.apache.tools.ant.util.FileUtils
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.transform.ArtifactTransform

import javax.inject.Inject
import java.util.jar.JarOutputStream
import java.util.jar.Manifest
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

public class MyPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {

        project.getConfigurations().create("AARCompile", new Action<Configuration>() {
            @Override
            public void execute(Configuration config) {

            }
        })
    }
}
public class ExtractAarTransform extends ArtifactTransform {
    private static final String LIBS_PREFIX = "libs/";
    private static final int LIBS_PREFIX_LENGTH = "libs/".length();
    private static final int JARS_PREFIX_LENGTH = "jars".length() + 1;

    @Inject
    public ExtractAarTransform() {
    }

    public List<File> transform(File input) {
        //TODO 1
        File outputDir = new File(this.getOutputDirectory(), input.name);
        FileUtils.mkdirs(outputDir);

        StringBuilder sb = new StringBuilder(20);
        sb.append("jars").append(File.separatorChar);

        try {
            InputStream fis = new BufferedInputStream(new FileInputStream(input));
            Throwable var5 = null;

            try {
                ZipInputStream zis = new ZipInputStream(fis);
                Throwable var7 = null;

                try {
                    ZipEntry entry;
                    try {
                        while ((entry = zis.getNextEntry()) != null) {
                            try {
                                String name = entry.getName();
                                if (!entry.isDirectory()) {
                                    //TODO 2
                                    String path = name;
                                    if (!name.equals("classes.jar")) {
                                        continue
                                    }

                                    if (!name.equals("classes.jar") && !name.equals("lint.jar")) {
                                        if (name.startsWith("libs/")) {
                                            sb.setLength(JARS_PREFIX_LENGTH);
                                            String path2 = name.substring(LIBS_PREFIX_LENGTH);
                                            if ("classes.jar".equals(path2)) {
                                                sb.append("libs/").append("classes-2.jar");
                                            } else if ("lint.jar".equals(path2)) {
                                                sb.append("libs/").append("lint-2.jar");
                                            } else {
                                                sb.append("libs/").append(path2);
                                            }

                                            path = sb.toString();
                                        }
                                    } else {
                                        sb.setLength(JARS_PREFIX_LENGTH);
                                        sb.append(name);
                                        path = sb.toString();
                                    }

                                    File outputFile = new File(outputDir, path.replace("/".chars[0], File.separatorChar));
                                    FileUtils.mkdirs(outputFile.getParentFile());
                                    OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFile));
                                    Throwable var13 = null;

                                    try {
                                        ByteStreams.copy(zis, outputStream);
                                        outputStream.flush();
                                    } catch (Throwable var81) {
                                        var13 = var81;
                                        throw var81;
                                    } finally {
                                        if (outputStream != null) {
                                            if (var13 != null) {
                                                try {
                                                    outputStream.close();
                                                } catch (Throwable var79) {
                                                    var13.addSuppressed(var79);
                                                }
                                            } else {
                                                outputStream.close();
                                            }
                                        }

                                    }
                                }
                            } finally {
                                zis.closeEntry();
                            }
                        }
                    } catch (Throwable var84) {
                        var7 = var84;
                        throw var84;
                    }
                } finally {
                    if (zis != null) {
                        if (var7 != null) {
                            try {
                                zis.close();
                            } catch (Throwable var78) {
                                var7.addSuppressed(var78);
                            }
                        } else {
                            zis.close();
                        }
                    }

                }
            } catch (Throwable var86) {
                var5 = var86;
                throw var86;
            } finally {
                if (fis != null) {
                    if (var5 != null) {
                        try {
                            fis.close();
                        } catch (Throwable var77) {
                            var5.addSuppressed(var77);
                        }
                    } else {
                        fis.close();
                    }
                }

            }
        } catch (Throwable var88) {
            throw new RuntimeException(var88);
        }

        File classesJar = new File(new File(outputDir, "jars"), "classes.jar");
        if (!classesJar.exists()) {
            try {
                Files.createParentDirs(classesJar);
                JarOutputStream jarOutputStream = new JarOutputStream(new BufferedOutputStream(new FileOutputStream(classesJar)), new Manifest());
                jarOutputStream.close();
            } catch (IOException var80) {
                throw new RuntimeException("Cannot create missing classes.jar", var80);
            }
        }

        return ImmutableList.of(outputDir);
    }
}

public class AarTransform extends ArtifactTransform {

    @Inject
    public AarTransform() {
    }

    public List<File> transform(File input) {
        return getJars(input);
    }

    private static List<File> getJars(File explodedAar) {
        List<File> files = List.newArrayList();
        File jarFolder = new File(explodedAar, "jars");

        File file = FileUtils.join(jarFolder, "classes.jar");

        if (file.isFile()) {
            files.add(file);
        }

        File localJarFolder = new File(jarFolder, "libs");
        File[] jars = localJarFolder.listFiles(new FilenameFilter() {
            @Override
            boolean accept(File dir, String name) {
                return name.endsWith(".jar");
            }
        });
        if (jars != null) {
            files.addAll(Arrays.asList(jars));
        }

        return files;
    }
}