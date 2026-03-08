package cc.viky.lumon.core;

import java.io.IOException;
import java.util.function.Consumer;

public class InputLoop {
    // TODO(inputloop): Implement InputLoop.java
    // - Handle Ctrl+C explicitly since raw mode disables OS interception
    // - Handle Ctrl+Z
    private volatile boolean running = false;
    private final Consumer<KeyEvent> onKey;

    public InputLoop(Consumer<KeyEvent> onKey) {
        this.onKey = onKey;
    }

    public void start() {
        running = true;
        Thread.ofVirtual().start(() -> {
            while (running) {
                try {
                    int b = System.in.read();
                    if (b == -1) break;

                    if (b == 27) {
                        // ESC — could be a sequence or standalone ESC
                        System.in.mark(4);
                        int next = System.in.read();
                        if (next != 91 && next != 79) {

                            boolean alt = next != -1;
                            int altKey = alt ? next : 0;
                            onKey.accept(new KeyEvent(27, false, alt, KeyEvent.SpecialKey.NONE));

                        } else {
                            // WARN: Ctrl+Z (byte 26) sends SIGTSTP on Unix, suspending the process even in raw mode.
                            // Proper fix requires signal handling (e.g. ignoring SIGTSTP via native call).
                            // For now, Ctrl+Z will suspend the app unexpectedly.
                            int fin = System.in.read();
                            KeyEvent.SpecialKey special = switch (next * 256 + fin) {
                                case 91 * 256 + 65 -> KeyEvent.SpecialKey.ARROW_UP;
                                case 91 * 256 + 66 -> KeyEvent.SpecialKey.ARROW_DOWN;
                                case 91 * 256 + 67 -> KeyEvent.SpecialKey.ARROW_RIGHT;
                                case 91 * 256 + 68 -> KeyEvent.SpecialKey.ARROW_LEFT;
                                case 79 * 256 + 80 -> KeyEvent.SpecialKey.F1;
                                case 79 * 256 + 81 -> KeyEvent.SpecialKey.F2;
                                case 79 * 256 + 82 -> KeyEvent.SpecialKey.F3;
                                case 79 * 256 + 83 -> KeyEvent.SpecialKey.F4;
                                default -> KeyEvent.SpecialKey.NONE;
                            };
                            onKey.accept(new KeyEvent(0, false, false, special));
                        }
                    } else {
                        boolean ctrl = b >= 1 && b <= 26 && b != 9 && b != 10 && b != 13;
                        onKey.accept(new KeyEvent(b, ctrl, false, KeyEvent.SpecialKey.NONE));
                    }
                } catch (IOException e) {
                    break;
                }
            }
        });
    }

    public void stop() {
        running = false;
    }
}