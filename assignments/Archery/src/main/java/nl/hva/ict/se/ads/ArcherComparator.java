package nl.hva.ict.se.ads;

import java.util.Comparator;

public class ArcherComparator implements Comparator<Archer> {
    @Override
    public int compare(Archer o1, Archer o2) {
        if (o1.getTotalScore() > o2.getTotalScore()) {
            return 1;
        } else if (o2.getTotalScore() > o1.getTotalScore()) {
            return -1;
        } else {
            if (o1.getWeightedScore() > o2.getWeightedScore()) {
                return 1;
            } else if (o2.getWeightedScore() > o1.getWeightedScore()) {
                return -1;
            } else {
                if (o1.getId() > o2.getId()) {
                    return 1;
                } else if (o2.getId() > o1.getId()) {
                    return -1;
                }
                return 0;
            }
        }


    }
}
