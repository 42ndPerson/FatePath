import java.util.Arrays;

class BezierSplinePath {
  private String name;
  private BezierPath[] paths;

  //Constructor
  public BezierSplinePath(String name, BezierPath[] paths) {
    this.name = name;
    this.paths = paths;
  }

  //Setter Methods
  public void setName(String newName) {
    this.name = newName;
  }
  public void setPaths(BezierPath[] newPaths) {
    this.paths = newPaths;
  }
  public void appendPath(BezierPath newPath) {
    this.paths = Arrays.copyOf(this.paths, this.paths.length+1);
    this.paths[this.paths.length-1] = newPath;
  }

  //Getter Methods
  public BezierPath[] getPaths() {
    return this.paths;
  }
  public boolean hasPathAt(int index) {
    return (this.paths.length < index);
  }
}
