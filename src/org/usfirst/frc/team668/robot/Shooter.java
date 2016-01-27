package org.usfirst.frc.team668.robot;

import edu.wpi.first.wpilibj.CANTalon;

public class Shooter {
	//i
	
	public static int error;	
	public static boolean movePID(double ref){

		Robot.canTalonShooterAngle.changeControlMode(CANTalon.TalonControlMode.Position);
		
		Robot.canTalonShooterAngle.setPID(1, .001, .3);
		
		Robot.canTalonShooterAngle.setSetpoint(ref);
		
		Robot.canTalonShooterAngle.enable();
		
		if (Robot.canTalonShooterAngle.getSpeed() < 4){
			Robot.canTalonShooterAngle.disable();
			return true;
		}
		else {
			return false;
		}
	}
	
	public static void fire(double speed){
		
		Robot.canTalonIntake.set(speed);
		
	}
	
	public static void spinFlyWheel(double speed){
		
		Robot.canTalonFlyWheel.set(speed);
		
	}
	
	public static void stopFlyWheel(){
		
		Robot.canTalonFlyWheel.set(0);
	
	}
	
	public static void stop(){
		
		Robot.canTalonIntake.set(0);
		
	}
	
	public static void stopAngle(){
		
		Robot.canTalonShooterAngle.set(0);
	
	}

}
