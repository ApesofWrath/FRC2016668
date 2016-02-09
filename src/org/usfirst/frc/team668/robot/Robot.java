

package org.usfirst.frc.team668.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

import java.io.PrintWriter;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.vision.USBCamera;

public class Robot extends IterativeRobot {

	public static Joystick joyWheel, joyThrottle, joyOp;
	public static CANTalon canTalonFlyWheel, canTalonIntake, 
	canTalonFrontLeft, canTalonFrontRight, canTalonRearLeft, canTalonRearRight,
	canTalonIntakeAngle, canTalonShooterAngle, canTalonShooterAngleTwo;
	public static RobotDrive robotDrive;
	public static DigitalInput opticSensor1, opticSensor2,limitSwitch, limitSwitchTwo;
	public static CameraServer server;
	public static PrintWriter system;
	public static DoubleSolenoid intakePiston, shiftPiston;
	public static Compressor compressor;
	public static AnalogInput pot;
	public static NetworkTable table;
	public double distance;
	public int isClose;
	public static int ref = 0;
	public static int lastRef = 100000;
	public static boolean canIntake = true;

	// public static USBCamera camFront = new USBCamera("cam1");
	// public static USBCamera camRear = new USBCamera("cam2");
	public void robotInit() {

		server = CameraServer.getInstance();
		server.setQuality(50);
		server.startAutomaticCapture("cam1");
		//     	 camFront.openCamera();
		//     	 camRear.openCamera();
		
		table = NetworkTable.getTable("SmartDashboard");

		joyWheel = new Joystick(RobotMap.WHEEL_ID);
		joyThrottle = new Joystick(RobotMap.THROTTLE_ID);

		joyOp = new Joystick(RobotMap.OPERATOR_ID);

		canTalonFlyWheel = new CANTalon(RobotMap.FLY_WHEEL_CAN_ID);
		//canTalonTrigger = new CANTalon(26);

		canTalonIntake = new CANTalon(RobotMap.INTAKE_CAN_ID);
		canTalonShooterAngleTwo = new CANTalon(RobotMap.SHOOTER_ANGLE_TWO_CAN_ID);

		canTalonFrontLeft = new CANTalon(RobotMap.FRONT_LEFT_CAN_ID);
		canTalonFrontRight = new CANTalon(RobotMap.FRONT_RIGHT_CAN_ID);
		canTalonRearLeft = new CANTalon(RobotMap.REAR_LEFT_CAN_ID);
		canTalonRearRight = new CANTalon(RobotMap.REAR_RIGHT_CAN_ID);

		canTalonShooterAngle = new CANTalon(RobotMap.SHOOTER_ANGLE_CAN_ID);

		robotDrive = new RobotDrive(canTalonFrontLeft, canTalonRearLeft, canTalonFrontRight, canTalonRearRight);
		robotDrive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
		robotDrive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
		robotDrive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
		robotDrive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);

		opticSensor1 = new DigitalInput(RobotMap.OPTIC_SENSOR_1_DIGITAL_INPUT_PORT);
		opticSensor2 = new DigitalInput(RobotMap.OPTIC_SENSOR_2_DIGITAL_INPUT_PORT);
		limitSwitch = new DigitalInput(RobotMap.LIMIT_SWITCH_DIGITAL_INPUT);
		limitSwitchTwo = new DigitalInput(RobotMap.LIMIT_SWITCH_TWO_DIGITAL_INPUT);
		
		intakePiston = new DoubleSolenoid(RobotMap.INTAKE_PISTON_EXPAND_CHANNEL, RobotMap.INTAKE_PISTON_RETRACT_CHANNNEL);
		shiftPiston = new DoubleSolenoid(RobotMap.PISTON_SHIFT_EXPAND_CHANNEL, RobotMap.PISTON_SHIFT_RETRACT_CHANNEL);
		
		pot = new AnalogInput(RobotMap.POT_ANALOG_INPUT_PORT);
		
		compressor = new Compressor(RobotMap.PCM_CAN_ID);

		compressor.setClosedLoopControl(true);

