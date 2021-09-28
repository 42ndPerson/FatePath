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
}
