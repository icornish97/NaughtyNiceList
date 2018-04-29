import java.io.*;
import java.sql.*;
import java.util.*;

/*
 * @author Silkia Carmona, Ian Cornish, Nafisa Syed Mohammed, and Toni Ruhl
 */
public class database 
{ 
	/*All methods in this class will take args as a parameter
	 *All methods are also void so they will not return anything
	 *It is important to note that if this is the first time running this program that you should create the tables first (a in the database menu)
	 *however once you have done this it shouldn't be necessacary to use create or drop table methods (a and b respectively in the database menu)
	 */
	
	
	//This method will create the tables that compose the database
   public static void createDataBase(String[] args) throws Exception
   {   
	   try{
	   if (args.length == 0)
	      {   
	         System.out.println(
	               "Usage: java -classpath driver_class_path" 
	               + File.pathSeparator 
	               + ". TestDB propertiesFile");
	         return;
	      }

	      databaseConnection.init(args[0]);
	      
	      try (Connection conn = databaseConnection.getConnection())
	      {
	    	  
	         Statement stat = conn.createStatement();
	         stat.execute("CREATE TABLE Child (childID CHAR(36) NOT NULL, firstName VARCHAR(20), lastName VARCHAR(20), zipCode CHAR(5), longitude DECIMAL(9,6), latitude DECIMAL(9,6), CONSTRAINT child_pk PRIMARY KEY(childID))");
	         stat.execute("CREATE TABLE Toy (toyID CHAR(36) NOT NULL, toyDesc VARCHAR(60), CONSTRAINT toy_pk PRIMARY KEY(toyID))");
	         stat.execute("CREATE TABLE Activity (activityID CHAR(36) NOT NULL, activityDesc VARCHAR(60), points INT, CONSTRAINT activity_pk PRIMARY KEY (activityID))");
	         stat.execute("CREATE TABLE Wish (childID CHAR(36) NOT NULL, toyID CHAR(36) NOT NULL, wishYear CHAR(4) NOT NULL, CONSTRAINT wish_pk PRIMARY KEY(childID, toyID, wishYear), CONSTRAINT wish_fk1 FOREIGN KEY(childID) REFERENCES Child (childID) ON DELETE CASCADE, CONSTRAINT wish_fk2 FOREIGN KEY(toyID) REFERENCES Toy (toyID) ON DELETE CASCADE)");
	         stat.execute("CREATE TABLE perform (childID CHAR(36) NOT NULL, activityID CHAR(36) NOT NULL, performYear CHAR(4) NOT NULL, CONSTRAINT perform_pk PRIMARY KEY(childID, activityID, performYear), CONSTRAINT perform_fk1 FOREIGN KEY(childID) REFERENCES Child (childID) ON DELETE CASCADE, CONSTRAINT perform_fk2 FOREIGN KEY(activityID) REFERENCES Activity (activityID) ON DELETE CASCADE)");
	      }} catch (SQLException e){
	    	  System.out.println("The tables have already been created");
	    	  System.out.println();
	      }
	   }
   
   
   
