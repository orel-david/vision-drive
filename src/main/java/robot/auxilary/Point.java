package robot.auxilary;

public class Point {
    private double x,y;

    public Point(double x , double y){
        this.x = x;
        this.y = y;
    }

    /**
     *
     * @return the x value of the point
     */
    public double getX(){
        return x;
    }

    /**
     *
     * @return the y value of the point
     */
    public double getY(){
        return y;
    }

    /**
     * set the point new x,y values
     * @param x the new x value
     * @param y the new y value
     */
    public void setPoint(double x, double y){
        this.y = y;
        this.x = x;
    }

}
