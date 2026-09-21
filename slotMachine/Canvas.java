import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;
/**
 * [CV] Administra la ventana compartida y el orden de las figuras,contiene las clases
 * auxiliares CanvasPane y ShapeDescription.
 */
public class Canvas{
    /**
     * [CV] Referencia static al lienzo compartido
     */
    private static Canvas canvasSingleton;
    /**
     * [CV] Obtiene el lienzo compartido y hace visible su ventana
     * ->CV
     */
    public static Canvas getCanvas(){
        if(canvasSingleton == null) {
            canvasSingleton = new Canvas("Slot Machine", 900, 550,
                                         Color.white);
        }
        canvasSingleton.setVisible(true);
        return canvasSingleton;
    }
    /**
     * [CV] Oculta la ventana compartida si ya existe
     * ->CV
     */
    public static void closeCanvas(){
        if(canvasSingleton != null) {
            canvasSingleton.setVisible(false);
        }
    }
    /**
     * [CV] Configuraciones iniciales y generales de cnavan
     */
    private JFrame frame;

    private CanvasPane canvas;
    private Graphics2D graphic;
    private Color backgroundColour;
    private Image canvasImage;
    private List <Object> objects;
    /**
     * [CV] Mapa de objeto a ShapeDescription con la geometría y el color de cada figura
     */
    private HashMap <Object,ShapeDescription> shapes;
    /**
     * [CV] Construye la ventana, el panel y las colecciones que guardan los dibujos
     */
    private Canvas(String title, int width, int height, Color bgColour){
        frame = new JFrame();
        canvas = new CanvasPane();
        frame.setContentPane(canvas);
        frame.setTitle(title);
        canvas.setPreferredSize(new Dimension(width, height));
        backgroundColour = bgColour;
        frame.pack();
        objects = new ArrayList <Object>();
        shapes = new HashMap <Object,ShapeDescription>();
    }
    /**
     * [CV] Prepara la imagen de dibujo sifalta y cambia la visibilidad de la ventana
     */
    public void setVisible(boolean visible){
        if(graphic == null) {
            Dimension size = canvas.getSize();
            canvasImage = canvas.createImage(size.width, size.height);
            graphic = (Graphics2D)canvasImage.getGraphics();
            graphic.setColor(backgroundColour);
            graphic.fillRect(0, 0, size.width, size.height);
            graphic.setColor(Color.black);
        }
        frame.setVisible(visible);
    }
    /**
     * [CV] Registra o actualiza una figura y reconstruye la imagen completa.
     * ->CV
     */
    public void draw(Object referenceObject, String color, Shape shape){
        objects.remove(referenceObject);
        objects.add(referenceObject);
        shapes.put(referenceObject, new ShapeDescription(shape, color));
        redraw();
    }
    /**
     * [CV] Retira una figura del registro de dibujos
     * ->CV
     */
    public void erase(Object referenceObject){
        objects.remove(referenceObject);
        shapes.remove(referenceObject);
        redraw();
    }
    /**
     * [CV] Pide a SymbolColors el color gráfico y lo aplica al pincel
     */
    public void setForegroundColor(String colorString){
        graphic.setColor(SymbolColors.forDrawing(colorString));
    }
    /**
     * [CV] Introduce una pausa medida 
     */
    public void wait(int milliseconds){
        try{
            Thread.sleep(milliseconds);
        } catch (Exception e){
        }
    }
    /**
     * [CV] Reconstruye toda la imagen respetando el orden de las figuras
     * ->CV
     */
    private void redraw(){
        erase();
        for(Object object : objects) {
                       shapes.get(object).draw(graphic);
        }
        canvas.repaint();
    }
    /**
     * [CV] Limpia los píxeles de la imagen usando el color del fondo
     */
    private void erase(){
        Color original = graphic.getColor();
        graphic.setColor(backgroundColour);
        Dimension size = canvas.getSize();
        graphic.fill(new java.awt.Rectangle(0, 0, size.width, size.height));
        graphic.setColor(original);
    }
    /**
     * [CV] Panel interno que hereda de JPanel.
     */
    private class CanvasPane extends JPanel{
        /**
         * [CV] Identificador de serialización de CanvasPane, heredera de JPanel
         */
        private static final long serialVersionUID = 1L;
        /**
         * [CV] Muestra en el panel la imagen que Canvas ya preparó
         */
        public void paint(Graphics g){
            g.drawImage(canvasImage, 0, 0, null);
        }
    }
    /**
     * [CV] Conserva la geometría y el color de una figura
     */
    private class ShapeDescription{
        /**
         * [CV] Geometría guardada dentro de ShapeDescription.
         */
        private Shape shape;
        /**
         * [CV] Nombre de color guardado dentro de ShapeDescription.
         */
        private String colorString;
        /**
         * [CV] Guarda juntos la geometría y el nombre del color de una figura
         */
        public ShapeDescription(Shape shape, String color){
            this.shape = shape;
            colorString = color;
        }
        /**
         * [CV] Dibuja el contorno y relleno de una geometría guardada
         */
        public void draw(Graphics2D graphic){
            setForegroundColor(colorString);
            graphic.draw(shape);
            graphic.fill(shape);
        }
    }
}