   //This method will drop all of the tables in the database, currently only used for the sake of our sanity
   public static void dropTables(String[] args) throws Exception
   {   
	   try{
	   if (args.length == 0)
	      {   
	         System.out.println(
	               "Usage: java -classpath driver_class_path" 
	               + File.pathSeparator 
	               + ". TestDB propertiesFile");
	         return;
	      }

	      databaseConnection.init(args[0]);
	      
	      try (Connection conn = databaseConnection.getConnection())
	      {
	    	  
	         Statement stat = conn.createStatement();
	         stat.execute("DROP TABLE Wish");
	         stat.execute("DROP TABLE Perform");
	         stat.execute("DROP TABLE Child");
	         stat.execute("DROP TABLE Toy");
	         stat.execute("DROP TABLE Activity");	         




	      }} catch (SQLException e){
	    	  System.out.println("The tables do not exist");
	    	  System.out.println();
	      }
   }
   
   
   //This method will add a record to the child table in the SQL Database
   public static void addChild(String[] args) throws Exception{
	   
	   Scanner in = new Scanner(System.in);

	   File latLong = new File("zipLatLongTD.txt");
	   Scanner ll = new Scanner(latLong);
	   
	   
	   
	   //Generate unique identifier for each child being added to the database 
	   
	   String childID = UUID.randomUUID().toString();
	   
	   //Collect a variety of information to be stored in the database
	   try{
	   System.out.print("First Name:" );
	   String childFName = in.next();
	   
	   System.out.println("");
	   
	   System.out.println("Last Name:");
	   String childLName = in.next(); 
	   
	   System.out.println("");
	   
	   System.out.println("Zip Code: ");
	   String childZip = in.next();
	   
	   //Store text file containing zip code, latitude, and longitude into Arraylists
	   
	   ArrayList<String> zip = new ArrayList<String>();
	   ArrayList<Double> latitude = new ArrayList<Double>();
	   ArrayList<Double> longitude = new ArrayList<Double>();
	   
	   while(ll.hasNext()){
		   zip.add(ll.next());
		   latitude.add(Double.parseDouble(ll.next()));
		   longitude.add(Double.parseDouble(ll.next()));
	   }
	   

	   //Gets the Latitude and Longitude for the child based on their zip code MAKE SURE TO CLEAN TO DEAL WITH BAD ZIPS 
	   int zipIndex = Collections.binarySearch(zip, childZip);
	   Double childLat = latitude.get(zipIndex);
	   Double childLong = longitude.get(zipIndex);
	   
	   //Connect to the database
	   if (args.length == 0)
	      {   
	         System.out.println(
	               "Usage: java -classpath driver_class_path" 
	               + File.pathSeparator 
	               + ". TestDB propertiesFile");
	         return;
	      }

	      databaseConnection.init(args[0]);
	      
	      try (Connection conn = databaseConnection.getConnection())
	      {
	    	  
	         Statement stat = conn.createStatement();
	         
	         stat.execute("INSERT INTO Child Values('"+childID+"', '"+childFName+"','"+childLName+"','"+ childZip+"',"+childLat+","+childLong+")");
	      }   } catch (ArrayIndexOutOfBoundsException e){
	    	  System.out.print("Invalid value!");
	    	  } 
	      }
	   
	   
	   


   //This method will drop a child from the database based on the ID number provided
   public static void dropChild(String[] args) throws Exception{
	   
	   Scanner in = new Scanner(System.in);
	   
	   System.out.println("What is the ID of the child that you would like to remove?: ");
	   String dropChildID = in.next();
	   
	   //Connect to the database
	   if (args.length == 0)
	      {   
	         System.out.println(
	               "Usage: java -classpath driver_class_path" 
	               + File.pathSeparator 
	               + ". TestDB propertiesFile");
	         return;
	      }

	      databaseConnection.init(args[0]);
	      
	      try (Connection conn = databaseConnection.getConnection())
	      {
	    	  
	         Statement stat = conn.createStatement();
	         
	         stat.execute("DELETE FROM Child WHERE childID ='" +dropChildID+"'" );
	      }
	   }
		
	
   
   //This method will add a toy into the database
	public static void addToy(String[] args) throws Exception{
		//Create unique id 
		String toyID = UUID.randomUUID().toString();
		//Get values to be inserted into database		
		Scanner in = new Scanner(System.in);
		
		System.out.println("Toy Description: ");
		String toyDesc = in.next();
		 
		//Connect to the database
		   if (args.length == 0)
		      {   
		         System.out.println(
		               "Usage: java -classpath driver_class_path" 
		               + File.pathSeparator 
		               + ". TestDB propertiesFile");
		         return;
		      }

		      databaseConnection.init(args[0]);
		      
		      try (Connection conn = databaseConnection.getConnection())
		      {
		    	  
		         Statement stat = conn.createStatement();
		         
		         stat.execute("INSERT INTO Toy Values('"+toyID+"','"+ toyDesc+"')");
		      }

   }
	
	//This method will drop a tow from the database
	public static void dropToy(String[] args) throws Exception{
		   
		   Scanner in = new Scanner(System.in);
		   
		   System.out.println("What is the ID of the toy that you would like to remove?: ");
		   String dropToyID = in.next();
		   
		   //Connect to the database
		   if (args.length == 0)
		      {   
		         System.out.println(
		               "Usage: java -classpath driver_class_path" 
		               + File.pathSeparator 
		               + ". TestDB propertiesFile");
		         return;
		      }

		      databaseConnection.init(args[0]);
		      
		      try (Connection conn = databaseConnection.getConnection())
		      {
		    	  
		         Statement stat = conn.createStatement();
		         
		         stat.execute("DELETE FROM Toy WHERE toyID ='" +dropToyID+"'" );
		      }
			
		}
	   
