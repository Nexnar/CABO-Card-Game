//////////////// FILE HEADER (INCLUDE IN EVERY FILE) //////////////////////////
//
// Title: CABO
// Course: CS 300 Fall 2024
//
// Author: Tristin Yun
// Email: tyun7@wisc.edu
// Lecturer: Hobbes LeGault
//
//////////////////////// ASSISTANCE/HELP CITATIONS ////////////////////////////
//
// Persons: NOBODY
// Online Sources: NONE
//
///////////////////////////////////////////////////////////////////////////////

/**
 * The Button class provides a basic interactive button in the Processing environment.
 */
public class Button {
  // Data fields
  private boolean active; // true if the button is active
  private int height; // the height of the button
  private int width; // the width of the button
  private int x; // x-coordinate of the button
  private int y; // y-coordinate of the button
  private String label; // the text shown on the button
  protected static processing.core.PApplet processing; // the processing environment for drawing the
                                                       // button

  /**
   * Constructor that creates a Button with given label and coordinates. The button is INACTIVE by
   * default.
   * 
   * @param label  the text shown on the button
   * @param x      the x-coordinate of the button
   * @param y      the y-coordinate of the button
   * @param width  the width of the button
   * @param height the height of the button
   * @throws IllegalStateException if the Processing environment is not set before creating the
   *                               button
   */
  public Button(String label, int x, int y, int width, int height) {
    if (Button.processing == null) {
      throw new IllegalStateException(
          "Processing environment is not set before ceating the button");
    }
    this.label = label;
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.active = false;
  }

  /**
   * Sets the Processing environment for the Button class. MUST be called before instantiating
   * button.
   * 
   * @param processing the Processing environment for drawing and interaction
   */
  public static void setProcessing(processing.core.PApplet processing) {
    Button.processing = processing;
  }

  /**
   * Accesses the label of this button
   * 
   * @return the label of the button
   */
  public String getLabel() {
    return this.label;
  }

  /**
   * Sets the label of this button
   * 
   * @param label the new label of the button
   */
  public void setLabel(String label) {
    this.label = label;
  }

  /**
   * Returns whether the button is currently active
   * 
   * @return true if active, false otherwise
   */
  public boolean isActive() {
    return this.active;
  }

  /**
   * Changes the boolean active.
   * 
   * @param active the new active state of the button
   */
  public void setActive(boolean active) {
    this.active = active;
  }

  /**
   * Draws the button onto the Processing canvas. The button switches its color depending on whether
   * it is active and if the mouse is currently over it.
   */
  public void draw() {
    if (this.active == true) { // button is active
      if (this.isMouseOver() == true) {
        processing.fill(150); // mouse is over the button
      } else {
        processing.fill(200);
      }
    } else { // the button is inactive
      processing.fill(255, 51, 51);
    }

    // draws a rectangle at coordinates and of desired size with rounded edges
    processing.rect(this.x, this.y, this.width, this.height, 5);

    // draw text
    processing.fill(0);
    processing.textSize(14);
    processing.textAlign(processing.CENTER, processing.CENTER);
    processing.text(this.label, this.x + this.width / 2, this.y + this.height / 2);


  }

  /**
   * Determines if the mouse is touching the button.
   * 
   * @return true if the mouse is over the button, false otherwise
   */
  public boolean isMouseOver() {
    if ((processing.mouseX >= this.x && processing.mouseX <= this.x + this.width)
        && (processing.mouseY >= this.y && processing.mouseY <= this.y + this.height)) {
      return true;
    }
    return false;
  }

}
