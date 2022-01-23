package nl.hva.ict.se.ads;

import java.util.*;

/**
 * Given a list of Archer's this class can be used to sort the list using one of three sorting algorithms.
 */
public class ChampionSelector {
    /**
     * This method uses either selection sort or insertion sort for sorting the archers.
     */
    public static List<Archer> selInsSort(List<Archer> archers, Comparator<Archer> scoringScheme) {
        System.out.println("archers voor het sorteren (insert sort) =\t" + archers);
        for (int i = 0; i < archers.size(); i++) {
            // Gets the archer to be sorted
            Archer archer = archers.get(i);
            int j;

            /* compares the 'archer to be sorted' with the archer that is before it in the list (i - 1), and checks if
            * the 'archer to be sorted' (i) has a lower score then the archer that is before it. If that is the case,
            * they get swapped around, if there is no other archer before 'the archer to be sorted' in the list, this
            * loop stops. */
            for (j = i - 1; j >= 0 && scoringScheme.compare(archer, archers.get(j)) < 0; j--) {
                archers.set(j + 1, archers.get(j));
            }
            archers.set(j + 1, archer);
        }
        System.out.println("archers na het sorteren (insert sort) =\t\t" + archers);
        return archers;
    }


    /**
     * This method uses quick sort for sorting the archers.
     */
    public static List<Archer> quickSort(List<Archer> archers, Comparator<Archer> scoringScheme) {
        System.out.println("archers voor het sorteren (quick sort) = " + archers);

        quickSort(archers, 0, archers.size() - 1, scoringScheme);

        System.out.println("archers na het sorteren (quick sort) =\t " + archers);
        return archers;
    }

    private static void quickSort(List<Archer> archers, int low, int high, Comparator<Archer> scoringScheme) {
        // @source http://www.java2novice.com/java-sorting-algorithms/quick-sort/
        int i = low;
        int j = high;
        // Selects the archer in the middle of the list as pivot point
        Archer pivot = archers.get(low + (high - low) / 2);

        while(i <= j) {
            // Finds the archer from the left sublist that 'wins' from the archer that is selected as pivot point
            while(scoringScheme.compare(archers.get(i), pivot) < 0)
                i++;

            // Finds the archer from the right sublist that 'wins' from the archer that is selected as pivot point
            while(scoringScheme.compare(archers.get(j), pivot) > 0)
                j--;

            // Swaps the archer on index i with the archer on index j when index i is smaller or equal to index j
            if (i <= j) {
                swap(archers, i, j);
                // Move the indexes on both sides to the next position
                i++;
                j--;
            }
        }

        // Checks if the left or right sub-lists still need sorting
        if (low < j)
            quickSort(archers, low, j, scoringScheme);
        if (i < high)
            quickSort(archers, i, high, scoringScheme);
    }

    // Swaps the archer on index i, to index j and visa versa
    private static void swap(List<Archer> archers, int i, int j) {
        Archer temp = archers.get(i);
        archers.set(i, archers.get(j));
        archers.set(j, temp);
    }

    /**
     * This method uses the Java collections sort algorithm for sorting the archers.
     */
    public static List<Archer> collectionSort(List<Archer> archers, Comparator<Archer> scoringScheme) {
        archers.sort(scoringScheme);
        return archers;
    }

    /**
     * This method uses quick sort for sorting the archers in such a way that it is able to cope with an Iterator.
     *
     * <b>THIS METHOD IS OPTIONAL</b>
     */
    public static Iterator<Archer> quickSort(Iterator<Archer> archers, Comparator<Archer> scoringScheme) {
        return null;
    }

}
