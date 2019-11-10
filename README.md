# Group Description
Ibars Cubel, Albert
Muñoz Busto, Isaac
Clemente Marín, Daniel
Pérez Josende, Alexandre

albert.ibars.cubel@est.fib.upc.edu
isaac.munoz.busto@est.fib.upc.edu
daniel.clemente.marin@est.fib.edu
alexandre.perez.josende@est.fib.upc.edu

## Road to first assignment
- [x] EXTRA: Custom dictionary size for LZ78
- [ ] EXTRA: Custom window size for LZSS
- [x] EXTRA: Custom dictionary size for LZW
- [x] JPEG using Huffman and not LZW/LZ78/LZSS
- [ ] EXTRA: Gradle, "Tota millora que, sense contravenir aquestes condicions, faciliti la lectura i comprensió del contingut del lliurament repercutirà positivament a la nota"
- [ ] "Diagrama de casos d’ús i breu descripció de cada cas d’ús (fet amb processador de textos)"
- [ ] "Diagrama estàtic complet del model conceptual de dades en versió disseny i breu descripció de cada classe (fet amb processador de textos)"
- [ ] "Relació de les classes implementades per cada membre del grup"
- [ ] "Breu descripció de les estructures de dades i algorismes utilitzats per a implementar les funcionalitats principals"
- [ ] "Codi de totes les classes del model"
- [ ] "Codi de les classes de domini associades a les funcionalitats principals de l’aplicació. Aquestes funcionalitat principals es detallaran a l’enunciat"
- [ ] "Executables que permetin provar totes les classes implementades, mitjançant stubs i drivers"
- [ ] "Tests que el grup ha fet servir per provar el seu projecte (a afegir als drivers i stubs: una cosa no exclou l’altre)"
- [ ] Documentation
...

## How to: Compile/Run/Test/Clean
This will assume you dont have gradle, if you have gradle installed you can run the following commands replacing the executable part of the commands with the executable name installed in your system.

### Compile
This will get all needed dependencies from the internet and build.
#### On Linux:
`> ./gradlew build`
#### On Windows:
`> gradlew.bat build`

### Run
This will get all needed dependencies from the internet , build if needed and run the application.
#### On Linux:
`> ./gradlew run --args="ARGUMENTS HERE"`
For example you could run:
`> ./gradlew run --args="LZSS compress README.md OUT"`
#### On Windows:
`> gradlew.bat run --args="ARGUMENTS HERE"`
For example you could run:
`> gradlew.bat run --args="LZSS compress README.md OUT"`

### Running Tests:
This will get all needed dependencies from the internet , build if needed and run the tests.
#### On Linux:
`> ./gradlew test`
#### On Windows:
`> gradlew.bat test`

### Clean
Its not really needed but its useful to know.
#### On Linux:
`> ./gradlew clean`
#### On Windows:
`> gradlew.bat clean`
