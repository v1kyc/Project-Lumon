package cc.viky.lumon.core;

public final class Enviroment {

    private Enviroment() {}

    public static final boolean IS_WINDOWS = System.getProperty("os.name").toLowerCase().contains("windows");
    public static final boolean IS_UNIX = !IS_WINDOWS;
    public static final boolean IS_MAC = System.getProperty("os.name").toLowerCase().contains("mac");

}