package nl.hva.ict.se.ads;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Place all your own tests for ChampionSelector in this class. Tests in any other class will be ignored!
 */
public class ExtendedChampionSelectorTest extends ChampionSelectorTest {
    protected Comparator<Archer> comparator;

    @BeforeEach
    public void createComparator() {
        comparator = new ArcherComparator();
    }

    @Test
    public void quickSortAndCollectionSortResultInSameOrder() {
        List<Archer> unsortedArchersForQuickSort = Archer.generateArchers(4);
        List<Archer> unsortedArchersForCollection = new ArrayList<>(unsortedArchersForQuickSort);

        List<Archer> sortedArchersQuickSort = ChampionSelector.quickSort(unsortedArchersForQuickSort, comparator);
        List<Archer> sortedArchersCollection = ChampionSelector.collectionSort(unsortedArchersForCollection, comparator);

        assertEquals(sortedArchersCollection, sortedArchersQuickSort);
    }
}
