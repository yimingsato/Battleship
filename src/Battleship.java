/*
   File name: Battleship.java
   Name: Lucas Chen
   Course: ICS3U-22
   Date: June 14, 2024
   Description: Online version against a computer of the board game Battleship.
*/

import java.util.*;
import java.io.*;

public class Battleship {

   // Constants
   final static int SIZE = 10;

   final static char HIT = 'X';
   final static char MISS = 'O';
   final static char EMPTY = '-';

   final static char DESTROYER = 'D';
   final static char CRUISER = 'C';
   final static char SUBMARINE = 'S';
   final static char BATTLESHIP = 'B';
   final static char CARRIER = 'A';

   final static int DESTROYER_IDX = 0;
   final static int CRUISER_IDX = 1;
   final static int SUBMARINE_IDX = 2;
   final static int BATTLESHIP_IDX = 3;
   final static int CARRIER_IDX = 4;

   final static int DESTROYER_SIZE = 2;
   final static int CRUISER_SIZE = 3;
   final static int SUBMARINE_SIZE = 3;
   final static int BATTLESHIP_SIZE = 4;
   final static int CARRIER_SIZE = 5;

   final static String[] shipName = { "DESTROYER", "CRUISER", "SUBMARINE", "BATTLESHIP", "AIRCRAFT CARRIER" };
   final static char[] shipSymbol = { DESTROYER, CRUISER, SUBMARINE, BATTLESHIP, CARRIER };

