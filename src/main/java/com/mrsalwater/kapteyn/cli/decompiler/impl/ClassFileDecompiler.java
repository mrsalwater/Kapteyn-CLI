package com.mrsalwater.kapteyn.cli.decompiler.impl;

import com.mrsalwater.kapteyn.cli.decompiler.Decompiler;
import com.mrsalwater.kapteyn.cli.decompiler.DecompilerSettings;
import com.mrsalwater.kapteyn.decompiler.bytecode.ByteCodeFile;
import com.mrsalwater.kapteyn.decompiler.bytecode.ByteCodeParser;
import com.mrsalwater.kapteyn.decompiler.classfile.ClassFile;
import com.mrsalwater.kapteyn.decompiler.classfile.ClassFileParser;
import com.mrsalwater.kapteyn.decompiler.exception.ClassFileException;

import java.io.IOException;
import java.nio.file.Files;

public final class ClassFileDecompiler implements Decompiler {

    @Override
    public void decompile(DecompilerSettings settings) throws IOException, ClassFileException {
        byte[] bytes = Files.readAllBytes(settings.getInputFile().toPath());

        ClassFileParser classFileParser = new ClassFileParser(bytes);
        ClassFile classFile = classFileParser.parse();

        ByteCodeParser byteCodeParser = new ByteCodeParser(classFile, settings.getSettings());
        ByteCodeFile byteCodeFile = byteCodeParser.parse();

        String source = byteCodeFile.getSource();
        Files.write(settings.getOutputFile().toPath(), source.getBytes());
    }

}
