# File archiver with compression
Simple yet powerful file archiver with simple and modern UI.
## Features
- [x] File and folder smart compression/decompression
- [x] JPEG compression algorithm
- [x] LZ78 compression algorithm
- [x] LZSS compression algorithm
- [x] LZW compression algorithm
- [x] Random compressed file access and decompression
- [x] Fully parametrized compression (Dictionary size, JPEG quality...)
- [x] Compressed image visualization
- [x] Unit Testing
- [x] Custom Gradle system
- [x] In-app manual supporting HTML
- [x] Total and per file statistics
- [x] Simple and powerful UI design

## How-to's:
This will assume you dont have gradle, if you have gradle 6.0 installed you can run the following commands replacing the executable part of the commands with the executable name installed in your system.

### Run
Use the .jar files under EXE/ if some .jar files are missing or recompilation its needed, see the section below

### Re/Packaging the Project
This will regenerate all .jar files under EXE/ and regenerate the documentation under DOCS/
#### On Linux:
`> ./gradlew PackProject`
#### On Windows:
`> gradlew.bat PackProject`

### Update and Run Tests:
Tests can be found under EXE/ but its useful to recompile and run tests on the go. This will also update test .jar's
#### Unit Test:
##### On Linux:
`> ./gradlew UnitTest`
##### On Windows:
`> gradlew.bat UnitTest`
#### Extra Tests
##### On Linux:
`> ./gradlew ExtraTests`
##### On Windows:
`> gradlew.bat ExtraTests`

### Update and Run Main Program:
Main program can be found under EXE/ but its useful to recompile and run on the go. This will also update the .jar file
#### On Linux:
`> ./gradlew Main`
#### On Windows:
`> gradlew.bat Main`

### Re/Generating Documentation Only:
Documentation can be found under DOCS/Documentation but its useful to regenerate it on the go
#### On Linux:
`> ./gradlew javadoc`
#### On Windows:
`> gradlew.bat javadoc`

### Clean
Its not really needed but its useful to know.
#### On Linux:
`> ./gradlew clean`
#### On Windows:
`> gradlew.bat clean`
