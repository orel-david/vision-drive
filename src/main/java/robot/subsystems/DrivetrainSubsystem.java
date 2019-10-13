package robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj.command.Subsystem;
import robot.Robot;
import robot.utilities.Point;

public class DrivetrainSubsystem extends Subsystem {

    public TalonSRX leftMaster = new TalonSRX(3);
    public TalonSRX rightMaster = new TalonSRX(6);
    public VictorSPX right1 = new VictorSPX(7);
    public VictorSPX left1 = new VictorSPX(4);
    public VictorSPX right2 = new VictorSPX(8);
    public VictorSPX left2 = new VictorSPX(5);
    public Point currentLocation = new Point(0,0);

    public DrivetrainSubsystem(){
        leftMaster.setInverted(true);
        left1.setInverted(true);
        left2.setInverted(true);
        rightMaster.setInverted(false);
        right1.setInverted(false);
        right2.setInverted(false);

        right1.follow(rightMaster);
        right2.follow(rightMaster);
        left1.follow(leftMaster);
        left2.follow(leftMaster);

        leftMaster.configPeakCurrentLimit(DrivetrainConstants.MAX_CURRENT);
        rightMaster.configPeakCurrentLimit(DrivetrainConstants.MAX_CURRENT);
    }

    public void setLeftSpeed(double speed){
        leftMaster.set(ControlMode.PercentOutput,speed);
    }

    public void setRightSpeed(double speed){
        rightMaster.set(ControlMode.PercentOutput,speed);
    }

    public double getAngle(){
        return Robot.navx.getAngle();
    }

    public double getLeftDistance(){
        return convertTicksToDistance(leftMaster.getSelectedSensorPosition());
    }

    public double getRightDistance(){
        return convertTicksToDistance(rightMaster.getSelectedSensorPosition());
    }

    public double getRightVelocity(){
        return convertTicksToDistance(rightMaster.getSelectedSensorVelocity())*10;
    }

    public double getLeftVelocity(){
        return convertTicksToDistance(leftMaster.getSelectedSensorVelocity())*10;
    }

    public int convertDistanceToTicks(double distance) {
        return (int) (distance * DrivetrainConstants.TICKS_PER_METER);
    }

    /**
     * because the max input from the joystick is 1 , the joystick input * max velocity is
     * function which represent the relation
     * @param joystickInput the y value from the joystick
     * @return joystick value in m/s
     */
    public double convertJoystickInputToVelocity(double joystickInput){
        return joystickInput*DrivetrainConstants.MAX_VEL;
    }


    /**
     * limit the drivetrain's right side acceleration to a certain acceleration
     * @param desiredVelocity the desired velocity
     * @return the desired velocity if possible, if not the current velocity plus the max acceleration
     */
    public double limitRightAcceleration(double desiredVelocity){

        //Take the attempted acceleration and see if it is too high.
        if (  Math.abs(desiredVelocity -getRightVelocity())/ DrivetrainConstants.TIME_STEP >= DrivetrainConstants.MAX_ACCELERATION){
            return getRightVelocity() + DrivetrainConstants.MAX_ACCELERATION;
        }

        return desiredVelocity;
    }

    /**
     * limit the drivetrain's left side acceleration to a certain acceleration
     * @param desiredVelocity the desired velocity
     * @return the desired velocity if possible, if not the current velocity plus the max acceleration
     */
    public double limitLeftAcceleration(double desiredVelocity){

        //Take the attempted acceleration and see if it is too high.
        if ( Math.abs((desiredVelocity - getLeftVelocity()) / DrivetrainConstants.TIME_STEP) >= DrivetrainConstants.MAX_ACCELERATION){
            return getLeftVelocity() + DrivetrainConstants.MAX_ACCELERATION;
        }

        return desiredVelocity;
    }

    public double convertTicksToDistance(int tick) {
        return tick / DrivetrainConstants.TICKS_PER_METER;
    }

    @Override
    protected void initDefaultCommand() {

    }

    public void resetLocation(){
        currentLocation.setPoint(0,0);
    }
}
