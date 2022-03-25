package battleship;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Field[] game = new Field[2];
        int player = 0;
        boolean isWin = false;
        Ship[] ships = Ship.values();
        Scanner scanner = new Scanner(System.in);
        for (int i = 0; i < 2; i++) {
            game[i] = new Field();
            System.out.println("Player " + (i + 1) + ", place your ships on the game field");
            game[i].printField();
            for (Ship ship : ships) {
                System.out.printf("\nEnter the coordinates of the %s (%d cells):\n\n", ship.getName(), ship.getSize());
                while (true) {
                    String coordinate1 = scanner.next();
                    String coordinate2 = scanner.next();
                    int row1 = coordinate1.charAt(0) - 65;
                    int col1 = Integer.parseInt(coordinate1.substring(1)) - 1;
                    int row2 = coordinate2.charAt(0) - 65;
                    int col2 = Integer.parseInt(coordinate2.substring(1)) - 1;
                    boolean isPLaced = game[i].placeShip(row1, col1, row2, col2, ship);
                    if (isPLaced) {
                        game[i].printField();
                        break;
                    }
                }
            }
            System.out.println("\nPress Enter and pass the move to another player");
            scanner.nextLine();
            scanner.nextLine();
        }

        while (!isWin) {
            switch (player) {
                case 0:
                    game[1].printHiddenField();
                    System.out.println("---------------------");
                    game[0].printField();
                    System.out.println("\nPlayer 1, it's your turn:");
                    while (game[1].getHealth() != 0) {
                        String shot = scanner.next();
                        int x = Integer.parseInt(shot.substring(1)) - 1;
                        int y = shot.charAt(0) - 65;
                        boolean isShot = game[1].shot(x, y);
                        if (isShot) {
                            break;
                        }
                    }
                    if (game[1].getHealth() == 0) {
                        isWin = true;
                        break;
                    }
                    player = 1;
                    System.out.println("Press Enter and pass the move to another player");
                    scanner.nextLine();
                    scanner.nextLine();
                    break;
                case 1:
                    game[0].printHiddenField();
                    System.out.println("---------------------");
                    game[1].printField();
                    System.out.println("\nPlayer 2, it's your turn:");
                    while (game[0].getHealth() != 0) {
                        String shot = scanner.next();
                        int x = Integer.parseInt(shot.substring(1)) - 1;
                        int y = shot.charAt(0) - 65;
                        boolean isShot = game[0].shot(x, y);
                        if (isShot) {
                            break;
                        }
                    }
                    if (game[0].getHealth() == 0) {
                        isWin = true;
                        break;
                    }
                    player = 0;
                    System.out.println("Press Enter and pass the move to another player");
                    scanner.nextLine();
                    scanner.nextLine();
            }
        }
    }
}