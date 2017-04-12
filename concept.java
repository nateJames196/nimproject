
import java.util.Scanner;

//class name can be anything, but it must be defined in its own file.
public class nimgame {
	
	public static void main(String[] args) {
		//Setting up instances of our pile class
		Pile pileA = new Pile("A");
		Pile pileB = new Pile("B");
		Pile pileC = new Pile("C");
		//The jackpot is special. We have to redefine its lower and upper limits
		Pile jackpot = new Pile("Jackpot");
		jackpot.lowerlimit = -300;
		jackpot.upperlimit = 600;
		
		/*The value below will be used to hold the pile the player is currently 
		 * interacting with. Note: we are not creating an instance of the pile class here,
		 * we are creating a variable of the TYPE pile, similar to the way we would create
		 * a string.
		*/
		Pile active_pile;
		
		//Values below represent the amount of money owned by each player.
		int player1, player2;
		
		//Value denotes whether or not the game is actively accepting input.
		boolean interaction = true;
		
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

			//game "rendering"; now that values are set up, time to display them.
			showPiles(pileA.value, pileB.value, pileC.value);
			showJackpot(jackpot.value);

			//While loop that runs until the player has taken a turn.
			while (interaction == true) {
				/* The following block of code is a do...while statement that receives
				 * player input and allows him or her to choose which pile to take from.
				 */
				do {
					System.out.print("Please enter the pile you would like to take from. ");
					input_choice = keyboard.nextLine();

					if (validInput(input_choice) != 1) {
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
						System.out.print("\nInvalid Input. ");
						System.out.println("Please try again.");
					}
					if (active_pile.isValid() == false) {
						System.out.println("\nThat choice is unavailable at this time. ");
						System.out.println("Please try again.");
					}
				} while (validInput(input_choice) == 1 && active_pile.isValid() == false);
				
				/* The following block of code is a do...while statement that receives
				 * player input and allows him or her to choose how much to take from
				 * the pile they have chosen.
				 */
				do {
					System.out.printf("Please enter the amount to take from pile %c. ", active_pile.name);
					input_choice = keyboard.nextLine();

					if (validChoice(input_choice, active_pile.value) == true) {
						//TODO: CODE
					} else {
						
					}
				} while (validChoice(input_choice, active_pile.value) == false);
				
			}
		}
				
		System.out.println("Press enter to generate again? ");
		quit = keyboard.nextLine();
	}

	/**
	 * showPiles(); is visually cleaner than the messy printf below
	 * we use this function to ensure that only logic appears in the main function
	 * showPiles() itself displays the value of each file.
	 */
	public static void showPiles(int A, int B, int C) {
		System.out.printf("Pile 1: %d\nPile 2: %d\nPile 3: %d\n", A, B, C);
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
		System.out.printf("\nThe jackpot is %s dollars.\n", format_pot);
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
		if (input.charAt(0) > 9) {
			return false;
		}
		/*
		 * We know by now that the string:
		 * 1. Has a length of 1 or more, so it has indexes we can check.
		 * 2. Has a number at index 0, so we know we can use it in an int.
		 */
		int choice = input.charAt(0);
		//4 or more. Number is too big.
		if (choice >= 4) {
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

}