   public static void main(String[] args) {
      Scanner sc = new Scanner(System.in);
      // while loop to continue the game until the user chooses to quit.
      boolean quit = false;
      while (!quit) {

         // create a new empty board for the player and computer
         char[][] playerBoard = emptyBoard();
         char[][] computerBoard = emptyBoard();
         // set the arrays to determine when a ship is sunk and if all are sunk
         int[] playerShipPartsLeft = { DESTROYER_SIZE, CRUISER_SIZE, SUBMARINE_SIZE, BATTLESHIP_SIZE, CARRIER_SIZE };
         int[] computerShipPartsLeft = { DESTROYER_SIZE, CRUISER_SIZE, SUBMARINE_SIZE, BATTLESHIP_SIZE, CARRIER_SIZE };
         // set the variable to determine difficulty
         boolean easyDifficulty = true;
         // set arrays to be used for the computer's normal attack
         int[] lastHit = { -1, -1 };
         int[] firstHit = { -1, -1 };

         // set variables to be used in while loop
         boolean userDeciding = true;
         boolean newGame = false;
         boolean gameOver = false;
         // create while loop to get the users decision
         while (userDeciding) {
            // print out the main menu and get the users input
            printMenu();
            int userChoice = sc.nextInt();
            sc.nextLine();
            clearConsole();
            // switch case to determine their input
            switch (userChoice) {
               case 1: // New Game
                  clearConsole();
                  newGame = true;
                  userDeciding = false;
                  break;
               case 2: // Load Saved Game
                  clearConsole();
                  boolean boardLoaded = false;
                  while (!boardLoaded) {
                     System.out.print("Enter the name of the saved game (Enter 'exit' to exit back to main menu): ");
                     String filename = sc.nextLine() + ".txt";
                     // if the user does not want to load a game they have the option to go back to
                     // main menu
                     if (filename.equals("exit.txt")) {
                        boardLoaded = true;
                     } else {
                        // create a new try catch for the Buffered Reader
                        try {
                           BufferedReader reader = new BufferedReader(new FileReader(filename));
                           String input;
                           reader.readLine();
                           // read in the player's board
                           for (int i = 0; i < 10; i++) {
                              input = reader.readLine();
                              for (int j = 0; j < 10; j++) {
                                 playerBoard[i][j] = input.charAt(j);
                              }
                           }
                           reader.readLine();
                           // read in the computer's board
                           for (int i = 0; i < 10; i++) {
                              input = reader.readLine();
                              for (int j = 0; j < 10; j++) {
                                 computerBoard[i][j] = input.charAt(j);
                              }
                           }
                           // read in the difficulty of the saved game
                            easyDifficulty = (input = reader.readLine()).equals("easy");
                           reader.close();
                           boardLoaded = true;
                           userDeciding = false;
                        } catch (IOException iox) {
                           // if there was an error let them try again
                           System.out.println("Error reading file or game cannot be found. Press ENTER to try again.");
                           sc.nextLine();
                           clearConsole();
                        }
                     }
                  }
                  // determine how many ship parts left of both boards in the saved game
                  shipPartsLeft(playerShipPartsLeft, playerBoard);
                  shipPartsLeft(computerShipPartsLeft, computerBoard);
                  break;
               case 3: // Print Instructions
                  printInstruction();
                  System.out.println("Press ENTER to return to main menu.");
                  sc.nextLine();
                  clearConsole();
                  userDeciding = true;
                  break;
               case 4: // Quit
                  quit = true;
                  userDeciding = false;
                  gameOver = true;
                  clearConsole();
                  break;
               default: // Invalid Input
                  clearConsole();
                  System.out.println("Invalid Input! Please try again.");
            }
         }

         if (newGame) {
            boolean validInput = false;
            // let user choose difficulty
            while (!validInput) {
               System.out.println("""
                     +=======================================+
                     |           CHOOSE DIFFICULTY           |
                     |---------------------------------------|
                     |  1) Easy                              |
                     |  2) Normal                            |
                     +=======================================+
                     """);
               int input = sc.nextInt();
               sc.nextLine();
               // valiate the input
               if (input == 1) {
                  easyDifficulty = true;
                  validInput = true;
               } else if (input == 2) {
                  easyDifficulty = false;
                  validInput = true;
               } else {
                  System.out.println("Invalid input. Press ENTER to try again.");
                  sc.nextLine();
                  clearConsole();
               }
            }
            // for loop for the user to place all ships
            for (int i = 0; i < playerShipPartsLeft.length; i++) {
               validInput = false;
               while (!validInput) {
                  clearConsole();
                  System.out.println("Player Board:");
                  displayShipBoard(playerBoard);
                  System.out.printf("""
                        +=======================================+
                        |    %-30s     |
                        |---------------------------------------|
                        |  1) Vertically                        |
                        |  2) Horizontally                      |
                        +=======================================+
                            """, "PLACING " + shipName[i] + " (" + playerShipPartsLeft[i] + ")");
                  int orientation = sc.nextInt();
                  clearConsole();
                  displayShipBoard(playerBoard);
                  System.out.printf("""
                        +=======================================+
                        |    %-30s     |
                        |---------------------------------------|
                        |  Enter the column number (left most   |
                        |  square if horizontal)                |
                        +=======================================+
                        """, "PLACING " + shipName[i] + " (" + playerShipPartsLeft[i] + ")");
                  int column = sc.nextInt();
                  displayShipBoard(playerBoard);
                  System.out.printf("""
                        +=======================================+
                        |    %-30s     |
                        |---------------------------------------|
                        |  Enter the row number (top square if  |
                        |  vertical)                            |
                        +=======================================+
                        """, "PLACING " + shipName[i] + " (" + playerShipPartsLeft[i] + ")");
                  int row = sc.nextInt();
                  sc.nextLine();
                  // check if the placement is valid
                  if (validShipPlacement(row, column, orientation, playerShipPartsLeft[i], playerBoard)) {
                     validInput = true;
                     placeShips(row, column, orientation, playerShipPartsLeft[i], shipSymbol[i], playerBoard);
                  } else {
                     sc.nextLine();
                  }
               }

            }
            // place and display computer's board
            placeComputerShips(computerBoard, computerShipPartsLeft);
            displayShipBoard(computerBoard);
         }

         // create the loop for the actual game
         while (!gameOver) {
            displayShipAndShotBoard(playerBoard, computerBoard);
            displayShipBoard(computerBoard);
            boolean validInput = false;
            int input = 0;
            // give the user a choice to attack, save, or surrender
            while (!validInput) {
               System.out.println("""
                     +=======================================+
                     |  1) Attack                            |
                     |  2) Save Game                         |
                     |  3) Surrender                         |
                     +=======================================+
                     """);
               input = sc.nextInt();
               sc.nextLine();
               // validate the input
               if (input != 1 && input != 2 && input != 3) {
                  System.out.println("Invalid Option. Press ENTER to try again.");
                  sc.nextLine();
               } else {
                  validInput = true;
               }
            }
            // Attack
            if (input == 1) {
               boolean validAttack = false;
               while (!validAttack) {
                  displayShipAndShotBoard(playerBoard, computerBoard);
                  displayShipBoard(computerBoard);
                  // let user choose row and column
                  System.out.print("Enter the column number: ");
                  int col = sc.nextInt();
                  System.out.print("Enter the row number: ");
                  int row = sc.nextInt();
                  
                  sc.nextLine();
                  // validate the attack
                  validAttack = isValidAttack(row, col, computerBoard);
                  if (validAttack) {
                     System.out.println("Attacking (" + col + ", " + row + ")...\n");
                     hitOrMiss(row, col, computerBoard, computerShipPartsLeft);
                     shipSunk(computerShipPartsLeft);
                     System.out.println("Player: ");
                     displayShipAndShotBoard(playerBoard, computerBoard);
                     System.out.println("Computer: ");
                     displayShipBoard(computerBoard);
                  } else {
                     sc.nextLine();
                     clearConsole();
                  }
               }
               // check if the user has won
               if (isGameOver(computerShipPartsLeft)) {
                  gameOver = true;
                  System.out.println("You WIN! You have sunk all Computer ships!");
               } else { // if not continue with the computer's attack
                  System.out.println("Press ENTER to continue...");
                  sc.nextLine();
                  clearConsole();

                  if (easyDifficulty) {
                     computerEasyAttack(playerBoard, playerShipPartsLeft);
                  } else {
                     computerNormalAttack(playerBoard, playerShipPartsLeft, lastHit, firstHit);
                  }
                  // check if the computer has won
                  gameOver = isGameOver(playerShipPartsLeft);
                  if (gameOver) {
                     System.out.println("You LOSE! Computer has sunk all your ships! Here was the Computer's board:");
                  }
               }
               // save the game if they choose to save
            } else if (input == 2) {
               boolean gameSaved = false;
               while (!gameSaved) {
                  System.out.print("Enter a save name: ");
                  String filename = sc.nextLine();
                  gameSaved = saveGame(filename, playerBoard, computerBoard, easyDifficulty);
               }
               gameOver = true;
               System.out.println("Game saved! Press ENTER to return to main menu.");
               sc.nextLine();
               // if they surrender end the game and show the computer board.
            } else {
               System.out.println("You have surrendered! Computer Wins! Here was the Computer's board: ");
               displayShipBoard(computerBoard);
               System.out.println("Press ENTER to return to main menu.");
               sc.nextLine();
               clearConsole();
               gameOver = true;
            }
         }
      }

      System.out.println("""
            +=======================================+
            |         THANK YOU FOR PLAYING!        |
            +=======================================+""");
   }

