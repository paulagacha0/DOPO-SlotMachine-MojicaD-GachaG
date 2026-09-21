import java.util.ArrayList;

/**
 * [SC] Encuentra un jackpot usando solo giros y el número de símbolos distintos
 */
public class SlotMachineContest
{
    private static final int MAX_ACTIONS = 10000;
    // [SC -> SM] Máquina usada en la ejecución actual
    private SlotMachine machine;
    private ArrayList<int[]> actions;
    // [SC] Única información que recibe sobre los símbolos visibles
    private int distinctCount;

    /**
     * [SC -> SM] Resuelve una máquina aleatoria sin mostrarla
     */
    public int[][] solve(int n)
    {
        return run(n, false);
    }

    /**
     * [SC -> SM] Ejecuta el mismo algoritmo sobre una nueva máquina visible
     */
    public void simulate(int n)
    {
        run(n, true);
    }

    /**
     * [SC -> SM] Separa la creación para poder sustituir la máquina en las pruebas
     */
    protected SlotMachine createMachine(int n)
    {
        return new SlotMachine(n);
    }

    /**
     * [SC] Prepara una ejecución y devuelve su registro de acciones
     */
    private int[][] run(int n, boolean visible)
    {
        if (n < 3 || n > 50) {
            throw new IllegalArgumentException("The size must be between 3 and 50.");
        }
        if (machine != null) {
            machine.makeInvisible();
        }
        machine = createMachine(n);
        actions = new ArrayList<>();
        if (visible) {
            machine.makeVisible();
        } else {
            machine.makeInvisible();
        }
        distinctCount = machine.distinctSymbols();
        if (distinctCount != 1) {
            findJackpot(n);
        }
        return actions.toArray(new int[actions.size()][]);
    }

    /**
     * [SC] Crea un color ausente, identifica sus desplazamientos y reúne las ruedas
     */
    private void findJackpot(int n)
    {
        separateWheels(n);
        if (distinctCount == 1) {
            return;
        }
        // [SC -> SM] Con todos distintos, elgiro deja exactamente un color ausente
        move(1, 1);
        if (distinctCount == 1) {
            return;
        }
        int[] offsets = new int[n];
        offsets[0] = -1;
        for (int wheel = 2; wheel <= n; wheel++) {
            offsets[wheel - 1] = findMissingColorOffset(wheel, n);
            if (distinctCount == 1) {
                return;
            }
        }
        for (int wheel = 1; wheel <= n; wheel++) {
            move(wheel, offsets[wheel - 1]);
            if (distinctCount == 1) {
                return;
            }
        }
        throw new IllegalStateException("The machine did not reach a jackpot.");
    }

    /**
     * [SC] Conserva los giros que aumentan los distintos y restaura los demás
     */
    private void separateWheels(int n)
    {
        for (int wheel = 1; wheel <= n && distinctCount < n; wheel++) {
            int previousCount = distinctCount;
            for (int step = 1; step < n; step++) {
                move(wheel, 1);
                if (distinctCount == 1 || distinctCount > previousCount) {
                    break;
                }
            }
            if (distinctCount == 1) {
                return;
            }
            if (distinctCount <= previousCount) {
                // [SC] despuesd n-1 pasos uno más devuelve la rueda a su posición inicial
                move(wheel, 1);
                if (distinctCount == 1) {
                    return;
                }
            }
        }
    }

    /**
     * [SC] Prueba las otras posiciones, conserva la de mayor conteo y restaura
     * int1 wheel posición externa de la rueda
     * int2 cantidad de símbolos
     * return pasos hasta el color ausente y 0 si encontró jackpot
     */
    private int findMissingColorOffset(int wheel, int n)
    {
        int bestCount = -1;
        int bestOffset = 0;
        for (int step = 1; step < n; step++) {
            move(wheel, 1);
            if (distinctCount == 1) {
                return 0;
            }
            if (distinctCount > bestCount) {
                bestCount = distinctCount;
                bestOffset = step;
            }
        }
        move(wheel, 1);
        return bestOffset;
    }

    /**
     * [SC -> SM] Ejecuta y registra un giro consulta el número de distintos
     */
    private void move(int wheel, int steps)
    {
        if (distinctCount == 1 || actions.size() >= MAX_ACTIONS) {
            throw new IllegalStateException("No further contest actions are allowed.");
        }
        machine.spin(wheel, steps);
        actions.add(new int[] {wheel, steps});
        distinctCount = machine.distinctSymbols();
    }
}
