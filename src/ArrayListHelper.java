

import java.util.ArrayList;
import java.util.Collection;

public class ArrayListHelper<E> extends ArrayList<E> {
	ArrayList<Integer> temp = new ArrayList<Integer>();
	int flag = 0;

	public ArrayListHelper() {
		// TODO Auto-generated constructor stub
	}

	public ArrayListHelper(int initialCapacity) {
		super(initialCapacity);
		// TODO Auto-generated constructor stub
	}

	public ArrayListHelper(Collection<? extends E> c) {
		super(c);
		// TODO Auto-generated constructor stub
	}
	
	public void addElement(int index, E s){
		temp.add(index);
		for(int i = 0; i < index; i++){
			for(int j = 0; j<temp.size(); j++){
				if(i!=temp.get(j)){
					flag++;
				}
			}
			if(flag >= temp.size()){
				this.add(i, null);
			}
		}
		this.add(index, s);
	}

}
