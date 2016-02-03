package javaseven;

import java.util.Collections;
import java.util.Set;
import java.util.HashSet;;

public class ClassicalElementsBetter {
	public static final Set<String> ELEMENTS;
	
	static {
		Set<String> temp = new HashSet<String>();
		temp.add("Earth");
		temp.add("Water");
		temp.add("Air");
		temp.add("Fire");
		ELEMENTS = (Set<String>) Collections.unmodifiableCollection(temp);
	}
/*	public final static void breakMethod(){
		Set<String> elements = ClassicalElementsBetter.ELEMENTS;
		elements.add("Metal");
		elements.add("Platinum");
	}
*/
	public int getSize(){
		return ELEMENTS.size();
	}
}
