/*
 * Class for rule objects that stores the type of rule, product affected, quantity
 */
public class Rule {

	private String type = "";	//'min' = minimum, 'block' = blocked order, and 'order' = bulk one-off order
	private String productAffected = "";
	private int quantity = 0;
	
	/*
	 * Constructor
	 */
	public Rule(String productAffected, String type, int quantity)
	{
		this.productAffected = productAffected;
		this.type = type;
		this.quantity = quantity;
	}
	
	/*
	 * Public set method for type
	 */
	public void setType(String type)
	{
		this.type = type;
	}
	
	/*
	 * Public set method for productAffected
	 */
	public void setProductAffected(String productAffected)
	{
		this.productAffected = productAffected;
	}
	
	/*
	 * Public set method for quantity
	 */
	public void setQuantity(int quantity)
	{
		this.quantity = quantity;
	}
	
	/*
	 * Public get method for type
	 */
	public String getType()
	{
		return type;
	}
	
	/*
	 * Public get method for productAffected
	 */
	public String getProductAffected()
	{
		return productAffected;
	}
	
	/*
	 * Public get method for quantity
	 */
	public int getQuantity()
	{
		return quantity;
	}
}
