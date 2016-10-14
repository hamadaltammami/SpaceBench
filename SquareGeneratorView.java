/**
 * SquareGeneratorView.java
 *
 * @author :  Tim Faulkner
 * @version : 12/19/2011
 */

import javax.swing.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent; 
import java.text.NumberFormat;
import javax.swing.text.*;

public class SquareGeneratorView implements PropertyChangeListener, ItemListener
{
   //***
   // class variables
   //***

   private static final boolean TRACE = false;
   
   private static final String TAB_TITLE = "Squares";
   private static final String TAB_TOOLTIP = "Square generator options";
   private static final String GENERATE_CHECKBOX_TITLE = "Generate data file?";
   private static final boolean DEFAULT_GENERATE_FLAG = true;
   private static final int DEFAULT_SQUARE_COUNT = 50;
   private static final int DEFAULT_MAX_SIDE_LENGTH = 100;
   
   //***
   // instance variables
   //***

   // parent panel for all elements
   private JPanel theTabbedPanePanel;

   // elements for 'generate' flag
   private JCheckBox theGenerateCheckbox;
   private JPanel theGeneratePanel;

   // elements for 'number of squares'
   private NumberFormat theNumberOfSquaresFormat;
   private JPanel theNumberOfSquaresPanel;
   private JLabel theNumberOfSquaresLabel;
   private JFormattedTextField theNumberOfSquaresField;

   // elements for 'max side length'
   private NumberFormat theMaximumSideLengthFormat;
   private JPanel theMaximumSideLengthPanel;
   private JLabel theMaximumSideLengthLabel;
   private JFormattedTextField theMaximumSideLengthField;

   // property values
   private boolean theGenerateFlag;
   private int theNumberOfSquares;
   private int theMaximumSideLength;

   /**
    * SquareGeneratorView
    *
    * This class implements the view for the SquareGenerator class and
    * handles related user interface events
    */
   SquareGeneratorView()
   {
      theGenerateFlag = DEFAULT_GENERATE_FLAG;
      theNumberOfSquares = DEFAULT_SQUARE_COUNT;
      theMaximumSideLength = DEFAULT_MAX_SIDE_LENGTH;
   }

   /**
    * setGenerateFlag
    *
    * This method sets the generate datfile property
    */
   public void setGenerateFlag(boolean aFlag)
   {
      if (theGenerateCheckbox != null)
      {
         if (theGenerateFlag != aFlag)
         {
            theGenerateCheckbox.doClick();
            theGenerateCheckbox.updateUI();
         }
      }
   }

   /**
    * setNumberOfSquares
    * 
    * This method sets the current number of squares
    */
   public void setNumberOfSquares(int aCount)
   {
      theNumberOfSquares = aCount;
      if (theNumberOfSquaresField != null)
      {
         theNumberOfSquaresField.setValue(theNumberOfSquares);
         theNumberOfSquaresField.updateUI();
      }
   }

   /**
    * setMaximumSideLength
    * 
    * This method sets the current maximum side length
    */
   public void setMaximumSideLength(int aLength)
   {
      theMaximumSideLength = aLength;
      if (theMaximumSideLengthField != null)
      {
         theMaximumSideLengthField.setValue(theMaximumSideLength);
         theMaximumSideLengthField.updateUI();
      }
   }

  /**
   * getGenerateFlag
   *
   * This method returns the generate datfile property
   */
   public boolean getGenerateFlag()
   {
      return theGenerateFlag;
   }

 /**
   * getNumberOfSquares
   *
   * This method returns the current number of squares
   */
   public int getNumberOfSquares()
   {
      return theNumberOfSquares;
   }

   /**
    * getMaximumSideLength
    * 
    * This method returns the current maximum side length
    */
   public int getMaximumSideLength()
   {
      return theMaximumSideLength;
   }

