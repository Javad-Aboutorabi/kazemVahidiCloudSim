/*
 * Title:        CloudSim Toolkit
 * Description:  CloudSim (Cloud Simulation) Toolkit for Modeling and Simulation
 *               of Clouds
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2009, The University of Melbourne, Australia
 */


package org.cloudbus.cloudsim.examples.KazemVahedi;

import java.text.DecimalFormat;
import java.util.*;
import java.util.Scanner;

import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;


/**
 * An example showing how to create
 * scalable simulations.
 */
public class CloudSimRun {
    /**
     * The cloudlet list.
     */
    private static List<Cloudlet> cloudletList;
    private static List<IHost> hostList = new ArrayList<IHost>();

    /**
     * The vmlist.
     */
    public static List<IVm> vmlist;

    private static List<IVm> createVM(int userId, int vms) {

        //Creates a container to store VMs. This list is passed to the broker later
        LinkedList<IVm> list = new LinkedList<IVm>();

        //VM Parameters
        long size = 10000; //image size (MB)
        int ram = 512; //vm memory (MB)
        int mips = 1000;
        long bw = 1000;
        int pesNumber = 1; //number of cpus
        String vmm = "Xen"; //VMM name


        //create VMs
        IVm[] vm = new IVm[vms];
        Random r;
        int type;
        for (int i = 0; i < vms; i++) {
            r = new Random();
            type = 1 + r.nextInt(3);
            vm[i] = new IVm(i, userId, false, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerSpaceShared());
            //for creating a VM with a space shared scheduling policy for cloudlets:
            //vm[i] = Vm(i, userId, mips, pesNumber, ram, bw, size, priority, vmm, new CloudletSchedulerSpaceShared());

            list.add(vm[i]);
        }

        return list;
    }


