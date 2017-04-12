
import java.util.Random;
/**
 *
 * The Pile class has four properties:
 * Value, which we refer to ingame and manipulate with Pile.value;
 * Name, which must be defined when we create an instance of the class.
 * Upper limit, which is the maximum value we supply to the random number generator.
 * Lower limit, which is the minimum value we apply to the random number generator.
 * Value is undefined when we create an instance of the class, so we use setVal(), 
 * upperlimit and lowerlimit in conjunction to create a random number to fill it.
 *
 */
public class Pile {
	public int value;
	public int lowerlimit = 4;
	public int upperlimit = 100;
	public char name;
	
	public Pile(String inputname) {
		this.name = inputname.charAt(0);
	}
	
	public void setVal() {
		Random rando = new Random();
		this.value = rando.nextInt(this.upperlimit) + this.lowerlimit;
		if (this.value == 0) {
			this.value++;
		}
		return;
	}
	
	public boolean isValid() {
		if (this.value != 0) {
			return true;
		}
		return false;
	}
}
