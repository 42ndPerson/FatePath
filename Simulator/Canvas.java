import java.util.Arrays;
import java.awt.*;
import javax.swing.*;

public class Canvas extends JPanel {
    private DrawableObject[] drawableStack = {};

    @Override
    public void paintComponent(Graphics g) {
        for (DrawableObject drawable : this.drawableStack) {
            System.out.println("y:" + drawable.getYCoords()[0]);
            System.out.println("x:" + drawable.getXCoords()[0]);
            drawable.getYCoords()
            System.out.println("y:" + drawable.getYCoords()[0]);
            System.out.println("x:" + drawable.getXCoords()[0]);
            if (drawable.isFilled()) {
                g.drawPolygon(drawable.getYCoords(), drawable.getXCoords(), drawable.getPointCount()); //X and Y are flipped to match coordinates of GUI
            }
            else {
                g.drawPolyline(drawable.getYCoords(), drawable.getXCoords(), drawable.getPointCount()); //X and Y are flipped to match coordinates of GUI
            }
        }
    }

    public DrawableObject[] getDrawableStack() { return this.drawableStack; }
    public void setDrawableStack(DrawableObject[] drawableStack) { this.drawableStack = drawableStack; }
    public void appendToDrawableStack(DrawableObject object) {
        this.drawableStack = Arrays.copyOf(this.drawableStack, this.drawableStack.length+1);
        this.drawableStack[this.drawableStack.length-1] = object;
    }
}