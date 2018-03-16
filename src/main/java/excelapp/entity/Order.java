package excelapp.entity;

import java.util.Map;

public class Order {

    private String orderNum;

    private String supplier;

    private Map<Integer, Product> productList;

    private String currency;

    private String lastModyfiedDate;


    public Order(String orderNum, String supplier, Map<Integer, Product> productList, String currency, String lastModyfiedDate) {
        this.orderNum = orderNum;
        this.supplier = supplier;
        this.productList = productList;
        this.currency = currency;
        this.lastModyfiedDate = lastModyfiedDate;
    }

    public Order() {

    }

    public Order(String orderNum, String supplier, Map<Integer, Product> productList, String currency) {
        this.orderNum = orderNum;
        this.supplier = supplier;
        this.productList = productList;
        this.currency = currency;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public Map<Integer, Product> getProductList() {
        return productList;
    }

    public void setProductList(Map<Integer, Product> productList) {
        this.productList = productList;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getLastModyfiedDate() {
        return lastModyfiedDate;
    }

    public void setLastModyfiedDate(String lastModyfiedDate) {
        this.lastModyfiedDate = lastModyfiedDate;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderNum='" + orderNum + '\'' +
                ", supplier='" + supplier + '\'' +
                ", productList=" + productList +
                ", currency='" + currency + '\'' +
                ", lastModyfiedDate='" + lastModyfiedDate + '\'' +
                '}';
    }
}
