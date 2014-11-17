
public class RegTable {
	Register [] reg;

	public RegTable() {
		this.reg = new Register[8];
		for(int i=0;i<8;i++){
			reg[i]=new Register("R"+i);
		}
	}
	public String getReg(int index){
		return reg[index].getValue();
	}
	public void setReg(int index, String Data){
		if(index != 0){
			reg[index].setValue(Data);
		}
	}

}