    private static List<Cloudlet> createCloudlet(int userId, int cloudlets) {
        // Creates a container to store Cloudlets
        LinkedList<Cloudlet> list = new LinkedList<Cloudlet>();

        //cloudlet parameters
        long length = 1000;
        long fileSize = 300;
        long outputSize = 300;
        int pesNumber = 1;
        UtilizationModel utilizationModel = new UtilizationModelFull();

        Cloudlet[] cloudlet = new Cloudlet[cloudlets];

        for (int i = 0; i < cloudlets; i++) {
            cloudlet[i] = new Cloudlet(i, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
            // setting the owner of these Cloudlets
            cloudlet[i].setUserId(userId);
            list.add(cloudlet[i]);
        }

        return list;
    }


    ////////////////////////// STATIC METHODS ///////////////////////

    /**
     * Creates main() to run this example
     */
    public static void main(String[] args) {
        Log.printLine("Starting ...");

        try {
            long start = System.currentTimeMillis();
            System.out.println(new Date() + "\n");

            // First step: Initialize the CloudSim package. It should be called
            // before creating any entities.
            int num_user = 1;   // number of grid users
            Calendar calendar = Calendar.getInstance();
            boolean trace_flag = false;  // mean trace events

            // Initialize the CloudSim library
            CloudSim.init(num_user, calendar, trace_flag);

            // Second step: Create Datacenters
            //Datacenters are the resource providers in CloudSim. We need at list one of them to run a CloudSim simulation
            @SuppressWarnings("unused")
            IDatacenter datacenter0 = createDatacenter("Datacenter_0");

            //Third step: Create Broker

            DatacenterBroker broker = createBroker();
            int brokerId = broker.getId();

            //Fourth step: Create VMs and Cloudlets and send them

            // Random r = new Random();
            // int j = 1 + r.nextInt(100);


            System.out.println("Number VM:");
            Scanner in = new Scanner(System.in);
            int j = in.nextInt();
            vmlist = createVM(brokerId, j);


            cloudletList = createCloudlet(brokerId, 10); // creating 40 cloudlets

            broker.submitVmList(vmlist);
            broker.submitCloudletList(cloudletList);
            // Fifth step: Starts the simulation
            // Fifth step: Starts the simulation
            CloudSim.startSimulation();

            // Final step: Print results when simulation is over
            List<Cloudlet> newList = broker.getCloudletReceivedList();

            CloudSim.stopSimulation();

            printCloudletList(newList);

            long end = System.currentTimeMillis();
            System.out.println("\n" + new Date() + "\n");
            long diff = end - start;
            System.out.println("Difference is : " + diff);

            Log.printLine(" finished!");


        } catch (Exception e) {
            e.printStackTrace();
            Log.printLine("The simulation has been terminated due to an unexpected error");
        }
    }


    private static IDatacenter createDatacenter(String name) {

        // Here are the steps needed to create a PowerDatacenter:
        // 1. We need to create a list to store one or more Machines

        // 2. A Machine contains one or more PEs or CPUs/Cores. Therefore, should
        //    create a list to store these PEs before creating a Machine.
        List<Pe> peList1 = new ArrayList<Pe>();
        int mips = 100000;


        // 3. Create PEs and add these into the list.
        //for a quad-core machine, a list of 4 PEs is required:
        peList1.add(new Pe(0, new PeProvisionerSimple(mips))); // need to store Pe id and MIPS Rating

        // Another list, for a dual-core machine
        // List<Pe> peList2 = new ArrayList<Pe>();
        // peList2.add(new Pe(0, new PeProvisionerSimple(mips)));
        // peList2.add(new Pe(1, new PeProvisionerSimple(mips)));
        int numberOfHosts = 2;
        //4. Create Hosts with its id and list of PEs and add them to the list of machines
        int hostId = 0;
        int ram = 4000; //host memory (MB)
        long storage = 10000000L; //host storage
        int bw = 4000;


//        hostList.add(new IHost(hostId, false, 1, new RamProvisionerSimple(ram), new BwProvisionerSimple(bw), storage, peList1,
//                new VmSchedulerTimeShared(peList1)));
        Random ra;
        int type;
        for (int i = 0; i < numberOfHosts; i++) {
            ra = new Random();
            type = 1 + ra.nextInt(3);
            hostList.add(new IHost(
                    i, false, new RamProvisionerSimple(ram), new BwProvisionerSimple(bw),
                    storage, peList1, new VmSchedulerTimeShared(peList1)));
        }

        // 5. Create a DatacenterCharacteristics object that stores the
        //    properties of a data center: architecture, OS, list of
        //    Machines, allocation policy: time- or space-shared, time zone
        //    and its price (G$/Pe time unit).
        String arch = "x86";      // system architecture
        String os = "Linux";          // operating system
        String vmm = "Xen";
        double time_zone = 10.0;         // time zone this resource located
        double cost = 3.0;              // the cost of using processing in this resource
        double costPerMem = 0.05;        // the cost of using memory in this resource
        double costPerStorage = 0.1;    // the cost of using storage in this resource
        double costPerBw = 0.1;            // the cost of using bw in this resource
        LinkedList<Storage> storageList = new LinkedList<Storage>();    //we are not adding SAN devices by now

        DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
                arch, os, vmm, hostList, time_zone, cost, costPerMem, costPerStorage, costPerBw);


        // 6. Finally, we need to create a PowerDatacenter object.
        IDatacenter datacenter = null;
        try {
            datacenter = new IDatacenter(name, characteristics,
                    new VmAllocationPolicySimple(hostList), storageList, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return datacenter;
    }

    //We strongly encourage users to develop their own broker policies, to submit vms and cloudlets according
    //to the specific rules of the simulated scenario
    private static DatacenterBroker createBroker() {

        DatacenterBroker broker = null;
        try {
            broker = new DatacenterBroker("Broker");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return broker;
    }

    /**
     * Prints the Cloudlet objects
     *
     * @param list list of Cloudlets
     */
    private static void printCloudletList(List<Cloudlet> list) {
        int size = list.size();
        Cloudlet cloudlet;

        String indent = "    ";
        Log.printLine();
        Log.printLine("========== OUTPUT ==========");
        Log.printLine("Cloudlet ID" + indent + "STATUS" + indent +
                "Data center ID" + indent + "VM ID" + indent + indent + "Time" + indent + "Start Time"
                + indent + "Finish Time");

        DecimalFormat dft = new DecimalFormat("###.##");
        for (int i = 0; i < size; i++) {
            cloudlet = list.get(i);
            Log.print(indent + cloudlet.getCloudletId() + indent + indent);

            if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS) {
                Log.print("SUCCESS");
                Log.printLine(indent + indent + cloudlet.getResourceId() + indent + indent + indent + cloudlet.getVmId() +
                        indent + indent + indent + dft.format(cloudlet.getActualCPUTime()) +
                        indent + indent + dft.format(cloudlet.getExecStartTime())
                        + indent + indent + indent + dft.format(cloudlet.getFinishTime())
                        + indent + indent);
            }
        }
        hostPrint();
    }

    public static void hostPrint() {
        Log.printLine();

        for (int i = 0; i < hostList.size(); i++) {
            Log.printLine("-----------------------------------");
            IHost host = hostList.get(i);

            String datacenterName = host.getDatacenter().getName();
            boolean a = host.isActiveFlag();
            if (a) {
                Log.printLine("Host ID  " + host.getId() + " In    "
                        + datacenterName + "    Is busy" + "   number of vm in host     " + host.iVmList.size());

            } else {
                Log.printLine("Host ID  " + host.getId() +" In    "
                        + datacenterName + "    Is idle" + "   number of vm in host     "
                        + host.iVmList.size());
            }



        }
        Log.printLine("\n-------------------------------------");
        IDatacenter dc = (IDatacenter) hostList.get(0).getDatacenter();
        Log.printLine("averagePower:  " + dc.averagePower());
        Log.printLine("hostActive:  " + dc.hostActive());
        Log.printLine("averageWastage:  " + dc.averageWastage());
        Log.printLine("\n-------------------------------------\n");

    }

}


