package robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import robot.Robot;
import robot.subsystems.Commands.DriveCommand;
import robot.utilities.Point;

public class DrivetrainSubsystem extends Subsystem {

    public TalonSRX leftMaster = new TalonSRX(16);
    public TalonSRX rightMaster = new TalonSRX(11);
    public VictorSPX right1 = new VictorSPX(12);
    public VictorSPX left1 = new VictorSPX(14);
    public VictorSPX right2 = new VictorSPX(13);
    public VictorSPX left2 = new VictorSPX(15);
    public Point currentLocation = new Point(0,0);

    public DrivetrainSubsystem(){
        leftMaster.setInverted(false);
        left1.setInverted(false);
        left2.setInverted(false);
        rightMaster.setInverted(true);
        right1.setInverted(true);
        right2.setInverted(true);

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

    public void updateConstants(){
        DrivetrainConstants.VISION_ANGLE_KP = getConstant("vision angle kp", DrivetrainConstants.VISION_ANGLE_KP);
        DrivetrainConstants.VISION_DISTANCE_KP = getConstant("vision distance kp", DrivetrainConstants.VISION_DISTANCE_KP);
        DrivetrainConstants.VISION_ANGLE_FEEDFORWARD = getConstant("feedforward", DrivetrainConstants.VISION_ANGLE_FEEDFORWARD);

        DrivetrainConstants.ANGLE_KP = getConstant("angle kp", DrivetrainConstants.ANGLE_KP);
        DrivetrainConstants.ANGLE_KI = getConstant("angle ki", DrivetrainConstants.ANGLE_KI);
        DrivetrainConstants.ANGLE_KD = getConstant("angle kd" , DrivetrainConstants.ANGLE_KD);

        DrivetrainConstants.DISTANCE_KP = getConstant("distance kp", DrivetrainConstants.DISTANCE_KP);
        DrivetrainConstants.DISTANCE_KI = getConstant("distance ki", DrivetrainConstants.DISTANCE_KI);
        DrivetrainConstants.DISTANCE_KD = getConstant("distance kd" , DrivetrainConstants.DISTANCE_KD);

        DrivetrainConstants.RAMP = getConstant("RAMP", DrivetrainConstants.RAMP);

    }

    private double getConstant(String key, double value){
        SmartDashboard.putNumber(key,SmartDashboard.getNumber(key,value));
        return SmartDashboard.getNumber(key,value);
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new DriveCommand());
    }

    public void resetLocation(){
        currentLocation.setPoint(0,0);
    }
}
