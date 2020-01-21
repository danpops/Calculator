import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import java.text.DecimalFormat;

/**
 * 
 * This is a basic calculator program. Everytime a new operator
 * is selected, new digit/value appears on next line. 
 * This calculator allows chain calculations as well.
 * Calculator stops user from entering operator twice and 
 * entering double digits.
 *
 */
public class Calculator extends JFrame
{
	private JTextArea display;
	private JScrollPane scroll;

	// formats all numbers to 2 decimal places
	DecimalFormat fmt = new DecimalFormat("0.##");
	
	private String allLines = "";
	private String currentNum = "";
	private String prevOperator = "";
	private String currentOp = "";

	private double firstVal;
	private double secondVal;
	private double answerVal;
	
	private boolean clearCur = false;
	private boolean clearLastOp = false;
	private boolean doubleDecimal = false;
	private boolean equalsFlag = false;
	private boolean firstOperatorFlag = false;
	private boolean operatorTwice = false;
	private boolean opTurn = false;
	private boolean startNextValue = false;
	
	// sets frame width and height
	private static final int FRAME_WIDTH = 266;
	private static final int FRAME_HEIGHT = 384;
	
	/**
	 * connects clear buttons, button panel and 
	 * display panel to one. 
	 */
	public Calculator()
	{
		// creates the clear panel
		createClearPanel();
		// creates the display
		createDisplay();
		// creates the button panels 
		createButtonPanel();
		
		// sets frame to open wherever on screen
		setLocationRelativeTo(null);
		// doesn't allow user to change frame size
		setResizable(false);
		// changes frame size 
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
	}
	
	/**
	 * Method to create Clear panel
	 */
	public void createClearPanel()
	{
		JPanel clearPanel = new JPanel();
		// initilizes button layout 
		clearPanel.setLayout(new GridLayout(1, 2));
		// initilizes clear button
		clearPanel.add(makeClearButton("Clear Last"));
		clearPanel.add(makeClearButton("Clear All"));
		
		// adds clear panel to top of frame
		add(clearPanel, BorderLayout.NORTH);
	}
	
