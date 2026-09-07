import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for all Cycle 2 functional requirements.
 * Every test runs with the simulator in invisible mode.
 *
 * @author Paula Gacha
 * @version Cycle 2 complete tests
 */
public class SlotMachineC2Test
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

    @Test
    public void shouldRejectInvalidLockAndUnlockPositions()
    {
        machine.addWheel(1);

        machine.lock(0);
        assertFalse(machine.ok());

        machine.unlock(2);
        assertFalse(machine.ok());
    }

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

    @Test
    public void shouldRejectRequestedConfigurationWhenAWheelIsEmpty()
    {
        machine.addSymbol(1, "red");
        machine.addWheel(1);

        machine.spin(new String[] {"red"});

        assertArrayEquals(new String[] {null}, machine.configuration());
        assertFalse(machine.ok());
    }

    private void prepareOneConfiguredWheel()
    {
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addWheel(1);
        machine.placeSymbol(1, "red");
    }

    private void prepareTwoConfiguredWheels()
    {
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addWheel(1);
        machine.addWheel(2);
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "blue");
    }

    private void prepareThreeSymbolsAndOneWheel()
    {
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addSymbol(3, "green");
        machine.addWheel(1);
        machine.placeSymbol(1, "red");
    }

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
