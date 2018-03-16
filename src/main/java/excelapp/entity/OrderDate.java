package excelapp.entity;

public class OrderDate {


    private String dateOfRealization;
    private String supplierName;
    private String zdName;

    public OrderDate() {
    }

    public OrderDate(String dateOfRealization, String supplierName, String zdName) {
        this.dateOfRealization = dateOfRealization;
        this.supplierName = supplierName;
        this.zdName = zdName;
    }

    public String getZdName() {
        return zdName;
    }

    public void setZdName(String zdName) {
        this.zdName = zdName;
    }

    public String getDateOfRealization() {
        return dateOfRealization;
    }

    public void setDateOfRealization(String dateOfRealization) {
        this.dateOfRealization = dateOfRealization;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    @Override
    public String toString() {
        return "OrderDate{" +
                "dateOfRealization='" + dateOfRealization + '\'' +
                ", supplierName='" + supplierName + '\'' +
                ", zdName='" + zdName + '\'' +
                '}';
    }
}
