package robot.subsystems.Commands;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.command.Command;
import robot.Robot;
import robot.subsystems.DrivetrainConstants;

/**
 *
 */
public class PIDvisionDrive extends Command {

    public double distanceMiniPID = 0;
    public double angleMiniPID = 0;
    private NetworkTableEntry distanceEntry = Robot.visionTable.getEntry("distance");
    private NetworkTableEntry angleEntry = Robot.visionTable.getEntry("angle");
    private double targetDistance;
    private double distanceError;
    private double angleError;
    private double distanceOutput;
    private double angleOutput;


    public PIDvisionDrive(double targetDistance) {
        requires(Robot.drivetrain);
        this.targetDistance = targetDistance;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        //when the MiniPID would be available , I would add here limits to it's values
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        distanceError = targetDistance - distanceEntry.getDouble(0);
        angleError = angleEntry.getDouble(0);

        //calculate the proportional outputs
        //currently this ain't the actual calculation and it would be a PID control with the constants from DrivetrainConstants
        distanceOutput = distanceError * distanceMiniPID;
        angleOutput = angleError*angleMiniPID;

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