## Android App - Minesweeper
    Java, XML, Android Studio

### Overview
- An Android mobile app implementation of the Minesweeper game.

- Individual project for a Software Engineering course in Fall 2022.

<br>

### Functional Requirements

- User starts a `New Game` and 4 mines are randomly hidden in the grid of unrevealed cells.

- With `Flag` selection, user may place flags on the unrevealed cells.

- With `Dig` selection, user may reveal cells.

- Reveal a cell without a mine, the cell shows the total number of mines in its adjacent cells (i.e., the number of adjacent mines).

- Reveal a cell without a mine and zero adjacent mines, then the app transitively reveals all adjacent cells with these conditions.

- Reveal a cell with a mine (loss) or flag all 4 mines (win), and user shall see the game result, the play time duration, and an option for `New Game`.

<br>

### Technical Specifications
- Language: Java
- Database: XML
- IDE: Android Studio Chipmunk (2021.2.1)
- API level 24
- Operating System: Android Mobile
- Virtual Device: Pixel 2 + Nougat 24 (Android 7.0).

<br>

## Resources
[Example Minesweeper Game](https://minesweeper.online/)
