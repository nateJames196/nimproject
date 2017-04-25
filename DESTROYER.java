import java.util.Random;

public class DESTROYER {

	//The Destroyer's name is randomly generated using the DESTROYER.getTitle() function
	public String getName() {
		DESTROYER D = new DESTROYER();
		return D.getTitle();
	}

	//This represents the Destroyer's AI
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
	 * This is the Pile.isValid() function from our original source, but in this case, its a method of Destroyer
	 * and recieves the Pile's value as an argument.
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
	 * This is a port of the countValids() function from our original source. Again, we pass a Destroyer instance as an 
	 * argument. This returns the number of piles with a value of 1 or more.
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
	 * This is more or less a 1 to 1 port of our enemyChoice() function. choice.value becmoes choice, potval becomes pot,
	 * v_count is the same, and we have to feed a destroyer to the function as an argument so we can use its methods.
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
	 * Determines the max amount we can take. Ported from Pile.getMax(). Recieves the value of a pile as an integer rather
	 * than using this.value. Like Math.max, Math.min will return the smaller of the two integers you feed to it.
	 */
	public int getMax(int A) {
		return Math.min(A, 5);
	}
	
	/**
	 * Generates a random number from 0 to 4, and then uses that number to run a switch statement to choose the output of
	 * getTitle(). Note, this could also be done with an array of strings, in which case the code would look more like this:
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
}
