import java.awt.*;
/**
 * [RE] Representa un rectángulo gráfico reutilizado de shapes, no participa enlógica actual
 * de SlotMachine
 */
public class Rectangle{
    /**
     * [RE] Valor compartido 4
     */
    public static int EDGES = 4;
    /**
     * [RE] Configuraciones iniciales y demas
     */
    private int height;
    private int width;
    private int xPosition;
    private int yPosition;
    private String color;
    private boolean isVisible;
    /**
     * [RE] Construye un rectángulo con sus valores iniciales y lo deja invisible
     */
    public Rectangle(){
        height = 30;
        width = 40;
        xPosition = 70;
        yPosition = 15;
        color = "magenta";
        isVisible = false;
    }
    /**
     * [RE] Muestra este rectángulo con el estado que tiene guardado
     */
    public void makeVisible(){
        isVisible = true;
        draw();
    }
    /**
     * [RE] Oculta este rectángulo sin borrar sus atributos
     */
    public void makeInvisible(){
        erase();
        isVisible = false;
    }
    /**
     * [RE] Desplaza este rectángulo 20 píxeles hacia derecha
     */
    public void moveRight(){
        moveHorizontal(20);
    }
    /**
     * [RE] Desplaza este rectángulo 20 píxeles hacia izquierda
     */
    public void moveLeft(){
        moveHorizontal(-20);
    }
    /**
     * [RE] Desplaza este rectángulo 20 píxeles hacia arriba
     */
    public void moveUp(){
        moveVertical(-20);
    }
    /**
     * [RE] Desplaza este rectángulo 20 píxeles hacia abajo
     */
    public void moveDown(){
        moveVertical(20);
    }
    /**
     * [RE] Cambia xPosition una distancia y actualiza el dibujo
     */
    public void moveHorizontal(int distance){
        erase();
        xPosition += distance;
        draw();
    }
    /**
     * [RE] Cambia yPosition una distanci y actualiza el dibujo
     */
    public void moveVertical(int distance){
        erase();
        yPosition += distance;
        draw();
    }
    /**
     * [RE] Desplaza xPosition píxel a píxel en la dirección pedida.
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
     * [RE] Desplaza yPosition píxel a píxel en la dirección pedida
     */
    public void slowMoveVertical(int distance){
        int delta;

        if(distance < 0) { delta = -1;
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
     * [RE] Cambia height y width y actualiza rectángulo
     */
    public void changeSize(int newHeight, int newWidth) {
        erase();
        height = newHeight;
        width = newWidth;
        draw();
    }
    /**
     * [RE] Guarda el color solicitado y vuelve a dibujar el rectángulo
     * ->CV
     */
    public void changeColor(String newColor){
        color = newColor;
        draw();
    }
    /**
     * [RE] Entrega a Canvas la geometría y el color de este rectángulo, si está visible
     * ->CV.
     */
    private void draw() {
        if(isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.draw(this, color,
                new java.awt.Rectangle(xPosition, yPosition,width, height));
            canvas.wait(10);
        }
    }
    /**
     * [RE] Pide retirar de Canvas el dibujo de este rectángulo si estaba visible
     * ->CV.
     */
    private void erase(){
        if(isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.erase(this);
        }
    }
}
