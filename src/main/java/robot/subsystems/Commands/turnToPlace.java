package robot.subsystems.Commands;

import com.stormbots.MiniPID;
import edu.wpi.first.wpilibj.command.Command;
import robot.Robot;
import robot.subsystems.DrivetrainConstants;

/**
 *
 */
public class turnToPlace extends Command {

    private double taretAngle;
    public MiniPID angleMiniPID = new MiniPID(DrivetrainConstants.ANGLE_KP, DrivetrainConstants.ANGLE_KI, DrivetrainConstants.ANGLE_KD);
    private double angleOutput;


    public turnToPlace(double taretAngle) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(Robot.drivetrain);
        angleMiniPID.setOutputLimits(-0.4,0.4);
        this.taretAngle = taretAngle;
    }

    // Called just before this Command runs the first time
    protected void initialize() {

    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        angleOutput = angleMiniPID.getOutput(Robot.drivetrain.getAngle() ,taretAngle);
        Robot.drivetrain.setRightSpeed(angleOutput);
        Robot.drivetrain.setLeftSpeed(-angleOutput);
        System.out.println(Robot.drivetrain.getAngle());
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Math.abs(Robot.drivetrain.getAngle())<1;

    }

    // Called once after isFinished returns true
    protected void end() {
        Robot.drivetrain.setRightSpeed(0);
        Robot.drivetrain.setLeftSpeed(0);
    }

    // Called when another command which requires one or more of the same
// subsystems is scheduled to run
    protected void interrupted() {
    }
}