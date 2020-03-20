# Kapteyn-CLI

An application to run the [Kapteyn](https://github.com/mrsalwater/Kapteyn) bytecode decompiler from command line

## How to build

```
git clone https://github.com/mrsalwater/Kapteyn-CLI.git
cd Kapteyn-CLI
./gradlew build
```

## How to use

`java -jar kapteyn-cli.jar -input <file> -output <file> [-option <id>=<true/false>]`

-input \<file>: Java class file or Jar to be decompiled. Valid file extensions are _.class_ and _.jar_.  

-output \<file>: Destination directory where the output file should be saved.  

-option \<id>=<true/false>: Optional command-line option to be passed to the bytecode decompiler. None, one or more options can be included. See [Command-line options](https://github.com/mrsalwater/Kapteyn-CLI#command-line-options) for possible values.    

### Command-line options

The bytecode decompiler accepts multiple options to alter its behaviour.  
They are listed in the following format: _\<id> (<true/false> \[default value]): \[description]_

* s-ic (true): Show inner classes
* s-em (false): Show enclosing methods
* s-bm (false): Show bootstrap methods
* s-ia (true): Show invisible annotations

---

_This software is licensed under the [MIT License](https://opensource.org/licenses/MIT)_  
_Copyright 2020 mrsalwater_