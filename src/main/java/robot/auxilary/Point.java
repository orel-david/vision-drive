package robot.auxilary;

public class Point {
    private double x,y;

    public Point(double x , double y){
        this.x = x;
        this.y = y;
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }

    public void moveY(double y){
        this.y += y;
    }

    public void moveX(double x){
        this.x = x;
    }

    public void setPoint(double x, double y){
        this.y = y;
        this.x = x;
    }

}
