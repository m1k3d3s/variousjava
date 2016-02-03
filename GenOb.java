package generics;

public class GenOb<T> {
	
	private T n;

	public GenOb(T n) {
		this.n = n;
		System.out.println("Integer is: " + n);
	}
	
	public static void main(String args[]) {
		GenOb ngo = new GenOb("7");
		System.out.println(ngo.n);
		
	}
}
