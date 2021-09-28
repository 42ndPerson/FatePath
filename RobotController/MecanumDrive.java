public class MecanumDrive {
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

  private abstract static class MecanumWheelDescriptors {
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
  public static class MecanumWheelPowers extends MecanumWheelDescriptors {
    public MecanumWheelPowers() { super() };
    public MecanumWheelPowers(double frontLeft, double frontRight, double backLeft, double backRight) { super(frontLeft, frontRight, backLeft, backRight); }
  }
  public static class MecanumWheelMovement extends MecanumWheelDescriptors {
    public MecanumWheelMovement() { super() };
    public MecanumWheelMovement(double frontLeft, double frontRight, double backLeft, double backRight) { super(frontLeft, frontRight, backLeft, backRight); }
  }
}