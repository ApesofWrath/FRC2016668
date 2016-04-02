package org.usfirst.frc.team668.robot;

public class Arm {
	
	public static void armStateMachine(boolean groundButton, boolean sallyButton){
		
		
		switch(RobotMap.armState){
		case RobotMap.ARM_WAIT_FOR_BUTTON_STATE:
			//armBang(RobotMap.ARM_ZERO_HEIGHT);
			armBang(RobotMap.ARM_ZERO_HEIGHT);
			if (groundButton){
				RobotMap.armState = RobotMap.ARM_GROUND_STATE;
			}
			else if (sallyButton){
				RobotMap.armState = RobotMap.ARM_SALLY_STATE;
			}
			break;
			
		case RobotMap.ARM_SALLY_STATE:
			if (sallyButton){
				armBang(RobotMap.SALLY_HEIGHT);
			}
			else{
				RobotMap.armState = RobotMap.ARM_WAIT_FOR_BUTTON_STATE;
			}
			break;
			
		case RobotMap.ARM_GROUND_STATE:
			if (groundButton){
				armBang(RobotMap.GROUND_HEIGHT);
			}
			else{
				RobotMap.armState = RobotMap.ARM_WAIT_FOR_BUTTON_STATE;
			}
			break;
		
			
		}
			
		
		
	}
	public static void armBang(int ref){
		if( Math.abs(ref - Robot.armPot.getValue()) < RobotMap.ACCEPTABLE_ARM_RANGE){
			Robot.canTalonArm.set(0);
		}
		else if( ref >= Robot.armPot.getValue()){
			Robot.canTalonArm.set(-.35);
		}
		else if( ref < Robot.armPot.getValue()){
			Robot.canTalonArm.set(.35);
		}
			
	}
	
	public static void armMove(){
		Robot.canTalonArm.set(Robot.joyOp.getY()*.25);
	}
	
	public static void armStop(){
		Robot.canTalonArm.set(0.0);
	}

}
