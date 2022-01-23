import utils.XMLParser;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.time.LocalTime;
import java.util.*;

/**
 * Supermarket Customer check-out and Cashier simulation
 * @author  hbo-ict@hva.nl
 */
public class Customer implements Comparable<Customer> {
    private LocalTime queuedAt;      // time of arrival at cashier
    private String zipCode;          // zip-code of the customer
    private Set<Purchase> items;     // items procured by customer
    private int actualWaitingTime;   // actual waiting time in seconds before check-out
    private int actualCheckOutTime;  // actual check-out time at cashier in seconds
    private Cashier checkOutCashier; // cashier that the customer has chosen for check-out

    public Customer(LocalTime queuedAt, String zipCode) {
        this.queuedAt = queuedAt;
        this.zipCode = zipCode;
        // TODO[DONE]: initialize an empty set of purchased items
        items = new HashSet<>();
    }

    /**
     * calculate the total number of items purchased by this customer
     * @return the total number of items purchased by this customer
     */
    public int getNumberOfItems() {
        int numItems = 0;

        // TODO[DONE]: Calculate the total number of items
        for (Purchase purchase : this.getItems()) {
            numItems += purchase.getAmount();
        }
        return numItems;
    }

    public double calculateTotalBill() {
        double totalBill = 0.0;

        // TODO[DONE]: Calculate the total cost of all items
        for (Purchase purchase : this.getItems()) {
            double productPrice = purchase.getProduct().getPrice();
            totalBill += (productPrice * purchase.getAmount());
        }
        return totalBill;
    }

    /**
     * find the cashier with the lowest expected pass-through time.
     *      passthrough time = waiting time + time to check-out my own bought items
     *      waiting time = remaining time for the current customer +
     *      check-out times of all other customers that will be in front of me in the cashier's queue
     * @param cashiers  the list of available cashiers to select from
     * @return
     */
    public Cashier selectCashier(List<Cashier> cashiers) {
        Cashier selectedCashier;

        if (cashiers.size() == 1) {
            selectedCashier = cashiers.get(0);
        } else {
            selectedCashier = null;
            int passThroughTime1 = 0, waitingTime1 = 0, checkoutTime1 = 0,
                    passThroughTime2 = 0, waitingTime2 = 0, checkoutTime2 = 0;
            // TODO[DONE] find the cashier with the lowest expected pass-through time.
            //  pass through time = waiting time + time to check-out my own bought items
            //  waiting time = remaining time for the current customer +
            //  check-out times of all other customers that will be in front of me in the cashier's queue
            for (Cashier cashier1 : cashiers) {
                if (cashier1.getCustomerZero() == null && cashier1.getWaitingQueue().size() < 1) {
                    selectedCashier = cashier1;
                    break;
                }

                for (Cashier cashier2 : cashiers) {
                    if (!cashier1.equals(cashier2)) {
                        for (Customer queuedCustomers1: cashier1.getWaitingQueue()) {
                            checkoutTime1 += queuedCustomers1.getActualCheckOutTime();
                            waitingTime1 += queuedCustomers1.getActualWaitingTime() + checkoutTime1;
                            passThroughTime1 += waitingTime1;
                        }
                        passThroughTime1 += cashier1.expectedCheckOutTime(getNumberOfItems());

                        for (Customer queuedCustomers2: cashier2.getWaitingQueue()) {
                            checkoutTime2 += queuedCustomers2.getActualCheckOutTime();
                            waitingTime2 += queuedCustomers2.getActualWaitingTime() + checkoutTime2;
                            passThroughTime2 += waitingTime2;
                        }
                        passThroughTime2 += cashier2.expectedCheckOutTime(getNumberOfItems());

                        if (passThroughTime1 > passThroughTime2) {
                            selectedCashier = cashier2;
                        } else {
                            selectedCashier = cashier1;
                        }
                    }
                    break;
                }
            }
        }
        return selectedCashier;
    }

    // TODO implement relevant overrides and/or local classes to be able to
    //  print Customers and/or use them in sets, maps and/or priority queues.

    public LocalTime getQueuedAt() {
        return queuedAt;
    }

    public String getZipCode() {
        return zipCode;
    }

    public Set<Purchase> getItems() {
        return items;
    }

    public int getActualWaitingTime() {
        return actualWaitingTime;
    }

    public int getActualCheckOutTime() {
        return actualCheckOutTime;
    }

    public Cashier getCheckOutCashier() {
        return checkOutCashier;
    }

    public void setCheckOutCashier(Cashier checkOutCashier) {
        this.checkOutCashier = checkOutCashier;
    }

    public void setActualWaitingTime(int actualWaitingTime) {
        this.actualWaitingTime = actualWaitingTime;
    }

    public void setActualCheckOutTime(int actualCheckOutTime) {
        this.actualCheckOutTime = actualCheckOutTime;
    }

    @Override
    public int compareTo(Customer o) {
        return getNumberOfItems() <= o.getNumberOfItems() ? -1 : 1;
    }

    /**
     * read a series of customers with their purchases from the xml stream
     * and add them to the provided customers list
     * associatiate the purchases with the appropriate products
     * @param xmlParser
     * @param customers
     * @param products
     * @return
     * @throws XMLStreamException
     */
    public static List<Customer> importCustomersFromXML(XMLParser xmlParser, List<Customer> customers,
                                                        Set<Product> products) throws XMLStreamException {
        if (xmlParser.nextBeginTag("customers")) {
            xmlParser.nextTag();
            if (customers != null) {
                Customer customer;
                while ((customer = importFromXML(xmlParser, products)) != null) {
                    customers.add(customer);
                }
            }

            xmlParser.findAndAcceptEndTag("customers");
            return customers;
        }
        return null;
    }

    /**
     * read a single customer with his purchases from the xml stream
     * associatiate the purchases with the appropriate products
     * @param xmlParser
     * @param products
     * @return
     * @throws XMLStreamException
     */
    public static Customer importFromXML(XMLParser xmlParser, Set<Product> products) throws XMLStreamException {
        if (xmlParser.nextBeginTag("customer")) {
            LocalTime qTime = LocalTime.parse(xmlParser.getAttributeValue(null, "queuedAt"));
            String zipCode = xmlParser.getAttributeValue(null, "zipCode");

            Customer customer = new Customer(qTime, zipCode);
            xmlParser.nextTag();
            if (customer.items != null) {
                Purchase purchase;
                while ((purchase = Purchase.importFromXML(xmlParser, products)) != null) {
                    customer.items.add(purchase);
                }
            }

            xmlParser.findAndAcceptEndTag("customer");
            return customer;
        }
        return null;
    }

    /**
     * write a single customer with its purchases to the xml stream
     * @param xmlWriter
     * @throws XMLStreamException
     */
    public void exportToXML(XMLStreamWriter xmlWriter) throws XMLStreamException {
        xmlWriter.writeStartElement("customer");
        xmlWriter.writeAttribute("queuedAt", this.queuedAt.toString().concat(":00").substring(0, 8));
        xmlWriter.writeAttribute("zipCode", this.zipCode);
        if (this.items != null) {
            for (Purchase pu : this.items) {
                pu.exportToXML(xmlWriter);
            }
        }
        xmlWriter.writeEndElement();
    }
}
