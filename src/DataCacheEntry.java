import java.util.ArrayList;


public class DataCacheEntry {
	String tag;
	ArrayList<String> data;
	int wordsperblock;
	int age;
	public DataCacheEntry(int wordsperblock , String tag) {
		this.wordsperblock = wordsperblock;
		this.tag = tag;
		age = 0;
		data = new ArrayList<String>();
		
	}
	public boolean addWord(String word){
		if(data!=null && this.data.size() == wordsperblock){
			return false;
		}
		else{
			this.data.add(word);
			return true;
		}
	}

}
