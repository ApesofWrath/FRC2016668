package org.usfirst.frc.team668.robot;

public class DriveController {
	
	public static int errorRight;
	public static long currentTimeRight = 0;
	public static long lastTimeRight = System.currentTimeMillis();
	public static double lastErrorRight = 0;
	public static double Kir = 1;
	public static double Kdr = 1;
	public static double Kpr = 1;
	public static double iR = 0;
	public static double dR = 0;
	public static double Pr, Ir, Dr;
	public static double speedRight;

	public static int errorLeft;
	public static long currentTimeLeft = 0;
	public static long lastTimeLeft = System.currentTimeMillis();
	public static double lastErrorLeft = 0;
	public static double Kil = 1;
	public static double Kdl = 1;
	public static double Kpl = 1;
	public static double iL = 0;
	public static double dL = 0;
	public static double Pl, Il, Dl;
	public static double speedLeft;

	public static boolean rightPID(int ref){
		
		errorRight= ref - Robot.canTalonRearRight.getEncPosition();
		
		currentTimeRight = System.currentTimeMillis();
		
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
		
		errorLeft= ref - Robot.canTalonRearLeft.getEncPosition();
		
		currentTimeLeft = System.currentTimeMillis();
		
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
	
	public static void stop(){
		
		Robot.robotDrive.drive(0.0, 0.0);
//		Robot.canTalonFrontLeft.set(0);
//		Robot.canTalonFrontRight.set(0);
//		Robot.canTalonRearLeft.set(0);
//		Robot.canTalonRearRight.set(0);
		
	}
	public static void turnInPlace(double speed){
		
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
}
