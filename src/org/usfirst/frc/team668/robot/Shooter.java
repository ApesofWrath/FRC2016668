package org.usfirst.frc.team668.robot;

import edu.wpi.first.wpilibj.CANTalon;

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
		
		if (Robot.isTestRobot){
			Ki = 0.0000007;//38
			Kd = 0.000;//8
			Kp = .003;
		}
		else {
			Ki = 0.0003;
			Kd = 0;
			Kp = 0.015;
		}
		error = ref - Robot.pot.getValue();
		
		currentTime = ((double)System.currentTimeMillis())/1000.0;
		
		i = i + (currentTime - lastTime)*(error);
		
		d = (error - lastError)/(currentTime - lastTime);
		
		P = Kp * error;
		I = Ki * i;
		D = Kd * d;
		
		if (error < 0){
			F = -.15; // make sure you know that F is not in VOlts!
		}
		else if (error >= 0){
			F = -.05;
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
			Robot.canTalonShooterAngle.set(-RobotMap.HOOD_SPEED);
			//Robot.canTalonShooterAngleTwo.set(-RobotMap.HOOD_SPEED);
			return false;
		}
		else{
			Robot.canTalonShooterAngle.set(RobotMap.HOOD_SPEED);
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
			movePotPID(RobotMap.CLOSE_ANGLE_VALUE);
			break;
		
		case RobotMap.HOOD_GET_STATE:
			angle = Vision.getAngle();
			movePotPID(angle);	
			break;
		
		case RobotMap.HOOD_ZERO_STATE:
			Robot.canTalonShooterAngle.set(0);
			break;
		
		case RobotMap.HOOD_MANUAL_FAR_STATE:
			movePotPID(RobotMap.FAR_ANGLE_VALUE);
			break;
		
		case RobotMap.HOOD_MANUAL_STATE:
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
