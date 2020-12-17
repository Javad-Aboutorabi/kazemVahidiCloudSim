package org.cloudbus.cloudsim.examples.KazemVahedi;

import java.util.List;

public class FFD {
    //    private ArrayList<Integer> ffdResult = new ArrayList();
    private int ffdResult;

    public int ffd(List<IHost> hosts, IVm vm) {
//        print(hosts, vm);
        int numberOfHost = hosts.size();
        for (int j = 0; j < numberOfHost; j++) {
            if (hosts.get(j).getMips() >= vm.getMips() &&
                    hosts.get(j).getRam() >= vm.getRam() &&
                    hosts.get(j).getBw() >= vm.getBw()) {
                ffdResult = j;
                vm.setSet(true);
                hosts.get(j).setMips((int) (hosts.get(j).getMips() - vm.getMips()));
                hosts.get(j).setRam(hosts.get(j).getRam() - vm.getRam());
                hosts.get(j).setBw((hosts.get(j).getBw() - vm.getBw()));
                break;
            }
        }

        if (!vm.isSet()) {
            for (int j = 0; j < numberOfHost; j++) {
                if (hosts.get(j).getMips() >= vm.getMips() && hosts.get(j).getRam() >= vm.getRam()) {
                    vm.setSet(true);
                    ffdResult = j;
                    hosts.get(j).setMips((int) (hosts.get(j).getMips() - vm.getMips()));
                    hosts.get(j).setRam(hosts.get(j).getRam() - vm.getRam());
                    hosts.get(j).setBw(hosts.get(j).getBw() - vm.getBw());
                    break;
                }
            }
        }
        return ffdResult;
    }

    public static void print(List<IHost> hosts, IVm vm) {
        System.out.println("--------------Hosts--------------");
        for (IHost h : hosts) {
            System.out.println(
                    "host ID: " + h.getId() + "\t" + "Mips: " + h.getMips() +
                            "\t" + "Bw: " + h.getBw() + "\t" + "Ram: " + h.getRam());
        }
        System.out.println("-------------- Vms --------------");
        System.out.println("vm ID: " + vm.getId() + "\t" + "Type: " + vm.getType()
                + "\t" + "Mips: " + vm.getMips() + "\t" + "Ram: " + vm.getRam());
    }
}