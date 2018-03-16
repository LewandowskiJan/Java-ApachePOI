package excelapp.entity;


public class Product {

    private String sku;
    private String name;
    private Double price;
    private Double quantityFv;
    private Double quantityOrdered;
    private Double quantityRealized;
    private Double available;
    private Double stock;

    private Double orderT;
    private Double realizedT;

    private String productBrand;


    public Product() {

    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getQuantityFv() {
        return quantityFv;
    }

    public void setQuantityFv(Double quantityFv) {
        this.quantityFv = quantityFv;
    }

    public Double getQuantityOrdered() {
        return quantityOrdered;
    }

    public void setQuantityOrdered(Double quantityOrdered) {
        this.quantityOrdered = quantityOrdered;
    }

    public Double getQuantityRealized() {
        return quantityRealized;
    }

    public void setQuantityRealized(Double quantityRealized) {
        this.quantityRealized = quantityRealized;
    }

    public Double getAvailable() {
        return available;
    }

    public void setAvailable(Double available) {
        this.available = available;
    }

    public Double getStock() {
        return stock;
    }

    public void setStock(Double stock) {
        this.stock = stock;
    }

    public Double getOrderT() {
        return orderT;
    }

    public void setOrderT(Double orderT) {
        this.orderT = orderT;
    }

    public Double getRealizedT() {
        return realizedT;
    }

    public void setRealizedT(Double realizedT) {
        this.realizedT = realizedT;
    }

    public String getProductBrand() {
        return productBrand;
    }

    public void setProductBrand(String productBrand) {
        this.productBrand = productBrand;
    }

    @Override
    public String toString() {
        return "Product{" +
                "sku='" + sku + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", quantityFv=" + quantityFv +
                ", quantityOrdered=" + quantityOrdered +
                ", quantityRealized=" + quantityRealized +
                ", available=" + available +
                ", stock=" + stock +
                ", orderT=" + orderT +
                ", realizedT=" + realizedT +
                ", productBrand='" + productBrand + '\'' +
                '}';
    }
}
