
import java.util.Random;
import java.util.Scanner;

//class name can be anything, but it must be defined in its own file.
public class nimgame {
	
	public static void main(String[] args) {
		//Setting up instances of our pile class
		Pile pileA = new Pile("A");
		Pile pileB = new Pile("B");
		Pile pileC = new Pile("C");
		//The jackpot is special. We have to redefine its lower and upper limits
		Pile jackpot = new Pile("J");
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
		int choice;
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
				System.out.printf("Take from pile %c?", active_pile.name);
				input_choice = keyboard.nextLine();
				/*Since keyboard.nextLine() produces a string, we have to
				* 1. Validate to ensure that we actually received an integer
				* 2. Parse the value of input_choice into an integer variable*/
				if (input_choice.length() != 1 || input_choice.charAt(0) > 9) {
					choice = 0;
				} else {
					choice = Integer.parseInt(input_choice);
				}
				/* Now that we know our value is actually an integer, we can pass it
				 * to validChoice() to determine if we can actually take it from the
				 * active_pile. Note that validChoice returns true or false. 
				 */
				if (validChoice(choice, active_pile.value)) {
					//Success!
					//TODO: Take value of choice from active_pile
					//The player has taken a turn, so now their opponent gets a turn.
					interaction = false;
				} else {
					//player entered an inactive value, so we switch to the next pile
				}
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
		}
		
		public static boolean validChoice(int choice, int pile) {
			if (choice >= 4) {
				return false;
			}
			if (choice <= 0) {
				return false;
			}
			if (choice > pile) {
				return false;
			}
			return true;
		}

}
