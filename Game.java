//package nimproject;

import java.util.Random;
import java.util.Scanner;

/**
 * Game is the name of the file that holds our main() method. It utilizes instances of a class called Pile, and handles player
 * input using a combination of a Scanner Object and nested while... and do...while loops.
 */
public class Game {
	
	public static void main(String[] args) {
		//Setting up instances of our pile class
		Pile pileA = new Pile("A");
		Pile pileB = new Pile("B");
		Pile pileC = new Pile("C");
		//The jackpot is special. We have to redefine its lower and upper limits
		Pile jackpot = new Pile("Jackpot");
		jackpot.lowerlimit = -50;
		jackpot.upperlimit = 50;
		
		/*The value below will be used to hold the pile the player is currently 
		 * interacting with. Note: we are not creating an instance of the pile class here,
		 * we are creating a variable of the TYPE pile, similar to the way we would create
		 * a string.
		*/
		Pile active_pile;
		
		//Values below represent the amount of money owned by each player.
		int player1 = 0;
		int player2 = 0;

		//Value denotes whether or not the game is actively accepting input.
		boolean gameactive;
		
		//Instance of a scanner object, which we can use to take player input.
		Scanner keyboard = new Scanner(System.in);
		
		//Variables below are used to control the while loop.
		String input_choice, quit;
		quit = "";
		//quit is declared before use so we can refer to length in a while loop
		//we would not need to declare it if we used a do...while() loop instead
		System.out.print("Welcome to NIM!\n");
		while (quit.length() < 1) {
			System.out.println("Setting up new game...\n");
			//This code block sets up an instance of the game.
			pileA.setVal();
			pileB.setVal();
			pileC.setVal();
			jackpot.setJack();
			active_pile = pileA;
			gameactive = true;

			//While loop that runs until the jackpot has been taken
			while (gameactive == true) {
				//game "rendering"; now that values are set up, time to display them.
				showPiles(pileA.value, pileB.value, pileC.value);
				showJackpot(jackpot.value);
				/* The following block of code is a do...while statement that receives
				 * player input and allows him or her to choose which pile to take from.
				 */
				do {
					System.out.print("Please enter the pile you would like to take from. ");
					input_choice = keyboard.nextLine();

					if (validInput(input_choice) != 49) {
					//Switch to decide which pile to use. if not 'B' or 'C', will be 'A'
						switch (validInput(input_choice)) {
							case 'B':
								active_pile = pileB;
								break;
							case 'C':
								active_pile = pileC;
								break;
							default:
								active_pile = pileA;
								break;
						}
					}
					else {
						//generic error text
						invalidError();
					}
					if (active_pile.isValid() == false) {
						System.out.println();
						System.out.println("That pile is unavailable.");
						System.out.println("Please try again.");
					}
				} while (validInput(input_choice) == 49 || active_pile.isValid() == false);
				
				/* The following block of code is a do...while statement that receives
				 * player input and allows him or her to choose how much to take from
				 * the pile they have chosen.
				 */
				
				int temp_value;
				do {
					temp_value = active_pile.value;
					System.out.printf("Please enter the amount to take from pile %c. ", active_pile.name);
					input_choice = keyboard.nextLine();

					if (validChoice(input_choice, temp_value) == true) {
						/*remember, an integer is different from a character
						 * char '1' = int 1 + 48.
						 */
						active_pile.value -= input_choice.charAt(0) - 48;
						player1 += input_choice.charAt(0) - 48;
					} else {
						//generic error text
						invalidError();
					}
				} while (validChoice(input_choice, temp_value) == false);
				System.out.println("");
				System.out.printf("Player 1 took $%c from pile %c", input_choice.charAt(0), active_pile.name);
				System.out.println("");
				/*
				 * Did the player empty all piles? Yes? Give the jackpot to player 2
				 * No? Player 2 takes a turn. See "AI FUNCTIONS" section
				 */
				boolean jpot_available;
				jpot_available = jackpotCheck(pileA.value, pileB.value, pileC.value);
				if (jpot_available == true) {
					player2 += jackpot.value;
					gameactive = false;
				} else {
					active_pile = enemyChoice(pileA, pileB, pileC);
					int validcount = countValids(pileA, pileB, pileC);
					int e_pick = enemyPick(active_pile, jackpot.value, validcount);
					active_pile.value -= e_pick;
					player2 += e_pick;
					System.out.printf("Player 2 took $%d from pile %c", e_pick, active_pile.name);
					System.out.println("");
				}
				/*
				 * Did the opponent take a turn and empty all piles? Yes? 
				 * Give the jackpot to player 1.
				 * No? Show the standings and return to the beginning of the loop
				 * Allow the player can take another turn.
				 */
				jpot_available = jackpotCheck(pileA.value, pileB.value, pileC.value);
				if (jpot_available == true && gameactive == true) {
					player1 += jackpot.value;
					gameactive = false;
				} else {
					showTotals(player1, player2);
				}
			}
			showEnding(player1, player2);
			System.out.println("Press enter to play another round. Enter anything else to quit.");
			quit = keyboard.nextLine();
		}
	}
	
