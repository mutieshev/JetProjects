package advisor;

import advisor.commands.CommandType;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static boolean isAuthorized = false;
    public static int PAGES = 5;
    public static Viewers current;
    public static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException, InterruptedException, InstantiationException, IllegalAccessException {
        while (true) {
            String command = scanner.next();
            CommandType.getTemplateByCode(command).getCommandTemplate().getTemplate();
        }
    }
}