package org.usfirst.frc.team668.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TeleopStateMachine {
		
	public static boolean canReverse = true;
	public static double reference;
	public static long time;
	public static long shootTime;
	
	public static boolean isClose = false;
	public static boolean isFar = false;
	
	public static boolean fromFar = false;
	public static boolean isSameState = false;
	
	public static boolean ballCleared = true;
	
	public static void stateMachine(boolean optic, boolean isCloseFire, 
			boolean isFarFire, boolean isIntakeLower, boolean isCollapse, boolean isManual,
			boolean isReturn, boolean farAngle, boolean closeAngle, boolean isFire, 
			boolean isReverse, boolean manualHood, boolean lowGoal ){
		
		
		
			SmartDashboard.putBoolean("Ready For Far Shot", Vision.isShotPossible());
		
		 
		if (!optic && !ballCleared){
			Shooter.setPID(RobotMap.FAR_FIRE_SPEED_RANGE);
		
		}
		
		else{
			Shooter.setPID(0);
		}
		
		
		if(isManual && RobotMap.currentState != RobotMap.MANUAL_OVERRIDE_STATE){
			Intake.stop();
			RobotMap.hoodState = RobotMap.HOOD_ZERO_STATE;
		//	Shooter.stopAngle();
		//	Shooter.stopFlyWheel();
			RobotMap.manualState = RobotMap.MANUAL_WAIT_FOR_BUTTON_STATE;
			RobotMap.currentState = RobotMap.MANUAL_OVERRIDE_STATE;
		}
//		
//		if (Robot.pot.getValue() > 3225){
//			System.out.println("pot: "+ Robot.pot.getValue());
//		}
		
		//System.out.print(" TARGET: " + RobotMap.CLOSE_ANGLE_VALUE);
		//System.out.print(" POWER: " + Robot.canTalonFlyWheel.getOutputVoltage());
		//System.out.println(" VAL: " + Robot.canTalonFlyWheel.get());

		System.out.print( " RPM: " + Robot.canTalonFlyWheel.getSpeed());
		System.out.println(" POT: " + Robot.pot.getValue());
	
		
		//teleop state machine 
		switch (RobotMap.currentState){

		case RobotMap.INIT_STATE:
			
			//System.out.println("Here");
			SmartDashboard.putString("State: ", "Init State");
			//System.out.println("Init State");
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
			RobotMap.currentState = RobotMap.WAIT_FOR_BUTTON_STATE;
			break;

		case RobotMap.WAIT_FOR_BUTTON_STATE:
			SmartDashboard.putString("State: ", "Wait For Button State");
			canReverse = true;
			ballCleared = true;
			//Prints whether a ball is ready to shoot
			if (!optic){
				SmartDashboard.putBoolean("BALL IN PLACE:", true);
			}
			else{
				SmartDashboard.putBoolean("BALL IN PLACE:", false);
			}

			//buttons
			 if (isFarFire){
				if (!optic){ //checks if the ball is in position
					isFar = true;
					isClose = false;
					RobotMap.currentState = RobotMap.INIT_FIRE_STATE;
				}
			}
			 
			  if (isCloseFire){
				if (!optic){
					 isFar = false;
					isClose = true;
					RobotMap.currentState = RobotMap.INIT_FIRE_STATE;
				 }
			}

			else if (isIntakeLower){
				RobotMap.currentState = RobotMap.LOWER_INTAKE_STATE;
			}
			 
			else if (isCollapse){
				RobotMap.currentState = RobotMap.COLLAPSE_STATE;
			}

			break;
			
		case RobotMap.LOW_GOAL_SHOT_STATE:
			SmartDashboard.putString("State: ", "Low Goal Shot State");

			if(lowGoal){
				RobotMap.currentState = RobotMap.LOW_GOAL_SHOT_STATE;
				if(Math.abs((RobotMap.BRIGHT_LOW_GOAL_ANGLE)-(Robot.pot.getValue())) <= RobotMap.ACCEPTABLE_HOOD_RANGE ){
					Intake.spit(.8);
				}
				else {
					Intake.stop();
				}
			}
			else{
				Intake.stop();
				RobotMap.hoodState = RobotMap.HOOD_ZERO_STATE;
				RobotMap.currentState = RobotMap.WAIT_FOR_BUTTON_STATE;
			}
			break;

		case RobotMap.LOWER_INTAKE_STATE:
			SmartDashboard.putString("State: ", "Lower Intake State");
			
			SmartDashboard.putBoolean("Intake Position ", true);
	
			if (optic && !isReverse){ //runs until one of the sensors are false. Inverted for that reason
				Intake.spin(.8);
			}
			else if(!optic){
				Intake.stop();
				RobotMap.currentState = RobotMap.WAIT_FOR_BUTTON_STATE;
			}
			else{
				Intake.spit(.8);
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
			Robot.intakePiston.set(DoubleSolenoid.Value.kForward);
			if (isFar == true && Vision.isShotPossible()){
				RobotMap.currentState = RobotMap.FAR_ANGLE_STATE; // begins the far firing sequence
			}
			else if (isClose){
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
			//	Robot.intakePiston.set(DoubleSolenoid.Value.kForward);
				Intake.spin(RobotMap.FIRE_INTAKE_SPEED); //fires the ball by spinning the intake
				if(optic){ //optic sensors no longer see the ball
					time = System.currentTimeMillis(); //sets the time for the timer in BALL_CLEAR_STATE
					RobotMap.currentState = RobotMap.BALL_CLEAR_STATE;
				}
			
			else if (isReturn){
				Intake.stop();
				RobotMap.hoodState = RobotMap.HOOD_ZERO_STATE;
				RobotMap.currentState = RobotMap.WAIT_FOR_BUTTON_STATE;
			}
			break;
			
		case RobotMap.BALL_CLEAR_STATE: //we have both fire states come here for continuity even though close fire state doesn't need to
			
			SmartDashboard.putString("State: ", "Ball Clear State");			
			if (System.currentTimeMillis() - time  >= RobotMap.BALL_WAIT_TIME){//waits for the ball to be out of the firing area before slowing the motor down
				Intake.stop();
				//Robot.canTalonFlyWheel.disable(); //TODO:Check if this line will hurt the code (i think so)
				Robot.canTalonFlyWheel.clearIAccum();
				RobotMap.hoodState = RobotMap.HOOD_ZERO_STATE;
				RobotMap.currentState = RobotMap.WAIT_FOR_BUTTON_STATE;
				ballCleared = true;
			}
			break;

		case RobotMap.CLOSE_FIRE_INIT_STATE:
			SmartDashboard.putString("State: ", "CLose Fire State");
			if (Math.abs(Shooter.angle - Robot.pot.getValue()) <= RobotMap.ACCEPTABLE_HOOD_RANGE){
				shootTime = System.currentTimeMillis();
				RobotMap.currentState = RobotMap.SHOOT_TIMER_STATE;
				
			}
			else if (isReturn){
				Intake.stop();
				RobotMap.hoodState = RobotMap.HOOD_ZERO_STATE;
				RobotMap.currentState = RobotMap.WAIT_FOR_BUTTON_STATE;
			}
			break;	
			
		case RobotMap.FAR_FIRE_INIT_STATE:
			SmartDashboard.putString("State: ", "Far Fire Init State");
			//Robot.intakePiston.set(DoubleSolenoid.Value.kForward);
			if ((Math.abs(reference - Robot.canTalonFlyWheel.getSpeed()) <= RobotMap.FAR_FIRE_SPEED_RANGE) 
					&& ( Math.abs(Shooter.angle - Robot.pot.getValue()) <= RobotMap.ACCEPTABLE_HOOD_RANGE)){ 
				shootTime = System.currentTimeMillis();
				RobotMap.currentState = RobotMap.SHOOT_TIMER_STATE;
			}
			else if (isReturn){
				Intake.stop();
				RobotMap.hoodState = RobotMap.HOOD_ZERO_STATE;
				RobotMap.currentState = RobotMap.WAIT_FOR_BUTTON_STATE;
			}
			break;
			
		case RobotMap.SHOOT_TIMER_STATE:
			SmartDashboard.putString("State: ", "Shoot Timer State");
			if (System.currentTimeMillis() - shootTime  >= 300){
				if (isFar){
					RobotMap.currentState = RobotMap.FAR_FIRE_STATE;
				}
				else{
					RobotMap.currentState = RobotMap.CLOSE_FIRE_STATE;
				}
			}
			break;
		case RobotMap.CLOSE_FIRE_STATE:
			SmartDashboard.putString("State: ", "Close Fire State");
		//	Robot.intakePiston.set(DoubleSolenoid.Value.kForward);
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
			//
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
		//	fromFar = true;
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
		
			SmartDashboard.putString("State: ", "Manual Override");
			//MANUAL OVERRRRRRIIIIIDDDDEEEEEE
			
			if (isReturn){ //"returns" to the state machine
				RobotMap.currentState = RobotMap.INIT_STATE;
			}
			
			
			//manual state machine
			if (RobotMap.currentState == RobotMap.MANUAL_OVERRIDE_STATE){
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
				//Robot.canTalonFlyWheel.disable();
				//Shooter.stopFlyWheel();
				ballCleared = false;
				isClose = false;
				SmartDashboard.putString("State: ", "Manual Wait For Button State");
				if (closeAngle){
					RobotMap.manualState = RobotMap.MANUAL_CLOSE_ANGLE_STATE;
				}
				else if (farAngle){
					RobotMap.manualState = RobotMap.MANUAL_FAR_ANGLE_STATE;
				}
//				else if (isCloseFire){
//					RobotMap.manualState = RobotMap.MANUAL_FIRE_STATE;
//				}
				else if (isCollapse){
					RobotMap.manualState = RobotMap.MANUAL_COLLAPSE_ANGLE_STATE;
				}
//				else if (isFarFire){
//					isClose = false;
//					RobotMap.manualState = RobotMap.MANUAL_FIRE_STATE;
//				}
//				else if (isCloseFire){
//					isClose = true;
//					RobotMap.manualState = RobotMap.MANUAL_FIRE_STATE;
//				}
				else if (manualHood){
					RobotMap.manualState = RobotMap.MANUAL_CONTROLL_HOOD_ANGLE_STATE;
				}
				else if (isFire){
					RobotMap.manualState = RobotMap.MANUAL_FIRE_STATE;
				}
				
				break;
				
			case RobotMap.MANUAL_FIRE_STATE:
				canReverse = false;
				SmartDashboard.putString("State: ", "Manual Fire State");
				if (!optic && (Math.abs(reference - Robot.canTalonFlyWheel.getSpeed()) <= RobotMap.FAR_FIRE_SPEED_RANGE)){
					Shooter.fire(RobotMap.FIRE_INTAKE_SPEED); //spins the intake which puts the ball into the flywheel
				}
				
				else{
					time = System.currentTimeMillis(); //starts the timer for the ball clear state
					RobotMap.manualState = RobotMap.MANUAL_BALL_CLEAR_STATE;
				}
				break;
				
			case RobotMap.MANUAL_BALL_CLEAR_STATE:
				
				SmartDashboard.putString("State: ", "Manual Ball Clear State");
				if (System.currentTimeMillis() >= time + RobotMap.BALL_WAIT_TIME){
					Intake.stop();
					RobotMap.manualState = RobotMap.MANUAL_WAIT_FOR_BUTTON_STATE;
					ballCleared = true;
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
				
			case RobotMap.MANUAL_CONTROLL_HOOD_ANGLE_STATE:
				if (manualHood){
					RobotMap.hoodState = RobotMap.HOOD_MANUAL_STATE;
				}
				else {
					RobotMap.hoodState = RobotMap.HOOD_ZERO_STATE;
					RobotMap.manualState = RobotMap.MANUAL_WAIT_FOR_BUTTON_STATE;
				}
				break;
			} //end of manual switch
			
			}
		default:
			//this should never ever happen
			RobotMap.currentState = RobotMap.MANUAL_OVERRIDE_STATE;
			break;
			
		}
	}
	
	

}
