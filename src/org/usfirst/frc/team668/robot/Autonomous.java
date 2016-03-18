package org.usfirst.frc.team668.robot;

public class Autonomous {

	public static boolean fired = false;
	public static void driveUnderBarAuton(Robot r){

		boolean doneRight = DriveController.rightPID(RobotMap.DRIVE_UNDER_BAR_RIGHT_DISTANCE);
		boolean doneLeft = DriveController.leftPID(RobotMap.DRIVE_UNDER_BAR_LEFT_DISTANCE);
		
		while(r.isAutonomous() && r.isEnabled() && (!doneRight || !doneLeft)){
			doneRight = DriveController.rightPID(RobotMap.DRIVE_UNDER_BAR_RIGHT_DISTANCE);
			doneLeft = DriveController.leftPID(RobotMap.DRIVE_UNDER_BAR_LEFT_DISTANCE);
		}
		
		DriveController.stop();
		
		return;
	
	}
	
	public static void driveAndShootCameraAuton(Robot r){
		
		RobotMap.hoodState = RobotMap.HOOD_ZERO_STATE;
		
		boolean doneRight = DriveController.rightMove(RobotMap.DRIVE_AND_SHOOT_RIGHT_DISTANCE);
		boolean doneLeft = DriveController.leftMove(RobotMap.DRIVE_AND_SHOOT_LEFT_DISTANCE);
		
		while(r.isAutonomous() && r.isEnabled() && (!doneRight || !doneLeft)){
			doneRight = DriveController.rightMove(RobotMap.DRIVE_AND_SHOOT_RIGHT_DISTANCE);
			doneLeft = DriveController.leftMove(RobotMap.DRIVE_AND_SHOOT_LEFT_DISTANCE);
		}
		
		DriveController.stop();
		
		while(r.isAutonomous() && r.isEnabled() && Robot.distance == 0.0){
			DriveController.turnInPlace(RobotMap.DRIVE_AND_SHOOT_TURN_SPEED);
		}
		
		while(r.isAutonomous() && r.isEnabled() && (Robot.azimuth > RobotMap.AZIMUTH_RANGE)
				|| (Robot.azimuth < 360 - RobotMap.AZIMUTH_RANGE) && (Robot.azimuth !=400) ){
			DriveController.aimPI();
		}
		
		int ref = RobotMap.FAR_FIRE_SPEED;
		Shooter.setPID(ref);
		
		RobotMap.hoodState = RobotMap.HOOD_GET_STATE;
		while ( Math.abs(Shooter.angle - Robot.pot.getValue()) <= RobotMap.ACCEPTABLE_HOOD_RANGE){
			RobotMap.hoodState = RobotMap.HOOD_GET_STATE;
		}
		
		while (r.isEnabled() && r.isAutonomous() && !fired){
			if (Math.abs(ref - Robot.canTalonFlyWheel.getSpeed()) <= RobotMap.FAR_FIRE_SPEED_RANGE){ //waits for the speed of the motor to be correct 
				Intake.spin(RobotMap.FIRE_INTAKE_SPEED);
				if (Robot.opticSensor.get()){ //the sensor returns true once the ball has been fired. 
					long time = System.currentTimeMillis(); //wait for the ball to be completely out of the firing chamber
					if (System.currentTimeMillis() - time >= RobotMap.BALL_WAIT_TIME){
						Robot.canTalonFlyWheel.disable(); //this might not allow the flywheel to start back up
						Intake.stop(); //stops the intake 
						fired = true; //kills the firing sequence
					}
				}
			}
		}
		
		return;
		
	}
	
	public static void driveAndShootPIDAuton(Robot r){
		
		boolean doneRight = DriveController.rightMove(RobotMap.DRIVE_AND_SHOOT_RIGHT_DISTANCE);
		boolean doneLeft = DriveController.leftMove(RobotMap.DRIVE_AND_SHOOT_LEFT_DISTANCE);
		
		while(r.isAutonomous() && r.isEnabled() && (!doneRight || !doneLeft)){
			doneRight = DriveController.rightMove(RobotMap.DRIVE_AND_SHOOT_RIGHT_DISTANCE);
			doneLeft = DriveController.leftMove(RobotMap.DRIVE_AND_SHOOT_LEFT_DISTANCE);
		}
		
		DriveController.stop();
		
		boolean turnRight = DriveController.rightMove(RobotMap.TURN_RIGHT_AUTON_DISTANCE);
		boolean turnLeft = DriveController.leftMove(RobotMap.TURN_LEFT_AUTON_DISTANCE);
		
		while (r.isEnabled() && r.isAutonomous() && (!turnRight || !turnLeft)){
			turnRight = DriveController.rightMove(RobotMap.TURN_RIGHT_AUTON_DISTANCE);
			turnLeft = DriveController.leftMove(RobotMap.TURN_LEFT_AUTON_DISTANCE);
		}
		

		int ref = RobotMap.FAR_FIRE_SPEED;
		Shooter.setPID(ref);
		
		RobotMap.hoodState = RobotMap.HOOD_GET_STATE;
		while ( Math.abs(Shooter.angle - Robot.pot.getValue()) <= RobotMap.ACCEPTABLE_HOOD_RANGE){
			RobotMap.hoodState = RobotMap.HOOD_GET_STATE;
		}
		
		
		while (r.isEnabled() && r.isAutonomous() && !fired){
			if (Math.abs(ref - Robot.canTalonFlyWheel.getSpeed()) <= RobotMap.FAR_FIRE_SPEED_RANGE){ //waits for the speed of the motor to be correct 
				Intake.spin(RobotMap.FIRE_INTAKE_SPEED);
				if (Robot.opticSensor.get()){
					long time = System.currentTimeMillis();
					if ( System.currentTimeMillis() - time >= RobotMap.BALL_WAIT_TIME){
						//Robot.canTalonFlyWheel.disable();
						Intake.stop();
						fired = true;
					}
				}
			}
		}
		
		return;
		
		
	}
	
