
public class Memory {
	DataMemory [] dm;
	int index;
	public Memory (){
		this.dm =  new DataMemory[65536];
		index = 0;
	}
	public String getDataMemory(int index) {
		return dm[index] == null ? null :dm[index].getData();
	}
	public void store(int index, String Data){
		dm[index]=new DataMemory(Data);
	}
	public void setInstruction(String instruction){
		dm[index++]=new DataMemory(instruction);
		
	}
	public void setDataMemory(String data){
		dm[index++]=new DataMemory(data);
	}
	
	
}
