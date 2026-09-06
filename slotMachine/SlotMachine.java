import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 * Models a slot machine whose wheels share the same ordered symbol catalog.
 * Public wheel positions are one-based, while internal list indexes are
 * zero-based.
 *
 * @author Paula Gacha and Diego Mojica
 * @version Cycle 1 corrected
 */
public class SlotMachine
{
    private static final int FIRST_WHEEL_X = 30;
    private static final int WHEEL_Y = 70;
    private static final int WHEEL_SPACING = 65;

    private final ArrayList<Wheel> wheels;
    private final ArrayList<String> symbols;
    private boolean isVisible;
    private boolean lastOperationSuccessful;
    private boolean isRunning;

    /**
     * Creates an empty, visible slot machine.
     */
    public SlotMachine()
    {
        wheels = new ArrayList<>();
        symbols = new ArrayList<>();
        isVisible = true;
        lastOperationSuccessful = true;
        isRunning = true;
    }

    /**
     * Inserts an empty wheel at the requested one-based position.
     *
     * @param pos position from 1 to the current number of wheels plus one
     */
    public void addWheel(int pos)
    {
        if (!ensureRunning()) {
            return;
        }
        if (!isInsertionPosition(pos, wheels.size())) {
            reportInvalidOperation("The wheel position is invalid.");
            return;
        }

        Wheel newWheel = new Wheel(FIRST_WHEEL_X, WHEEL_Y);
        wheels.add(pos - 1, newWheel);
        arrangeWheels();
        if (isVisible) {
            newWheel.makeVisible();
        }
        updateJackpotAppearance();
        reportSuccessfulOperation();
    }

    /**
     * Removes the wheel at the requested one-based position.
     *
     * @param pos position of the wheel to remove
     */
    public void delWheel(int pos)
    {
        if (!ensureRunning()) {
            return;
        }
        if (!isWheelPosition(pos)) {
            reportInvalidOperation("The wheel position is invalid.");
            return;
        }

        Wheel removedWheel = wheels.remove(pos - 1);
        removedWheel.makeInvisible();
        arrangeWheels();
        updateJackpotAppearance();
        reportSuccessfulOperation();
    }

    /**
     * Inserts a new color symbol at the requested one-based position.
     * Existing wheels keep showing the same colors after the insertion.
     *
     * @param pos position from 1 to the current number of symbols plus one
     * @param color supported color name
     */
    public void addSymbol(int pos, String color)
    {
        String normalizedColor = normalizeColor(color);
        if (!ensureRunning()) {
            return;
        }
        if (!isInsertionPosition(pos, symbols.size())) {
            reportInvalidOperation("The symbol position is invalid.");
            return;
        }
        if (!isSupportedSymbolColor(normalizedColor)) {
            reportInvalidOperation("The symbol color is invalid.");
            return;
        }
        if (symbols.contains(normalizedColor)) {
            reportInvalidOperation("The symbol color already exists.");
            return;
        }

        int symbolIndex = pos - 1;
        symbols.add(symbolIndex, normalizedColor);
        for (Wheel wheel : wheels) {
            wheel.adjustAfterSymbolInsertion(symbolIndex);
        }
        reportSuccessfulOperation();
    }

    /**
     * Deletes a symbol. A wheel showing that symbol becomes empty.
     *
     * @param symbol color symbol to remove
     */
    public void delSymbol(String symbol)
    {
        String normalizedSymbol = normalizeColor(symbol);
        int symbolIndex = symbols.indexOf(normalizedSymbol);
        if (!ensureRunning()) {
            return;
        }
        if (symbolIndex < 0) {
            reportInvalidOperation("The symbol does not exist.");
            return;
        }

        symbols.remove(symbolIndex);
        for (Wheel wheel : wheels) {
            wheel.adjustAfterSymbolRemoval(symbolIndex);
        }
        updateJackpotAppearance();
        reportSuccessfulOperation();
    }

    /**
     * Places an existing symbol on a wheel.
     *
     * @param wheel one-based wheel position
     * @param symbol existing color symbol
     */
    public void placeSymbol(int wheel, String symbol)
    {
        String normalizedSymbol = normalizeColor(symbol);
        int symbolIndex = symbols.indexOf(normalizedSymbol);
        if (!ensureRunning()) {
            return;
        }
        if (!isWheelPosition(wheel)) {
            reportInvalidOperation("The wheel position is invalid.");
            return;
        }
        if (symbolIndex < 0) {
            reportInvalidOperation("The symbol does not exist.");
            return;
        }

        wheels.get(wheel - 1).setSymbol(symbolIndex, normalizedSymbol);
        updateJackpotAppearance();
        reportSuccessfulOperation();
    }

    /**
     * Rotates one wheel one step.
     *
     * @param wheel one-based wheel position
     */
    public void spin(int wheel)
    {
        if (!ensureRunning()) {
            return;
        }
        if (!isWheelPosition(wheel)) {
            reportInvalidOperation("The wheel position is invalid.");
            return;
        }

        Wheel selectedWheel = wheels.get(wheel - 1);
        if (symbols.isEmpty() || !selectedWheel.hasSymbol()) {
            reportInvalidOperation("The wheel does not have a symbol to rotate.");
            return;
        }

        rotateWheel(selectedWheel, 1);
        updateJackpotAppearance();
        reportSuccessfulOperation();
    }

