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
    private double targetDistance;
    private double distanceError;
    private double angleError;
    private double distanceOutput;
    private double angleOutput;


    public LatencyVisionDrive() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
// subsystems is scheduled to run
    protected void interrupted() {
    }

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

    public void scalePositionsList(){
        if(positionsList.size()>10){
            positionsList.remove(0);
        }
    }
}