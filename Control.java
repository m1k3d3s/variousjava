package generics;

public class Control {
	// control statements and blocks
	public static void main(String args[]) {
    		//forStatement();
    		//ifStatement();
            //GenericFrame ga = new GenericFrame();
    }
    
	public static void ifStatement() {
           
            int n1, n2, n3;
   
            
            n1 = 10;
            n2 = 20;
           
            if(n1 > n2) {
                    System.out.println(n1 + " is greater than " + n2);
            } else {
                    System.out.println(n1 + " is less than " + n2);
            }
           
            n3 = n1 - n2;
           
            if( n3 < 0 )  {
                    System.out.println("n3 is negative");
            }
    }
   
    public static void forStatement() {
            // for loop
           
            int counter;
            int[] anArray;

            // allocates memory for 10 integers
            anArray = new int[10];
               
            // initialize first element
            anArray[0] = 100;
            // initialize second element
            anArray[1] = 200;
            // etc.
            anArray[2] = 300;
            anArray[3] = 400;
            anArray[4] = 500;
            anArray[5] = 600;
            anArray[6] = 700;
            anArray[7] = 800;
            anArray[8] = 900;
            anArray[9] = 1000;
            
            for(counter = 0; counter < 10; counter = counter + 1 ) {
                    System.out.println("i can count to " + counter);
                    System.out.println("contents of array at position: " + counter + "=" + anArray[counter]);
            }      
           
            System.out.println("I reached " + counter);
    }
}
