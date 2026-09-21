import java.util.ArrayList;
import java.util.Random;
import javax.swing.JOptionPane;
/**
 * [SM] Administra el catálogo compartido, la colección de ruedas, las validaciones y las
 * operaciones públicas del simulador
 */
public class SlotMachine
{
    /**
     * conf generales, posicion y demas
     */    
    private static final int FIRST_WHEEL_X = 30;
    private static final int WHEEL_Y = 70;
    private static final int WHEEL_SPACING = 65;
    private static final int WHEELS_PER_ROW = 10;
    private static final int ROW_SPACING = 80;
    private static final int STEP_ANIMATION_DELAY_MS = 200;

    /**
     * [SM] Lista de referencias a objetos Wheel final-> impide sustituir la lista, pero
     * permite editar sus elementos
     */
    private final ArrayList<Wheel> wheels;
    /**
     * [SM] Catálogo compartido de color,cada rueda guarda un índice dentro
     * de esta lista
     */
    private final ArrayList<String> symbols;
    /**
     * [SM] Indica si las órdenes deben mostrar dibujos, animación y mensajes
     */
    private boolean isVisible;
    /**
     * [SM] Resultado del último comando registrado ok
     */
    private boolean lastOperationSuccessful;
    /**
     * [SM] Indica si simulador sigue aceptando órdenes
     */
    private boolean isRunning;
    /**
     * [SM] Crea una máquina con dos listas vacías y deja preparadas en estado inicial
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
     * [SM] Crea n ruedas y n símbolos signa cada rueda al azar y empieza invisible
     */
    public SlotMachine(int n)
    {
        this();
        if (n < 3 || n > 50) {
            throw new IllegalArgumentException("The size must be between 3 and 50.");
        }
        isVisible = false;
        Random random = new Random();
        for (String color : SymbolColors.createPalette(n)) {
            symbols.add(color);
        }
        for (int i = 0; i < n; i++) {
            Wheel wheel = new Wheel(FIRST_WHEEL_X, WHEEL_Y);
            int symbolIndex = random.nextInt(n);
            wheel.setSymbol(symbolIndex, symbols.get(symbolIndex));
            wheels.add(wheel);
        }
        if (hasJackpot()) {
            int nextIndex = (wheels.get(0).getCurrentSymbolIndex() + 1) % n;
            wheels.get(n - 1).setSymbol(nextIndex, symbols.get(nextIndex));
        }
        arrangeWheels();
        updateJackpotAppearance();
    }
    /**
     * [SM] Inserta una rueda vacía en la posición indicada por el usuario.
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
     * [SM] Quita una rueda existente y borrarepr esentación
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
     * [SM] Intercambia dos objetos Wheel completos dentro de la lista
     */
    public void swap(int wheel1, int wheel2)
    {
        if (!ensureRunning()) {
            return;
        }
        if (!isWheelPosition(wheel1) || !isWheelPosition(wheel2)) {
            reportInvalidOperation("A wheel position is invalid.");
            return;
        }

        Wheel firstWheel = wheels.get(wheel1 - 1);
        wheels.set(wheel1 - 1, wheels.get(wheel2 - 1));
        wheels.set(wheel2 - 1, firstWheel);
        arrangeWheels();
        updateJackpotAppearance();
        reportSuccessfulOperation();
    }
    /**
     * [SM] Bloquea una rueda para impedir su rotación
     */
    public void lock(int wheel)
    {
        if (!ensureRunning()) {
            return;
        }
        if (!isWheelPosition(wheel)) {
            reportInvalidOperation("The wheel position is invalid.");
            return;
        }

        Wheel selectedWheel = wheels.get(wheel - 1);
        if (selectedWheel.isLocked()) {
            reportInvalidOperation("The wheel is already locked.");
            return;
        }

        selectedWheel.lock();
        reportSuccessfulOperation();
    }
    /**
     * [SM] Desbloquea una rueda para permitirle girar de nuevo
     */
    public void unlock(int wheel)
    {
        if (!ensureRunning()) {
            return;
        }
        if (!isWheelPosition(wheel)) {
            reportInvalidOperation("The wheel position is invalid.");
            return;
        }

        Wheel selectedWheel = wheels.get(wheel - 1);
        if (!selectedWheel.isLocked()) {
            reportInvalidOperation("The wheel is already unlocked.");
            return;
        }

        selectedWheel.unlock();
        reportSuccessfulOperation();
    }
    /**
     * [SM] Inserta un color válido y no repetido de el catálogo
     * ->W
     */
    public void addSymbol(int pos, String color)
    {
        String normalizedColor = normalizeColor(color);
        if (!ensureRunning()) {
            return;
        }
        if (!isInsertionPosition(pos, symbols.size())) {
            reportInvalidOperation("The symbol position is invalid");
            return;
        }
        if (!isSupportedSymbolColor(normalizedColor)) {
            reportInvalidOperation("The symbol color is invalid");
            return;
        }
        if (symbols.contains(normalizedColor)) {
            reportInvalidOperation("The symbol color already exists");
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
     * [SM] Elimina un color del catálogo y mantiene los índices de las ruedas
     */
    public void delSymbol(String symbol)
    {
        String normalizedSymbol = normalizeColor(symbol);
        int symbolIndex = symbols.indexOf(normalizedSymbol);
        if (!ensureRunning()) {
            return;
        }
        if (symbolIndex < 0) {
            reportInvalidOperation("The symbol does not exist");
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
     * [SM] Asigna a una rueda un símbolo que ya pertenece al catálogo
     * ->CI, W
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
     * [SM] Pide girar una rueda un solo paso hacia adelante
     */
    public void spin(int wheel)
    {
        spin(wheel, 1);
    }
    /**
     * [SM] Gira una rueda una cantidad de pasos positiva, negativa o cero
     * ->W.
     */
    public void spin(int wheel, int steps)
    {
        if (!ensureRunning()) {
            return;
        }
        if (!isWheelPosition(wheel)) {
            reportInvalidOperation("The wheel position is invalid");
            return;
        }
        Wheel selectedWheel = wheels.get(wheel - 1);
        if (selectedWheel.isLocked()) {
            reportInvalidOperation("The wheel is locked");
            return;
        }
        if (symbols.isEmpty() || !selectedWheel.hasSymbol()) {
            reportInvalidOperation("The wheel does not have a symbol to rotate");
            return;
        }
        rotateWheelBySteps(selectedWheel, steps);
        updateJackpotAppearance();
        reportSuccessfulOperation();
    }
    /**
     * [SM] Avanza un paso las ruedas desbloqueadas
     */
    public void spin()
    {
        if (!ensureRunning()) {
            return;
        }
        if (wheels.isEmpty() || symbols.isEmpty()) {
            reportInvalidOperation("The machine does not have wheels and symbols to rotate");
            return;
        }
        if (!hasUnlockedWheel()) {
            reportInvalidOperation("The machine does not have an unlocked wheel to rotate");
            return;
        }
        if (!allUnlockedWheelsHaveSymbols()) {
            reportInvalidOperation("Every unlocked wheel must have a symbol before rotating the machine");
            return;
        }

        for (Wheel wheel : wheels) {
            if (!wheel.isLocked()) {
                rotateWheel(wheel, 1);
            }
        }
        updateJackpotAppearance();
        reportSuccessfulOperation();
    }
    /**
     * [SM] Intenta alcanzar una configuración completa después de validar todos elementos
     * ->W
     */
    public void spin(String[] setSymbols)
    {
        if (!ensureRunning()) {
            return;
        }
        if (setSymbols == null) {
            reportInvalidOperation("The requested configuration is null");
            return;
        }
        if (wheels.isEmpty()) {
            reportInvalidOperation("The machine does not have wheels");
            return;
        }
        if (setSymbols.length != wheels.size()) {
            reportInvalidOperation("The requested configuration has an invalid size");
            return;
        }
        if (symbols.isEmpty()) {
            reportInvalidOperation("The machine does not have symbols");
            return;
        }
        int[] targetIndexes = new int[setSymbols.length];
        for (int i = 0; i < setSymbols.length; i++) {
            String normalizedSymbol = normalizeColor(setSymbols[i]);
            int targetIndex = symbols.indexOf(normalizedSymbol);
            Wheel wheel = wheels.get(i);

            if (targetIndex < 0) {
                reportInvalidOperation(
                    "A symbol in the requested configuration does not exist"
                );
                return;
            }
            if (!wheel.hasSymbol()) {
                reportInvalidOperation(
                    "Every wheel must have a symbol before setting a configuration"
                );
                return;
            }
            if (wheel.isLocked()
                && wheel.getCurrentSymbolIndex() != targetIndex) {
                reportInvalidOperation(
                    "A locked wheel cannot reach the requested configuration"
                );
                return;
            }

            targetIndexes[i] = targetIndex;
        }
        for (int i = 0; i < wheels.size(); i++) {
            Wheel wheel = wheels.get(i);
            if (!wheel.isLocked()) {
                int steps = Math.floorMod(
                    targetIndexes[i] - wheel.getCurrentSymbolIndex(),
                    symbols.size()
                );
                rotateWheelBySteps(wheel, steps);
            }
        }

        updateJackpotAppearance();
        reportSuccessfulOperation();
    }
    /**
     * [SM] Devuelve una copia del catálogo ordenado de símbolos
     */
    public String[] symbols()
    {
        return symbols.toArray(new String[0]);
    }
    /**
     * [SM] Devuelve el símbolo de cada rueda, en el orden actual de las ruedas
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
     * [SM] Cuenta cuántos colores diferentes aparecen en las ruedas configuradas
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
     * [SM] Consulta si la configuración actual es ganadora.
     */
    public boolean isJackpot()
    {
        return hasJackpot();
    }
    /**
     * [SM] Muestra las ruedas de una máquina que sigue activa
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
     * [SM] Oculta la máquina conservando sus ruedas, símbolos y configuración
     *  ->CV, W
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
     * [SM] Termina las órdenes del simulador y oculta sus dibujos
     * ->CV, W
     */
    public void exit()
    {
        if (!isRunning) {
            reportInvalidOperation("The simulator has already finished");
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
     * [SM] Devuelve si el último comando registrado fue válido
     */
    public boolean ok()
    {
        return lastOperationSuccessful;
    }

    /**
     * [SM] Comprueba que la máquina pueda seguir recibiendo órdenes
     */
    private boolean ensureRunning()
    {
        if (!isRunning) {
            reportInvalidOperation("The simulator has already finished");
            return false;
        }
        return true;
    }

    /**
     * [SM] Comprueba una posición donde se puede insertar un element  
    */
    private boolean isInsertionPosition(int pos, int currentSize)
    {
        return pos >= 1 && pos <= currentSize + 1;
    }

    /**
     * [SM] Comprueba una posición de rueda que ya debe existir.
     */
    private boolean isWheelPosition(int wheel)
    {
        return wheel >= 1 && wheel <= wheels.size();
    }

    /**
     * [SM] Normaliza el texto y unifica alias usando SymbolColors.l
     */
    private String normalizeColor(String color)
    {
        return SymbolColors.normalize(color);
    }

    /**
     * [SM] Valida nombres básicos y colores, el blanco queda reservado
     */
    private boolean isSupportedSymbolColor(String color)
    {
        return SymbolColors.isSymbol(color);
    }

    /**
     * [SM] Distribuye las ruedas en filas de hasta diez y actualiza sus coordenadas
     * ->W
     */
    private void arrangeWheels()
    {
        for (int i = 0; i < wheels.size(); i++) {
            int xPosition = FIRST_WHEEL_X + (i % WHEELS_PER_ROW) * WHEEL_SPACING;
            int yPosition = WHEEL_Y + (i / WHEELS_PER_ROW) * ROW_SPACING;
            wheels.get(i).moveTo(xPosition, yPosition);
        }
    }

    /**
     * [SM] Aplica un desplazamiento lógico y actualiza el color correspondiente
     * ->W
     */
    private void rotateWheel(Wheel wheel, int steps)
    {
        wheel.rotate(steps, symbols.size());
        String color = symbols.get(wheel.getCurrentSymbolIndex());
        wheel.showSymbol(color);
    }

    /**
     * [SM] Elige entre giro directo invisible y giro visible
     * ->CV
     */
    private void rotateWheelBySteps(Wheel wheel, int steps)
    {
        if (!isVisible || steps == 0) {
            rotateWheel(wheel, steps);
            return;
        }

        int direction = steps > 0 ? 1 : -1;
        long remainingSteps = Math.abs((long) steps);
        while (remainingSteps > 0) {
            rotateWheel(wheel, direction);
            updateJackpotAppearance();
            Canvas.getCanvas().wait(STEP_ANIMATION_DELAY_MS);
            remainingSteps--;
        }
    }
    /**
     * [SM] Verifica que ninguna rueda esté vacía
     * ->W
     */
    private boolean allWheelsHaveSymbols()
    {
        for (Wheel wheel : wheels) {
            if (!wheel.hasSymbol()) {
                return false;
            }
        }
        return true;
    }
    /**
     * [SM] Comprueba si existe al menos una rueda que pueda girar por bloqueo
     * ->W
     */
    private boolean hasUnlockedWheel()
    {
        for (Wheel wheel : wheels) {
            if (!wheel.isLocked()) {
                return true;
            }
        }
        return false;
    }

    /**
     * [SM] Comprueba que todas las ruedas que participarán en spin() tengan símbolo
     * ->W
     */
    private boolean allUnlockedWheelsHaveSymbols()
    {
        for (Wheel wheel : wheels) {
            if (!wheel.isLocked() && !wheel.hasSymbol()) {
                return false;
            }
        }
        return true;
    }
    /**
     * [SM] Comprueba que exista una máquina configurada con el mismo símbolo en todas sus
     * ruedas
     * ->W
     */
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
    /**
     * [SM] Aplica el estado ganador a los marcos de todas las ruedas
     * ->W
     */
    private void updateJackpotAppearance()
    {
        boolean jackpot = hasJackpot();
        for (Wheel wheel : wheels) {
            wheel.setJackpotAppearance(jackpot);
        }
    }
    /**
     * [SM] Registra que el último comando fue válido
     */
    private void reportSuccessfulOperation()
    {
        lastOperationSuccessful = true;
    }
    /**
     * [SM] Registra un errorsi la máquina está visible informa al usuario
     */
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