	//This method will add an activity to the database   
	public static void addActivity(String[] args) throws Exception{
		//Create unique id
		String activityID = UUID.randomUUID().toString();
		//Get values to be inserted into database
		Scanner in = new Scanner(System.in);
		
		System.out.println("Activity description: ");
		String actDesc = in.nextLine();
		System.out.println("Points: ");
		int point = in.nextInt();
		
		//Connect to the database
		   if (args.length == 0)
		      {   
		         System.out.println(
		               "Usage: java -classpath driver_class_path" 
		               + File.pathSeparator 
		               + ". TestDB propertiesFile");
		         return;
		      }

		      databaseConnection.init(args[0]);
		      
		      try (Connection conn = databaseConnection.getConnection())
		      {
		    	  
		         Statement stat = conn.createStatement();
		         
		         stat.execute("INSERT INTO Activity Values('"+activityID+"','"+ actDesc+"',"+ point+")");
		      }}
		
   //This method will drop an activity from the database
	public static void dropActivity(String[] args) throws Exception{
		   
		   Scanner in = new Scanner(System.in);
		 
		   System.out.println("What is the ID of the Activity that you would like to remove?: ");
		   String dropActivityID = in.next();
		   
		   //Connect to the database
		   if (args.length == 0)
		      {   
		         System.out.println(
		               "Usage: java -classpath driver_class_path" 
		               + File.pathSeparator 
		               + ". TestDB propertiesFile");
		         return;
		      }

		      databaseConnection.init(args[0]);
		      
		      try (Connection conn = databaseConnection.getConnection())
		      {
		    	  
		         Statement stat = conn.createStatement();
		         
		         stat.execute("DELETE FROM Activity WHERE activityID ='" +dropActivityID+"'" );
		      }}
			
	
	//This method will add a performance (of an activity) into the database   
	public static void addPerform(String[] args) throws Exception{
		try{
		//Get values to be inserted into database
		database db = new database(); 
		Scanner in = new Scanner(System.in);
		db.getTable(args, "Child");
		System.out.println("Child id:");
		String childID = in.next();
		db.getTable(args, "Activity");
		System.out.println("Activity id:");
		String actID = in.next();
		System.out.println("Year:");
		String year = in.next();
		
		//Connect to the database
		   if (args.length == 0)
		      {   
		         System.out.println(
		               "Usage: java -classpath driver_class_path" 
		               + File.pathSeparator 
		               + ". TestDB propertiesFile");
		         return;
		      }

		      databaseConnection.init(args[0]);
		      
		      try (Connection conn = databaseConnection.getConnection())
		      {
		    	  
		         Statement stat = conn.createStatement();
		         
		         stat.execute("INSERT INTO perform Values('"+childID+"','"+ actID+"','"+ year+"')");
		      }}catch (SQLDataException e){
		    	  System.out.println("Invalid Input");
		      }
   }
	
	//This method will drop a performance (of an activity) into the database
	public static void dropPerform(String[] args) throws Exception{
		   
		   database db = new database(); 
		   Scanner in = new Scanner(System.in);
		   
		   db.getTable(args, "Perform");
		   System.out.println("What is the ID of the child that you would like to remove?: ");
		   String dropChildID = in.next();
		   System.out.println("What is the ID of the activity that you would like to remove?: ");
		   String dropActivityID = in.next();
		   System.out.println("What year would you like to remove?: ");
		   String dropYear = in.next();
		   
		   //Connect to the database
		   if (args.length == 0)
		      {   
		         System.out.println(
		               "Usage: java -classpath driver_class_path" 
		               + File.pathSeparator 
		               + ". TestDB propertiesFile");
		         return;
		      }

		      databaseConnection.init(args[0]);
		      
		      try (Connection conn = databaseConnection.getConnection())
		      {
		    	  
		         Statement stat = conn.createStatement();
		         
		         stat.execute("DELETE FROM Perform WHERE childID ='" +dropChildID+ "' AND ToyID ='" +dropActivityID+"' AND performYear ='" + dropYear +"'" );
		      }
		}
	   
