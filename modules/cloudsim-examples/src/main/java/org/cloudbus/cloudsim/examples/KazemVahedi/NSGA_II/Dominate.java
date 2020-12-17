package org.cloudbus.cloudsim.examples.KazemVahedi.NSGA_II;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Dominate {
    public Map<Integer, ArrayList<Solution>> fronts = new HashMap<>();
    private int k = 1;

    private boolean dominate(Solution p, Solution q) {
        if ((p.f1 <= q.f1 && p.f2 <= q.f2) && ((p.f1 < q.f1) || p.f2 < q.f2))
            return true;
        return false;
    }

    public Map<Integer, ArrayList<Solution>> front(ArrayList<Solution> populations) {
        ArrayList<Solution> f1 = new ArrayList<>();
        for (int i = 0; i < populations.size(); i++) {
            for (int j = i + 1; j < populations.size(); j++) {
                if (dominate(populations.get(i), populations.get(j))) {
                    populations.get(j).np++;
                    populations.get(i).sp.add(populations.get(j));
                }
                if (dominate(populations.get(j), populations.get(i))) {
                    populations.get(i).np++;
                    populations.get(j).sp.add(populations.get(i));
                }
            }
            if (populations.get(i).np == 0) {
                populations.get(i).front = 1;
                f1.add(populations.get(i));
            } else populations.get(i).front = -1;
        }
        if (f1.size() > 0) {
            fronts.put(k, f1);
        }

        boolean b = true;
        while (b) {
            ArrayList<Solution> Q = new ArrayList<>();
            for (Solution front : fronts.get(k)) {
                for (Solution q : front.sp) {
                    q.np--;
                    if (q.np == 0) {
                        q.front = k + 1;
                        Q.add(q);
                    }
                }
            }
            k++;
            if (Q.size() > 0) {
                fronts.put(k, Q);
            } else
                b = false;
        }
        return fronts;
    }

    public int numberOfFronts() {
        return fronts.size();
    }


}