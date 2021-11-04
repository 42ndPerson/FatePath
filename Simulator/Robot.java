public class Robot {
    private DrawableObject drawable;

    public Robot(Vector2d pos, DrawableObject drawable) {
        this.drawable = drawable;
    }

    public void update() {
        this.drawable.setCenterPos(Vector2d.add(this.drawable.getCenterPos, new Vector2d(0.5,0.7)));
        this.drawable.setRotation(this.drawable.getRotation() + 0.007);
    }
}