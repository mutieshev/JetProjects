package battleship;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Field field = new Field();
        Ship[] ships = Ship.values();
        Scanner scanner = new Scanner(System.in);
        field.printField();
        for (Ship ship : ships) {
            System.out.printf("Enter the coordinates of the %s (%d cells): ", ship.getName(), ship.getSize());
            while (true) {
                String coordinate1 = scanner.next();
                String coordinate2 = scanner.next();
                int row1 = coordinate1.charAt(0) - 65;
                int col1 = Integer.parseInt(coordinate1.substring(1)) - 1;
                int row2 = coordinate2.charAt(0) - 65;
                int col2 = Integer.parseInt(coordinate2.substring(1)) - 1;
                boolean isPLaced = field.placeShip(row1, col1, row2, col2, ship);
                if (isPLaced) {
                    field.printField();
                    break;
                }
            }
        }
    }
}