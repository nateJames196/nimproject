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
	
	/**
	 * Don't miss this tiny constructor! Pile() accepts a string argument and assigns the
	 * first character as the name of our Pile.
	 * @param inputname String, will be used mostly for its first character.
	 */
	public Pile(String inputname) {
		this.name = inputname.charAt(0);
	}
	
	/**
	 * setVal() uses a random number generator to create the value of the relevant Pile
	 * Note, rando.nextInt(21) + 5 as an expression will range from 26 to 5, so we need
	 * to cap the value with our first if-statement. This means that a Pile with the max
	 * value is slightly more likely than any other type of pile.
	 * We also want to avoid a value of zero, although it shouldn't be possible due to the way in
	 * which our random number generator is set up. if the value is 0, it gets set to the lowerlimit
	 */
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
	 * setJack is an alternative version of setVal, with special handling unique to the jackpot.
	 * If I coded this now, I would have created a subclass of Pile called Jackpot and put
	 * this method there. Anyway, the this function ensures that we get the difference between
	 * jackpot's lower limit and its upper limit, and then we add those together and feed it to
	 * Random.nextint(). This will work as long as jackpots upper and lower limits are positive
	 * and negative, respectively.
	 * To calculate distance:
	 * Math.abs() receives a number and outputs its "absolute value", which is always positive.
	 * We than add the absolute value together to get the distance between our numbers.
	 * This will not work correctly if lowerlimit is changed to a positive number.
	 * After calculating distance, function is the same as setVal();
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
	
	/**
	 * getMax() simplifies a lot of if-statements, even if we were cleaner and made
	 * better use of the ternary operator.
	 * @return int, will either return 5 or the value of the pile, whichever is smaller.
	 */
	public int getMax() {
		if (this.value > 4) {
			return 5;
		} else {
			return this.value;
		}
	}
	
	/**
	 * isValid() is a quick solution in if-statements where we want to know if the player is trying to take
	 * from a pile with a value less than or equal to zero.
	 * @return boolean, returns false if the value of the pile is 0, or if the name of the pile is jackpot.
	 * otherwise, returns true.
	 */
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
