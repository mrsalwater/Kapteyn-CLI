package com.mrsalwater.kapteyn.cli.decompiler;

import com.mrsalwater.kapteyn.decompiler.bytecode.settings.ByteCodeSettings;

import java.io.File;

public final class DecompilerSettings {

    private final DecompilerType type;

    private final File inputFile;
    private final File outputFile;

    private final ByteCodeSettings settings;

    public DecompilerSettings(DecompilerType type, File inputFile, File outputFile, ByteCodeSettings settings) {
        this.type = type;
        this.inputFile = inputFile;
        this.outputFile = outputFile;
        this.settings = settings;
    }

    public DecompilerType getType() {
        return type;
    }

    public File getInputFile() {
        return inputFile;
    }

    public File getOutputFile() {
        return outputFile;
    }

    public ByteCodeSettings getSettings() {
        return settings;
    }

}
