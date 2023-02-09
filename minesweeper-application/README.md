# MineSweeper Android Mobile Application

### Description:
- Individual project for Software Engineering course in Fall 2022.
- Mobile application implementation of classic Minesweeper game.

### Technical Specifications:
###### Languages and Tools
- Android, XML, Java, Android Studio

###### Recommended Versions
- Android Studio Chipmunk | 2021.2.1.
- API level 24.
- Android Virtual Device (AVD): Pixel 2 + Nougat 24 (Android 7.0).

### Requirements:
- Being able to randomly place 4 mines in the grid at the start of each game.
- Allowing the user to place flags on the not-yet-revealed cells.
- Allowing the user to reveal cells.
- Reporting the game result correctly (win or loss).
- If the user reveals a cell that does not have a mine, the total number of mines in its adjacent cells (i.e., the number of adjacent mines) must be displayed inside the revealed cell.
- After a cell is revealed, if the number of adjacent mines is 0, your app must transitively reveal all the adjacent cells as well.
