import java.util.ArrayList;

public class DataCache {
	int associativity;
	int offsetLength;
	int tagLength;
	int indexLength;
	int cacheSize;
	int cacheLineSize;
	int wordsperBlock;
	ArrayList<ArrayList<DataCacheEntry>> dataCache ;
    String type;
	public DataCache(int l, int m ,int s) {
		associativity = m;
		cacheLineSize = l;
		cacheSize = s;
		wordsperBlock = l/2;
		offsetLength = (int) (Math.log(cacheLineSize)/Math.log(2));
		int numblocks = cacheSize/cacheLineSize;
		if(m == 1){
			type = "direct mapped";
		}
		else if(m == numblocks){
			type = "fully associative";
		}
		else{
			type = associativity + " way associative";
		}
		indexLength = type.equals("fully associative") ? 0 :(int) (Math.log(numblocks/associativity)/Math.log(2));
		tagLength = 16 - indexLength - offsetLength ;
		dataCache = new ArrayList<ArrayList<DataCacheEntry>>();
		
	}
	public void addData(){
		
	}

}
