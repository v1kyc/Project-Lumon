package  cc.viky.lumon.core;

import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;

public final class Terminal {

    private boolean rawMode = false;
    private int width = 80;
    private int height = 24;

    private static final Terminal INSTANCE = new Terminal();
    private static final boolean IS_UNIX = !System.getProperty("os.name").toLowerCase().contains("windows");

    // Native Bindings
    private MemorySegment originalTermios;
    private final Linker linker = Linker.nativeLinker();
    private final SymbolLookup libc = linker.defaultLookup();
    private final MethodHandle tcgetattr = linker.downcallHandle(
            libc.find("tcgetattr").orElseThrow(),
            FunctionDescriptor.of(ValueLayout.JAVA_INT,
                    ValueLayout.JAVA_INT,
                    ValueLayout.ADDRESS)
    );
    private final MethodHandle tcsetattr = linker.downcallHandle(
            libc.find("tcsetattr").orElseThrow(),
            FunctionDescriptor.of(ValueLayout.JAVA_INT,
                    ValueLayout.JAVA_INT,
                    ValueLayout.JAVA_INT,
                    ValueLayout.ADDRESS)
    );

    private Terminal() {}
    public static Terminal getInstance() {
        return INSTANCE;
    }

    public void clear() {
        System.out.print("\033[2J");
    }

    public void flush(){
        System.out.flush();
    }

    public void moveCursor(int row, int col) {
        System.out.print("\033[" + row + ";" + col + "H");
    }

    public void enterAlternateScreen() {
        System.out.print("\033[?1049h");
        Runtime.getRuntime().addShutdownHook(new Thread(this::exitAlternateScreen));
    }

    public void exitAlternateScreen() {
        System.out.print("\033[?1049l");
    }

    public void enableRawMode() {
        if (rawMode) { return; }

        if (IS_UNIX) {
            try (Arena arena = Arena.ofConfined()) {
                originalTermios = Arena.ofAuto().allocate(60);
                MemorySegment raw = arena.allocate(60);
                tcgetattr.invoke(0, originalTermios);
                raw.copyFrom(originalTermios);
                int lflag = raw.get(ValueLayout.JAVA_INT, 12);
                lflag &= ~(0x0002 | 0x0008);
                raw.set(ValueLayout.JAVA_INT, 12, lflag);
                tcsetattr.invoke(0, 0, raw);
            } catch (Throwable e) {
                throw new RuntimeException("Failed to enable raw mode", e);
            }
        }

        rawMode = true;
        Runtime.getRuntime().addShutdownHook(new Thread(this::disableRawMode));
    }

    public void disableRawMode() {
        if (!rawMode || originalTermios == null) return;

        if (IS_UNIX) {
            try {
                tcsetattr.invoke(0, 0, originalTermios);
            } catch (Throwable e) {
                throw new RuntimeException("Failed to restore terminal", e);
            }
        }

        rawMode = false;
    }
}