package org.usfirst.frc.team668.robot;

public class RobotMap {
	
	// Talon CAN IDs
	public static final int FLY_WHEEL_CAN_ID = 27;
	public static final int INTAKE_CAN_ID = 26;
	
	public static final int FRONT_LEFT_CAN_ID = 24;
	public static final int FRONT_RIGHT_CAN_ID = 21;
	public static final int REAR_LEFT_CAN_ID = 23;
	public static final int REAR_RIGHT_CAN_ID = 22;
	
	public static final int SHOOTER_ANGLE_CAN_ID = 28;
//	public static final int SHOOTER_ANGLE_TWO_CAN_ID = 2;
	public static final int ARM_CAN_ID = 25;
	public static final int OPTIC_SENSOR_DIGITAL_INPUT_PORT = 0;
	
	public static final int PCM_CAN_ID = 4;
	
	public static final int WHEEL_ID = 0;
	public static final int THROTTLE_ID = 1;
	public static final int OPERATOR_ID = 2;
	
	public static final int INTAKE_PISTON_EXPAND_CHANNEL = 3;
	public static final int INTAKE_PISTON_RETRACT_CHANNNEL = 2;
	public static final int PISTON_SHIFT_EXPAND_CHANNEL = 0;
	public static final int PISTON_SHIFT_RETRACT_CHANNEL = 1;
	
	
	public static final int MINIMIZE_BUTTON = 2; // throttle
	public static final int INTAKE_BUTTON = 2; //4
	public static final int LOB_SHOT_BUTTON = 3;
	public static final int REVERSE_BUTTON = 6;
	public static final int FIRE_BUTTON = 1; // manual
	public static final int FAR_FIRE_BUTTON = 1;
	public static final int LOWER_INTAKE_BUTTON = 7;
	public static final int RISE_INTAKE_BUTTON = 8;
	public static final int STOP_FLYWHEEL_BUTTON = 111; // not needed
	public static final int CLOSE_ANGLE_BUTTON = 5; // manual
	public static final int CLOSE_FIRE_BUTTON = 5;
	public static final int FAR_ANGLE_BUTTON = 3; //manual
	public static final int PORT_BUTTON = 10; //kkk
//	public static final int LOWER_BUTTON = 2;
	public static final int GROUND_BUTTON = 4; //kkk
	public static final int MANUAL_HOOD_BUTTON = 9; //manuAL
	public static final int RETURN_BUTTON = 11;
	public static final int LOW_GEAR_BUTTON = 4; //throttle
	public static final int HIGH_GEAR_BUTTON = 5; //throttle
	public static final int AIM_BUTTON = 3; //throttle
	public static final int LOW_GOAL_BUTTON = 9;
	
	public static final boolean OPTIC_SENSOR_VALUE = Robot.opticSensor.get();
	
	//TELEOP STATE MACHINe
	public static final int INIT_STATE = 1;
	public static final int WAIT_FOR_BUTTON_STATE = 2;
	public static final int LOWER_INTAKE_STATE = 3;
	public static final int INIT_FIRE_STATE = 4;
	public static final int FAR_FIRE_STATE = 5;
	public static final int CLOSE_FIRE_STATE = 6;
	public static final int CLOSE_ANGLE_STATE = 7;
	public static final int FAR_ANGLE_STATE = 8;
	public static final int COLLAPSE_STATE = 9;
	public static final int MANUAL_OVERRIDE_STATE = 10;
	public static final int SET_FAR_PID_STATE = 11;
	public static final int SET_CLOSE_PID_STATE = 13;
	public static final int BALL_CLEAR_STATE = 12;
	public static final int CLOSE_FIRE_INIT_STATE = 14;
	public static final int SHOOT_TIMER_STATE = 15;
	public static final int FAR_FIRE_INIT_STATE = 16;
	public static final int LOW_GOAL_SHOT_STATE = 17;
	public static final int LOB_SHOT_STATE = 18;
	public static final int LOB_SHOT_ANGLE_STATE = 19;
	public static final int DEFAULT_STATE = INIT_STATE;
	public static int currentState = DEFAULT_STATE;
	
	//MANUAL STATE MACHINE
	public static final int MANUAL_CLOSE_ANGLE_STATE = 0;
	public static final int MANUAL_FAR_ANGLE_STATE = 1;
	public static final int MANUAL_CONTROLL_HOOD_ANGLE_STATE = 2;
	public static final int MANUAL_FIRE_STATE = 3;
	public static final int MANUAL_WAIT_FOR_BUTTON_STATE = 4; 
	public static final int MANUAL_COLLAPSE_ANGLE_STATE = 5;
	public static final int MANUAL_BALL_CLEAR_STATE = 6;
	public static final int MANUAL_DEFAULT_STATE = MANUAL_WAIT_FOR_BUTTON_STATE;
	public static int manualState = MANUAL_DEFAULT_STATE;
	
	//HOOD STATE MACHINE
	public static final int HOOD_CLOSE_SHOT_STATE = 0;
	public static final int HOOD_GET_STATE = 1;
	public static final int HOOD_ZERO_STATE = 2;
	public static final int HOOD_MANUAL_FAR_STATE = 3;
	public static final int HOOD_MANUAL_STATE = 4;
	public static final int HOOD_SET_FAR_ANGLE_STATE = 5;
	public static final int HOOD_LOW_GOAL_SHOT_STATE = 6;
	public static final int HOOD_LOB_ANGLE_STATE = 7;
	public static final int HOOD_DEFAULT_STATE = HOOD_ZERO_STATE;
	public static int hoodState = HOOD_DEFAULT_STATE;
	
