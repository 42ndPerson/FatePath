import java.awt.*;
import javax.swing.*;

public class SimulatorTests {
    public static void main(String[] args) {
        //Create window
        JFrame frame = new JFrame();
        frame.setTitle("Simulator");
        frame.setSize(600,600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Canvas canvas = new Canvas();
        DrawableObject square = new DrawableObject(
            new Vector2d(200,200),
            new Vector2d[]{
                new Vector2d(3*7,3*7),
                new Vector2d(-3*7,3*7),
                new Vector2d(-3*7,-3*7),
                new Vector2d(3*7,-3*7)
            },
            13.0,
            true
        );
        canvas.appendToDrawableStack(square);
        
        frame.getContentPane().add(canvas);

        frame.setVisible(true);

        canvas.getDrawableStack()[0].setRotation(0.3);

        frame.repaint();

        /*for (double i = 0; i < 21; i += 0.5) {
            canvas.getDrawableStack()[0].setRotation(i);

            frame.repaint();
        }*/
    }
}