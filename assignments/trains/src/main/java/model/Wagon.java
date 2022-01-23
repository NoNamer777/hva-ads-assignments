package model;

public abstract class Wagon {
    private int wagonId;
    private Wagon previousWagon;
    private Wagon nextWagon;

    public Wagon (int wagonId) {
        this.wagonId = wagonId;
    }

    public Wagon getLastWagonAttached() {
        // find the last wagon of the row of wagons attached to this wagon
        // if no wagons are attached return this wagon
        Wagon wagon = this;
        if (wagon.hasNextWagon()) {
            wagon = wagon.getNextWagon();
            return wagon.getLastWagonAttached();
        }
        return this;
    }

    public void setNextWagon(Wagon nextWagon) {
        // when setting the next wagon, set this wagon to be previous wagon of next wagon
        if (nextWagon != null) {
            this.nextWagon = nextWagon;
            this.nextWagon.setPreviousWagon(this);
        } else {
            this.nextWagon = null;
        }
    }

    public Wagon getPreviousWagon() {
        return previousWagon;
    }

    public void setPreviousWagon(Wagon previousWagon) {
        this.previousWagon = previousWagon;
    }

    public Wagon getNextWagon() {
        return nextWagon;
    }

    public int getWagonId() {
        return wagonId;
    }

    public int getNumberOfWagonsAttached() {
        Wagon wagon = this;
        int numberOfWagons = 0;
        while (wagon.hasNextWagon()) {
            numberOfWagons++;
            wagon = wagon.getNextWagon();
        }
        return numberOfWagons;
    }

    public boolean hasNextWagon() {
        return !(nextWagon == null);
    }

    public boolean hasPreviousWagon() {
        return !(previousWagon == null);
    }

    @Override
    public String toString() {
        return String.format("[Wagon %d]", wagonId);
    }
}
