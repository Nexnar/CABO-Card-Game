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
import processing.core.PApplet;

/**
 * The CaboGame class implements the main game logic for the card game CABO. It manages the deck,
 * discard pile, players, game state, and user interactions.
 */
public class CaboGame extends PApplet {

  // data fields
  // set up deck and discard pile
  private Deck deck;
  private Deck discard;
  private BaseCard drawnCard;

  private Player[] players;
  private int currentPlayer;
  private int caboPlayer; // index of player who declared CABO

  // Button array
  private Button[] buttons = new Button[5];

  private boolean gameOver; // flag indicating the game is over
  private int selectedCardFromCurrentPlayer; // index of the the first card selected
  // by the current player for the switching action



  /**
   * Enum representing the different action states in the game (e.g., swapping cards, peeking,
   * spying, switching).
   * 
   * This allows us to easily restrict the possible values of a variable.
   */
  private enum ActionState {
    NONE, SWAPPING, PEEKING, SPYING, SWITCHING
  }

  private ActionState actionState = ActionState.NONE;

  // provided data fields for tracking the players' moves through the game
  private ArrayList<String> gameMessages = new ArrayList<>();

  /**
   * Launch the game window; PROVIDED. Note: the argument to PApplet.main() must match the name of
   * this class, or it won't run!
   * 
   * @param args unused
   */
  public static void main(String[] args) {
    PApplet.main("CaboGame");
  }

  /**
   * Sets up the initial window size for the game; PROVIDED.
   */
  @Override
  public void settings() {
    size(1000, 800);
  }

  /**
   * Sets up the game environment, including the font, game state, and game elements.
   */
  @Override
  public void setup() {
    textFont(createFont("Arial", 16));
    BaseCard.setProcessing(this);
    Deck.setProcessing(this);
    // setProcessing for the classes which require it

    deckCheck();

    // initialize data fields
    deck = new Deck(Deck.createDeck());
    discard = new Deck(new ArrayList<BaseCard>());
    drawnCard = null;

    // set up players array and deal their cards
    players = new Player[4];
    players[0] = new Player("Cyntra", 0, false);
    players[1] = new AIPlayer("Avalon", 1, true);
    players[2] = new AIPlayer("Balthor", 2, true);
    players[3] = new AIPlayer("Ophira", 3, true);
    currentPlayer = 0;
    caboPlayer = -1;
    setGameStatus("Turn for " + players[currentPlayer].getName());
    // Deal 4 cards to each player
    for (Player p : players) {
      for (int i = 0; i < 4; i++) {
        p.addCardToHand(this.deck.drawCard());
      }
    }
    // set human's player hand to face up at the start of the game
    players[0].getHand().setFaceUp(0, true);
    players[0].getHand().setFaceUp(1, true);
    // verify that there are 4 players with 4 cards, and 36 remaining cards
    System.out.println("Number of players: " + players.length);
    for (Player p : players) {
      System.out.println(p.getName() + " has " + p.getHand().size() + " cards");
    }
    System.out.println("Remaining cards: " + this.deck.size());

    // set up buttons and update their states for the beginning of the game
    Button.setProcessing(this);
    buttons[0] = new Button("Draw from Deck", 50, 700, 150, 40);
    buttons[1] = new Button("Swap a Card", 220, 700, 150, 40);
    buttons[2] = new Button("Declare Cabo", 390, 700, 150, 40);
    buttons[3] = new Button("Use Action", 390 + 170, 700, 150, 40);
    buttons[4] = new Button("End Turn", 390 + 170 + 170, 700, 150, 40);

    this.updateButtonStates();

    selectedCardFromCurrentPlayer = -1;

    // TODO: update the gameMessages log: "Turn for "+currentPlayer.name
  }

  /**
   * Console-only output for verifying the setup of the card objects and the deck containing them
   */
  public void deckCheck() {
    System.out.println("TODO");
    Deck deck = new Deck(Deck.createDeck());
    System.out.println(deck.size()); // verify that there are 52 cards in the deck

    int actionCardCount = 0;
    int heartCount = 0;
    int diamondCount = 0;
    int clubCount = 0;
    int spadeCount = 0;
    for (BaseCard card : deck.cardList) {
      if (card instanceof ActionCard) {
        actionCardCount++;
      }
      if (card.suit.equals("Hearts"))
        heartCount++;
      if (card.suit.equals("Diamonds")) {
        diamondCount++;
        // verify that the king of diamonds' getRank() returns -1
        if (card.rank == 13 && card.suit.equals("Diamonds"))
          System.out.println("King of Diamonds Rank: " + card.getRank());
      }
      if (card.suit.equals("Clubs"))
        clubCount++;
      if (card.suit.equals("Spades"))
        spadeCount++;
    }
    System.out.println("Total action cards: " + actionCardCount); // verify that there are 8 of each
                                                                  // type of ActionCard
    // verify that there are 13 of each suit
    System.out.println("Hearts: " + heartCount + " Diamonds: " + diamondCount + " Clubs: "
        + clubCount + " Spades: " + spadeCount);
    System.out.println();

  }

