package org.usfirst.frc.team668.robot;

public class Vision {
	
	public static double getSpeed(){
		
		double[] speeds = {1, 2, 3, 4};
		
		if (Robot.distance < RobotMap.FIRE_DISTANCE_1){
			return speeds[0];
		}
		else if (Robot.distance > RobotMap.FIRE_DISTANCE_1 && Robot.distance < RobotMap.FIRE_DISTANCE_2){
			return speeds[1];
		}
		else if (Robot.distance > RobotMap.FIRE_DISTANCE_2 && Robot.distance < RobotMap.FIRE_DISTANCE_3){
			return speeds[2];
		}
		else {
			return speeds[3];
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
