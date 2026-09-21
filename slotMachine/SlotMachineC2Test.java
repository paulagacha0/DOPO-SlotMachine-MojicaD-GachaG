import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 * [P] Agrupa veinte pruebas de intercambio bloqueo y giros contiene cuatro ayudantes privados
 * de preparación
 */
public class SlotMachineC2Test
{
    /**
     * [P] Referencia al simulador bajo prueba, setUp crea uno nuevo antes de cada caso
     */
    private SlotMachine machine;

    /**
     * [P] Prepara una máquina nueva e invisible antes de cada caso de prueba
     */
    @Before
    public void setUp()
    {
        machine = new SlotMachine();
        machine.makeInvisible();
    }

    /**
     * [P] Comprueba el intercambio de dos ruedas
     * ->JUnit
     */
    @Test
    public void shouldSwapTwoWheels()
    {
        prepareTwoConfiguredWheels();

        machine.swap(1, 2);

        assertArrayEquals(
            new String[] {"blue", "red"},
            machine.configuration()
        );
        assertTrue(machine.ok());
    }

    /**
     * [P] Comprueba que no haya intercambio parcial con una posición inválida
     * ->JUnit
     */
    @Test
    public void shouldNotSwapWhenAWheelPositionIsInvalid()
    {
        prepareTwoConfiguredWheels();

        machine.swap(1, 3);

        assertArrayEquals(
            new String[] {"red", "blue"},
            machine.configuration()
        );
        assertFalse(machine.ok());
    }

    /**
     * [P] Comprueba que un giro directo respete el bloqueo
     * Conecta con JUnit
     */
    @Test
    public void shouldKeepALockedWheelStillDuringDirectSpin()
    {
        prepareOneConfiguredWheel();
        machine.lock(1);

        machine.spin(1);

        assertArrayEquals(
            new String[] {"red"},
            machine.configuration()
        );
        assertFalse(machine.ok());
    }

    /**
     * [P] Comprueba que desbloquear permita un giro posterior
     * ->JUnit
     */
    @Test
    public void shouldUnlockAWheelAndAllowItToSpinAgain()
    {
        prepareOneConfiguredWheel();
        machine.lock(1);

        machine.unlock(1);
        machine.spin(1);

        assertArrayEquals(
            new String[] {"blue"},
            machine.configuration()
        );
        assertTrue(machine.ok());
    }

    /**
     * [P] Comprueba que el giro general omita las bloqueadas
     * ->JUnit
     */
    @Test
    public void shouldSpinOnlyUnlockedWheels()
    {
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addWheel(1);
        machine.addWheel(2);
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "red");
        machine.lock(1);

        machine.spin();

