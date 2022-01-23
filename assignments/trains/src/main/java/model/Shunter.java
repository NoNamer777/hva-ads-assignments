package model;

public class Shunter {


    /* four helper methods than are used in other methods in this class to do checks */
    private static boolean isSuitableWagon(Train train, Wagon wagon) {
        // trains can only exist of passenger wagons or of freight wagons
        if (train.isPassengerTrain()) {
            return wagon instanceof PassengerWagon;
        } else if (train.isFreightTrain()) {
            return wagon instanceof FreightWagon;
        }
        return true;
    }

    private static boolean isSuitableWagon(Wagon one, Wagon two) {
        // passenger wagons can only be hooked onto passenger wagons
        if (one instanceof PassengerWagon) {
            return two instanceof PassengerWagon;
        } else {
            return two instanceof FreightWagon;
        }
    }

    private static boolean hasPlaceForWagons(Train train, Wagon wagon) {
        // the engine of a train has a maximum capacity, this method checks for a row of wagons
        if (hasPlaceForOneWagon(train, wagon)) {
            int trainAttachedWagons = train.getNumberOfWagons();
            int wagonAttachedWagons = wagon.getNumberOfWagonsAttached();
            int maxWagonsPossible = train.getEngine().getMaxWagons();

            return ((trainAttachedWagons + wagonAttachedWagons) < maxWagonsPossible);
        }
        return false;
    }

    private static boolean hasPlaceForOneWagon(Train train, Wagon wagon) {
        // the engine of a train has a maximum capacity, this method checks for one wagon
        if (!train.hasNoWagons()) {
            if (isSuitableWagon(train, wagon)) {
                int attachedWagons = train.getNumberOfWagons();
                int maxWagonsPossible = train.getEngine().getMaxWagons();

                return (attachedWagons < maxWagonsPossible);
            } else {
                return false;
            }
        }
        return true;
    }

    public static boolean hookWagonOnTrainRear(Train train, Wagon wagon) {
         /* check if Locomotive can pull new number of Wagons
         check if wagon is correct kind of wagon for train
         find the last wagon of the train
         hook the wagon on the last wagon (see Wagon class)
         adjust number of Wagons of Train */
        if (hasPlaceForWagons(train, wagon)) {
            if (train.hasNoWagons()) {
                train.setFirstWagon(wagon);
            } else {
                train.getFirstWagon().getLastWagonAttached().setNextWagon(wagon);
            }
            train.resetNumberOfWagons();
            return true;
        }
        return false;
    }

    public static boolean hookWagonOnTrainFront(Train train, Wagon wagon) {
        /* check if Locomotive can pull new number of Wagons
         check if wagon is correct kind of wagon for train
         if Train has no wagons hookOn to Locomotive
         if Train has wagons hookOn to Locomotive and hook firstWagon of Train to lastWagon attached to the wagon
         adjust number of Wagons of Train */
        if (hasPlaceForWagons(train, wagon)) {
            if (train.hasNoWagons()) {
                train.setFirstWagon(wagon);
            } else {
                Wagon firstWagon = train.getFirstWagon();
                detachAllFromTrain(train, firstWagon);
                train.setFirstWagon(wagon);
                train.getFirstWagon().getLastWagonAttached().setNextWagon(firstWagon);
            }
            train.resetNumberOfWagons();
            return true;
        }
        return false;
    }

    public static boolean hookWagonOnWagon(Wagon first, Wagon second) {
        /* check if wagons are of the same kind (suitable)
        * if so make second wagon next wagon of first */
        if (isSuitableWagon(first, second)) {
            first.setNextWagon(second);
        }
        return false;
    }


    public static boolean detachAllFromTrain(Train train, Wagon wagon) {
        /* check if wagon is on the train
         detach the wagon from its previousWagon with all its successor
         recalculate the number of wagons of the train */
        if (train.getPositionOfWagon(wagon.getWagonId()) > -1) {
            if (wagon.hasPreviousWagon()) {
                wagon.getPreviousWagon().setNextWagon(null);
                train.resetNumberOfWagons();
                return true;
            } else {
                train.setFirstWagon(null);
                train.resetNumberOfWagons();
                return true;
            }
        }
        return false;
    }

    public static boolean detachOneWagon(Train train, Wagon wagon) {
        /* check if wagon is on the train
         detach the wagon from its previousWagon and hook the nextWagon to the previousWagon
         so, in fact remove the one wagon from the train
        */
        if (train.getPositionOfWagon(wagon.getWagonId()) > -1) {
            if (wagon.hasPreviousWagon()) {
                wagon.getPreviousWagon().setNextWagon(wagon.getNextWagon());
                wagon.setNextWagon(null);
                train.resetNumberOfWagons();
                return true;
            } else {
                train.setFirstWagon(wagon.getNextWagon());
                wagon.setNextWagon(null);
                train.resetNumberOfWagons();
                return true;
            }
        }
        return false;
    }

    public static boolean moveAllFromTrain(Train from, Train to, Wagon wagon) {
        /* check if wagon is on train from
         check if wagon is correct for train and if engine can handle new wagons
         detach Wagon and all successors from train from and hook at the rear of train to
         remember to adjust number of wagons of trains */
        if (from.getPositionOfWagon(wagon.getWagonId()) > -1) {
            if (hasPlaceForWagons(to, wagon)) {
                return detachAllFromTrain(from, wagon) && hookWagonOnTrainRear(to, wagon);
            }
        }
        return false;
    }

    public static boolean moveOneWagon(Train from, Train to, Wagon wagon) {
        // detach only one wagon from train from and hook on rear of train to
        // do necessary checks and adjustments to trains and wagon
        return detachOneWagon(from, wagon) && hookWagonOnTrainRear(to, wagon);
    }
}
