import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class Main {

	public static void main(String[] args) {
		Memory m = new Memory();
		RegTable rt = new RegTable();
		DataCache cache = new DataCache(1, 1, 2);
		try {
			BufferedReader reader = new BufferedReader(new FileReader("instructions"));
			String line ="";
			try {
				while ((line = reader.readLine()) != null) {
					if(line.split(" ").length >1){
						m.setInstruction(line);
					}
					else{
						m.setDataMemory(line);
					}
				    
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Processor p = new Processor(rt,m,cache,Write_Policy.WRITETHROUGH);
		for(Register r:rt.reg){
			System.out.println(r.getValue()+"\n");
		}
		
		
		
	}

}
