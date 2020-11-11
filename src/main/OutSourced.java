package main;

public class OutSourced extends Part {
    private String companyName;

    public OutSourced(int id, String name, double price, int stock, int max, int min, String companyName) {
        super(id, name, price, stock, max, min);
        this.companyName = companyName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