  /**
   * Updates the state of the action buttons based on the current game state. Activates or
   * deactivates buttons depending on whether it's the start of a player's turn, a card has been
   * drawn, or the player is an AI.
   */
  public void updateButtonStates() {
    // if the current player is a computer, deactivate all buttons
    if (players[this.currentPlayer].isComputer() == true) {
      for (Button b : buttons) {
        b.setActive(false);
      }
    } else if (this.drawnCard == null) { // otherwise, if no card has been drawn, activate
                                         // accordingly (see writeup)
      buttons[0].setActive(true);
      buttons[1].setActive(false);
      if (this.caboPlayer == -1) { // can only be declared if nobody has done it yet
        buttons[2].setActive(true);
      } else {
        buttons[2].setActive(false);
      }
      buttons[3].setActive(false);
      buttons[4].setActive(false);
    } else {// otherwise, if a card has been drawn, activate accordingly (see writeup)
      buttons[0].setActive(false);
      buttons[1].setActive(true);
      buttons[2].setActive(false);
      if (this.drawnCard instanceof ActionCard) {
        buttons[3].setActive(true); // if action card was drawn, can use this button
        ActionCard actionCard = (ActionCard) this.drawnCard; // down casting
        buttons[3].setLabel(actionCard.getActionType());
      } else {
        buttons[3].setActive(false);
      }
      buttons[4].setActive(true);
    }

  }

  /**
   * Renders the graphical user interface; also handles some game logic for the computer players.
   */
  @Override
  public void draw() {
    background(0, 128, 0);

    // draw the deck and discard pile
    deck.draw(500, 80, false);
    discard.draw(600, 80, true);
    // Add labels to the decks
    textSize(16);
    fill(255);
    text("Deck:", 520, 60);
    text("Discard Pile:", 644, 60);

    // draw the players' hands
    textSize(16);
    fill(255);
    for (int i = 0; i < players.length; i++) {
      text(players[i].getName(), 50, 45 + 150 * i);
      players[i].getHand().draw(60 + 150 * i);
    }

    // draw the buttons only if it is a human
    if (players[this.currentPlayer].isComputer() == false) {
      for (Button b : buttons) {
        b.draw();
      }
    }

    // show the drawn card, if there is one
    if (this.drawnCard != null) {
      this.drawnCard.setFaceUp(true);
      this.drawnCard.draw(500, 500);
    }

    // Display game messages with different colors based on the content
    int y = 200; // Starting y-position for messages
    for (String message : gameMessages) {
      textSize(16);
      if (message.contains("CABO")) {
        fill(255, 128, 0);
      } else if (message.contains("switched")) {
        fill(255, 204, 153);
      } else if (message.contains("spied")) {
        fill(255, 229, 204);
      } else {
        fill(255);
      }
      text(message, width - 300, y); // Adjust x-position as needed
      y += 20; // Spacing between messages
    }

    // if the game is over, display the game over status
    if (gameOver)
      this.displayGameOver();

    // handle the computer players' turns
    if (!gameOver && players[currentPlayer].isComputer()) {
      this.performAITurn();
    }
  }

