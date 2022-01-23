import java.time.LocalTime;
import java.util.*;

/**
 * Supermarket Customer check-out and Cashier simulation
 * @author  hbo-ict@hva.nl
 */
public abstract class Cashier {

    private String name;                    // name of the cashier, for results identification
    private int numberOfCustomers;          // keeps track of the number of customers the cashier has handled
    private double avgCheckoutTime;         // keeps track of the average checkout time per customer
    private double avgWaitingTime;          // keeps track of the average waiting time per customer
    private List<Double> avgWaitingTimes;          // keeps track of the average waiting time per customer
    private double maxWaitingTime;          /* keeps track of the longest time a customer needed to wait
                                             * before it could be handled */
    private List<Customer> uniqueCustomers; // keeps track of the unique customers
    protected Queue<Customer> waitingQueue; // queue of waiting customers
    protected LocalTime currentTime;        // tracks time for the cashier during simulation
    protected int totalIdleTime;            // tracks cumulative seconds when there was no work for the cashier
    protected int maxQueueLength;           /* tracks the maximum number of customers at the cashier at any time
                                             * during simulation. Includes both waiting customers and the customer being served */
    public Customer customerZero;

    protected Cashier(String name) {
        this.name = name;
        this.waitingQueue = new ArrayDeque<>();
        this.uniqueCustomers = new ArrayList<>();
        avgWaitingTimes = new ArrayList<>();
    }

    /**
     * restart the state if simulation of the cashier to initial time
     * with empty queues
     * @param currentTime
     */
    public void reStart(LocalTime currentTime) {
        this.waitingQueue.clear();
        this.currentTime = currentTime;
        this.totalIdleTime = 0;
        this.maxQueueLength = 0;
        // TODO[DONE]: you may need to override this method in sub-classes
    }

    /**
     * calculate the expected nett checkout time of a customer with a given number of items
     * this may be different for different types of Cashiers
     * @param numberOfItems
     * @return
     */
    public abstract int expectedCheckOutTime(int numberOfItems);

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
    public abstract int expectedWaitingTime(Customer customer);

    /**
     * proceed the cashier's work until the given targetTime has been reached
     * this work may involve:
     * a) continuing or finishing the current customer(s) begin served
     * b) serving new customers that are waiting on the queue
     * c) sitting idle, taking a break until time has reached targetTime,
     *      after which new customers may arrive.
     * @param targetTime
     */
    public abstract void doTheWorkUntil(LocalTime targetTime);

    /**
     * add a new customer to the queue of the cashier
     * the position of the new customer in the queue will depend on the priority configuration of the queue
     * @param customer
     */
    // TODO[DONE] add the customer to the queue of the cashier (if check-out is required)
    public void add(Customer customer) {
        getWaitingQueue().add(customer);
        customer.setActualCheckOutTime(expectedCheckOutTime(customer.getNumberOfItems()));
        numberOfCustomers++;

        if (getCustomerZero() == null && getWaitingQueue().size() > getMaxQueueLength()) {
            maxQueueLength++;
        } else if (getCustomerZero() != null && getWaitingQueue().size() + 1 > getMaxQueueLength()) {
            maxQueueLength++;
        }
        addUniqueCustomer(customer);
    }

    // TODO implement relevant overrides and/or local classes to be able to
    //  print Cashiers and/or use them in sets, maps and/or priority queues.

    public int getTotalIdleTime() {
        return totalIdleTime;
    }

    public LocalTime getCurrentTime() {
        return currentTime;
    }

    public String getName() {
        return name;
    }

    public int getNumberOfCustomers() {
        return numberOfCustomers;
    }

    public int getMaxQueueLength() {
        return maxQueueLength;
    }

    public void setCurrentTime(LocalTime currentTime) {
        this.currentTime = currentTime;
    }

    public void setTotalIdleTime(int totalIdleTime) {
        this.totalIdleTime = totalIdleTime;
    }

    public Queue<Customer> getWaitingQueue() {
        return waitingQueue;
    }

    public Customer getCustomerZero() {
        return customerZero;
    }

    public void setCustomerZero(Customer customer) {
        customerZero = customer;
    }

    private void addUniqueCustomer(Customer customer) {
        if (!uniqueCustomers.contains(customer)) {
            uniqueCustomers.add(customer);
        }

        calculateAvgCheckoutTime();
        calculateAvgWaitingTime();
    }

    private void calculateAvgCheckoutTime() {
        double avgCheckoutTime = 0;
        for (Customer customer : uniqueCustomers) {
            avgCheckoutTime += customer.getActualCheckOutTime();
        }
        avgCheckoutTime /= uniqueCustomers.size();
        this.avgCheckoutTime = avgCheckoutTime;
    }

    private void calculateAvgWaitingTime() {
        double longestWaitingTime = 0;
        double avgWaitingTime = 0;

        for (Customer customer : uniqueCustomers) {
            // Finds the longestTime a customer needed to wait in queue
            if (longestWaitingTime < expectedWaitingTime(customer)) {
                 longestWaitingTime = customer.getActualWaitingTime();
            }
            // Adds the expected waiting time of the selected unique customer
            avgWaitingTime += customer.getActualWaitingTime();
        }
        avgWaitingTime /= uniqueCustomers.size();
        avgWaitingTimes.add(avgWaitingTime);

        if (maxWaitingTime < longestWaitingTime)
            maxWaitingTime = longestWaitingTime;

        for (double waitingTimes : avgWaitingTimes) {
            this.avgWaitingTime += waitingTimes;
        }

        this.avgWaitingTime /= avgWaitingTimes.size();
    }

    public double getAvgCheckoutTime() {
        return avgCheckoutTime;
    }

    public double getAvgWaitingTime() {
        return avgWaitingTime;
    }

    public double getMaxWaitingTime() {
        return maxWaitingTime;
    }

    public List<Customer> getUniqueCustomers() {
        return uniqueCustomers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cashier)) return false;
        Cashier cashier = (Cashier) o;
        return Objects.equals(name, cashier.name);
    }
}
