package org.usfirst.frc.team668.robot;

public class RobotMap {
	
	// Talon CAN IDs
	public static final int FLY_WHEEL_CAN_ID = 26;
	public static final int INTAKE_CAN_ID = 25;
	public static final int FRONT_LEFT_CAN_ID = 21;
	public static final int FRONT_RIGHT_CAN_ID = 23;
	public static final int REAR_LEFT_CAN_ID = 22;
	public static final int REAR_RIGHT_CAN_ID = 24;
	public static final int SHOOTER_ANGLE_CAN_ID = 25;
	
	public static final int OPTIC_SENSOR_1_DIGITAL_INPUT_PORT = 0;
	public static final int OPTIC_SENSOR_2_DIGITAL_INPUT_PORT = 1;
	
	public static final int PCM_CAN_ID = 20;
	
	public static final int WHEEL_ID = 0;
	public static final int THROTTLE_ID = 1;
	public static final int OPERATOR_ID = 2;
	
	public static final int INTAKE_PISTON_EXPAND_CHANNEL = 0;
	public static final int INTAKE_PISTON_RETRACT_CHANNNEL = 1;
	public static final int PISTON_SHIFT_RIGHT_EXPAND_CHANNEL = 2;
	public static final int PISTON_SHIFT_RIGHT_RETRACT_CHANNEL = 3;
	public static final int PISTON_SHIFT_LEFT_EXPAND_CHANNEL = 4;
	public static final int PISTON_SHIFT_LEFT_RETRACT_CHANNEL = 5;
	
	public static final boolean MINIMIZE_BUTTON = Robot.joyThrottle.getRawButton(3);
	public static final boolean INTAKE_BUTTON = Robot.joyOp.getRawButton(2);
	public static final boolean REVERSE_BUTTON = Robot.joyOp.getRawButton(3);
	public static final boolean FIRE_BUTTON = Robot.joyOp.getRawButton(3);
	public static final boolean LOWER_INTAKE_BUTTON = Robot.joyOp.getRawButton(7);
	public static final boolean RISE_INTAKE_BUTTON = Robot.joyOp.getRawButton(8);
	public static final boolean STOP_FLYWHEEL_BUTTON = Robot.joyOp.getRawButton(5);
	public static final boolean CLOSE_ANGLE_BUTTON = Robot.joyOp.getRawButton(6);
	public static final boolean FAR_ANGLE_BUTTON = Robot.joyOp.getRawButton(4);
	
	public static final boolean OPTIC_SENSOR_VALUE = Robot.opticSensor1.get();
	public static final boolean OPTIC_SENSOR_VALUE_2 = Robot.opticSensor2.get();
}
