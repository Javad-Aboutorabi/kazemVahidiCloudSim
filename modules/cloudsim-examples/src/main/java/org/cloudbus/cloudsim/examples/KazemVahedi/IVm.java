package org.cloudbus.cloudsim.examples.KazemVahedi;

import org.cloudbus.cloudsim.CloudletScheduler;
import org.cloudbus.cloudsim.Vm;

public class IVm extends Vm {
    private int type;
    private static final double idle=162;
    private double vmPower;
    private boolean set;


    public IVm(int id, int userId,boolean set, double mips, int numberOfPes, int ram, long bw, long size, String vmm, CloudletScheduler cloudletScheduler) {
        super(id, userId, mips, numberOfPes, ram, bw, size, vmm, cloudletScheduler);
        this.set = set;
        vmPower=(mips*numberOfPes)+idle;
    }
    public double getVmPower() {

        return vmPower;
    }



    public boolean isSet() {
        return set;
    }

    public void setSet(boolean set) {
        this.set = set;
    }
}
