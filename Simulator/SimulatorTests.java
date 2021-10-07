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
            new Vector2d(50,50),
            new Vector2d[]{
                new Vector2d(7,7),
                new Vector2d(14,14),
                new Vector2d(21,21),
                new Vector2d(28,28)
            },
            0.0,
            false
        );
        canvas.appendToDrawableStack(square);
        
        frame.getContentPane().add(canvas);

        frame.setVisible(true);
    }
}