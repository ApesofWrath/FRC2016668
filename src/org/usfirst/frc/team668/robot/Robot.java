

package org.usfirst.frc.team668.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Timer;
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
   public static Encoder encoderLeft, encoderRight, flywheelEncoder, intakeEncoder, angleEncoder;
   public static DigitalInput shooterLimitTop, shooterLimitBot, intakeLimitTop, intakeLimitBot; 
   public static CameraServer server;
   
    public void robotInit() {
    	
    	server = CameraServer.getInstance();
    	server.setQuality(50);
    	server.startAutomaticCapture("cam1");
        
    	 joyWheel = new Joystick(0);
         joyThrottle = new Joystick(1);
         
         joyOp = new Joystick(2);
         
         canTalonFlyWheel = new CANTalon(0);
         canTalonTrigger = new CANTalon(1);
         
         canTalonIntake = new CANTalon(2);
         
         canTalonFrontLeft = new CANTalon(3);
         canTalonFrontRight = new CANTalon(4);
         canTalonRearLeft = new CANTalon(5);
         canTalonRearRight = new CANTalon(6);

         canTalonIntakeAngle = new CANTalon(7);
         canTalonShooterAngle = new CANTalon(8);
         
         robotDrive = new RobotDrive(canTalonFrontLeft, canTalonRearLeft, canTalonFrontRight, canTalonRearRight);
         robotDrive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
     	 robotDrive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
         robotDrive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
     	 robotDrive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
     	 
     	 encoderRight = new Encoder(0,1);
     	 encoderLeft = new Encoder(2,3);
     	 flywheelEncoder = new Encoder(4,5);
     	 intakeEncoder = new Encoder(6,7);
     	 angleEncoder = new Encoder(8,9);
     	 
     	 shooterLimitTop = new DigitalInput(0);
     	 shooterLimitBot = new DigitalInput(1);
     	 intakeLimitTop = new DigitalInput(2);
     	 intakeLimitBot = new DigitalInput(3);
     	 
    }
    
    public void autonomousInit() {
    	
    }

  
    public void autonomousPeriodic() {
    	
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
    		robotDrive.arcadeDrive(joyThrottle.getY()*.6, joyWheel.getX()*.6);
    	}
    	else{
    		robotDrive.arcadeDrive(joyThrottle.getY(), joyWheel.getX());
    	}
    	
    	if (isIntakeLower){
    		Intake.movePID(0);
    	}
    	if (isIntakeRise){
    		Intake.movePID(0);
    	}
    	
    	
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
    
    }
    
}