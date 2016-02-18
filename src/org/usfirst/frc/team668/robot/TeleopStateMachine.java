package org.usfirst.frc.team668.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TeleopStateMachine {
		
	public static boolean canReverse = true;
	public static double reference;
	public static long time;
	public static long shootTime;
	
	public static void stateMachine(boolean optic, boolean isCloseFire, 
			boolean isFarFire, boolean isLower, boolean isCollapse, boolean isManual,
			boolean isReturn, boolean farAngle, boolean closeAngle, boolean isFire){
			
		boolean isClose = false;
		boolean isFar = false;
		boolean fromFar = false;
	
		if(isFar = true){
			SmartDashboard.putBoolean("Ready For Far Shot", Vision.isShotPossible());
		}
		else{
			SmartDashboard.putBoolean("Ready For Far Shot", false);
		}
		
		
		if(isManual && RobotMap.currentState != RobotMap.MANUAL_OVERRIDE_STATE){
			Intake.stop();
		//	Shooter.stopAngle();
			Shooter.stopFlyWheel();
			RobotMap.currentState = RobotMap.MANUAL_OVERRIDE_STATE;
		}
		System.out.print("pot: "+ Robot.pot.getValue());
		System.out.print(" POWER: " + Robot.canTalonFlyWheel.getOutputVoltage());
		System.out.println(" VAL: " + Robot.canTalonFlyWheel.get());
		System.out.println( " RPM: " + Robot.canTalonFlyWheel.getSpeed());
		//teleop state machine 
		switch (RobotMap.currentState){

		case RobotMap.INIT_STATE:
			
			//System.out.println("Here");
			SmartDashboard.putString("State: ", "Init State");
			System.out.println("Init State");
			canReverse = true;																																																																																																																																																																																																			
			Shooter.stopAngle();
			Shooter.stopFlyWheel();																																																																																																																																																																																																																																			
			Intake.stop();
//			isClose = false;
//			isFar = false;
			Robot.intakePiston.set(DoubleSolenoid.Value.kReverse);
//			boolean completed = Shooter.moveHoodBang(RobotMap.CLOSE_ANGLE_VALUE);
//			if(completed){
//				Shooter.stopAngle();
//				RobotMap.currentState = RobotMap.SET_CLOSE_PID_STATE;
//			}
			RobotMap.hoodState = RobotMap.HOOD_ZERO_STATE;
			RobotMap.currentState = RobotMap.SET_CLOSE_PID_STATE;
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
//			if(closeAngle){
//				RobotMap.currentState = RobotMap.CLOSE_ANGLE_STATE;
//			}
//
//			else if (farAngle){
//				RobotMap.currentState = RobotMap.FAR_ANGLE_STATE;
//			}

			 if (isFarFire){
				if (!optic){ //checks if the ball is in position
					isFar = true;
					isClose = false;
					RobotMap.currentState = RobotMap.INIT_FIRE_STATE;
				}
			}
			 
			 else if (isCloseFire){
				if (!optic){
					 isFar = false;
					isClose = true;
					RobotMap.currentState = RobotMap.INIT_FIRE_STATE;
				 }
			}

			else if (isLower){
				RobotMap.currentState = RobotMap.LOWER_INTAKE_STATE;
			}
			 
			else if (isCollapse){
				RobotMap.currentState = RobotMap.COLLAPSE_STATE;
			}

			break;

		case RobotMap.LOWER_INTAKE_STATE:
			SmartDashboard.putString("State: ", "Lower Intake State");
			
			SmartDashboard.putBoolean("Intake Position ", true);
			Robot.intakePiston.set(DoubleSolenoid.Value.kReverse);
			
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
				RobotMap.currentState = RobotMap.SET_FAR_PID_STATE; // begins the far firing sequence
			}

			else {
				RobotMap.currentState = RobotMap.CLOSE_ANGLE_STATE; // begins the close firing sequence
			}

//			else {
//				RobotMap.currentState = RobotMap.WAIT_FOR_BUTTON_STATE;
//			}

			break;
			
		case RobotMap.SET_FAR_PID_STATE:
			SmartDashboard.putString("State: ", "Set Far PID State");
			reference = RobotMap.FAR_FIRE_SPEED; //gets speed from the array of speeds dependent on the distance to the target
			Shooter.setPID(reference); //sets the speed of the motor according to the camera
			RobotMap.currentState = RobotMap.FAR_ANGLE_STATE;
			break;
			
		case RobotMap.SET_CLOSE_PID_STATE:	
			SmartDashboard.putString("State: ", "Set Close PID State");		
			Shooter.setPID(RobotMap.CONSTANT_SPEED); //sets the speed of the flywheel to the "constant speed" (the speed for close shot)
			RobotMap.currentState = RobotMap.WAIT_FOR_BUTTON_STATE;
			break;
			
		case RobotMap.FAR_FIRE_STATE:
			SmartDashboard.putString("State: ", "Far Fire State");
			
				Intake.spin(RobotMap.FIRE_INTAKE_SPEED); //fires the ball by spinning the intake
				if(optic){ //optic sensors no longer see the ball
					time = System.currentTimeMillis(); //sets the time for the timer in BALL_CLEAR_STATE
					RobotMap.currentState = RobotMap.BALL_CLEAR_STATE;
				}
			
			else if (isReturn){
				Intake.stop();
				RobotMap.hoodState = RobotMap.HOOD_ZERO_STATE;
				RobotMap.currentState = RobotMap.SET_CLOSE_PID_STATE;
			}
			break;
			
		case RobotMap.BALL_CLEAR_STATE: //we have both fire states come here for continuity even though close fire state doesn't need to
			
			SmartDashboard.putString("State: ", "Ball Clear State");			
			if (System.currentTimeMillis() - time  >= RobotMap.BALL_WAIT_TIME){//waits for the ball to be out of the firing area before slowing the motor down
				Intake.stop();
				//Robot.canTalonFlyWheel.disable(); //TODO:Check if this line will hurt the code (i think so)
				RobotMap.hoodState = RobotMap.HOOD_ZERO_STATE;
				RobotMap.currentState = RobotMap.SET_CLOSE_PID_STATE;
			}
			break;

		case RobotMap.CLOSE_FIRE_INIT_STATE:
			SmartDashboard.putString("State: ", "CLose Fire State");
			if (Math.abs(RobotMap.CLOSE_ANGLE_VALUE - Robot.pot.getValue()) <= RobotMap.ACCEPTABLE_HOOD_RANGE){
				shootTime = System.currentTimeMillis();
				RobotMap.currentState = RobotMap.SHOOT_TIMER_STATE;
				
			}
			break;	
			
		case RobotMap.FAR_FIRE_INIT_STATE:
			if ((Math.abs(reference - Robot.canTalonFlyWheel.getSpeed()) <= RobotMap.FAR_FIRE_SPEED_RANGE) 
					&& ( Math.abs(Shooter.angle - Robot.pot.getValue()) <= RobotMap.ACCEPTABLE_HOOD_RANGE)){ 
				RobotMap.currentState = RobotMap.SHOOT_TIMER_STATE;
			}
				
		case RobotMap.SHOOT_TIMER_STATE:
			if (System.currentTimeMillis() - shootTime  >= 1000){//waits for the ball to be out of the firing area before slowing the motor down
				if (fromFar){
					fromFar = false;
					RobotMap.currentState = RobotMap.FAR_FIRE_STATE;
				}
				else{
					RobotMap.currentState = RobotMap.CLOSE_FIRE_STATE;
				}
			}
			
		case RobotMap.CLOSE_FIRE_STATE:
			Intake.spin(RobotMap.FIRE_INTAKE_SPEED); //spins the ball into the flywheel
				if(optic){ //checks if the ball is gone (inversed logic)
					RobotMap.currentState = RobotMap.BALL_CLEAR_STATE;
				}
		 if (isReturn){
				Intake.stop();
				RobotMap.hoodState = RobotMap.HOOD_ZERO_STATE;
				RobotMap.currentState = RobotMap.WAIT_FOR_BUTTON_STATE;
			}
			break;

		case RobotMap.CLOSE_ANGLE_STATE:
			SmartDashboard.putString("State: ", "Close Angle State");
			isFar = false;
			isClose = true;
//			boolean reached = Shooter.moveHoodBang(RobotMap.CLOSE_ANGLE_VALUE); //starts moving the hood
//			if(reached){ //checks if the hood has made it to the target
//				Shooter.stopAngle(); //stops the hood
//				RobotMap.currentState = RobotMap.WAIT_FOR_BUTTON_STATE;
//			}
			RobotMap.hoodState = RobotMap.HOOD_CLOSE_SHOT_STATE;
			RobotMap.currentState = RobotMap.CLOSE_FIRE_INIT_STATE;
			break;

		case RobotMap.FAR_ANGLE_STATE:
			SmartDashboard.putString("State: ",  "Far Angle State");
			isFar = true;
			isClose = false;
			fromFar = true;
//			boolean finished = Shooter.moveHoodBang(RobotMap.FAR_ANGLE_VALUE); //starts moving the hood
//			if(finished){ //checks if the hood has made it to the target
//				Shooter.stopAngle(); // stops the hood
//				RobotMap.currentState = RobotMap.WAIT_FOR_BUTTON_STATE;
//			}
			RobotMap.hoodState = RobotMap.HOOD_GET_STATE;
			RobotMap.currentState = RobotMap.FAR_FIRE_INIT_STATE;
			break;

		case RobotMap.COLLAPSE_STATE:
			SmartDashboard.putString("State: ", "Collapse State");
			isFar = false;
			isClose = false;
			Robot.intakePiston.set(DoubleSolenoid.Value.kForward); //closes the intake
//			boolean done = Shooter.moveHoodBang(RobotMap.COLLAPSE_ANGLE_VALUE); //moves the hood 
//			if(done){ //checks if the hood has made it to the target
//				Shooter.stopAngle(); //stops the hood
//				RobotMap.currentState = RobotMap.WAIT_FOR_BUTTON_STATE;
//			}
			RobotMap.hoodState = RobotMap.HOOD_ZERO_STATE;
			RobotMap.currentState = RobotMap.WAIT_FOR_BUTTON_STATE;
			break;
			
		case RobotMap.MANUAL_OVERRIDE_STATE:
		
			//MANUAL OVERRRRRRIIIIIDDDDEEEEEE
			
			if (isReturn){ //"returns" to the state machine
				RobotMap.currentState = RobotMap.INIT_STATE;
			}
			
			
			//manual state machine
			switch(RobotMap.manualState){ //manual state machine 
			
			case RobotMap.MANUAL_FAR_ANGLE_STATE:
				SmartDashboard.putString("State: ", "Manual Far Angle State");
//				isFar = true;
//				isClose = false;
//				boolean manualFar = Shooter.moveHoodBang(RobotMap.FAR_ANGLE_VALUE); //moves the hood towards the far angle position
//				if (manualFar){//checks if the hood has made it to the target
//					Shooter.stopAngle(); //stops the angle
//					RobotMap.manualState = RobotMap.MANUAL_WAIT_FOR_BUTTON_STATE;
//				}
				RobotMap.hoodState = RobotMap.HOOD_MANUAL_FAR_STATE;
				break;
				
			
			case RobotMap.MANUAL_WAIT_FOR_BUTTON_STATE:
				canReverse = true;
				Shooter.stopFlyWheel();
				isClose = false;
				SmartDashboard.putString("State: ", "Manual Wait For Button State");
				if (closeAngle){
					RobotMap.manualState = RobotMap.MANUAL_CLOSE_ANGLE_STATE;
				}
				else if (farAngle){
					RobotMap.manualState = RobotMap.MANUAL_FAR_ANGLE_STATE;
				}
				else if (isCloseFire){
					RobotMap.manualState = RobotMap.MANUAL_FIRE_STATE;
				}
				else if (isCollapse){
					RobotMap.manualState = RobotMap.MANUAL_COLLAPSE_ANGLE_STATE;
				}
				else if (isFarFire){
					isClose = false;
					RobotMap.manualState = RobotMap.MANUAL_FIRE_STATE;
				}
				else if (isCloseFire){
					isClose = true;
					RobotMap.manualState = RobotMap.MANUAL_FIRE_STATE;
				}
				
				break;
				
			case RobotMap.MANUAL_FIRE_STATE:
				canReverse = false;
				SmartDashboard.putString("State: ", "Manual Fire State");
				if (isClose && !optic){ //checks if the angle is set to "close" and that there is a ball in the firing position
					Robot.canTalonFlyWheel.set(RobotMap.CLOSE_FIRE_SPEED);// sets the flywheel to a speed for close fire (does not use a PID)
					Shooter.fire(RobotMap.FIRE_INTAKE_SPEED); //spins the intake which puts the ball into the flywheel
				}
				else if (!optic){ //we did not have to check if it was in far angle because of the else-if but we still check for a ball being in position
					Robot.canTalonFlyWheel.set(RobotMap.FAR_FIRE_SPEED); //sets the speed of the ball to the far fire speed
					Shooter.fire(RobotMap.FIRE_INTAKE_SPEED); //spins the ball into the flywheel using the intake
				}
				else{
					Shooter.stopFlyWheel(); //stops the flywheel either because it is collapsed or because the ball has been fired
					time = System.currentTimeMillis(); //starts the timer for the ball clear state
					RobotMap.manualState = RobotMap.MANUAL_BALL_CLEAR_STATE;
				}
				break;
				
			case RobotMap.MANUAL_BALL_CLEAR_STATE:
				
				SmartDashboard.putString("State: ", "Manual Ball Clear State");
				if (System.currentTimeMillis() >= time + RobotMap.BALL_WAIT_TIME){
					Intake.stop();
					RobotMap.manualState = RobotMap.MANUAL_WAIT_FOR_BUTTON_STATE;
				}
				break;
				
			case RobotMap.MANUAL_CLOSE_ANGLE_STATE:
				SmartDashboard.putString("State: ", "Manual Close Angle State");
//				isClose = true;
//				isFar = false;
//				boolean manualClose = Shooter.moveHoodBang(RobotMap.CLOSE_ANGLE_VALUE);
//				if (manualClose){
//					Shooter.stopAngle();
//					RobotMap.manualState = RobotMap.MANUAL_WAIT_FOR_BUTTON_STATE;
//				}
				RobotMap.hoodState = RobotMap.HOOD_CLOSE_SHOT_STATE;
				break;
			
			case RobotMap.MANUAL_COLLAPSE_ANGLE_STATE:
				SmartDashboard.putString("State: ", "Manual Collapse State");
//				isClose = false;
//				isFar = true;
//				boolean collapsed = Shooter.moveHoodBang(RobotMap.COLLAPSE_ANGLE_VALUE);
//				if(collapsed){
//					Shooter.stopAngle();
//					RobotMap.manualState = RobotMap.MANUAL_WAIT_FOR_BUTTON_STATE;
//				}
				RobotMap.hoodState = RobotMap.HOOD_ZERO_STATE;
				break;
				
			} //end of manual switch
			
		default:
			//this should never ever happen
			RobotMap.currentState = RobotMap.MANUAL_OVERRIDE_STATE;
			break;
			
		}
	}
	
	

}
