package org.usfirst.frc.team668.robot;

public class Intake {
	
	public static void spin(double speed){
		
		Robot.canTalonIntakeOne.set(speed);
		Robot.canTalonintakeTwo.set(speed);
		
	}
	
	public static void spit(double speed){
		
		Robot.canTalonIntakeOne.set(-speed);
		Robot.canTalonintakeTwo.set(-speed);
		
	}
	
	public static void stop(){
		
		Robot.canTalonIntakeOne.set(0.0);
		Robot.canTalonintakeTwo.set(0.0);
		
	}

}