

package org.usfirst.frc.team668.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Timer;

import java.io.PrintWriter;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {
	
   public static Joystick joyWheel, joyThrottle, joyOp;
   public static CANTalon canTalonFlyWheel, canTalonTrigger, canTalonIntake, 
   							 canTalonFrontLeft, canTalonFrontRight, canTalonRearLeft, canTalonRearRight,
   							canTalonIntakeAngle, canTalonShooterAngle;
   public static RobotDrive robotDrive;
   public static DigitalInput shooterLimitTop, shooterLimitBot, intakeLimitTop, intakeLimitBot, opticSensor;
   public static CameraServer server;
   public static PrintWriter system;
   public static DoubleSolenoid intakePiston, shiftRight, shiftLeft;
   
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

         canTalonShooterAngle = new CANTalon(0);
         
         robotDrive = new RobotDrive(canTalonFrontLeft, canTalonRearLeft, canTalonFrontRight, canTalonRearRight);
         robotDrive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
     	 robotDrive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
         robotDrive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
     	 robotDrive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
     	 
     	 //shooterLimitTop = new DigitalInput(0);
     	 //shooterLimitBot = new DigitalInput(4);
     	 //intakeLimitTop = new DigitalInput(2);
     	 //intakeLimitBot = new DigitalInput(3);
     	 opticSensor = new DigitalInput(1);
     	 
     	 intakePiston = new DoubleSolenoid( 0, 1);
     	 shiftRight = new DoubleSolenoid( 2, 3);
     	 shiftLeft = new DoubleSolenoid( 4, 5);
     	 
    }
    
    public void autonomousInit() {
    	
    }

  
    public void autonomousPeriodic() {
    	
    }

    public void teleopInit(){
    	
    	canTalonShooterAngle.setEncPosition(0);
    	System.out.println("Working");
    	
    }
    

    public void teleopPeriodic() {
    	
    	boolean isMinimize = joyThrottle.getRawButton(3);
    	boolean isIntaking = joyOp.getRawButton(3);
    	boolean isReverse = joyOp.getRawButton(5);
    	boolean isFire = joyOp.getRawButton(1);
    	boolean isIntakeLower = joyOp.getRawButton(8);
    	boolean isIntakeRise = joyOp.getRawButton(7);
    	boolean stopFlyWheel = joyOp.getRawButton(6);
    	boolean optic = opticSensor.get();
    	
    	//Drive Code
    	if (isMinimize){
    		robotDrive.arcadeDrive(joyThrottle.getY()*.6, joyWheel.getX()* .6);
    	}
    	else{
    		robotDrive.arcadeDrive(joyThrottle.getY(), joyWheel.getX());
    	}
    	
    	
    	if (isIntakeLower){
    		intakePiston.set(DoubleSolenoid.Value.kForward);
    	}
    	if (isIntakeRise){
    		intakePiston.set(DoubleSolenoid.Value.kReverse);   		
    	}
    
    	
    		System.out.println(opticSensor.get());
    	
    	    	
    	//intake
    	if (isIntaking && !optic){  //TODO: add the sensor
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
    		Shooter.fire(.8); //TODO: change to controller
    	}
    		else{
    			Shooter.stop();
    	}
    	
    	//spins the flywheel
    	if (stopFlyWheel){
    		Shooter.stopFlyWheel();
    	}
    	else{
    		Shooter.spinFlyWheel(.8);
    	}
    	
    	
    	
    	
    	
    
    }
   
    public void testPeriodic() {
        	System.out.println("hello");
    }
    
}