	public static void driveToDefenseAuton(Robot r){
		boolean doneRight = DriveController.rightMove(RobotMap.DRIVE_TO_DEFENSE_RIGHT_DISTANCE);
		boolean doneLeft = DriveController.leftMove(RobotMap.DRIVE_TO_DEFENSE_LEFT_DISTANCE);
		
		while(r.isAutonomous() && r.isEnabled() && (!doneRight || !doneLeft)){
			doneRight = DriveController.rightMove(RobotMap.DRIVE_TO_DEFENSE_RIGHT_DISTANCE);
			doneLeft = DriveController.leftMove(RobotMap.DRIVE_TO_DEFENSE_LEFT_DISTANCE);
		}
		
		DriveController.stop();
		
		return;
	}
	public static void stopAuton(){
		
		return;
		
	}
	
	public static void leeroyJenkins(Robot r){
		
		long time = System.currentTimeMillis();
		
		while (r.isEnabled() && r.isAutonomous() && (Math.abs(Robot.canTalonFrontRight.getEncPosition()) < Math.abs(RobotMap.LEEROY_JENKINS_DISTANCE))
				&& (Math.abs(Robot.canTalonFrontLeft.getEncPosition()) < Math.abs(RobotMap.LEEROY_JENKINS_DISTANCE))){
			DriveController.drive(RobotMap.LEEROY_JENKINS_SPEED);
		}
		
		DriveController.stop();
		return;
		
	}
	
	public static void chivelDeFriseAuton(Robot r){
		
		boolean doneRight = DriveController.rightPID(RobotMap.DRIVE_TO_DEFENSE_RIGHT_DISTANCE);
		boolean doneLeft = DriveController.leftPID(RobotMap.DRIVE_TO_DEFENSE_LEFT_DISTANCE);
		
		while(r.isAutonomous() && r.isEnabled() && (!doneRight || !doneLeft)){
			doneRight = DriveController.rightPID(RobotMap.DRIVE_TO_DEFENSE_RIGHT_DISTANCE);
			doneLeft = DriveController.leftPID(RobotMap.DRIVE_TO_DEFENSE_LEFT_DISTANCE);
		}
		
		DriveController.stop();

	}
	
	public static void spyBotShotAutonomous(Robot r){
		
		boolean completeRight = DriveController.rightMove(0);
		boolean completeLeft = DriveController.leftMove(0);
		
		while(r.isAutonomous() && r.isEnabled() && (!completeRight || !completeLeft)){
			completeRight = DriveController.rightPID(0);
			completeLeft = DriveController.leftPID(0);
		}
		
		while (r.isAutonomous() && r.isEnabled() && (Robot.azimuth > RobotMap.AZIMUTH_RANGE)
				|| (Robot.azimuth < 360 - RobotMap.AZIMUTH_RANGE) && (Robot.azimuth !=400) ){
			DriveController.aimPI();
		}
		
		DriveController.stop();
		
		int ref = RobotMap.FAR_FIRE_SPEED;
		Shooter.setPID(ref);
		
		RobotMap.hoodState = RobotMap.HOOD_GET_STATE;
		while ( Math.abs(Shooter.angle - Robot.pot.getValue()) <= RobotMap.ACCEPTABLE_HOOD_RANGE){
			RobotMap.hoodState = RobotMap.HOOD_GET_STATE;
		}
		
		while (r.isEnabled() && r.isAutonomous() && !fired){
			if (Math.abs(ref - Robot.canTalonFlyWheel.getSpeed()) <= RobotMap.FAR_FIRE_SPEED_RANGE){ //waits for the speed of the motor to be correct 
				Intake.spin(RobotMap.FIRE_INTAKE_SPEED);
				if (Robot.opticSensor.get()){
					long time = System.currentTimeMillis();
					if ( System.currentTimeMillis() - time >= RobotMap.BALL_WAIT_TIME){
						Robot.canTalonFlyWheel.disable(); //this might not allow the flywheel to start back up try adding .enable()
						Intake.stop();
						fired = true;
					}
				}
			}
		}
		
		return;
		
	}
	
	
	
}
												