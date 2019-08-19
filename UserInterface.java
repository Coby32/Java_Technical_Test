import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

/*
 * Class to initialise and handle the user interface
 */
public class UserInterface extends JFrame implements ActionListener {

	//Window variable
	private JPanel window;
	
	//Button Variables
	private JButton loadStockFileButton;
	private JButton loadRulesFileButton;
	private JButton exitButton;
	private JButton startButton;
	
	//Info display variables
	private JTextArea loadStockFileDescText = new JTextArea(10,10);
	private JTextArea loadRulesFileDescText = new JTextArea(10,10);
	private JTextArea exitText = new JTextArea(10,10);
	private JTextArea startText = new JTextArea(10,10);
	
	//File display text areas
	private JTextArea stockFileDisplay = new JTextArea(10,10);
	private JTextArea rulesFileDisplay = new JTextArea(10,10);
	private JTextArea outputFileDisplay = new JTextArea(10,10);
	
	//Filename used to create output filename
	private String outputFileName = "";
	
	//Boolean conditions for start button being enabled
	private boolean stockLoaded = false;
	private boolean rulesLoaded = false;
	
	//Constants
	private final int BUTTON_WIDTH = 120;
	private final int BUTTON_HEIGHT = 30;
	
	//File Chooser
	final JFileChooser fileChooser = new JFileChooser();
	
	//Stock Handler object
	private StockHandler stockHandler;
	
	
	/*
	 * Constructor
	 */
	public UserInterface(StockHandler stockHandler)
	{
		//Initialise stock handler object
		this.stockHandler = stockHandler;
		
		//Initialise user interface
		initialiseUI();
		
		//Set the File Chooser to only show order files
		fileChooser.setFileFilter(new FileNameExtensionFilter("Stockdb/Rules Files", "stockdb", "rules"));
		fileChooser.setAcceptAllFileFilterUsed(false);
	}
	
