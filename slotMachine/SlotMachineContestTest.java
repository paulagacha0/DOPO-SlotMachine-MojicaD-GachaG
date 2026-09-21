import static org.junit.Assert.*;
import org.junit.Test;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

/** [P -> SC, SM] Comprueba constructor, restricciones y solución del concurso */
public class SlotMachineContestTest
{
    /** [P -> SM] El nuevo constructor crea igual cantidad de ruedas y símbolos */
    @Test
    public void shouldCreateThreeWheelsAndThreeSymbols()
    {
        SlotMachine machine = new SlotMachine(3);
        assertEquals(3, machine.symbols().length);
        assertEquals(3, machine.configuration().length);
    }

    /** [P -> SM] Todas las ruedas automáticas comienzan con un símbolo válido */
    @Test
    public void shouldAssignKnownSymbolsAtConstruction()
    {
        SlotMachine machine = new SlotMachine(50);
        HashSet<String> catalog = new HashSet<>();
        for (String color : machine.symbols()) {
            catalog.add(color);
        }
        for (String symbol : machine.configuration()) {
            assertNotNull(symbol);
            assertTrue(catalog.contains(symbol));
        }
        assertTrue(machine.ok());
    }

    /** [P -> SM] El problema exige comenzar sin jackpot */
    @Test
    public void shouldAvoidAnInitialJackpot()
    {
        for (int n = 3; n <= 50; n++) {
            assertTrue(new SlotMachine(n).distinctSymbols() > 1);
        }
    }

    /** [P -> COL] Cincuenta símbolos representan cincuenta colores reales diferentes */
    @Test
    public void shouldCreateFiftyDifferentVisualColors()
    {
        HashSet<Integer> colors = new HashSet<>();
        for (String color : new SlotMachine(50).symbols()) {
            assertTrue(SymbolColors.isSymbol(color));
            colors.add(SymbolColors.forDrawing(color).getRGB());
        }
        assertEquals(50, colors.size());
        assertFalse(colors.contains(Color.WHITE.getRGB()));
    }

    /** [P -> SM] Mantiene la construcción manual con ruedas vacías */
    @Test
    public void shouldKeepManuallyAddedWheelsEmpty()
    {
        SlotMachine machine = new SlotMachine();
        machine.makeInvisible();
        machine.addSymbol(1, "red");
        machine.addWheel(1);
        assertNull(machine.configuration()[0]);
    }

    /** [P -> SM] Rechaza tamaños menores que el mínimo de la maratón */
    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectATooSmallMachine()
    {
        new SlotMachine(2);
    }

    /** [P -> SM] Rechaza tamaños mayores que el máximo de la maratón */
    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectATooLargeMachine()
    {
        new SlotMachine(51);
    }

    /** [P -> SC] solve también valida el tamaño antes de crear la máquina */
    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectInvalidSolveSize()
    {
        new SlotMachineContest().solve(0);
    }

    /** [P -> SC] simulate valida el tamaño sin abrir una ventana inválida */
    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectInvalidSimulationSize()
    {
        new SlotMachineContest().simulate(51);
    }

    /** [P -> COL, SM] Admite hexadecimales y no permite duplicar red con su alias */
    @Test
    public void shouldNormalizeHexadecimalSymbolsAndRejectAliases()
    {
        SlotMachine machine = new SlotMachine();
        machine.makeInvisible();
        machine.addSymbol(1, " #19AbCf ");
        assertArrayEquals(new String[] {"#19abcf"}, machine.symbols());
        machine.addSymbol(2, "red");
        machine.addSymbol(3, "#FF0000");
        assertFalse(machine.ok());
        assertEquals(2, machine.symbols().length);
    }

    /** [P -> COL] El blanco sigue reservado para mostrar una rueda vacía */
    @Test
    public void shouldRejectBlankAndMalformedColors()
    {
        for (String invalid : new String[] {null, "", "white", "#ffffff", "#12xz89", "#fff"}) {
            assertFalse(SymbolColors.isSymbol(invalid));
        }
    }

    /** [P -> COL] Canvas recibe el RGB correcto para un hexadecimal válido */
    @Test
    public void shouldConvertHexadecimalColorsForCanvas()
    {
        assertEquals(new Color(0x19abcf), SymbolColors.forDrawing("#19abcf"));
        assertEquals(Color.WHITE, SymbolColors.forDrawing("white"));
    }

    /** [P -> SC] Resuelve el tamaño mínimo con todos los símbolos diferentes */
    @Test
    public void shouldSolveThreeDifferentSymbols()
    {
        checkSolution(new int[] {0, 1, 2});
    }

    /** [P -> SC] Resuelve una configuración con símbolos repetidos */
    @Test
    public void shouldSolveRepeatedSymbols()
    {
        checkSolution(new int[] {0, 0, 1, 1, 2});
    }

