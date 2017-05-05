import java.util.Random;

public class DESTROYER {

	/**
	 * getName() shares a name with RandomPlayer.getName() and GreedyPlayer.getName(), and returns
	 * the same type of value, a String. However, it uses a function to output a randomly changing
	 * name.
	 * @return String, getTitle() outputs a randomly selected string representing the name of the
	 * DESTROYER. We define a destroyer instance in order to get at its methods, although
	 * this.getTitle() would have been faster and more compact.
	 */
	public String getName() {
		DESTROYER D = new DESTROYER();
		return D.getTitle();
	}

	/**
	 * move() is a representation of the DESTROYER's main AI. It uses a heap and a small improvement
	 * over the GreedyPlayer's move selection function in order to choose which pile to take from.
	 * The amount it actually takes from the pile in question is controlled by a port of 
	 * game.enemyChoice() from the original source code.
	 * @param G GameBoard, represents the active instance of the gameboard object
	 * @return move array(an array with the type move). Returns an integer representing the INDEX of
	 * an active pile, and a number to take from it.
	 */
	public move move(GameBoard G) {
		//D will represent an instance of our destroyer class. We must do this in order to use its methods.
		DESTROYER D = new DESTROYER();
		//int[] heap declares an array of the type int. new int[3] defines an integer array with three indexes.
		int[] heap = new int[3];
		heap[0] = G.getA();//index 0 is the first index. We initialize that with the value of our first pile, A
		heap[1] = G.getB();//We continue in the same way for each pile and index.
		heap[2] = G.getC();
		
		//The destroyer will choose the pile with most money and take from it. GreedyPlayer but BETTER
		int maxTake = Math.max(Math.max(heap[0], heap[1]), heap[2]); // Which pot has the most money?
		int n = 0;
		while (heap[n] < maxTake) {
			n = (n + 1) % 3; 
		}
		
		//Now that we have chosen a pile, we have our destroyer choose how much to take from it
		int validcount = D.countValids(heap[0], heap[1], heap[2], D);
		int take = D.chooseTake(heap[n], G.getPot(), D, validcount);//feed chooseTake our chosen pile and the jackpot
	
		return new move(n, take);
	}
	
	/**
	 * checkValid() is a port of Pile.isValid(), but in this case its a method of our destroyer.
	 * @param Pileval int, determines if the DESTROYER can take from a pile that has this value.
	 * @return boolean, returns true if Pileval is 1 or more. otherwise returns false.
	 */
	public boolean checkValid(int Pileval) {
		//is the value of our integer above zero? Yes? Return true.
		if (Pileval > 0) {
			return true;
		}
		//We know our integer is at or below zero, so we return false.
		return false;
	}
	
	/**
	 * countValids() is a port of game.countValids() from the orginal source. We pass an instance of
	 * DESTROYER as an argument so we can use its methods. D.checkValid is only possible for this
	 * reason.
	 * @param A int, represents the value of a pile
	 * @param B int, represents the value of a pile
	 * @param C int, represents the value of a pile
	 * @param D DESTROYER, represents an instance of the DESTROYER class
	 * @return int n, which is incremented whenever a pile is discovered to be valid. ranges from
	 * 0 to 3.
	 */
	public int countValids(int A, int B, int C, DESTROYER D) {
		int n = 0;
		//Increment n each time we find a valid pile.
		if (D.checkValid(A)) {
			n++;
		}
		if (D.checkValid(B)) {
			n++;
		}
		if (D.checkValid(C)) {
			n++;
		}
		//Now that we have checked all piles, return n.
		return n;
	}
	
