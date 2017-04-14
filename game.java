//package nimproject;

import java.util.Scanner;

//class name can be anything, but it must be defined in its own file.
public class game {
	
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
		boolean gameactive = true;
		
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
			jackpot.setVal();
			active_pile = pileA;

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
				 * No? Player 2 takes a turn. 
				 */
				boolean jpot_available;
				jpot_available = jackpotCheck(pileA.value, pileB.value, pileC.value);
				if (jpot_available == true) {
					player2 += jackpot.value;
					gameactive = false;
				} else {
					enemyTurn();
				}
				/*
				 * Did the opponent empty all piles? Yes? Give the jackpot to player 1
				 * No? Show the standings and return to the beginning of the loop
				 * so the player can take another turn.
				 */
				jpot_available = jackpotCheck(pileA.value, pileB.value, pileC.value);
				if (jpot_available == true) {
					player1 += jackpot.value;
					gameactive = false;
				} else {
					showTotals(player1, player2);
				}
			}
			showEnding(player1, player2);
			System.out.println("Press enter to play another round. ");
			quit = keyboard.nextLine();
		}
	}
	
	public static void enemyTurn() {
		//TODO
		return;
	}

	/**
	 * showPiles(); is visually cleaner than the messy printf below
	 * we use this function to ensure that only logic appears in the main function
	 * showPiles() itself displays the value of each file.
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
	 * 
	 * showJackpot() is used for the same reason as showPiles()
	 * showJackpot formats the jackpot and displays it.
	 * if we can, we want the main function to only contain game logic
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
	 * 
	 * showTotal() is just like the above
	 * showTotal() formats the total of each player and displays it to the console
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
	 * 
	 * showEnding is showTotal() but with different output.
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
	 * invalidError() is a visually cleaner version of statement block below
	 * We use this function to make our main function easier to read
	 * invalidError() displays a generic error message
	 */
	public static void invalidError() {
		System.out.print("\nInvalid Input. ");
		System.out.println("Please try again.");
		return;
	}
	
	/**
	 * validInput() checks for three different formats of input, in order.
	 * Single character: "A"
	 * Pile with space: "Pile B"
	 * Pile without space: "PileC"
	 * If it finds a valid name, it will return it as a character.
	 * If it cannot find a valid pile name, it will return 1 (failure.)
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
		//4 or more. Number is too big.
		if (choice > 4) {
			return false;
		}
		//0 or less. Number is too small.
		if (choice <= 0) {
			return false;
		}
		//between 1 and 3, but higher than total. Number is too big.
		if (choice > total) {
			return false;
		}
		//The number input passed all these checks, so we can use it.
		return true;
	}
	
	public static boolean jackpotCheck(int A, int B, int C) {
		if (A == 0 && A == B && A == C) {
			return true;
		}
		return false;
	}
}
