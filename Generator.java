package generator;

import java.util.Arrays;
import java.util.Random;
public class Generator {
	public static void main(String[] args) {
		int MAX_NUM_LENGTH = 4;
		Random rangen = new Random();
		int[] numArray = new int[5];
		for(int i=0;i<=MAX_NUM_LENGTH;i++){
			
			if((numArray[i] !=0) && !(Arrays.asList(numArray).contains(numArray[i]))) {
				//System.out.println(randint);
				numArray[i] = rangen.nextInt(75);
				//Arrays.sort(numArray);
			} else {
				numArray[i] = rangen.nextInt(75);
			}
		}
		Arrays.sort(numArray);
		System.out.println(Arrays.toString(numArray));
		Random powball = new Random();
		int pb = powball.nextInt(15);
		if (pb != 0) {
			System.out.println("Powerball: " + pb);
		} else {
			pb = powball.nextInt(15);
			System.out.println("Powerball: " + pb);
		}
	}
}


