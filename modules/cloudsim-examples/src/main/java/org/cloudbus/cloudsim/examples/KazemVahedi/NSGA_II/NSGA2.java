package org.cloudbus.cloudsim.examples.KazemVahedi.NSGA_II;

import org.cloudbus.cloudsim.examples.KazemVahedi.IHost;
import org.cloudbus.cloudsim.examples.KazemVahedi.IVm;

import java.util.*;


@SuppressWarnings("ALL")
public class NSGA2 {
    private final int maxIt = 1000, nRt = 100, nQt = 50, nPt = 50;
    private int numberOfVms, numberOfHost;
    private Dominate dominate = new Dominate();
    private CrowdingDistance crowdingDistance = new CrowdingDistance();
    private Map<Integer, ArrayList<Solution>> map = new HashMap<>();
    private final Solution solution = new Solution();
    private ArrayList<Solution> nRtList = new ArrayList<>(nRt);
    private final ArrayList<Solution> nPtList = new ArrayList<>(nPt);
    private ArrayList<Solution> nQtList = new ArrayList<>(nQt);
    private ArrayList<Solution> chromosome = new ArrayList<>();


    public ArrayList<Integer> nsga2(ArrayList<IHost> hostList, ArrayList<IVm> vmList) {
        chromosome.addAll(generateSolution(hostList, vmList, nRt));
        // population evaluation

        // performing fast non-dominated sorting and crowding distance operator on initial solution
        map = crowdingDistance.CD(dominate.front(chromosome), dominate.numberOfFronts());

//        printMap(map, dominate.numberOfFronts());
        for (int i = 0; i < maxIt; i++) {
            nQtList.clear();
            nPtList.clear();
            nRtList.clear();
            // selecting parents to reproduce

            nRtList = toSplitFor_nPtList(map, nPtList, dominate.numberOfFronts());
            //Crossover
            for (int counter = 0; counter < nQt; counter++) {
                ArrayList<Integer> list = new ArrayList();
                list = createRandom(nQt);
                nQtList.add(crosoverAndMutaion(nRtList.get(list.get(0)), nRtList.get(list.get(1))));
            }
            nRtList.addAll(nQtList);//merging the population
//            nQtList.addAll(generateSolution(nQt));// بعدا پاک شود

            nRtList = RemoveDuplicates(nRtList);
            nRtList.addAll(generateSolution(hostList, vmList, nRt - nRtList.size()));
//
            // performing fast non-dominated sorting and crowding distance operators on new solution
            dominate = new Dominate();
            crowdingDistance = new CrowdingDistance();
            map = crowdingDistance.CD(dominate.front(nRtList), dominate.numberOfFronts());

//            System.out.println("nRtList : " + nRtList.size() + "     nQtList : " + nQtList.size() + "    nPtList : " + nPtList.size());
//            printSolutionList(map.get(1));  //  print front 1
//            printMap(map, dominate.numberOfFronts());
//            System.out.println("size    : "+map.size() +"   number of fronts : "+dominate.numberOfFronts());
        }
        System.out.println("f1  : " + map.get(1).get(0).f1 + "\n" +
                "f2 : " + map.get(1).get(0).f2 + "\n" +
                "chromosome : " + map.get(1).get(0).chromosome);
        return map.get(1).get(0).chromosome;
    }


    private ArrayList<Solution> RemoveDuplicates(ArrayList<Solution> nRtList) {
        ArrayList<Solution> temp = new ArrayList<>();
        for (int i = 0; i < nRtList.size(); i++) {
            Solution solution = nRtList.get(i);
            for (int j = i + 1; j < nRtList.size() - 1; j++) {
                if ((nRtList.get(i).f1 == nRtList.get(j).f1 && nRtList.get(i).f2 == nRtList.get(j).f2)) {
                    nRtList.remove(j);
                    j--;
                }
            }
        }
        return nRtList;
    }

