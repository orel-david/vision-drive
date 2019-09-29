package robot.subsystems.Commands;

import com.stormbots.MiniPID;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.command.Command;
import robot.Robot;
import robot.subsystems.DrivetrainConstants;


/**
 *
 */
public class PIDvisionDrive extends Command {


    public MiniPID angleMiniPID = new MiniPID(DrivetrainConstants.ANGLE_KP, DrivetrainConstants.ANGLE_KI, DrivetrainConstants.ANGLE_KD);
    public MiniPID distanceMiniPID = new MiniPID(DrivetrainConstants.DISTANCE_KP, DrivetrainConstants.DISTANCE_KI, DrivetrainConstants.DISTANCE_KD);
    private NetworkTableEntry distanceEntry = Robot.visionTable.getEntry("distance");
    private NetworkTableEntry angleEntry = Robot.visionTable.getEntry("angle");
    private double targetDistance;
    private double distanceError;
    private double angleError;
    private double distanceOutput;
    private double angleOutput;


    public PIDvisionDrive(double targetDistance) {
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
        distanceError = targetDistance - distanceEntry.getDouble(0);
        angleError = angleEntry.getDouble(0);

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
}