   /*
    * Determines if the player's attack on the given board (player's board) is
    * valid.
    * If invalid it will provide specific feedback on the error. The attack is
    * valid if it is
    * within the boards boundaries and has not already been attacked.
    *
    * @param row, the row number of the location.
    * 
    * @param col, the column number of the location.
    * 
    * @param board, the board where the validity of the attack is checked.
    *
    * @return true if the attack is valid for the given board, and false otherwise.
    */
   public static boolean isValidAttack(int row, int col, char[][] board) {
      // check if the attack is outside the board's boundaries
      if (row > 10 || row < 1) {
         System.out.println("Invalid row number! Press ENTER to try again.");
         return false;
      }
      if (col > 10 || col < 1) {
         System.out.println("Invalid col number! Press ENTER to try again.");
         return false;
      }
      // check if the attack on the board was already attacked
      if (board[row - 1][col - 1] == HIT || board[row - 1][col - 1] == MISS) {
         System.out.println("Cannot attack the same spot twice! Press ENTER to try again.");
         return false;
      }
      return true;

   }

   /*
    * Does the same thing as isValidAttack() but does not print feedback.
    * The method is used for the computer's attack to determine if it is valid
    * without outputting feedback.
    *
    * @param row, the row number of the location.
    * @param col, the column number of the location.
    * @param board, the board where the validity of the attack is checked.
    *
    * @return true if the attack is valid for the given board, and false otherwise.
    */
   public static boolean isValidAttack2(int row, int col, char[][] board) {
      // check if the attack is outside the board's boundaries
      if (row > 10 || row < 1) {
         return false;
      }
      if (col > 10 || col < 1) {
         return false;
      }
      // check if the attack on the board was already attacked
       return board[row - 1][col - 1] != HIT && board[row - 1][col - 1] != MISS;

   }

