import java.util.*;
import java.io.*;
import java.sql.*;
/*
 * @author Silkia Carmona, Ian Cornish, Nafisa Syed Mohammed, and Toni Ruhl
 */
public class userInteraction {
	
/*
 *This method accepts args as a parameter
 *This method gets the user's choice for what part of the program they would like to access
 *Returns userChoice
 */
	public String mainMenu(String[] args) throws Exception{

		Scanner in = new Scanner(System.in);
		trip tr = new trip();
		System.out.println("Select A to interact with the database");
		System.out.println("Select B to plan a trip");
		System.out.println("Select Q to quit");
		System.out.print("Enter option: ");
		String userChoice = in.next();
		System.out.println("");
		boolean kill = false;
		while(kill == false){
			if(userChoice.equalsIgnoreCase("A")){
				userChoice = "databaseMenu";
				kill = true;
			}else if(userChoice.equalsIgnoreCase("B")){
				userChoice = "tripMenu";
				kill= true; 
			}else if(userChoice.equalsIgnoreCase("Q")){
				userChoice = "quit";
				kill = true;
			}else{
				System.out.println("Invalid option!");
				System.out.println("Please select a new option: ");
				userChoice = in.next();
			} 
		}
		return userChoice;
	}

/*This method accepts args  
 *We will prompt the user to enter what they would like to do 
 *Returns one of the following values: create, destroy, alter, select, add/drop Child/Toy/Activity/Perform/Wish, databaseMenu, mainMenu, or quit
 */
	public String databaseMenu(String[] args) throws Exception{
		
		String userChoice;
		boolean kill = false;
		boolean killAdd = false;
		boolean killDrop = false;
		Scanner in = new Scanner(System.in);
		database db = new database();
		
		System.out.println("Select A to create the database tables");
		System.out.println("Select B to drop the database tables");
		System.out.println("Select C to alter a record in the database");
		System.out.println("Select D to select a table");
		System.out.println("Select E to add a record to a table");
		System.out.println("Select F to remove a record from a table");
		System.out.println("Select M to return the main menu");
		System.out.println("Select Q to quit");
		System.out.println("Enter choice: ");
		userChoice = in.next();
		System.out.println("");
		while(kill == false)
		{
			
			if (userChoice.equalsIgnoreCase("A")){
				userChoice = "create";
				database.createDataBase(args);
				kill = true;
			}
			else if(userChoice.equalsIgnoreCase("B")){
				userChoice = "destroy";
				database.dropTables(args);
				kill = true;
			}
			else if(userChoice.equalsIgnoreCase("C")){
				userChoice = "alter";
				database.alterTable(args);
				kill = true;
			}
			else if(userChoice.equalsIgnoreCase("D")){
				userChoice = "select";
				System.out.println("Table names: Child : Toy : Activity : Perform : Wish :");
				System.out.println("What table would you like to select?: ");
				
				String table = in.next().trim();
				db.getTable(args, table);
				kill = true;
			}
			else if(userChoice.equalsIgnoreCase("E")){
						
				System.out.println("Select A to add a child");
				System.out.println("Select B to add a toy");
				System.out.println("Select C to add an activity");
				System.out.println("Select D to report behavior");
				System.out.println("Select E to add to a wishlist");
				System.out.println("Select F to return to the database menu");
				System.out.println("Select M to reurn to the main menu");
				System.out.println("Select Q to quit");
				System.out.println("Enter option: ");
				String tableAdd = in.next();
				System.out.println("");
				
				while(killAdd == false){
					
					if(tableAdd.equalsIgnoreCase("A")){
						userChoice = "addChild";
						database.addChild(args);
						killAdd = true; 
					}
					else if(tableAdd.equalsIgnoreCase("B")){
						userChoice = "addToy";
						database.addToy(args);
						killAdd = true; 
					}
					else if(tableAdd.equalsIgnoreCase("C")){
						userChoice = "addActivity";
						database.addActivity(args);
						killAdd = true; 
					}
					else if(tableAdd.equalsIgnoreCase("D")){
						userChoice = "addPerform";
						database.addPerform(args);
						killAdd = true; 
					}
					else if(tableAdd.equalsIgnoreCase("E")){
						userChoice = "addWish";
						database.addWish(args);
						killAdd = true; 
					}
					else if(tableAdd.equalsIgnoreCase("F")){
						userChoice = "databaseMenu";
						killAdd = true; 
					}
					else if(tableAdd.equalsIgnoreCase("M")){
						userChoice = "mainMenu";
						killAdd = true; 
					}
					else if(tableAdd.equalsIgnoreCase("Q")){
						userChoice =  "quit";
						killAdd = true; 
					}
					else{
						System.out.println("Invalid option!");
						System.out.print("Enter a valid option: ");
						tableAdd = in.next();
					}
				}	
				kill = true;
			}
			else if(userChoice.equalsIgnoreCase("F")){
				System.out.println("Select A to remove a child");
				System.out.println("Select B to remove a toy");
				System.out.println("Select C to remove an activity");
				System.out.println("Select D to remove a behavior");
				System.out.println("Select E to remove from a wishlist");
				System.out.println("Select F to return to the database menu");
				System.out.println("Select M to reurn to the main menu");
				System.out.println("Select Q to quit");
				System.out.println("Enter option: ");
				String tableDrop = in.next();
				System.out.println("");
				
				while(killDrop == false){
					if(tableDrop.equalsIgnoreCase("A")){
						userChoice = "dropChild";
						db.getTable(args, "Child");
						database.dropChild(args);
						killDrop = true;
					}
					else if(tableDrop.equalsIgnoreCase("B")){
						userChoice = "dropToy";
						db.getTable(args, "Toy");
						database.dropToy(args);
						killDrop = true;
					}
					else if(tableDrop.equalsIgnoreCase("C")){
						userChoice = "dropActivity";
						db.getTable(args, "Activity");
						database.dropActivity(args);
						killDrop = true;
					}
					else if(tableDrop.equalsIgnoreCase("D")){
						userChoice = "dropPerform";
						db.getTable(args, "Perform");
						database.dropPerform(args);
						killDrop = true;
					}
					else if(tableDrop.equalsIgnoreCase("E")){
						userChoice = "dropWish";
						db.getTable(args, "Wish");
						database.dropWish(args);
						killDrop = true;
					}
					else if(tableDrop.equalsIgnoreCase("F")){
						userChoice = "databaseMenu";
						killDrop = true;
					}
					else if(tableDrop.equalsIgnoreCase("M")){
						userChoice = "mainMenu";
						killDrop = true;
					}
					else if(tableDrop.equalsIgnoreCase("Q")){
						userChoice = "quit";
						killDrop = true;
					}
					else{
						System.out.println("Invalid option!");
						System.out.print("Enter a valid option: ");
						tableDrop = in.next();
					}
				}	
				kill = true;
			}
			else if(userChoice.equalsIgnoreCase("M")){
				userChoice = "mainMenu";
				kill = true;
			}
			else if(userChoice.equalsIgnoreCase("Q")){
				userChoice  = "quit";
				kill = true;
			}
			else{
				System.out.println("Invalid option!");
				System.out.print("Enter a valid option: ");
				userChoice = in.next();
			}
		}
		
		return userChoice;
	    } 
	
/*This method accepts no parameters
 * This will ask the user what trip functionalities they would like to use
 * A value describing the users choice will be returned 
 */
	public String tripMenu(String[] args)throws Exception{
		
		String userChoice; 
		trip tr = new trip();
		Scanner in = new Scanner(System.in);

		System.out.println("Select A to Populate the trip list");
		System.out.println("Select B judge who's been naughty and who's been nice");
		System.out.println("Select C to make a delivery");
		System.out.println("Select D to make a yearly summary");
		System.out.println("Select E to view the hall of fame");
		System.out.println("Select M to return to the main menu");
		System.out.println("Select Q to quit");
		System.out.print("Enter option: ");
		userChoice = in.next();
		System.out.println("");

		boolean kill = false;
		
		while(kill == false){
		if (userChoice.equalsIgnoreCase("A")){
			userChoice = "populateChildren";	
			tr.populateChildren(args);
			kill = true; 
		}
		else if(userChoice.equalsIgnoreCase("B")){
			userChoice ="judge";
				tr.judgement(tr.behaviorThreshold(args), args);
				kill = true;
			}
		
		else if(userChoice.equalsIgnoreCase("C")){
			tr.delivery(tr.nextStop());
			userChoice = "delivery";
			kill = true; 
		}
		else if(userChoice.equalsIgnoreCase("D")){
			userChoice = "summary";
			tr.getSummary();
			kill = true; 
		}
		else if(userChoice.equalsIgnoreCase("E")){
			userChoice = "hallOfFame";
			trip.hallOfFame(args);
			kill = true; 
		}
		else if(userChoice.equalsIgnoreCase("M")){
			userChoice = "mainMenu";
			kill = true; 
		}
		else if(userChoice.equalsIgnoreCase("Q")){
			userChoice = "quit";
			kill = true; 
		}
		else {
			System.out.println("Invalid option!");
			System.out.print("Enter a valid option: ");
			userChoice = in.next();
		}
		}
		return userChoice;
	    } 
	
	
/*
 * This method accepts a parameter called menuName 
 * This method will aid the user in navigating through the menus based on the input that it receives
 * This method will not return any values
 */
	public void navigateMenu(String menuName) throws Exception{
		
		userInteraction ui = new userInteraction();
		String[] args = {"src\\database.properties"}; 
		if(menuName.equalsIgnoreCase("databaseMenu")){
			ui.navigateMenu(ui.databaseMenu(args));
		}
		else if(menuName.equalsIgnoreCase("tripMenu")){
			ui.navigateMenu(ui.tripMenu(args));
		}
		else if(menuName.equalsIgnoreCase("mainMenu")){
			ui.navigateMenu(ui.mainMenu(args));
		}
		else if(menuName.equalsIgnoreCase("quit")){
			System.exit(0);
		}		
	}
}
