import java.util.ArrayList;


public class DataCacheEntry {
	String tag;
	ArrayList<String> data;
	int wordsperblock;
	int age;
	boolean dirty;
	int startaddress;
	public DataCacheEntry(int wordsperblock , String tag , int address) {
		this.wordsperblock = wordsperblock;
		this.tag = tag;
		age = 0;
		data = new ArrayList<String>();
		startaddress = address;
		dirty = false;
		
	}
	public boolean addWord(String word){
		if(this.data.size() == wordsperblock){
			return false;
		}
		else{
			this.data.add(word);
			return true;
		}
	}

}
