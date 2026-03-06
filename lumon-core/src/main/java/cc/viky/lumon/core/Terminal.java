package  cc.viky.lumon.core;

import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.SymbolLookup;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;

public final class Terminal {

    private boolean rawMode = false;
    private int width = 80;
    private int height = 24;

    private static final boolean IS_UNIX = !System.getProperty("os.name").toLowerCase().contains("windows");
    public boolean isUnix() { return IS_UNIX; }

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
}