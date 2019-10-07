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

## Setup
WIP - Documentación

## Normas de etiqueta
- No se hacen commits en el Master excepto merges desde la rama Development.
- No se hacen commits en el Development excepto merges desde las ramas Feature/*
- Las ramas donde se hacen commits deben llamarse Feature/NOMBRE y se especializan lo maximo posible, en el caso de que dos miembros o mas trabajen sobre una rama Feature se deben crear subramas para cada miembro.
- Para añadir una nueva funcionalidad se hara Merge desde una rama Feature a la rama Development, a ser posible esto debe realizarse avisando al resto de miembros y revisando que no rompe nada, precaución.
- Para actualizar el Master se hará merge desde la rama Development y se hará solo con previo aviso de todos los miembros y despues de verificar exhaustivamente.
- Se hace un commit extra por cada merge (git merge --no-ff NOMBRERAMA) y recordad eliminar las ramas despues del merge.
- No se ejecutaran operaciones destructivas que atenten contra cualquiera de las reglas anteriores directa o indirectamente.