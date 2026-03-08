package cc.viky.lumon.core;

public record KeyEvent(
        int key,
        boolean ctrl,
        boolean alt,
        SpecialKey special
) {
    public enum SpecialKey {
        NONE,
        ARROW_UP, ARROW_DOWN, ARROW_LEFT, ARROW_RIGHT,
        F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12,
        HOME, END, PAGE_UP, PAGE_DOWN, INSERT, DELETE
    }

    public KeyEvent(int key, boolean ctrl) {
        this(key, ctrl, false, SpecialKey.NONE);
    }
}