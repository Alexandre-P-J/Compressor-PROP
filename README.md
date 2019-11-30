# Group Description
##### Ibars Cubel, Albert
##### Muñoz Busto, Isaac
##### Clemente Marín, Daniel
##### Pérez Josende, Alexandre

##### albert.ibars.cubel@est.fib.upc.edu
##### isaac.munoz.busto@est.fib.upc.edu
##### daniel.clemente.marin@est.fib.upc.edu
##### alexandre.perez.josende@est.fib.upc.edu

## Road to second assignment
- [x] DomainController basico para hacer un placeholder de la ui decente
- [ ] Serializado y Decodificacion del filetree
- [ ] Acceso aleatorio usando skip(long)
- [ ] Separar Domain de Persistance y refactor
- [ ] UI placeholder
- [ ] UI Features
- [ ] Actualizar diagramas, manual, etc

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
