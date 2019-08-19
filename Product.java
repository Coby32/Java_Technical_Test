/*
 * Class for product objects that stores the name and stock of a product
 */
public class Product {

	private String name = "";
	private int stock = 0;
	private boolean passesRules = false; //True if product passes all rules, false otherwise
	private boolean blocked = false;	//If true then product will not be ordered regardless of how it is affected by other rules
	private boolean oneOffOrder = false; //If true then order will always be ordered with given quantity if it isn't blocked
	private int oneOffOrderSize = 0;
	
	/*
	 * Constructor
	 */
	public Product(String name, int stock)
	{
		this.name = name;
		this.stock = stock;
	}
	
	/*
	 * Public set method for product name
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	
	/*
	 * Public set method for stock level
	 */
	public void setStock(int stock)
	{
		this.stock = stock;
	}
	
	/*
	 * Public set method for the 'passesRules' variable
	 */
	public void setPassesRules(boolean pass)
	{
		this.passesRules = pass;
	}
	
	/*
	 * Public set method for the blocked variable
	 */
	public void setBlocked(boolean blocked)
	{
		this.blocked = blocked;
	}
	
	/*
	 * Public set method for the one off order variable
	 */
	public void setOneOffOrder(boolean oneOffOrder)
	{
		this.oneOffOrder = oneOffOrder;
	}
	
	/*
	 * Public set method for the one off order size variable
	 */
	public void setOneOffOrderSize(int oneOffOrderSize)
	{
		this.oneOffOrderSize = oneOffOrderSize;
	}
	
	/*
	 * Public get method for product name
	 */
	public String getName()
	{
		return name;
	}
	
	/*
	 * Public get method for the products stock level
	 */
	public int getStock()
	{
		return stock;
	}
	
	/*
	 * Public get method for 'passesRules' variable
	 */
	public boolean getPassesRules()
	{
		return passesRules;
	}
	
	/*
	 * Public get method for 'blocked' variable
	 */
	public boolean getBlocked()
	{
		return blocked;
	}
	
	/*
	 * Public get method for 'one off order' variable
	 */
	public boolean getOneOffOrder()
	{
		return oneOffOrder;
	}
	
	/*
	 * Public get method for 'one off order size' variable
	 */
	public int getOneOffOrderSize()
	{
		return oneOffOrderSize;
	}
}
