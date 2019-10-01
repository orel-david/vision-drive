package robot.subsystems.Commands;

import com.stormbots.MiniPID;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.command.Command;
import robot.Robot;
import robot.auxilary.Point;
import robot.subsystems.DrivetrainConstants;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class LatencyVisionDrive extends Command {
    private double lastRightDistance, lastLeftDistance = 0;
    private int direction;
    private double distance;
    private List<Point> positionsList = new ArrayList<>();
    public MiniPID angleMiniPID = new MiniPID(DrivetrainConstants.ANGLE_KP, DrivetrainConstants.ANGLE_KI, DrivetrainConstants.ANGLE_KD);
    public MiniPID distanceMiniPID = new MiniPID(DrivetrainConstants.DISTANCE_KP, DrivetrainConstants.DISTANCE_KI, DrivetrainConstants.DISTANCE_KD);
    private NetworkTableEntry distanceEntry = Robot.visionTable.getEntry("distance");
    private NetworkTableEntry angleEntry = Robot.visionTable.getEntry("angle");
    private NetworkTableEntry latencyEntry = Robot.visionTable.getEntry("latency");
    private double targetDistance;
    private double distanceError;
    private double angleError;
    private double distanceOutput;
    private double angleOutput;


    public LatencyVisionDrive(double targetDistance) {
        requires(Robot.drivetrain);
        angleMiniPID.setOutputLimits(-0.25,0.25);
        distanceMiniPID.setOutputLimits(-0.75,0.75);
        this.targetDistance = targetDistance;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        updatePoint();
        scalePositionsList();

        //get the robot position when the robot recognized the target
        int latency = (int)(latencyEntry.getDouble(0)/20);
        Point positionBeforeLatency = positionsList.get(positionsList.size() - latency);
        double previousAngle = angleEntry.getDouble(0);

        distanceError = targetDistance - distanceEntry.getDouble(0);

        //calculate the angle after the robot drove a certain distance because of the delay
        angleError = Math.atan((distanceEntry.getDouble(0.1)*Math.sin(previousAngle)-(Robot.drivetrain.currentLocation.getX()-positionBeforeLatency.getX()))
                /distanceEntry.getDouble(0.1)*Math.cos(previousAngle)-(Robot.drivetrain.currentLocation.getY()-positionBeforeLatency.getY()));

        //calculate the proportional outputs
        //currently this ain't the actual calculation and it would be a PID control with the constants from DrivetrainConstants
        distanceOutput = distanceMiniPID.getOutput(distanceError, targetDistance);
        angleOutput = angleMiniPID.getOutput(angleError , 0);

        //set the output for each motor
        Robot.drivetrain.setRightSpeed(distanceOutput + angleOutput);
        Robot.drivetrain.setLeftSpeed(distanceOutput - angleOutput);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Math.abs(angleError) < 2// if the angle mistake is smaller than 2 degrees
                && Math.abs(distanceError) < 0.3;//and the distance mistake is smaller than 0.3 meters
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
// subsystems is scheduled to run
    protected void interrupted() {
    }

    /**
     * calculate the robot current position
     */
    public void updatePoint(){
        distance = ((Robot.drivetrain.getLeftDistance()- lastLeftDistance)+(Robot.drivetrain.getRightDistance()- lastRightDistance))/2;
        direction =  (Robot.drivetrain.getLeftVelocity()+Robot.drivetrain.getRightDistance()/2)>0 ?  1: -1;

        double x = Robot.drivetrain.currentLocation.getX()+ distance*direction*Math.sin(Robot.drivetrain.getAngle()*(Math.PI / 180.0));
        double y = Robot.drivetrain.currentLocation.getX()+ distance*direction*Math.cos(Robot.drivetrain.getAngle()*(Math.PI / 180.0));

        Robot.drivetrain.currentLocation.setPoint(x,y);

        lastRightDistance = Robot.drivetrain.getRightDistance();
        lastLeftDistance = Robot.drivetrain.getLeftDistance();

        positionsList.add(new Point(x,y));
    }

    /**
     * limit the amount of points that are being stored to 10
     */
    public void scalePositionsList(){
        if(positionsList.size()>10){
            positionsList.remove(0);
        }
    }
}