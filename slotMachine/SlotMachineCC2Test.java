import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 * [P] Contiene dos casos de prueba identificados por el equipo sobre bloqueo y rechazo de
 * destinos inválidos
 */
public class SlotMachineCC2Test
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
     * [P] Comprueba el giro general con una rueda bloqueada en un caso identificado por el
     * equipo
     * ->JUnit
     */
    @Test
    public void accordingGgMdShouldKeepLockedWheelStillWhileSpinningAll()
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
     * [P] Comprueba que se rechace todo el destino cuando uno de sus colores es desconocido
     * ->JUnit
     */
    @Test
    public void accordingGgMdShouldRejectInvalidTargetWithoutPartialChanges()
    {
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addWheel(1);
        machine.addWheel(2);
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "blue");

        machine.spin(new String[] {"blue", "cyan"});

        assertArrayEquals(
            new String[] {"red", "blue"},
            machine.configuration()
        );
        assertFalse(machine.ok());
    }
}
