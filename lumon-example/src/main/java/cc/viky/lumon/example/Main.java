package cc.viky.lumon.example;

import cc.viky.lumon.core.Terminal;

public class Main {
    public static void main(String[] args) throws Exception {
        Terminal terminal = Terminal.getInstance();
        terminal.enterAlternateScreen();
        terminal.enableRawMode();
        terminal.clear();
        terminal.moveCursor(0, 0);
        System.out.print("Raw mode works! Press Enter to exit.");
        terminal.flush();
        System.in.read();
        terminal.disableRawMode();
        terminal.exitAlternateScreen();
    }
}