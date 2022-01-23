import java.util.PriorityQueue;

public class PriorityCashier extends FIFOCashier {
    public int maxNumPriorityItems;

    public PriorityCashier(String name, int maxNumPriorityItems) {
        super(name);
        this.maxNumPriorityItems = maxNumPriorityItems;
        this.waitingQueue = new PriorityQueue<>();
    }

    /**
     * calculate the expected nett checkout time of a customer with a given number of items
     * this may be different for different types of Cashiers
     * @param numberOfItems the number of items the customer is going to purchase
     * @return the time (in seconds) the cashier is going to need to checkout the given customer
     */
    @Override
    public int expectedCheckOutTime(int numberOfItems) {
        if (numberOfItems == 0) return 0;
        return checkoutTimePerCustomer + (numberOfItems * checkOutTimePerItem);
    }

    /**
     * calculate the currently expected waiting time of a given customer for this cashier.
     * this may depend on:
     * a) the type of cashier,
     * b) the remaining work of the cashier's current customer(s) being served
     * c) the position that the given customer may obtain in the queue
     * d) and the workload of the customers in the waiting queue in front of the given customer
     * @param customer
     * @return
     */
    @Override
    public int expectedWaitingTime(Customer customer) {
        int expectedWaitingTime = 0;

        if (waitingQueue.size() > 0) {
            for (Customer queuedCustomer : waitingQueue) {
                /* New Customer has priority when it has less then or equal to 5 items,
                 * but not to the other priority customers already in queue */
                if (customer.getNumberOfItems() > maxNumPriorityItems
                        || queuedCustomer.getNumberOfItems() <= maxNumPriorityItems) {
                    expectedWaitingTime += queuedCustomer.getActualCheckOutTime();
                }
            }
        }

        if (getCustomerZero() != null) {
            expectedWaitingTime += getCustomerZero().getActualCheckOutTime();
        }

        return expectedWaitingTime;
    }
}
