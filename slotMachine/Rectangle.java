import java.awt.*;
public class Rectangle{
    public static int EDGES = 4;
    private int height;
    private int width;
    private int xPosition;
    private int yPosition;
    private String color;
    private boolean isVisible;
    public Rectangle(){
        height = 30;
        width = 40;
        xPosition = 70;
        yPosition = 15;
        color = "magenta";
        isVisible = false;
    }
    public void makeVisible(){
        isVisible = true;
        draw();
    }
    public void makeInvisible(){
        erase();
        isVisible = false;
    }
    public void moveRight(){
        moveHorizontal(20);
    }
    public void moveLeft(){
        moveHorizontal(-20);
    }
    public void moveUp(){
        moveVertical(-20);
    }
    public void moveDown(){
        moveVertical(20);
    }
    public void moveHorizontal(int distance){
        erase();
        xPosition += distance;
        draw();
    }
    public void moveVertical(int distance){
        erase();
        yPosition += distance;
        draw();
    }
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
    public void changeSize(int newHeight, int newWidth) {
        erase();
        height = newHeight;
        width = newWidth;
        draw();
    }
    public void changeColor(String newColor){
        color = newColor;
        draw();
    }
    private void draw() {
        if(isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.draw(this, color,
                new java.awt.Rectangle(xPosition, yPosition,width, height));
            canvas.wait(10);
        }
    }
    private void erase(){
        if(isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.erase(this);
        }
    }
}
