package org.usfirst.frc.team668.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Shooter {
	//i
	public static int angle;
	
	public static int error;
	public static double currentTime = 0;
	public static double lastTime = ((double)System.currentTimeMillis())/1000.0;
	public static double lastError = 0;
	public static double Ki = 0.0000007;//38
	public static double Kd = 0.000;//8
	public static double Kp = .003;
	public static double i = 0;
	public static double d = 0;
	public static double P, I , D, F;
	public static double speed;
	
	public static void setPID(double ref){
		//Robot.canTalonFlyWheel.ConfigFwdLimitSwitchNormallyOpen(false);
		Robot.canTalonFlyWheel.changeControlMode(CANTalon.TalonControlMode.Speed);
//		Robot.canTalonFlyWheel.setP(.00002);
//		Robot.canTalonFlyWheel.setI(.00078);
//		Robot.canTalonFlyWheel.setD(.000000009);
		//Robot.canTalonFlyWheel.setPID(.0002, .00078, .0001);
		Robot.canTalonFlyWheel.set(ref);
		//Robot.canTalonFlyWheel.enable();
		//error = (int) (ref - Robot.canTalonFlyWheel.getSpeed());
		
		System.out.println(error);
		
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
		
		if (Robot.isBrightEyes){
			Ki = 0.0095;//38
			Kd = 0.0003;//8
			Kp = .039;
		}
		else {
			Ki = 0.0002;
			Kd = 0.0003;
			Kp = 0.009;
		}
		error = ref - Robot.pot.getValue();
		
		currentTime = ((double)System.currentTimeMillis())/1000.0;
		
		i = i + (currentTime - lastTime)*(error);
		
		d = (error - lastError)/(currentTime - lastTime);
		
		P = Kp * error;
		I = Ki * i;
		D = Kd * d;
		
		if (error < 0){
			if (Robot.isBrightEyes){
				F = -.15; // make sure you know that F is not in VOlts
			}
			else{
				F = 0;
			}
		}
		else if (error >= 0){
			if (Robot.isBrightEyes){
				F = -.05;
			}
			else{
				F = .05;
			}
		}
		 
		speed = P + I + D + F;
		
		if (Math.abs(speed) > 1){
			if (speed > 0){
				speed = 1;
			}
			else{
				speed = -1;
			}
		}
		
//		if (!Robot.limitSwitch.get() && !Robot.limitSwitchTwo.get()){
//			if (!Robot.limitSwitch.get()){
//				if (speed > 0){
//					speed = 0;
//				}
//			}
//			else{
//				if (speed < 0){
//					speed = 0;
//				}
//			}
//		}
		Robot.canTalonShooterAngle.set(-speed);
	//	Robot.canTalonShooterAngleTwo.set(-speed);
		
		lastError = error;
		lastTime = currentTime;
	
	
		
		
	}
	public static void moveHood(double speed){
		
		Robot.canTalonShooterAngle.set(speed);
		//Robot.canTalonShooterAngleTwo.set(-speed);
	}
	
	public static boolean moveHoodBang(double ref){
		
		if(Math.abs(Robot.pot.getValue() - ref) <= RobotMap.ACCEPTABLE_HOOD_RANGE){
			stopAngle();
			return true;
		}
		else if(Robot.pot.getValue() > ref){
			Robot.canTalonShooterAngle.set(RobotMap.HOOD_SPEED);
			//Robot.canTalonShooterAngleTwo.set(-RobotMap.HOOD_SPEED);
			return false;
		}
		else{
			Robot.canTalonShooterAngle.set(-RobotMap.HOOD_SPEED);
		//	Robot.canTalonShooterAngleTwo.set(RobotMap.HOOD_SPEED);
			return false;
		}
		
	
	}
	
	
	
	public static boolean hoodCollapse(){
		Robot.canTalonShooterAngle.set(-.7);
	//	Robot.canTalonShooterAngleTwo.set(.7);
		
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
	//	Robot.canTalonShooterAngleTwo.set(0);
	
	}
	
	public static void hoodStateMachine(boolean manualHood){
		
		switch(RobotMap.hoodState){
		
		case RobotMap.HOOD_CLOSE_SHOT_STATE:
			SmartDashboard.putString("HOOD: " , "CLOSE");
			if (Robot.isBrightEyes){
				angle = RobotMap.BRIGHT_CLOSE_ANGLE_VALUE;
			}
			else{
				angle = RobotMap.CLOSE_ANGLE_VALUE;
			}
			movePotPID(angle);
			break;
		
		case RobotMap.HOOD_GET_STATE:
			SmartDashboard.putString("HOOD: " , "ZERO");
			angle = Vision.getAngle();
			RobotMap.hoodState = RobotMap.HOOD_SET_FAR_ANGLE_STATE;
			break;
			
		case RobotMap.HOOD_SET_FAR_ANGLE_STATE:
			SmartDashboard.putString("HOOD: " , "FAR");
			System.out.println("ANGLE: " + angle);
			movePotPID(angle);
			break;
		
		case RobotMap.HOOD_ZERO_STATE:
			SmartDashboard.putString("HOOD: " , "ZERO");
			Robot.canTalonShooterAngle.set(0);
			i = 0.0;
			d = 0.0;
			I = 0.0;
			D = 0.0;
			lastError = 0; //TODO: make a constant
			lastTime = ((double)System.currentTimeMillis())/1000.0;
			break;
		
		case RobotMap.HOOD_LOW_GOAL_SHOT_STATE:
			SmartDashboard.putString("HOOD: ", "Low Goal Shot");
			if(Robot.isBrightEyes){
				angle = RobotMap.BRIGHT_LOW_GOAL_ANGLE;
			}
			else{
				angle = RobotMap.LOW_GOAL_ANGLE;
			}
			movePotPID(angle);
			break;
			
		case RobotMap.HOOD_MANUAL_FAR_STATE:
			SmartDashboard.putString("HOOD: " , "MANUAL FAR");
			if(Robot.isBrightEyes){
				angle = RobotMap.BRIGHT_FAR_ANGLE_VALUE;
			}
			else{
				angle = RobotMap.FAR_ANGLE_VALUE;
			}
			movePotPID(angle);
			break;
		
		case RobotMap.HOOD_MANUAL_STATE:
			SmartDashboard.putString("HOOD: " , "MANUAL CLOSE");
			if (manualHood){
				Shooter.moveHood(-Robot.joyOp.getY()/3);
			}
			else{
				Shooter.stopAngle();
			}

			break;
			}
		
		
	}

}
