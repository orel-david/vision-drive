package robot.subsystems.Commands;

import com.stormbots.MiniPID;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.command.Command;
import robot.Robot;
import robot.subsystems.DrivetrainConstants;


/**
 *
 */
public class PIDVisionDriveOdometry extends Command {

    public MiniPID angleMiniPID = new MiniPID(DrivetrainConstants.ANGLE_KP, DrivetrainConstants.ANGLE_KI, DrivetrainConstants.ANGLE_KD);
    public MiniPID distanceMiniPID = new MiniPID(DrivetrainConstants.DISTANCE_KP, DrivetrainConstants.DISTANCE_KI, DrivetrainConstants.DISTANCE_KD);
    private NetworkTableEntry distanceEntry = Robot.visionTable.getEntry("distance");
    private NetworkTableEntry angleEntry = Robot.visionTable.getEntry("angle");
    private double targetDistance;
    private double visionDistance;
    private double visionAngle;
    private double distanceOutput;
    private double angleOutput;
    private double iRange = 0.4;


    public PIDVisionDriveOdometry(double targetDistance) {
        requires(Robot.drivetrain);
        angleMiniPID.setOutputLimits(-0.25, 0.25);
        distanceMiniPID.setOutputLimits(-0.25, 0.75);

        distanceMiniPID.setDirection(true);
        distanceMiniPID.setOutputRampRate(0.1);
        this.targetDistance = targetDistance;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        updateConstants();
        visionDistance = distanceEntry.getDouble(0);
        visionAngle = angleEntry.getDouble(0);

        if(visionDistance < iRange)
            distanceMiniPID.setI(0);


        //calculate the proportional outputs
        //currently this ain't the actual calculation and it would be a PID control with the constants from DrivetrainConstants
        distanceOutput = distanceMiniPID.getOutput(visionDistance ,targetDistance);
        angleOutput = angleMiniPID.getOutput(Robot.drivetrain.getAngle(), Robot.drivetrain.getAngle() + visionAngle);

        //set the output for each motor
        Robot.drivetrain.setRightSpeed(distanceOutput - angleOutput);
        Robot.drivetrain.setLeftSpeed(distanceOutput + angleOutput);
    }

    private void updateConstants() {
        angleMiniPID.setPID(DrivetrainConstants.ANGLE_KP, DrivetrainConstants.ANGLE_KI, DrivetrainConstants.ANGLE_KD);
        distanceMiniPID.setPID(DrivetrainConstants.DISTANCE_KP, DrivetrainConstants.DISTANCE_KI, DrivetrainConstants.DISTANCE_KD);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Math.abs(visionAngle) < 0.1
                && Math.abs(visionDistance - targetDistance) < 0.1;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
// subsystems is scheduled to run
    protected void interrupted() {
    }
}
