package cc.viky.lumon.example;

import cc.viky.lumon.core.Terminal;

public class Main {
    public static void main(String[] args) {
        Terminal terminal = new Terminal();
        System.out.println("Is Unix: " + terminal.isUnix());
    }
}