  /**
   * Handles mouse press events during the game. It manages user interactions with buttons (that is,
   * drawing a card, declaring CABO, swapping cards, using action cards) and updates the game state
   * accordingly.
   */
  @Override
  public void mousePressed() {
    // if game is over or it's the computer's turn, do nothing
    if (gameOver)
      return;

    // handle button clicks

    // if a button is ACTIVE and also being clicked on (mouse is over it)
    for (int i = 0; i < buttons.length; i++) {
      if (buttons[i].isMouseOver() && buttons[i].isActive()) {
        if (i == 0) { // this is Draw from Deck
          this.drawFromDeck();
        } else if (i == 1) { // this is Swap a Card
          actionState = ActionState.SWAPPING;
        } else if (i == 2) { // this is Declare CABO
          this.declareCabo();
        } else if (i == 3) { // this is Use Action
          buttons[i].setLabel("Use Action");
          ActionCard actionCard = (ActionCard) this.drawnCard;
          if (actionCard.getActionType().equalsIgnoreCase("peek")) {
            actionState = ActionState.PEEKING;
            gameMessages.add("Click a card in your hand to peek at it.");

          } else if (actionCard.getActionType().equalsIgnoreCase("spy")) {
            actionState = ActionState.SPYING;
            gameMessages.add("Click a card in another player's hand to spy on it");

          } else {
            actionState = ActionState.SWITCHING;
            gameMessages.add(
                "Click a card from your hand, then a card from another Kingdom's hand to switch");

          }
        } else if (i == 4) { // this is End Turn
          this.nextTurn();
        }
      }
    }


    // handle additional action states (TODO: complete these methods)
    switch (actionState) {
      case SWAPPING -> handleCardSwap();
      case PEEKING -> handlePeek();
      case SPYING -> handleSpy();
      case SWITCHING -> handleSwitch();
      default -> {
        /* No action to be taken */ }
    }
  }

  ///////////////////////////////////// BUTTON CLICK HANDLERS /////////////////////////////////////

  /**
   * Handles the action of drawing a card from the deck. If the deck is empty, the game ends.
   * Otherwise, the drawn card is displayed in the middle of the table. The game status and button
   * states are updated accordingly.
   */
  public void drawFromDeck() {
    // if the deck is empty, game over
    if (this.deck.isEmpty()) {
      gameOver = true;
    } else {// otherwise, draw the next card from the deck
      this.drawnCard = this.deck.drawCard();
      // update the gameMessages log: player.name+" drew a card."
      gameMessages.add(players[currentPlayer].getName() + " drew a card.");
      // update the button states
      this.updateButtonStates();
    }

  }

  /**
   * Handles the action of declaring CABO. Updates the game status to show that the player has
   * declared CABO.
   */
  public void declareCabo() {
    // update the gameMessages log: player.name+" declares CABO!"
    gameMessages.add(players[currentPlayer].getName() + " declares CABO!");
    // set the caboPlayer to the current player's index
    this.caboPlayer = players[currentPlayer].getLabel();
    // end this player's turn
    this.nextTurn();
  }

  ///////////////////////////////////// ACTION STATE HANDLERS /////////////////////////////////////

  /**
   * This method runs when the human player has chosen to SWAP the drawn card with one from their
   * hand. Detect if the mouse is over a card from the currentPlayer's hand and, if it is, swap the
   * drawn card with that card.
   * 
   * If the mouse is not currently over a card from the currentPlayer's hand, this method does
   * nothing.
   */
  public void handleCardSwap() {
    // find a card from the current player's hand that the mouse is currently over
    int selectedCardIndex = players[currentPlayer].getHand().indexOfMouseOver();
    if (selectedCardIndex == -1) {
      return; // not over an actual card in the hand
    }
    BaseCard selectedCard = players[currentPlayer].getHand().cardList.get(selectedCardIndex);
    // swap that card with the drawnCard
    players[currentPlayer].getHand().swap(this.drawnCard, selectedCardIndex);
    // add the swapped-out card from the player's hand to the discard pile
    this.discard.addCard(selectedCard);
    // update the gameMessages log: "Swapped the drawn card with card "+(index+1)+" in the
    // hand."
    gameMessages
        .add("Swapped the drawn card with card " + (selectedCardIndex + 1) + " in the hand.");
    // set the drawnCard to null and the actionState to NONE
    this.drawnCard = null;
    actionState = ActionState.NONE;
    // set all buttons except End Turn to inactive
    buttons[0].setActive(false);
    buttons[1].setActive(false);
    buttons[2].setActive(false);
    buttons[3].setActive(false);
    buttons[4].setActive(true);

    // uncomment this code to erase all knowledge of the card at that index from the AI
    // (you may need to adjust its indentation and/or change some variables)

    AIPlayer AI;
    for (int j = 1; j < players.length; ++j) {
      AI = (AIPlayer) players[j];
      AI.setCardKnowledge(0, selectedCardIndex, false);
    } //


  }

