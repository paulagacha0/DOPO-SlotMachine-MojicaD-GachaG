# Slot Machine - DOPO 2026-2

Project developed by Paula Gacha and Diego Mojica for Desarrollo Orientado por Objetos.

## Repository structure

- `slotMachine/`: BlueJ project with the Java source code.
- `docs/ciclo1_original/`: original Cycle 1 retrospective and Astah design, kept only as historical evidence.
- `docs/ciclo1_corregido/`: location for the corrected Cycle 1 design and retrospective.
- `docs/ciclo2/`: location for the Cycle 2 design, retrospective and acceptance-test description.

## Current status

The source code in `slotMachine/` contains the corrected Cycle 1 foundation:

- All team-created code uses English names and documentation.
- A new wheel is empty and has no default symbol.
- Symbol and wheel positions are validated.
- Duplicate or unsupported symbols are rejected.
- Deleting a symbol keeps indexes consistent.
- Invalid visible operations use `JOptionPane`.
- `ok()` reports the result of the last command.
- The machine changes its frame color when it reaches a jackpot.
- Unit tests are included in `SlotMachineC1Test` and run in invisible mode.

Cycle 2 extensions are intentionally not implemented yet.

## Open in BlueJ

1. Extract the downloaded repository folder.
2. Open BlueJ.
3. Select **Project > Open Project**.
4. Select the `slotMachine` folder.
5. Press **Compile**.
6. Right-click `SlotMachineC1Test` and select **Test All**.

## Git rule

Commit the individual source and document files. Do not place a `slotMachine.zip` file inside the repository.
