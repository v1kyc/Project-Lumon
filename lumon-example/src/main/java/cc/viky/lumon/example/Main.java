package cc.viky.lumon.example;

import cc.viky.lumon.core.Terminal;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Terminal terminal = new Terminal();
        Scanner scanner = new Scanner(System.in);

        terminal.enterAlternateScreen();
        terminal.clear();

        int selected = 0;
        String[] options = {"banana", "apple"};

        while (true) {
            terminal.clear();
            for (int i = 0; i < options.length; i++) {
                if (i == selected) {
                    System.out.println(options[i] + " <");
                } else {
                    System.out.println(options[i]);
                }
            }

            System.out.print("Choice (1/2, 0 to confirm): ");
            int choice = scanner.nextInt();

            if (choice == 0) break;
            selected = choice - 1;
        }

        terminal.exitAlternateScreen();
        System.out.println("You selected: " + options[selected]);
    }
}