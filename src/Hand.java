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
import java.util.ArrayList;

/**
 * The Hand class represents a mini-deck that each player holds
 */
public class Hand extends Deck {
  private final int HAND_SIZE = 4; // max number of cards in a hand

  /**
   * Constructor that creates a new empty deck for this hand
   */
  public Hand() {
    super(new ArrayList<BaseCard>());
  }

  /**
   * Overrides the Deck's addCard method to prevent the player from being dealt more than cards that
   * allowed by HAND_SIZE
   * 
   * @param card the card to be added
   * @throws IllegalStateException if the player already has max cards in hand
   */
  @Override
  public void addCard(BaseCard card) {
    if (this.cardList.size() == 4) {
      throw new IllegalStateException("Player has max number of cards");
    }
    this.cardList.add(card);
  }

  /**
   * Replaces the card at the given index (ASSUME it is between 0 and HAND_SIZE - 1) with the given
   * card, returns the card that was previously there
   * 
   * @param newCard the card to swap in
   * @param index   the index to swap
   * @return the previous card
   */
  public BaseCard swap(BaseCard newCard, int index) {
    BaseCard prevCard = this.cardList.get(index);
    this.cardList.set(index, newCard);
    return prevCard;
  }

  /**
   * Switches a card in this hand with a card in another hand
   * 
   * @param myIndex    the index of the card in this hand to switch
   * @param otherHand  the other hand to switch cards with
   * @param otherIndex the index of the card in the other hand to switch
   */
  public void switchCards(int myIndex, Hand otherHand, int otherIndex) {
    // stores the card in the other persons hand
    BaseCard otherCard = otherHand.cardList.get(otherIndex);
    otherHand.cardList.set(otherIndex, this.cardList.get(myIndex));

    this.cardList.set(myIndex, otherCard);
  }

  /**
   * Changes the face-up value of the card at the given index to the desired value
   * 
   * @param index  the index of the card to change
   * @param faceUp true if the card should be face-up, false otherwise
   */
  public void setFaceUp(int index, boolean faceUp) {
    this.cardList.get(index).faceUp = faceUp;
  }

  /**
   * Draws the entire hand at the given y-coordinate. Can calculate the x-coordinate using 50 +
   * 60*index
   * 
   * @param y the y-coordinate of all of the cards
   */
  public void draw(int y) {
    for (int i = 0; i < this.cardList.size(); i++) {
      this.cardList.get(i).draw(50 + 60 * i, y);
    }
  }

  /**
   * Determines if the mouse is over any of the cards in this hand, and returns the index of any
   * card which the mouse is over, or -1 if the mouse is not over any card in this hand
   * 
   * @return the index of the card that the mouse is over, otherwise -1
   */
  public int indexOfMouseOver() {
    for (int i = 0; i < this.cardList.size(); i++) {
      if (this.cardList.get(i).isMouseOver()) {
        return i;
      }
    }
    return -1;
  }

  /**
   * Returns the rank of the card at the given index
   * 
   * @param index the index of the card to access
   * @return the rank of the card at the index
   */
  public int getRankAtIndex(int index) {
    return this.cardList.get(index).getRank();
  }

  /**
   * Calculates the total value of the hand, based on the sum of the ranks
   * 
   * @return the total value of this player's hand
   */
  public int calcHand() {
    int value = 0;
    for (BaseCard card : this.cardList) {
      value += card.getRank();
    }
    return value;
  }


}
