/**
 * SceneOptionsView.java
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

public class SceneOptionsView implements PropertyChangeListener, ActionListener
{
   //***
   // class variables
   //***

   private static final boolean TRACE = false;
   
   private static final String TAB_TITLE = "Scene";
   private static final String TAB_TOOLTIP = "Scene size and output options";
   private static final int DEFAULT_SCENE_LENGTH = 1000;
   private static final String DEFAULT_FILENAME_PREFIX = "data.";
   
   //***
   // instance variables
   //***

   // parent panel for all elements
   private JPanel theTabbedPanePanel;

   // elements for 'number of squares'
   private NumberFormat theSceneLengthFormat;
   private JPanel theSceneLengthPanel;
   private JLabel theSceneLengthLabel;
   private JFormattedTextField theSceneLengthField;

   // elements for 'max side length'
   private JPanel theFilenamePrefixPanel;
   private JLabel theFilenamePrefixLabel;
   private JTextField theFilenamePrefixField;

   // property values
   private int theSceneLength;
   private String theFilenamePrefix;

   /**
    * SceneOptionsView
    *
    * This class implements the view for the SceneOptions class and
    * handles related user interface events
    */
   SceneOptionsView()
   {
      theSceneLength = DEFAULT_SCENE_LENGTH;
      theFilenamePrefix = DEFAULT_FILENAME_PREFIX;
   }

   /**
    * setSceneLength
    * 
    * This method sets the current number of squares
    */
   public void setSceneLength(int aCount)
   {
      theSceneLength = aCount;
      if (theSceneLengthField != null)
      {
         theSceneLengthField.setValue(theSceneLength);
         theSceneLengthField.updateUI();
      }
   }

   /**
    * setFilenamePrefix
    * 
    * This method sets the current maximum side length
    */
   public void setFilenamePrefix(String aPrefix)
   {
      theFilenamePrefix = aPrefix;
      if (theFilenamePrefixField != null)
      {
         theFilenamePrefixField.setText(theFilenamePrefix);
         theFilenamePrefixField.updateUI();
      }
   }

   /**
    * getSceneLength
    * 
    * This method returns the current number of scene length
    */
   public int getSceneLength()
   {
      return theSceneLength;
   }

   /**
    * getFilenamePrefix
    * 
    * This method returns the current filename prefix
    */
   public String getFilenamePrefix()
   {
      return theFilenamePrefix;
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
      // length of (NxN) scene
      //***

      // build format arguments
      theSceneLengthFormat = NumberFormat.getIntegerInstance();

      // create scene length elements [label, field]
      theSceneLengthLabel = new JLabel("Length of (NxN) scene:");
      theSceneLengthLabel.setHorizontalAlignment(JLabel.LEFT);

      theSceneLengthField = new JFormattedTextField(theSceneLengthFormat);
      theSceneLengthField.setValue(new Double(theSceneLength));
      theSceneLengthField.setColumns(10);
      theSceneLengthField.addPropertyChangeListener("value", this);

      // add to containing panel
      theSceneLengthPanel = new JPanel();
      theSceneLengthPanel.add(theSceneLengthLabel);
      theSceneLengthPanel.add(theSceneLengthField);

      //***
      // Filename prefix for output files
      //***

      // create filename prefix elements [label, field]
      theFilenamePrefixLabel = new JLabel("Prefix for generated data files:");
      theFilenamePrefixLabel.setHorizontalAlignment(JLabel.LEFT);

      theFilenamePrefixField = new JTextField();
      theFilenamePrefixField.setText(theFilenamePrefix);
      theFilenamePrefixField.setColumns(10);
      theFilenamePrefixField.setActionCommand("prefixChanged");
      theFilenamePrefixField.addActionListener(this);

      // add to containing panel
      theFilenamePrefixPanel = new JPanel();
      theFilenamePrefixPanel.add(theFilenamePrefixLabel);
      theFilenamePrefixPanel.add(theFilenamePrefixField);

      //***
      // update tabbed pane
      //***

      // build tab
      theTabbedPanePanel = new JPanel();
      theTabbedPanePanel.setLayout(new BoxLayout(theTabbedPanePanel, BoxLayout.PAGE_AXIS));
      theTabbedPanePanel.add(theSceneLengthPanel);
      theTabbedPanePanel.add(theFilenamePrefixPanel);

      // add new tab to tabbed pane
      aTabbedPane.addTab(TAB_TITLE, null, theTabbedPanePanel, TAB_TOOLTIP);
   }

   /** 
    * propertyChange
    * 
    * Called when a formatted text field's "value" property changes
    */
   public void propertyChange(PropertyChangeEvent e)
   {
      Object source = e.getSource();
      if (source == theSceneLengthField)
      {
         theSceneLength = ((Number)theSceneLengthField.getValue()).intValue();
         if (TRACE)
            System.out.println("Scene: scene length = " + theSceneLength);
      }
   }

   /** 
    * actionPerformed
    * 
    * Called when a text field's "value" changes
    */
   public void actionPerformed(ActionEvent ae)
   {
      if (TRACE)
         System.out.println("Command: " + ae.getActionCommand());
      if (ae.getActionCommand().equalsIgnoreCase("prefixChanged"))
         captureFilenamePrefix();
   }

   /**
    * captureFilenamePrefix
    * 
    * This method captures the currentent filename prefix
    */
   public void captureFilenamePrefix()
   {
      theFilenamePrefix = theFilenamePrefixField.getText();
      System.out.println("Scene: filename prefix = " + theFilenamePrefix);
   }
}