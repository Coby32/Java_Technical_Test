import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JOptionPane;

/*
 * Storage for products & rules
 * Handles operations pertaining to products 
 * (i.e. processing user input order and creating appropriate output)
 */
public class StockHandler {
	
	//Array list to store list of products in stock
	private ArrayList<Product> productList = new ArrayList<Product>();
	
	//Array to store input file
	private ArrayList<Rule> ruleList = new ArrayList<Rule>();
	
	//File that will be used as basis for the output file
	private File outputFileBasis;
	
	/*
	 * Constructor
	 */
	public StockHandler()
	{
		System.out.println("StockHandler Initialised");
	}

	/*
	 * Open the given stock file and store contents inside Products array list
	 */
	public void loadStockFile(File file)
	{		
		try
		{
			this.outputFileBasis = file;
			Scanner scan = new Scanner(file);
			
			//Scan through file and store each line as a product
			while(scan.hasNextLine())
			{
				String line = scan.nextLine();
				String[] split = line.split("\\s+|,\\s+");
				
				//Check if the line was split into a valid input (first part is a string (a to z) and second part is a number)
				if(split[0].matches("^[a-zA-F]{1}$") && split[1].matches("^[0-9]{1,3}$"))
				{
					Product p = new Product(split[0], Integer.parseInt(split[1]));
					productList.add(p);
				}
				else
				{
					System.out.println("Invalid stock line in input file. It was ignored. (" + split[0] + ", " + split[1] + ")");
				}
			}
			
			//Close File
			scan.close();
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, "Error when loading file", "File Load Error", JOptionPane.ERROR_MESSAGE);
			System.err.println("Error in Loading File:\n" + e);
		}
		
		//Print product list to check
		for(int i = 0; i < productList.size(); i++)
		{
			System.out.println(productList.get(i).getName() + " " + productList.get(i).getStock());
		}
	}
	
	/*
	 * Load the given rules file and store the contents in the rules array list
	 */
	public void loadRulesFile(File file)
	{
		try
		{
			Scanner scan = new Scanner(file);
			
			//Scan through file and store each line as a product
			while(scan.hasNextLine())
			{
				String line = scan.nextLine();
				String[] split = line.split("\\s+|,\\s+");
				
				//If length is 3 then the quantity at the end was included
				if(split.length == 3)
				{
					//Check if the line was split into a valid input (first part is a string (a to z), second part is a word and third part is a number)
					if(split[0].matches("^[a-zA-F]{1}$") && split[1].matches("^[a-z]{1,5}$") && split[2].matches("^[0-9]{1,3}$"))
					{
						//Check that the second part matches the available rules (min, block, and order)
						if(split[1].equals("min") || split[1].equals("block") || split[1].equals("order"))
						{
							Rule r = new Rule(split[0], split[1], Integer.parseInt(split[2]));
							ruleList.add(r);
						}
					}
					else
					{
						System.out.println("Invalid rule line in input file. It was ignored. (" + split[0] + " " + split[1] + " " + split[2] + ")");
					}
				}
				else if(split.length == 2) //If length of 2 then quantity omitted (ony possible with 'block', as it isn't mandatory for the rule)
				{
					//Check if the line was split into a valid input (first part is a string (a to z) and the second part is a word)
					if(split[0].matches("^[a-zA-F]{1}$") && split[1].matches("^[a-z]{1,5}$"))
					{
						//Check that the second part matches the block rule (quantity auto-set to 0)
						if(split[1].equals("block"))
						{
							Rule r = new Rule(split[0], split[1], 0);
							ruleList.add(r);
						}
					}
					else
					{
						System.out.println("Invalid rule line in input file. It was ignored. (" + split[0] + " " + split[1] + ")");
					}
				}
				else
				{
					System.out.println("Invlaid rule. (Wrong syntax)");
				}
			}
			
			//Close File
			scan.close();
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, "Error when loading file", "File Load Error", JOptionPane.ERROR_MESSAGE);
			System.err.println("Error in Loading File:\n" + e);
		}
		
		//Print rules list to check
		for(int i = 0; i < ruleList.size(); i++)
		{
			System.out.println(ruleList.get(i).getProductAffected() + " " + ruleList.get(i).getType() + " " + ruleList.get(i).getQuantity());
		}
	}
	
	/*
	 * Process the contents of input files, determine responses
	 * Alter variables of the products dependent on the rules applied
	 */
	public void processInput()
	{
		String product = "";
		String type = "";
		int quantity = 0;
		
		//Process rules and stock
		//Loop through each rule and determine if the product affected passes/fails it
		for(int i = 0; i < ruleList.size(); i++)
		{
			product = ruleList.get(i).getProductAffected();
			type = ruleList.get(i).getType();
			quantity = ruleList.get(i).getQuantity();
			
			//Go through each product to find the one affected by the current rule
			for(int j = 0; j < productList.size(); j++)
			{
				//If product matches product affected by rule then
				if(productList.get(j).getName().equals(product))
				{
					if(type.equals("min"))
					{
						//Check stock level of product versus the minimum quantity given
						if(productList.get(j).getStock() < quantity)
						{
							productList.get(j).setPassesRules(true);
						}
						else
						{
							productList.get(j).setPassesRules(false);
						}
					}
					else if(type.equals("block"))
					{
						productList.get(j).setBlocked(true);
					}
					else if(type.equals("order"))
					{
						productList.get(j).setOneOffOrder(true);
						productList.get(j).setOneOffOrderSize(quantity);
					}
				}
			}
		}
		
		System.out.println("Processed Input");
		
		//Start creation of output file
		createOutputFile();
	}
	
	/*
	 * Create output file with responses created from processing the input files
	 * 
	 * If product passes rules is below minimum set then recommend ordering, otherwise don't
	 * If product is has a one off bulk order request then recommend order with the given quantity
	 * If product is blocked then it isn't ordered
	 */
	private void createOutputFile()
	{
		//Create new file with name and path based on original input file
		String newFileName = outputFileBasis.getName().replaceAll(".stockdb", "_OUTPUT.result");
		String path = outputFileBasis.getPath().replaceAll(outputFileBasis.getName(), newFileName);
		File outputFile = new File(path);
		
		try
		{
			//Open output file
			PrintWriter outputWriter = new PrintWriter(outputFile); 
			
			//Check products and generate appropriate response
			for(int i = 0; i < productList.size(); i++)
			{
				//Check if blocked
				if(productList.get(i).getBlocked() == false)
				{
					//Check if one off order is requested
					if(productList.get(i).getOneOffOrder() == false)
					{
						//If product has passed all rules
						if(productList.get(i).getPassesRules())
						{
							//Print product passes rules response
							outputWriter.println(productList.get(i).getName() + " should be ordered.");
						}
						else
						{
							//Print product fails rules response
							outputWriter.println(productList.get(i).getName() + " shouldn't be ordered.");
						}
					}
					else
					{
						//Print product one off order response
						outputWriter.println(productList.get(i).getName() + " should be ordered. (One-off order of " + productList.get(i).getOneOffOrderSize() + ")");
					}
				}
				else
				{
					//Print product blocked response
					outputWriter.println(productList.get(i).getName() + " shouldn't be ordered. (Blocked)");
				}
			}
			
			//Close File
			outputWriter.close();
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, "Error when generating output file", "File creation Error", JOptionPane.ERROR_MESSAGE);
			System.err.println("Error in creating output file:\n" + e);
		}
		
		System.out.println("Created output file");
		
	}
}
