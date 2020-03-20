package com.mrsalwater.kapteyn.cli;

import com.mrsalwater.kapteyn.cli.arguments.CommandLineParser;
import com.mrsalwater.kapteyn.cli.decompiler.Decompiler;
import com.mrsalwater.kapteyn.cli.decompiler.DecompilerSettings;
import com.mrsalwater.kapteyn.cli.decompiler.impl.ClassFileDecompiler;
import com.mrsalwater.kapteyn.cli.decompiler.impl.JarFileDecompiler;
import com.mrsalwater.kapteyn.decompiler.exception.ClassFileException;

import java.io.IOException;

public final class Main {

    private Main() {

    }

    public static void main(String[] args) {
        DecompilerSettings settings = CommandLineParser.parseCommandLine(args);

        long start = System.nanoTime();
        System.out.println("Decompiling input file...");

        try {
            decompile(settings);
        } catch (IOException | ClassFileException e) {
            System.out.println("Failed to decompile file:");
            e.printStackTrace();
        } finally {
            long end = System.nanoTime();
            double delta = (end - start) * 1e-9;
            double time = Math.floor(delta * 10000) / 10000.0;
            System.out.println("Decompiled file in " + time + " seconds");
        }
    }

    private static void decompile(DecompilerSettings settings) throws IOException, ClassFileException {
        Decompiler decompiler;
        switch (settings.getType()) {
            case CLASS:
                decompiler = new ClassFileDecompiler();
                break;
            case JAR:
                decompiler = new JarFileDecompiler();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + settings.getType());
        }

        decompiler.decompile(settings);
    }

}
