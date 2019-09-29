package robot.subsystems.Commands;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.command.Command;
import robot.Robot;
import robot.subsystems.DrivetrainConstants;

public class ProportionalVisionDriveCommand extends Command {

    private NetworkTableEntry distanceEntry = Robot.visionTable.getEntry("distance");
    private NetworkTableEntry angleEntry = Robot.visionTable.getEntry("angle");
    private double targetDistance;
    private double distanceError;
    private double angleError;
    private double distanceOutput;
    private double angleOutput;


    public ProportionalVisionDriveCommand(double targetDistance) {

        requires(Robot.drivetrain);
        this.targetDistance = targetDistance;

    }

    @Override
    // Called just before this Command runs the first time
    protected void initialize() {
    }

    @Override
    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        distanceError = targetDistance - distanceEntry.getDouble(0);
        angleError = angleEntry.getDouble(0);

        //calculate the proportional outputs
        distanceOutput = distanceError * DrivetrainConstants.VISION_DISTANCE_KP;
        angleOutput = angleError * DrivetrainConstants.VISION_ANGLE_KP;

        //set the output for each motor
        Robot.drivetrain.setRightSpeed(distanceOutput + angleOutput);
        Robot.drivetrain.setLeftSpeed(distanceOutput - angleOutput);

    }

    @Override
    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Math.abs(angleError) < 2// if the angle mistake is smaller than 2 degrees
                && Math.abs(distanceError) < 0.3;//and the distance mistake is smaller than 0.3 meters

    }

    @Override
    // Called once after isFinished returns true
    protected void end() {
    }

    @Override
    // Called when another command which requires one or more of the same
// subsystems is scheduled to run
    protected void interrupted() {
    }
}
