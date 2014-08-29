package nl.michelgreve.plugins;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Assert;
import org.junit.Test;

public class PairTest {
    private static final String FIRST = "First";
    private static final String SECOND = "Second";

    private static final Pair<String, String> PAIR = new Pair<String, String>(FIRST, SECOND);

    @Test
    public void theHashCode() {
        assertNotEquals(0, PAIR.hashCode());
    }

    @Test
    public void theEquals() {
        final Pair<String, String> PAIR_1 = new Pair<String, String>(FIRST, SECOND);
        final Pair<String, String> PAIR_2 = new Pair<String, String>(SECOND, FIRST);
        assertEquals(PAIR, PAIR_1);
        assertNotEquals(PAIR, PAIR_2);
    }

    @Test
    public void theToString() {
        Assert.assertEquals(-1, PAIR.toString().indexOf("@"));
    }

    @Test
    public void getFirst() {
        assertEquals(FIRST, PAIR.getFirst());
    }

    @Test
    public void getSecond() {
        assertEquals(SECOND, PAIR.getSecond());
    }
}
