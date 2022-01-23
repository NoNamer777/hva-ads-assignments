package nl.hva.ict.se.ads;

import org.apache.commons.lang3.time.StopWatch;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        Comparator<Archer> comparator = new ArcherComparator();
        StopWatch timerInsertionSort = new StopWatch();
        StopWatch timerQuickSort = new StopWatch();
        StopWatch timerCollectionSort = new StopWatch();

//            int totalArchers = (int) Math.pow(2, i) * 100;
        System.out.printf("%-25s%-25s%s\n", "InsertionSort", "QuickSort", "CollectionSort");
        for (int i = 100; i < 5_000_000; i *= 2) {
            List<Archer> archersForInsertionSort = Archer.generateArchers(i);
            List<Archer> archersForQuickSort = new ArrayList<>(archersForInsertionSort);
            List<Archer> archersForCollectionSort = new ArrayList<>(archersForInsertionSort);

            if (timerInsertionSort.getTime(TimeUnit.SECONDS) <= 20) {
                timerInsertionSort.reset();
                timerInsertionSort.start();
                ChampionSelector.selInsSort(archersForInsertionSort, comparator);
                timerInsertionSort.stop();
            }

            if (timerQuickSort.getTime(TimeUnit.SECONDS) <= 20) {
                timerQuickSort.reset();
                timerQuickSort.start();
                ChampionSelector.quickSort(archersForQuickSort, comparator);
                timerQuickSort.stop();
            }

            if (timerCollectionSort.getTime(TimeUnit.SECONDS) <= 20) {
                timerCollectionSort.reset();
                timerCollectionSort.start();
                ChampionSelector.collectionSort(archersForCollectionSort, comparator);
                timerCollectionSort.stop();
            }

            System.out.printf("%s~%s~%s\n",
                    (timerInsertionSort.getTime(TimeUnit.SECONDS) <= 20) ? i + ";" + timerInsertionSort.getTime() : "",
                    (timerQuickSort.getTime(TimeUnit.SECONDS) <= 20) ? i + ";" + timerQuickSort.getTime() : "",
                    (timerCollectionSort.getTime(TimeUnit.SECONDS) <= 20) ? i + ";" + timerCollectionSort.getTime() : "");
        }
    }
}
