public class SimulatorTests {
    public static void main(String[] args) {
        Canvas canvas = new Canvas();
        DrawableObject square = new DrawableObject(
            new Vector2d(50,50),
            new Vector2d[]{
                new Vector2d(7,7),
                new Vector2d(-7,7),
                new Vector2d(-7,-7),
                new Vector2d(7,-7)
            },
            0.5,
            true
        );

        canvas.appendToDrawableStack(square);
        canvas.draw();
    }
}