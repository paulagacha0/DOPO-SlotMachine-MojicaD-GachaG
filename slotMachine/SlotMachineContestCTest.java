import static org.junit.Assert.*;
import org.junit.Test;

/**
 * [P -> SC] Dos casos preparados para compartir 
 */
public class SlotMachineContestCTest
{
    /** [P] Caso compartible: salir en cuanto una acción consigue jackpot. */
    @Test
    public void accordingGgMdShouldStopImmediatelyAfterJackpot()
    {
        int[][] actions = SlotMachineContestTest.checkSolution(new int[] {0, 1, 1});
        assertEquals(1, actions.length);
        assertArrayEquals(new int[] {1, 1}, actions[0]);
    }

    /** [P] Caso compartible: resolver símbolos repetidos y devolver una traza válida. */
    @Test
    public void accordingGgMdShouldSolveAndReplayRepeatedSymbols()
    {
        int[][] actions = SlotMachineContestTest.checkSolution(new int[] {0, 0, 1, 1, 2, 2});
        assertTrue(actions.length > 0);
        assertTrue(actions.length <= 73);
    }
}
