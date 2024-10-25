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
 * This class represents a card that has an action. It extends the BaseCard class and adds action
 * types that affect the CABO game. Specific actions are: (7 & 8): Peek at a card in the player's
 * own hand, (9 & 10): Spy on a card in another player's and, (Jack and Queen): Swap cards with a
 * different player
 */
public class ActionCard extends BaseCard {
  private String actionType; // can be either peek, spy, or switch

  /**
   * Constructs a new Action card with given rank, suit, and actionType. ASSUME provided action type
   * is valid
   * 
   * @param rank       the rank of the card
   * @param suit       the suit of the card
   * @param actionType the type of action associated with this card ("peek", "spy", "switch")
   */
  public ActionCard(int rank, String suit, String actionType) {
    super(rank, suit);
    this.actionType = actionType.toUpperCase();
  }

  /**
   * Returns the type of action associated w/ card
   * 
   * @return the action type as a String: "peek", "spy", or "switch"
   */
  public String getActionType() {
    return this.actionType;
  }

}
