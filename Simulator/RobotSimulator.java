import java.awt.*;
import java.awt.event.*;
import javax.swing.Timer;

public class RobotSimulator {
    private Canvas canvas = new Canvas();
    private Robot[] robots;
    
    public RobotSimulator() {
        this.robots = new Robot[]{ 
            new Robot(
                new DrawableObject(
                    new Vector2d(200,200),
                    new Vector2d[]{
                        new Vector2d(3*7,3*7),
                        new Vector2d(-3*7,3*7),
                        new Vector2d(-3*7,-3*7),
                        new Vector2d(3*7,-3*7)
                    },
                    13.0,
                    true
                )
            )
        };
    }

    public Canvas getCanvas() {
        return this.canvas;
    }

    public void run() {
        Timer timer = new Timer(16, new ActionListener() {
            @Override
            public void actionPreformed(ActionEvent e) {
                this.robots[0].update();
                this.canvas.repaint();
            }
        });
    }
}