    /** [P -> SC] Acepta como caso defensivo una máquina que ya ganó */
    @Test
    public void shouldReturnNoActionsForAnExistingJackpot()
    {
        assertEquals(0, checkSolution(new int[] {2, 2, 2}).length);
    }

    /** [P -> SC] Se detiene inmediatamente si encuentra jackpot durante una prueba */
    @Test
    public void shouldStopAsSoonAsAProbeWins()
    {
        int[][] actions = checkSolution(new int[] {0, 1, 1});
        assertEquals(1, actions.length);
        assertArrayEquals(new int[] {1, 1}, actions[0]);
    }

    /** [P -> SC] El resultado reproduce todas las acciones sobre el estado inicial */
    @Test
    public void shouldReturnAnOrderedReplayableTrace()
    {
        checkSolution(new int[] {0, 1, 2, 3, 4, 5});
    }

    /** [P -> SC] La máquina observada prohíbe leer colores, configuración, jackpot y ok */
    @Test
    public void shouldUseOnlyAllowedInformation()
    {
        checkSolution(new int[] {3, 1, 3, 0, 4, 1});
    }

    /** [P -> SC] solve solicita modo invisible durante todas sus acciones */
    @Test
    public void shouldKeepSolveInvisible()
    {
        ObservedMachine machine = new ObservedMachine(new int[] {0, 1, 2, 3});
        new FixedContest(machine).solve(4);
        assertTrue(machine.invisibleRequested);
        assertFalse(machine.visibleRequested);
        assertFalse(machine.anyVisibleMove);
    }

    /** [P -> SC] Comprueba la orden de visibilidadel doble no abre una ventana */
    @Test
    public void shouldRequestVisibilityBeforeSimulationActions()
    {
        ObservedMachine machine = new ObservedMachine(new int[] {0, 1, 2, 3});
        new FixedContest(machine).simulate(4);
        assertTrue(machine.visibleRequested);
        assertFalse(machine.anyInvisibleMove);
        assertEquals(1, machine.finalDistinctCount());
    }

    /** [P -> SC] Con el mismo estado inicial solve y simulate siguen la misma estrategia */
    @Test
    public void shouldUseTheSameAlgorithmInBothModes()
    {
        int[] initial = {0, 0, 2, 3, 1};
        ObservedMachine hidden = new ObservedMachine(initial);
        ObservedMachine visible = new ObservedMachine(initial);
        int[][] actions = new FixedContest(hidden).solve(initial.length);
        new FixedContest(visible).simulate(initial.length);
        assertEquals(actions.length, visible.recorded.size());
        for (int i = 0; i < actions.length; i++) {
            assertArrayEquals(actions[i], visible.recorded.get(i));
        }
    }

    /** [P -> SC] Comprueba exhaustivamente todos los estados de 3, 4 y 5 ruedas */
    @Test
    public void shouldSolveEverySmallConfiguration()
    {
        for (int n = 3; n <= 5; n++) {
            int combinations = (int) Math.pow(n, n);
            for (int encoded = 0; encoded < combinations; encoded++) {
                int[] initial = new int[n];
                int value = encoded;
                for (int i = 0; i < n; i++) {
                    initial[i] = value % n;
                    value /= n;
                }
                checkSolution(initial);
            }
        }
    }

    /** [P -> SC] Recorre todos los tamaños grandes con datos reproducibles */
    @Test
    public void shouldSolveSeededConfigurationsUpToFifty()
    {
        Random random = new Random(2026);
        for (int n = 6; n <= 50; n++) {
            for (int sample = 0; sample < 5; sample++) {
                int[] initial = new int[n];
                for (int i = 0; i < n; i++) {
                    initial[i] = random.nextInt(n);
                }
                checkSolution(initial);
            }
        }
    }

    /** [P -> SC] Resuelve 50 ruedas respetando también la cota de 5001 acciones */
    @Test
    public void shouldRespectTheActionLimitForFiftyWheels()
    {
        int[] initial = new int[50];
        for (int i = 0; i < initial.length; i++) {
            initial[i] = i;
        }
        int[][] actions = checkSolution(initial);
        assertTrue(actions.length <= 5001);
    }

    /** [P -> SC] Integra el constructor real y el algoritmo, sin dobles*/
    @Test
    public void shouldSolveARealRandomMachine()
    {
        int[][] actions = new SlotMachineContest().solve(50);
        assertTrue(actions.length > 0);
        assertTrue(actions.length <= 5001);
    }

    /** [P -> SC] Cada invocación inicia su propio registro y máquina */
    @Test
    public void shouldResetTheRunBetweenCalls()
    {
        SlotMachineContest contest = new SlotMachineContest();
        assertTrue(contest.solve(3).length <= 19);
        assertTrue(contest.solve(4).length <= 33);
    }

