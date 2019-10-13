package robot.subsystems;

public class DrivetrainConstants {
    public static final double TICKS_PER_METER = 256 / (4*0.0254*Math.PI);
    public static final double MAX_VEL = 3;// in m/s
    public static final double TIME_STEP = 0.02;
    public static final double MAX_ACCELERATION = 0.4;// in m/s^2 (currently not the correct number)
    public static final int MAX_CURRENT = 35;

    //constants for proportional vision drive
    public static final double VISION_DISTANCE_KP =0.2;
    public static final double VISION_ANGLE_KP =0.2;

    //constants for PID vision drive
    public static final double DISTANCE_KP = 0.2;
    public static final double DISTANCE_KI = 0;
    public static final double DISTANCE_KD = 0;
    public static final double ANGLE_KP = 0.17;
    public static final double ANGLE_KI = 0;
    public static final double ANGLE_KD = 0.6;
}
