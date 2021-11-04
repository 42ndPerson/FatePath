import java.lang.Math;

public class Vector2d {
  private double x;
  private double y;

  //Constructor
  public Vector2d(double x, double y) {
    this.x = x;
    this.y = y;
  }

  //Getter Methods
  public double getX() {
    return this.x;
  }
  public double getY() {
    return this.y;
  }

  //Object Arithmatic Methods
  public Vector2d add(Vector2d vector) {
    this.x += vector.x;
    this.y += vector.y;

    return this; //Allows for chaining methods
  }
  public Vector2d subtract(Vector2d vector) {
    this.x -= vector.x;
    this.y -= vector.y;

    return this; //Allows for chaining methods
  }
  public Vector2d multiply(double factor) {
    this.x *= factor;
    this.y *= factor;

    return this; //Allows for chaining methods
  }

  //Static Arithmatic Methods
  public static Vector2d add(Vector2d vectorA, Vector2d vectorB) {
    return new Vector2d(vectorA.x+vectorB.x, vectorA.y+vectorB.y);
  }
  public static Vector2d subtract(Vector2d vectorA, Vector2d vectorB) {
    return new Vector2d(vectorA.x-vectorB.x, vectorA.y-vectorB.y);
  }
  public static Vector2d multiply(Vector2d vectorA, double factor) {
    return new Vector2d(factor*vectorA.x, factor*vectorA.y);
  }

  //Computational Methods
  public double getDistanceTo(Vector2d point) {
    return Math.sqrt(Math.pow(this.x - point.x, 2) + Math.pow(this.y - point.y, 2));
  }
  public void translate(double[][] translationMatrix) {
    if (translationMatrix.length == 3 && translationMatrix[0].length == 3) {
      double x = this.x*translationMatrix[0][0] + this.y*translationMatrix[1][0] + translationMatrix[2][0];
      double y = this.x*translationMatrix[0][1] + this.y*translationMatrix[1][1] + translationMatrix[2][1];

      this.x = x;
      this.y = y;
    } else {
      throw new Error("Invalid Translation Matrix.");
    }
  }
  public void rotateAround(Vector2d centerPoint, double radians) {
    this.translate( //Translate coordinates so centerPoint is at the origin
      new double[][]{
        new double[]{1,0,0},
        new double[]{0,1,0},
        new double[]{-centerPoint.getX(),-centerPoint.getY(),1}
      }
    );
    this.translate( //Rotate
      new double[][]{
        new double[]{Math.cos(radians),-Math.sin(radians),0},
        new double[]{Math.sin(radians),Math.cos(radians),0},
        new double[]{0,0,1}
      }
    );
    this.translate( //Translate coordinates so centerPoint is at the back at its original position
      new double[][]{
        new double[]{1,0,0},
        new double[]{0,1,0},
        new double[]{centerPoint.getX(),centerPoint.getY(),1}
      }
    );
    //this.print();
  }
  public void translateDistAtAngle(double distance, double radians) {
    this.translate( //Translate coordinates so centerPoint is at the origin
      new double[][]{
        new double[]{1,0,0},
        new double[]{0,1,0},
        new double[]{0,distance,1}
      }
    );
    this.translate( //Rotate
      new double[][]{
        new double[]{Math.cos(radians),-Math.sin(radians),0},
        new double[]{Math.sin(radians),Math.cos(radians),0},
        new double[]{0,0,1}
      }
    );
  }

  //Average
  public static Vector2d biasedAverage(Vector2d pointA, Vector2d pointB, double bias) {
    return Vector2d.add(Vector2d.multiply(pointA, 1-bias), Vector2d.multiply(pointB, bias));
  }

  //Utility
  public Vector2d clone() {
    return new Vector2d(this.x,this.y);
  }

  //Debug
  public void print() {
    System.out.println(String.valueOf(this.x) + "," + String.valueOf(this.y));
    //System.out.println("X: " + String.valueOf(x));
    //System.out.println("Y: " + String.valueOf(y));
  }
}
