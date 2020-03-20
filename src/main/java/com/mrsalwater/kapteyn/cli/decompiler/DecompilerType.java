package com.mrsalwater.kapteyn.cli.decompiler;

public enum DecompilerType {

    CLASS, JAR;

    public static DecompilerType match(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();

        switch (extension) {
            case "class":
                return CLASS;
            case "jar":
                return JAR;
            default:
                return null;
        }
    }

}
