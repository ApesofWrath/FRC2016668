package org.usfirst.frc.team668.robot;

public class Vision {
	
	public static int getAngle(){
		
			int[] angles = {1, 2, 3, 4};
		
		if (Robot.distance < RobotMap.FIRE_DISTANCE_1){
			return angles[0];
		}
		else if (Robot.distance > RobotMap.FIRE_DISTANCE_1 && Robot.distance < RobotMap.FIRE_DISTANCE_2){
			return angles[1];
		}
		else if (Robot.distance > RobotMap.FIRE_DISTANCE_2 && Robot.distance < RobotMap.FIRE_DISTANCE_3){
			return angles[2];
		}
		else {
			return angles[3];
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
