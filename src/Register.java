
public class Register {
	String value ;
	String name;
	public Register (String name){
		value = "0";
		this.name = name;
	}
	public String getValue(){
		return Integer.parseInt(value,2)+ "" ;
	}
	public void setValue(String v){
		
		value = add_bits(Integer.toBinaryString(Integer.parseInt(v)));
	}
	private String add_bits(String s){
		for(int i = s.length(); i< 16; i++){
			s = "0"+s;
		}
		return s;
	}
			
}
	
	
	
	
	