	//--------------------------------AI FUNCTIONS----------------------------------------------
	
	/**
	 * enemyTurn() receives all three piles from main() as arguments, and then choses between them.
	 * @param A pile
	 * @param B pile
	 * @param C pile
	 * @return pile. If the number of valid piles is greater than 1, chooses randomly. Else, chooses
	 * the only pile.
	 */
	public static Pile enemyChoice(Pile A, Pile B, Pile C) {
		if (countValids(A, B, C) > 1) {
			return randomSelect(A, B, C);
		}
		/* randomSelect() uses a do..while, so we use the block of code below to save time
		 * when we know there's only one valid option.
		 */
		if (A.isValid() == true) {
			return A;
		} else if (B.isValid() == true) {
			return B;
		} else {
			return C;
		}
	}
	
	/**
	 * randomSelect() receives all three piles as arguments, and selects randomly among
	 * the three. If it selects a pile that is not valid, will continue.
	 * @param A Pile
	 * @param B Pile
	 * @param C Pile
	 * @return Pile. random choice based on a switch statement.
	 */
	public static Pile randomSelect(Pile A, Pile B, Pile C) {
		Pile p_choice;
		int n_choice;
		Random rando = new Random();
		/*
		 * This do while loop will run until a valid pile has been selected.
		 * rando.nextInt(2) returns 0, 1 or 2. we increase that outcome by 1 and get 1, 2 or 3
		 * we set p_choice to A by default so the compiler won't complain, but we don't need to.
		 */
		p_choice = A;
		do {
			n_choice = rando.nextInt(2) + 1;
			switch (n_choice) {
			    case 1:
			    	p_choice = A;
			    	break;
			    case 2:
			    	p_choice = B;
			    	break;
			    case 3:
			    	p_choice = C;
			    	break;
			}
		} while (p_choice.isValid() != true);
		return p_choice;
	}
	
	/**
	 * enemyPick() represents the bulk of the computer player's AI. Note that it does not take
	 * information about two of the three piles, so its decisions will lack the nuance of a
	 * human or an ECPLAYER
	 * @param choice The pile the AI has selected
	 * @param potval The current value of the jackpot.
	 * @param v_count The number of currently active piles
	 * @return returns an integer between 1 and 5, preferring 5 and preferring other numbers based on
	 * the number of active piles and the value of the jackpot.
	 */
	public static int enemyPick(Pile choice, int potval, int v_count) {
		//Unless this is the only available pile, take the largest amount possible.
		if (v_count > 1) {
			return choice.getMax();
		}
		if (choice.value == 1) {
			return 1;
		}
		/* We now know we have only one pile left, every line after this uses that assumption.
		 * We also know the selection has a value of 2 or more, so we don't need to check
		 */
		
		/* If the jackpot is above zero, so the AI tries to leave the player with 6
		 * No matter what the player takes they will leave a value between 5 and 2.
		 * The AI takes enough to leave choice.value at 1 so the opponent must empty the pile.
		 */
		if (potval > 0) {
			//value is under 6, and we already know the value is 2+, so we take enough leave 1
			if (choice.value <= 6) {
				return choice.getMax() - 1;
			}
			//The AI can leave a multiple of six, so it does!
			if (choice.value % 7 != 0 && choice.value % 7 < 6) {
				return choice.value % 7;
			}
			//the AI cannot leave a multiple of six so it takes as much as it can.
			return choice.getMax();
		}
		/* If the jackpot is below zero, the AI won't want it, so it tries to take the last 
		 * piece. It tries to leave the player with a value of 6.
		 * No matter what the player takes they will leave a value between 5 and 1.
		 * The AI will take the rest of the pile, forcing the player to take the jackpot.
		 */
		if (potval < 0) {
			//value is 4 or less so the AI takes the rest of the pile.
			if (choice.value <= 5) {
				return choice.value;
			}
			//value is not a multiple of 6 so the AI makes it a multiple of six.
			if (choice.value % 6 != 0) {
				return choice.value % 6;
			}
			//value is a multiple of 5 and it must take something, so it takes the most possible
			return choice.getMax();
		}
		//Shouldn't happen, but just in case
		return 1;
	}
	
