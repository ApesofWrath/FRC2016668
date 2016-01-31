

package org.usfirst.frc.team668.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Timer;

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
   							canTalonIntakeAngle, canTalonShooterAngle;
   public static RobotDrive robotDrive;
   public static DigitalInput opticSensor1, opticSensor2;
  // public static CameraServer server;
   public static PrintWriter system;
   public static DoubleSolenoid intakePiston, shiftRight, shiftLeft, shooterPiston;
   public static Compressor compressor;
   public boolean isClose;
//   public static USBCamera camFront = new USBCamera("cam1");
//   public static USBCamera camRear = new USBCamera("cam2");
    public void robotInit() {
    	
//    	server = CameraServer.getInstance();
//    	server.setQuality(50);
//    	camFront.openCamera();
//    	camRear.openCamera();
       
    	 joyWheel = new Joystick(RobotMap.WHEEL_ID);
         joyThrottle = new Joystick(RobotMap.THROTTLE_ID);
         
         joyOp = new Joystick(RobotMap.OPERATOR_ID);
         
         canTalonFlyWheel = new CANTalon(RobotMap.FLY_WHEEL_CAN_ID);
        // canTalonTrigger = new CANTalon(26);
         
         canTalonIntake = new CANTalon(RobotMap.INTAKE_CAN_ID);
         
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
     	 
     	 //shooterLimitTop = new DigitalInput(1);
     	 //shooterLimitBot = new DigitalInput(4);
     	 //intakeLimitTop = new DigitalInput(2);
     	 //intakeLimitBot = new DigitalInput(3);
     	 opticSensor1 = new DigitalInput(RobotMap.OPTIC_SENSOR_1_DIGITAL_INPUT_PORT);
     	 opticSensor2 = new DigitalInput(RobotMap.OPTIC_SENSOR_2_DIGITAL_INPUT_PORT);
     	 
     	 intakePiston = new DoubleSolenoid(RobotMap.INTAKE_PISTON_EXPAND_CHANNEL, RobotMap.INTAKE_PISTON_RETRACT_CHANNNEL);
     	 shiftRight = new DoubleSolenoid(RobotMap.PISTON_SHIFT_RIGHT_EXPAND_CHANNEL, RobotMap.PISTON_SHIFT_RIGHT_RETRACT_CHANNEL);
     	 shiftLeft = new DoubleSolenoid(RobotMap.PISTON_SHIFT_LEFT_EXPAND_CHANNEL, RobotMap.PISTON_SHIFT_LEFT_RETRACT_CHANNEL);
     	 shooterPiston = new DoubleSolenoid(6,7);
     	 
     	 compressor = new Compressor(RobotMap.PCM_CAN_ID);
     	 
     	 compressor.setClosedLoopControl(true);
     	 
     	 isClose = false;
     	 
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
    	
    	boolean isMinimize = RobotMap.MINIMIZE_BUTTON;
    	boolean isIntaking = RobotMap.INTAKE_BUTTON;
    	boolean isReverse = RobotMap.REVERSE_BUTTON;
    	boolean isFire = RobotMap.FIRE_BUTTON;
    	boolean isIntakeLower = RobotMap.LOWER_INTAKE_BUTTON;
    	boolean isIntakeRise = RobotMap.RISE_INTAKE_BUTTON;
    	boolean stopFlyWheel = RobotMap.STOP_FLYWHEEL_BUTTON;
    	boolean closeAngle = RobotMap.CLOSE_ANGLE_BUTTON;
    	boolean farAngle = RobotMap.FAR_ANGLE_BUTTON;
    	boolean optic = RobotMap.OPTIC_SENSOR_VALUE;
    	boolean optic2 = RobotMap.OPTIC_SENSOR_VALUE_2;
    	
//    	if(joyOp.getRawButton(9)){
//    		camFront.startCapture();
//    		camRear.stopCapture();
//    	}
//    	else{
//    		camFront.stopCapture();
//    		camRear.startCapture();
//    	}
    	
    	//Drive Code
    	if (isMinimize){
    		robotDrive.arcadeDrive(joyThrottle.getY()*.6, joyWheel.getX()* .6);
    	}
    	else{
    		robotDrive.arcadeDrive(joyThrottle.getY(), joyWheel.getX());
    	}
    	
    	if(joyOp.getRawButton(8)){
    		
    	//controls the state of the intake pistons 
    	if (isIntakeLower){
    		intakePiston.set(DoubleSolenoid.Value.kForward);
    	}
    	if (isIntakeRise){
    		intakePiston.set(DoubleSolenoid.Value.kReverse);   		
    	}
    
    	
    		System.out.println(canTalonFlyWheel.getSpeed());
    	
    	    	
    	//intake
    	if (isIntaking && !optic && !optic2 ){  //TODO: add the sensor
    		Intake.spin(.8);
    	}
    		else{
    			Intake.stop();
    	}
    	
    	//reverse intake
    	if (isReverse){
    		Intake.spit(.8);
    	}
    		else{
    			Intake.stop();
    	}
    	
    	//Firing
    	if (isFire){
    		if(isClose){
    			Shooter.spinFlyWheel(.7);
    			Shooter.fire(.8); 
    		}
    		else{
    			//TODO: Camera assisted firing
    			boolean isDone = Shooter.setPID(0);
    			if (isDone){
    				Shooter.setPID(0);
    				Shooter.fire(.8);
    			}
    		}
    	}
    		else{
    			Shooter.stop();
    			Shooter.stopFlyWheel();
    	}
    	
    	
    	//Shooter angle control
    	if (closeAngle){
    		isClose = true;
    		boolean done = Shooter.movePID(0);
    		if (done){
    			Shooter.stopAngle();
    		}
    	}
    	if(farAngle){
    		isClose = false;
    		boolean done1 = Shooter.movePID(0);
    		if(done1){
    			Shooter.stopAngle();
    		}
    	}
    	
    	//Experimental code
    	
    	if (joyOp.getRawButton(10) && !optic){
    		canTalonShooterAngle.set(.5);
    	}
    	else {
    		canTalonShooterAngle.set(0);
    	}
    	
    	if (joyOp.getRawButton(11)){
    		canTalonFlyWheel.set(.5);
    	}
    	if (joyOp.getRawButton(12)){
    		boolean isdone = Shooter.setPID(70);
    		if(isdone){
    			Shooter.stopFlyWheel();
    		}
    	}
    	}
    	//Experimental code end
    
    }
   
    public void testPeriodic() {
        	System.out.println("hello");
    }
    
}