package org.usfirst.frc.team668.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TeleopStateMachine {
		
	public static boolean canReverse = true;
	public static double reference;
	public static long time;
	public static void stateMachine(boolean optic, boolean closeAngle, boolean farAngle, 
			boolean isFire, boolean isLower, boolean isCollapse, boolean isManual,
			boolean isReturn){
			
		boolean isClose = true;
		boolean isFar = false;
		
		
		if(isFar = true){
			SmartDashboard.putBoolean("Ready For Far Shot", Vision.isShotPossible());
		}
		else{
			SmartDashboard.putBoolean("Ready For Far Shot", false);
		}
		
		
		if(isManual && RobotMap.currentState != RobotMap.MANUAL_OVERRIDE_STATE){
			Intake.stop();
			Shooter.stopAngle();
			Shooter.stopFlyWheel();
			RobotMap.currentState = RobotMap.MANUAL_OVERRIDE_STATE;
		}
		
		switch (RobotMap.currentState){

		case RobotMap.INIT_STATE:
			
			//System.out.println("Here");
			SmartDashboard.putString("State: ", "Init State");
			Shooter.stopAngle();
			Shooter.stopFlyWheel();
			Intake.stop();
			isClose = true;
			isFar = false;
			Robot.intakePiston.set(DoubleSolenoid.Value.kForward);
			boolean completed = Shooter.moveHoodBang(RobotMap.CLOSE_ANGLE_VALUE);
			if(completed){
				Shooter.stopAngle();
				RobotMap.currentState = RobotMap.SET_CLOSE_PID_STATE;
			}
			break;

		case RobotMap.WAIT_FOR_BUTTON_STATE:
			SmartDashboard.putString("State: ", "Wait For Button State");
			canReverse = true;
			//Prints whether a ball is ready to shoot
			if (!optic){
				SmartDashboard.putBoolean("BALL IN PLACE:", true);
			}
			else{
				SmartDashboard.putBoolean("BALL IN PLACE:", false);
			}
			
			//buttons
			if(closeAngle){
				RobotMap.currentState = RobotMap.CLOSE_ANGLE_STATE;
			}

			else if (farAngle){
				RobotMap.currentState = RobotMap.FAR_ANGLE_STATE;
			}

			else if (isFire){
				if (!optic){ //checks if the ball is in position
				RobotMap.currentState = RobotMap.INIT_FIRE_STATE;
				}
			}

			else if (isLower){
				RobotMap.currentState = RobotMap.LOWER_INTAKE_STATE;
			} 

			break;

		case RobotMap.LOWER_INTAKE_STATE:
			SmartDashboard.putString("State: ", "Lower Intake State");
			System.out.println("Spin");
			
			Robot.intakePiston.set(DoubleSolenoid.Value.kForward);
			
			if (optic ){ //runs until one of the sensors are false. Inverted for that reason
				Intake.spin(.8);
			}
			else{
				Intake.stop();
				RobotMap.currentState = RobotMap.WAIT_FOR_BUTTON_STATE;
			}
			
			 if (isCollapse){ //TODO: consider putting this outside the switch
				Intake.stop();//
				RobotMap.currentState = RobotMap.COLLAPSE_STATE;//
			}//
			 if (isReturn){
				Intake.stop();
				RobotMap.currentState = RobotMap.WAIT_FOR_BUTTON_STATE;
			}

			break;

		case RobotMap.INIT_FIRE_STATE:
			SmartDashboard.putString("State: ", "Init Fire State");	
			canReverse = false;
			if (isFar == true && Vision.isShotPossible()){
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
			SmartDashboard.putString("State: ", "Set Far PID State");
			reference = Vision.getSpeed(); //gets speed. Getter method for speed will go here
			Shooter.setPID(reference);
			RobotMap.currentState = RobotMap.FAR_FIRE_STATE;
			break;
			
		case RobotMap.SET_CLOSE_PID_STATE:	
			SmartDashboard.putString("State: ", "Set Close PID State");		
			Shooter.setPID(RobotMap.CONSTANT_SPEED);
			RobotMap.currentState = RobotMap.WAIT_FOR_BUTTON_STATE;
			break;
			
		case RobotMap.FAR_FIRE_STATE:
			SmartDashboard.putString("State: ", "Far Fire State");
			if (reference - Robot.canTalonFlyWheel.getSpeed() <= RobotMap.FAR_FIRE_SPEED_RANGE){
				Intake.spin(RobotMap.FIRE_INTAKE_SPEED);
				if(optic){ //optic sensors no longer see the ball
					time = System.currentTimeMillis();
					RobotMap.currentState = RobotMap.BALL_CLEAR_STATE;
				}
			}
			break;
			
		case RobotMap.BALL_CLEAR_STATE:
			
			SmartDashboard.putString("State: ", "Ball Clear State");			
			if (System.currentTimeMillis() >= time + RobotMap.BALL_WAIT_TIME){
				Intake.stop();
				Robot.canTalonFlyWheel.disable();
				RobotMap.currentState = RobotMap.SET_CLOSE_PID_STATE;
			}

		case RobotMap.CLOSE_FIRE_STATE:
			SmartDashboard.putString("State: ", "CLose Fire State");
			Intake.spin(RobotMap.FIRE_INTAKE_SPEED);
			if(optic){
				RobotMap.currentState = RobotMap.BALL_CLEAR_STATE;
			}  
			break;

		case RobotMap.CLOSE_ANGLE_STATE:
			SmartDashboard.putString("State: ", "Close Angle State");
			isFar = false;
			isClose = true;
			boolean reached = Shooter.moveHoodBang(RobotMap.CLOSE_ANGLE_VALUE);
			if(reached){
				Shooter.stopAngle();
				RobotMap.currentState = RobotMap.WAIT_FOR_BUTTON_STATE;
			}
			break;

		case RobotMap.FAR_ANGLE_STATE:
			SmartDashboard.putString("State: ", "Far Angle State");
			isFar = true;
			isClose = false;
			boolean finished = Shooter.moveHoodBang(RobotMap.FAR_ANGLE_VALUE);
			if(finished){
				Shooter.stopAngle();
				RobotMap.currentState = RobotMap.WAIT_FOR_BUTTON_STATE;
			}
			break;

		case RobotMap.COLLAPSE_STATE:
			SmartDashboard.putString("State: ", "Collapse State");
			isFar = false;
			isClose = false;
			Robot.intakePiston.set(DoubleSolenoid.Value.kForward);
			boolean done = Shooter.moveHoodBang(RobotMap.COLLAPSE_ANGLE_VALUE);
			if(done){
				Shooter.stopAngle();
				RobotMap.currentState = RobotMap.WAIT_FOR_BUTTON_STATE;
			}
			break;
			
		case RobotMap.MANUAL_OVERRIDE_STATE:
		
			
			if (isReturn){
				RobotMap.currentState = RobotMap.INIT_STATE;
			}
			
			//manual state machine
			switch(RobotMap.manualState){
			
			case RobotMap.MANUAL_FAR_ANGLE_STATE:
				SmartDashboard.putString("State: ", "Manual Far Angle State");
				isFar = true;
				isClose = false;
				boolean manualFar = Shooter.moveHoodBang(RobotMap.FAR_ANGLE_VALUE);
				if (manualFar){
					Shooter.stopAngle();
					RobotMap.manualState = RobotMap.MANUAL_WAIT_FOR_BUTTON_STATE;
				}
				break;
				
			
			case RobotMap.MANUAL_WAIT_FOR_BUTTON_STATE:
				SmartDashboard.putString("State: ", "Manual Wait For Button  State");
				if (isClose){
					RobotMap.manualState = RobotMap.MANUAL_CLOSE_ANGLE_STATE;
				}
				else if (isFar){
					RobotMap.manualState = RobotMap.MANUAL_FAR_ANGLE_STATE;
				}
				else if (isFire){
					RobotMap.manualState = RobotMap.MANUAL_FIRE_STATE;
				}
				
				break;
				
			case RobotMap.MANUAL_FIRE_STATE:
				SmartDashboard.putString("State: ", "Manual Fire State");
				if (isClose && !optic){
					Robot.canTalonFlyWheel.set(RobotMap.MANUAL_CLOSE_FIRE_SPEED);
					Shooter.fire(RobotMap.FIRE_INTAKE_SPEED);
				}
				else if (!optic){
					Robot.canTalonFlyWheel.set(RobotMap.MANUAL_FAR_FIRE_SPEED);
					Shooter.fire(RobotMap.FIRE_INTAKE_SPEED);
				}
				else{
					Shooter.stopFlyWheel();
					time = System.currentTimeMillis();
					RobotMap.manualState = RobotMap.MANUAL_BALL_CLEAR_STATE;
				}
				break;
				
			case RobotMap.MANUAL_BALL_CLEAR_STATE:
				SmartDashboard.putString("State: ", "Manual Ball Clear State");
				if (System.currentTimeMillis() >= time + RobotMap.BALL_WAIT_TIME){
					Intake.stop();
					RobotMap.manualState = RobotMap.MANUAL_WAIT_FOR_BUTTON_STATE;
				}
				
			case RobotMap.MANUAL_CLOSE_ANGLE_STATE:
				SmartDashboard.putString("State: ", "Manual Close Angle State");
				isClose = true;
				isFar = false;
				boolean manualClose = Shooter.moveHoodBang(RobotMap.CLOSE_ANGLE_VALUE);
				if (manualClose){
					Shooter.stopAngle();
					RobotMap.manualState = RobotMap.MANUAL_WAIT_FOR_BUTTON_STATE;
				}
				break;
			}
			
			break;
			
		default:
			//this should never ever happen
			RobotMap.currentState = RobotMap.MANUAL_OVERRIDE_STATE;
			break;
			
		}
	}
	
	

}