  /**
   * Handles the action of peeking at one of your cards. The player selects a card from their own
   * hand, which is then revealed (set face-up).
   * 
   * If the mouse is not currently over a card from the currentPlayer's hand, this method does
   * nothing.
   */
  public void handlePeek() {

    // find a card from the current player's hand that the mouse is currently over
    int selectedCardIndex = players[currentPlayer].getHand().indexOfMouseOver();
    if (selectedCardIndex == -1) {
      return; // not over an actual card in the hand
    }
    // set that card to be face-up
    players[currentPlayer].getHand().setFaceUp(selectedCardIndex, true);
    // update the gameMessages log: "Revealed card "+(index+1)+" in the hand."
    gameMessages.add("Revealed card " + (selectedCardIndex + 1) + " in the hand.");
    // add the drawnCard to the discard, set drawnCard to null and actionState to NONE
    this.discard.addCard(this.drawnCard);
    this.drawnCard = null;
    actionState = ActionState.NONE;
    // set all buttons except End Turn to inactive
    buttons[0].setActive(false);
    buttons[1].setActive(false);
    buttons[2].setActive(false);
    buttons[3].setActive(false);
    buttons[4].setActive(true);
  }

  /**
   * Handles the spy action, allowing the current player to reveal one of another player's cards.
   * The current player selects a card from another player's hand, which is temporarily revealed.
   * 
   * If the mouse is not currently over a card from another player's hand, this method does nothing.
   */
  public void handleSpy() {
    // find a card from any player's hand that the mouse is currently over
    int selectedCardIndex = -1;
    for (Player p : players) {
      if (p.isComputer()) { // this is NOT the player's own hand
        selectedCardIndex = p.getHand().indexOfMouseOver();
        if (selectedCardIndex != -1) { // if the above command is successful, retrieve the card
          // set it to be face-up
          p.getHand().setFaceUp(selectedCardIndex, true);

          gameMessages
              // update the gameMessages log: "Spied on "+player.name+"'s card.";
              .add("Spied on " + p.getName() + "'s card.");

          // add the drawnCard to the discard, set drawnCard to null and actionState to NONE
          this.discard.addCard(this.drawnCard);
          this.drawnCard = null;
          actionState = ActionState.NONE;
          // set all buttons except End Turn to inactive
          buttons[0].setActive(false);
          buttons[1].setActive(false);
          buttons[2].setActive(false);
          buttons[3].setActive(false);
          buttons[4].setActive(true);
          break;
        }
      }
    }
  }


  /**
   * Handles the switch action, allowing the current player to switch one of their cards with a card
   * from another player's hand.
   * 
   * This action is performed in 2 steps, in this order: (1) select a card from the current player's
   * hand (2) select a card from another player's hand
   * 
   * If the mouse is not currently over a card, this method does nothing.
   */
  public void handleSwitch() {
    // add CaboGame instance variable to store the index of the card from the currentPlayer's
    // hand
    if (selectedCardFromCurrentPlayer == -1) {
      selectedCardFromCurrentPlayer = players[currentPlayer].getHand().indexOfMouseOver();
    }
    // check if the player has selected a card from their own hand yet
    else if (selectedCardFromCurrentPlayer != -1) {
      // if they have selected a card from their own hand already:
      // find a card from any OTHER player's hand that the mouse is currently over
      int otherSelectedCardIndex = -1;
      for (Player p : players) {
        if (p.isComputer()) { // this is NOT the player's own hand
          otherSelectedCardIndex = p.getHand().indexOfMouseOver();
          if (otherSelectedCardIndex != -1) {
            // swap the selected card with the card from the currentPlayer's hand
            players[currentPlayer].getHand().switchCards(selectedCardFromCurrentPlayer, p.getHand(),
                otherSelectedCardIndex);
            // update the gameMessages log: "Switched a card with "+player.name
            gameMessages.add("Switched a card with " + p.getName());
            // add the drawnCard to the discard, set drawnCard to null and actionState to NONE
            discard.addCard(this.drawnCard);
            this.drawnCard = null;
            actionState = ActionState.NONE;
            // set all buttons except End Turn to inactive
            buttons[0].setActive(false);
            buttons[1].setActive(false);
            buttons[2].setActive(false);
            buttons[3].setActive(false);
            buttons[4].setActive(true);



            // uncomment this code to update the knowledge of the swapped card for the other player
            // (you may need to adjust its indentation and variables)

            boolean knowledge = ((AIPlayer) players[p.getLabel()]).getCardKnowledge(p.getLabel(),
                otherSelectedCardIndex);
            ((AIPlayer) players[p.getLabel()]).setCardKnowledge(p.getLabel(),
                otherSelectedCardIndex, ((AIPlayer) players[p.getLabel()])
                    .getCardKnowledge(currentPlayer, selectedCardFromCurrentPlayer));
            ((AIPlayer) players[p.getLabel()]).setCardKnowledge(currentPlayer,
                selectedCardFromCurrentPlayer, knowledge);

            // reset the selected card instance variable to -1
            selectedCardFromCurrentPlayer = -1;
          }
        }
      }
    } else {
      // if they haven't: determine which card in their own hand the mouse is over & store it
      // and do nothing else
      return;
    }



  }

