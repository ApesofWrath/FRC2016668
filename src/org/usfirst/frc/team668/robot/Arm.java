package org.usfirst.frc.team668.robot;

public class Arm {
	
	public static void armStateMachine(boolean groundButton, boolean sallyButton){
		
		/*
		if (!Robot.isBrightEyes){
			if (Robot.pot.getValue() <= RobotMap.ARM_ZERO_HEIGHT){
				if ()
			}
		}
		else{
			if (Robot.pot.getValue() < RobotMap.BRIGHT_ARM_ZERO_HEIGHT){
				armBang(RobotMap.BRIGHT_ARM_ZERO_HEIGHT + 10);
			}
		}
		*/
		switch(RobotMap.armState){
		case RobotMap.ARM_WAIT_FOR_BUTTON_STATE:
			//armBang(RobotMap.ARM_ZERO_HEIGHT);
			if (!Robot.isBrightEyes){
				armBang(RobotMap.ARM_ZERO_HEIGHT);
			}
			else{
				armBang(RobotMap.BRIGHT_ARM_ZERO_HEIGHT);
			}
			
			if (groundButton){
				RobotMap.armState = RobotMap.ARM_GROUND_STATE;
			}
			else if (sallyButton){
				RobotMap.armState = RobotMap.ARM_SALLY_STATE;
			}
			break;
			
		case RobotMap.ARM_SALLY_STATE:
			if (sallyButton){
				if (!Robot.isBrightEyes){
					armBang(RobotMap.SALLY_HEIGHT);
				}
				else{
					armBang(RobotMap.BRIGHT_SALLY_HEIGHT);
				}
			}
			else{
				RobotMap.armState = RobotMap.ARM_WAIT_FOR_BUTTON_STATE;
			}
			break;
			
		case RobotMap.ARM_GROUND_STATE:
			if (groundButton){
				if (!Robot.isBrightEyes){
					armBang(RobotMap.GROUND_HEIGHT);
				}
				else{
					armBang(RobotMap.BRIGHT_GROUND_HEIGHT);
				}
			}
			else{
				RobotMap.armState = RobotMap.ARM_WAIT_FOR_BUTTON_STATE;
			}
			break;
		
			
		}
			
		
		
	}
	public static void armBang(int ref){
		System.out.println("MOVINGP");
		double speed = 0;
		int error =  Math.abs(ref - Robot.armPot.getValue());
		if( Math.abs(ref - Robot.armPot.getValue()) < RobotMap.ACCEPTABLE_ARM_RANGE){
			speed = 0;
		}
		else if( ref >= Robot.armPot.getValue()){
			if (error > 30){
				speed = -.25;
			}
			else if(error > 20){
				speed = -.15;
			}
			else{
				speed = -.1;
			}
		}
		else if( ref < Robot.armPot.getValue()){
			if (error > 30){
				speed = .2;
			}
			else if (error > 15){
				speed = .1;
			} 
			else{
				speed = .07;
			}
		}
		
		if (!Robot.isBrightEyes){
			if (Robot.armPot.getValue() <= RobotMap.ARM_ZERO_HEIGHT){
				if (speed  > 0){
					speed = 0;
				}
			}
			if (Robot.armPot.getValue() >= RobotMap.GROUND_HEIGHT){
				if ( speed < 0){
					speed = 0;
				}
			}
		}
		else{
			if (Robot.armPot.getValue() <= RobotMap.BRIGHT_ARM_ZERO_HEIGHT){
				if (speed  > 0){
					speed = 0;
				}
			}
			if (Robot.armPot.getValue() >= RobotMap.BRIGHT_GROUND_HEIGHT){
				if ( speed < 0){
					speed = 0;
				}
			}
		}
		
		Robot.canTalonArm.set(speed);
			
	}
	
	public static void armMove(){
		Robot.canTalonArm.set(Robot.joyOp.getY()*.25);
	}
	
	public static void armStop(){
		Robot.canTalonArm.set(0.0);
	}

}
