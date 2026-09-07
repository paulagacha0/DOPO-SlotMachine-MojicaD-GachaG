# Integración del Ciclo 2 en GitHub

Esta guía supone que el repositorio nuevo ya contiene la base corregida del Ciclo 1 en la rama `main`.

## Paula: publicar la implementación completa

1. Abre GitHub Desktop y selecciona el repositorio del proyecto.
2. Verifica que la rama actual sea `main` y pulsa **Fetch origin**; si aparece **Pull origin**, púlsalo.
3. Crea la rama `paula/cycle-2-complete` desde `main`.
4. Extrae el paquete recibido fuera de la carpeta del repositorio.
5. Copia **el contenido interno** de `SlotMachine_Cycle2_Complete` dentro de la carpeta local del repositorio. No copies la carpeta exterior como una subcarpeta y no borres la carpeta oculta `.git`.
6. Abre `docs/ciclo2/GachaG-MojicaD.txt` y sustituye el marcador por la URL real del repositorio.
7. Abre `docs/ciclo2/Retrospectiva_Ciclos_1_y_2.docx` y completa únicamente las horas reales del Ciclo 2.
8. Abre `slotMachine/package.bluej` en BlueJ, compila y ejecuta **Test All** en `SlotMachineC1Test`, `SlotMachineC2Test` y `SlotMachineCC2Test`.
9. En GitHub Desktop confirma que no aparezca ningún archivo ZIP ni archivos compilados `.class`.
10. Realiza commits pequeños:
    - `feat: complete Cycle 2 simulator`
    - `test: add Cycle 2 automated tests`
    - `docs: add Cycle 2 acceptance and retrospective`
11. Pulsa **Push origin**.
12. Crea un Pull Request desde `paula/cycle-2-complete` hacia `main`, revísalo y fusiónalo cuando las pruebas estén correctas.

## Diego: descargar y añadir Astah

1. Acepta la invitación de colaboración al repositorio, si todavía está pendiente.
2. En GitHub Desktop elige **File > Clone repository** y selecciona el repositorio del equipo.
3. Verifica que la rama `main` incluya el Ciclo 2 de Paula; pulsa **Fetch origin** y luego **Pull origin** si aparece.
4. Crea la rama `diego/cycle-2-astah` desde `main`.
5. Lee `docs/ciclo2/ASTAH_Handoff_Diego.md` y usa `docs/ciclo1_original/proyecto_ciclo1_original.asta` solamente como punto de partida histórico.
6. Actualiza completamente el diagrama de clases y los diagramas de secuencia para que coincidan con el Java final.
7. Guarda el resultado como `docs/ciclo2/proyecto_ciclo2.asta`.
8. Haz el commit `docs: add synchronized Cycle 2 Astah design` y pulsa **Push origin**.
9. Crea un Pull Request desde `diego/cycle-2-astah` hacia `main`.

## Revisión final del equipo

- Confirmar que `main` contiene el archivo `docs/ciclo2/proyecto_ciclo2.asta` creado por Diego.
- Comparar en Astah todos los nombres, parámetros y tipos de retorno contra `SlotMachine.java` y `Wheel.java`.
- Confirmar o publicar en el wiki las dos pruebas de `SlotMachineCC2Test` con las iniciales acordadas.
- Ejecutar las 32 pruebas y las dos pruebas de aceptación.
- Entregar en Moodle el archivo `GachaG-MojicaD.txt`, ya actualizado con la URL del repositorio.

El ZIP se utiliza únicamente para transferir la versión preparada. En GitHub deben aparecer las carpetas y archivos individuales, nunca el ZIP.
