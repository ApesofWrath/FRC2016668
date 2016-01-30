package org.usfirst.frc.team668.robot;

import edu.wpi.first.wpilibj.CANTalon;

public class Shooter {
	//i
	
	public static int error;	
	public static boolean setPID(double ref){

		Robot.canTalonFlyWheel.changeControlMode(CANTalon.TalonControlMode.Speed);
		
		Robot.canTalonFlyWheel.setPID(2, .078, 1);
		
		Robot.canTalonFlyWheel.setSetpoint(ref);
		
		Robot.canTalonFlyWheel.enable();
		
		error = (int) (ref - Robot.canTalonFlyWheel.getSpeed());
		if (error < 1){
			Robot.canTalonFlyWheel.disable();
			return true;
		}
		else {
			return false;
		}
	}
	
	public static boolean movePID(double ref){
		
		Robot.canTalonShooterAngle.changeControlMode(CANTalon.TalonControlMode.Position);
		
		Robot.canTalonShooterAngle.setPID(2, .078, 1);
		
		Robot.canTalonShooterAngle.setSetpoint(ref);
		
		Robot.canTalonShooterAngle.enable();
		
		error = (int) (ref - Robot.canTalonShooterAngle.getEncPosition());
		if (error < 1){
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
