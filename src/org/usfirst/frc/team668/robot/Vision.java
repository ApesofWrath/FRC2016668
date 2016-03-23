package org.usfirst.frc.team668.robot;

public class Vision {
	
//	public static int getAngle(){
//		
//			int[] angles = {3205, 3215, 3225, 3225};
//		
//		if (Robot.distance < RobotMap.FIRE_DISTANCE_1){
//			return angles[0];
//		}
//		else if (Robot.distance > RobotMap.FIRE_DISTANCE_1 && Robot.distance < RobotMap.FIRE_DISTANCE_2){
//			return angles[1];
//		}
//		else if (Robot.distance > RobotMap.FIRE_DISTANCE_2 && Robot.distance < RobotMap.FIRE_DISTANCE_3){
//			return angles[2];
//		}
//		else {
//			return angles[3];
//		}
//	}
	
	public static int getAngle(){
		int angle;
		int yInt;
		int slope; 
		if (Robot.isBrightEyes){
			angle = 376;
			yInt = 363;
			slope = 10;
		}
		else{
			angle = 3222;
			yInt = 3204;
			slope = 20;
		}
		
		if (Robot.distance < 132){
			angle =  (int)(((Robot.distance - 84)/(48))*(slope))+ yInt;
		}
		System.out.println("Target " + angle);
		return angle;
	}
	
	public static boolean isAzimuthReady(){
		
		if ((Robot.azimuth < .5 || Robot.azimuth > 359.5) && (Robot.azimuth != 400)){
			return true;
		}
		else{
			return false;
		}
	}
	
	public static boolean isShotPossible(){
		
		if (Robot.distance > 0 && Robot.distance < 2000){
			return true;
		}
		else{
			return false;
		}
	}
	

}
