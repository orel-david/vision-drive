package robot.subsystems.Commands;

import edu.wpi.first.wpilibj.command.Command;
import robot.Robot;

/**
 *
 */
public class LatencyVisionDrive extends Command {
    private double lastRightDistance, lastLeftDistance = 0;
    private int direction;
    double distance;



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

    }
}