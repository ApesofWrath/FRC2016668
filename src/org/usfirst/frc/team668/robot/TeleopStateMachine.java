package org.usfirst.frc.team668.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;

public class TeleopStateMachine {
		
	public static int reference;
	public static long time;
	public static void stateMachine(boolean optic, boolean optic2, boolean closeAngle, boolean farAngle, 
			boolean isFire, boolean isLower, boolean isCollapse, boolean isManual, boolean isReturn){
			
		boolean isClose = true;
		boolean isFar = false;
		
		if(isManual){
			Intake.stop();
			RobotMap.currentState = RobotMap.MANUAL_OVERRIDE_STATE;
		}
		
		switch (RobotMap.currentState){

		case RobotMap.INIT_STATE:
			
			System.out.println("Here");
			Robot.intakePiston.set(DoubleSolenoid.Value.kForward);
			boolean completed = Shooter.moveHood(RobotMap.CLOSE_ANGLE_VALUE);
			if(completed){
				Shooter.stopAngle();
				RobotMap.currentState = RobotMap.SET_CLOSE_PID_STATE;
			}
			break;

		case RobotMap.WAIT_FOR_BUTTON_STATE:
			
			System.out.println("hehehe");
			if(closeAngle){
				RobotMap.currentState = RobotMap.CLOSE_ANGLE_STATE;
			}

			else if (farAngle){
				RobotMap.currentState = RobotMap.FAR_ANGLE_STATE;
			}

			else if (isFire){
				RobotMap.currentState = RobotMap.INIT_FIRE_STATE;
			}

			else if (isLower){
				RobotMap.currentState = RobotMap.LOWER_INTAKE_STATE;
			} 

			break;

		case RobotMap.LOWER_INTAKE_STATE:
			
			System.out.println("Spin");
			
			Robot.intakePiston.set(DoubleSolenoid.Value.kForward);
			
			if (optic && optic2){ //runs until one of the sensors are false. Inverted for that reason
				Intake.spin(.8);
			}
			else if (isCollapse){
				RobotMap.currentState = RobotMap.COLLAPSE_STATE;
			}
			else{
				Intake.stop();
				RobotMap.currentState = RobotMap.WAIT_FOR_BUTTON_STATE;
			}

			break;

		case RobotMap.INIT_FIRE_STATE:

			if (isFar == true){
				RobotMap.currentState = RobotMap.SET_FAR_PID_STATE;
			}

			else if (isClose == true){
				RobotMap.currentState = RobotMap.CLOSE_FIRE_STATE;
			}

			else {
				RobotMap.currentState = RobotMap.WAIT_FOR_BUTTON_STATE;
			}

			break;
			
		case RobotMap.SET_FAR_PID_STATE:
			
			reference = 0; //gets speed
			Shooter.setPID(reference);
			RobotMap.currentState = RobotMap.FAR_FIRE_STATE;
			break;
			
		case RobotMap.SET_CLOSE_PID_STATE:	
			Shooter.setPID(RobotMap.CONSTANT_SPEED);
			RobotMap.currentState = RobotMap.WAIT_FOR_BUTTON_STATE;
			break;
			
		case RobotMap.FAR_FIRE_STATE:
			
			if (reference - Robot.canTalonFlyWheel.getSpeed() <= 0){
				Intake.spin(.8);
				if(optic && optic2){
					Robot.canTalonFlyWheel.disable();
					Intake.stop();
					time = System.currentTimeMillis();
					RobotMap.currentState = RobotMap.BALL_CLEAR_STATE;
				}
			}
			break;
			
		case RobotMap.BALL_CLEAR_STATE:
		
			
			if (System.currentTimeMillis() >= time + 1000){
				RobotMap.currentState = RobotMap.SET_CLOSE_PID_STATE;
			}

		case RobotMap.CLOSE_FIRE_STATE:
			
			Intake.spin(.8);
			if(optic && optic2){
				Intake.stop();
				RobotMap.currentState = RobotMap.BALL_CLEAR_STATE;
			}  
			break;

		case RobotMap.CLOSE_ANGLE_STATE:

			isFar = false;
			isClose = true;
			boolean reached = Shooter.moveHood(RobotMap.CLOSE_ANGLE_VALUE);
			if(reached){
				Shooter.stopAngle();
				RobotMap.currentState = RobotMap.WAIT_FOR_BUTTON_STATE;
			}
			break;

		case RobotMap.FAR_ANGLE_STATE:

			isFar = true;
			isClose = false;
			boolean finished = Shooter.moveHood(RobotMap.FAR_ANGLE_VALUE);
			if(finished){
				Shooter.stopAngle();
				RobotMap.currentState = RobotMap.WAIT_FOR_BUTTON_STATE;
			}
			break;

		case RobotMap.COLLAPSE_STATE:

			isFar = false;
			isClose = false;
			Robot.intakePiston.set(DoubleSolenoid.Value.kForward);
			boolean done = Shooter.moveHood(RobotMap.COLLAPSE_ANGLE_VALUE);
			if(done){
				Shooter.stopAngle();
				RobotMap.currentState = RobotMap.WAIT_FOR_BUTTON_STATE;
			}
			break;
			
		case RobotMap.MANUAL_OVERRIDE_STATE:
			
			if (isReturn){
				RobotMap.currentState = RobotMap.INIT_STATE;
			}
			break;
			
		default:
			//this should never ever happen
			RobotMap.currentState = RobotMap.MANUAL_OVERRIDE_STATE;
			break;
			
		}
	}
	
	

}
