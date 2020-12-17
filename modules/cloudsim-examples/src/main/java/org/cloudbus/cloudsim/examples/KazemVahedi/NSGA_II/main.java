package org.cloudbus.cloudsim.examples.KazemVahedi.NSGA_II;

import org.cloudbus.cloudsim.examples.KazemVahedi.IHost;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class main {
    public static void main(String[] args) {
        Dominate dominate = new Dominate();
        CrowdingDistance crowdingDistance = new CrowdingDistance();
        Map<Integer, ArrayList<Solution>> map = new HashMap<>();
        ArrayList<Solution> chromosome = new ArrayList<>();
        Solution solution = new Solution();
        for (int i = 0; i < 100; i++) {
            chromosome.add(
                    solution.CreateChromosome((ArrayList<IHost>) Chromosome.createHost(20),
                            Chromosome.createVM(1, 40)));
        }
        map = crowdingDistance.CD(dominate.front(chromosome), dominate.numberOfFronts());

//        for (int i = 1; i <= dominate.numberOfFronts(); i++) {
//            for (int j = 0; j < map.get(i).size(); j++)
//                System.out.println(
//                        "front : " + map.get(i).get(j).front +
//                                "  f1 : " + (map.get(i).get(j).f1*100) +
//                                "  f2 : " + map.get(i).get(j).f2 +
//                                "  distance : " + map.get(i).get(j).distance +
//                                "      : " + map.get(i).get(j).chromosome);
//        }
    }
}








