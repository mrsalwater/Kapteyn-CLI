package com.mrsalwater.kapteyn.cli.decompiler;

import com.mrsalwater.kapteyn.decompiler.exception.ClassFileException;

import java.io.IOException;

public interface Decompiler {

    void decompile(DecompilerSettings settings) throws IOException, ClassFileException;

}
