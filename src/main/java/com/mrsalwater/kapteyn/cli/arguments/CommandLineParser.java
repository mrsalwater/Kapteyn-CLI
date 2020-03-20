package com.mrsalwater.kapteyn.cli.arguments;

import com.mrsalwater.kapteyn.cli.decompiler.DecompilerSettings;
import com.mrsalwater.kapteyn.cli.decompiler.DecompilerType;
import com.mrsalwater.kapteyn.decompiler.bytecode.settings.ByteCodeSetting;
import com.mrsalwater.kapteyn.decompiler.bytecode.settings.ByteCodeSettings;

import java.io.File;
import java.io.IOException;

public final class CommandLineParser {

    private CommandLineParser() {

    }

    public static DecompilerSettings parseCommandLine(String[] args) {
        if (args.length < 4 || args.length % 2 != 0 || !args[0].equals("-input") || !args[2].equals("-output")) {
            printUsage();
        }

        File inputFile = getInputFile(args[1]);
        DecompilerType decompilerType = getDecompilerType(inputFile);
        File outputFile = getOutputFile(args[3], decompilerType);
        ByteCodeSettings settings = getByteCodeSettings(args);

        return new DecompilerSettings(decompilerType, inputFile, outputFile, settings);
    }

    private static void printUsage() {
        System.out.println("Usage: java -jar decompiler.jar -input <file> -output <file> [-option <id>=<true/false>]");
        System.out.println("Options:");

        StringBuilder builder = new StringBuilder();
        for (ByteCodeSetting setting : ByteCodeSetting.values()) {
            builder.append("    ").append(setting.getID());

            builder.append(" - ").append(setting.getName());
            if (setting.isDefaultValue()) {
                builder.append(" (default)");
            }

            System.out.println(builder.toString());
            builder.setLength(0);
        }

        System.exit(1);
    }

    private static File getInputFile(String path) {
        File inputFile = new File(path);

        if (!inputFile.exists() || !inputFile.isFile()) {
            System.out.println("Error: Invalid input file");
            System.exit(2);
        }

        return inputFile;
    }

    private static DecompilerType getDecompilerType(File inputFile) {
        DecompilerType decompilerType = DecompilerType.match(inputFile.getName());

        if (decompilerType == null) {
            System.out.println("Error: Input file has to be a \"class\" or \"jar\" file");
            System.exit(3);
        }

        return decompilerType;
    }

    private static File getOutputFile(String path, DecompilerType decompilerType) {
        if (decompilerType == DecompilerType.JAR && !path.endsWith(".zip")) {
            path = path.concat(".zip");
        } else if (decompilerType == DecompilerType.CLASS && !path.endsWith(".source")) {
            path = path.concat(".source");
        }

        return generateOutputFile(path);
    }

    private static File generateOutputFile(String path) {
        File outputFile = new File(path);

        if (!outputFile.exists()) {
            try {
                boolean result = outputFile.createNewFile();
                if (!result) {
                    System.out.println("Error: Can not create output file");
                }
            } catch (IOException e) {
                System.out.println("Error: Can not create output file");
                e.printStackTrace();
            }
        } else if (!outputFile.canWrite()) {
            System.out.println("Error: Can not write to output file");
            System.exit(4);
        }

        return outputFile;
    }

    private static ByteCodeSettings getByteCodeSettings(String[] args) {
        if (args.length > 4) {
            String[] options = new String[args.length - 4];
            System.arraycopy(args, 4, options, 0, args.length - 4);

            return generateByteCodeSettings(options);
        }

        return new ByteCodeSettings();
    }

    private static ByteCodeSettings generateByteCodeSettings(String[] options) {
        ByteCodeSettings settings = new ByteCodeSettings();

        for (int i = 0, length = options.length; i < length; i++) {
            if (!options[i++].equals("-option")) {
                printUsage();
            }

            String option = options[i];
            if (!option.contains("=")) {
                printUsage();
            }

            String[] strings = option.split("=");

            String id = strings[0];
            ByteCodeSetting setting = ByteCodeSetting.match(id);
            if (setting == null) {
                printUsage();
            }

            String value = strings[1];
            if (!value.equals("true") && !value.equals("false")) {
                printUsage();
            }

            settings.setSetting(setting, Boolean.parseBoolean(value));
        }

        return settings;
    }

}
