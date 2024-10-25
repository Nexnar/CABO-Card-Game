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
import java.util.Collections;

/**
 * The Deck class represents a deck of playing cards for the game Cabo. It manages a collection of
 * cards, including shuffling, drawing, and adding cards.
 */
public class Deck {

  protected static processing.core.PApplet processing; // Processing environment to draw deck
  protected ArrayList<BaseCard> cardList; // deck of cards

  /**
   * Constructs a new Deck from parameter. Pass the output of createDeck() to create a full deck
   *
   * @param starting list of cards for the deck; either full deck or empty list
   * @throws IllegalStateException if the Processing environment is not set before making deck
   */
  public Deck(ArrayList<BaseCard> deck) {
    if (Deck.processing == null) {
      throw new IllegalStateException(
          "Processing environment is not set before ceating the button");
    }
    this.cardList = deck;
  }


  /**
   * Sets up the deck with CABO cards, including action cards. Initializes the deck with all
   * necessary cards and shuffles them.
   *
   * @return the completed ArrayList of CABO cards
   */
  public static ArrayList<BaseCard> createDeck() {
    ArrayList<BaseCard> cardList = new ArrayList<>(); // list of cards in deck

    // Define the suits
    String[] suits = {"Clubs", "Diamonds", "Hearts", "Spades"};

    // Cards from 1 (Ace) to 13 (King)
    for (int rank = 1; rank <= 13; ++rank) {
      // Loop through each suit
      for (String suit : suits) {
        if (rank >= 7 && rank <= 12) {
          // Special action cards
          String actionType = "";
          if (rank == 7 || rank == 8) {
            actionType = "peek";
          } else if (rank == 9 || rank == 10) {
            actionType = "spy";
          } else {
            actionType = "switch";
          }
          cardList.add(new ActionCard(rank, suit, actionType)); // Add ActionCard to deck
        } else {
          cardList.add(new BaseCard(rank, suit)); // Add NumberCard to deck
        }
      }
    }
    Collections.shuffle(cardList);
    return cardList;
  }

  /**
   * Sets the Processing environment for the Deck class. MUST be called before instantiating deck.
   * 
   * @param processing the Processing environment for drawing and interaction
   */
  public static void setProcessing(processing.core.PApplet processing) {
    Deck.processing = processing;
  }

  /**
   * Draws a card from the top of the deck (end)
   * 
   * @return the top card from the deck, or null if the deck is empty
   */
  public BaseCard drawCard() {
    if (cardList.isEmpty())
      return null;
    BaseCard topCard = cardList.get(cardList.size() - 1);
    cardList.remove(cardList.size() - 1);
    return topCard;
  }

  /**
   * Adds a card to the top of the deck (end)
   * 
   * @param card the card to be added
   */
  public void addCard(BaseCard card) {
    cardList.add(card);
  }

  /**
   * Retrieves the number of cards in the deck
   * 
   * @return the size of the deck
   */
  public int size() {
    return cardList.size();
  }

  /**
   * Checks if deck is empty
   * 
   * @return true if the deck is empty, false otherwise
   */
  public boolean isEmpty() {
    return cardList.isEmpty();
  }

  /**
   * Draws the top (end) card of the deck onto Processing canvas at given coordinates. If the deck
   * is empty, draws a placeholder indicating the deck is empty.
   * 
   * @param x         the x-coordinate of the card to be drawn
   * @param y         the y-coordinate of the card to be drawn
   * @param isDiscard true if the deck is a discard pile (in which case the top card should be drawn
   *                  face-up), otherwise the top card should be face-down
   */
  public void draw(int x, int y, boolean isDiscard) {
    if (isEmpty()) {
      // Draw a black rectangle if the discard pile is empty
      processing.stroke(0);
      processing.fill(0);
      processing.rect(x, y, 50, 70, 7);
      processing.fill(255);
      processing.textSize(12);
      processing.textAlign(processing.CENTER, processing.CENTER);
      processing.text("Empty", x + 25, y + 35);
    } else {
      if (isDiscard == true) {
        cardList.get(cardList.size() - 1).faceUp = true;
        cardList.get(cardList.size() - 1).draw(x, y);
      } else {
        cardList.get(cardList.size() - 1).faceUp = false;
        cardList.get(cardList.size() - 1).draw(x, y);
      }
    }
  }

}
