package org.cloudbus.cloudsim.examples.KazemVahedi.NSGA_II;

import org.cloudbus.cloudsim.examples.KazemVahedi.IHost;
import org.cloudbus.cloudsim.examples.KazemVahedi.IVm;

import java.util.ArrayList;
import java.util.Random;

@SuppressWarnings("CollectionAddedToSelf")
public class Solution {
    public double f1, f2, np, front, distance, f1d = 0, f2d = 0;
    public static int numberOfVms, numberOfHosts;
    public static ArrayList<IHost> hostList;
    public static ArrayList<IVm> vmList;
    public ArrayList<Solution> sp = new ArrayList<>();
    public ArrayList<Integer> chromosome = new ArrayList<>();

    //     f1 is efficiency
    //     f2 is number of active hosts
    public Solution() {

    }

    public Solution(ArrayList<Integer> chromosome, int f2) {
        this.chromosome = chromosome;
        this.f1 = f1(this.chromosome);
        this.f2 = f2;
        this.distance = -1;
//        System.out.println(this.f1);
    }

    public Solution CreateChromosome(ArrayList<IHost> hostList, ArrayList<IVm> vmList) {
        Solution.hostList = hostList;
        Solution.vmList = vmList;
        ArrayList<Integer> chromosome = new ArrayList<>(vmList.size());
        numberOfHosts = hostList.size();
        numberOfVms = vmList.size();
        Random random = new Random();
        for (int i = 0; i < numberOfVms; i++)
            chromosome.add(i, random.nextInt(numberOfHosts));
        Solution solution = new Solution(chromosome, numberOfActiveHostForF2(chromosome));
        return solution;
    }

    //    f2 is , number of active host

    public Integer numberOfActiveHostForF2(ArrayList<Integer> sorted) {
        sorted = sortChromosome(sorted);
        // Number of active host in each chromosome
        int numberOfActiveHost = 1;
        int temp = sorted.get(0);
        for (Integer i : sorted) {
            if (temp != i) {
                temp = i;
                numberOfActiveHost++;
            }
        }
        return numberOfActiveHost;
    }

    public ArrayList<Integer> sortChromosome(ArrayList<Integer> chromosome) {
        ArrayList<Integer> sort = new ArrayList<>();
        ArrayList<Integer> sorted = new ArrayList<>();
        int max, position;
        sort.addAll(chromosome);
        int size = sort.size();
        for (int i = 0; i < size; i++) {
            max = sort.get(0);
            position = 0;
            for (int j = 0; j < sort.size(); j++) {
                if (sort.get(j) >= max) {
                    max = sort.get(j);
                    position = j;
                }
            }
            sorted.add(i, max);
            sort.remove(position);
        }
        return sorted;
    }


    private double f1(ArrayList<Integer> chromosome) {
        double totalVmRamInHost, totalVmCpuInHost, totalBandwidthInHost, wastage = 0;
        for (IHost host : hostList) {
            totalVmCpuInHost = 0;
            totalVmRamInHost = 0;
            totalBandwidthInHost = 0;
            for (int i = 0; i < chromosome.size(); i++) {

                if (host.getId() == chromosome.get(i)) {
                    totalVmRamInHost = totalVmRamInHost + vmList.get(i).getRam();
                    totalVmCpuInHost = totalVmCpuInHost + vmList.get(i).getMips();
                    totalBandwidthInHost = totalBandwidthInHost + vmList.get(i).getBw();
                }
                wastage += ((host.getHostCpu() - totalVmCpuInHost) / (3 * host.getHostCpu())) +
                        ((host.getHostRam() - totalVmRamInHost) / (3 * host.getHostRam()) +
                                ((host.getBw() - totalBandwidthInHost) / (3 * host.getBw())));

            }
            if (totalVmCpuInHost > host.getMips() || totalVmRamInHost > host.getHostRam() || totalBandwidthInHost > host.getBw()) {
                System.out.println("wastage  : " + wastage + "\n" +
                        "totalVmCpuInHost   : " + totalVmCpuInHost + "\n" +
                        "totalVmRamInHost   : " + totalVmRamInHost + "\n" +
                        "totalBandwidthInHost   : " + totalBandwidthInHost + "\n" +
                        "host CPU    " + host.getMips() + "\n" +
                        "host Ram    " + host.getHostRam() + "\n" +
                        "host BW    " + host.getBw());
                return 100000.0;
            }

        }
        return wastage / 100;
    }
}