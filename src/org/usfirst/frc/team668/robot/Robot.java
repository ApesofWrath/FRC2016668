

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

public class Robot extends IterativeRobot {
	
   public static Joystick joyWheel, joyThrottle, joyOp;
   public static CANTalon canTalonFlyWheel, canTalonTrigger, canTalonIntake, 
   							 canTalonFrontLeft, canTalonFrontRight, canTalonRearLeft, canTalonRearRight,
   							canTalonIntakeAngle, canTalonShooterAngle;
   public static RobotDrive robotDrive;
   public static DigitalInput opticSensor, opticSensor2;
   public static CameraServer server;
   public static PrintWriter system;
   public static DoubleSolenoid intakePiston, shiftRight, shiftLeft, shooterPiston;
   public static Compressor compressor;
   public boolean isClose;
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
     	 
     	 //shooterLimitTop = new DigitalInput(1);
     	 //shooterLimitBot = new DigitalInput(4);
     	 //intakeLimitTop = new DigitalInput(2);
     	 //intakeLimitBot = new DigitalInput(3);
     	 opticSensor = new DigitalInput(0);
     	 opticSensor2 = new DigitalInput(1);
     	 
     	 intakePiston = new DoubleSolenoid( 0, 1);
     	 shiftRight = new DoubleSolenoid( 2, 3);
     	 shiftLeft = new DoubleSolenoid( 4, 5);
     	 shooterPiston = new DoubleSolenoid(6,7);
     	 
     	 compressor = new Compressor(20);
     	 
     	 compressor.setClosedLoopControl(true);
     	 
     	 isClose = false;
     	 
     	 
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
    	boolean isIntaking = joyOp.getRawButton(2);
    	boolean isReverse = joyOp.getRawButton(3);
    	boolean isFire = joyOp.getRawButton(1);
    	boolean isIntakeLower = joyOp.getRawButton(7);
    	boolean isIntakeRise = joyOp.getRawButton(8);
    	boolean stopFlyWheel = joyOp.getRawButton(5);
    	boolean closeAngle = joyOp.getRawButton(6);
    	boolean farAngle = joyOp.getRawButton(4);
    	boolean optic = opticSensor.get();
    	boolean optic2 = opticSensor2.get();
    	
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
    		}
    	}
    		else{
    			Shooter.stop();
    	}
    	
    	
    	//Shooter angle control
    	if (closeAngle){
    		isClose = true;
    		shooterPiston.set(DoubleSolenoid.Value.kForward);
    	}
    	if(farAngle){
    		isClose = false;
    		shooterPiston.set(DoubleSolenoid.Value.kReverse);
    	}
    	
    	
    	
    	
    	
    	
    	
    	if (joyOp.getRawButton(10) && !optic){
    		canTalonShooterAngle.set(.5);
    	}
    	else {
    		
    		canTalonShooterAngle.set(0);
    	
    	}
    
    }
   
    public void testPeriodic() {
        	System.out.println("hello");
    }
    
}