	//ARM STATE MACHINE
	
	public static final int ARM_WAIT_FOR_BUTTON_STATE = 0;
	public static final int ARM_PORT_STATE = 1;
	public static final int ARM_GROUND_STATE = 2;
	public static final int ARM_DEFAULT_STATE = ARM_WAIT_FOR_BUTTON_STATE;
	public static int armState = ARM_DEFAULT_STATE;
	
	
	public static final int DRIVE_FORWARD_STATE = 0;
	public static final int DRIVE_MORE_STATE = 2;
	public static final int STOP_STATE = 1;
	public static int autonStateForward = DRIVE_FORWARD_STATE;
	
	public static final int DRIVE_FORWARD_SHOOT_STATE = 0;
	public static final int TURN_STATE = 1;
	public static final int AIM_STATE = 2;
	public static final int SPIN_STATE = 3;
	public static final int FIRE_SHOOT_STATE = 4;
	public static final int DONE_STATE = 5;
	public static final int ANGLE_SET_SHOOT_STATE = 7;
	public static final int DRIVE_FORWARD_COMPLETE_STATE = 6;
	public static int autonStateShoot = DRIVE_FORWARD_SHOOT_STATE;
	
	public static final int DRIVE_FORWARD_SPY_STATE = 0;
	public static final int AIM_SPY_STATE = 1;
	public static final int SPIN_SPY_STATE = 2;
	public static final int ANGLE_SET_SPY_STATE = 3;
	public static final int FIRE_SPY_STATE = 4;
	public static final int SPY_DONE_STATE = 5;
	public static int autonSpyState = DRIVE_FORWARD_SPY_STATE;
	
	public static final int POT_ANALOG_INPUT_PORT = 0;
	public static final int ARM_POT_ANALOG_INPUT_PORT = 2;
	public static final int LIMIT_SWITCH_DIGITAL_INPUT = 2;
	public static final int LIMIT_SWITCH_TWO_DIGITAL_INPUT = 3;
	
	public static int autonState;
	public static int autonMode;
	public static final int DRIVE_AND_SHOOT_CAMERA_AUTON = 0;
	public static final int DRIVE_UNDER_BAR_AUTON = 1;
	public static final int STOP_AUTON = 2;
	public static final int DRIVE_TO_DEFENSE_AUTON = 3;
	public static final int SPYBOT_SHOT_AUTON = 4;
	
	//CONSTANTS
	public static final int CONSTANT_SPEED = 7000;
	public static final double HOOD_SPEED = .3;
	public static final int PORT_HEIGHT = 1860;
	public static final int GROUND_HEIGHT = 1990;
	public static final int ARM_ZERO_HEIGHT = 1770;
	
	
	public static final int BANG_DRIVE_RANGE = 100;
	
	public static final double BANG_DRIVE_OUTPUT = .3;
	
	//RANGES
	public static final double ACCEPTABLE_HOOD_RANGE = 1;
	public static final double ACCEPTABLE_ARM_RANGE = 20;
	
	public static final int FAR_ANGLE_VALUE = 3200;
	public static final int CLOSE_ANGLE_VALUE = 3208;
	
	public static final int BRIGHT_FAR_ANGLE_VALUE = 0;
	public static final int BRIGHT_CLOSE_ANGLE_VALUE = 362;
	
	public static final int COLLAPSE_ANGLE_VALUE = 0;
	
	public static final int LOW_GOAL_ANGLE = 3200;
	public static final int BRIGHT_LOW_GOAL_ANGLE = 0;
	
	public static final int MAX_HOOD_POSITION = 0;
	public static final int MIN_HOOD_POSITION = 0;
	
	public static final double FAR_FIRE_SPEED_RANGE = 100;
	public static final double FIRE_INTAKE_SPEED = 1;
	public static final double BALL_WAIT_TIME = 1000;
	public static final int CLOSE_FIRE_SPEED = 7000;
	public static final int FAR_FIRE_SPEED = 7000;
	public static final int LOB_SPEED = 5900;
	
	public static final double ACCEPTABLE_JOYSTICK_RANGE = .2;
	public static final double AZIMUTH_RANGE = .3;
	
	public static final double AIM_SPEED = 0;
	public static final double DRIVE_AND_SHOOT_TURN_SPEED = 0.3;
	
	public static final double FIRE_DISTANCE_1 = 0;
	public static final double FIRE_DISTANCE_2 = 0;
	public static final double FIRE_DISTANCE_3 = 0;
	
	public static final int DRIVE_UNDER_BAR_RIGHT_DISTANCE = 31000;
	public static final int DRIVE_UNDER_BAR_LEFT_DISTANCE = 31000;
	
	public static final int DRIVE_TO_DEFENSE_RIGHT_DISTANCE = 30000;
	public static final int DRIVE_TO_DEFENSE_LEFT_DISTANCE = 30000;
	
	public static final int DRIVE_SPY_DISTANCE = 5000;
	
	public static final int DRIVE_AND_CLOSE_DISTANCE = 2000;
	
	public static final int DRIVE_AND_SHOOT_DISTANCE = 30089;
	
	public static final int TURN_RIGHT_AUTON_DISTANCE = 0;
	public static final int TURN_LEFT_AUTON_DISTANCE = 0;
	
	public static final int LEEROY_JENKINS_DISTANCE = 0;
	public static final int LEEROY_JENKINS_SPEED = 1;
			
}

