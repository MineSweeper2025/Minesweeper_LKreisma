# Minesweeper-LKreisma

### A Project made for the ITP lesson at HTL Steyr

#### It's a recreation of the classic Minesweeper game with JavaFx. 

---

## - User Interfaces

- When you first start the game, the class ```StartApplication.java``` is called, which loads
the FXML file ``settings-view.fxml`` to edit the difficulty of the game.

<img src="src/main/resources/htl/steyr/minesweeper_lkreisma/icons/minesweeper_settings.png" width="300" />

- With the Button "-Quit-" you can close the application.
- After clicking on "-START GAME-", a new window opens with the use of ```Field.java``` and the method ```createGameField()```.

<img src="src/main/resources/htl/steyr/minesweeper_lkreisma/icons/minesweeper_gameField.png" width="300" />

---

## - Controls

### 💣: 
  - bombs are placed randomly on the field
  - when opening a Cell there is the possibility that there will be a number shown (1-8 for how many bombs are next to it) or a bomb (game over)
  - if there are no bombs next to the opened cell, all surrounding cells will be opened

### 🚩: 
  - you can place a Flag on a cell using the RIGHT mouse button
  - this is useful to mark where you think a bomb is located
  - you can remove the flag by right-clicking again
  <br>
  
#### Game End:
- the game is over if you either open a bomb (you lose) or open all cells without bombs (you win)
- You can easily restart the game by clicking on the "-RESET-" button ont the top


---

## - How to Download and Run
1. Make sure you have Java 17 or higher installed on your machine.
2. Make shure you have the version Temurin Temurin-25 ```Eclipse Temurin-25.0.1``` installed.
3. Use an IDE like IntelliJ IDEA or Eclipse to import the project as a Maven project
4. Download the project file from the repository as a ZIP file and extract it 

    OR Clone the repository using Git:
    
    ````bash
    git clone

---

###### ©  Kreismayr Lorenz - Minesweeper