   /*
    * Determines whether a ship was sunk or not given the number of ships left.
    * Used for the player to print out a message which tells them which ship
    * they have sunk.
    *
    * @param shipsLeft, an array of the number of parts left of each type of the player's ships.
    */
   public static void shipSunk(int[] shipsLeft) {
      // check the number of ship parts left and print out a message if any were sunk
      if (shipsLeft[DESTROYER_IDX] == 0) {
         System.out.println("You have sunk Computer's Destroyer!");
         shipsLeft[DESTROYER_IDX] = -1;
      } else if (shipsLeft[CRUISER_IDX] == 0) {
         System.out.println("You have sunk Computer's Cruiser!");
         shipsLeft[CRUISER_IDX] = -1;
      } else if (shipsLeft[SUBMARINE_IDX] == 0) {
         System.out.println("You have sunk Computer's Submarine!");
         shipsLeft[SUBMARINE_IDX] = -1;
      } else if (shipsLeft[BATTLESHIP_IDX] == 0) {
         System.out.println("You have sunk Computer's Battleship!");
         shipsLeft[BATTLESHIP_IDX] = -1;
      } else if (shipsLeft[CARRIER_IDX] == 0) {
         System.out.println("You have sunk Computer's Aircraft Carrier!");
         shipsLeft[CARRIER_IDX] = -1;
      }
   }

   /*
    * Determines whether a ship was sunk or not given the number of ships left.
    * Used for the computer and prints out a message which tells the player which ship
    * was sunk.
    *
    * @param shipsLeft, an array of the number of parts left of each of the computer's ships.
    *
    * @return true if a ship was sunk, false otherwise.
    */
   public static boolean shipSunk2(int[] shipsLeft) {
      // check the number of ship parts left and print out a message if any were sunk. Also returns true
      // so the computer is aware they landed a hit
      if (shipsLeft[DESTROYER_IDX] == 0) {
         System.out.println("Computer has sunk your Destroyer!");
         shipsLeft[DESTROYER_IDX] = -1;
         return true;
      } else if (shipsLeft[CRUISER_IDX] == 0) {
         System.out.println("Computer has sunk your Cruiser");
         shipsLeft[CRUISER_IDX] = -1;
         return true;
      } else if (shipsLeft[SUBMARINE_IDX] == 0) {
         System.out.println("Computer has sunk your Submarine!");
         shipsLeft[SUBMARINE_IDX] = -1;
         return true;
      } else if (shipsLeft[BATTLESHIP_IDX] == 0) {
         System.out.println("Computer has sunk your Battleship!");
         shipsLeft[BATTLESHIP_IDX] = -1;
         return true;
      } else if (shipsLeft[CARRIER_IDX] == 0) {
         System.out.println("Computer has sunk your Aircraft Carrier!");
         shipsLeft[CARRIER_IDX] = -1;
         return true;
      }
      return false;

   }

   /*
    * Determines if the game has ended by checking the number of ships left. If 
    * there are no more ships left the game has ended.
    *
    * @param shipsLeft, uses the array which holds the number of ship parts left to determine 
    * if the game has ended.
    *
    * @return gameOver, a boolean which would be true if the game is over and false otherwise.
    */
   public static boolean isGameOver(int[] shipsLeft) {
      // create boolean to determine whether the game was over
      boolean gameOver = true;
      for (int i = 0; i < shipsLeft.length; i++) {
         // if there are still ships left, set gameOver to false
          if (shipsLeft[i] > 0) {
              gameOver = false;
              break;
          }
      }
      return gameOver;
   }

