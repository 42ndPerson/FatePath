import java.awt.*;
import javax.swing.*;

public class Canvas extends JPanel {
    private DrawableObject[] drawableStack = {};

    @Override
    public void paintComponent(Graphics g) {
        for (DrawableObject drawable : this.drawableStack) {
            if (drawable.isFilled) {
                g.drawPolygon()
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