	//------------------------------------FORMATTING FUNCTIONS----------------------------------

	/**
	 * showPiles() has no return value. Its main purpose is to display information about the piles
	 * in a way that it compact. One line "showPiles();" is better than six lines.
	 * @param A int, represents the value property of a Pile.
	 * @param B int, represents the value property of a Pile.
	 * @param C int, represents the value property of a Pile.
	 */
	public static void showPiles(int A, int B, int C) {
		System.out.printf("Pile A: $%d", A);
		System.out.println("");
		System.out.printf("Pile B: $%d", B);
		System.out.println("");
		System.out.printf("Pile C: $%d", C);
		System.out.println("");
		return;
	}

	/**
	 * showJackpot() has no return value. Like showPiles(), its function is to reduce visual clutter
	 * @param jackpot int, represents the value property of the Pile named jackpot
	 */
	public static void showJackpot(int jackpot) {
		//Preformatting to prevent ugly stuff like "$-38 dollars"
		String format_pot = ("$" + Math.abs(jackpot));
		if (jackpot < 0) {
			format_pot = ("-" + format_pot);
		}
		System.out.println("");
		System.out.printf("The jackpot is %s dollars.", format_pot);
		System.out.println("");
		return;
	}
	
	/**
	 * showTotal() is another formatting function. We take the total of each player and display
	 * either player 1 winning, player 2 winning, or both players tied.
	 * @param p1 int, represents the total of a player
	 * @param p2 int, represents the total of a player
	 */
	public static void showTotals(int p1, int p2) {
		//First off, we set up values to store information about who is in the lead
		int winner, loser, wname, lname;
		//Now, we determine who is in the lead, if anyone.
		if (p1 > p2) {
			winner = p1;wname = 1;loser = p2;lname = 2;
		} else {
			winner = p2;wname = 2;loser = p1;lname = 1;
		}
		//Next, we use a printf statement to output the information we just stored.
		if (p1 != p2) {
			System.out.printf("Player %d is leading, with a total of %d!", wname, winner);
			System.out.println();
			System.out.printf("Player %d is trailing with a total of %d.", lname, loser);
		} else {
			System.out.printf("Player %d and Player %d are tied, with a total of %d!", wname, lname, winner);
		}
		
		System.out.println();
		System.out.println();
		//Note the use of println() statements here, these will make the output look better.
		return;
	}
	
	/**
	 * showEnding() is very similar to showTotal(). In fact, ideally these functions would be
	 * combined and take a boolean argument to determine how to modify the output. See the
	 * documentation for showTotal()
	 * @param p1 int, represents the total associated with player 1
	 * @param p2 int, represents the total associated with player 2
	 */
	public static void showEnding(int p1, int p2) {
		//First off, we set up values to store information about who is in the lead
		int winner, loser;
		int wname, lname;
		//Now, we determine who is in the lead, if anyone.
		if (p1 > p2) {
			winner = p1;
			wname = 1;
			loser = p2;
			lname = 2;
		} else {
			winner = p2;
			wname = 2;
			loser = p1;
			lname = 1;
		}
		//Next, we use a printf statement to output the information we just stored.
		if (p1 != p2) {
			System.out.printf("Player %d is the winner, with a total of %d!", wname, winner);
			System.out.println();
			System.out.printf("Player %d is the loser, with a total of %d.", lname, loser);
		} else {
			System.out.printf("Its a tie! Both players have a total of %d!", winner);
		}
		
		System.out.println();
		System.out.println();
		//Note the use of println() statements here, these will make the output look better.
		return;
	}
	
	/**
	 * invalidError() displays a generic error message. Its main purpose is to reduce visual clutter.
	 */
	public static void invalidError() {
		System.out.print("\nInvalid Input. ");
		System.out.println("Please try again.");
		return;
	}
	
	//-------------------------------INPUT VALIDATION FUNCTIONS---------------------------------
	
