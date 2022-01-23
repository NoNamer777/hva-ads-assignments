import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TrainTest {
    private Train thomas;
    private Locomotive loc;
    private Wagon firstWagon;
    private Wagon secondWagon;
    private Wagon thirdWagon;


    @BeforeEach
    void setUp() {
        loc = new Locomotive(1, 2);
        thomas = new Train(loc, "Zaandam", "Amsterdam");
        firstWagon = new PassengerWagon(8, 100);
        secondWagon = new PassengerWagon(5, 75);
        thirdWagon = new PassengerWagon(13, 125);

        thomas.setFirstWagon(firstWagon);
        thomas.resetNumberOfWagons();
    }

    @Test
    void getFirstWagon() {
        Wagon expected = firstWagon;
        Wagon result = thomas.getFirstWagon();

        assertEquals(expected, result);
    }

    @Test
    void setFirstWagon() {
        thomas.setFirstWagon(secondWagon);

        Wagon expected = secondWagon;
        Wagon result = thomas.getFirstWagon();

        assertEquals(expected, result);
    }

    @Test
    void resetNumberOfWagons() {
        thomas.resetNumberOfWagons();

        int expected = 1;
        int result = thomas.getNumberOfWagons();

        assertEquals(expected, result);
    }

    @Test
    void getNumberOfWagons() {
        // Because in the "set before" the number of wagons is not reset
        int expected = 1;
        int result = thomas.getNumberOfWagons();

        assertEquals(expected, result);
    }

    @Test
    void hasNoWagons() {
        boolean result = thomas.hasNoWagons();

        // Is false because he has 1 wagon
        assertFalse(result);
    }

    @Test
    void isPassengerTrain() {
        boolean result = thomas.isPassengerTrain();

        // Is true because he has 1 wagon that is an passenger wagon
        assertTrue(result);
    }

    @Test
    void isFreightTrain() {
        boolean result = thomas.isFreightTrain();

        // Is false because he has 1 wagon that is an passenger wagon
        assertFalse(result);
    }

    @Test
    void getPositionOfWagon() {
        int expected1 = 1;
        int result1 = thomas.getPositionOfWagon(8);

        assertEquals(expected1, result1);

        thomas.getFirstWagon().setNextWagon(secondWagon);

        int expected2 = 2;
        int result2 = thomas.getPositionOfWagon(5);

        assertEquals(expected2, result2);

        int expected3 = -1;
        int result3 = thomas.getPositionOfWagon(13);

        assertEquals(expected3, result3);
    }

    @Test
    void getWagonOnPosition() {
        Wagon expected1 = firstWagon;
        Wagon result1 = thomas.getWagonOnPosition(1);

        assertEquals(expected1, result1);

        Wagon result2 = thomas.getWagonOnPosition(-1);
        assertNull(result2);

        Wagon result3 = thomas.getWagonOnPosition(3);
        assertNull(result3);
    }

    @Test
    void getNumberOfSeats() {
        int expected1 = 100;
        int result1 = thomas.getNumberOfSeats();

        assertEquals(expected1, result1);

        Train tom = new Train(new Locomotive(4, 2), "Amsterdam", "Rotterdam");
        tom.setFirstWagon(new FreightWagon(10, 1000));

        int expected2 = 0;
        int result2 = tom.getNumberOfSeats();

        assertEquals(expected2, result2);
    }

    @Test
    void getTotalMaxWeight() {
        int expected1 = 0;
        int result1 = thomas.getTotalMaxWeight();

        assertEquals(expected1, result1);

        Train tom = new Train(new Locomotive(4, 2), "Amsterdam", "Rotterdam");
        tom.setFirstWagon(new FreightWagon(10, 1000));

        int expected2 = 1000;
        int result2 = tom.getTotalMaxWeight();

        assertEquals(expected2, result2);
    }

    @Test
    void getEngine() {
        Locomotive expected = loc;
        Locomotive result = thomas.getEngine();

        assertEquals(expected, result);
    }
}