    private ArrayList<Integer> createRandom(int nQt) {
        ArrayList<Integer> randomList = new ArrayList();
        Random random = new Random();
        int rnd;
        randomList.add(random.nextInt(nQt));
        rnd = random.nextInt(nQt);
        while (rnd == randomList.get(0)) {
            rnd = random.nextInt(nQt);
        }
        randomList.add(rnd);
        return randomList;
    }

    private Solution crosoverAndMutaion(Solution parent1, Solution parent2) {
        Solution solution = new Solution();
        ArrayList<Integer> p1 = new ArrayList<>();
        ArrayList<Integer> p2 = new ArrayList<>();
        ArrayList<Integer> ch = new ArrayList<>();
        p1.addAll(convertSolutionToArrayList(parent1));
        p2.addAll(convertSolutionToArrayList(parent2));
        ch.addAll(p1.subList(0, (p1.size() / 2)));
        ch.addAll(p2.subList((p2.size() / 2), p2.size()));
        Solution child = new Solution(ch, solution.numberOfActiveHostForF2(ch));
        mutation(child);
        return child;
    }

    private Solution mutation(Solution child) {
        Solution solution = new Solution();
        Random random = new Random();
        int mutation = random.nextInt(child.numberOfHosts);
        int positionForMutation = random.nextInt(child.numberOfVms);
        ArrayList<Integer> p = new ArrayList<>();
        p.addAll(convertSolutionToArrayList(child));
        while (p.get(positionForMutation) == mutation) {
            mutation = random.nextInt(child.numberOfHosts);
        }
        p.set(positionForMutation, mutation);
        Solution mutant = new Solution(p, solution.numberOfActiveHostForF2(p));
        return mutant;
    }

    private ArrayList<Integer> convertSolutionToArrayList(Solution solution) {
        ArrayList<Integer> chromosome = new ArrayList<>();
        chromosome.addAll(solution.chromosome);
        return chromosome;
    }

    //انتخاب و جداسازی به اندازه nPt برای نسل بعد
    private ArrayList<Solution> toSplitFor_nPtList(Map<Integer, ArrayList<Solution>> map, ArrayList<Solution> nPtList, int numberOfFronts) {
        for (int i = 1; i <= numberOfFronts; i++) {
            if (nPtList.size() + map.get(i).size() < nPt) {
                nPtList.addAll(map.get(i));
            } else {
                int reminder = nPt - nPtList.size();
                for (int j = 0; j < reminder; j++) {
                    nPtList.add(map.get(i).get(j));
                }
            }
        }
        resetSolutionList(nPtList);
        return nPtList;
    }

    private ArrayList<Solution> resetSolutionList(ArrayList<Solution> nPtList) {
        for (Solution sol : nPtList) {
            sol.distance = -1;
            sol.front = 0;
            sol.sp.clear();
            sol.f1d = 0;
            sol.f2d = 0;
            sol.np = 0;
        }
        return nPtList;
    }

    private ArrayList<Solution> generateSolution(ArrayList<IHost> hostList, ArrayList<IVm> vmList, int nRt) {
        ArrayList<Solution> chromosome = new ArrayList<>();
        for (int i = 0; i < nRt; i++)
            chromosome.add(
                    solution.CreateChromosome(hostList, vmList));
        return chromosome;
    }

    private void printSolutionList(ArrayList<Solution> nPtList) {
        System.out.println("================ print Solution List ================");
        for (Solution solution1 : nPtList)
            System.out.println(solution1.front + "  f1 : " + 100 * (solution1.f1) + "  f2 : " + solution1.f2 + "  distance : " + solution1.distance + "      : " + solution1.chromosome);
    }

    private void printMap(Map<Integer, ArrayList<Solution>> map, int numberOfFronts) {
        System.out.println("===================== print Map =====================");
        for (int i = 1; i <= numberOfFronts; i++) {
            for (int j = 0; j < map.get(i).size(); j++)
                System.out.println("front : " + map.get(i).get(j).front + "  f1 : " + 100 * (map.get(i).get(j).f1) +
                        "  f2 : " + map.get(i).get(j).f2 + "  distance : " + map.get(i).get(j).distance +
                        "      : " + map.get(i).get(j).chromosome);
        }
    }
}