		SmartDashboard.putString("Status:", " Working");

	}

	public void autonomousInit() {

	}


	public void autonomousPeriodic() {

	}

	public void teleopInit(){

		canTalonShooterAngle.setEncPosition(0);


	}


	public void teleopPeriodic() {

		boolean isMinimize = joyOp.getRawButton(RobotMap.MINIMIZE_BUTTON);
		boolean lowGear = joyOp.getRawButton(RobotMap.HIGH_GEAR_BUTTON);
		boolean highGear = joyOp.getRawButton(RobotMap.LOW_GEAR_BUTTON);
		boolean isIntaking = joyOp.getRawButton(RobotMap.INTAKE_BUTTON);
		boolean isReverse = joyOp.getRawButton(RobotMap.REVERSE_BUTTON);
		boolean isFire = joyOp.getRawButton(RobotMap.FIRE_BUTTON);
		boolean isIntakeLower = joyOp.getRawButton(RobotMap.LOWER_INTAKE_BUTTON);
		boolean isIntakeRise = joyOp.getRawButton(RobotMap.RISE_INTAKE_BUTTON);
		boolean stopFlyWheel = joyOp.getRawButton(RobotMap.STOP_FLYWHEEL_BUTTON);
		boolean closeAngle = joyOp.getRawButton(RobotMap.CLOSE_ANGLE_BUTTON);
		boolean farAngle = joyOp.getRawButton(RobotMap.FAR_ANGLE_BUTTON);
		boolean optic = opticSensor1.get();
		boolean optic2 = opticSensor2.get();
		boolean limit1 = limitSwitch.get();
		boolean limit2 = limitSwitchTwo.get();
		boolean isReturn = joyOp.getRawButton(RobotMap.RETURN_BUTTON);
		boolean isManual = joyOp.getRawButton(RobotMap.MANUAL_BUTTON);
		boolean isCollapse = joyOp.getRawButton(RobotMap.COLLAPSE_BUTTON);
		boolean isLower = joyOp.getRawButton(RobotMap.LOWER_BUTTON);
		boolean manualHood = joyOp.getRawButton(RobotMap.MANUAL_HOOD_BUTTON);
		TeleopStateMachine.stateMachine(optic, optic2, closeAngle, farAngle, isFire, isLower, 
				isCollapse, isManual, isReturn);
		
		//gear shifting code 
		if (lowGear){
			intakePiston.set(DoubleSolenoid.Value.kReverse);
		}
		else if (highGear){
			intakePiston.set(DoubleSolenoid.Value.kForward);
		}
		
		//Drive Code
		if (isMinimize){
			robotDrive.arcadeDrive(joyThrottle.getY()*.6, joyWheel.getX()* .6);
		}
		else{
			robotDrive.arcadeDrive(joyThrottle.getY(), joyWheel.getX());
		}

		//System.out.println(RobotMap.currentState);
		
		
		//controls the state of the intake pistons 
		if (isIntakeLower){
			intakePiston.set(DoubleSolenoid.Value.kForward);
		}
		if (isIntakeRise){
			intakePiston.set(DoubleSolenoid.Value.kReverse); 
		}

		
		//INTAKE SPEED
		//If none of these are true the teleop state machine can take over and use the intake for fire. 
		if (isIntaking && optic && optic2 && (RobotMap.currentState != RobotMap.FAR_FIRE_STATE)
				&& (RobotMap.currentState != RobotMap.CLOSE_FIRE_STATE)){  //TODO: add the sensor
			Intake.spin(.8);
		}
		else if (isReverse  && TeleopStateMachine.canReverse){
			Intake.spit(.8);
		}
		else if((RobotMap.currentState != RobotMap.LOWER_INTAKE_STATE) 
				&& (RobotMap.currentState != RobotMap.FAR_FIRE_STATE)
				&& (RobotMap.currentState != RobotMap.CLOSE_FIRE_STATE)
				&& (RobotMap.manualState != RobotMap.MANUAL_FIRE_STATE)){
			Intake.stop();
		}
		
		//System.out.println(pot.getValue());
		
		if (RobotMap.currentState == RobotMap.MANUAL_OVERRIDE_STATE){
			if (manualHood){
				Shooter.moveHood(-joyOp.getY());
			}
			else{
				Shooter.stopAngle();
			}
		}
		
		distance = table.getNumber("Distance", 0);
		
		System.out.println(distance);
	}
		
		
		
		
		
		
		
		
		
		
		
		
		//Firing
//		else if (isFire){
//			if(isClose == 1){
//				Shooter.spinFlyWheel(.7);
//				Shooter.fire(.8);
//			}
//			else if (isClose == 0){
//				//TODO: Camera assisted firing
//				boolean isDone = Shooter.setPID(0);
//				if (isDone){
//					Shooter.setPID(0);
//					Shooter.fire(.8);
//				}
//			}
//		}
//		else{
//			Shooter.spinFlyWheel(.7);
//			Intake.stop(); //has the same function as Shooter.stop();
//		}
//
//
////		//Shooter angle control
////		if (closeAngle){
////			isClose = 1;
////			boolean done = Shooter.movePID(0);
////			if (done){
////				Shooter.stopAngle();
////			}
////		}
////		if(farAngle){
////			isClose = 0;
////			boolean done1 = Shooter.movePID(0);
////			if(done1){
////				Shooter.stopAngle();
////			}
////		}
//		
////		if (isCollapse){
////			intakePiston.set(DoubleSolenoid.Value.kForward);
////			isClose = 2;
////			boolean done2 = Shooter.movePID(500);
////			if(done2){
////				Shooter.stopAngle();
////			}
////		}
//		System.out.println(canTalonShooterAngle.getEncPosition());
//		if(joyOp.getRawButton(12)){
//			boolean done2 = Shooter.movePID(500);
//			System.out.println(done2);
//			if(done2){
//				Shooter.stopAngle();
//			}
//		}
//		
//		
	

	public void testPeriodic() {
		System.out.println("hello");
	}

}