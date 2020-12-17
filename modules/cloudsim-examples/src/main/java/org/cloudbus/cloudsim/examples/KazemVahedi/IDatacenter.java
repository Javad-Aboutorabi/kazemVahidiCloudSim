package org.cloudbus.cloudsim.examples.KazemVahedi;


import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.VmAllocationPolicy;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.CloudSimTags;
import org.cloudbus.cloudsim.core.SimEvent;
import org.cloudbus.cloudsim.examples.KazemVahedi.NSGA_II.NSGA2;

import java.util.ArrayList;
import java.util.List;

public class IDatacenter extends Datacenter {
    static int counter = 0;
    private ArrayList<Integer> chromosome = new ArrayList<>();
    private boolean b = true;
    private int count = 0;


    /**
     * Allocates a new PowerDatacenter object.
     *
     * @param name               the name to be associated with this entity (as required by Sim_entity class from
     *                           simjava package)
     * @param characteristics    an object of DatacenterCharacteristics
     * @param vmAllocationPolicy the vmAllocationPolicy
     * @param storageList        a LinkedList of storage elements, for data simulation
     * @param schedulingInterval
     * @throws Exception This happens when one of the following scenarios occur:
     *                   <ul>
     *                   <li>creating this entity before initializing CloudSim package
     *                   <li>this entity name is <tt>null</tt> or empty
     *                   <li>this entity has <tt>zero</tt> number of PEs (Processing Elements). <br>
     *                   No PEs mean the Cloudlets can't be processed. A CloudResource must contain one or
     *                   more Machines. A Machine must contain one or more PEs.
     *                   </ul>
     * @pre name != null
     * @pre resource != null
     * @post $none
     */
    public IDatacenter(String name, DatacenterCharacteristics characteristics, VmAllocationPolicy vmAllocationPolicy, List<Storage> storageList, double schedulingInterval) throws Exception {
        super(name, characteristics, vmAllocationPolicy, storageList, schedulingInterval);
    }

    @Override
    /**
     * Process the event for an User/Broker who wants to create a VM in this PowerDatacenter. This
     * PowerDatacenter will then send the status back to the User/Broker.
     *
     * @param ev a Sim_event object
     * @param ack the ack
     * @pre ev != null
     * @post $none
     */
    protected void processVmCreate(SimEvent ev, boolean ack) {
        boolean ffd = false;
        IVm vm = (IVm) ev.getData();
        ArrayList<IHost> hosts = new ArrayList<>();
        hosts.addAll(getHostList());
        IHost hs = hosts.get(0);
        if (ffd) {                                          // FFD
            for (int i = 0; i < getHostList().size(); i++) {
                hosts.add((IHost) getHostList().get(i));
            }
            //List<IHost> hosts = new ArrayList<>();
            //hosts = getHostList();
            FFD f = new FFD();
            int allocateHost = f.ffd(hosts, vm);
            hs = hosts.get(allocateHost);
            hs.setActiveFlag(true);
            hs.iVmList.add(vm);
        } else {                                                //  nsga2
            if (b) {
                NSGA2 nsga2 = new NSGA2();
                ArrayList<IVm> vms = new ArrayList<>(CloudSimRun.vmlist);
                chromosome = nsga2.nsga2(hosts, vms);
                b = false;
            }
            if (count < chromosome.size()) {
                vm = CloudSimRun.vmlist.get(count);
                hs = hosts.get(chromosome.get(count));
                count++;
            }
        }


        boolean result = getVmAllocationPolicy().allocateHostForVm(vm, hs);
        if (ack) {
            int[] data = new int[3];


            data[0] = getId();
            data[1] = vm.getId();

            if (result) {
                data[2] = CloudSimTags.TRUE;
            } else {
                data[2] = CloudSimTags.FALSE;
            }
            send(vm.getUserId(), CloudSim.getMinTimeBetweenEvents(), CloudSimTags.VM_CREATE_ACK, data);
        }

        if (result) {
            getVmList().add(vm);

            if (vm.isBeingInstantiated()) {
                vm.setBeingInstantiated(false);

            }

            vm.updateVmProcessing(CloudSim.clock(), getVmAllocationPolicy().getHost(vm).getVmScheduler()
                    .getAllocatedMipsForVm(vm));
        }
    }


    public int hostActive() {
        List<IHost> hosts = new ArrayList<>();
        for (int i = 0; i < getHostList().size(); i++)
            hosts.add((IHost) getHostList().get(i));
        for (IHost host : hosts)
            if (host.isActiveFlag())
                counter++;
        return counter;
    }


    public double averagePower() {
        double averagePower;
        double sumPower = 0;
        int a;
        List<IHost> hosts = new ArrayList<>();
        for (int i = 0; i < getHostList().size(); i++) {
            hosts.add((IHost) getHostList().get(i));
        }
        a = hosts.size();
        for (IHost host : hosts) {
            sumPower += host.powerConsumption();
        }
        averagePower = sumPower / a;
        return averagePower;
    }

    public double averageWastage() {
        double averageWastage;
        double sumWastage = 0;
        List<IHost> hosts = new ArrayList<>();
        for (int i = 0; i < getHostList().size(); i++) {
            hosts.add((IHost) getHostList().get(i));
        }
        for (IHost host : hosts) {
            sumWastage += host.resourceWastage();
        }
        averageWastage = sumWastage / counter;
        return averageWastage;
    }

}