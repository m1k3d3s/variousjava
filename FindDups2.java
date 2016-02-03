import java.util.*;
public class FindDups2 implements ModInterface {
	public static void main(String[] args){
		Set<String> uniques = new HashSet<String>();
		Set<String> dups	= new HashSet<String>();
		
		for (String a : args)
			if (!uniques.add(a))
				dups.add(a);
		
		
		//Destructive set-difference
		uniques.removeAll(dups);
		int dupssize = dups.size();
		
		System.out.println(dupssize);
		System.out.println("Unique words:    "+uniques);
		System.out.println("Duplicate words: "+dups);

		dups.removeAll(dups);
		if(dups.isEmpty()){
			System.out.println("Dups is empty");
		}
		
		
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean contains(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Iterator iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object[] toArray() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object[] toArray(Object[] a) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean add(Object e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean remove(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean containsAll(Collection c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addAll(Collection c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeAll(Collection c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean retainAll(Collection c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void countdupe(Object element, Object element1) {
		// TODO Auto-generated method stub
		System.out.println(element + " " + element1);
		
	}
}
