package robot.subsystems.Commands;

import edu.wpi.first.wpilibj.command.Command;
import robot.Robot;

public class DriveCommand extends Command {



    public DriveCommand(){
        requires(Robot.drivetrain);

    }

    @Override
    protected void initialize() {

    }

    @Override
    protected void execute() {
        Robot.drivetrain.setLeftSpeed(-Robot.m_oi.left.getY());
        Robot.drivetrain.setRightSpeed(-Robot.m_oi.right.getY());
    }

    @Override
    protected boolean isFinished() {
        return Robot.m_oi.xbox.getYButton();
    }

    @Override
    protected void interrupted() {

    }

    @Override
    protected void end() {
        Robot.drivetrain.setRightSpeed(0);
        Robot.drivetrain.setLeftSpeed(0);
    }
}