        assertArrayEquals(
            new String[] {"red", "blue"},
            machine.configuration()
        );
        assertTrue(machine.ok());
    }

    /**
     * [P] Comprueba que el bloqueo viaje con el objeto intercambiado
     * ->JUnit
     */
    @Test
    public void shouldKeepTheLockStateWithTheSwappedWheel()
    {
        prepareTwoConfiguredWheels();
        machine.lock(1);

        machine.swap(1, 2);
        machine.spin(2);

        assertArrayEquals(
            new String[] {"blue", "red"},
            machine.configuration()
        );
        assertFalse(machine.ok());
    }

    /**
     * [P] Comprueba el rechazo de bloquear o desbloquear dos veces
     * ->JUnit
     */
    @Test
    public void shouldRejectRepeatedLockAndUnlockCommands()
    {
        prepareOneConfiguredWheel();

        machine.lock(1);
        assertTrue(machine.ok());
        machine.lock(1);
        assertFalse(machine.ok());

        machine.unlock(1);
        assertTrue(machine.ok());
        machine.unlock(1);
        assertFalse(machine.ok());
    }

    /**
     * [P] Comprueba posiciones que no corresponden a ruedas
     * ->JUnit
     */
    @Test
    public void shouldRejectInvalidLockAndUnlockPositions()
    {
        machine.addWheel(1);

        machine.lock(0);
        assertFalse(machine.ok());

        machine.unlock(2);
        assertFalse(machine.ok());
    }

    /**
     * [P] Comprueba un giro que da más de una vuelta al catálogo
     * ->JUnit
     */
    @Test
    public void shouldRotateAWheelSeveralPositiveSteps()
    {
        prepareThreeSymbolsAndOneWheel();

        machine.spin(1, 5);

        assertArrayEquals(
            new String[] {"green"},
            machine.configuration()
        );
        assertTrue(machine.ok());
    }

    /**
     * [P] Comprueba un paso negativo desde el inicio del catálogo
     * ->JUnit
     */
    @Test
    public void shouldRotateAWheelBackwards()
    {
        prepareThreeSymbolsAndOneWheel();

        machine.spin(1, -1);

        assertArrayEquals(
            new String[] {"green"},
            machine.configuration()
        );
        assertTrue(machine.ok());
    }

    /**
     * [P] Comprueba el giro válido de cero pasos
     * ->JUnit
     */
    @Test
    public void shouldAcceptZeroStepsWithoutChangingTheWheel()
    {
        prepareThreeSymbolsAndOneWheel();

        machine.spin(1, 0);

        assertArrayEquals(
            new String[] {"red"},
            machine.configuration()
        );
        assertTrue(machine.ok());
    }

    /**
     * [P] Comprueba el cálculo con un entero muy grande
     * ->JUnit
     */
    @Test
    public void shouldRotateSafelyWithAVeryLargeStepCount()
    {
        prepareThreeSymbolsAndOneWheel();

        machine.spin(1, Integer.MAX_VALUE);

        assertArrayEquals(
            new String[] {"blue"},
            machine.configuration()
        );
        assertTrue(machine.ok());
    }

    /**
     * [P] Comprueba que varios pasos tampoco ignoren el bloqueo
     * ->JUnit
     */
    @Test
    public void shouldNotRotateSeveralStepsWhenWheelIsLocked()
    {
        prepareThreeSymbolsAndOneWheel();
        machine.lock(1);

        machine.spin(1, 2);

        assertArrayEquals(
            new String[] {"red"},
            machine.configuration()
        );
        assertFalse(machine.ok());
    }

    /**
     * [P] Comprueba un destino completo válido
     * ->JUnit
     */
    @Test
    public void shouldReachARequestedConfiguration()
    {
        prepareThreeConfiguredWheels();

        machine.spin(new String[] {"green", "red", "blue"});

        assertArrayEquals(
            new String[] {"green", "red", "blue"},
            machine.configuration()
        );
        assertTrue(machine.ok());
    }

    /**
     * [P] Comprueba la normalización de cada destino
     * ->JUnit
     */
    @Test
    public void shouldNormalizeRequestedConfigurationSymbols()
    {
        prepareThreeConfiguredWheels();

        machine.spin(new String[] {" GREEN ", "RED", "Blue"});

        assertArrayEquals(
            new String[] {"green", "red", "blue"},
            machine.configuration()
        );
        assertTrue(machine.ok());
    }

    /**
     * [P] Comprueba un destino compatible con una rueda bloqueada
     * ->JUnit
     */
    @Test
    public void shouldReachConfigurationWhenLockedWheelAlreadyMatches()
    {
        prepareThreeConfiguredWheels();
        machine.lock(1);

        machine.spin(new String[] {"red", "green", "blue"});

        assertArrayEquals(
            new String[] {"red", "green", "blue"},
            machine.configuration()
        );
        assertTrue(machine.ok());
    }

    /**
     * [P] Comprueba el rechazo de un destino incompatible con el bloqueo
     * ->JUnit
     */
    @Test
    public void shouldRejectConfigurationThatChangesALockedWheel()
    {
        prepareThreeConfiguredWheels();
        machine.lock(1);

        machine.spin(new String[] {"blue", "green", "red"});

        assertArrayEquals(
            new String[] {"red", "blue", "green"},
            machine.configuration()
        );
        assertFalse(machine.ok());
    }

    /**
     * [P] Comprueba que un color desconocido no deje cambios parciales
     * ->JUnit
     */
    @Test
    public void shouldRejectUnknownConfigurationAtomically()
    {
        prepareThreeConfiguredWheels();

        machine.spin(new String[] {"green", "cyan", "red"});

        assertArrayEquals(
            new String[] {"red", "blue", "green"},
            machine.configuration()
        );
        assertFalse(machine.ok());
    }

    /**
     * [P] Comprueba un destino nulo y otro de tamaño incorrecto
     * ->JUnit
     */
    @Test
    public void shouldRejectNullAndWrongSizedConfigurations()
    {
        prepareTwoConfiguredWheels();

        machine.spin((String[]) null);
        assertFalse(machine.ok());
        assertArrayEquals(
            new String[] {"red", "blue"},
            machine.configuration()
        );

        machine.spin(new String[] {"red"});
        assertFalse(machine.ok());
        assertArrayEquals(
            new String[] {"red", "blue"},
            machine.configuration()
        );
    }

    /**
     * [P] Comprueba que la configuración objetivo no se use para iniciar una rueda vacía
     * ->JUnit
     */
    @Test
    public void shouldRejectRequestedConfigurationWhenAWheelIsEmpty()
    {
        machine.addSymbol(1, "red");
        machine.addWheel(1);

        machine.spin(new String[] {"red"});

        assertArrayEquals(new String[] {null}, machine.configuration());
        assertFalse(machine.ok());
    }

    /**
     * [P] Prepara el catálogo [red, blue] y la configuración [red] para las pruebas que lo
     * necesitan
     */
    private void prepareOneConfiguredWheel()
    {
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addWheel(1);
        machine.placeSymbol(1, "red");
    }

    /**
     * [P] Prepara el catálogo [red, blue] y la configuración [red, blue] para las pruebas que lo
     * necesitan
     */
    private void prepareTwoConfiguredWheels()
    {
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addWheel(1);
        machine.addWheel(2);
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "blue");
    }

    /**
     * [P] Prepara el catálogo [red, blue, green] y la configuración [red] para las pruebas que
     * lo necesitan
     */
    private void prepareThreeSymbolsAndOneWheel()
    {
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addSymbol(3, "green");
        machine.addWheel(1);
        machine.placeSymbol(1, "red");
    }

    /**
     * [P] Prepara el catálogo [red, blue, green] y la configuración [red, blue, green] para las
     * pruebas que lo necesitan
     */
    private void prepareThreeConfiguredWheels()
    {
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addSymbol(3, "green");
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addWheel(3);
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "blue");
        machine.placeSymbol(3, "green");
    }
}
