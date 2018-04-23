/*
 * @author Silkia Carmona, Ian Cornish, Nafisa Syed Mohammed, and Toni Ruhl
 */
import java.util.*;
import java.io.*;
import java.sql.*;
import java.awt.geom.Point2D;
/*It is important to note that before you use this class you must populate the database by adding children, activities, and assigning
 *at least 1 behavior per child (by populating the perform class) 
 *When you use this class you are going to want to use populate children first, and judgement second (they are a and b in the trip menu)
 */ 
public class trip {
	static ArrayList<child> tripList = new ArrayList<child>();
	static ArrayList<String> nice = new ArrayList<String>();
	static ArrayList<String> naughty = new ArrayList<String>();
	Point2D.Double currentLocation = new Point2D.Double(72.62, 80.31);
	 
	/*Accepts input args
	 *This method will calculate the average behavior score per child for a user selected year
	 *This method will return that number to be fed into judgement 
	 */
	public double behaviorThreshold(String[] args) throws Exception{
	      databaseConnection.init(args[0]);
	      Connection conn = databaseConnection.getConnection();
	      Scanner in = new Scanner(System.in);
	    	System.out.print("What year would you like to find the behavior threshold for?: " );
	  		String year = in.next();
	  		
	  		System.out.print("The behavior threshold for " + year+ " is: ");
	    	  
	        Statement stat = conn.createStatement();
	        String query = "SELECT avg(scale) FROM (SELECT SUM(a.points) as scale FROM Activity a JOIN perform p on p.activityID = a.activityID WHERE performYear =  '" +year+ "' GROUP BY p.childID) TMP";
	        ResultSet rsone = stat.executeQuery(query);
	     	ResultSetMetaData rsmd = rsone.getMetaData();
	     	int numCols = rsmd.getColumnCount();
	     	boolean force = true;
	     	double threshold;
	     	if(force == true){
	    	rsone.next();
	    		for (int i =1; i<= numCols;){
	    			System.out.print(rsone.getString(i)+ " ");
	    			threshold = Double.parseDouble(rsone.getString(i));
	    	    	System.out.println();
	    	    	return threshold;		
	    	}}
				return 100;	}
	    		
	/*Accepts inout args and input threshold which is generated from the behavior threshold method
	 *This method will add children to naughty and nice arraylists based on how their score for the user selected year compares
	 *to the behavior threshold that is given for that year
	 *This method will not return anything
	 */
	public void judgement(double threshold, String [] args) throws Exception{
		
		Scanner in = new Scanner(System.in);
		
		System.out.print("What year would you like make a judgement on?: " );
		String year = in.next();
		String queryNice = "SELECT c.firstName, c.lastName, TMP.niceID from Child c Join (SELECT p.childID as niceID, SUM(a.points) as totalPoints FROM Activity a JOIN perform p on p.activityID = a.activityID WHERE performYear =  '" +year+ "' GROUP BY p.childID)TMP on c.childID = TMP.niceID WHERE tmp.totalPoints >= "+ threshold;
		String queryNaughty = "SELECT c.firstName, c.lastName, TMP.naughtyID from Child c Join (SELECT p.childID as naughtyID, SUM(a.points) as totalPoints FROM Activity a JOIN perform p on p.activityID = a.activityID WHERE performYear =  '" +year+ "' GROUP BY p.childID)TMP on c.childID = TMP.naughtyID WHERE tmp.totalPoints < " + threshold;
		databaseConnection.init(args[0]);
		try (Connection conn = databaseConnection.getConnection())
	      {
	    	  
	         Statement stat = conn.createStatement();
	        System.out.println();
	        System.out.println("The following Children have been nice this year: " );
	        ResultSet rsNice = stat.executeQuery(queryNice);
	     	ResultSetMetaData rsmdNice = rsNice.getMetaData();
	    	int numCols = rsmdNice.getColumnCount();
	    	while (rsNice.next()){
	    		for (int i =1; i< numCols;i++){	    			
	    	    	nice.add(rsNice.getString(i));
	    	    	System.out.print(rsNice.getString(i)+ " ");
	    		}
	    		System.out.println("");}
	    	
	    	System.out.println();
	        System.out.println("The following Children have been naughty this year: " );
	        ResultSet rsNaughty = stat.executeQuery(queryNaughty);
	     	ResultSetMetaData rsmdNaughty = rsNaughty.getMetaData();
	    	int Cols = rsmdNaughty.getColumnCount();
	    	while (rsNaughty.next()){
	    		for (int i =1; i< Cols;i++){
	    	    	naughty.add(rsNaughty.getString(i));
	    	    	System.out.print(rsNaughty.getString(i)+ " ");
	    		}
	    		System.out.println("");}
	    	System.out.println("");
	    	}    
	     } 
	        