	/**
	 * chooseTake() is almost identical to enemyChoice() from our original source. Choice.value 
	 * becomes simply "choice", although pot and v_count are almost the same. D is an instance of
	 * destroyer, which allows us to make statments like D.getMax() although this.getMax() or simply
	 * getmax() would probably be better.
	 * @param choice int, represents the value of a pile.
	 * @param pot int, represents the value of a jackpot.
	 * @param D DESTROYER, represents an arbitrary instance of a destroyer object.
	 * @param v_count int, represents the number of piles it is acceptable for the player to take
	 * from.
	 * @return int, represents the number the AI will try to take from the pile it has chosen.
	 */
	public int chooseTake(int choice, int pot, DESTROYER D, int v_count) {
		//Unless this is the only available pile, take the largest amount possible.
		if (v_count > 1) {
			return D.getMax(choice);
		}
		if (choice == 1) {
			return choice;
		}
		/* We now know we have only one pile left, every line after this uses that assumption.
		 * We also know the selection has a value of 2 or more, so we don't need to check
		 */
		
		/* If the jackpot is above zero, so the AI tries to leave the player with 6
		 * No matter what the player takes they will leave a value between 5 and 2.
		 * The AI takes enough to leave choice.value at 1 so the opponent must empty the pile.
		 */
		if (pot > 0) {
			//value is under 6, and we already know the value is 2+, so we take enough leave 1
			if (choice <= 6) {
				return D.getMax(choice) - 1;
			}
			//The AI can leave a multiple of seven, so it does!
			if (choice % 7 != 0 && choice % 7 < 6) {
				return choice % 7;
			}
			//the AI cannot leave a multiple of six so it takes as much as it can.
			return D.getMax(choice);
		}
		/* If the jackpot is below zero, the AI won't want it, so it tries to take the last 
		 * piece. It tries to leave the player with a value of 5.
		 * No matter what the player takes they will leave a value between 4 and 1.
		 * The AI will take the rest of the pile, forcing the player to take the jackpot.
		 */
		if (pot < 0) {
			//value is 5 or less so the AI takes the rest of the pile.
			if (choice <= 5) {
				return choice;
			}
			//value is not a multiple of 6 so the AI makes it a multiple of six.
			if (choice % 6 != 0) {
				return choice % 6;
			}
			//value is a multiple of 6 and it must take something, so it takes the most possible
			return D.getMax(choice);
		}
		//Shouldn't happen, but just in case
		return 1;
	}
	
	/**
	 * getMax() returns the highest possible amount the DESTROYER can take from a pile.
	 * Ported version of Pile.getMax().
	 * @param A int, represents the value of a pile.
	 * @return integer, returns either 5 or the value of the pile, whichever is smaller.
	 */
	public int getMax(int A) {
		return Math.min(A, 5);
	}
	
	/**
	 * getTitle() Generates a random number from 0 to 4 and uses that number to run a switch 
	 * statement that supplies a value to a variable called output. Note, this could also be done 
	 * with an array of strings.
	 * @return String, output. Output is defined based on a switch statement that recieves its value
	 * from Random.nextint();  Note that in class, case 4 never occured. This might be wrong, but it 
	 * appears that rando.nextInt(x) may not actually be inclusive. 
	 */
	public String getTitle() {
		Random rando = new Random();
		//rando.nextInt(4) produces a value between 0 and 4
		int n = rando.nextInt(4);
		
		/* 
		 * we use a switch statement to set output based on the value of n.
		 */
		String output = "";
		switch (n) {
			case 1:
				output = "DOMINATOR OF THE WEAK";
				break;
			case 2:
				output = "ENDER OF WELLBEING";
				break;
			case 3:
				output = "MASTER OF CHAOS";
				break;
			case 4:
				output = "DESTROOOYEEEEERRR";
				break;
			default:
				output = "LORD OF CARNAGE";
				break;
		}
		return output;
	}
	/*
	 * Example array based code:
	 * String[] output = new String[5];
	 * output[0] = "DOMINATOR OF THE WEAK";
	 * output[1] = "ENDER OF WELLBEING";
	 * ...etc.
	 * Once our array is set up we would use write a return statement like so:
	 * return output[rando.nextInt(4)];
	 * We could go further and use a value of n to set up the size of our string array:
	 * int n = 5;
	 * String[] output = new String[n];
	 * and make a more modular return:
	 * return output[rando.nextint(n-1)];
	 */
}
