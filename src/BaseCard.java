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

import java.io.File;

/**
 * This class serves as a model for a playing card in CABO. Every card has the following attributes:
 * rank, suit, and face-up status. This class also has methods that helps render the card in a
 * Processing environment
 */
public class BaseCard {
  protected static processing.core.PApplet processing; // PApplet environment; draws card into
                                                       // window
  private static processing.core.PImage cardBack; // image of back of card
  private processing.core.PImage cardImage; // image of front of card

  protected boolean faceUp; // stores whether or not the card is face-up
  protected int rank; // stores the rank of the card (Ace = 1, King = 13)
  protected String suit; // stores the suit of the card

  private final int HEIGHT = 70; // height of the card
  private final int WIDTH = 50; // width of the card
  private int x; // x coordinate for drawing the card
  private int y; // y coordinate for drawing the card

  /**
   * Constructs a new BaseCard with given rank and suit. The card is set to be face-down by default.
   * ASSUME rank and suit are valid. This constructor also initializes cardImage and cardBack if it
   * has not already been down by a subclass.
   * 
   * @param rank the rank of the card
   * @param suit the suit of the card
   * @throws IllegalStateException if the Processing environment was not set before creating card
   */
  public BaseCard(int rank, String suit) {
    if (BaseCard.processing == null) {
      throw new IllegalStateException(
          "Processing environment is not set before ceating the button");
    }
    this.rank = rank;
    this.suit = suit;
    this.faceUp = false;
    this.cardImage = processing
        .loadImage("cabo_images" + File.separator + rank + "_of_" + suit.toLowerCase() + ".png");
    cardBack = processing.loadImage("cabo_images" + File.separator + "back.png");
  }

  /**
   * Sets the Processing environment to draw and interact with cards. MUST be called before
   * instantiating BaseCard objects.
   * 
   * @param processing the Processing PApplet environment
   */
  public static void setProcessing(processing.core.PApplet processing) {
    BaseCard.processing = processing;
  }

  /**
   * Returns the rank of the card (-1 if the card is the King of Diamonds)
   * 
   * @return the rank of the card
   */
  public int getRank() {
    if (this.rank == 13 && this.suit.equals("Diamonds"))
      return -1;
    return this.rank;
  }

  /**
   * Changes the faceUp status of the card
   * 
   * @param faceUp the boolean value to set this.faceUp
   */
  public void setFaceUp(boolean faceUp) {
    this.faceUp = faceUp;
  }

  /**
   * Creates a string representation of the card, which shows in the suit and rank
   * 
   * @return a string in the format "(suit) (rank)"
   */
  @Override
  public String toString() {
    return this.suit + " " + this.rank;
  }

  /**
   * Draws the card on the PApplet at the given coordinates Must first draw white rectangle for
   * card's image to be placed upon.
   * 
   * @param xPosition the x-coordinate to draw the card
   * @param yPosition the y-coordinate to draw the card
   */
  public void draw(int xPosition, int yPosition) {
    this.x = xPosition;
    this.y = yPosition;
    processing.fill(255);
    processing.rect(this.x, this.y, WIDTH, HEIGHT);
    if (faceUp == true) { // draw facing up
      processing.image(cardImage, this.x, this.y, WIDTH, HEIGHT);
    } else { // draw facing down
      processing.image(cardBack, this.x, this.y, WIDTH, HEIGHT);
    }
  }

  /**
   * Determines if the user's mouse is over card.
   * 
   * @return true if the mouse is over the card's position, false otherwise
   */
  public boolean isMouseOver() {
    if ((processing.mouseX >= this.x && processing.mouseX <= this.x + WIDTH)
        && (processing.mouseY >= this.y && processing.mouseY <= this.y + HEIGHT)) {
      return true;
    }
    return false;
  }

}
