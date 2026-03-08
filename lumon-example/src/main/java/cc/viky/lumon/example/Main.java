package cc.viky.lumon.example;

import cc.viky.lumon.core.InputLoop;
import cc.viky.lumon.core.KeyEvent;
import cc.viky.lumon.core.Terminal;

public class Main {
    public static void main(String[] args) throws Exception {
        Terminal terminal = Terminal.getInstance();
        terminal.enterAlternateScreen();
        terminal.enableRawMode();
        terminal.clear();
        terminal.moveCursor(0, 0);
        System.out.print("Press any key (q to quit):");
        terminal.flush();

        InputLoop[] inputLoop = new InputLoop[1];
        inputLoop[0] = new InputLoop(event -> {
            terminal.clear();
            terminal.moveCursor(1, 0);
            //WARN(InputLoop.java) doesn't handle Ctrl+Z escape on Unix
            System.out.print("Key pressed: " +
                    (event.special() != KeyEvent.SpecialKey.NONE
                            ? event.special()
                            : event.key()) +
                    " ctrl: " + event.ctrl() +
                    " alt: " + event.alt());
            terminal.flush();
            if (event.key() == 113) {
                inputLoop[0].stop();
                terminal.disableRawMode();
                terminal.exitAlternateScreen();
                System.exit(0);
            }
        });

        inputLoop[0].start();
        Thread.currentThread().join();
    }
}