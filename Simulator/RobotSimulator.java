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
                        new Vector2d(2*7,3*7),
                        new Vector2d(-2*7,3*7),
                        new Vector2d(-2*7,-3*7),
                        new Vector2d(2*7,-3*7)
                    },
                    13.0,
                    true
                )
            )
        };
        this.canvas.appendToDrawableStack(this.robots[0].getDrawable());
    }

    public Canvas getCanvas() {
        return this.canvas;
    }
    public void frameUpdate() {
        robots[0].update(); //"this" doesn't work here for some reason
        canvas.repaint();
    }

    public void run() {
        Timer timer = new Timer(16, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameUpdate();
            }
        });
        timer.start();
    }
}