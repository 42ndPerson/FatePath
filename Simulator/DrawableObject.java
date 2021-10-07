public class DrawableObject {
    private Vector2d centerPosition;
    private double rotation; //Straight ahead is zero; Positive is clockwise; Radians
    private Vector2d[] relativePoints;
    private boolean isFilled;

    public DrawableObject(Vector2d centerPosition, Vector2d[] relativePoints, double rotation, boolean isFilled) {
        this.centerPosition = centerPosition;
        this.relativePoints = relativePoints;
        this.rotation = rotation;
        this.isFilled = isFilled;
    }

    //Getter Methods
    public Vector2d getCenterPos() { return this.centerPosition; }
    public double getRotation() { return this.rotation; }
    public Vector2d[] getPoints() { 
        Vector2d[] absolutePoints = this.relativePoints;

        for (int i = 0; i < this.absolutePoints.length; i++) {
            this.absolutePoints[i].rotateAround(new Vector2d(0,0), this.rotation); //Rotate points to reflect object rotation
            this.absolutePoints[i].add(this.centerPosition); //Find point positions relative to global coordniate system
        }

        return absolutePoints;
    }
    public boolean isFilled() { return this.isFilled; }

    //Setter Methods
    public void setCenterPos(Vector2d centerPosition) { this.centerPosition = centerPosition; }
    public void setRoation(double rotation) { this.rotation = rotation; }
    public void setRelativePoints(Vector2d[] relativePoints) { this.relativePoints = relativePoints; }
    public void setIsFilled(boolean isFilled) { this.isFilled = isFilled; }
}