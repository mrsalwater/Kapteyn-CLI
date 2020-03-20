package com.mrsalwater.kapteyn.cli.decompiler.impl;

import com.mrsalwater.kapteyn.cli.decompiler.Decompiler;
import com.mrsalwater.kapteyn.cli.decompiler.DecompilerSettings;
import com.mrsalwater.kapteyn.decompiler.bytecode.ByteCodeFile;
import com.mrsalwater.kapteyn.decompiler.bytecode.ByteCodeParser;
import com.mrsalwater.kapteyn.decompiler.classfile.ClassFile;
import com.mrsalwater.kapteyn.decompiler.classfile.ClassFileParser;
import com.mrsalwater.kapteyn.decompiler.exception.ClassFileException;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public final class JarFileDecompiler implements Decompiler {

    @Override
    public void decompile(DecompilerSettings settings) throws IOException, ClassFileException {
        JarFile jarFile = new JarFile(settings.getInputFile());
        Enumeration<JarEntry> entries = jarFile.entries();

        ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(settings.getOutputFile()));

        while (entries.hasMoreElements()) {
            JarEntry jarEntry = entries.nextElement();

            if (jarEntry.getName().contains(".")) {
                String jarEntryName = jarEntry.getName().substring(0, jarEntry.getName().lastIndexOf(".") + 1);
                String jarEntryExtension = jarEntry.getName().substring(jarEntry.getName().lastIndexOf(".") + 1);

                if (jarEntryExtension.equals("class")) {
                    byte[] bytes = toByteArray(jarFile.getInputStream(jarEntry), (int) jarEntry.getCompressedSize());

                    ClassFileParser classFileParser = new ClassFileParser(bytes);
                    ClassFile classFile = classFileParser.parse();

                    ByteCodeParser byteCodeParser = new ByteCodeParser(classFile, settings.getSettings());
                    ByteCodeFile byteCodeFile = byteCodeParser.parse();

                    byte[] source = byteCodeFile.getSource().getBytes();
                    ZipEntry zipEntry = new ZipEntry(jarEntryName.concat("source"));
                    zipOutputStream.putNextEntry(zipEntry);

                    zipOutputStream.write(source, 0, source.length);
                    zipOutputStream.closeEntry();
                }
            }
        }

        zipOutputStream.close();
        jarFile.close();
    }

    private byte[] toByteArray(InputStream inputStream, int size) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        byte[] buffer = new byte[size];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, length);
        }

        return byteArrayOutputStream.toByteArray();
    }

}
