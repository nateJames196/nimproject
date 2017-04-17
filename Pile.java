//package nimproject;

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
	public int lowerlimit = 5;
	public int upperlimit = 21;
	public char name;
	
	public Pile(String inputname) {
		this.name = inputname.charAt(0);
	}
	
	public void setVal() {
		Random rando = new Random();
		this.value = rando.nextInt(this.upperlimit) + this.lowerlimit;
		if (this.value > this.upperlimit) {
			this.value = this.upperlimit;
		}
		if (this.value == 0) {
			this.value = this.lowerlimit;
		}
		return;
	}
	
	/**
	 * jackpot is special, so we follow different rules.
	 * The difference between 50 and -50 is 100, so we can write rando.nextInt(100) and be fine.
	 * However, if we want to change it we can use the code I've written below,
	 * Math.abs() receives a number and outputs its "absolute value", which is always positive.
	 * We than add the absolute value together to get the distance between our numbers.
	 * This will not work correctly if lowerlimit is changed to a positive number.
	 * Otherwise it functions more or less the same as setVal
	 */
	
	public void setJack() {
		Random rando = new Random();
		this.value = rando.nextInt(Math.abs(this.upperlimit) + Math.abs(this.lowerlimit));
		this.value -= this.upperlimit;
		if (this.value > this.upperlimit) {
			this.value = this.upperlimit;
		}
		if (this.value == 0) {
			this.value = this.lowerlimit;
		}
		return;
	}
	
	public int getMax() {
		if (this.value > 3) {
			return 4;
		} else {
			return this.value;
		}
	}
	
	public boolean isValid() {
		//'J' is the name of jackpot, which the player should never be able to select.
		if (this.name == 'J') {
			return false;
		}
		//the player should not be able to select an empty pile
		if (this.value != 0) {
			return true;
		}
		
		return false;
	}
}
