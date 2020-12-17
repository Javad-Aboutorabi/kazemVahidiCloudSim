package org.cloudbus.cloudsim.examples.KazemVahedi.NSGA_II;

import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.examples.KazemVahedi.IHost;
import org.cloudbus.cloudsim.examples.KazemVahedi.IVm;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Chromosome {
    private ArrayList<IHost> chHosts = new ArrayList<>();
    private ArrayList<IVm> chVms = new ArrayList<>();

    public Chromosome(ArrayList<IHost> hosts, ArrayList<IVm> vms) {

        this.chHosts = hosts;
        this.chVms = vms;
    }

    public Chromosome() {

    }

    // Chromosome = Array list of vm that each index of list vm and each sell host id

    public ArrayList<Integer> F1AndF2() {
        Random random = new Random();
        ArrayList<Integer> chromosome = new ArrayList<>();
        ArrayList<Integer> sort = new ArrayList<>();
        ArrayList<Integer> sorted = new ArrayList<>();
        int numberOfHosts = chHosts.size();
//        int numberOfVms = chVms.size();
        int max = 0, position = 0, rnd;
        // Create random chromosome
        for (IVm vm : chVms) {
            rnd = random.nextInt(numberOfHosts);
            chromosome.add(vm.getId(), rnd);
        }
        // Sort
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
        System.out.println("chromosome  : " + chromosome);
        System.out.println("sorted      : " + sorted);

        return sorted;
    }

    public static Integer numberOfActiveHost(ArrayList<Integer> sorted) {
        // Number of active host in each chromosome
        int numberOfActiveHost = 1;
        int temp = sorted.get(0);
        for (Integer i : sorted) {
            if (temp != i) {
                temp = i;
                numberOfActiveHost++;
            }
        }
        System.out.println("numberOfActiveHost :  " + numberOfActiveHost);
        return numberOfActiveHost;
    }


    public static ArrayList<IVm> createVM(int userId, int vms) {

        //Creates a container to store VMs. This list is passed to the broker later
        ArrayList<IVm> list = new ArrayList<IVm>();

        //VM Parameters
        long size = 10000; //image size (MB)
        int ram = 512; //vm memory (MB)
        int mips = 1000;
        long bw = 1000;
        int pesNumber = 1; //number of cpus
        String vmm = "Xen"; //VMM name

        //create VMs
        IVm[] vm = new IVm[vms];

        for (int i = 0; i < vms; i++) {
            vm[i] = new IVm(1, userId, false, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());

            list.add(vm[i]);
        }

        return list;
    }

    public static List<IHost> createHost(int numberOfHost) {

        List<IHost> hostList = new ArrayList<IHost>();
        List<Pe> peList1 = new ArrayList<Pe>();

//        int mips = 1000;

        Random random = new Random();
        int[] Mips = {5000, 6000, 7000, 8000, 9000, 10000, 15000, 20000, 25000, 30000};
        int[] ram = {2048, 4096, 4096 * 2, 4096 * 4};

        int mips = Mips[random.nextInt(10)];
        peList1.add(new Pe(0, new PeProvisionerSimple(mips))); // need to store Pe id and MIPS Rating
        peList1.add(new Pe(1, new PeProvisionerSimple(mips)));
        peList1.add(new Pe(2, new PeProvisionerSimple(mips)));
        peList1.add(new Pe(3, new PeProvisionerSimple(mips)));

        //Another list, for a dual-core machine
        List<Pe> peList2 = new ArrayList<Pe>();
        mips = Mips[random.nextInt(10)];
        peList2.add(new Pe(0, new PeProvisionerSimple(mips)));
        peList2.add(new Pe(1, new PeProvisionerSimple(mips)));

        int hostId = 0;
        long storage = 1000000; //host storage
        int bw = 10000;
//        hostList.add(
//                new IHost(
//                        hostId, false,
//                        1, new RamProvisionerSimple(ram[random.nextInt(4)]), new BwProvisionerSimple(bw),
//                        storage, peList1, new VmSchedulerTimeShared(peList1)
//                )
//        ); // 1 machine
//        hostId++;
//        hostList.add(
//                new IHost(
//                        hostId, false, 2,
//                        new RamProvisionerSimple(ram[random.nextInt(4)]), new BwProvisionerSimple(bw),
//                        storage, peList2, new VmSchedulerTimeShared(peList2)
//                )
//        ); // 2 machine
//        hostId++;
//        hostList.add(
//                new IHost(
//                        hostId, false, 2,
//                        new RamProvisionerSimple(ram[random.nextInt(4)]), new BwProvisionerSimple(bw),
//                        storage, peList2, new VmSchedulerTimeShared(peList1)
//                )
//        ); // 3 machine
//        hostId++;
//        hostList.add(
//                new IHost(
//                        hostId, false, 3,
//                        new RamProvisionerSimple(ram[random.nextInt(4)]), new BwProvisionerSimple(bw),
//                        storage, peList2, new VmSchedulerTimeShared(peList2)
//                )
//        ); // 4 machine
//        hostId++;
//        hostList.add(
//                new IHost(
//                        hostId, false, 2,
//                        new RamProvisionerSimple(ram[random.nextInt(4)]), new BwProvisionerSimple(bw),
//                        storage, peList2, new VmSchedulerTimeShared(peList2)
//                )
//        ); // 5 machine
//        hostId++;
//        hostList.add(
//                new IHost(
//                        hostId, false, 1,
//                        new RamProvisionerSimple(ram[random.nextInt(4)]), new BwProvisionerSimple(bw),
//                        storage, peList2, new VmSchedulerTimeShared(peList2)
//                )
//        ); // 6 machine
//        hostId++;
//        hostList.add(
//                new IHost(
//                        hostId, false, 3,
//                        new RamProvisionerSimple(ram[random.nextInt(4)]), new BwProvisionerSimple(bw),
//                        storage, peList2, new VmSchedulerTimeShared(peList2)
//                )
//        ); // 7 machine

        for (int i = 0; i < numberOfHost; i++) {
            hostList.add(
                    new IHost(
                            i, false,
                            new RamProvisionerSimple(ram[random.nextInt(4)]), new BwProvisionerSimple(bw),
                            storage, peList2, new VmSchedulerTimeShared(peList1)
                    )
            );
        }

        return hostList;
    }
}
