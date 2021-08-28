public class PIDController {
    private PIDInfo pidInfo;
    private double integral;
    private double lastTime;
    private double previousError; 
    private double responce;

    public PIDController(PIDInfo pidInfo, double error) { //A PIDController is only to be initialized where it will be used, as error input at initialization must be accurate
        this.pidInfo = pidInfo;
        this.integral = 0;
        this.lastTime = System.currentTimeMillis()/1000;
        this.previousError = error;
    }

    //Getter Method
    public double getResponce() {
        return this.responce;
    }

    //Calculation Method
    public void update(double error) {
        double currentTime = System.currentTimeMillis()/1000;

        this.integral += (currentTime-this.lastTime)*error;
        double derivative = (error-this.previousError) / (currentTime-this.lastTime);
        this.previousError = error;

        this.lastTime = currentTime;

        this.responce = this.pidInfo.getP()*error + this.pidInfogetI()*this.integral + this.pidInfo.getD()*derivative;
    }

    //Sub-Classes
    public static class PIDInfo {
        private double P, I, D;

        //Constructor
        public PIDInfo(double P, double I, double D) {
            this.P = P;
            this.I = I;
            this.D = D;
        }

        //Setter Methods
        public void setP(double newP) {
            this.P = newP;
        }
        public void setI(double newI) {
            this.I = newI;
        }
        public void setP(double newP) {
            this.I = newI;
        }
        public setPID(double P, double I, double D) {
            this.P = P;
            this.I = I;
            this.D = D;
        }

        //Getter Method
        public double getP() {
            return this.P;
        }
        public double getI() {
            return this.I;
        }
        public double getD() {
            return this.D;
        }
    }
}