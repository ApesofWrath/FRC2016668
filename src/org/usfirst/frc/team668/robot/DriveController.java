
package org.usfirst.frc.team668.robot;

public class DriveController {
	
	public static double Kp, Ki;
	public static double error;
	public static double currentTime = 0;
	public static double lastTime = ((double)System.currentTimeMillis())/1000.0;
	public static double lastError = 0;
	public static double P, I;
	public static double i;
	public static double speed;
	
	public static int errorRight;
	public static double currentTimeRight = 0;
	public static double lastTimeRight = ((double)System.currentTimeMillis())/1000.0;
	public static double lastErrorRight = 0;
	public static double Kir;
	public static double Kdr;
	public static double Kpr;
	public static double iR = 0;
	public static double dR = 0;
	public static double Pr, Ir, Dr;
	public static double speedRight;

	public static int errorLeft;
	public static double currentTimeLeft = 0;
	public static double lastTimeLeft = ((double)System.currentTimeMillis())/1000.0;
	public static double lastErrorLeft = 0;
	public static double Kil;
	public static double Kdl;
	public static double Kpl;
	public static double iL = 0;
	public static double dL = 0;
	public static double Pl, Il, Dl;
	public static double speedLeft;

	
	public static void aimPI(){
		
		if (Robot.isBrightEyes){
			Kp = 1.7;
			Ki = 2.3;
		}
		else{
			Kp = 0;
		}
		error = Math.sin((Robot.azimuth*Math.PI)/180);

		
		currentTime = ((double)System.currentTimeMillis())/1000.0;
		
		if(Robot.azimuth > 355 || Robot.azimuth < 5){
			i = i + (currentTime-lastTime)*(error);
		}
		
		P = Kp * error;
		I = i*Ki;
		
		speed = P + I;
		
		if (Math.abs(speed) > 1){
			if ( speed > 0 ){
				speed = 1;
			}
			else {
				speed = -1;
			}
		}
		
		Robot.canTalonFrontRight.set(speed);
		Robot.canTalonRearRight.set(speed);
		Robot.canTalonFrontLeft.set(speed);
		Robot.canTalonRearLeft.set(speed);
	
		lastTime = currentTime;
		lastError = error;
		
		
		
	}
	public static boolean rightPID(int ref){
		
		if (Robot.isBrightEyes){
			Kil = 1;
			Kdl = 1;
			Kpl = 1;
		}
		else{
			Kil = 1;
			Kdl = 1;
			Kpl = 1;
		}
		errorRight= ref - Robot.canTalonRearRight.getEncPosition();
		
		currentTimeRight = ((double)System.currentTimeMillis())/1000.0;
		
		iR = iR + (currentTimeRight - lastTimeRight)*(errorRight-lastErrorRight);
		
		dR = (errorRight - lastErrorRight)/(currentTimeRight - lastTimeRight);
		
		Pr = Kpr * errorRight;
		Ir = Kir * iR;
		Dr = Kdr * dR;
		
		speedRight = Pr + Ir + Dr;
		
		if (Math.abs(speedRight) > 1){
			if ( speedRight > 0 ){
				speedRight = 1;
			}
			else {
				speedRight = -1;
			}
		}
		
		
		
		Robot.canTalonRearRight.set(speedRight);
		Robot.canTalonFrontRight.set(speedRight);
		
		lastErrorRight = errorRight;
		lastTimeRight = currentTimeRight;
		
		if (errorRight <= 4){
			return true;
		}
		else{
			return false;
		}
		
		
	}
	public static boolean leftPID(int ref){
		
		if (Robot.isBrightEyes){
			Kir = 1;
			Kdr = 1;
			Kpr = 1;
		}
		else{
			Kir = 1;
			Kdr = 1;
			Kpr = 1;
		}
		
		errorLeft= ref - Robot.canTalonRearLeft.getEncPosition();
		
		currentTimeLeft = ((double)System.currentTimeMillis())/1000.0;
		
		iL = iL + (currentTimeLeft - lastTimeLeft)*(errorLeft-lastErrorLeft);
		
		dL = (errorLeft - lastErrorLeft)/(currentTimeLeft - lastTimeLeft);
		
		Pl = Kpl * errorLeft;
		Il = Kil * iL;
		Dl = Kdl * dL;
		
		speedLeft = Pl + Il + Dl;
		
		if (Math.abs(speedLeft) > 1){
			if ( speedLeft > 0 ){
				speedLeft = 1;
			}
			else {
				speedLeft = -1;
			}
		}
		
		
		
		Robot.canTalonRearLeft.set(speedLeft);
		Robot.canTalonFrontLeft.set(speedLeft);
		
		lastErrorLeft = errorLeft;
		lastTimeLeft = currentTimeLeft;
		
		if (errorLeft <= 4){
			return true;
		}
		else{
			return false;
		}
	}
	public static boolean rightMove(int ref){
		
		if (Math.abs(ref - Robot.canTalonFrontRight.getEncPosition()) <= RobotMap.BANG_DRIVE_RANGE){
			return true;
		}
		else{
			if (ref > Robot.canTalonFrontRight.getEncPosition()){
				Robot.canTalonFrontRight.set(RobotMap.BANG_DRIVE_OUTPUT);
				Robot.canTalonRearRight.set(RobotMap.BANG_DRIVE_OUTPUT);
			}
			else{
				Robot.canTalonFrontRight.set(-RobotMap.BANG_DRIVE_OUTPUT);
				Robot.canTalonRearRight.set(-RobotMap.BANG_DRIVE_OUTPUT);
			}
			return false;
		}
	}
	public static boolean leftMove(int ref){
		
		if (Math.abs(ref - Robot.canTalonFrontLeft.getEncPosition()) <= RobotMap.BANG_DRIVE_RANGE){
			return true;
		}
		else{
			if (ref > Robot.canTalonFrontLeft.getEncPosition()){
				Robot.canTalonFrontLeft.set(RobotMap.BANG_DRIVE_OUTPUT);
				Robot.canTalonRearLeft.set(RobotMap.BANG_DRIVE_OUTPUT);
			}
			else{
				Robot.canTalonFrontLeft.set(-RobotMap.BANG_DRIVE_OUTPUT);
				Robot.canTalonRearLeft.set(-RobotMap.BANG_DRIVE_OUTPUT);
			}
			return false;
		}
	}
	public static void stop(){
		Robot.robotDrive.drive(0.0, 0.0);
//		Robot.canTalonFrontLeft.set(0);
//		Robot.canTalonFrontRight.set(0);
//		Robot.canTalonRearLeft.set(0);
//		Robot.canTalonRearRight.set(0);
		
	}
	public static void turnInPlace(double speed){
		//System.out.println("HERE");
		Robot.canTalonFrontLeft.set(speed);
		Robot.canTalonFrontRight.set(speed);
		Robot.canTalonRearLeft.set(speed);
		Robot.canTalonRearRight.set(speed);
	}
	public static void drive(double speed){
		Robot.canTalonFrontLeft.set(speed);
		Robot.canTalonFrontRight.set(-speed);
		Robot.canTalonRearLeft.set(speed);
		Robot.canTalonRearRight.set(-speed);
	}
	public static void aim(double speed){
		if ((Robot.azimuth > RobotMap.AZIMUTH_RANGE) && (Robot.azimuth < (360 - RobotMap.AZIMUTH_RANGE)) 
				&& (Robot.azimuth !=400)){
			if (Robot.azimuth > 180){
				turnInPlace(-speed);
				System.out.println("HERE");
			}
			else{
				turnInPlace(speed);
				System.out.println("HERE");
			}
		}
		else{
			stop();
		}
	}
}
