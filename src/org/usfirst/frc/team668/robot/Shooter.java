package org.usfirst.frc.team668.robot;

import edu.wpi.first.wpilibj.CANTalon;

public class Shooter {
	//i
	
	public static int error;
	public static long currentTime = 0;
	public static long lastTime = System.currentTimeMillis();
	public static double lastError = 0;
	public static double Ki = 1;
	public static double Kd = 1;
	public static double Kp = 1;
	public static double i = 0;
	public static double d = 0;
	public static double P, I , D;
	public static double speed;
	
	public static void setPID(double ref){

		Robot.canTalonFlyWheel.changeControlMode(CANTalon.TalonControlMode.Speed);
		
		Robot.canTalonFlyWheel.setPID(2, .078, 1);
		
		Robot.canTalonFlyWheel.setSetpoint(ref);
		
		Robot.canTalonFlyWheel.enable();
		
		error = (int) (ref - Robot.canTalonFlyWheel.getSpeed());
		
//		if (error <= 10){
//			
//			Robot.canTalonFlyWheel.set(0);
//			
//			Robot.canTalonFlyWheel.disable();
//			
//			return true;
//		}
//		else {
//			return false;
//		}
	}
	
	
	public static void movePotPID(int ref){
		
		error = ref - Robot.pot.getValue();
		
		currentTime = System.currentTimeMillis();
		
		i = i + (currentTime - lastTime)*(error - lastError);
		
		d = (error - lastError)/(currentTime - lastTime);
		
		P = Kp * error;
		I = Ki * i;
		D = Kd * d;
		 
		speed = P + I + D;
		
		if (Math.abs(speed) > 1){
			if (speed > 0){
				speed = 1;
			}
			else{
				speed = -1;
			}
		}
		
		if (!Robot.limitSwitch.get() && !Robot.limitSwitchTwo.get()){
			if (!Robot.limitSwitch.get()){
				if (speed > 0){
					speed = 0;
				}
			}
			else{
				if (speed < 0){
					speed = 0;
				}
			}
		}
		Robot.canTalonShooterAngle.set(speed);
		Robot.canTalonShooterAngleTwo.set(-speed);
		
		lastError = error;
		lastTime = currentTime;
	
	
		
		
	}
	public static void moveHood(double speed){
		
		Robot.canTalonShooterAngle.set(speed);
		Robot.canTalonShooterAngleTwo.set(-speed);
	}
	
	public static boolean moveHoodBang(double ref){
		
		if(Math.abs(Robot.pot.getValue() - ref) <= RobotMap.ACCEPTABLE_HOOD_RANGE){
			stopAngle();
			return true;
		}
		else if(Robot.pot.getValue() > ref){
			Robot.canTalonShooterAngle.set(RobotMap.HOOD_SPEED);
			Robot.canTalonShooterAngleTwo.set(-RobotMap.HOOD_SPEED);
			return false;
		}
		else{
			Robot.canTalonShooterAngle.set(-RobotMap.HOOD_SPEED);
			Robot.canTalonShooterAngleTwo.set(RobotMap.HOOD_SPEED);
			return false;
		}
		
	
	}
	
	
	
	public static boolean hoodCollapse(){
		Robot.canTalonShooterAngle.set(-.7);
		Robot.canTalonShooterAngleTwo.set(.7);
		
		if (Robot.limitSwitch.get() == false){
			return true;
		}
		return false;
	}
	
	
	public static void fire(double speed){
		
		Robot.canTalonIntake.set(speed);
		
	}
	
	public static void spinFlyWheel(double speed){
		
		Robot.canTalonFlyWheel.set(speed);
		//Robot.canTalonFlyWheelTwo.set(-speed);
		
	}
	
	public static void stopFlyWheel(){
		
		Robot.canTalonFlyWheel.set(0);
		//Robot.canTalonFlyWheelTwo.set(0);
	
	}
	
	public static void stop(){
		
		Robot.canTalonIntake.set(0);
		
	}
	
	public static void stopAngle(){
		
		Robot.canTalonShooterAngle.set(0);
		Robot.canTalonShooterAngleTwo.set(0);
	
	}

}
