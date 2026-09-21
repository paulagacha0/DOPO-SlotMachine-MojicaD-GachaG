import java.awt.*;

/**
 * [TR] Representa un triángulo gráfico reutilizado de shapes, no participa en la lógica actual
 * de SlotMachine
 */
public class Triangle{

    /**
     * [TR] Valor compartido 3
     */
    public static int VERTICES=3;

    /**
     * [TR] Configruraciones iniciales y demas
     */
    private int height;
    private int width;
    private int xPosition;
    private int yPosition;
    private String color;
    private boolean isVisible;

    /**
     * [TR] Construye un triángulo con sus valores iniciales y lo deja invisible
     */
    public Triangle(){
        height = 30;
        width = 40;
        xPosition = 140;
        yPosition = 15;
        color = "green";
        isVisible = false;
    }

    /**
     * [TR] Muestra este triángulo con el estado que tiene guardado
     */
    public void makeVisible(){
        isVisible = true;
        draw();
    }

    /**
     * [TR] Oculta este triángulo sin borrar sus atributos
     */
    public void makeInvisible(){
        erase();
        isVisible = false;
    }

    /**
     * [TR] Desplaza este triángulo 20 píxeles hacia derecha
     */
    public void moveRight(){
        moveHorizontal(20);
    }

    /**
     * [TR] Desplaza este triángulo 20 píxeles hacia izquierda
     */
    public void moveLeft(){
        moveHorizontal(-20);
    }

    /**
     * [TR] Desplaza este triángulo 20 píxeles hacia arriba
     */
    public void moveUp(){
        moveVertical(-20);
    }

    /**
     * [TR] Desplaza este triángulo 20 píxeles hacia abajo
     */
    public void moveDown(){
        moveVertical(20);
    }

    /**
     * [TR] Cambia xPosition una distancia, actualiza el dibujo
     */
    public void moveHorizontal(int distance){
        erase();
        xPosition += distance;
        draw();
    }

    /**
     * [TR] Cambia yPosition una distancia y actualiza el dibujo
     */
    public void moveVertical(int distance){
        erase();
        yPosition += distance;
        draw();
    }

    /**
     * [TR] Desplaza xPosition píxel a píxel en la dirección pedida
     */
    public void slowMoveHorizontal(int distance){
        int delta;

        if(distance < 0) {
            delta = -1;
            distance = -distance;
        } else {
            delta = 1;
        }

        for(int i = 0; i < distance; i++){
            xPosition += delta;
            draw();
        }
    }

    /**
     * [TR] Desplaza yPosition píxel a píxel en la dirección pedida
     */
    public void slowMoveVertical(int distance){
        int delta;

        if(distance < 0) {
            delta = -1;
            distance = -distance;
        } else {
            delta = 1;
        }

        for(int i = 0; i < distance; i++){
            yPosition += delta;
            draw();
        }
    }

    /**
     * [TR] Cambia height y width y actualiza la geometría del triángulo
     */
    public void changeSize(int newHeight, int newWidth) {
        erase();
        height = newHeight;
        width = newWidth;
        draw();
    }

    /**
     * [TR] Guarda el color solicitado y vuelve a dibujar el triángulo
     * ->CV
     */
    public void changeColor(String newColor){
        color = newColor;
        draw();
    }

    /**
     * [TR] Entrega a Canvas la geometría y el color de este triángulosi está visible
     * ->CV.
     */
    private void draw(){
        if(isVisible) {
            Canvas canvas = Canvas.getCanvas();
            int[] xpoints = { xPosition, xPosition + (width/2), xPosition - (width/2) };
            int[] ypoints = { yPosition, yPosition + height, yPosition + height };
            canvas.draw(this, color, new Polygon(xpoints, ypoints, 3));
            canvas.wait(10);
        }
    }

    /**
     * [TR] Pide retirar de Canvas el dibujo de este triángulo
     * ->CV
     */
    private void erase(){
        if(isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.erase(this);
        }
    }
}