	/*
	 * Create and initialise the user interface
	 */
	private void initialiseUI()
	{
		//Main Window
		window = new JPanel();
		window.setBorder(new EmptyBorder(5, 5, 5, 5));
		window.setLayout(null);
		setTitle("Java Tech Test");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(800, 300, 300, 250);
		setContentPane(window);
		setResizable(false);
		
		//Load stock file description box
		loadStockFileDescText.setEditable(false);
		loadStockFileDescText.setBounds(10, 10, 100, 18);
		loadStockFileDescText.setBackground(getBackground());
		loadStockFileDescText.setText("Open stock file:");
		window.add(loadStockFileDescText);
		
		//Load stock Button
		loadStockFileButton = new JButton("OPEN");
		loadStockFileButton.setBounds(10, 30, BUTTON_WIDTH, BUTTON_HEIGHT);
		window.add(loadStockFileButton);
		loadStockFileButton.addActionListener(this);
		
		//Load rule file description box
		loadRulesFileDescText.setEditable(false);
		loadRulesFileDescText.setBounds(160, 10, 100, 18);
		loadRulesFileDescText.setBackground(getBackground());
		loadRulesFileDescText.setText("Open rules file:");
		window.add(loadRulesFileDescText);
		
		//Load rules Button
		loadRulesFileButton = new JButton("OPEN");
		loadRulesFileButton.setBounds(160, 30, BUTTON_WIDTH, BUTTON_HEIGHT);
		window.add(loadRulesFileButton);
		loadRulesFileButton.addActionListener(this);
		
		//Start button text box
		startText.setEditable(false);
		startText.setBounds(10, 65, 100, 18);
		startText.setBackground(getBackground());
		startText.setText("Start:");
		window.add(startText);
		
		//Start Button
		startButton = new JButton("START");
		startButton.setBounds(10, 83, BUTTON_WIDTH, BUTTON_HEIGHT);
		window.add(startButton);
		startButton.addActionListener(this);
		startButton.setEnabled(false);
		
		//Exit file description box
		exitText.setEditable(false);
		exitText.setBounds(160, 65, 100, 18);
		exitText.setBackground(getBackground());
		exitText.setText("Close Application:");
		window.add(exitText);
		
		//Exit Button
		exitButton = new JButton("EXIT");
		exitButton.setBounds(160, 83, BUTTON_WIDTH, BUTTON_HEIGHT);
		window.add(exitButton);
		exitButton.addActionListener(this);
		
		//Stock file text box
		stockFileDisplay.setEditable(false);
		stockFileDisplay.setBounds(10, 128, 200, 18);
		stockFileDisplay.setBackground(getBackground());
		stockFileDisplay.setText("Stock File: ");
		window.add(stockFileDisplay);
		
		//Rules file text box
		rulesFileDisplay.setEditable(false);
		rulesFileDisplay.setBounds(10, 156, 200, 18);
		rulesFileDisplay.setBackground(getBackground());
		rulesFileDisplay.setText("Rules File: ");
		window.add(rulesFileDisplay);
		
		//Output file text box
		outputFileDisplay.setEditable(false);
		outputFileDisplay.setBounds(10, 184, 300, 18);
		outputFileDisplay.setBackground(getBackground());
		outputFileDisplay.setText("Output File: ");
		window.add(outputFileDisplay);
		
		//Make UI visible
	  	setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		//Source is the Exit button
		if(e.getSource() == exitButton)
		{
			//Close Application
			System.exit(0);
		}
				
		//Source is the loadStockFileButton Button
		if(e.getSource() == loadStockFileButton)
		{
			//Get file from user through File Chooser
			//Open load dialog box
			int returnVal = fileChooser.showOpenDialog(this);
			
			//If user chooses to load a file and didn't cancel
			if(returnVal == JFileChooser.APPROVE_OPTION)
			{
				int count = 0;
				
				//Get file
				File file = fileChooser.getSelectedFile();	
				
				//Get count of number of '.' in filename
				for(int i = 0; i < file.getName().length(); i++)
				{
					if(file.getName().charAt(i) == '.')
					{
						count++;
					}
				}
				
				//Check that there is only one extension in the filename to avoid false positives
				if(count == 1)
				{
					//Check file extension
					if(fileChooser.getSelectedFile().getName().contains(".stockdb"))
					{
						System.out.println("File to load: " + file.getName());
						
						//Open file and extract contents
						stockHandler.loadStockFile(file);
						
						//Enable start button if both boolean conditions true
						stockLoaded = true;
						if(rulesLoaded)
						{
							startButton.setEnabled(true);
						}
						
						//Display filename on screen
						stockFileDisplay.setText("Stock File: " + file.getName());
						
						//Store filename for use as output name in display
						outputFileName = file.getName().replaceAll(".stockdb", "_OUTPUT.result");
					}
					else
					{
						JOptionPane.showMessageDialog(window, "File to load doesn't have the correct extension ('.stockdb')", "File Load Error: Invalid File Type", JOptionPane.ERROR_MESSAGE);
					}
				}
				else
				{
					JOptionPane.showMessageDialog(window, "File to load has an invalid name. Ensure that there is only one '.' in the name.", "File Load Error: Invalid File Name", JOptionPane.ERROR_MESSAGE);
				}
			}
		} //end of openButton action IF
		
		//Source is the loadRulesFileButton Button
		if(e.getSource() == loadRulesFileButton)
		{
			//Get file from user through File Chooser
			//Open load dialog box
			int returnVal = fileChooser.showOpenDialog(this);
			
			//If user chooses to load a file and didn't cancel
			if(returnVal == JFileChooser.APPROVE_OPTION)
			{
				int count = 0;
				
				//Get file
				File file = fileChooser.getSelectedFile();	
				
				//Get count of number of '.' in filename
				for(int i = 0; i < file.getName().length(); i++)
				{
					if(file.getName().charAt(i) == '.')
					{
						count++;
					}
				}
				
				//Check that there is only one extension in the filename to avoid false positives
				if(count == 1)
				{
					//Check file extension
					if(fileChooser.getSelectedFile().getName().contains(".rules"))
					{
						System.out.println("File to load: " + file.getName());
						
						//Open file and extract contents
						stockHandler.loadRulesFile(file);
						
						//Enable start button if both boolean conditions true
						rulesLoaded = true;
						if(stockLoaded)
						{
							startButton.setEnabled(true);
						}
						
						//Display filename on screen
						rulesFileDisplay.setText("Rules File: " + file.getName());
					}
					else
					{
						JOptionPane.showMessageDialog(window, "File to load doesn't have the correct extension ('.rules')", "File Load Error: Invalid File Type", JOptionPane.ERROR_MESSAGE);
					}
				}
				else
				{
					JOptionPane.showMessageDialog(window, "File to load has an invalid name. Ensure that there is only one '.' in the name.", "File Load Error: Invalid File Name", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		
		//Source is the Start Button
		if(e.getSource() == startButton)
		{
			//Start processing of the products and rules
			stockHandler.processInput();
			
			//Display output filename on screen
			outputFileDisplay.setText("Output File: " + outputFileName);
		}
	} //end of method
}
