package org.usfirst.frc.team668.robot;

public class Shooter {
	
	public static double Kp = 0;
	public static double Ki = 0;
	public static double Kd = 0;
	public static long lastTime = System.currentTimeMillis();
	public static double speed;
	public static int error = 0;
	public static int lastError = 0;
	public static double i = 0;
	public static double d = 0;
	public static double P, I, D;
	
	public static boolean talonPID(double ref){
		
		Robot.canTalonShooterAngle.setPID(0,0,0);
		
		Robot.canTalonIntakeAngle.setSetpoint(ref);
		
		error = (int) Robot.canTalonShooterAngle.getError();
		
		Robot.canTalonShooterAngle.enable();
		
		if( error < 4){
			Robot.canTalonShooterAngle.disable();
			return true;
		}		
		else{
			return false;
		}
		
		
		
	}
	public static boolean movePID(int ref){
		

		error = (int) (ref - Robot.canTalonShooterAngle.get());
		
		long currentTime = System.currentTimeMillis();
		
		i = i + (currentTime - lastTime)*(error);
		
		d = (error - lastError)/(currentTime- lastTime);
		
		P = Kp * error;
		I = Ki * i;
		D = Kd * d;
		
		speed = P + I + D;
		
		if(Math.abs(speed) > 1) {
			if(speed > 1){
				speed = 1;
			}
			else{
				speed = -1;
			}
		}
		
		Robot.canTalonShooterAngle.set(speed);
		
		lastError = error;
		lastTime = currentTime;
			
		if (!Robot.shooterLimitTop.get() || !Robot.shooterLimitBot.get()){
			Robot.canTalonShooterAngle.set(0);
			return true;
		}
		else{
		
		if (Math.abs(error) <= 4){
			Robot.canTalonShooterAngle.set(0);
			return true;
		}
		else{
			return false;
		}
	}
				
		
	}
	
	public static void fire(double speed){
		
		Robot.canTalonTrigger.set(speed);
		
	}
	
	public static void spinFlyWheel(double speed){
		Robot.canTalonFlyWheel.set(speed);
	}
	
	public static void stopFlyWheel(){
		Robot.canTalonFlyWheel.set(0);
	}
	
	public static void stop(){
		
		Robot.canTalonFlyWheel.set(0);
		
	}
	

}
