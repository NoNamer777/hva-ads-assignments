package model;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Train implements Iterable<Wagon> {
    private Locomotive engine;
    private Wagon firstWagon;
    private String destination;
    private String origin;
    private int numberOfWagons;

    public Train(Locomotive engine, String origin, String destination) {
        this.engine = engine;
        this.destination = destination;
        this.origin = origin;
    }

    public Wagon getFirstWagon() {
        return firstWagon;
    }

    public void setFirstWagon(Wagon firstWagon) {
        this.firstWagon = firstWagon;
    }

    public void resetNumberOfWagons() {
       /*  when wagons are hooked to or detached from a train,
         the number of wagons of the train should be reset
         this method does the calculation */
       int numberOfWagons = 0;
       if (!this.hasNoWagons()) {
           numberOfWagons = 1 + getFirstWagon().getNumberOfWagonsAttached();
       }
       this.numberOfWagons = numberOfWagons;
    }

    public int getNumberOfWagons() {
        return numberOfWagons;
    }

    /* three helper methods that are useful in other methods */
    public boolean hasNoWagons() {
        return (firstWagon == null);
    }

    public boolean isPassengerTrain() {
        return firstWagon instanceof PassengerWagon;
    }

    public boolean isFreightTrain() {
        return firstWagon instanceof FreightWagon;
    }

    public int getPositionOfWagon(int wagonId) {
        // find a wagon on a train by id, return the position (first wagon had position 1)
        // if not found, than return -1
        int wagonPosition = -1;
        if (!hasNoWagons()) {
            wagonPosition = 0;
            for (Wagon wagon : this) {
                wagonPosition++;
                if (wagon.getWagonId() == wagonId) {
                    return wagonPosition;
                }
            }
            wagonPosition = -1;
        }
        return wagonPosition;
    }

    public Wagon getWagonOnPosition(int position) throws IndexOutOfBoundsException {
        /* find the wagon on a given position on the train
         position of wagons start at 1 (firstWagon of train)
         use exceptions to handle a position that does not exist */
        try {
            if (position <= 0) {
                throw new IndexOutOfBoundsException();
            }
            Wagon wagon = getFirstWagon();
            int currentPosition = 1;
            if (wagon.hasNextWagon() && position > 1) {
                while (wagon.hasNextWagon()) {
                    wagon = wagon.getNextWagon();
                    currentPosition++;

                    if (currentPosition == position) {
                        break;
                    } else if (currentPosition < position && !wagon.hasNextWagon()) {
                        throw new IndexOutOfBoundsException();
                    }
                }
            } else if (!wagon.hasNextWagon() && position > 1) {
                throw new IndexOutOfBoundsException();
            }
            return wagon;
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    public int getNumberOfSeats() {
        /* give the total number of seats on a passenger train
         for freight trains the result should be 0 */
        int numberOfSeats = 0;
        if (isPassengerTrain()) {
            for (Wagon wagon : this) {
                numberOfSeats += ((PassengerWagon) wagon).getNumberOfSeats();
            }
        }
        return numberOfSeats;
    }

    public int getTotalMaxWeight() {
        /* give the total maximum weight of a freight train
         for passenger trains the result should be 0 */
        int totalMaxWeight = 0;
        if (isFreightTrain()) {
            for (Wagon wagon : this) {
                totalMaxWeight += ((FreightWagon) wagon).getMaxWeight();
            }
        }
        return totalMaxWeight;
    }

    public Locomotive getEngine() {
        return engine;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(engine.toString());
        for (Wagon wagon : this) {
            result.append(wagon.toString());
        }
        result.append(String.format(" with %d wagons and %d seats from %s to %s", numberOfWagons, getNumberOfSeats(), origin, destination));
        return result.toString();
    }

    @Override
    public Iterator iterator() {
        return new WagonIterator(this);
    }
}
