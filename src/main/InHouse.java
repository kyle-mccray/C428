package main;

public class InHouse extends Part {
    private int machineId;

    public InHouse(int id, String name, double price, int stock, int max, int min, int machineId) {
        super(id, name, price, stock, max, min);
        this.machineId = machineId;
    }

    public int getMachineId() {
        return machineId;
    }

    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }
}
