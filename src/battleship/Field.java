package battleship;

import java.util.Arrays;

public class Field {
    private final char[][] field;
    private int health;

    public Field() {
        this.health = 0;
        this.field = new char[10][10];
        for (char[] chars : this.field) {
            Arrays.fill(chars, '~');
        }
    }

    public int getHealth() {
        return health;
    }

    public void printField() {
        System.out.print("\n ");
        for (int i = 0; i < this.field.length; i++) {
            System.out.printf(" %d", i + 1);
        }
        System.out.println();
        char row;
        for (int i = 0; i < field.length; i++) {
            row = (char) ('A' + i);
            System.out.printf("%c", row);
            for (int j = 0; j < field[i].length; j++) {
                System.out.printf(" %c", this.field[i][j]);
            }
            System.out.println();
        }
    }

    public void printHiddenField() {
        System.out.print(" ");
        for (int i = 0; i < this.field.length; i++) {
            System.out.printf(" %d", i + 1);
        }
        System.out.println();
        char row;
        for (int i = 0; i < field.length; i++) {
            row = (char) ('A' + i);
            System.out.printf("%c", row);
            for (int j = 0; j < field[i].length; j++) {
                if (this.field[i][j] == 'O') {
                    System.out.print(" ~");
                }
                else {
                    System.out.printf(" %c", this.field[i][j]);
                }
            }
            System.out.println();
        }
    }

    public boolean placeShip(int startRow, int startColumn, int endRow, int endColumn, Ship ship) {
        if (startRow != endRow && startColumn != endColumn) {
            System.out.println("\nError! Wrong ship location! Try again:\n");
            return false;
        }
        else if (startRow == endRow) {
            return this.placeShipHorizontally(startRow, startColumn, endColumn, ship);
        }
        else if (startColumn == endColumn) {
            return this.placeShipVertically(startColumn, startRow, endRow, ship);
        }
        return true;
    }

    public boolean placeShipHorizontally(int row, int startColumn, int endColumn, Ship ship) {
        int temp;
        if (startColumn > endColumn) {
            temp = startColumn;
            startColumn = endColumn;
            endColumn = temp;
        }
        if ((endColumn - startColumn) + 1 != (ship.getSize())) {
            System.out.printf("\nError! Wrong length of the %s! Try again:\n\n", ship.getName());
            return false;
        }

        for (int r = row - 1; r <= row + 1; r++) {
            if (r < 0 || r > 9) {
                continue;
            }
            for (int c = startColumn - 1; c <= endColumn + 1; c++) {
                if (c < 0 || c > 9) {
                    continue;
                }
                if (this.field[r][c] == 'O') {
                    System.out.println("\nError! You placed it too close to another one. Try again:\n");
                    return false;
                }
            }
        }

        for (int col = startColumn; col <= endColumn; col++) {
            this.field[row][col] = 'O';
        }
        this.health += ship.getSize();
        return true;
    }

    public boolean placeShipVertically(int col, int startRow, int endRow, Ship ship) {
        int temp;
        if (startRow > endRow) {
            temp = startRow;
            startRow = endRow;
            endRow = temp;
        }
        if ((endRow - startRow) + 1 != (ship.getSize())) {
            System.out.printf("\nError! Wrong length of the %s! Try again:\n\n", ship.getName());
            return false;
        }

        for (int c = col - 1; c <= col + 1; c++) {
            if (c < 0 || c > 9) {
                continue;
            }
            for (int r = startRow - 1; r <= endRow + 1; r++) {
                if (r < 0 || r > 9) {
                    continue;
                }
                if (this.field[r][c] == 'O') {
                    System.out.println("\nError! You placed it too close to another one. Try again:\n");
                    return false;
                }
            }
        }

        for (int row = startRow; row <= endRow; row++) {
            this.field[row][col] = 'O';
        }
        this.health += ship.getSize();
        return true;
    }

    public boolean shot(int x, int y) {
        if ((x < 0 || x > 9) || (y < 0 || y > 9)) {
            System.out.println("\nError! You entered the wrong coordinates! Try again:\n");
            return false;
        }

        if (field[y][x] == 'O') {
            if (health != 0) {
                field[y][x] = 'X';
                health--;
                printHiddenField();
                System.out.println("\nYou hit a ship!");
                return true;
            }
            else {
                field[y][x] = 'X';
                health--;
                printHiddenField();
                System.out.println("You sank the last ship. You won. Congratulations!");
                return true;
            }
        }
        else if (field[y][x] == '~') {
            field[y][x] = 'M';
            printHiddenField();
            System.out.println("\nYou missed!");
            return true;
        }
        else if (field[y][x] == 'X') {
            printHiddenField();
            System.out.print("\nYou hit a ship!");
            return true;
        }
        return false;
    }
}