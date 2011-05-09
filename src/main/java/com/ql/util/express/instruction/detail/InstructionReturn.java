package com.ql.util.express.instruction.detail;

import java.util.List;

import com.ql.util.express.RunEnvironment;

public class InstructionReturn extends Instruction{
	boolean haveReturnValue;
	public InstructionReturn(boolean aHaveReturnValue){
		this.haveReturnValue = aHaveReturnValue;
	}
	public void execute(RunEnvironment environment,List<String> errorList)throws Exception{
		//目前的模式，不需要执行任何操作
		if(environment.isTrace()){
			log.debug(this);
		}
		if(this.haveReturnValue == true){			
		   environment.quitExpress(environment.pop().getObject(environment.getContext()));
		}else{
		   environment.quitExpress();
		}
		environment.programPointAddOne();
	}
	public String toString(){
		if(this.haveReturnValue){
	         return "return [value]";	
		}else{
			return "return";
		}
	}	
}