package tictactoe;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    static void printTable(StringBuilder str) {
        System.out.println("---------");
        for(int i = 0; i < 9;) {
            System.out.printf("| %c", str.charAt(i++));
            System.out.printf(" %c ", str.charAt(i++));
            System.out.printf("%c |\n", str.charAt(i++));
        }
        System.out.println("---------");
    }

    static boolean check(StringBuilder str) {
        //row check
        for (int i = 0; i < 9; i += 3) {
            if ((str.charAt(i) == str.charAt(i+1))
                    && (str.charAt(i+1) == str.charAt(i+2))) {
                if (str.charAt(i) == 'X') {
                    System.out.println("X wins");
                    return true;
                } else if (str.charAt(i) == 'O') {
                    System.out.println("O wins");
                    return true;
                }
            }
        }
        //column check
        for (int i = 0; i < 3; i++) {
            if ((str.charAt(i) == str.charAt(i+3))
                    && (str.charAt(i+3) == str.charAt(i+6))) {
                if (str.charAt(i) == 'X') {
                    System.out.println("X wins");
                    return true;
                } else if (str.charAt(i) == 'O') {
                    System.out.println("O wins");
                    return true;
                }
            }
        }
        //diagonal1 check
        if ((str.charAt(0) == str.charAt(4))
                && (str.charAt(4) == str.charAt(8))) {
            if (str.charAt(0) == 'X') {
                System.out.println("X wins");
                return true;
            } else if (str.charAt(0) == 'O') {
                System.out.println("O wins");
                return true;
            }
        }
        //diagonal2 check
        else if ((str.charAt(2) == str.charAt(4))
                && (str.charAt(4) == str.charAt(6))) {
            if (str.charAt(2) == 'X') {
                System.out.println("X wins");
                return true;
            } else if (str.charAt(2) == 'O') {
                System.out.println("O wins");
                return true;
            }
        }
        for (int i = 0; i < 9; i++) {
            if (str.charAt(i) != '_') {
                continue;
            }
            else if (i == 8) {
                System.out.println("Draw");
                return true;
            }
            else {
                return false;
            }
        }
        return false;
    }

    static int numOffset(int x, int y) {
        int offset = 0;
        if (y == 1) {
            offset = x;
        }
        else if (y == 2) {
            offset = y * 2 + x - 1;
        }
        else if (y == 3) {
            offset = y * 2 + x;
        }
        return offset;
    }

    static StringBuilder nTable(int offset, StringBuilder game, boolean isX) {
        if (isX) {
            game.insert(offset, 'X');
            game.delete(offset - 1, offset);
            return game;
        }
        else {
            game.insert(offset, 'O');
            game.delete(offset - 1, offset);
            return game;
        }
    }

    static boolean cellCheck(int offset, StringBuilder game) {
        if (game.charAt(offset-1) != '_') {
            System.out.println("This cell is occupied! Choose another one!");
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        StringBuilder game = new StringBuilder("_________");
        printTable(game);
        int x, y, offset = 0;
        boolean cell, win = false, isX = true;
        while (!win) {
            try {
                System.out.print("Enter the coordinates: ");
                y = scan.nextInt(); x = scan.nextInt();
                if ((y >= 1 && y <= 3)
                        && (x >= 1 && x <= 3)) {
                    offset = numOffset(x, y);
                    cell = cellCheck(offset, game);
                    if (cell) {
                        game = nTable(offset, game, isX);
                        isX = !isX;
                        printTable(game);
                        win = check(game);
                    }
                }
                else {
                    System.out.println("Coordinates should be from 1 to 3!");
                }
            } catch (InputMismatchException e) {
                System.out.println("You should enter numbers!");
                scan.nextLine();
            }
        }
    }
}