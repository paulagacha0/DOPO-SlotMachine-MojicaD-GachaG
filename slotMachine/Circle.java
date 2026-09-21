import java.awt.*;
import java.awt.geom.*;
/**
 * [CI] Representa un círculo gráfico con posición, diámetro, color y visibilidad, Wheel utiliza
 * dos instancias de esta clase
 */
public class Circle{
    /**
     * [CI] Detalles generales y demas
     */
    public static final double PI=3.1416;
    private int diameter;
    private int xPosition;
    private int yPosition;
    private String color;
    private boolean isVisible;
    /**
     * [CI] Construye un círculo con sus valores iniciales y lo deja invisible
     */
    public Circle(){
        diameter = 30;
        xPosition = 20;
        yPosition = 15;
        color = "blue";
        isVisible = false;
    }
    /**
     * [CI] Muestra este círculo con el estado que tiene guardado
     * ->CI
     */
    public void makeVisible(){
        isVisible = true;
        draw();
    }
    /**
     * [CI] Oculta este círculo sin borrar sus atributos
     * ->CI
     */
    public void makeInvisible(){
        erase();
        isVisible = false;
    }
    /**
     * [CI] Entrega a Canvas la geometría y el color de este círculo si está visible
     * ->CV
     */
    private void draw(){
        if(isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.draw(this, color,
                new Ellipse2D.Double(xPosition, yPosition,
                diameter, diameter));
            canvas.wait(10);
        }
    }

    /**
     * [CI] Pide retirar de Canvas el dibujo de este círculo
     * ->CV
     */
    private void erase(){
        if(isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.erase(this);
        }
    }
    /**
     * [CI] Desplaza este círculo 20 píxeles hacia derecha
     * ->CI
     */
    public void moveRight(){
        moveHorizontal(20);
    }
    /**
     * [CI] Desplaza este círculo 20 píxeles hacia izquierda
     * ->CI
     */
    public void moveLeft(){
        moveHorizontal(-20);
    }
    /**
     * [CI] Desplaza este círculo 20 píxeles hacia arriba
     * ->CI
     */
    public void moveUp(){
        moveVertical(-20);
    }
    /**
     * [CI] Desplaza este círculo 20 píxeles hacia abajo
     * ->CI
     */
    public void moveDown(){
        moveVertical(20);
    }
    /**
     * [CI] Cambia xPosition una distancia,actualiza el dibujo
     * ->CI
     */
    public void moveHorizontal(int distance){
        erase();
        xPosition += distance;
        draw();
    }
    /**
     * [CI] Cambia yPosition una distancia y actualiza el dibujo
     * ->CI
     */
    public void moveVertical(int distance){
        erase();
        yPosition += distance;
        draw();
    }
    /**
     * [CI] Desplaza xPosition píxel a píxel en la dirección pedida
     * ->CI
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
     * [CI] Desplaza yPosition píxel a píxel en la dirección pedida
     * ->CI
     */
    public void slowMoveVertical(int distance){
        int delta;

        if(distance < 0) {
            delta = -1;
            distance = -distance;
        }else {
            delta = 1;
        }
        for(int i = 0; i < distance; i++){
            yPosition += delta;
            draw();
        }
    }
    /**
     * [CI] Cambia diameter y actualiza la geometría del círculo
     * ->CI
     */
    public void changeSize(int newDiameter){
        erase();
        diameter = newDiameter;
        draw();
    }
    /**
     * [CI] Guarda el color solicitado y vuelve a dibujar el círculo
     * ->CI, CV
     */
    public void changeColor(String newColor){
        color = newColor;
        draw();
    }
}
