
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
        
    }
    
    public void autonomousInit() {
    	
    }

  
    public void autonomousPeriodic() {
    	
    }

   
    public void teleopPeriodic() {
    	
    	boolean isMinimize = joyThrottle.getRawButton(3);
    	
    	if (isMinimize){
    		robotDrive.arcadeDrive(joyThrottle.getY()*.6, joyWheel.getX()*.6);
    	}
    	else{
    		robotDrive.arcadeDrive(joyThrottle.getY(), joyWheel.getX());
    	}
    	
    
    }
   
    public void testPeriodic() {
    
    }
    
}
