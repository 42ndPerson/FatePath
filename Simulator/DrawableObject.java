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
        Vector2d[] absolutePoints = new Vector2d[this.relativePoints.length];
        for (int i = 0; i < this.relativePoints.length; i++) {
            absolutePoints[i] = this.relativePoints[i].clone(); //Must clone points or otherwise subsequent code ment to convert from relative to actual will modify source relative points
        }

        for (int i = 0; i < absolutePoints.length; i++) {
            absolutePoints[i].rotateAround(new Vector2d(0,0), 20);//this.rotation); //Rotate points to reflect object rotation
            absolutePoints[i].add(this.centerPosition); //Find point positions relative to global coordniate system
        }

        return absolutePoints;
    }
    public boolean isFilled() { return this.isFilled; }
    public int[] getXCoords() {
        Vector2d[] absolutePoints = this.getPoints();
        int[] xCoords = new int[this.relativePoints.length];

        for (int i = 0; i < absolutePoints.length; i++) {
            xCoords[i] = (int)absolutePoints[i].getX();
        }

        return xCoords;
    }
    public int[] getYCoords() {
        Vector2d[] absolutePoints = this.getPoints();
        int[] yCoords = new int[this.relativePoints.length];

        for (int i = 0; i < absolutePoints.length; i++) {
            yCoords[i] = (int)absolutePoints[i].getY();
        }

        return yCoords;
    }
    public int getPointCount() { return this.relativePoints.length; }

    //Setter Methods
    public void setCenterPos(Vector2d centerPosition) { this.centerPosition = centerPosition; }
    public void setRotation(double rotation) { this.rotation = rotation; }
    public void setRelativePoints(Vector2d[] relativePoints) { this.relativePoints = relativePoints; }
    public void setIsFilled(boolean isFilled) { this.isFilled = isFilled; }
}