   /*
    * Lets the user save the current game as a file with a save name of their choice. It saves the player's board, then the computer's board
    * and the difficulty at the end.
    * 
    * @param filename, the name the user wants the save the game as.
    * @param playerBoard, the player's board to be saved.
    * @param computerBoard, the computer's board to be saved.
    * @param difficulty, the difficulty of the game to be saved.
    *
    * @return true if the game was successfully saved, false otherwise
    */
   public static boolean saveGame(String filename, char[][] playerBoard, char[][] computerBoard, boolean difficulty) {
      // create try catch for the BufferedWriter
      try {
         // create a new buffered writer
         BufferedWriter writer = new BufferedWriter(new FileWriter(filename + ".txt"));
         // save the player's board onto the file
         writer.write("Player Board: \n");
         for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
               writer.write(playerBoard[i][j]);
            }
            writer.newLine();
         }
         // save the computer's board onto the file
         writer.write("Computer Board: \n");
         for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
               writer.write(computerBoard[i][j]);
            }
            writer.newLine();
         }
         // save the game's difficulty onto the file.
         if (difficulty) {
            writer.write("easy");
         } else {
            writer.write("normal");
         }
         writer.close();
         return true;
      // catch and print out and error for the user
      } catch (IOException iox) {
         System.out.println("Error saving game. Try again.");
         return false;
      }
   }

   /*
    * Determines how many ship parts are left on the given board. Used when loading a saved game to determine 
    * how many ships of each type are left.
    *
    * @param partsLeft, the array to save the number of ships parts left of each ship.
    * @param board, the given board to find the number of ship parts left.
    */
   public static void shipPartsLeft(int[] partsLeft, char[][] board) {
      // set the number of ship parts left of each ship type to 0
      for (int i = 0; i < partsLeft.length; i++) {
         partsLeft[i] = 0;
      }
      // add the ship parts of each type
      for (int i = 0; i < SIZE; i++) {
         for (int j = 0; j < SIZE; j++) {
            if (board[i][j] == DESTROYER) {
               partsLeft[DESTROYER_IDX] += 1;
            } else if (board[i][j] == CRUISER) {
               partsLeft[CRUISER_IDX] += 1;
            } else if (board[i][j] == SUBMARINE) {
               partsLeft[SUBMARINE_IDX] += 1;
            } else if (board[i][j] == BATTLESHIP) {
               partsLeft[BATTLESHIP_IDX] += 1;
            } else if (board[i][j] == CARRIER) {
               partsLeft[CARRIER_IDX] += 1;
            }
         }
      }
   }

   /*
    * The computer that attacks the player on easy difficulty. It will always attack a random spot 
    * with a random row and column on the given board.
    * 
    * @param playerBoard, the given board that the computer is attacking.
    * @param shipPartsLeft, an array with the number of ship parts left of the player
    * used for the hitOrMiss() method as well as the shipSunk2() method.
    */
   public static void computerEasyAttack(char[][] playerBoard, int[] shipPartsLeft) {
      int row = 0;
      int col = 0;
      boolean validAttack = false;
      while (!validAttack) {
         row = (int) (Math.random() * 10) + 1;
         col = (int) (Math.random() * 10) + 1;
         validAttack = isValidAttack2(row, col, playerBoard);
      }
      System.out.println("Computer attacks (" + col + ", " + row + ")...\n");
      hitOrMiss(row, col, playerBoard, shipPartsLeft);
      shipSunk2(shipPartsLeft);
   }

   /*
    * The computer that attacks the player on normal difficulty. It will continue to attack randomly 
    * until it gets a hit. Once a hit is landed it will begin to attack around the hit until the ship is sunk.
    *
    * @param playerBoard, the given ship board that the computer is attacking.
    * @param shipPartsLeft, the number of ship parts left used for the hitOrMiss() and shipSunk2() methods
    * @param lastHit, the coordinates [row, column] of the last computer hit so it can target around it.
    * @param firstHit, the coordinates [row, column] of the first hit the computer lands. If the ship is still not 
    * sunk and there are no more areas around the hit to target, it will go back to the first hit.
    */
   public static void computerNormalAttack(char[][] playerBoard, int[] shipPartsLeft, int[] lastHit, int[] firstHit) {
      int row = 0;
      int col = 0;
      // if the computer has not landed a hit, keep randomly attacking.
      if (lastHit[0] == -1) {
         boolean validAttack = false;
         while (!validAttack) {
            row = (int) (Math.random() * 10) + 1;
            col = (int) (Math.random() * 10) + 1;
            validAttack = isValidAttack2(row, col, playerBoard);
         }
         System.out.println("Computer attacks (" + col + ", " + row + ")...\n");
         if (hitOrMiss(row, col, playerBoard, shipPartsLeft)) {
            lastHit[0] = row;
            lastHit[1] = col;
            firstHit[0] = col;
            firstHit[1] = row;
         }
      // if the computer did land a hit, attack around that hit randomly.
      } else {
         boolean validAttack = false;
         while (!validAttack) {
            row = lastHit[0];
            col = lastHit[1];
            if (isValidAttack2(row + 1, col, playerBoard) || isValidAttack2(row - 1, col, playerBoard)
                  || isValidAttack2(row, col + 1, playerBoard) || isValidAttack2(row, col - 1, playerBoard)) {
               // generate a random number that represents which square to attack.
               int computerGuess = (int) (Math.random() * 4) + 1;
               // switch case to execute the attack.
               switch (computerGuess) {
                  case 1:
                     row += 1;
                     // validate the attack
                     validAttack = isValidAttack2(row, col, playerBoard);
                     if (validAttack) {
                        System.out.println("Computer attacks (" + col + ", " + row + ")...\n");
                        // check if it is a hit or miss and if the ship is sunk make the computer randomly attack again.
                        if (hitOrMiss(row, col, playerBoard, shipPartsLeft)) {
                           if (shipSunk2(shipPartsLeft)) {
                              lastHit[0] = -1;
                              lastHit[1] = -1;
                           } else {
                              lastHit[0] = row;
                           }
                        }
                     }
                     break;
                  case 2:
                     row -= 1;
                     validAttack = isValidAttack2(row, col, playerBoard);
                     if (validAttack) {
                        System.out.println("Computer attacks (" + col + ", " + row + ")...\n");
                        if (hitOrMiss(row, col, playerBoard, shipPartsLeft)) {
                           if (shipSunk2(shipPartsLeft)) {
                              lastHit[0] = -1;
                              lastHit[1] = -1;
                           } else {
                              lastHit[0] = row;
                           }
                        }
                     }
                     break;
                  case 3:
                     col += 1;
                     validAttack = isValidAttack2(row, col, playerBoard);
                     if (validAttack) {
                        System.out.println("Computer attacks (" + col + ", " + row + ")...\n");
                        if (hitOrMiss(row, col, playerBoard, shipPartsLeft)) {
                           if (shipSunk2(shipPartsLeft)) {
                              lastHit[0] = -1;
                              lastHit[1] = -1;
                           } else {
                              lastHit[1] = col;
                           }
                        }
                     }
                     break;
                  case 4:
                     col -= 1;
                     validAttack = isValidAttack2(row, col, playerBoard);
                     if (validAttack) {
                        System.out.println("Computer attacks (" + col + ", " + row + ")...\n");
                        if (hitOrMiss(row, col, playerBoard, shipPartsLeft)) {
                           if (shipSunk2(shipPartsLeft)) {
                              lastHit[0] = -1;
                              lastHit[1] = -1;
                           } else {
                              lastHit[1] = col;
                           }
                        }
                     }
                     break;
               }
            // if there are no available attacks and the ship is not sunk set the last hit to the first hit.
            } else {
               lastHit[0] = firstHit[0];
               lastHit[1] = firstHit[1];
            }
         }
      }
   }

   /*
    * Determines the if the attack was a hit or a miss. It will print a message for hit or miss. 
    * It also subtracts a ship part from the type of ship hit. It will then replace the square 
    * with the hit symbol if hit and the miss symbol if a miss.
    *
    * @param row, the row of the location being attacked.
    * @param col, the column of the location being attacked.
    * @param board, the given board being attacked on to determine a hit or miss.
    * @param shipPartsLeft, the number of parts left of each ship type. specified by DESTROYER_IDX, 
    * CRUISER_IDX, BATTLESHIP_IDX, SUBMARINE_IDX and CARRIER_IDX.
    *
    * @return true if the attack was a hit on the given board, false otherwise.
    */
   public static boolean hitOrMiss(int row, int col, char[][] board, int[] shipPartsLeft) {
      // if the square is not an empty square print out "HIT!", set the board location to a hit
      // and determine which ship was hit to subtract a part.
      if (board[row - 1][col - 1] != EMPTY) {
         System.out.println("HIT!");
         if (board[row - 1][col - 1] == DESTROYER) {
            shipPartsLeft[DESTROYER_IDX] -= 1;
         } else if (board[row - 1][col - 1] == CRUISER) {
            shipPartsLeft[CRUISER_IDX] -= 1;
         } else if (board[row - 1][col - 1] == BATTLESHIP) {
            shipPartsLeft[BATTLESHIP_IDX] -= 1;
         } else if (board[row - 1][col - 1] == SUBMARINE) {
            shipPartsLeft[SUBMARINE_IDX] -= 1;
         } else {
            shipPartsLeft[CARRIER_IDX] -= 1;
         }
         board[row - 1][col - 1] = HIT;
         return true;
      // if the board was empty print out "MISS!" and set the board location to a miss.
      } else {
         System.out.println("MISS!");
         board[row - 1][col - 1] = MISS;
         return false;
      }
   }

   /*
    * Places the computer's ship when creating a new game. The ships are placed with a random orientation, row, and column.
    *
    * @param computerBoard, the board where the ships will be placed.
    * @param shipSizes, an array of the sizes of each ship type to be placed.
    */
   public static void placeComputerShips(char[][] computerBoard, int[] shipSizes) {
      // create the row and column variables.
      int row;
      int col;
      // set a for loop for each ship and randomize the orientation, row and column.
      for (int i = 0; i < 5; i++) {
         int orientation = (int) (Math.random() * 2) + 1;
         if (orientation == 1) {
            row = (int) (Math.random() * (10 - shipSizes[i])) + 1;
            col = (int) (Math.random() * 10) + 1;
         } else {
            col = (int) (Math.random() * (10 - shipSizes[i])) + 1;
            row = (int) (Math.random() * 10) + 1;
         }
         while (!validShipPlacement(row, col, orientation, shipSizes[i], computerBoard)) {
            if (orientation == 1) {
               row = (int) (Math.random() * (10 - shipSizes[i])) + 1;
               col = (int) (Math.random() * 10) + 1;
            } else {
               col = (int) (Math.random() * (10 - shipSizes[i])) + 1;
               row = (int) (Math.random() * 10) + 1;
            }   
         }
         // place the ships using the placeShips() method.
         placeShips(row, col, orientation, shipSizes[i], shipSymbol[i], computerBoard);
      }  
   }

   /*
    * Determine if, on the given game board, the given location is a valid place for the 
    * given ship placed in the given orientation (horizontal / vertical)
    * For horizontal placement, the location is the left most slot of the ship
    * For vertical placement, the location is the top most slot of the ship
    * The placement is valid if the whole ship is within the board and the entire ship
    * is placed on empty slots
    * 
    * @param board, the game board where the validity of ship placement is checked
    * @param row, row number of the location to be checked
    * @param col, column number of the location to be checked
    * @param size, size of the ship specified by DESTROYER_SIZE, CRUISER_SIZE, SUBMARINE_SIZE...
    * @param orientation, orientation of the ship specified using the number 1 or 2.
    *
    * @return true if the given location is valid for the given ship placed with the 
    * given orientation, false otherwise
   */
   public static boolean validShipPlacement(int row, int col, int orientation, int size, char[][] board) {
      // validates the orientation
      if (orientation != 1 && orientation != 2) {
         System.out.println("Invalid orientation! Press ENTER to try again.");
         return false;
      }
      // validates the row and column for the vertical orientation
      if (orientation == 1) {
         if (row + size - 1 > SIZE || row < 1) {
            System.out.println("Invalid row number! Press ENTER to try again.");
            return false;
         }
         if (col > SIZE || col < 1) {
            System.out.println("Invalid column number! Press ENTER to try again.");
            return false;
         }
         for (int i = row - 1; i < row - 1 + size; i++) {
            if (board[i][col - 1] != '-') {
               System.out.println("Cannot place ship on another ship! Press ENTER to try again.");
               return false;
            }
         }
      // validates the row and column for the horizontal orientation.
      } else if (orientation == 2) {
         if (col + size - 1 > SIZE || col < 1) {
            System.out.println("Invalid column number! Press ENTER to try again.");
            return false;
         }
         if (row > SIZE || row < 1) {
            System.out.println("Invalid row number! Press ENTER to try again.");
            return false;
         }
         for (int i = col - 1; i < col - 1 + size; i++) {
            if (board[row - 1][i] != '-') {
               System.out.println("Cannot place ship on another ship! Press ENTER to try again.");
               return false;
            }
         }

      }
      return true;
   }
   /*
    * Places the ships onto the given board using the given row, column, orientation, size, and symbol.
    *
    * @param row, the row where the ship will be placed on. The top of the ship if the orientation is vertical.
    * @param col, the column where the ship will be placed on. Far left of the ship if the orientation is horizontal.
    * @param orientation, the orientation (horizontal/vertical) the ship will be placed in.
    * @param size, the size of the ship.
    * @param symbol, the symbol of the ship to represent which type of ship it is.
    * @param board, the board where the ship will be placed on.
    */
   public static void placeShips(int row, int col, int orientation, int size, char symbol, char[][] board) {
      // if the orientation is vertical, place the ships along the row.
      if (orientation == 1) {
         for (int i = row - 1; i < row - 1 + size; i++) {
            board[i][col - 1] = symbol;
         }
      // if the orientation is horizontal, place the ships along the columns.
      } else {
         for (int i = col - 1; i < col - 1 + size; i++) {
            board[row - 1][i] = symbol;
         }
      }
   }

   /*
    * Clears the console by printing out many blank lines. Used to help make the screen organized and help the user
    * understand what was old information and what is new information.
    */
   public static void clearConsole() {
      for (int i = 0; i < 15; i++) {
         System.out.println();
      }
   }

   /*
    * Prints out the main menu of the game with the list of options (New game, Load game, Show instructions, Quit) the user can do.
    */
   public static void printMenu() {
      System.out.println("""
            +=======================================+
            |         WELCOME TO BATTLESHIP         |
            |---------------------------------------|
            |  1) New Game                          |
            |  2) Load Saved Game                   |
            |  3) Show Instructions                 |
            |  4) Quit                              |
            +=======================================+""");
   }

   /*
    * Displays the ship board with the row and columns.
    *
    * @param board, the given board that is being displayed.
    */
   public static void displayShipBoard(char[][] board) {
      // prints out the column numbers
      for (int i = 1; i <= SIZE; i++) {
         System.out.printf("%4s", i);
      }
      System.out.println();
      for (int i = 0; i < SIZE; i++) {
         // prints out the row numbers
         System.out.printf("%2d ", i + 1);
         for (int j = 0; j < SIZE; j++) {
            // prints the board
            System.out.printf("%-4s", board[i][j]);
         }
         System.out.println();
      }
      System.out.println();
   }

   /*
    * Displays both the ship board along with the shot board with the rows and columns as well as labels
    * to determine which board is which.
    *
    * @param ship, the board with the ships to be displayed.
    * @param shots, the other/opponent board containing the shots to be displayed.
    */
   public static void displayShipAndShotBoard(char[][] ship, char[][] shots) {
      // the labels for each board
      System.out.println("Ship Board:                                     Shots Board:");
      // the column numbers for each board
      for (int i = 1; i <= SIZE; i++) {
         System.out.printf("%4s", i);
      }
      System.out.print("\t");
      
      for (int i = 1; i <= SIZE; i++) {
         System.out.printf("%4s", i);
      }
      System.out.println();
      
      // printint out the ship board
      for (int i = 0; i < SIZE; i++) {
         System.out.printf("%2d ", i + 1);
         for (int j = 0; j < SIZE; j++) {
            System.out.printf("%-4s", ship[i][j]);
         }
         System.out.print("     ");
         System.out.printf("%2d ", i + 1);
         // printing out the shot board
         for (int j = 0; j < SIZE; j++) {
            if (shots[i][j] == HIT || shots[i][j] == MISS) {
               System.out.printf("%-4s", shots[i][j]);
            } else {
               System.out.printf("%-4s", EMPTY);
            }
         }
         System.out.println();
      }
   }

   /*
    * Returns a new empty board array.
    * 
    * @return returns a 2-D array of characters filled with the EMPTY symbol.
    */
   public static char[][] emptyBoard() {
      // create a new board the size of a battleship board
      char[][] board = new char[SIZE][SIZE];
      // set each square to the EMPTY symbol
      for (int i = 0; i < SIZE; i++) {
         for (int j = 0; j < SIZE; j++) {
            board[i][j] = EMPTY;
         }
      }
      return board;
   }

   /*
    * Prints out the instructions of the game and how to play.
    */
   public static void printInstruction() {
      System.out.println("""
            +=======================================+
            |             INSTRUCTIONS              |
            |---------------------------------------|
            |  In this game of Battleship against   |
            |  a computer, the first to sink all    |
            |  opponent ships is victorious. The    |
            |  player can choose between an easy    |
            |  and normal difficulty. In easy, the  |
            |  AI will always attack random squares |
            |  on the board. In normal, once the AI |
            |  lands a random hit, it will begin to |
            |  attack squares around the hit. To    |
            |  start the game the player will place |
            |  5 ships of varying sizes on a 10x10  |
            |  board. An aircraft carrier is 5      |
            |  squares long, a  battleship is 4     |
            |  squares long, both a submarine and a |
            |  cruiser are 3 squares long and a     |
            |  destroyer is 2 squares long. These   |
            |  can be placed vertically or          |
            |  horizontally. Once in play, the      |
            |  player will choose to attack a       |
            |  square. The game will tell whether   |
            |  it was a hit or miss. The player     |
            |  and the AI will continue to          |
            |  alternate turns until the player or  |
            |  the AI's ships have  all been sunk.  |
            +=======================================+
            """);
   }
}