	/*This method will accept nearestChildIndex (which comes from nextStop method) as a parameter
	 *This method will change a child objects delivery status to true and will print waiting for all of the children
	 *that still have not got presents so that the user has a visual representation of how much there is left to go
	 *This method will not return anything
	 */
	public void delivery(int nearestChildIndex) throws Exception{
		currentLocation = tripList.get(nearestChildIndex).getLocation();
			tripList.get(nearestChildIndex).delivered = true;
			for(int i = 0; i<tripList.size(); i++){
				if(tripList.get(i).delivered== true){
				}else{
					System.out.println("Waiting");
				}
			}
	}
	
	/*This method will not accept any parameters
	 *This method will find the nearest child and return the index that they are at so that 
	 *they know where to make the next stop at
	 *This method will return the index of the child that is the closest
	 */
	public int nextStop() throws Exception{
		int nearestChildIndex = 0 ;
		double smallest = 999999;
		for(int i = 0; i<tripList.size(); i++){
			double distance = currentLocation.distance(tripList.get(i).getLocation());	
			if(distance<smallest && tripList.get(i).checkVisited() == false){
				smallest = distance; 
				nearestChildIndex = i;	
				return nearestChildIndex;
			}
		}
		return nearestChildIndex;}
	
	/*This method will not accept any parameters 
	 *This method will print a brief summary of the amount of visits, the amount of nice kids, and the amount of naughty kids
	 *Make sure that before running this that you read the instructions at the top of the class about populating fields
	 *This method will not return anything  
	 */
	public static void getSummary() throws Exception{
		try{
		PrintWriter out = new PrintWriter("yearlyReport.txt");
		out.printf("                    YEAR END REPORT                    ");
		out.println();
		out.printf("-------------------------------------------------------");
		out.println();
		out.println();
		out.printf("Number of visits: %37d\n",tripList.size() );
		out.println();
		out.println();
		out.printf("Number Nice: %42d\n",nice.size()/2);
		out.println();
		out.printf("Number Naughty: %39d\n",naughty.size()/2);
		out.close();
		} catch (ArithmeticException e){
			System.out.println("There have not been any visits made yet this year!");
		}
	}
	
	/*This method will accept args as a parameter 
	 *This method will print the top 5 good kids of all time and include the year that they achieved the feat
	 *This method will not return anything
	 */
	public static void hallOfFame(String [] args) throws Exception{
		databaseConnection.init(args[0]);
	     try (Connection conn = databaseConnection.getConnection())
	      {
	    	  
	         Statement stat = conn.createStatement();
	        System.out.println();
	        String query = "SELECT c.firstName, c.lastName, tmp.totalPoints, tmp.date FROM (SELECT p.performYear as date, p.childID as children, SUM(a.points) as totalPoints FROM Activity a JOIN perform p on p.activityID = a.activityID GROUP BY p.childID, p.performYear)TMP JOIN Child c on TMP.children = c.childID ORDER BY tmp.totalPoints desc FETCH FIRST 5 ROWS ONLY  ";
	        ResultSet rs = stat.executeQuery(query);
	     	ResultSetMetaData rsmd = rs.getMetaData();
	    	int numCols = rsmd.getColumnCount();
	    	System.out.printf("Fist Name      Last Name      Points         Year\n");
	    	while (rs.next()){
	    		for (int i =1; i<= numCols;i++){	    			
	    			System.out.printf("%-15s", rs.getString(i)+ " ");	
	    		}
	    		System.out.println("");}
	    	System.out.print("");
	}	
}
	/*This method will accept args as a parameter 
	 *This method will populate an arraylist with objects of the child class
	 *This method will not return anything
	 */
	public void populateChildren(String[] args)throws Exception{
		databaseConnection.init(args[0]);
	     try (Connection conn = databaseConnection.getConnection())
	      {
	    	 
	         Statement stat = conn.createStatement();
	        System.out.println();
		        String query = "SELECT childID,longitude,latitude FROM Child";
		        ResultSet rs = stat.executeQuery(query);
		        while(rs.next()){
		        tripList.add(new child(rs.getString("childID"), false, rs.getDouble("longitude"), rs.getDouble("latitude")));
	        	}
	        
	        }
	       	}			
	    	}
	


