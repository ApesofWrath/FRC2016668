
package org.usfirst.frc.team668.robot;

import edu.wpi.first.wpilibj.CANTalon;

public class Intake {
	
	
	
	public static void spin(double speed){
		
		Robot.canTalonIntake.set(-speed);
		
	}
	
	public static void spit(double speed){
		
		Robot.canTalonIntake.set(speed);
		
	}
	
	public static void stop(){
		
		Robot.canTalonIntake.set(0.0);
		
	}
	
}