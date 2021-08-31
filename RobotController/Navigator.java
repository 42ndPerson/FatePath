public class Navigator {
  public static class NavigationState {
    private int pathComponent;
    private double curveT;
    private boolean isTurning;

    public NavigationState() {
      this.pathComponent = 0;
      this.curveT = 0;
      this.isTurning = true;
    }

    //Setter
    public void incrementPathComponent() {
      this.pathComponent++;
    }
    public void incrementCurveT(double increment) {
      this.curveT += increment;
    }
    public void zeroCurveT() {
      this.curveT = 0;
    }
    public void toggleTurningState() {
      this.isTurning = this.isTurning ? false : true;
    }
    public void setTurningState(boolean newTurningState) {
      this.isTurning = newTurningState;
    }

    //Getter
    public int getPathComponent() {
      return this.pathComponent;
    }
    public double getCurveT() {
      return this.curveT;
    }
    public boolean getTurningState() {
      return this.isTurning;
    }
  }

  public static class TankDrive {
    private double wheelbase;
    private TankWheelPowers wheelPowers;
    private double rotation; //In radians; 0 is directly ahead, positive is to the right, negative is to the left
    private Vector2d position;

    private BezierSplinePath path;
    private Navigator.NavigationState navState = new Navigator.NavigationState();

    private PIDController.PIDInfo pidInfo;
    private PIDController pidController;

    //Constructors
    public TankDrive(double wheelbase, TankWheelPowers wheelPowers, PIDController.PIDInfo pidInfo) {
      this.wheelbase = wheelbase;
      this.wheelPowers = wheelPowers;
      this.rotation = 0;
      this.position = new Vector2d(0,0);
      this.path = null;
      this.pidInfo = pidInfo;
    }
    public TankDrive(double wheelbase, TankWheelPowers wheelPowers, double rotation, Vector2d position, PIDController.PIDInfo pidInfo) {
      this.wheelbase = wheelbase;
      this.wheelPowers = wheelPowers;
      this.rotation = rotation;
      this.position = position;
      this.path = null;
      this.pidInfo = pidInfo;
    }
    public TankDrive(double wheelbase, TankWheelPowers wheelPowers, BezierSplinePath path, PIDController.PIDInfo pidInfo) {
      this.wheelbase = wheelbase;
      this.wheelPowers = wheelPowers;
      this.rotation = 0;
      this.position = new Vector2d(0,0);
      this.path = path;
      this.pidInfo = pidInfo;
    }
    public TankDrive(double wheelbase, TankWheelPowers wheelPowers, double rotation, Vector2d position, BezierSplinePath path, PIDController.PIDInfo pidInfo) {
      this.wheelbase = wheelbase;
      this.wheelPowers = wheelPowers;
      this.rotation = rotation;
      this.position = position;
      this.path = path;
      this.pidInfo = pidInfo;
    }

    //Setter and Getter Methods
    public double getWheelBase() {
      return this.wheelbase;
    }
    public TankWheelPowers getWheelPowers() {
      return this.wheelPowers;
    }
    public double getRotation() {
      return this.rotation;
    }
    public Vector2d getPosition() {
      return this.position;
    }
    private void setWheelPowers(TankWheelPowers newWheelPowers) {
      this.wheelPowers = newWheelPowers;
    }
    private void setRotation(double newRotation) {
      this.rotation = newRotation;
    }
    private void setPosition(Vector2d newPosition) {
      this.position = newPosition;
    }

    //Calculation Methods
    public double odometryPosUpdate(TankWheelMovement wheelMovement) {
      double distanceTraveled = (wheelMovement.left + wheelMovement.right) / 2;
      double turnRadius = this.wheelbase * ((wheelMovement.left / (wheelMovement.right - wheelMovement.left)) + 0.5);

      if (Double.isInfinite(turnRadius)) { //Occurs when wheelMovement moves robot straight
        this.position.translateDistAtAngle(distanceTraveled, this.rotation);
      } else { //Occurs when robot is turning
        double turnRadians = (wheelMovement.left - wheelMovement.right) / this.wheelbase;
        Vector2d turnCenter = new Vector2d(this.position.getX()-turnRadius, this.position.getY()); //Not actually center of turn at creation
        turnCenter.rotateAround(this.position, this.rotation); //Makes turnCenter perpendicular to robot direction at distance of turnRadius

        this.position.rotateAround(turnCenter, turnRadians);
        this.rotation += turnRadians;
      }

      return distanceTraveled; //Returns distance traveled
    }
    public void turnAtRadiusAtPower(double radius, double power) { //Positive radius turns to the right; radius 0 turns right unless power is negative
      //Find wheel power ratio
      double leftRatio = radius + this.wheelbase / 2;
      double rightRatio = radius - this.wheelbase / 2;

      //Normalize
      double left = leftRatio / (Math.abs(leftRatio) < Math.abs(rightRatio) ? rightRatio : leftRatio);
      double right = rightRatio / (Math.abs(leftRatio) < Math.abs(rightRatio) ? rightRatio : leftRatio);

      //Scale
      left *= power;
      right *= power;

      this.wheelPowers.set(left, right);
      System.out.println(left + " " + right);
    }
    public void update(TankWheelMovement wheelMovement) {
      final double distanceEstimator = 0.0001;

      if (this.path != null) { //Check that there is currently a path to navigate
        //Work through steps on path
        if (this.navState.getTurningState()) { //--Look into making it navState.isTurning()
          this.pidController.update(this.path.getPaths()[this.navState.pathComponent].getAnchor1Angle()-this.path.getPaths()[this.navState.pathComponent-1].getAnchor2Angle());  //Update with new error info
          this.wheelPowers = (new TankWheelPowers(-1,1)).multiply(pidController.getResponce); //--Output range of PID might by wrong
        } else {
          double distanceTraveled = this.odometryPosUpdate(wheelMovement);
          double estimatedDistanceTraveled = 0;

          while(true) {
            if (distanceTraveled - estimatedDistanceTraveled < 0) {
              navState.incrementCurveT(distanceEstimator);
              estimatedDistanceTraveled += this.path.getPaths()[navState.getPathComponent()].getPoint(navState.getCurveT()-distanceEstimator).getDistanceTo(this.path.getPaths()[navState.getPathComponent()].getPoint(navState.getCurveT()));
            } else {
              if (this.path.hasPathAt(this.navState.pathComponent+1)) { //Check that there is another path to turn to
                this.navState.setTurningState(true);
              } else { //Path has been navigated
                this.path = null;
              }

              break;
            }
          }

          this.turnAtRadiusAtPower(
            this.path.getPaths()[this.navState.getPathComponent()].getRadiusOfCurvature(this.navState.curveT)
            this.path.getPaths()[this.navState.getPathComponent()].getPower()
          );
        }
      } else { //Stop monvement in case of no path
        this.wheelPowers = new TankWheelPowers(0,0);
      }

      //Work to next path
      if (this.navState.curveT >= 1) {
        this.navState.incrementPathComponent();
        this.navState.zeroCurveT();
        this.navState.setTurningState(true);
        this.pidController = new PIDController(this.pidInfo, this.path.getPaths()[this.navState.pathComponent].getAnchor1Angle()-this.path.getPaths()[this.navState.pathComponent-1].getAnchor2Angle());  //Initialize a PIDController with fresh error info
      }
    }
    public void testUpdate() {
      System.out.println("\u001B[31m" + this.path.getPaths()[navState.getPathComponent()].getRadiusOfCurvature(navState.getCurveT()) + "\u001B[0m");//.getPoint(curveT).print();
      this.turnAtRadiusAtPower(-this.path.getPaths()[navState.getPathComponent()].getRadiusOfCurvature(navState.getCurveT()), this.path.getPaths()[navState.getPathComponent()].getPower());
      navState.incrementCurveT(0.01);
      if (navState.getCurveT() >= 1.0) {
        navState.zeroCurveT();
        navState.incrementPathComponent();
        System.out.println("Next Curve");
      }
    }

    //Sub Classes
    private static class TankWheelDescriptors {
      private double left;
      private double right;

      //Constructors
      public TankWheelDescriptors() {
        this.left = 0;
        this.right = 0;
      }
      public TankWheelDescriptors(double left, double right) {
        this.left = left;
        this.right = right;
      }

      //Getter and Setter Methods
      public double getLeft() {
        return this.left;
      }
      public double getRight() {
        return this.right;
      }
      public void set(double left, double right) {
        this.left = left;
        this.right = right;
      }

      //Calculation Methods
      public void multiply(double factor) {
        this.left *= factor;
        this.right *= factor;
      }
    }
    public static class TankWheelPowers extends TankWheelDescriptors {
      public TankWheelPowers() { super(); }
      public TankWheelPowers(double left, double right) { super(left, right); }
    }
    public static class TankWheelMovement extends TankWheelDescriptors {
      public TankWheelMovement() { super(); }
      public TankWheelMovement(double left, double right) { super(left, right); }
    }

  }

  public class MecanumDirve {
    double horizontalWheelBase;
    MecanumWheelPowers wheelPowers;

    public MecanumDrive(double horizontalWheelBase) {
      this.horizontalWheelBase = horizontalWheelBase;
      this.wheelPowers = MecanumWheelPowers();
    }

    void move(double angle, double power) {
      double radiansAngle = Math.toRadians(angle);
      double negativeAxis = Math.sin(radiansAngle - (Math.PI));
      double positiveAxis = Math.sin(radiansAngle + (Math.PI));

      this.wheelPowers.frontLeft, this.wheelPowers.backRight = negativeAxis;
      this.wheelPowers.frontRight, this.wheelPowers.backLeft = positiveAxis;
    }

    private static class MecanumWheelDescriptors {
      private double frontLeft;
      private double frontRight;
      private double backLeft;
      private double backRight;

      //Constructors
      public MecanumWheelDescriptors() {
        this.frontLeft = 0;
        this.frontRight = 0;
        this.backLeft = 0;
        this.backRight = 0;
      }
      public MecanumWheelDescriptors(double frontLeft, double frontRight, double backLeft, double backRight) {
        this.frontLeft = frontLeft;
        this.frontRight = frontRight;
        this.backLeft = backLeft;
        this.backRight = backRight;
      }

      //Getter and Setter Methods
      public double getFrontLeft() {
        return this.frontLeft;
      }
      public double getFrontRight() {
        return this.frontRight;
      }
      public double getBackLeft() {
        return this.backLeft;
      }
      public double getBackRight() {
        return this.backRight;
      }
      public void set(double frontLeft, double frontRight, double backLeft, double backRight) {
        this.frontLeft = frontLeft;
        this.frontRight = frontRight;
        this.backLeft = backLeft;
        this.backRight = backRight;
      }

      //Calculation Methods
      public void multiply(double factor) {
        this.frontLeft *= factor;
        this.frontRight *= factor;
        this.backLeft *= factor;
        this.backRight *= factor;
      }
    }
  }
}
