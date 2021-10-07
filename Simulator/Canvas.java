import java.awt.*;
import javax.swing.*;

public class Canvas extends JPanel {
    private DrawableObject[] drawableStack = {};

    @Override
    public void paintComponent(Graphics g) {
        for (DrawableObject drawable : this.drawableStack) {
            if (drawable.isFilled) {
                g.drawPolygon(drawable.getYCoords(), drawable.getXCoords(), drawable.getPointCount()); //X and Y are flipped to match coordinates of GUI
            }
            else {
                g.drawPolyLine(drawable.getYCoords(), drawable.getXCoords(), drawable.getPointCount()); //X and Y are flipped to match coordinates of GUI
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