
/*
 * @author Silkia Carmona, Ian Cornish, Nafisa Syed Mohammed, and Toni Ruhl
 */
public class runner {
	//This program uses derby.jar 
	//runconfig argument should be: src\database.properties
	//This method will run the program
	public static void main(String[] args) throws Exception {
		userInteraction ui = new userInteraction();
		boolean run = true;
		while(run == true){
		ui.navigateMenu(ui.mainMenu(args));
		}

	}

}
