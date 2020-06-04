import java.util.*;
import java.util.Random;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.Button;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Arrays;

public class tileGame
{
	public static void main( String[] args )
	{
		ButtonArrayFrame frame = new ButtonArrayFrame();
		frame.setTitle("Tile Game");
		frame.setVisible(true);
	}
}

class ButtonArrayFrame extends Frame implements ActionListener
{
	public ButtonArrayFrame()
	{
		final int DEFAULT_FRAME_WIDTH = 300;
		final int DEFAULT_FRAME_HEIGHT = 300;
		setSize(DEFAULT_FRAME_WIDTH, DEFAULT_FRAME_HEIGHT);
		WindowCloser listener = new WindowCloser();
		addWindowListener(listener);
		
		// Panel 1:
		panel1 = new Panel();
		// Make an x=4, y=4 grid:
		panel1.setLayout(new GridLayout( 4, 4 ));
		// Create a button array of length 16:
		button = new Button[16];
		// Iterate over all the empty buttons (from i=0 to i=15):
		for ( int i = 0; i < 16; i++)
		{
			// Put a button in each cell:
			button[i] = new Button();
			// Take the index we are on, add one to this, put result in cell (i.e. 1, 2, 3, ... , 15):
			button[i].setLabel( Integer.toString( i + 1) );
			// Allow this button to be clicked:
			button[i].addActionListener( this );
			panel1.add(button[i]);
			
			
		}
		
		//Make the last button blank:
		button[15].setLabel( "" );
		
		// Save index of blank button to index:
		index = 15;
		
		//Make a button array that represents the completed game we can use to see if the game is complete:
		buttonComplete = new Button[16];
		for (int i = 0; i < 16; i++)
		{
			buttonComplete[i] = new Button();
			buttonComplete[i].setLabel( Integer.toString( i + 1) );
		}
		buttonComplete[15].setLabel( "" );
		
		// Panel 2:
		panel2 = new Panel();
		panel2.setLayout(new GridLayout( 1, 4));
		message = new TextField();
		panel2.add( message );
		shuffle = new Button ("Shuffle");
		shuffle.addActionListener( this );
		panel2.add( shuffle );
		
		
		
		//combine the panels:
		setLayout( new GridLayout( 5, 4) );
		add( panel1 ); 
		add( panel2 );
		
	}
	
	// Write a function that is used when some button is pressed:
	public void actionPerformed( ActionEvent e )
	{	
		// A button is pressed so we run a for that traverses the button array:
		for ( int i = 0; i < 16; i++ )
		{
			// as we traverse, if we are at the same button as that pressed:
			if ( e.getSource() == button[i] )
			{
				// and if the button that was pressed was not the blank button i.e. a button with a number in it:
				if ( i != index)
				{
					
					// create int called xBlank that is the x coordinate of blank button in grid
					int xBlank = index % 4;
					// create int called yBlank which is the y coordinate of blank button
					int yBlank = index/4;
					
					//if button pressed is to left, right, above or below:
					if ( ( i % 4 == xBlank - 1 && i / 4 == yBlank) || (i % 4 == xBlank + 1 && i / 4 == yBlank) || (i % 4 == xBlank && i/4 == yBlank - 1) || (i % 4 == xBlank && i/4 == yBlank + 1) )
					{
						
						// and if it does have a blank button next to it we move the number to where the blank was:
						button[index].setLabel( button[ i ].getLabel());
						// and make the old number button blank:
						button[ i ].setLabel( "" );
						// and make sure that index still refers to index of blank button:
						index = i;
						
						// Test to see if the game is complete:
						if ( arrayCompare(button, buttonComplete) )
							message.setText("You win!");
						else
							message.setText("");
					}
					
					else
					{
						message.setText("Bad Move");	
					}
					
					
				}
				
				else
				{
					message.setText("You must not press the blank tile.");	
				}
				
				
			}
		}
		
		// We implement an action for the case of the shuffle button having been pressed:
		if ( e.getSource() == shuffle )
		{
			
			for ( int i = 0; i <= 300; i++)
			{
				// Pick a valid button:
				int chosenButton = randomButton(validPresses(index));
				
				//Below is code for rearranging the grid after a button press as copied from line 97:
				button[index].setLabel( button[ chosenButton ].getLabel());
				button[ chosenButton ].setLabel( "" );
				index = chosenButton;
				
			}
			// and clear the text area:
			message.setText( "" );

			
		}
		
		
	
	}
	
	// The rest of this file is just functions that I used in code above.
	
	// Function that takes our array of the indexes of the buttons that surround the blank button
	// and returns one of those indexes:
	public int randomButton(int [] intArray)
		{
		
		Random x = new Random();
		int rand = x.nextInt(4);
		
		if (intArray[rand] != -1)
			return intArray[rand];
		else
			return randomButton(intArray);
		}
	
	// We define a function that takes in the location of the blank button (index) and returns
	// the buttons surrounding this that can be pressed:
	
	public int[] validPresses(int index)
	{
		
		// Create an array that stores the indexes of buttons that can be pressed at a particular moment
		// of the form {leftindex, rightindex, aboveindex, belowindex} where -1 is used to flag
		// the fact that there is no button that can be pressed at that location:
		
		
		int [] pressIndexes = new int [4];
		int xBlank = index % 4;
		int yBlank = index/4;

		
		// if blank tile has a number tile to left of it, save that number tiles index (otherwise save as -1):
		if (xBlank > 0)
			pressIndexes[0] = index - 1;
		else
			pressIndexes[0] = -1;
		
		// if blank tile has a number tile to right of it, do the same:
		if (xBlank < 3)
			pressIndexes[1] = index + 1;
		else
			pressIndexes[1] = -1;
		
		// if blank tile has a number tile above it, do the same:
		if(yBlank > 0)
			pressIndexes[2] = index - 4;
		else
			pressIndexes[2] = -1;
		
		// if blank tile has a number below it, do the same:
		if(yBlank < 3)
			pressIndexes[3] = index + 4;
		else
			pressIndexes[3] = -1;
		
		return pressIndexes;
	}
	
	public boolean arrayCompare(Button[] button1, Button[] button2)
	{
		if (button1.length != button2.length)
			return false;
		
		else
		{
			for (int i = 0; i < button1.length; i++)
			{
				if (button1[i].getLabel().equals(button2[i].getLabel()) == false)
					return false;
			}
			
			return true;
		}
	}
	
	private class WindowCloser extends WindowAdapter
	{
		public void windowClosing( WindowEvent event)
		{
			System.exit( 0 );
		}
	}
	
	private TextField message;
	private Button[] button, buttonComplete;
	private Button shuffle;
	private int index;
	private Panel panel1, panel2;
}