	//This method will add a wish to the database  
	public static void addWish(String[] args) throws Exception{
		try{
		database db = new database(); 
		//Get values to be inserted into database
		Scanner in = new Scanner(System.in);
	
		db.getTable(args, "Child");
		System.out.println("Child id:");
		String childID = in.next();
		db.getTable(args, "Toy");
		System.out.println("Toy id: ");
		String toyID = in.next(); 
		System.out.println("Year: ");
		String year=in.next();
		
		//Connect to the database
		   if (args.length == 0)
		      {   
		         System.out.println(
		               "Usage: java -classpath driver_class_path" 
		               + File.pathSeparator 
		               + ". TestDB propertiesFile");
		         return;
		      }

		      databaseConnection.init(args[0]);
		      
		      try (Connection conn = databaseConnection.getConnection())
		      {
		    	  
		         Statement stat = conn.createStatement();
		         
		         stat.execute("INSERT INTO Wish Values('"+childID+"','"+ toyID+"','"+ year+"')");
		      }} catch(SQLDataException e ){
		    	  System.out.print("Invalid Input!");
		      }
   }
	
	//This method will drop a wish to the database
	public static void dropWish(String[] args) throws Exception{
		   database db = new database();
		   db.getTable(args, "Wish");
		   Scanner in = new Scanner(System.in);
		 
		   System.out.println("What is the ID of the child that you would like to remove?: ");
		   String dropChildID = in.next();
		   System.out.println("What is the ID of the toy that you would like to remove?: ");
		   String dropToyID = in.next();
		   System.out.println("What is the ID of the child that you would like to remove?: ");
		   String dropYear = in.next();
		   
		   //Connect to the database
		   if (args.length == 0)
		      {   
		         System.out.println(
		               "Usage: java -classpath driver_class_path" 
		               + File.pathSeparator 
		               + ". TestDB propertiesFile");
		         return;
		      }

		      databaseConnection.init(args[0]);
		      
		      try (Connection conn = databaseConnection.getConnection())
		      {
		    	  
		         Statement stat = conn.createStatement();
		         
		         stat.execute("DELETE FROM Child WHERE childID ='" +dropChildID+ "' AND ToyID ='" +dropToyID+"' AND wishYear ='" + dropYear +"'" );
		      
		      }
	}

	//This method will change an element in a table based on user inputs 
    public static void alterTable(String[] args) throws Exception{
		   database db = new database();
		   Scanner in = new Scanner(System.in);
		 
		   System.out.println("What table would you like to alter: ");
		   String tableName = in.next();
		   db.getTable(args, tableName);
		   System.out.println("What column would you like to update?: ");
		   String columnName = in.next();
		   System.out.println("What record do you want to update?: ");
		   String updatedValue = in.next();
		   System.out.println("What value do you want to put there?: ");
		   String valueUpdated = in.next();	   
		   //Connect to the database
		   if (args.length == 0)
		      {   
		         System.out.println(
		               "Usage: java -classpath driver_class_path" 
		               + File.pathSeparator 
		               + ". TestDB propertiesFile");
		         return;
		      }

		      databaseConnection.init(args[0]);
		      
		      try (Connection conn = databaseConnection.getConnection())
		      {
		    	  
		         Statement stat = conn.createStatement();
		         if(valueUpdated instanceof String){
		        	stat.execute("UPDATE " + tableName + " SET " +columnName+ " = '" + updatedValue + "' WHERE " + columnName + " = '" + valueUpdated+"' "); 
		         }else{
		  	        stat.execute("UPDATE " + tableName + " SET " +columnName+ " = " + updatedValue + " WHERE " + columnName + " = " + valueUpdated);
		         }
		         
		      }
			
		}

    //This method will accept a string of the table name (child, activity, toy, perform, or wish)
    //This method will print the table that the user selected
    public void getTable(String[] args, String table) throws Exception{
	   //Connect to the database
	
	   if (args.length == 0)
	      {   
	         System.out.println(
	               "Usage: java -classpath driver_class_path" 
	               + File.pathSeparator 
	               + ". TestDB propertiesFile");
				return;		         
	      }

	      databaseConnection.init(args[0]);
	      
	      try (Connection conn = databaseConnection.getConnection())
	      {
	    	  
	         Statement stat = conn.createStatement();
	         
	         String Query = "SELECT * FROM " +table;
	         ResultSet rs = stat.executeQuery(Query);
	     	ResultSetMetaData rsmd = rs.getMetaData();
	    	int numCols = rsmd.getColumnCount();
	    	while (rs.next()){
	    		for (int i =1; i<= numCols;i++){
	    			System.out.print(rs.getString(i)+ " ");
	    		}
	    		System.out.println("");}
	      }
}
}
	    
	     

	   


    