	/**
	 * validInput() checks for three different formats of input, in order.
	 * Single character: "A"
	 * Pile with space: "Pile B"
	 * Pile without space: "PileC"
	 * If it finds a valid name, it will return it as a character.
	 * If it cannot find a valid pile name, it will return 1 (failure.)
	 * @param input String
	 * @return returns a character, A, B, C or 1. 1 indicates that input was not a valid name.
	 */
	public static char validInput(String input) {
		if (input.length() == 0) {
			return 1;
		}
		else if (input.toUpperCase().charAt(0) <= 'C' && input.toUpperCase().charAt(0) >= 'A') {
			return input.toUpperCase().charAt(0);
		}
		else if (input.length() > 5 && input.toUpperCase().charAt(5) <= 'C' && input.toUpperCase().charAt(5) >= 'A') {
			return input.toUpperCase().charAt(5);
		}
		else if (input.length() > 4 && input.toUpperCase().charAt(4) <= 'C' && input.toUpperCase().charAt(4) >= 'A') {
			return input.toUpperCase().charAt(4);
		}
		else {
			return 1;
		}
		//A return value of 1 indicates failure.
	}
	
	/**
	 * validChoice() validates the user input based on length, value and the total of a pile
	 * First, we check to see if the string we recieved from the player contains any characters.
	 * No? The string is invalid, return false.
	 * Second, we check the first character of the string, and see if its matches an integer
	 * if the first character is not between character values 48 - 58 (0-9), return false
	 * Third, we create a temporary variable, choice, of the type int based on the first character of
	 * input. This means that inputs like 12 will be accepted, but 12 will never actually be used.
	 * Fourth, we check if choice is greater than 5. Yes? return false.
	 * Fifth, we check if choice is below 0. Yes? return false.
	 * Sixth, we check if choice is greater than total. Yes? return false
	 * Our number passed all of our checks, so we can return true.
	 * @param input String, represents an input received from the player
	 * @param total int, represents the value of a pile
	 * @return boolean, true or false based on total and input's length and first character.
	 */
	public static boolean validChoice(String input, int total) {
		//String was too short. Invalid.
		if (input.length() == 0) {
			return false;
		}
		//String did not properly supply a number. Invalid.
		//Remember: char '58' = 9, char '49' = 1
		if (input.charAt(0) > 58 && input.charAt(0) < 48) {
			return false;
		}
		/*
		 * We know by now that the string:
		 * 1. Has a length of 1 or more, so it has indexes we can check.
		 * 2. Has a number at index 0, so we know we can use it in an int.
		 */
		int choice = input.charAt(0) - 48;
		//6 or more. Number is too big.
		if (choice > 5) {
			return false;
		}
		//0 or less. Number is too small.
		if (choice <= 0) {
			return false;
		}
		//between 1 and 5, but higher than total. Number is too big.
		if (choice > total) {
			return false;
		}
		//The number input passed all these checks, so we can use it.
		return true;
	}
	
	//---------------------------------MISCELLANEOUS FUNCTIONS----------------------------------
	
	/**
	 * jackpotCheck() determines whether or not the jackpot is available to take. Note that it 
	 * does not directly check whether A, B, or C is equal to zero. Rarely, the jackpot can be
	 * taken a little early.
	 * @param A int, represents the value of a Pile.
	 * @param B int, represents the value of a Pile.
	 * @param C int, represents the value of a Pile.
	 * @return boolean, returns true if A, B, and C are equal. otherwise returns false
	 */
	public static boolean jackpotCheck(int A, int B, int C) {
		if (A == 0 && A == B && A == C) {
			return true;
		}
		return false;
	}
	
	/**
	 * countValids() takes all active piles and outputs the total number of Piles that are currently
	 * acceptable for the player to take money from. Jackpot is special, so we do not include it.
	 * @param A Pile, represents a Pile.
	 * @param B Pile, represents a Pile.
	 * @param C Pile, represents a Pile.
	 * @return int, returns the number of times that Pile.isValid() evaluated to true. ranges 
	 * from 0 to 3, athough in practice it will return 1 to 3.
	 */
	public static int countValids(Pile A, Pile B, Pile C) {
		int count = 0;
		if (A.isValid() == true) {
			count++;
		}
		if (B.isValid() == true) {
			count++;
		}
		if (C.isValid() == true){
			count++;
		}
		return count;
	}
}
