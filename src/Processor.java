public class Processor {

	String [] insTable;
	RegTable rt;
	Memory m;
	public Processor(RegTable rt, Memory m) {
		this.rt = rt;
		this.m = m;
		process();
	}
	public void process(){
		while(true){
			int pc = Integer.parseInt(rt.getReg(7));
			String instruction = m.dm[pc] == null ? null: m.dm[pc].data;
			if(instruction == null || instruction.split(" ").length < 2){
				break;
				
			}
			else{
				instruct(instruction);
			}
			
		}
			
	}
	
	public void instruct(String ins){
		int pc = Integer.parseInt(rt.getReg(7));
		rt.setReg(7, pc+1+"");
		insTable = ins.replaceAll(",", "").split(" ");
		//System.out.println(ins.replaceAll(",",""));
		switch (insTable[0]){
		case "LW":
		case "SW":
			loadStore();
			break;
		case "JMP":
			uncondBranch();
			break;
		case "BEQ":
			condBranch();
			break;
		case "JALR":
		case "RET":
			callReturn();
			break;
		default:
			arthmetic();
			break;
		}
	}
	
	public void loadStore(){
		int regb = Integer.parseInt(rt.getReg(Integer.parseInt(insTable[2].substring(1))));
		int imm = Integer.parseInt(insTable[3]);
		int address = regb + imm;
		if(insTable[0].equals("LW")){
			int regaIndex = Integer.parseInt(insTable[1].substring(1));
			rt.setReg(regaIndex, m.getDataMemory(address));
			
		} else {
			int rega = Integer.parseInt(rt.getReg(Integer.parseInt(insTable[1].substring(1))));
			m.store(address, rega + "");
			
		}
	}
	
	public void uncondBranch(){
		int imm = Integer.parseInt(insTable[2]);
		int rega = Integer.parseInt(rt.getReg(Integer.parseInt(insTable[1].substring(1))));
		int pc =Integer.parseInt(rt.getReg(7));
		rt.setReg(7, pc + rega + imm + "");
	
	}

	public void condBranch(){
		int pc = Integer.parseInt(rt.getReg(7));
		int rega = Integer.parseInt(rt.getReg(Integer.parseInt(insTable[1].substring(1))));
		int regb = Integer.parseInt(rt.getReg(Integer.parseInt(insTable[2].substring(1))));
		int imm = Integer.parseInt(insTable[3]);
		if(rega == regb){
			rt.setReg(7,pc + imm + "" );
		}
	
	}

	public void callReturn(){
		int regaIndex = Integer.parseInt(insTable[1].substring(1));
		if(insTable[0].equals("RET")){
			rt.setReg(7, rt.getReg(regaIndex));
		}
		else{
			int pc = Integer.parseInt(rt.getReg(7));
			int regb = Integer.parseInt(rt.getReg(Integer.parseInt(insTable[2].substring(1))));
			rt.setReg(regaIndex, pc + "");
			rt.setReg(7, regb+"");
			
		}
	
	}

	public void arthmetic(){
		int regb = Integer.parseInt(rt.getReg(Integer.parseInt(insTable[2].substring(1))));
		int regc = insTable[0].equals("ADDI") ?Integer.parseInt(insTable[3])
				:Integer.parseInt(rt.getReg(Integer.parseInt(insTable[3].substring(1))));
		int regaIndex=Integer.parseInt(insTable[1].substring(1));
		switch(insTable[0]){
		case "ADD":
			rt.setReg(regaIndex, regb+regc+"");
			break;
		case "SUB" :
			rt.setReg(regaIndex, regb-regc+"");
			break;
		case "MUL" :
			rt.setReg(regaIndex, regb*regc+"");
		break;
		case "NAND" : 
			rt.setReg(regaIndex, (regb & ~regc)+"" );
			break;
		case "ADDI" :
			rt.setReg(regaIndex, regb+regc+"");
			break;
		
		}
	
	}

}
