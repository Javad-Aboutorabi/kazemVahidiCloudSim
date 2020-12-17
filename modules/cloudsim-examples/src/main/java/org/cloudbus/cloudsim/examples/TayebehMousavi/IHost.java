package org.cloudbus.cloudsim.examples.TayebehMousavi;

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
    private int type;
    public final double busy = 215, idle = 162;
    private int ram;
    private boolean activeFlag;

    public double getHostCpu() {
        return hostCpu;
    }

    public double getHostRam() {
        return hostRam;
    }

    private double hostCpu, hostRam;

    public IHost(int id, boolean activeFlag, int type, RamProvisioner ramProvisioner, BwProvisioner bwProvisioner,
                 long storage, List<? extends Pe> peList, VmScheduler vmScheduler) {
        super(id, ramProvisioner, bwProvisioner, storage, peList, vmScheduler);
        this.type = type;
        this.activeFlag=activeFlag;
        mips = this.getTotalMips();;
        hostCpu = this.getTotalMips();;

        ram = getRamProvisioner().getRam();
        hostRam = getRamProvisioner().getRam();
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

    public double numberVmType1() {
        double numberVmType1 = 0;
        if (!iVmList.isEmpty()) {
            for (IVm vm : iVmList) {
                if (vm.getType() == 1)
                    numberVmType1++;
            }
            return numberVmType1;
        }
        return 0;
    }

    public double numberVmType2() {
        double numberVmType2 = 0;
        if (!iVmList.isEmpty()) {
            for (IVm vm : iVmList) {
                if (vm.getType() == 2)
                    numberVmType2++;
            }
            return numberVmType2;
        }
        return 0;
    }

    public double numberVmType3() {
        double numberVmType3 = 0;
        if (!iVmList.isEmpty()) {
            for (IVm vm : iVmList) {
                if (vm.getType() == 3)
                    numberVmType3++;
            }
            return numberVmType3;
        }
        return 0;
    }


    public double gini() {
       // double numberVmType1 = 0, numberVmType2 = 0, numberVmType3 = 0, gini, sum;
        double gini,sum;
        if (!iVmList.isEmpty()) {
          // for (IVm vm : iVmList) {
           //     if (vm.getType() == 1) numberVmType1++;
          //      else if (vm.getType() == 2) numberVmType2++;
          //      else if (vm.getType() == 3) numberVmType3++;
          //  }
            double a = (numberVmType1() / iVmList.size());
            double b = (numberVmType2() / iVmList.size());
            double c = (numberVmType3()/ iVmList.size());
            double type1= Math.pow(a,2);
            double type2= Math.pow(b,2);
            double type3= Math.pow(c,2);
            sum = type1 + type2+type3;
            // sum = ((numberVmType1 / iVmList.size()) + (numberVmType2 / iVmList.size()) + (numberVmType3 / iVmList.size()));
            //jini = (1 - (Math.pow(sum, 2)));
            gini= 1- sum;
            return gini;
        }
        return 0;
    }


    public int getType() {
        return type;
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

    public boolean isActiveFlag() {
        return activeFlag;
    }

    public void setActiveFlag(boolean activeFlag) {
        this.activeFlag = activeFlag;
    }
}
