

package org.usfirst.frc.team668.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Relay.Value;
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
	canTalonIntakeAngle, canTalonShooterAngle, canTalonShooterAngleTwo, canTalonArm;
	public static RobotDrive robotDrive;
	public static DigitalInput opticSensor, limitSwitch, limitSwitchTwo;
	public static CameraServer server;
	public static PrintWriter system;
	public static DoubleSolenoid intakePiston, shiftPiston;
	public static Compressor compressor;
	public static AnalogInput pot, armPot;
	public static NetworkTable table;
	public static SendableChooser autonChooser;
	public static PowerDistributionPanel pdp;
	public static Relay light;
	
	public static double distance; //get from network table
	public static double azimuth; //get from network table
	
	public int isClose;
	public static int ref = 0;
	public static int lastRef = 100000;
	public static boolean canIntake = true;
	public static boolean ballInPlace = false;
	
	public int target = 3210;

	public static boolean isBrightEyes = true;
	// public static USBCamera camFront = new USBCamera("cam1");
	// public static USBCamera camRear = new USBCamera("cam2");
	
	
	
	public void robotInit() {

		server = CameraServer.getInstance();
		server.setQuality(50);
		server.startAutomaticCapture("cam0");
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
		canTalonArm = new CANTalon(RobotMap.ARM_CAN_ID);

		robotDrive = new RobotDrive(canTalonFrontLeft, canTalonRearLeft, canTalonFrontRight, canTalonRearRight);
		robotDrive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, false);
		robotDrive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, false);
		robotDrive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, false);
		robotDrive.setInvertedMotor(RobotDrive.MotorType.kRearRight, false);

		opticSensor = new DigitalInput(RobotMap.OPTIC_SENSOR_DIGITAL_INPUT_PORT);
		//limitSwitch = new DigitalInput(RobotMap.LIMIT_SWITCH_DIGITAL_INPUT);
	//	limitSwitchTwo = new DigitalInput(RobotMap.LIMIT_SWITCH_TWO_DIGITAL_INPUT);
		
		intakePiston = new DoubleSolenoid(RobotMap.PCM_CAN_ID, RobotMap.INTAKE_PISTON_EXPAND_CHANNEL, RobotMap.INTAKE_PISTON_RETRACT_CHANNNEL);
		shiftPiston = new DoubleSolenoid(RobotMap.PCM_CAN_ID, RobotMap.PISTON_SHIFT_EXPAND_CHANNEL, RobotMap.PISTON_SHIFT_RETRACT_CHANNEL);
		
		pdp = new PowerDistributionPanel(20);
		
		light = new Relay(0);
		
		pot = new AnalogInput(RobotMap.POT_ANALOG_INPUT_PORT);
		armPot = new AnalogInput(RobotMap.ARM_POT_ANALOG_INPUT_PORT);
		
		compressor = new Compressor(RobotMap.PCM_CAN_ID);

		compressor.setClosedLoopControl(true);

		SmartDashboard.putString("Status:", " Working");
		
		autonChooser = new SendableChooser();
		
		autonChooser.addObject("Drive and Shoot Camera Autonomous", new Integer(RobotMap.DRIVE_AND_SHOOT_CAMERA_AUTON));
		autonChooser.addObject("Drive Under Bar Autonomous", new Integer(RobotMap.DRIVE_UNDER_BAR_AUTON));
		autonChooser.addObject("Stop Autonomous", new Integer(RobotMap.STOP_AUTON));
		/*autonChooser.addObject("Drive to Defense Autonomous", new Integer(RobotMap.DRIVE_TO_DEFENSE_AUTON));
		 */
		autonChooser.addObject("Spy Bot Shoot", new Integer(RobotMap.SPYBOT_SHOT_AUTON));
		autonChooser.addDefault("Spy Bot no auto aim", RobotMap.SPYBOT_NO_AIM);
		
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
		
		
		if (isBrightEyes){
			target = 750;
		}
		else{
			target = 3210;
		}

	}

	public void autonomousInit() {
		
		canTalonFrontRight.setEncPosition(0);
		canTalonFrontLeft.setEncPosition(0);
		
		
		Intake.stop();
		Shooter.stopAngle();
		Shooter.stopFlyWheel();
		RobotMap.hoodState = RobotMap.HOOD_ZERO_STATE;
		RobotMap.autonStateShoot = RobotMap.DRIVE_FORWARD_SHOOT_STATE;
		RobotMap.autonSpyState = RobotMap.AIM_SPY_STATE;
		RobotMap.autonStateForward = RobotMap.DRIVE_FORWARD_STATE;
		RobotMap.noCamSpyState = RobotMap.SET_ANGLE_STATE;
		
		DriveController.I = 0;
		DriveController.i = 0;
		DriveController.lastTime = ((double)System.currentTimeMillis())/1000.0;
		DriveController.lastError = 0;
		
	}


	public void autonomousPeriodic() {
		
		
		distance = table.getNumber("Distance", 0);
		
		azimuth = table.getNumber("Azimuth", 666);
		
		Shooter.hoodStateMachine();
		
		System.out.println("I: " + DriveController.I);
		
		System.out.println("RPM: " + canTalonFlyWheel.getSpeed());
		
		RobotMap.autonMode = ((Integer) (autonChooser.getSelected())).intValue();
		
		if (RobotMap.autonMode == RobotMap.DRIVE_AND_SHOOT_CAMERA_AUTON){
			System.out.println("DRIVE_AND SHOOT");
			Autonomous.driveAndShootCameraAuton(this);
		}
		else if (RobotMap.autonMode == RobotMap.DRIVE_UNDER_BAR_AUTON){
			Autonomous.driveUnderBarAuton(this);
		}
		else if (RobotMap.autonMode == RobotMap.STOP_AUTON){
			System.out.println("HAH");
			Autonomous.stopAuton();
		}
		/*else if (RobotMap.autonMode == RobotMap.DRIVE_TO_DEFENSE_AUTON){
			Autonomous.driveToDefenseAuton(this);
		}
		*/
		else if (RobotMap.autonMode == RobotMap.SPYBOT_SHOT_AUTON){
			Autonomous.spyBotShotAutonomous(this);
		}
		else if (RobotMap.autonMode == RobotMap.SPYBOT_NO_AIM){
			Autonomous.spyBotNoCamera();
		}
		
		System.out.println("ENC: " + canTalonFrontRight.getEncPosition());
	}

	public void teleopInit(){

		robotDrive.drive(0, 0);
		
		Intake.stop();
		Shooter.stopAngle();
		//Shooter.stopFlyWheel();
		
		canTalonFrontRight.setEncPosition(0);
		canTalonFrontRight.setEncPosition(0);
		
		Shooter.i = 0.0;
		Shooter.d = 0.0;
		Shooter.I = 0.0;
		Shooter.D = 0.0;
		Shooter.lastError = 0; //TODO: make a constant
		Shooter.lastTime = ((double)System.currentTimeMillis())/1000.0;

		DriveController.I = 0;
		DriveController.i = 0;
		DriveController.lastTime = ((double)System.currentTimeMillis())/1000.0;
		
		RobotMap.hoodState = RobotMap.HOOD_DEFAULT_STATE;
		RobotMap.currentState = RobotMap.DEFAULT_STATE;
		RobotMap.manualState = RobotMap.MANUAL_DEFAULT_STATE;
		
		TeleopStateMachine.flyWheelSpeed = 7800;

	}

	public void teleopPeriodic() {

		boolean isMinimize = joyThrottle.getRawButton(RobotMap.MINIMIZE_BUTTON);
		boolean lowGear = joyOp.getRawButton(RobotMap.HIGH_GEAR_BUTTON);
		boolean highGear = joyOp.getRawButton(RobotMap.LOW_GEAR_BUTTON);
		boolean isIntaking = joyOp.getRawButton(RobotMap.INTAKE_BUTTON);
		boolean isReverse = joyOp.getRawButton(RobotMap.REVERSE_BUTTON);
		boolean isFarFire = joyOp.getRawButton(RobotMap.FAR_FIRE_BUTTON);
		boolean isFlashFire = joyOp.getRawButton(RobotMap.CLOSE_FIRE_BUTTON);
		boolean isIntakeLower = joyOp.getRawButton(RobotMap.LOWER_INTAKE_BUTTON);
		boolean isIntakeRise = joyOp.getRawButton(RobotMap.RISE_INTAKE_BUTTON);
		boolean stopFlyWheel = joyOp.getRawButton(RobotMap.STOP_FLYWHEEL_BUTTON); //we dont use this button
		boolean lowGoal = joyOp.getRawButton(RobotMap.LOW_GOAL_BUTTON);
		boolean isLob = joyOp.getRawButton(RobotMap.LOB_SHOT_BUTTON);
		//boolean isLower = joyOp.getRawButton(RobotMap.LOWER_BUTTON);

		
		boolean isReturn = joyOp.getRawButton(RobotMap.RETURN_BUTTON);
		//boolean optic = opticSensor.get();
		boolean optic = opticSensor.get();
//		boolean limit1 = limitSwitch.get();
//		boolean limit2 = limitSwitchTwo.get();
		
		boolean farAngle = joyOp.getRawButton(RobotMap.FAR_ANGLE_BUTTON);
		boolean isSally = joyOp.getRawButton(RobotMap.SALLY_BUTTON);
		boolean isGround = joyOp.getRawButton(RobotMap.GROUND_BUTTON);
		boolean manualHood = joyOp.getRawButton(RobotMap.MANUAL_HOOD_BUTTON);
		boolean closeAngle = joyOp.getRawButton(RobotMap.CLOSE_ANGLE_BUTTON);
		boolean isFire = joyOp.getRawButton(RobotMap.FIRE_BUTTON);
		
		boolean aim = joyThrottle.getRawButton(RobotMap.AIM_BUTTON);
		boolean flash = joyThrottle.getRawButton(RobotMap.FLASH_ON_BUTTON);
		boolean flashOff = joyThrottle.getRawButton(RobotMap.FLASH_OFF_BUTTON);
		
//		if ((RobotMap.currentState != RobotMap.CLOSE_FIRE_STATE) && (RobotMap.currentState != RobotMap.FAR_FIRE_STATE) 
//				&& (RobotMap.currentState != RobotMap.BALL_CLEAR_STATE) && (RobotMap.currentState != RobotMap.MANUAL_FIRE_STATE)){
//			optic = opticSensor.get();
//		}
//		else{
//			optic = true;
//		}
		
		//Arm.armStateMachine(isGround, isSally);
			
		TeleopStateMachine.stateMachine(optic, isFlashFire, isFarFire, isIntakeLower
				, isReturn, farAngle, closeAngle, isFire, isReverse, manualHood, lowGoal
				, isLob);
		
		
		Shooter.hoodStateMachine();
		
		//Arm.armStateMachine(isPort, isGround);
		//gear shifting code 
		
		SmartDashboard.putNumber("Encoder", canTalonFrontRight.getEncPosition());
		
		if ((Math.abs(joyThrottle.getY()) < RobotMap.ACCEPTABLE_JOYSTICK_RANGE 
				&& Math.abs(joyWheel.getX()) < RobotMap.ACCEPTABLE_JOYSTICK_RANGE) && aim && (azimuth != 400 || azimuth != 666)){
			DriveController.aimPI();
		}
		
//		if ((Math.abs(joyThrottle.getY()) < RobotMap.ACCEPTABLE_JOYSTICK_RANGE ) && aim && azimuth != 400){
//			DriveController.aimP();
//			System.out.println("HI");
//		}
		
		if(!aim){
			DriveController.i = 0;
			DriveController.I = 0;
			DriveController.lastTime = ((double)System.currentTimeMillis())/1000.0; 
		}
		
		//System.out.println("AZIMUTH: " + azimuth);
		
		if (lowGear){
			intakePiston.set(DoubleSolenoid.Value.kReverse);
		}
		else if (highGear){
			intakePiston.set(DoubleSolenoid.Value.kForward);
		}
		
		//Drive Code
		if ((Math.abs(joyThrottle.getY()) > .2 || Math.abs(joyWheel.getX()) > .2) 
				&& !joyThrottle.getRawButton(3)){
			if (isMinimize){
				robotDrive.arcadeDrive(joyThrottle.getY()*.6, -joyWheel.getX()* .6);
			}
			else{
				robotDrive.arcadeDrive(joyThrottle.getY()*1.3, -joyWheel.getX()*1.3);
			}
		}

		//System.out.println(RobotMap.currentState);
	
		if (flash){
			light.set(Value.kOn);
		}
		else{
			light.set(Value.kOff);
		}
		
		//controls the state of the intake pistons 
		if (isIntakeLower){
			intakePiston.set(DoubleSolenoid.Value.kReverse);
			SmartDashboard.putBoolean("Intake Postion ", true);
		}
		if (isIntakeRise){
			intakePiston.set(DoubleSolenoid.Value.kForward);
			SmartDashboard.putBoolean("Intake Position ", false);
		}

	// 	System.out.print("  Intake Speed: " + canTalonIntake.get());
		
//		if (isReverse){
//			System.out.print(" HELLO WORLD");
//		}
		
//		if (joyOp.getRawButton(4)){
//			canTalonIntake.set(.8);
//		}
		
		//INTAKE SPEED
		//If none of these are true the teleop state machine can take over and use the intake for fire state. 
		if (isIntaking && optic && (RobotMap.currentState != RobotMap.FAR_FIRE_STATE)
				&& (RobotMap.currentState != RobotMap.CLOSE_FIRE_STATE)){  //TODO: add the sensor
			Intake.spin(.8);
		}
		else if (isReverse  && TeleopStateMachine.canReverse){
		//	Intake.spit(.8);
			canTalonIntake.set(.8);
		}
		else if((RobotMap.currentState != RobotMap.LOWER_INTAKE_STATE) 
				&& (RobotMap.currentState != RobotMap.FAR_FIRE_STATE)
				&& (RobotMap.currentState != RobotMap.CLOSE_FIRE_STATE)
				&& (RobotMap.manualState != RobotMap.MANUAL_FIRE_STATE)){
			Intake.stop();
		}
		
		//System.out.println(pot.getValue());
		
	
		
		distance = table.getNumber("Distance", 0);
		
		azimuth = table.getNumber("Azimuth", 666);
		//System.out.println(azimuth);
		
		System.out.print("Azimuth: " + azimuth);
		System.out.println(" Distance: " + distance);
	}//end of periodic
		
		
		
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
		Shooter.i = 0.0;
		Shooter.d = 0.0;
		Shooter.I = 0.0;
		Shooter.D = 0.0;
		Shooter.lastError = 0; //TODO: make a constant
	}
	public void testPeriodic() {
		//System.out.println("hello");
		
		compressor.setClosedLoopControl(true);
		
		if((Math.abs(joyThrottle.getY()) > .2 || Math.abs(joyWheel.getX()) > .2) 
				&& !joyThrottle.getRawButton(2) && !joyThrottle.getRawButton(1)){
			robotDrive.arcadeDrive(joyThrottle.getY(), -joyWheel.getX());
		}
		
//		robotDrive.arcadeDrive(joyThrottle.getY(), joyWheel.getX());

//		
//		System.out.println(pdp.getCurrent(0));
//		System.out.println(pdp.getCurrent(1));
//		System.out.println(pdp.getCurrent(2));
//		System.out.println(pdp.getCurrent(3));
		
		
		//SmartDashboard.putNumber("RPM: ", canTalonFlyWheel.getSpeed());
		System.out.print("isBrightEyes: " + isBrightEyes);
		System.out.print(" RPM: " + canTalonFlyWheel.getSpeed());
		//System.out.print(" OUTPUT: " + canTalonFlyWheel.getOutputVoltage());
		//System.out.print(" SPEED: " + Shooter.speed);
		//System.out.println(" Error: " + Shooter.error);
		//System.out.print(" ANGLE: " + pot.getValue());
		//System.out.println(" Target: " + target);
		
		
		if ( Math.abs(joyThrottle.getY()) < .1 && Math.abs(joyWheel.getX()) < .1 && joyThrottle.getRawButton(7)){
			DriveController.aim(-.13);
		}
//		else if (){
//			DriveController.stop();
//		}
		
		if (joyOp.getRawButton(8)){
			//canTalonShooterAngle.set(.3);
			target = target + 2;
		}
		else if (joyOp.getRawButton(7)){
			//canTalonShooterAngle.set(-.3);
			target = target -2;
		}
//		else{
//			//canTalonShooterAngle.set(0);
//		}
		
		if (joyOp.getRawButton(11)){
			Shooter.movePotPID(target);
			
		}
//		else if(joyOp.getRawButton(12)){
//			Shooter.moveHoodBang(target);
//		}
		else{
			canTalonShooterAngle.set(0);
			
		}
		
		
		if (!joyOp.getRawButton(11)){
			Shooter.i = 0.0;
			Shooter.d = 0.0;
			Shooter.P = 0.0;
			Shooter.I = 0.0;
			Shooter.D = 0.0; 
			Shooter.lastError = 0;
			Shooter.lastTime = ((double)System.currentTimeMillis())/1000.0;
		}
		
		
		
		if ( joyOp.getRawButton(2)){
			canTalonIntake.set(1);
		}
		/*
		else if (joyOp.getRawButton(3)){
			canTalonIntake.set(-1);
		}
		else{
			canTalonIntake.set(0);
		}
		*/
	//	canTalonFlyWheel.set(((joyOp.getRawAxis(3)/2)+.5));
		
	//	System.out.println(pot.getValue());
		
		if (joyThrottle.getRawButton(3)){
			shiftPiston.set(DoubleSolenoid.Value.kReverse);
		}
		else if (joyThrottle.getRawButton(2)){
			shiftPiston.set(DoubleSolenoid.Value.kForward);
		}
		
		if (joyOp.getRawButton(9)){
			intakePiston.set(DoubleSolenoid.Value.kReverse);
		}
		else if (joyOp.getRawButton(10)){
			intakePiston.set(DoubleSolenoid.Value.kForward);
		}
		
		
		distance = table.getNumber("Distance" , 0);
		SmartDashboard.putNumber("Distance: ", distance);
			
		azimuth = table.getNumber("Azimuth", 666);
		SmartDashboard.putNumber("AZIMUTH: ", azimuth);
		
//		SmartDashboard.putNumber("P", Shooter.P);
//		SmartDashboard.putNumber("I", Shooter.I);
//		SmartDashboard.putNumber("D", Shooter.D);
		
	
	//	int ref = (int)(((joyOp.getRawAxis(3)/2)+.5)*11000);
		
		System.out.printf("P: %2.2f I: %2.2f D: %2.2f Speed: %2.2f Target: %2.2f TargetAng: %2.2f Angle: %2.2f\n" 
				, Shooter.P, Shooter.I, Shooter.D, Shooter.speed, (double)ref, (double)target, (double)pot.getValue());
		
		if(joyOp.getRawButton(4)){
			Shooter.setPID(7000);
		}
		else if (joyOp.getRawButton(6)){
			Shooter.setPID(6500);
		}
		else if (joyOp.getRawButton(5)){
			canTalonFlyWheel.disable();
		}
		//5700
		//671
		if (joyOp.getRawButton(8)){
			canTalonArm.set(.4);
		}
		else if (joyOp.getRawButton(7)){
			canTalonArm.set(-.4);
		}
		else{
			canTalonArm.set(0);
		}
		
		if ( joyOp.getRawButton(5)){
			//canTalonArm.set(.3);
			Shooter.movePotPID(RobotMap.BRIGHT_FLASH_ANGLE_VALUE);
		}
		//else if (joyOp.getRawButton(3)){
			//canTalonArm.set(-.3);
		//}
		else{
			canTalonShooterAngle.set(0);
			Shooter.lastTime = ((double)System.currentTimeMillis())/1000.0;
		}
	}

}