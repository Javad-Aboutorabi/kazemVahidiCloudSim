package org.cloudbus.cloudsim.examples.KazemVahedi;

import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.VmScheduler;
import org.cloudbus.cloudsim.provisioners.BwProvisioner;
import org.cloudbus.cloudsim.provisioners.RamProvisioner;

import java.util.ArrayList;
import java.util.List;

public class IHost extends Host {

    public ArrayList<IVm> iVmList = new ArrayList<>();
    private int mips;
    public final double busy = 215, idle = 162;
    private int ram;
    private boolean activeFlag;

    public double getHostCpu() {
        return hostCpu;
    }

    public double getHostRam() {
        return hostRam;
    }

    private double hostCpu, hostRam,hostBW;

    public IHost(int id, boolean activeFlag, int type, RamProvisioner ramProvisioner, BwProvisioner bwProvisioner,
                 long storage, List<? extends Pe> peList, VmScheduler vmScheduler) {
        super(id, ramProvisioner, bwProvisioner, storage, peList, vmScheduler);
        this.activeFlag=activeFlag;
        mips = this.getTotalMips();;
        hostCpu = this.getTotalMips();;
        hostRam = getRamProvisioner().getRam();
        hostBW=this.getBwProvisioner().getBw();
        ram = getRamProvisioner().getRam();
    }


    public double resourceWastage() {
        double wastage, totalVmRamInHost = 0, totalVmCpuInHost = 0;
        for (IVm vm : iVmList) {
            totalVmRamInHost = totalVmRamInHost + vm.getRam();
            totalVmCpuInHost = totalVmCpuInHost + vm.getMips();


            if ((totalVmCpuInHost >= hostCpu) || (totalVmRamInHost >= hostRam))
                break;
        }
        wastage = ((hostCpu - totalVmCpuInHost) / (2 * hostCpu)) + ((hostRam - totalVmRamInHost) / (2 * hostRam));
        if (!iVmList.isEmpty())
            return wastage;
        else return 0;
    }

    public double powerConsumption() {
        double power = 0;
        for (IVm vm : iVmList) {
            power = power + (((busy - idle) * vm.getVmPower()) + idle);
        }
        if (iVmList.isEmpty())
            power = idle;
        return power;
    }

    public int getMips() {
        return mips;
    }

    public void setMips(int mips) {
        this.mips = mips;
    }

    @Override
    public int getRam() {
        return ram;
    }

    public void setRam(int ram) {
        this.ram = ram;
    }
    public void setBw(double bw) {
        this.hostBW = bw;
    }

    public boolean isActiveFlag() {
        return activeFlag;
    }

    public void setActiveFlag(boolean activeFlag) {
        this.activeFlag = activeFlag;
    }

    @Override
    public long getBw() {
        return super.getBw();
    }
}
