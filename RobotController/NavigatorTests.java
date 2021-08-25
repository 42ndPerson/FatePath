import java.lang.Math;

class NavigatorTests {
  public static void main(String[] args) {
    Vector2d a = new Vector2d(5,12.19);
    Vector2d b = new Vector2d(1,0);

    //Vector2d.biasedAverage(a, b, 0.87).print();

    BezierPath path1 = new BezierPath(
      new Vector2d(5,12.19),
      new Vector2d(6.3593,4.7058),
      new Vector2d(4.8425,0.8452),
      new Vector2d(0.7665,4.5747),
      0.5,
      0
    );
    BezierPath path2 = new BezierPath(
      new Vector2d(6.3593,4.7058),
      new Vector2d(5.8923,9.9724),
      new Vector2d(7.7874,4.7394),
      new Vector2d(1.1655,9.6136),
      0.5,
      0
    );
    BezierPath path3 = new BezierPath(
      new Vector2d(5.8923,9.9724),
      new Vector2d(7.4932,1.7900),
      new Vector2d(7.3204,9.7810),
      new Vector2d(8.7503,2.1602),
      0.5,
      0
    );
    //System.out.println(path1.getAnchor1Angle());
    //path1.getPoint(0).print();

    BezierSplinePath spline = new BezierSplinePath("A", new BezierPath[]{path1,path2,path3});

    Navigator.TankDrive tank = new Navigator.TankDrive(1, new Navigator.TankDrive.TankWheelPowers(0,0), 0, a, spline);

    BezierPath pathA = new BezierPath(
      new Vector2d(5,5),
      new Vector2d(3,3),
      new Vector2d(8,4),
      new Vector2d(4,3),
      0.5,
      0
    );
    System.out.println(pathA.getAnchor1Angle());
    pathA.getFirstDerivative(0).print();

    /*while (true) {
      tank.testUpdate();
    }*/
    //tank.turnAtRadiusAtPower(0, 0.5);
    /*System.out.println(tank.odometryPosUpdate(new Navigator.TankDrive.TankWheelMovement(-0.2,0.2)));
    tank.getPosition().print();
    System.out.println(tank.getRotation());*/
    //a.translate(new double[][]{ new double[]{0,1,0}, new double[]{-1,0,0}, new double[]{0,0,1} });

    /*
    BezierPath path1 = new BezierPath(
      new Vector2d(90,200),
      new Vector2d(210,240),
      new Vector2d(25,100),
      new Vector2d(220,40),
      0.5,
      0
    );

    path1.print();

    path1.getPoint(0.5).print();

    path1.getPathTo(0.5).print();

    System.out.println("--------");

    BezierPath path2 = new BezierPath(
      new Vector2d(400,140),
      new Vector2d(750,165),
      new Vector2d(645,165),
      new Vector2d(645,70),
      0.5,
      0
    );

    System.out.println(path2.getRadiusOfCurvature(0.5));*/


    /*
    //double a = 5;
    //Vector2d b = new Vector2d(2,3);
    //System.out.println(b.x);
    BezierPath p = new BezierPath(new Vector2d(0,0), new Vector2d(4,5), new Vector2d(2,3), new Vector2d(1,2), 0.5, true);
    System.out.println(p.getLength(15));
    System.out.print(p.getPoint(0.3984).x);
    System.out.print(" ");
    System.out.println(p.getPoint(0.3984).y);
    System.out.println(p.getRadiusOfCurvature(0.3984));
    System.out.println(p.getFirstDerivative(0.3984).y);
    /*MecanumWheelPowers test = new MecanumWheelPowers();
    System.out.print(p.getPoint(0.5).x);
    System.out.print(" ");
    System.out.print(p.getPoint(0.5).y);*/
  }
}
/*
class Navigator { //Add public
  public class MecanumDrive {
    double horizontalWheelBase;
    double wheelCircumference;
    MecanumWheelPowers wheelPowers = new MecanumWheelPowers();

    BezierSplinePath splinePath = null;
    int currentPath = 0;
    double currentT = 0;
    boolean drive = false;

    MecanumDrive(double horizontalWheelBase, double wheelCircumference) {
      this.horizontalWheelBase = horizontalWheelBase;
      this.wheelCircumference = wheelCircumference;
    }

    void move(double angle, double power) {
      double radiansAngle = Math.toRadians(angle);
      double negativeAxis = Math.sin(radiansAngle - (Math.PI));
      double positiveAxis = Math.sin(radiansAngle + (Math.PI));

      this.wheelPowers.frontLeft = negativeAxis;
      this.wheelPowers.backRight = negativeAxis;
      this.wheelPowers.frontRight = positiveAxis;
      this.wheelPowers.backLeft = positiveAxis;
    }

    void drive() {
      this.drive = true;
    }

    void increment()
  }
}

class MecanumWheelPowers {
  public double frontLeft;
  public double frontRight;

  public double backLeft;
  public double backRight;

  void MecanumWheelPowers() {
    this.frontLeft = 0;
    this.frontRight = 0;
    this.backLeft = 0;
    this.backRight = 0;
  }
}

class Vector2d {
  double x;
  double y;

  Vector2d(double x, double y) {
    this.x = x;
    this.y = y;
  }

  double getDistanceTo(Vector2d point) {
    return Math.sqrt(Math.pow(this.x - point.x, 2) + Math.pow(this.y - point.y, 2));
  }
}

class BezierPath {
  Vector2d anchor1;
  Vector2d anchor2;
  Vector2d control1;
  Vector2d control2;

  double power;
  boolean forward;

  BezierPath(Vector2d anchor1, Vector2d anchor2, Vector2d control1, Vector2d control2, double power, boolean forward) {
    System.out.println(control2);
    this.anchor1 = anchor1;
    this.anchor2 = anchor2;
    this.control1 = control1;
    this.control2 = control2;

    this.power = power;
    this.forward = forward;
  }

  Vector2d getPoint(double t) {
    double x = (this.anchor1.x * Math.pow((1 - t), 3)) + (this.control1.x * 3 * t * Math.pow((1 - t), 2)) + (this.control2.x * 3 * Math.pow(t, 2) * (1 - t)) + (this.anchor2.x * Math.pow(t, 3));
    double y = (this.anchor1.y * Math.pow((1 - t), 3)) + (this.control1.y * 3 * t * Math.pow((1 - t), 2)) + (this.control2.y * 3 * Math.pow(t, 2) * (1 - t)) + (this.anchor2.y * Math.pow(t, 3));
    return new Vector2d(x,y);
  }

  Vector2d getFirstDerivative(double t) {
    double x = (this.control1.x - this.anchor1.x) * 3 * Math.pow((1 - t), 2) + (this.control2.x - this.control1.x) * (1 - t) * 6 * t + (this.anchor2.x - this.control2.x) * 3 * Math.pow(t, 2);
    double y = (this.control1.y - this.anchor1.y) * 3 * Math.pow((1 - t), 2) + (this.control2.y - this.control1.y) * (1 - t) * 6 * t + (this.anchor2.y - this.control2.y) * 3 * Math.pow(t, 2);
    return new Vector2d(x,y);//(this.control1 - this.anchor1) * 3 * Math.pow((1 - t), 2) + (this.control2 - this.control1) * (1 - t) * 6 * t + (this.anchor2 - this.control2) * 3 * Math.pow(t, 2);
  };

  Vector2d getSecondDerivative(double t) {
    double x = (this.control2.x - this.control1.x * 2 + this.anchor1.x) * 6 * (1 - t) + (this.anchor2.x - this.control2.x * 2 + this.control1.x) * 6 * t;
    double y = (this.control2.y - this.control1.y * 2 + this.anchor1.y) * 6 * (1 - t) + (this.anchor2.y - this.control2.y * 2 + this.control1.y) * 6 * t;
    return new Vector2d(x,y);//(this.control2 - this.control1 * 2 + this.anchor1) * 6 * (1 - t) + (this.anchor2 - this.control2 * 2 + this.control1) * 6 * t;
  };

  double getRadiusOfCurvature(double t) {
      Vector2d d1 = this.getFirstDerivative(t);
      Vector2d d2 = this.getSecondDerivative(t);
      double numerator = d1.x * d2.y - d2.x * d1.y;
      double denominator = Math.pow(Math.pow(d1.x, 2) + Math.pow(d1.y, 2), 1.5);

      return denominator / numerator;
  };

  double getLength(double increments) {
      double totalLength = 0;
      Vector2d lastPoint = this.getPoint(0);

      for (int i = 1; i <= increments; i++) {
        Vector2d newPoint = this.getPoint(i/increments);
        totalLength += newPoint.getDistanceTo(lastPoint);
        lastPoint = newPoint;

          //total_length += pow(pow((curve_points[i].x - curve_points[i + 1].x), 2) + pow((curve_points[i].y - curve_points[i + 1].y), 2), 0.5);
      };
      /*
      //Get points for increments and store them in curve_points
      for (i = 0; i <= increments; i++) {
          curve_points.push_back(get_point(i/increments));
      };

      //Calculate distance between all points in curve_points and sum them
      for (i = 0; i < (increments - 1); i++) {
          total_length += pow(pow((curve_points[i].x - curve_points[i + 1].x), 2) + pow((curve_points[i].y - curve_points[i + 1].y), 2), 0.5);
      };

      return totalLength;
  };
}

class BezierSplinePath {
  String name;
  BezierPath[] paths;

  BezierSplinePath(String name, BezierPath[] paths) {
    this.name = name;
    this.paths = paths;
  }
}
*/