    /** [P -> SC] Oculta la máquina anterior al comenzar otra simulación*/
    @Test
    public void shouldHidePreviousMachineBeforeANewRun()
    {
        ObservedMachine machine = new ObservedMachine(new int[] {0, 1, 2, 3});
        SlotMachineContest contest = new FixedContest(machine);
        contest.simulate(4);
        assertFalse(machine.invisibleRequested);
        contest.simulate(4);
        assertTrue(machine.invisibleRequested);
        assertTrue(machine.visibleRequested);
    }

    /**
     * [P] Reproduce el resultado de forma independiente y comprueba el jackpot
     */
    public static int[][] checkSolution(int[] initial)
    {
        ObservedMachine machine = new ObservedMachine(initial);
        int[][] actions = new FixedContest(machine).solve(initial.length);
        assertEquals(1, machine.finalDistinctCount());
        assertTrue(actions.length <= 2 * initial.length * initial.length + 1);
        assertTrue(actions.length <= 10000);
        assertEquals(machine.recorded.size(), actions.length);
        int[] replay = initial.clone();
        for (int i = 0; i < actions.length; i++) {
            assertEquals(2, actions[i].length);
            assertArrayEquals(machine.recorded.get(i), actions[i]);
            int wheel = actions[i][0];
            int steps = actions[i][1];
            assertTrue(wheel >= 1 && wheel <= initial.length);
            assertTrue(steps >= -1000000000 && steps <= 1000000000);
            replay[wheel - 1] = Math.floorMod(replay[wheel - 1] + steps, initial.length);
        }
        for (int index : replay) {
            assertEquals(replay[0], index);
        }
        return actions;
    }

    /** [P] Sustituye solo la creación,el algoritmo de producción no cambia */
    private static class FixedContest extends SlotMachineContest
    {
        private final ObservedMachine preparedMachine;

        /** [P] Conserva el escenario que se quiere comprobar*/
        FixedContest(ObservedMachine preparedMachine)
        {
            this.preparedMachine = preparedMachine;
        }

        /** [P] Entrega la máquina preparada al método run */
        @Override
        protected SlotMachine createMachine(int n)
        {
            assertEquals(preparedMachine.size, n);
            return preparedMachine;
        }
    }

    /** [P] Observa el protocolo sin mostrar gráficos ni revelar símbolos al algoritmo*/
    private static class ObservedMachine extends SlotMachine
    {
        private final int size;
        private final ArrayList<int[]> recorded = new ArrayList<>();
        private boolean tracking;
        private boolean visibleRequested;
        private boolean invisibleRequested;
        private boolean anyVisibleMove;
        private boolean anyInvisibleMove;

        /** [P -> SM] Usa el simulador real y fija sus símbolos antes de observarlo */
        ObservedMachine(int[] initial)
        {
            super(initial.length);
            size = initial.length;
            String[] catalog = super.symbols();
            for (int i = 0; i < size; i++) {
                super.placeSymbol(i + 1, catalog[initial[i]]);
            }
            tracking = true;
        }

        /** [P] Registra la solicitud visible, la aceptación comprueba la ventana real */
        @Override
        public void makeVisible()
        {
            visibleRequested = true;
        }

        /** [P -> SM] Registra la solicitud invisible y conserva el simulador oculto */
        @Override
        public void makeInvisible()
        {
            invisibleRequested = true;
            visibleRequested = false;
            super.makeInvisible();
        }

        /** [P -> SM] Comprueba y registra cada acción antes de ejecutar el giro real */
        @Override
        public void spin(int wheel, int steps)
        {
            assertTrue("No actions are allowed after jackpot", distinctSymbols() > 1);
            recorded.add(new int[] {wheel, steps});
            anyVisibleMove |= visibleRequested;
            anyInvisibleMove |= !visibleRequested;
            super.spin(wheel, steps);
            assertTrue(super.ok());
        }

        /** [P] Consulta final exclusiva de las pruebas, el solucionador no la conoce */
        int finalDistinctCount()
        {
            return distinctSymbols();
        }

        /** [P -> SM] Permite las consultas internas del simulador durante el conteo autorizado */
        @Override
        public int distinctSymbols()
        {
            boolean wasTracking = tracking;
            tracking = false;
            try {
                return super.distinctSymbols();
            } finally {
                tracking = wasTracking;
            }
        }

        /** [P] Impide que el solucionador lea la configuración oculta */
        @Override
        public String[] configuration()
        {
            assertFalse("The solver must not read configuration", tracking);
            return super.configuration();
        }

        /** [P] Impide que el solucionador lea el catálogo */
        @Override
        public String[] symbols()
        {
            assertFalse("The solver must not read symbols", tracking);
            return super.symbols();
        }

        /** [P] El algoritmo debe detectar el triunfo por distinctSymbols */
        @Override
        public boolean isJackpot()
        {
            assertFalse("The solver must not call isJackpot", tracking);
            return super.isJackpot();
        }

        /** [P] El solucionador tampoco dispone de ok como información extra */
        @Override
        public boolean ok()
        {
            assertFalse("The solver must not call ok", tracking);
            return super.ok();
        }
    }
}