  /////////////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Advances the game to the next player's turn. Hides all players' cards, updates the current
   * player, checks for game-over conditions, resets action states, and updates the UI button states
   * for the new player's turn.
   */
  public void nextTurn() {
    // hide all players' cards
    for (Player p : players) {
      for (int i = 0; i < 4; i++) {
        p.getHand().setFaceUp(i, false);
      }
    }
    // if there is still an active drawnCard, discard it and set drawnCard to null
    if (this.drawnCard != null) {
      discard.addCard(this.drawnCard);
      this.drawnCard = null;
    }
    // advance the current player to the next one in the list
    currentPlayer = (currentPlayer + 1) % 4;
    // check if the new player is the one who declared CABO (and end the game if so)
    if (currentPlayer == caboPlayer) {
      gameOver = true;
    }
    // update the gameMessages log: "Turn for "+player.name
    gameMessages.add("Turn for " + players[currentPlayer].getName());
    // reset the action state to NONE
    actionState = ActionState.NONE;
    // update the button states
    this.updateButtonStates();
  }

  /**
   * Displays the game-over screen and reveals all players' cards. The method calculates each
   * player's score, identifies the winner, and displays a message about the game's result,
   * including cases where there is no winner.
   * 
   * We've provided the code for the GUI parts, but the logic behind this method is still TODO
   */
  public void displayGameOver() {
    // Create a dimmed background overlay
    fill(0, 0, 0, 200);
    rect(0, 0, width, height);
    fill(255);
    textSize(32);
    textAlign(CENTER, CENTER);
    text("Game Over!", (float) width / 2, (float) height / 2 - 150);

    // reveal all players' cards
    for (Player p : players) {
      for (int i = 0; i < 4; i++) {
        p.getHand().setFaceUp(i, false);
      }
    }


    // calculate and display each player's score
    int yPosition = height / 2 - 100;
    textSize(24);
    int currentLowestScore = 1000;
    int score = 0;
    ArrayList<String> winners = new ArrayList<>();
    for (Player p : players) {
      score = p.getHand().calcHand();
      text(p.getName() + "'s score: " + score, (float) width / 2, yPosition);
      yPosition += 30;
      // check if there is a tie or a specific CABO winner (lowest score wins)
      if (score < currentLowestScore) {
        currentLowestScore = score;
        for (int i = 0; i < winners.size(); i++) {
          winners.remove(0);
        }

        winners.add(p.getName());
      }
      // tie
      else if (score == currentLowestScore) {
        winners.add(p.getName());
      }
    }

    if (winners.size() > 1) {
      // display this message if there is no winner
      text("No Winner. The war starts.", (float) width / 2, yPosition + 30);
    } else { // display this message if there is a winner
      text("Winner: " + winners.get(0), (float) width / 2, yPosition + 30);
    }
  }

  /**
   * PROVIDED: Sets the current game status message and updates the message log. If the message log
   * exceeds a maximum number of messages, the oldest message is removed.
   *
   * @param message the message to set as the current game status.
   */
  private void setGameStatus(String message) {
    gameMessages.add(message);
    int MAX_MESSAGES = 15;
    if (gameMessages.size() > MAX_MESSAGES) {
      gameMessages.remove(0); // Remove the oldest message
    }
  }

  /////////////////////////////////////////////////////////////////////////////////////////////////
  // The 2 methods below this line are PROVIDED in their entirety to run the AIPlayer interactions
  // with the CABO game. Uncomment them once you are ready to add AIPlayer actions to your game!
  /////////////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Performs the AI player's turn by drawing a card and deciding whether to swap, discard, or use
   * an action card. If the AI player draws a card that is better than their highest card, they swap
   * it; otherwise, they discard it. If the drawn card is an action card, the AI player performs the
   * corresponding action. If the AI player's hand value is low enough, they may declare CABO.
   */

