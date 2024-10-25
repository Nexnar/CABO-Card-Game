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
 * The Player class models a CABO player who can be either human or AI.
 */
public class Player {
  private Hand hand; // the hand of this player
  private boolean isComputer; // indicator of whether this is a human or a computer
  private int label; // the player's label for running the game (0-3)
  private String name; // the name of the Player

  /**
   * Constructor that creates a new Player object with provided values and initializes the hand
   * 
   * @param name       the name of the player
   * @param label      the new player's label, assumed to be 0-3
   * @param isComputer true if this is a computer, false otherwise
   */
  public Player(String name, int label, boolean isComputer) {
    this.name = name;
    this.label = label;
    this.isComputer = isComputer;
    this.hand = new Hand();
  }

  /**
   * Accesses the name of this player
   * 
   * @return the name of the player
   */
  public String getName() {
    return this.name;
  }

  /**
   * Accesses the label of this player
   * 
   * @return the label of the player
   */
  public int getLabel() {
    return this.label;
  }

  /**
   * Accesses a shallow-copy reference of this player's hand
   * 
   * @return a reference to the hand
   */
  public Hand getHand() {
    return this.hand;
  }

  /**
   * Returns whether this is a computer or human
   * 
   * @return true if this is a computer, false otherwise
   */
  public boolean isComputer() {
    return this.isComputer;
  }

  /**
   * adds a card to the player's hand
   * 
   * @param card the card to be added
   */
  public void addCardToHand(BaseCard card) {
    this.hand.addCard(card);
  }

}
