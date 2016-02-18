

package org.usfirst.frc.team668.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

import java.io.PrintWriter;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
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
	public static DigitalInput opticSensor, limitSwitch, limitSwitchTwo;
	public static CameraServer server;
	public static PrintWriter system;
	public static DoubleSolenoid intakePiston, shiftPiston;
	public static Compressor compressor;
	public static AnalogInput pot;
	public static NetworkTable table;
	public static SendableChooser autonChooser;
	public static PowerDistributionPanel pdp;
	
	public static double distance; //get from network table
	public static double azimuth; //get from network table
	
	public int isClose;
	public static int ref = 0;
	public static int lastRef = 100000;
	public static boolean canIntake = true;
	
	public int target = 1070;

	// public static USBCamera camFront = new USBCamera("cam1");
	// public static USBCamera camRear = new USBCamera("cam2");
	public void robotInit() {

//		server = CameraServer.getInstance();
//		server.setQuality(50);
//		server.startAutomaticCapture("cam2");
		//     	 camFront.openCamera();
		//     	 camRear.openCamera();
		
		table = NetworkTable.getTable("SmartDashboard");

		joyWheel = new Joystick(RobotMap.WHEEL_ID);
		joyThrottle = new Joystick(RobotMap.THROTTLE_ID);

		joyOp = new Joystick(RobotMap.OPERATOR_ID);

		canTalonFlyWheel = new CANTalon(RobotMap.FLY_WHEEL_CAN_ID);
		//canTalonTrigger = new CANTalon(26);

		canTalonIntake = new CANTalon(RobotMap.INTAKE_CAN_ID);
		//canTalonShooterAngleTwo = new CANTalon(RobotMap.SHOOTER_ANGLE_TWO_CAN_ID);

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

		opticSensor = new DigitalInput(RobotMap.OPTIC_SENSOR_DIGITAL_INPUT_PORT);
		limitSwitch = new DigitalInput(RobotMap.LIMIT_SWITCH_DIGITAL_INPUT);
		limitSwitchTwo = new DigitalInput(RobotMap.LIMIT_SWITCH_TWO_DIGITAL_INPUT);
		
		intakePiston = new DoubleSolenoid(RobotMap.PCM_CAN_ID, RobotMap.INTAKE_PISTON_EXPAND_CHANNEL, RobotMap.INTAKE_PISTON_RETRACT_CHANNNEL);
		shiftPiston = new DoubleSolenoid(RobotMap.PCM_CAN_ID, RobotMap.PISTON_SHIFT_EXPAND_CHANNEL, RobotMap.PISTON_SHIFT_RETRACT_CHANNEL);
		
		pdp = new PowerDistributionPanel(20);
		
		pot = new AnalogInput(RobotMap.POT_ANALOG_INPUT_PORT);
		
		compressor = new Compressor(RobotMap.PCM_CAN_ID);

		compressor.setClosedLoopControl(true);

		SmartDashboard.putString("Status:", " Working");
		
		autonChooser = new SendableChooser();
		
		autonChooser.addDefault("Drive and Shoot Camera Autonomous", new Integer(RobotMap.DRIVE_AND_SHOOT_CAMERA_AUTON));
		autonChooser.addObject("Drive Autonomous", new Integer(RobotMap.DRIVE_UNDER_BAR_AUTON));
		autonChooser.addObject("Stop Autonomous", new Integer(RobotMap.STOP_AUTON));
		autonChooser.addObject("Drive to Defense Autonomous", new Integer(RobotMap.DRIVE_TO_DEFENSE_AUTON));
		autonChooser.addObject("Drive and shoot PID Autonomous", new Integer(RobotMap.DRIVE_AND_SHOOT_PID_AUTON));
		
		SmartDashboard.putData("Autonomous Selection: ", autonChooser);
		
//		canTalonFlyWheel.setClosedLoopOutputDirection(true);
		canTalonFlyWheel.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
		canTalonFlyWheel.reverseSensor(true);
		canTalonFlyWheel.configNominalOutputVoltage(+2.0f, -0.0f);
		canTalonFlyWheel.configPeakOutputVoltage(+12.0f, +2.0f);
		//canTalonFlyWheel.configMaxOutputVoltage(0.0);
		canTalonFlyWheel.setProfile(0);
//		canTalonFlyWheel.setF(0);
//		canTalonFlyWheel.setP(0);
//		canTalonFlyWheel.setI(0);
//		canTalonFlyWheel.setD(0);

	}

	public void autonomousInit() {
		
		canTalonFrontRight.setEncPosition(0);
		canTalonFrontRight.setEncPosition(0);
		
		
		Intake.stop();
		Shooter.stopAngle();
		Shooter.stopFlyWheel();

		
		RobotMap.autonMode = ((Integer) (autonChooser.getSelected())).intValue();
		
		if (RobotMap.autonMode == RobotMap.DRIVE_AND_SHOOT_CAMERA_AUTON){
			Autonomous.driveAndShootCameraAuton(this);
		}
		else if (RobotMap.autonMode == RobotMap.DRIVE_UNDER_BAR_AUTON){
			Autonomous.driveUnderBarAuton(this);
		}
		else if (RobotMap.autonMode == RobotMap.STOP_AUTON){
			Autonomous.stopAuton();
		}
		else if (RobotMap.autonMode == RobotMap.DRIVE_TO_DEFENSE_AUTON){
			Autonomous.driveToDefenseAuton(this);
		}
		else if (RobotMap.autonMode == RobotMap.DRIVE_AND_SHOOT_PID_AUTON){
			Autonomous.driveAndShootPIDAuton(this);
		}
	}


	public void autonomousPeriodic() {

	}

	public void teleopInit(){

		robotDrive.drive(0, 0);
		
		Intake.stop();
		Shooter.stopAngle();
		Shooter.stopFlyWheel();
		
		canTalonFrontRight.setEncPosition(0);
		canTalonFrontRight.setEncPosition(0);
		
		RobotMap.hoodState = RobotMap.HOOD_DEFAULT_STATE;
		RobotMap.currentState = RobotMap.DEFAULT_STATE;
		RobotMap.manualState = RobotMap.MANUAL_DEFAULT_STATE;

	}

	public void teleopPeriodic() {

		boolean isMinimize = joyOp.getRawButton(RobotMap.MINIMIZE_BUTTON);
		boolean lowGear = joyOp.getRawButton(RobotMap.HIGH_GEAR_BUTTON);
		boolean highGear = joyOp.getRawButton(RobotMap.LOW_GEAR_BUTTON);
		boolean isIntaking = joyOp.getRawButton(RobotMap.INTAKE_BUTTON);
		boolean isReverse = joyOp.getRawButton(RobotMap.REVERSE_BUTTON);
		boolean isFarFire = joyOp.getRawButton(RobotMap.FAR_FIRE_BUTTON);
		boolean isIntakeLower = joyOp.getRawButton(RobotMap.LOWER_INTAKE_BUTTON);
		boolean isIntakeRise = joyOp.getRawButton(RobotMap.RISE_INTAKE_BUTTON);
		boolean stopFlyWheel = joyOp.getRawButton(RobotMap.STOP_FLYWHEEL_BUTTON); //prob won't need this 
		boolean isCloseFire = joyOp.getRawButton(RobotMap.CLOSE_FIRE_BUTTON);
		boolean farAngle = joyOp.getRawButton(RobotMap.FAR_ANGLE_BUTTON);
		boolean optic = opticSensor.get();
		boolean limit1 = limitSwitch.get();
		boolean limit2 = limitSwitchTwo.get();
		boolean isReturn = joyOp.getRawButton(RobotMap.RETURN_BUTTON);
		boolean isManual = joyOp.getRawButton(RobotMap.MANUAL_BUTTON);
		boolean isCollapse = joyOp.getRawButton(RobotMap.COLLAPSE_BUTTON);
		boolean isLower = joyOp.getRawButton(RobotMap.LOWER_BUTTON);
		boolean manualHood = joyOp.getRawButton(RobotMap.MANUAL_HOOD_BUTTON);
		boolean closeAngle = joyOp.getRawButton(RobotMap.FIRE_BUTTON);
		boolean isFire = joyOp.getRawButton(RobotMap.CLOSE_ANGLE_BUTTON);
		boolean aim = joyThrottle.getRawButton(RobotMap.AIM_BUTTON);
		TeleopStateMachine.stateMachine(optic, isCloseFire, isFarFire, isLower, 
				isCollapse, isManual, isReturn, closeAngle, farAngle, isFire);
		Shooter.hoodStateMachine();
		//gear shifting code 
		
		if (aim){
			DriveController.aim(.5);
		}
		else{
			DriveController.stop();
		}
		
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
			SmartDashboard.putBoolean("Intake Postion ", true);
		}
		if (isIntakeRise){
			intakePiston.set(DoubleSolenoid.Value.kReverse);
			SmartDashboard.putBoolean("Intake Position: ", false);
		}

		
		//INTAKE SPEED
		//If none of these are true the teleop state machine can take over and use the intake for fire state. 
		if (isIntaking && optic && (RobotMap.currentState != RobotMap.FAR_FIRE_STATE)
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
		
		azimuth = table.getNumber("Azimuth", 400);
		System.out.println(azimuth);
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
////			}22
////		}
////		if(farAngle){
////			isClose = 0;
////			boolean done1 = Shooter.movePID(0);
////			if(done1){
////				Shooter.stopAngle();
////			}
////		}
	//		WHY SEAN WHY< WHY ARE YOU SO BAD
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
	
	public void testInit(){
		//canTalonFlyWheel.setEncPosition(0);
	}
	public void testPeriodic() {
		//System.out.println("hello");
		
		compressor.setClosedLoopControl(true);
		
		robotDrive.arcadeDrive(joyThrottle.getY(), joyWheel.getX());
//		
//		System.out.println(pdp.getCurrent(0));
//		System.out.println(pdp.getCurrent(1));
//		System.out.println(pdp.getCurrent(2));
//		System.out.println(pdp.getCurrent(3));
		
		System.out.print( "RPM: " + canTalonFlyWheel.getSpeed());
		System.out.println(" OUTPUT: " + canTalonFlyWheel.getOutputVoltage());
	//	System.out.println(" SPEED: " + Shooter.speed);
		//System.out.print(" Error: " + Shooter.error);
		//System.out.print(" ANGLE: " + pot.getValue());
		//System.out.println(" Target: " + target);
		
		
		if (joyOp.getRawButton(8)){
			//canTalonShooterAngle.set(.3);
			target = target + 2;
		}
		else if (joyOp.getRawButton(7)){
			//canTalonShooterAngle.set(-.3);
			target = target -2;
		}
		else{
			canTalonShooterAngle.set(0);
		}
		if (joyOp.getRawButton(11)){
			Shooter.movePotPID(target);
			
		}
		else if(joyOp.getRawButton(12)){
			Shooter.moveHoodBang(target);
		}
		else{
			canTalonShooterAngle.set(0);
		}
		
		
		if (!joyOp.getRawButton(11)){
			Shooter.i = 0.0;
		}
		
		
		
		if ( joyOp.getRawButton(2)){
			canTalonIntake.set(.7);
		}
		else if (joyOp.getRawButton(3)){
			canTalonIntake.set(-.7);
		}
		else{
			canTalonIntake.set(0);
		}
		
		canTalonFlyWheel.set(((joyOp.getRawAxis(3)/2)+.5));
		
	//	System.out.println(pot.getValue());
		
		if (joyOp.getRawButton(9)){
			intakePiston.set(DoubleSolenoid.Value.kReverse);
		}
		else if (joyOp.getRawButton(10)){
			intakePiston.set(DoubleSolenoid.Value.kForward);
		}
//			
//		if(joyOp.getRawButton(4)){
//			Shooter.setPID(7000);
//			
//		}
//		else if (joyOp.getRawButton(6)){
//			Shooter.setPID(6500);
//		}
//		else if (joyOp.getRawButton(5)){
//			canTalonFlyWheel.disable();
//		}
	}

}