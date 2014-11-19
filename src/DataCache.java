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
		offsetLength = log2(cacheLineSize);
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
		indexLength = type.equals("fully associative") ? 0 :log2(numblocks/associativity);
		tagLength = 16 - indexLength - offsetLength ;
		dataCache = new ArrayList<ArrayList<DataCacheEntry>>();
		
	}
	public void addblock(int startaddress , ArrayList<String> data){
		
	}
	public String findWord(int address){
		String Address16bit = generate16bitAddress(address);
		String offset = Address16bit.substring(16 - offsetLength);
		String tag = Address16bit.substring(0,tagLength);
		String index = type.equals("fully associative") ? "":Address16bit.substring(tagLength ,indexLength + tagLength );
		if(index.equals("")){
			for(DataCacheEntry dce:dataCache.get(0)){
				if(dce != null && dce.tag.equals(tag)){
					return dce.data.get(Integer.parseInt(offset, 2)/2);
				}
			}
			return null;
		}
		else{
			for(DataCacheEntry dce:dataCache.get(Integer.parseInt(index))){
				if(dce != null && dce.tag.equals(tag)){
					return dce.data.get(Integer.parseInt(offset, 2)/2);
				}
			}
			return null;
		}
		
	}
	
	public String generate16bitAddress(int address){
		String newAddress = Integer.toBinaryString(address);
		for(int i = log2(wordsperBlock) ; i < offsetLength ; i++){
			newAddress += "0" ;
		}
		for(int i = newAddress.length(); i< 16;i++){
			newAddress = "0" + newAddress;
		}
		return newAddress;
	}
	public static int log2(int n){
		return (int) Math.ceil(Math.log(n)/Math.log(2));
	}

}
