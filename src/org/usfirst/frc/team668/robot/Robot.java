
package org.usfirst.frc.team668.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {
	
   public static Joystick joyWheel, joyThrottle, joyOp;
   public static CANTalon canTalonFlyWheel, canTalonTrigger, canTalonIntakeOne, 
   							canTalonIntakeTwo, canTalonFrontLeft, canTalonFrontRight, canTalonRearLeft, canTalonRearRight;
   public static RobotDrive robotDrive;
   	
   
   
    public void robotInit() {
        
    	 joyWheel = new Joystick(0);
         joyThrottle = new Joystick(1);
         
         joyOp = new Joystick(2);
         
         canTalonFlyWheel = new CANTalon(0);
         canTalonTrigger = new CANTalon(1);
         
         canTalonIntakeOne = new CANTalon(2);
         canTalonIntakeTwo = new CANTalon(3);
         
         canTalonFrontLeft = new CANTalon(4);
         canTalonFrontRight = new CANTalon(5);
         canTalonRearLeft = new CANTalon(6);
         canTalonRearRight = new CANTalon(7);

         canTalonFlyWheel = new CANTalon(2);
         canTalonTrigger = new CANTalon(3);
         
         canTalonIntakeOne = new CANTalon(4);
         canTalonIntakeTwo = new CANTalon(5);
         
         canTalonFrontLeft = new CANTalon(6);
         canTalonFrontRight = new CANTalon(7);
         canTalonRearLeft = new CANTalon(8);
         canTalonRearRight = new CANTalon(9);
         
         robotDrive = new RobotDrive(canTalonFrontLeft, canTalonRearLeft, canTalonFrontRight, canTalonRearRight);
         robotDrive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
     	 robotDrive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
         robotDrive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
     	 robotDrive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
         
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
    	
    	//Drive Code
    	if (isMinimize){
    		robotDrive.arcadeDrive(joyThrottle.getY()*.6, joyWheel.getX()*.6);
    	}
    	else{
    		robotDrive.arcadeDrive(joyThrottle.getY(), joyWheel.getX());
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
    		Shooter.fire(1.0); //TODO: change to controller
    	}
    	else{
    		Shooter.stop();
    	}
    
    }
   
    public void testPeriodic() {
    
    }
    
}
