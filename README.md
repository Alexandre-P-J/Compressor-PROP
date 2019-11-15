# Group Description
##### Ibars Cubel, Albert
##### Muñoz Busto, Isaac
##### Clemente Marín, Daniel
##### Pérez Josende, Alexandre

##### albert.ibars.cubel@est.fib.upc.edu
##### isaac.munoz.busto@est.fib.upc.edu
##### daniel.clemente.marin@est.fib.edu
##### alexandre.perez.josende@est.fib.upc.edu

## Road to first assignment
- [x] EXTRA: Custom dictionary size for LZ78
- [x] EXTRA: Custom dictionary size for LZW
- [x] JPEG using Huffman and not LZW/LZ78/LZSS
- [ ] EXTRA: Gradle, "Tota millora que, sense contravenir aquestes condicions, faciliti la lectura i comprensió del contingut del lliurament repercutirà positivament a la nota"
- [ ] Profe dijo: "1 driver por alg, 1 driver para estadisticas, 1 driver para el test unitario de una clase concreta" (no entiendo los dos primeros)
- [ ] "Diagrama de casos d’ús i breu descripció de cada cas d’ús (fet amb processador de textos)"
- [ ] "Diagrama estàtic complet del model conceptual de dades en versió disseny i breu descripció de cada classe (fet amb processador de textos)"
- [ ] "Relació de les classes implementades per cada membre del grup"
- [ ] "Breu descripció de les estructures de dades i algorismes utilitzats per a implementar les funcionalitats principals"
- [ ] "Codi de totes les classes del model"
- [ ] "Codi de les classes de domini associades a les funcionalitats principals de l’aplicació. Aquestes funcionalitat principals es detallaran a l’enunciat"
- [ ] (Esto creo que esta anticuado) "Executables que permetin provar totes les classes implementades, mitjançant stubs i drivers"
- [ ] "Tests que el grup ha fet servir per provar el seu projecte (a afegir als drivers i stubs: una cosa no exclou l’altre)"
- [ ] index.txt in every folder of the project (Estructura dels lliuraments en format digital, Normativa PG 3 apartado 2)
- [ ] Descripció dels jocs de proves (Normativa PG 4)
- [ ] (Donde sacar estos datos? creo que el canal de salida es para el archivo) Generació de les estadístiques de compressió (grau de compressió, velocitat de compressió, etc.)
- [ ] Documentation

## How-to's:
This will assume you dont have gradle, if you have gradle installed you can run the following commands replacing the executable part of the commands with the executable name installed in your system.

### Run
Use the .jar files under EXE/ if some .jar files are missing see the section below

### Re/Packaging the Project
This will regenerate all .jar files under EXE/ and regenerate the documentation under DOCS/
#### On Linux:
`> ./gradlew PackProject`
#### On Windows:
`> gradlew.bat PackProject`

### Running Tests:
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
