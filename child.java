import java.awt.geom.Point2D;

//Creates object to be used when delivering presents
public class child {
	public String id; 
	public boolean delivered;
	public Point2D.Double coords = new Point2D.Double();
	
	public child(String childID, boolean beenDelivered, double longitude, double latitude){
		id = childID; 
		delivered = beenDelivered;
		coords.setLocation(longitude, latitude);
	}
	//ID Number
	public String getid(){
		return id; 
	}
	//Seeing if the package has been delivered 
	public boolean checkVisited(){
		return delivered;	
	}
	//Visiting the house and changing the status to delivered
	public void visit(){
		delivered = true;
	}
	//the longitude and latitude of the child's house
	public Point2D.Double getLocation(){
		return coords;
	}
}

