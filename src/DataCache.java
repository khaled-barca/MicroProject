import java.util.ArrayList;

public class DataCache {
	int associativity;
	int offsetLength;
	int tagLength;
	int indexLength;
	int cacheSize;
	int cacheLineSize;
	int wordsperBlock;
	int blocknum;
	ArrayList<ArrayList<DataCacheEntry>> dataCache ;
    String type;
	public DataCache(int l, int m ,int s) {
		blocknum = s/l;
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
	public String findWord(int address){
		String Address16bit = generate16bitAddress(address);
		String offset = Address16bit.substring(16 - offsetLength);
		String tag = Address16bit.substring(0,tagLength);
		String index = type.equals("fully associative") ? "":Address16bit.substring(tagLength ,indexLength + tagLength );
		System.out.println("tits");
		if(index.equals("")){
			// fully asscoiative
			for(DataCacheEntry dce:dataCache.get(0)){
				if(dce != null && dce.tag.equals(tag)){
					dce.age = 0;
					return dce.data.get(Integer.parseInt(offset, 2)/2);
				}
			}
			return null;
		}
		else{
			for(DataCacheEntry dce:dataCache.get(Integer.parseInt(index,2))){
				if(dce != null && dce.tag.equals(tag)){
					dce.age = 0;
					return dce.data.get(Integer.parseInt(offset, 2)/2);
				}
			}
			return null;
		}
		
	}
	public DataCacheEntry addblock(int startaddress , ArrayList<String> data){
		String Address16bit = generate16bitAddress(startaddress);
		String tag = Address16bit.substring(0,tagLength);
		DataCacheEntry dce = new DataCacheEntry(wordsperBlock, tag , startaddress);
		dce.data = data;
		if(type.equals("fully associative")){
			if(dataCache.get(0) == null){
				// empty cache
				ArrayList<DataCacheEntry> datac = new ArrayList<DataCacheEntry>();
				datac.add(dce);
				dataCache.add(0, datac);
				return null;
				
			}
			else if(dataCache.get(0).size() == blocknum){
				// replacement
				int maximum_age = dataCache.get(0).get(0).age;
				DataCacheEntry toBeReplaced = dataCache.get(0).get(0);
				int i = 0 ,toBeReplacedIndex = 0;
				for(DataCacheEntry block : dataCache.get(0)){
					if(block !=null && block.age > maximum_age){
						maximum_age = block.age;
						toBeReplaced = block;
						toBeReplacedIndex = i;
					}
					i++;	
				}
				dataCache.get(0).set(toBeReplacedIndex, dce);
				return toBeReplaced;
				
				
			}
			else{
				// can add block without replacement
				dataCache.get(0).add(dce);
				return null;
			}
		}
		else{
		int index = Integer.parseInt(Address16bit.substring(tagLength ,indexLength + tagLength ),2);
		if(type.equals("direct mapped")){
			ArrayList<DataCacheEntry> datac = new ArrayList<DataCacheEntry>();
			datac.add(dce);
			if(dataCache.get(index) == null){
				// empty cache entry
				// can add block without replacement
				dataCache.add(index, datac);
				return null;
			}
			else{
				DataCacheEntry toBeReplaced = dataCache.get(index).get(0);
				dataCache.set(index, datac);
				return toBeReplaced;
				
				// replacement
			}
		}
		else{
			// n way associative
			if(dataCache.get(index) == null){
				// empty cache
				ArrayList<DataCacheEntry> set = new ArrayList<DataCacheEntry>();
				set.add(dce);
				dataCache.add(index, set);
				return null;
			}
			else if(dataCache.get(index).size() < associativity){
				dataCache.get(index).add(dce);
				return null;
			}
			else{
				// replacement
				int maximum_age = dataCache.get(index).get(0).age;
				DataCacheEntry toBeReplaced = dataCache.get(index).get(0);
				int i = 0,toBeReplacedIndex = 0;
				for(DataCacheEntry block : dataCache.get(index)){
					if(block!=null && block.age > maximum_age){
						maximum_age = block.age;
						toBeReplaced = block;
						toBeReplacedIndex = i;
					}
					i++;	
				}
				dataCache.get(index).set(toBeReplacedIndex, dce);
				return toBeReplaced;
				
			}
			
		}
		}
		
	}
	public boolean modify_block(int address , String word){
		String address16bit = generate16bitAddress(address);
		int offset = getOffset(address16bit);
		String tag = getTag(address16bit);
		if(type.equals("fully associative")){
			for(DataCacheEntry block :dataCache.get(0)){
				if(block !=null && block.tag.equals(tag)){
					block.data.set(offset/2, word);
					block.dirty=true;
					return true;
				}
			}
			return false;
			
		}
		else 
		{
			int index = getIndex(address16bit);
			if(type.equals("direct mapped")){
				ArrayList<DataCacheEntry> block = dataCache.get(index);
				if(block != null){
					if(block.get(0) !=null){
						block.get(0).data.set(offset/2, word);
						block.get(0).dirty =true;
						return true;
					}
				}
				return false;
				
			}
			else{
				// n way associative
				ArrayList<DataCacheEntry> set = dataCache.get(index);
				for(DataCacheEntry block:set){
					if(block !=null && block.tag.equals(tag)){
						block.data.set(offset/2, word);
						block.dirty = true;
						return true;
					}
				}
				return false;
			}
			
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
	public int getIndex(String address16bit){
		return Integer.parseInt(address16bit.substring(tagLength ,indexLength + tagLength ),2);
	}
    public int getOffset(String address16bit){
    	return Integer.parseInt(address16bit.substring(16 - offsetLength));
	}
    public String getTag(String address16bit){
    	return address16bit.substring(0,tagLength);
    	
    }

}
