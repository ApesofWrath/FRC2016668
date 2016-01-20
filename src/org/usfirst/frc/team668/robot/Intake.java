package org.usfirst.frc.team668.robot;

public class Intake {
	
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
	
	public static boolean movePID (int ref){
		
		error = ref - Robot.intakeEncoder.get();
		
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
		
		Robot.canTalonIntakeAngle.set(speed);
		
		lastError = error;
		currentTime = lastTime;
			
		if (!Robot.intakeLimitTop.get() || !Robot.intakeLimitBot.get()){
			Robot.canTalonIntakeAngle.set(0);
			return true;
		}else{
		if (Math.abs(error) <= 4){
			Robot.canTalonIntakeAngle.set(0);
			return true;
		}
		else{
			return false;
		}
	}
				
	
	}
	
	
	public static void spin(double speed){
		
		Robot.canTalonIntake.set(speed);
		
	}
	
	public static void spit(double speed){
		
		Robot.canTalonIntake.set(-speed);
		
	}
	
	public static void stop(){
		
		Robot.canTalonIntake.set(0.0);
		
	}
	
}