

package org.usfirst.frc.team668.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Timer;

import java.io.PrintWriter;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {
	
   public static Joystick joyWheel, joyThrottle, joyOp;
   public static CANTalon canTalonFlyWheel, canTalonTrigger, canTalonIntake, 
   							 canTalonFrontLeft, canTalonFrontRight, canTalonRearLeft, canTalonRearRight,
   							canTalonIntakeAngle, canTalonShooterAngle;
   public static RobotDrive robotDrive;
  // public static Encoder encoderLeft, encoderRight, flywheelEncoder, intakeEncoder, angleEncoder;
   public static DigitalInput shooterLimitTop, shooterLimitBot, intakeLimitTop, intakeLimitBot; 
   public static CameraServer server;
   public static PrintWriter system;
   
    public void robotInit() {
    	
    	server = CameraServer.getInstance();
    	server.setQuality(50);
    	server.startAutomaticCapture("cam1");
        
    	 joyWheel = new Joystick(0);
         joyThrottle = new Joystick(1);
         
         joyOp = new Joystick(2);
         
         canTalonFlyWheel = new CANTalon(4);
         canTalonTrigger = new CANTalon(5);
         
         canTalonIntake = new CANTalon(6);
         
         canTalonFrontLeft = new CANTalon(7);
         canTalonFrontRight = new CANTalon(1);
         canTalonRearLeft = new CANTalon(2);
         canTalonRearRight = new CANTalon(3);

         canTalonIntakeAngle = new CANTalon(0);
         canTalonShooterAngle = new CANTalon(8);
         
         robotDrive = new RobotDrive(canTalonFrontLeft, canTalonRearLeft, canTalonFrontRight, canTalonRearRight);
         robotDrive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
     	 robotDrive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
         robotDrive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
     	 robotDrive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
     	 
//     	 encoderRight = new Encoder(0, 1)
//     	 encoderLeft = new Encoder(2,3);
//     	 flywheelEncoder = new Encoder(4,5);
//     	 intakeEncoder = new Encoder(6,7);
//     	 angleEncoder = new Encoder(8,9);
     	 
     	 shooterLimitTop = new DigitalInput(1);
     	 shooterLimitBot = new DigitalInput(2);
     	 intakeLimitTop = new DigitalInput(3);
     	 intakeLimitBot = new DigitalInput(4);
     	 
    }
    
    public void autonomousInit() {
    	
    }

  
    public void autonomousPeriodic() {
    	
    }

    public void teleopInit(){
    	canTalonIntakeAngle.setEncPosition(0);
    	System.out.println("Working");
    	
    }
    public void teleopPeriodic() {
    	
    	boolean isMinimize = joyThrottle.getRawButton(3);
    	boolean isIntaking = joyOp.getRawButton(3);
    	boolean isReverse = joyOp.getRawButton(5);
    	boolean isFire = joyOp.getRawButton(1);
    	boolean isIntakeLower = joyOp.getRawButton(8);
    	boolean isIntakeRise = joyOp.getRawButton(7);
    	
    	
    	//Drive Code
    	if (isMinimize){
    		robotDrive.arcadeDrive(joyThrottle.getY()*.6, joyWheel.getX()* .6);
    	}
    	else{
    		robotDrive.arcadeDrive(joyThrottle.getY(), joyWheel.getX());
    	}
    	
    	
    	if (isIntakeLower){
    		boolean isDone = Intake.talonPID(500);
    		if(isDone){
    			Intake.stopAngle();
    		}
    	}
    	if (isIntakeRise){
    		boolean isDone = Intake.talonPID(0);
    		if(isDone){
    			Intake.stopAngle();
    		}
    	}
    	
    	if (joyOp.getRawButton(1)){
    		canTalonIntakeAngle.set(.5);
    	}
    	if (joyOp.getRawButton(3)){
    		canTalonIntakeAngle.set(0);
    	}
    	if(joyOp.getRawButton(4)){
    		canTalonIntakeAngle.set(-.5);
    	}
    	
    	
    	System.out.println(canTalonIntakeAngle.getEncPosition());
    	
    	    	
    	//intake
    	if (isIntaking){
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
    		Shooter.fire(0); //TODO: change to controller
    	}
    		else{
    			Shooter.stop();
    	}
    	
    	
    	
    
    }
   
    public void testPeriodic() {
    
    	System.out.println("hello");
    }
    
}