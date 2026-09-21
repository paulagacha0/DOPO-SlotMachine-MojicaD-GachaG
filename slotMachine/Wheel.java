/**
 * [W] Conserva el índice y el bloqueo de una rueda, controla dos Circle que dibujan el marco y
 * el interior
 */
public class Wheel
{
    /**
     * [W] Indicador -1 que significa que aún no hay un símbolo asignado
     */
    private static final int NO_SYMBOL = -1;
    /**
     * [W] Configuraciones y cosasgenerales
     */
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
     * [W] Construye una rueda vacía con un círculo para el marco y otro para el interior
     * ->CI
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
     * [W] Asigna el índice de un símbolo y actualiza su color
     * ->W
     */
    public void setSymbol(int symbolIndex, String color)
    {
        currentSymbolIndex = symbolIndex;
        showSymbol(color);
    }

    /**
     * [W] Cambia únicamente el color del círculo interior
     * ->CI
     */
    public void showSymbol(String color)
    {
        symbolCircle.changeColor(color);
    }

    /**
     * [W] Deja la rueda sin símbolo asignado
     * ->CI
     */
    public void clearSymbol()
    {
        currentSymbolIndex = NO_SYMBOL;
        symbolCircle.changeColor(EMPTY_COLOR);
    }

    /**
     * [W] Calcula el nuevo índice circular de una rueda que puede girar
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
     * [W] Marca la rueda como bloqueada
     */
    public void lock()
    {
        isLocked = true;
    }

    /**
     * [W] Marca la rueda como desbloqueada
     */
    public void unlock()
    {
        isLocked = false;
    }

    /**
     * [W] Devuelve si esta rueda está bloqueada
     */
    public boolean isLocked()
    {
        return isLocked;
    }

    /**
     * [W] Devuelve si la rueda tiene un índice distinto del indicador de vacío
     */
    public boolean hasSymbol()
    {
        return currentSymbolIndex != NO_SYMBOL;
    }

    /**
     * [W] Devuelve el índice actual del símbolo
     */
    public int getCurrentSymbolIndex()
    {
        return currentSymbolIndex;
    }

    /**
     * [W] Corrige el índice cuando se inserta un símbolo delante del actual
     * ->W
     */
    public void adjustAfterSymbolInsertion(int insertedIndex)
    {
        if (hasSymbol() && currentSymbolIndex >= insertedIndex) {
            currentSymbolIndex++;
        }
    }

    /**
     * [W] Corrige o vacía la rueda después de eliminar un símbolo del catálogo
     * ->W
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
     * [W] Mueve los dos círculos a una nueva posición de pantalla
     * ->CI.
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
     * [W] Muestra el marco y después el interior de larueda
     * ->CI
     */
    public void makeVisible()
    {
        isVisible = true;
        frameCircle.makeVisible();
        symbolCircle.makeVisible();
    }

    /**
     * [W] Oculta los dos círculos de esta rueda
     * ->CI
     */
    public void makeInvisible()
    {
        symbolCircle.makeInvisible();
        frameCircle.makeInvisible();
        isVisible = false;
    }

    /**
     * [W] Cambia el marco a amarillo si hay jackpot y a negro si no lo hay
     * ->CI.
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