	/**
	 * Method to create display
	 */
	public void createDisplay()
	{
		// initializes text area for digits
		display = new JTextArea();
		
		// sets text font
		display.setFont(new Font("SansSerif", Font.PLAIN, 18));
		// creates a scroll panel
		scroll = new JScrollPane(display);
		// display not editable by user
		display.setEditable(false); 
		// display is visible 
		display.setVisible(true);
		// sets boundary for text to stay in one display
		display.setLineWrap(true);
		
		// initializes display to empty text
		display.setText("");
	
		// initializes scrollbar to always appear
		scroll.setVerticalScrollBarPolicy(
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	
		// sets display to middle of frame
		add(scroll, BorderLayout.CENTER);
	}
	
	/**
	 * Method to create button panel
	 */
	public void createButtonPanel()
	{
		JPanel buttonPanel = new JPanel();
		
		// initilizes button layout 
		buttonPanel.setLayout(new GridLayout(4, 4));
		// initilizes digit/operator buttons
		buttonPanel.add(makeDigitButton("7"));
		buttonPanel.add(makeDigitButton("8"));
		buttonPanel.add(makeDigitButton("9"));
		buttonPanel.add(makeOperatorButton("+"));
		buttonPanel.add(makeDigitButton("4"));
		buttonPanel.add(makeDigitButton("5"));
		buttonPanel.add(makeDigitButton("6"));
		buttonPanel.add(makeOperatorButton("-"));
		buttonPanel.add(makeDigitButton("1"));
		buttonPanel.add(makeDigitButton("2"));
		buttonPanel.add(makeDigitButton("3"));
		buttonPanel.add(makeOperatorButton("*"));
		buttonPanel.add(makeDigitButton("0"));
		buttonPanel.add(makeDigitButton("."));
		buttonPanel.add(makeOperatorButton("="));
		buttonPanel.add(makeOperatorButton("/"));
		
		// adds button panel to bottom of frame
		add(buttonPanel, BorderLayout.SOUTH);
	}
	
	/**
	 * Method to calculate two values entered by user
	 * @param value1 the first value of calculation
	 * @param value2 the second value of calculation
	 * @param op the operator to calculate value1 and value2
	 * @return returns result after calculation
	 */
	public double calculate(double value1, double value2, String op)
	{  		
		// addition
		if (op.equals("+"))
		{
			return value1 + value2;
		}
		// subtraction
		else if (op.equals("-"))
		{
			return value1 - value2;
		}
		// multiplication
		else if (op.equals("*"))
		{
			return value1 * value2;
		}
		// division
		else if (op.equals("/")) 
		{
			return value1 / value2;
		}
		// equals
		else 
		{
			return value2;
		}
	}
	
	/**
	 * Listener for when a clear button is selected
	 */
	class ClearButtonListener implements ActionListener
	{
		private String clear;
		
		public ClearButtonListener(String aClear)
		{
			clear = aClear;
		}
		
		public void actionPerformed(ActionEvent event)
		{
			// clear all will reset all calculations providing a 
			// blank new screen
			if (clear.equals("Clear All"))
			{
				// all variables reset
				allLines = "";
				currentNum = "";
				prevOperator = "";
				currentOp = "";
				doubleDecimal = false;
				operatorTwice = false;
				opTurn = false;
				clearLastOp = false;
				firstOperatorFlag = false;
				equalsFlag = false;
				startNextValue = false;
				clearCur = false;
				
				display.setText("");
			}
			else if (clear.equals("Clear Last"))
			{	
				// clear last will work if number is not a result
				if (!prevOperator.equals("="))
				{
					// clear last to reset operator that was just entered
					if (opTurn)
					{
						clearLastOp = true;
						operatorTwice = false;
						opTurn = true;
						firstOperatorFlag = true;
						equalsFlag = true;
						clearCur = false;
						
						// creates new string to store current text displayed
						String currentLines = allLines;
						// keeps string stored minus last operator selected
						String allLinesMinus = 
								currentLines.substring(0,allLines.lastIndexOf(currentOp));
						
						// turns text displayed to original text minus operator
						allLines = allLinesMinus;
						
						// displays new text
						display.setText(allLines);
						opTurn = false;
					}
					// clear last to remove current string of digits
					else
					{
						// calculates text area minus last character
						int lineIndex = allLines.length() - 1;
						// calculates currentNum minus last digit
						int numIndex = currentNum.length() - 1;
						
 						// stores new allLines in String
						String allLinesMinus = allLines.substring(0, lineIndex);
						// stores new currentNum in String
						String currentNumMinus = currentNum.substring(0, numIndex);
						
						// allLines updated with new value
						allLines = allLinesMinus;
						// currentNum updated with new value
						currentNum = currentNumMinus;
						
						// displays new text
						display.setText(allLines);
						
						doubleDecimal = false;
					}
				}
				// clear last if new digit value entered during a new
				// operation
				else if (clearCur)
				{
					// calculates text area minus last character
					int lineIndex = allLines.length() - 1;
					// calculates currentNum minus last digit
					int numIndex = currentNum.length() - 1;
					
					// stores new allLines in String
					String allLinesMinus = allLines.substring(0, lineIndex);
					// stores new currentNum in String
					String currentNumMinus = currentNum.substring(0, numIndex);
					
					// allLines updated with new value
					allLines = allLinesMinus;
					// currentNum updated with new value
					currentNum = currentNumMinus;
					
					// displays new text
					display.setText(allLines);
					
					doubleDecimal = false;
				}	
			}
		}
	}
	
	/**
	 * Listener for when a digit button is selected
	 */
	class DigitButtonListener implements ActionListener
	{
		private String digit;

		public DigitButtonListener(String aDigit)
		{
			digit = aDigit;
		}
	      
		public void actionPerformed(ActionEvent event)
		{
			// notifies program that a digit 
			// has been selected
			opTurn = false;
			// notifies program that operator
			   // hasn't been selected twice
			operatorTwice = false;
			
			// if user enters 2+ decimals in a number program will give
			// error message and continue number before
			if (doubleDecimal && digit.equals("."))
			{
				doubleDecimal = false;

				allLines += "\n\nERROR:\nONE DECIMAL\n"
						+ "ALLOWED\n\n" + currentNum;
				display.setText(allLines);
				
				throw new IllegalArgumentException("ERROR: DECIMAL "
						+ " ONLY ALLOWED ONCE");
			}
			
			// if new digit entered and it is new calculation
			// sets digit to new line
			if (startNextValue && prevOperator.equals("=")) 
			{
				allLines += "\n";
				clearCur = true;
				startNextValue = false;
			}
			
			// user pressed "=" then user starts new calculation
			if (equalsFlag)
			{
				// start a new line
				allLines += "\n";
				// set current number to empty
				currentNum = "";
				clearCur = true;
				equalsFlag = false;
			}
			
			// keeps track of currentNum to ensure double
			// decimals are not entered in same value
			if (digit.equals("."))
			{
				doubleDecimal = true;
			}
			
			// adds current digit to allLines
			allLines += digit;
			// stores current digit to currentNum
			currentNum += digit;
			// display text on screen			
			display.setText(allLines);
		}        
	}
	
	/**
	 * Listener for when an operator button is selected
	 */
	class OperatorButtonListener implements ActionListener
	{
		private String operator;
		
		public OperatorButtonListener(String anOperator)
		{
			operator = anOperator;
		}

		public void actionPerformed(ActionEvent event)
		{		
			opTurn = true;
			currentOp = operator;
			doubleDecimal = false;
			
			// notifies the program that operator 
			// has been cleared and inserts new operator 
			// for calculation
			if (clearLastOp)
			{
				prevOperator = operator;
				allLines += prevOperator;
				display.setText(allLines);
				
				operatorTwice = true;
			}
			
			// if user divides number by 0 error message will appear
			if (currentNum.equals("0") && prevOperator.equals("/"))
			{
				allLines += "\n\nERROR:\nCANNOT DIVIDE\nBY 0\n\n" + fmt.format(firstVal)
				+ prevOperator + "\n";

				display.setText(allLines);
				
				throw new IllegalArgumentException("ERROR: CANNOT DIVIDE 0 BY 0");
			}
			// first operator has NOT been set 
			// OR a new calculation has started
			// OR operator was cleared
			if (!firstOperatorFlag || clearLastOp)
			{
				firstOperatorFlag = true;
				clearLastOp = false;
				
				// get first value from display and set it to firstVal
				firstVal = Double.parseDouble(currentNum);
				// set first operator to prevOperator
				prevOperator = operator;
				// add operator to string start a new line
				allLines += operator + "\n";
							
				currentNum = ""; 
							
				equalsFlag = false;
			} 
			else
			{
				// if user enters operator consecutively program will show error
				// and continue operation
				if (operatorTwice)
				{
					allLines += "\nERROR:\nOPERATOR " + 
							operator + " HAS\nBEEN IGNORED\n\n" + fmt.format(firstVal)
							+ prevOperator + "\n";
					display.setText(allLines);
					
					throw new IllegalArgumentException("ERROR: OPERATOR " +
							operator + " HAS BEEN IGNORED.");
				}
				// second operator is '=' 
				else if (operator.equals("="))
				{
					firstOperatorFlag = false;
					//get second value from display and set it to secondVal
					secondVal = Double.parseDouble(currentNum);
					// add operator to string and start new line
					allLines += operator + "\n";
					
					// calculate answer
					answerVal = calculate(firstVal, secondVal, prevOperator);
					// add answer to string
					allLines += fmt.format(answerVal);
								
					// in case user wants to continue with this calculation,
					// currentNum is now set to answerVal
					currentNum = Double.toString(answerVal);
							
					prevOperator = operator;
					equalsFlag = true;	
				}
				// second operator NOT '='
				else
				{
					// get second value from display and set it to secondVal
					secondVal = Double.parseDouble(currentNum);
					// add '=' sign to string
					allLines += "=\n";
					
					// calculate answer to first equation
					// (ie. 1+1+1 will show:
					//		1+
					//		1=
					//		2+
					//		1
					//
					answerVal = calculate(firstVal, secondVal, prevOperator);
					allLines += fmt.format(answerVal) + operator + "\n";
						
					// firstVal is now set to answerVal
					firstVal = answerVal;
					prevOperator = operator;
					// set currentNum to empty
					currentNum = "";					
				}
			}
			
			// display text on screen
			display.setText(allLines);  
			operatorTwice = true;
			clearLastOp = false;
			clearCur = false;
		}
	}
	
	/**
	 * Creates clear panel buttons and initializes listener
	 * @param clear button that is selected
	 * @return returns clear button selected
	 */
	public JButton makeClearButton(String clear)
	{	
		// create clear button
		JButton button = new JButton(clear);
		// create button size
		button.setPreferredSize(new Dimension(35, 35));
		// doesnt highlight button when clicked
		button.setFocusPainted(false);
		// connect clear button to listener
		ActionListener listener = new ClearButtonListener(clear);
		// add listener to clear button
		button.addActionListener(listener);  
		// return button output
		return button;    
	}
	
	/**
	 * Creates digit buttons and initializes listener
	 * @param digit button that is selected
	 * @return returns digit button selected
	 */
	public JButton makeDigitButton(String digit)
	{	
		// create digit button
		JButton button = new JButton(digit);
		// create button size
		button.setPreferredSize(new Dimension(35, 35));
		// doesnt highlight button when clicked
		button.setFocusPainted(false);
		// connect digit button to listener
		ActionListener listener = new DigitButtonListener(digit);
		// add listener to digit button
		button.addActionListener(listener);  
		// return button output
		return button;    
	}
	
	/**
	 * Creates operator button and initializes listener
	 * @param op operator button that is selected
	 * @return returns operator button selected
	 */
	public JButton makeOperatorButton(String op)
	{
		// create operator button
		JButton button = new JButton(op);
		// create button size
		button.setPreferredSize(new Dimension(35, 35));
		// highlights button when clicked
		button.setFocusPainted(true);
		// connect operator button to listener
		ActionListener listener = new OperatorButtonListener(op);
		// add listener to operator button
		button.addActionListener(listener);  
		// return button output
		return button;   
	}

	/**
	 * Method displays calculator program to user
	 */
	public static void main(String[] args)
	{  
		JFrame frame = new Calculator();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Calculator");
		frame.setVisible(true);
	}
}