    /**
     * Rotates every wheel one step.
     */
    public void spin()
    {
        if (!ensureRunning()) {
            return;
        }
        if (wheels.isEmpty() || symbols.isEmpty()) {
            reportInvalidOperation("The machine does not have wheels and symbols to rotate.");
            return;
        }
        if (!allWheelsHaveSymbols()) {
            reportInvalidOperation("Every wheel must have a symbol before rotating the machine.");
            return;
        }

        for (Wheel wheel : wheels) {
            rotateWheel(wheel, 1);
        }
        updateJackpotAppearance();
        reportSuccessfulOperation();
    }

    /**
     * Returns a copy of the ordered symbol catalog.
     *
     * @return available color symbols
     */
    public String[] symbols()
    {
        return symbols.toArray(new String[0]);
    }

    /**
     * Returns the visible symbol of each wheel. An empty wheel is represented
     * by a null value.
     *
     * @return current machine configuration
     */
    public String[] configuration()
    {
        String[] result = new String[wheels.size()];
        for (int i = 0; i < wheels.size(); i++) {
            Wheel wheel = wheels.get(i);
            if (wheel.hasSymbol()) {
                result[i] = symbols.get(wheel.getCurrentSymbolIndex());
            }
        }
        return result;
    }

    /**
     * Counts the different assigned symbols in the current configuration.
     * Empty wheels are ignored.
     *
     * @return number of distinct assigned symbols
     */
    public int distinctSymbols()
    {
        ArrayList<String> distinct = new ArrayList<>();
        for (String symbol : configuration()) {
            if (symbol != null && !distinct.contains(symbol)) {
                distinct.add(symbol);
            }
        }
        return distinct.size();
    }

    /**
     * Reports whether every wheel has the same assigned symbol.
     *
     * @return true only for a non-empty, fully assigned winning machine
     */
    public boolean isJackpot()
    {
        return hasJackpot();
    }

    /**
     * Makes every wheel visible.
     */
    public void makeVisible()
    {
        if (!ensureRunning()) {
            return;
        }

        isVisible = true;
        for (Wheel wheel : wheels) {
            wheel.makeVisible();
        }
        updateJackpotAppearance();
        reportSuccessfulOperation();
    }

    /**
     * Makes every wheel invisible while preserving the machine state.
     */
    public void makeInvisible()
    {
        if (!ensureRunning()) {
            return;
        }

        for (Wheel wheel : wheels) {
            wheel.makeInvisible();
        }
        isVisible = false;
        Canvas.closeCanvas();
        reportSuccessfulOperation();
    }

    /**
     * Finishes the simulator and closes its visual canvas.
     */
    public void exit()
    {
        if (!isRunning) {
            reportInvalidOperation("The simulator has already finished.");
            return;
        }

        for (Wheel wheel : wheels) {
            wheel.makeInvisible();
        }
        isVisible = false;
        isRunning = false;
        Canvas.closeCanvas();
        reportSuccessfulOperation();
    }

    /**
     * Reports whether the last command was successful.
     *
     * @return true when the last command was valid
     */
    public boolean ok()
    {
        return lastOperationSuccessful;
    }

    private boolean ensureRunning()
    {
        if (!isRunning) {
            reportInvalidOperation("The simulator has already finished.");
            return false;
        }
        return true;
    }

    private boolean isInsertionPosition(int pos, int currentSize)
    {
        return pos >= 1 && pos <= currentSize + 1;
    }

    private boolean isWheelPosition(int wheel)
    {
        return wheel >= 1 && wheel <= wheels.size();
    }

    private String normalizeColor(String color)
    {
        return color == null ? null : color.trim().toLowerCase();
    }

    private boolean isSupportedSymbolColor(String color)
    {
        return "red".equals(color) || "yellow".equals(color)
            || "blue".equals(color) || "green".equals(color)
            || "magenta".equals(color) || "black".equals(color);
    }

    private void arrangeWheels()
    {
        for (int i = 0; i < wheels.size(); i++) {
            int xPosition = FIRST_WHEEL_X + i * WHEEL_SPACING;
            wheels.get(i).moveTo(xPosition, WHEEL_Y);
        }
    }

    private void rotateWheel(Wheel wheel, int steps)
    {
        wheel.rotate(steps, symbols.size());
        String color = symbols.get(wheel.getCurrentSymbolIndex());
        wheel.showSymbol(color);
    }

    private boolean allWheelsHaveSymbols()
    {
        for (Wheel wheel : wheels) {
            if (!wheel.hasSymbol()) {
                return false;
            }
        }
        return true;
    }

    private boolean hasJackpot()
    {
        if (wheels.isEmpty() || !allWheelsHaveSymbols()) {
            return false;
        }

        int firstIndex = wheels.get(0).getCurrentSymbolIndex();
        for (Wheel wheel : wheels) {
            if (wheel.getCurrentSymbolIndex() != firstIndex) {
                return false;
            }
        }
        return true;
    }

    private void updateJackpotAppearance()
    {
        boolean jackpot = hasJackpot();
        for (Wheel wheel : wheels) {
            wheel.setJackpotAppearance(jackpot);
        }
    }

    private void reportSuccessfulOperation()
    {
        lastOperationSuccessful = true;
    }

    private void reportInvalidOperation(String message)
    {
        lastOperationSuccessful = false;
        if (isVisible) {
            JOptionPane.showMessageDialog(
                null,
                message,
                "Slot Machine",
                JOptionPane.WARNING_MESSAGE
            );
        }
    }
}
