package org.cloudbus.cloudsim.examples.KazemVahedi.NSGA_II;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CrowdingDistance {
    public Map<Integer, ArrayList<Solution>> CD(Map<Integer, ArrayList<Solution>> solutions, int numberOfFronts) {
        Map<Integer, ArrayList<Solution>> temp = new HashMap<>();
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMaximumFractionDigits(10);
        CrowdingDistance crowdingDistance = new CrowdingDistance();
        int front = numberOfFronts;
        for (int i = 1; i <= front; i++) {
            temp.put(i, crowdingDistance.distance(solutions.get(i)));
        }
        return temp;
    }

    public ArrayList<Solution> distance(ArrayList<Solution> solutionList) {
        ArrayList<Solution> distance = new ArrayList<>();
        distance.addAll(solutionList);
        distance = maxSortForF1(distance);
        distance.get(0).f1d = Double.POSITIVE_INFINITY;
        for (int i = 1; i < distance.size() - 1; i++) {
            double next = (distance.get(i + 1).f1);
            double previous = (distance.get(i - 1).f1);
            double first = (distance.get(0).f1);
            double end = (distance.get(distance.size() - 1).f1);
//            System.out.println("next    "+next+"   previous"+previous+"   end"+end+"  first"+first);
            distance.get(i).f1d = Math.abs(previous - next) / Math.abs(end - first);
        }
        distance.get(distance.size() - 1).f1d = Double.POSITIVE_INFINITY;

        distance = minSortForF2(distance);
        distance.get(0).f2d = Double.POSITIVE_INFINITY;
        for (int i = 1; i < distance.size() - 1; i++) {
            double next = (distance.get(i + 1).f2);
            double previous = (distance.get(i - 1).f2);
            double first = (distance.get(0).f2);
            double end = (distance.get(distance.size() - 1).f2);

            distance.get(i).f2d = Math.abs(next - previous) / Math.abs(first - end);
        }
        distance.get(distance.size() - 1).f2d = Double.POSITIVE_INFINITY;
        for (int i = 0; i < distance.size(); i++) {
            distance.get(i).distance = distance.get(i).f1d + distance.get(i).f2d;
        }

        distance = distanceSort(distance);

        return distance;
    }

    private ArrayList<Solution> maxSortForF1(ArrayList<Solution> solutionList) {
        ArrayList<Solution> sortSolutionList = new ArrayList<>();
        Solution s = new Solution();
        double min;
        int size = solutionList.size();
        int x = solutionList.size();
        for (int j = 0; j < x; j++) {
            min = 100;
            for (int i = 0; i < size; i++) {
                if (solutionList.get(i).f1 < min) {
                    min = solutionList.get(i).f1;
                    s = solutionList.get(i);
                }
            }
            sortSolutionList.add(s);
            solutionList.remove(s);
            size -= 1;
        }
        return sortSolutionList;
    }

    private ArrayList<Solution> minSortForF2(ArrayList<Solution> solutionList) {
        ArrayList<Solution> sortSolutionList = new ArrayList<>();
        Solution s = new Solution();
        double min;
        int size = solutionList.size();
        int x = solutionList.size();
        for (int j = 0; j < x; j++) {
            min = 100;
            for (int i = 0; i < size; i++) {
                if (solutionList.get(i).f2 < min) {
                    min = solutionList.get(i).f2;
                    s = solutionList.get(i);
                }
            }
            sortSolutionList.add(s);
            solutionList.remove(s);
            size -= 1;
        }
        return sortSolutionList;
    }

    private ArrayList<Solution> distanceSort(ArrayList<Solution> solutionList) {
        ArrayList<Solution> sortSolutionList = new ArrayList<>();
        Solution s = new Solution();
        double max;
        double size = solutionList.size();
        double x = solutionList.size();
        for (int j = 0; j < x; j++) {
            max = 0;
            for (int i = 0; i < size; i++) {
                if (solutionList.get(i).distance > max) {
                    max = solutionList.get(i).distance;
                    s = solutionList.get(i);
                }
            }
            sortSolutionList.add(j, s);
            solutionList.remove(s);
            size -= 1;
        }
        return sortSolutionList;
    }
}