  private void performAITurn() {
    AIPlayer aiPlayer = (AIPlayer) players[currentPlayer];
    String gameStatus = aiPlayer.getName() + " is taking their turn.";
    setGameStatus(gameStatus);

    // Draw a card from the deck
    drawnCard = deck.drawCard();
    if (drawnCard == null) {
      gameOver = true;
      return;
    }

    gameStatus = aiPlayer.getName() + " drew a card.";
    setGameStatus(gameStatus);

    // Determine if AI should swap or discard
    int drawnCardValue = drawnCard.getRank();
    int highestCardIndex = aiPlayer.getHighestIndex();
    if (highestCardIndex == -1) {
      highestCardIndex = 0;
    }
    int highestCardValue = aiPlayer.getHand().getRankAtIndex(highestCardIndex);

    // Swap if the drawn card has a lower value than the highest card in hand
    if (drawnCardValue < highestCardValue) {
      BaseCard cardInHand = aiPlayer.getHand().swap(drawnCard, highestCardIndex);
      aiPlayer.setCardKnowledge(aiPlayer.getLabel(), highestCardIndex, true);
      discard.addCard(cardInHand);
      gameStatus = aiPlayer.getName() + " swapped the drawn card with card "
          + (highestCardIndex + 1) + " in their hand.";
      setGameStatus(gameStatus);
    } else if (drawnCard instanceof ActionCard) {
      // Use the action card
      String actionType = ((ActionCard) drawnCard).getActionType();
      gameStatus = aiPlayer.getName() + " uses an action card: " + actionType;
      setGameStatus(gameStatus);
      performAIAction(aiPlayer, actionType);
      discard.addCard(drawnCard);
    } else { // Discard the drawn card
      discard.addCard(drawnCard);
      gameStatus = aiPlayer.getName() + " discarded the drawn card: " + drawnCard;
      setGameStatus(gameStatus);
    }

    // AI may declare Cabo if hand value is low enough
    int handValue = aiPlayer.calcHandBlind();
    if (handValue <= random(13, 21) && caboPlayer == -1) {
      declareCabo();
    }

    // Prepare for the next turn
    drawnCard = null;
    nextTurn();
  }//


  /**
   * Performs the specified action for the AI player based on the drawn action card. Actions include
   * peeking at their own cards, spying on another player's card, or switching cards with another
   * player.
   *
   * @param aiPlayer   the AI player performing the action.
   * @param actionType the type of action to perform ("peek", "spy", or "switch").
   */

  private void performAIAction(AIPlayer aiPlayer, String actionType) {
    Player otherPlayer = players[0];
    // Assuming Player 1 is the human player
    String gameStatus = "";
    switch (actionType) {
      case "peek" -> { // AI peeks at one of its own cards
        int unknownCardIndex = aiPlayer.getUnknownCardIndex();
        if (unknownCardIndex != -1) {
          aiPlayer.setCardKnowledge(aiPlayer.getLabel(), unknownCardIndex, true);
          gameStatus = aiPlayer.getName() + " peeked at their card " + (unknownCardIndex + 1);
          setGameStatus(gameStatus);
        }
      }
      case "spy" -> { // AI spies on one of the human player's cards
        int spyIndex = aiPlayer.getSpyIndex();
        if (spyIndex != -1) {
          aiPlayer.setCardKnowledge(0, spyIndex, true);
          gameStatus = aiPlayer.getName() + " spied on Player 1's card " + (spyIndex + 1);
          setGameStatus(gameStatus);
        }
      }
      case "switch" -> {
        // AI switches one of its cards with one of the human player's cards
        int aiCardIndex = aiPlayer.getHighestIndex();
        if (aiCardIndex == -1) {
          aiCardIndex = (int) random(aiPlayer.getHand().size());
        }
        int otherCardIndex = aiPlayer.getLowestIndex(otherPlayer);
        if (otherCardIndex == -1)
          otherCardIndex = (int) random(otherPlayer.getHand().size());

        // Swap the cards between AI and the human player
        aiPlayer.getHand().switchCards(aiCardIndex, otherPlayer.getHand(), otherCardIndex);
        boolean preCardKnowledge = aiPlayer.getCardKnowledge(aiPlayer.getLabel(), aiCardIndex);
        aiPlayer.setCardKnowledge(aiPlayer.getLabel(), aiCardIndex,
            aiPlayer.getCardKnowledge(0, otherCardIndex));
        aiPlayer.setCardKnowledge(0, otherCardIndex, preCardKnowledge);

        gameStatus = aiPlayer.getName() + " switched card " + (aiCardIndex + 1) + " with "
            + otherPlayer.getName() + "'s " + (otherCardIndex + 1) + ".";
        setGameStatus(gameStatus);
      }
    }
  }//


}
