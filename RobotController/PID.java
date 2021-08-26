public class PID {
    private double P, I, D;
    private double integral;
    private double lastTime;
    private double previousError; 
    private double responce;

    public PID(double P, double I, double D, double error) {
        this.P = P;
        this.I = I;
        this.D = D;
        this.integral = 0;
        this.lastTime = System.currentTimeMillis()/1000;
        this.previousError = error;
    }

    public void update(double error) {
        double currentTime = System.currentTimeMillis()/1000;

        this.integral += (currentTime-this.lastTime)*error;
        double derivative = (error-this.previousError) / (currentTime-this.lastTime);
        this.previousError = error;

        this.lastTime = currentTime;

        this.responce = this.P*error + this.I*this.integral + this.D*derivative;
    }

    public double getResponce() {
        return this.responce;
    }
}