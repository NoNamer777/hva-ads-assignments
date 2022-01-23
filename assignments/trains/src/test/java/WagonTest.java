import model.PassengerWagon;
import model.Wagon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WagonTest {
    private Wagon firstWagon;
    private Wagon secondWagon;
    private Wagon thirdWagon;

    @BeforeEach
    void setUp() {
        firstWagon = new PassengerWagon(1, 100);
        secondWagon = new PassengerWagon(2, 75);
        thirdWagon = new PassengerWagon(3, 50);
    }

    @Test
    void getLastWagonAttached() {
        Wagon expected = firstWagon;
        Wagon result = firstWagon.getLastWagonAttached();

        assertEquals(expected, result);

        firstWagon.setNextWagon(secondWagon);
        secondWagon.setNextWagon(thirdWagon);
        expected = thirdWagon;
        result = firstWagon.getLastWagonAttached();

        assertEquals(expected, result);
    }

    @Test
    void setNextWagon() {
        firstWagon.setNextWagon(secondWagon);

        Wagon expected1 = secondWagon;
        Wagon result1 = firstWagon.getNextWagon();

        assertEquals(expected1, result1);

        secondWagon.setNextWagon(null);
        Wagon result2 = secondWagon.getNextWagon();

        assertNull(result2);
    }

    @Test
    void getPreviousWagon() {
        firstWagon.setNextWagon(secondWagon);

        Wagon expected = firstWagon;
        Wagon result = secondWagon.getPreviousWagon();

        assertEquals(expected, result);
    }

    @Test
    void setPreviousWagon() {
        secondWagon.setPreviousWagon(firstWagon);

        Wagon expected = firstWagon;
        Wagon result = secondWagon.getPreviousWagon();

        assertEquals(expected, result);
    }

    @Test
    void getNextWagon() {
        firstWagon.setNextWagon(secondWagon);

        Wagon expected = secondWagon;
        Wagon result = firstWagon.getNextWagon();

        assertEquals(expected, result);
    }

    @Test
    void getWagonId() {
        int expected = 1;
        int result = firstWagon.getWagonId();

        assertEquals(expected, result);
    }

    @Test
    void getNumberOfWagonsAttached() {
        int expected1 = 0;
        int result1 = thirdWagon.getNumberOfWagonsAttached();

        assertEquals(expected1, result1);

        firstWagon.setNextWagon(secondWagon);
        secondWagon.setNextWagon(thirdWagon);

        int expected2 = 2;
        int result2 = firstWagon.getNumberOfWagonsAttached();

        assertEquals(expected2, result2);
    }

    @Test
    void hasNextWagon() {
        firstWagon.setNextWagon(secondWagon);
        boolean result1 = firstWagon.hasNextWagon();
        boolean result2 = thirdWagon.hasNextWagon();

        assertTrue(result1);
        assertFalse(result2);
    }

    @Test
    void hasPreviousWagon() {
        secondWagon.setPreviousWagon(firstWagon);
        boolean result1 = secondWagon.hasPreviousWagon();
        boolean result2 = thirdWagon.hasPreviousWagon();

        assertTrue(result1);
        assertFalse(result2);
    }
}