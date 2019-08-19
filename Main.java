/*
 * Java Technical Test
 * Created by: James Coburn
 */
public class Main {

	public static void main(String[] args) 
	{
		//Create StockHandler Object
		StockHandler stockHandler = new StockHandler();
		
		//Start application
		UserInterface ui = new UserInterface(stockHandler);
	}
}
