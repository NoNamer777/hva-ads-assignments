package model;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class WagonIterator implements Iterator<Wagon> {
    private Train train;
    private Wagon wagon;

    public WagonIterator(Train train) {
        this.train = train;
    }

    @Override
    public boolean hasNext() {
        if (train.hasNoWagons() || (wagon != null && !wagon.hasNextWagon())) {
            return false;
        }
        return (train.isPassengerTrain() || train.isFreightTrain());
    }

    @Override
    public Wagon next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        if (wagon == null) {
            wagon = train.getFirstWagon();
        } else {
            wagon = wagon.getNextWagon();
        }
        return wagon;
    }
}
