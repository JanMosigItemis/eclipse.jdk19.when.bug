package eclipse.jdk19.when.bug;

import static java.lang.foreign.MemorySegment.allocateNative;
import static java.lang.invoke.MethodType.methodType;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.foreign.MemorySession;
import java.lang.foreign.SymbolLookup;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.util.Optional;

class AppTest {
    private SymbolLookup libMock;
    private MethodHandle realMethodHandle;

    @BeforeEach
    void setUp() throws Exception {
        libMock = mock(SymbolLookup.class);

        var publicLookup = MethodHandles.publicLookup();
        var methodType = methodType(int.class);
        realMethodHandle = publicLookup.findVirtual(String.class, "length", methodType);

    }

    @Test
    void test_correct_stage_order() {
        var knownSymbol = "strlen";
        addKnownSymbol(knownSymbol);
        removeSymbol(knownSymbol);
    }

    private void addKnownSymbol(String symbolName) {
        var seg = allocateNative(1, MemorySession.global());
        when(libMock.lookup(symbolName)).thenReturn(Optional.of(seg));
    }

    private void removeSymbol(String symbolName) {
        when(libMock.lookup(symbolName)).thenReturn(Optional.empty());
    }
}
