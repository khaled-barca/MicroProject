import java.util.ArrayList;


public class Processor {

	String [] insTable;
	RegTable rt;
	Memory m;
	DataCache datacache;
	int hits;
	int misses;
	Write_Policy write_policy;
	public Processor(RegTable rt, Memory m , DataCache datacache , Write_Policy wp) {
		this.rt = rt;
		this.m = m;
		this.datacache = datacache;
		write_policy = wp;
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
			// load
			int regaIndex = Integer.parseInt(insTable[1].substring(1));
			String word = datacache.findWord(address);
			if(word == null){
				misses++;
				word = m.getDataMemory(address);
				// caching entire block from memory
				int startAddress = getStartAddress(address);
				address = startAddress;
				ArrayList<String> data = new ArrayList<String>();
				for(int i = 0; i < datacache.wordsperBlock; i++ ){
					data.add(m.getDataMemory(address++));
				}
				DataCacheEntry replaced_block = datacache.addblock(startAddress, data);
				if(replaced_block != null){
					// replacement takes place
					if(write_policy == Write_Policy.WRITE_BACK && replaced_block.dirty){
						// write replaced block to memory
						for(String w : replaced_block.data ){
							m.store(replaced_block.startaddress++, w);
						}
						
					}
				}
				
			}
			else{
				hits++;
			}
			
			rt.setReg(regaIndex, word);
		} else {
			// store
			int rega = Integer.parseInt(rt.getReg(Integer.parseInt(insTable[1].substring(1))));
			if(write_policy == Write_Policy.WRITE_BACK){
				if(!datacache.modify_block(address, rega+"")){
					ArrayList<String> data = new ArrayList<String>();
					String address16bit = datacache.generate16bitAddress(address);
					int startaddress = getStartAddress(address);
					int offset = datacache.getOffset(address16bit);
					data.add(offset/2, rega+"");
					DataCacheEntry replaced_block = datacache.addblock(startaddress,data);
					if(replaced_block != null && replaced_block.dirty){
						for(String w : replaced_block.data ){
							m.store(replaced_block.startaddress++, w);
						}
					}
				}
				
			}
			else{
				m.store(address, rega + "");
				int startaddress = getStartAddress(address);
				ArrayList<String> data = new ArrayList<String>();
				for(int i = 0; i < datacache.wordsperBlock; i++ ){
					data.add(m.getDataMemory(startaddress++));
				}
				datacache.addblock(startaddress, data);
				
			}
			
			
		}
	}
	
	public int getStartAddress(int address){
		for(int i = address; i > 0 ; i--){
			if(i%datacache.wordsperBlock == 0){
				return i;
			}
		}
		return 0;
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
	public static int log2(int n){
		return (int) Math.ceil(Math.log(n)/Math.log(2));
	}
	
	public void increment_age(){
		for(ArrayList<DataCacheEntry> array: datacache.dataCache){
			for(DataCacheEntry dce : array){
				if(dce !=null){
					dce.age++;
				}
			}
		}
	}

}
