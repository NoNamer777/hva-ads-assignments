import java.time.LocalTime;

public class FIFOCashier extends Cashier {
    public int checkoutTimePerCustomer;
    public int checkOutTimePerItem;

    public FIFOCashier(String name) {
        super(name);
        checkoutTimePerCustomer = 20;
        checkOutTimePerItem = 2;
    }

    /**
     * restart the state if simulation of the cashier to initial time
     * with empty queues
     * @param currentTime the time of which the cashier starts its shift
     */
    @Override
    public void reStart(LocalTime currentTime) {
        super.reStart(currentTime);
    }

    /**
     * calculate the expected nett checkout time of a customer with a given number of items
     * this may be different for different types of Cashiers
     * @param numberOfItems the number of goods the customer is going to purchase
     * @return the time in seconds the cashier will be expected to need in order to check out the current customer
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
     * @param customer the customer for which the waiting time is calculated
     * @return the expected time (in seconds) the customer needs to wait in order to be checked out
     */
    @Override
    public int expectedWaitingTime(Customer customer) {
        int expectedWaitingTime = 0;

        for (Customer queuedCustomer : getWaitingQueue()) {
            expectedWaitingTime += queuedCustomer.getActualCheckOutTime();
        }
        if (getCustomerZero() != null) {
            expectedWaitingTime += getCustomerZero().getActualCheckOutTime();
        }

        customer.setActualWaitingTime(expectedWaitingTime);
        return expectedWaitingTime;
    }

    /**
     * proceed the cashier's work until the given targetTime has been reached
     * this work may involve:
     * a) continuing or finishing the current customer(s) begin served
     * b) serving new customers that are waiting on the queue
     * c) sitting idle, taking a break until time has reached targetTime,
     *    after which new customers may arrive.
     * @param targetTime time until the cashier needs to work
     */
    @Override
    public void doTheWorkUntil(LocalTime targetTime) {
        int targetTimeSeconds = targetTime.toSecondOfDay();
        int currentTimeSeconds = getCurrentTime().toSecondOfDay();
        int availableTime = targetTimeSeconds - currentTimeSeconds;

        // Cashier idles
        if (getWaitingQueue().size() == 0 && getCustomerZero() == null) {
            setTotalIdleTime(getTotalIdleTime() + availableTime);
            setCurrentTime(targetTime);
        }

        // Cashier needs to work
        if (getWaitingQueue().size() > 0 || getCustomerZero() != null) {
            if (getCustomerZero() == null) setCustomerZero(getWaitingQueue().poll());

            int timePassed = 0, checkoutTimeNeeded = customerZero.getActualCheckOutTime();
            while (timePassed < availableTime) {
                // Customer is checked out completely
                if (checkoutTimeNeeded == 0) {
                    setCustomerZero(null);
                    setCurrentTime(getCurrentTime().plusSeconds(timePassed));
                    doTheWorkUntil(targetTime);
                    return;
                }

                checkoutTimeNeeded--;
                timePassed++;
            }

            // Cashier couldn't finish customer in time, save progress and check what to do next
            if (timePassed == availableTime) {
                customerZero.setActualCheckOutTime(checkoutTimeNeeded);
                setCurrentTime(targetTime);
            }
        }
    }
}
