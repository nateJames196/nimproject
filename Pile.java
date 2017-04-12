
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

	public void setName(String inputname) {
		this.name = inputname.charAt(0);
		return;
	}
	/**
	 *
	 * For our purposes, we will only ever use this for Pile J(jackpot) to decrease its lower bounds.
	 */
	public void setMin(int lowlimit) {
		this.lowerlimit = lowlimit;
		return;
	}
	/**
	 *
	 * For our purposes, we will only ever use this for Pile J(jackpot) to decrease its higher bounds.
	 */
	public void setMax(int uplimit) {
		this.upperlimit = uplimit;
		return;
	}
	public void setVal() {
		Random rando = new Random();
		this.value = rando.nextInt(this.upperlimit) + this.lowerlimit;
		return;
	}
}
