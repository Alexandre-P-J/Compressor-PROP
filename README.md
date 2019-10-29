# Compressor-PROP
Descripción

## Features (Primera entrega)
- [x] LZ78
- [x] Custom dictionary size for LZ78
- [ ] LZSS
- [ ] Custom dictionary size for LZSS
- [x] LZW
- [x] Custom dictionary size for LZW
- [ ] JPEG
- [ ] Diagrama de uso
- [ ] Diagrama de clases
- [ ] Documentación
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
`> ./gradlew run --args='ARGUMENTS HERE'`
For example you could run:
`> ./gradlew run --args='LZSS compress README.md OUT'`
#### On Windows:
`> gradlew.bat run --args='ARGUMENTS HERE'`
For example you could run:
`> gradlew.bat run --args='LZSS compress README.md OUT'`

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


## Normas de etiqueta
- No se hacen commits en el Master excepto merges desde la rama Development.
- No se hacen commits en el Development excepto merges desde las ramas Feature/*
- Las ramas donde se hacen commits deben llamarse Feature/NOMBRE y se especializan lo maximo posible, en el caso de que dos miembros o mas trabajen sobre una rama Feature se deben crear subramas para cada miembro.
- Para añadir una nueva funcionalidad se hara Merge desde una rama Feature a la rama Development, a ser posible esto debe realizarse avisando al resto de miembros y revisando que no rompe nada, precaución.
- Para actualizar el Master se hará merge desde la rama Development y se hará solo con previo aviso de todos los miembros y despues de verificar exhaustivamente.
- Se hace un commit extra por cada merge (git merge --no-ff NOMBRERAMA) y recordad eliminar las ramas despues del merge.
- No se ejecutaran operaciones destructivas que atenten contra cualquiera de las reglas anteriores directa o indirectamente.