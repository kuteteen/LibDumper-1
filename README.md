# LibDumper
This Project Is Using For Make You Easy Dump Lib From The Memory

# Changelog 3.5 :
- fixing corrupt file after fixing elf
- format result dump [startAddress-fileDump]
- fixing some bugs

# Features
- support for dumping global-metadata.dat from memory
- fast read & writing file
- support for root 
- support non root [use virtual space]
- support 32bit and 64bit game

# Usage :
- run the app game 
- run [app-debug.apk](https://github.com/BryanGIG/LibDumper/releases)
- put the package name from the game
- put the lib name (default name is "libil2cpp.so")
- check the dump metadata if you want to dump the metadata from memory
- press button "Dump"
- wait progress done
- the result dumped file will be in /sdcard/Download/

# Credit :
- [libsu](https://github.com/topjohnwu/libsu)
- [elf-dump-fix](https://github.com/maiyao1988/elf-dump-fix)