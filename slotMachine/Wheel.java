/**
 * Represents one wheel of the slot machine. A new wheel is empty until the
 * machine explicitly places a symbol on it.
 *
 * The outer circle represents the wheel frame and the inner circle represents
 * its current color symbol. Both circles are reused from the shapes project.
 *
 * @author Paula Gacha and Diego Mojica
 * @version Cycle 2 complete implementation
 */
public class Wheel
{
    private static final int NO_SYMBOL = -1;
    private static final int OUTER_DIAMETER = 52;
    private static final int INNER_DIAMETER = 42;
    private static final int INNER_OFFSET = 5;
    private static final String EMPTY_COLOR = "white";
    private static final String NORMAL_FRAME_COLOR = "black";
    private static final String JACKPOT_FRAME_COLOR = "yellow";

    private int currentSymbolIndex;
    private final Circle frameCircle;
    private final Circle symbolCircle;
    private int xPosition;
    private int yPosition;
    private boolean isVisible;
    private boolean hasJackpotAppearance;
    private boolean isLocked;

    /**
     * Creates an empty wheel at the requested position.
     *
     * @param x horizontal position of the wheel frame
     * @param y vertical position of the wheel frame
     */
    public Wheel(int x, int y)
    {
        currentSymbolIndex = NO_SYMBOL;
        frameCircle = new Circle();
        symbolCircle = new Circle();
        xPosition = x;
        yPosition = y;
        isVisible = false;
        hasJackpotAppearance = false;
        isLocked = false;

        frameCircle.changeSize(OUTER_DIAMETER);
        frameCircle.changeColor(NORMAL_FRAME_COLOR);
        symbolCircle.changeSize(INNER_DIAMETER);
        symbolCircle.changeColor(EMPTY_COLOR);
        frameCircle.moveHorizontal(x - 20);
        frameCircle.moveVertical(y - 15);
        symbolCircle.moveHorizontal(x + INNER_OFFSET - 20);
        symbolCircle.moveVertical(y + INNER_OFFSET - 15);
    }

    /**
     * Assigns a symbol and its visual color to this wheel.
     *
     * @param symbolIndex index in the shared symbol catalog
     * @param color color used to represent the symbol
     */
    public void setSymbol(int symbolIndex, String color)
    {
        currentSymbolIndex = symbolIndex;
        showSymbol(color);
    }

    /**
     * Changes only the visual color of the assigned symbol.
     *
     * @param color color to display
     */
    public void showSymbol(String color)
    {
        symbolCircle.changeColor(color);
    }

    /**
     * Removes the current symbol and restores the empty-wheel appearance.
     */
    public void clearSymbol()
    {
        currentSymbolIndex = NO_SYMBOL;
        symbolCircle.changeColor(EMPTY_COLOR);
    }

    /**
     * Rotates the stored symbol index using circular arithmetic.
     *
     * @param steps number of positions to move
     * @param symbolCount number of symbols in the shared catalog
     */
    public void rotate(int steps, int symbolCount)
    {
        if (!isLocked && hasSymbol() && symbolCount > 0) {
            currentSymbolIndex = (int) Math.floorMod(
                (long) currentSymbolIndex + steps,
                (long) symbolCount
            );
        }
    }

    /**
     * Locks this wheel so rotation operations leave it unchanged.
     */
    public void lock()
    {
        isLocked = true;
    }

    /**
     * Unlocks this wheel so it can rotate again.
     */
    public void unlock()
    {
        isLocked = false;
    }

    /**
     * Reports whether this wheel is locked.
     *
     * @return true when rotation is disabled for this wheel
     */
    public boolean isLocked()
    {
        return isLocked;
    }

    /**
     * Reports whether this wheel currently has an assigned symbol.
     *
     * @return true when a symbol has been placed
     */
    public boolean hasSymbol()
    {
        return currentSymbolIndex != NO_SYMBOL;
    }

    /**
     * Returns the current index in the shared symbol catalog.
     *
     * @return symbol index or -1 when the wheel is empty
     */
    public int getCurrentSymbolIndex()
    {
        return currentSymbolIndex;
    }

    /**
     * Preserves the current symbol after a catalog insertion.
     *
     * @param insertedIndex zero-based inserted symbol index
     */
    public void adjustAfterSymbolInsertion(int insertedIndex)
    {
        if (hasSymbol() && currentSymbolIndex >= insertedIndex) {
            currentSymbolIndex++;
        }
    }

    /**
     * Preserves the current symbol after a catalog deletion, or empties the
     * wheel when its displayed symbol was deleted.
     *
     * @param removedIndex zero-based removed symbol index
     */
    public void adjustAfterSymbolRemoval(int removedIndex)
    {
        if (currentSymbolIndex == removedIndex) {
            clearSymbol();
        } else if (currentSymbolIndex > removedIndex) {
            currentSymbolIndex--;
        }
    }

    /**
     * Moves both visual circles to a new absolute position.
     *
     * @param x new horizontal position
     * @param y new vertical position
     */
    public void moveTo(int x, int y)
    {
        int horizontalDistance = x - xPosition;
        int verticalDistance = y - yPosition;

        frameCircle.moveHorizontal(horizontalDistance);
        frameCircle.moveVertical(verticalDistance);
        symbolCircle.moveHorizontal(horizontalDistance);
        symbolCircle.moveVertical(verticalDistance);

        xPosition = x;
        yPosition = y;
    }

    /**
     * Makes the empty frame and current symbol visible.
     */
    public void makeVisible()
    {
        isVisible = true;
        frameCircle.makeVisible();
        symbolCircle.makeVisible();
    }

    /**
     * Hides both visual circles.
     */
    public void makeInvisible()
    {
        symbolCircle.makeInvisible();
        frameCircle.makeInvisible();
        isVisible = false;
    }

    /**
     * Changes the wheel frame so the complete machine looks different when it
     * reaches a jackpot.
     *
     * @param jackpot true to show the winning appearance
     */
    public void setJackpotAppearance(boolean jackpot)
    {
        if (hasJackpotAppearance == jackpot) {
            return;
        }

        hasJackpotAppearance = jackpot;
        String frameColor = jackpot
            ? JACKPOT_FRAME_COLOR
            : NORMAL_FRAME_COLOR;
        frameCircle.changeColor(frameColor);

        if (isVisible) {
            symbolCircle.makeVisible();
        }
    }
}
