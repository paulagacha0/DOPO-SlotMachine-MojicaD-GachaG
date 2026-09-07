import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 * Collective Cycle 2 tests prepared for the corresponding class wiki.
 * The method names identify the authors using their surname initials. The
 * team must publish or verify these cases in the wiki before the delivery.
 *
 * @author Paula Gacha and Diego Mojica
 * @version Cycle 2 complete collective tests
 */
public class SlotMachineCC2Test
{
    private SlotMachine machine;

    @Before
    public void setUp()
    {
        machine = new SlotMachine();
        machine.makeInvisible();
    }

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
