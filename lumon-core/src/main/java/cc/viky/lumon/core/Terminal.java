package  cc.viky.lumon.core;

public final class Terminal {

    private boolean rawMode = false;
    private int width = 80;
    private int height = 24;

    private static final boolean IS_UNIX = !System.getProperty("os.name").toLowerCase().contains("windows");

    public void clear() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public void enterAlternateScreen() {
        System.out.print("\033[?1049h");
        System.out.flush();
    }

    public void exitAlternateScreen() {
        System.out.print("\033[?1049l");
        System.out.flush();
    }
}