   /**
    * build
    * 
    * This method builds the user interface and ties in any
    * evenet listeners
    */
   public void build(JTabbedPane aTabbedPane)
   {
      //***
      // generate flag
      //***

      // create generate flag [checkbox]
      theGenerateCheckbox = new JCheckBox(GENERATE_CHECKBOX_TITLE);
      theGenerateCheckbox.setSelected(theGenerateFlag);
      theGenerateCheckbox.addItemListener(this);

      // add to containing panel
      theGeneratePanel = new JPanel();
      theGeneratePanel.add(theGenerateCheckbox);

      //***
      // number of squares
      //***

      // build format arguments
      theNumberOfSquaresFormat = NumberFormat.getIntegerInstance();

      // create number of point elements [label, field]
      theNumberOfSquaresLabel = new JLabel("Number of squares:");
      theNumberOfSquaresLabel.setHorizontalAlignment(JLabel.LEFT);
      theNumberOfSquaresField = new JFormattedTextField(theNumberOfSquaresFormat);
      theNumberOfSquaresField.setValue(new Double(theNumberOfSquares));
      theNumberOfSquaresField.setColumns(10);
      theNumberOfSquaresField.addPropertyChangeListener("value", this);

      // add to containing panel
      theNumberOfSquaresPanel = new JPanel();
      theNumberOfSquaresPanel.add(theNumberOfSquaresLabel);
      theNumberOfSquaresPanel.add(theNumberOfSquaresField);

      //***
      // maximum side length
      //***

      // build format arguments
      theMaximumSideLengthFormat = NumberFormat.getIntegerInstance();

      // create number of point elements [label, field]
      theMaximumSideLengthLabel = new JLabel("Maximum side length:");
      theMaximumSideLengthLabel.setHorizontalAlignment(JLabel.LEFT);
      theMaximumSideLengthField = new JFormattedTextField(theMaximumSideLengthFormat);
      theMaximumSideLengthField.setValue(new Double(theMaximumSideLength));
      theMaximumSideLengthField.setColumns(10);
      theMaximumSideLengthField.addPropertyChangeListener("value", this);

      // add to containing panel
      theMaximumSideLengthPanel = new JPanel();
      theMaximumSideLengthPanel.add(theMaximumSideLengthLabel);
      theMaximumSideLengthPanel.add(theMaximumSideLengthField);

      //***
      // update tabbed pane
      //***

      // build tab
      theTabbedPanePanel = new JPanel();
      theTabbedPanePanel.setLayout(new BoxLayout(theTabbedPanePanel, BoxLayout.PAGE_AXIS));
      theTabbedPanePanel.add(theGeneratePanel);
      theTabbedPanePanel.add(theNumberOfSquaresPanel);
      theTabbedPanePanel.add(theMaximumSideLengthPanel);

      // add new tab to tabbed pane
      aTabbedPane.addTab(TAB_TITLE, null, theTabbedPanePanel, TAB_TOOLTIP);
   }

   /** 
    * propertyChange
    * 
    * Called when a field's "value" property changes
    */
   public void propertyChange(PropertyChangeEvent e)
   {
      Object source = e.getSource();
      if (source == theNumberOfSquaresField)
      {
         theNumberOfSquares = ((Number)theNumberOfSquaresField.getValue()).intValue();
         if (TRACE)
            System.out.println("Squares: number of squares = " + theNumberOfSquares);
      }
      else if (source == theMaximumSideLengthField)
      {
         theMaximumSideLength = ((Number)theMaximumSideLengthField.getValue()).intValue();
         if (TRACE)
            System.out.println("Squares: maximum side length = " + theMaximumSideLength);
      }
   }
   
   /**
    * itemStateChanged
    *
    * Called when a checkbox's state changes
    */
   public void itemStateChanged(ItemEvent e)
   {
      Object source = e.getItemSelectable();
      if (source == theGenerateCheckbox)
      {
         theGenerateFlag = !theGenerateFlag;
         if (theGenerateFlag)
         {
           theNumberOfSquaresField.setEnabled(true);
           theMaximumSideLengthField.setEnabled(true);
         }
         else
         {
           theNumberOfSquaresField.setEnabled(false);
           theMaximumSideLengthField.setEnabled(false);
         }
         if (TRACE)
            System.out.println("Squares: generate = " + theGenerateFlag);
      }
   }
}