package org.usfirst.frc.team668.robot;

public class Shooter {
	
	public static void fire(double speed){
		
		Robot.canTalonTrigger.set(speed));
		
	}
	
	public static void spinFlyWheel(double speed){
		Robot.canTalonFlywheel.set(speed);
	}

}
