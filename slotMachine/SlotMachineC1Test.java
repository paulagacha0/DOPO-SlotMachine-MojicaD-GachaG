import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the corrected Cycle 1 behavior.
 * Every test runs with the simulator in invisible mode.
 *
 * @author Paula Gacha and Diego Mojica
 * @version Cycle 1 corrected
 */
public class SlotMachineC1Test
{
    private SlotMachine machine;

    /**
     * Creates an invisible machine before every test.
     */
    @Before
    public void setUp()
    {
        machine = new SlotMachine();
        machine.makeInvisible();
    }

    @Test
    public void shouldCreateAnEmptyMachine()
    {
        assertEquals(0, machine.symbols().length);
        assertEquals(0, machine.configuration().length);
        assertFalse(machine.isJackpot());
    }

    @Test
    public void shouldAddAnEmptyWheelWithoutDefaultSymbol()
    {
        machine.addSymbol(1, "red");
        machine.addWheel(1);

        assertEquals(1, machine.configuration().length);
        assertNull(machine.configuration()[0]);
        assertFalse(machine.isJackpot());
        assertTrue(machine.ok());
    }

    @Test
    public void shouldInsertSymbolsAtTheRequestedPositions()
    {
        machine.addSymbol(1, "red");
        machine.addSymbol(1, "blue");

        assertArrayEquals(
            new String[] {"blue", "red"},
            machine.symbols()
        );
    }

    @Test
    public void shouldPlaceAnExistingSymbolOnAWheel()
    {
        machine.addSymbol(1, "red");
        machine.addWheel(1);
        machine.placeSymbol(1, "red");

        assertArrayEquals(new String[] {"red"}, machine.configuration());
        assertTrue(machine.ok());
    }

    @Test
    public void shouldPreserveDisplayedSymbolAfterCatalogInsertion()
    {
        machine.addSymbol(1, "red");
        machine.addWheel(1);
        machine.placeSymbol(1, "red");

        machine.addSymbol(1, "blue");

        assertArrayEquals(new String[] {"red"}, machine.configuration());
    }

    @Test
    public void shouldClearWheelWhenItsSymbolIsDeleted()
    {
        machine.addSymbol(1, "red");
        machine.addWheel(1);
        machine.placeSymbol(1, "red");

        machine.delSymbol("red");

        assertNull(machine.configuration()[0]);
        assertFalse(machine.isJackpot());
    }

    @Test
    public void shouldRotateAnAssignedWheel()
    {
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addWheel(1);
        machine.placeSymbol(1, "red");

        machine.spin(1);

        assertArrayEquals(new String[] {"blue"}, machine.configuration());
        assertTrue(machine.ok());
    }

    @Test
    public void shouldNotRotateAnEmptyWheel()
    {
        machine.addSymbol(1, "red");
        machine.addWheel(1);

        machine.spin(1);

        assertNull(machine.configuration()[0]);
        assertFalse(machine.ok());
    }

    @Test
    public void shouldDetectJackpotOnlyWhenEveryWheelHasTheSameSymbol()
    {
        machine.addSymbol(1, "red");
        machine.addWheel(1);
        machine.addWheel(2);
        machine.placeSymbol(1, "red");

        assertFalse(machine.isJackpot());

        machine.placeSymbol(2, "red");

        assertTrue(machine.isJackpot());
    }

    @Test
    public void shouldRejectInvalidPositionsAndDuplicateSymbols()
    {
        machine.addWheel(0);
        assertFalse(machine.ok());

        machine.addSymbol(1, "red");
        machine.addSymbol(2, "RED");

        assertFalse(machine.ok());
        assertArrayEquals(new String[] {"red"}, machine.symbols());
    }
}
