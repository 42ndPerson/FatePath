import java.lang.Math;

public class BezierPath {
  //BezierCurve
  private Vector2d anchor1;
  private Vector2d anchor2;
  private Vector2d control1;
  private Vector2d control2;
  //Path Controls
  private double power;
  private double rotation;

  //Contructors
  public BezierPath(Vector2d anchor1, Vector2d anchor2, Vector2d control1, Vector2d control2) {
    this.anchor1 = anchor1;
    this.anchor2 = anchor2;
    this.control1 = control1;
    this.control2 = control2;

    this.power = 0.5;
    this.rotation = 0;
  }
  public BezierPath(Vector2d anchor1, Vector2d anchor2, Vector2d control1, Vector2d control2, double power) {
    this.anchor1 = anchor1;
    this.anchor2 = anchor2;
    this.control1 = control1;
    this.control2 = control2;

    this.power = power;
    this.rotation = 0;
  }
  public BezierPath(Vector2d anchor1, Vector2d anchor2, Vector2d control1, Vector2d control2, double power, double rotation) {
    this.anchor1 = anchor1;
    this.anchor2 = anchor2;
    this.control1 = control1;
    this.control2 = control2;

    this.power = power;
    this.rotation = rotation;
  }

  //Setter Methods
  public void setanchor1(Vector2d newAnchor1) {
    this.anchor1 = newAnchor1;
  }
  public void setanchor2(Vector2d newAnchor2) {
    this.anchor2 = newAnchor2;
  }
  public void setControl1(Vector2d newControl1) {
    this.control1 = newControl1;
  }
  public void setControl2(Vector2d newControl2) {
    this.control2 = newControl2;
  }
  public void setpower(double newPower) {
    if (newPower < -1) {
      this.power = -1;
    } else if (newPower > 1) {
      this.power = 1;
    } else {
      this.power = newPower;
    }
  }
  public void setRotation(double newRotation) {
    this.rotation = newRotation;
  }
  public void overwrite(BezierPath newPath) {
    this.anchor1 = newPath.anchor1;
    this.anchor2 = newPath.anchor2;
    this.control1 = newPath.control1;
    this.control2 = newPath.control2;

    this.power = newPath.power;
    this.rotation = newPath.rotation;
  }

  //Getter Methods
  public double getPower() {
    return this.power;
  }

  //Calculation Methods
  public Vector2d getPoint(double t) {
    double x = (this.anchor1.getX() * Math.pow((1 - t), 3)) + (this.control1.getX() * 3 * t * Math.pow((1 - t), 2)) + (this.control2.getX() * 3 * Math.pow(t, 2) * (1 - t)) + (this.anchor2.getX() * Math.pow(t, 3));
    double y = (this.anchor1.getY() * Math.pow((1 - t), 3)) + (this.control1.getY() * 3 * t * Math.pow((1 - t), 2)) + (this.control2.getY() * 3 * Math.pow(t, 2) * (1 - t)) + (this.anchor2.getY() * Math.pow(t, 3));
    return new Vector2d(x,y);
  }

  public Vector2d getFirstDerivative(double t) {
    double x = (this.control1.getX() - this.anchor1.getX()) * 3 * Math.pow((1 - t), 2) + (this.control2.getX() - this.control1.getX()) * (1 - t) * 6 * t + (this.anchor2.getX() - this.control2.getX()) * 3 * Math.pow(t, 2);
    double y = (this.control1.getY() - this.anchor1.getY()) * 3 * Math.pow((1 - t), 2) + (this.control2.getY() - this.control1.getY()) * (1 - t) * 6 * t + (this.anchor2.getY() - this.control2.getY()) * 3 * Math.pow(t, 2);
    return new Vector2d(x,y);//(this.control1 - this.anchor1) * 3 * Math.pow((1 - t), 2) + (this.control2 - this.control1) * (1 - t) * 6 * t + (this.anchor2 - this.control2) * 3 * Math.pow(t, 2);
  };
  public Vector2d getSecondDerivative(double t) {
    double x = (this.control2.getX() - this.control1.getX() * 2 + this.anchor1.getX()) * 6 * (1 - t) + (this.anchor2.getX() - this.control2.getX() * 2 + this.control1.getX()) * 6 * t;
    double y = (this.control2.getY() - this.control1.getY() * 2 + this.anchor1.getY()) * 6 * (1 - t) + (this.anchor2.getY() - this.control2.getY() * 2 + this.control1.getY()) * 6 * t;
    return new Vector2d(x,y);//(this.control2 - this.control1 * 2 + this.anchor1) * 6 * (1 - t) + (this.anchor2 - this.control2 * 2 + this.control1) * 6 * t;
  };
  public double getRadiusOfCurvature(double t) {
      Vector2d d1 = this.getFirstDerivative(t);
      Vector2d d2 = this.getSecondDerivative(t);
      double numerator = d1.getX() * d2.getY() - d2.getX() * d1.getY();
      double denominator = Math.pow(Math.pow(d1.getX(), 2) + Math.pow(d1.getY(), 2), 1.5);

      return denominator / numerator;
  };
  public double getLength(double increments) {
      double totalLength = 0;
      Vector2d lastPoint = this.getPoint(0);

      for (int i = 1; i <= increments; i++) {
        Vector2d newPoint = this.getPoint(i/increments);
        totalLength += newPoint.getDistanceTo(lastPoint);
        lastPoint = newPoint;
      }

      return totalLength;
  };
  public BezierPath getPathTo(double t) {
    return new BezierPath(
      this.anchor1,
      this.getPoint(t),
      Vector2d.biasedAverage(this.anchor1, this.control1, t),
      Vector2d.biasedAverage(
        Vector2d.biasedAverage(this.anchor1, this.control1, t),
        Vector2d.biasedAverage(this.control1, this.control2, t),
        t
      ),
      this.power,
      this.rotation
    );
  }
  public BezierPath getPathFrom(double t) {
    return new BezierPath(
      this.getPoint(t),
      this.anchor2,
      Vector2d.biasedAverage(
        Vector2d.biasedAverage(this.control1, this.control2, t),
        Vector2d.biasedAverage(this.control2, this.anchor2, t),
        t
      ),
      Vector2d.biasedAverage(this.control2, this.anchor2, t),
      this.power,
      this.rotation
    );
  }
  public double getAnchor1Angle() {
    double slopeToAngle = Math.PI/2 - Math.atan((this.control1.getY()-this.anchor1.getY())/Math.abs(this.control1.getX()-this.anchor1.getX())); //Get raw angle and convert to north as zero clockwise positive
    double fullCircleAngle = (this.control1.getX() - this.anchor1.getX() < 0 ? -1 : 1) * slopeToAngle; //Account for slopes headed to the left

    return fullCircleAngle;
  }
  public double getAnchor2Angle() {
    double slopeToAngle = Math.PI/2 - Math.atan((this.anchor2.getY()-this.control2.getY())/Math.abs(this.anchor2.getX()-this.control2.getX())); //Get raw angle and convert to north as zero clockwise positive
    double fullCircleAngle = (this.anchor2.getX() - this.control2.getX() < 0 ? -1 : 1) * slopeToAngle; //Account for slopes headed to the left

    return fullCircleAngle;
  }

  //Debug methods
  public void print() {
    System.out.println("Anchor1: ");
    this.anchor1.print();
    System.out.println("Anchor2: ");
    this.anchor2.print();
    System.out.println("Control1: ");
    this.control1.print();
    System.out.println("Control2: ");
    this.control2.print();
    System.out.println("Power: " + String.valueOf(this.power));
    System.out.println("Rotation: " + String.valueOf(this.rotation));
  }
}
