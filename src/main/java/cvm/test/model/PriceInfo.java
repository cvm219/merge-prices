package cvm.test.model;

import java.util.Date;
import java.util.Objects;

public class PriceInfo {

    private Long id;
    private String productCode;
    private int number;
    private int depart;
    private Date begin;
    private Date end;
    private long value;

    public PriceInfo() {
    }

    public PriceInfo(Long id, String productCode, int number, int depart, Date begin, Date end, long value) {
        this.id = id;
        this.productCode = productCode;
        this.number = number;
        this.depart = depart;
        this.begin = begin;
        this.end = end;
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getDepart() {
        return depart;
    }

    public void setDepart(int depart) {
        this.depart = depart;
    }

    public Date getBegin() {
        return begin;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PriceInfo priceInfo = (PriceInfo) o;
        return number == priceInfo.number &&
                depart == priceInfo.depart &&
                value == priceInfo.value &&
                Objects.equals(productCode, priceInfo.productCode) &&
                Objects.equals(begin, priceInfo.begin) &&
                Objects.equals(end, priceInfo.end);
    }

    public String toMyString(Date dt) {
        return "PriceInfo{" +
                "id=" + id +
                ", productCode='" + productCode + '\'' +
                ", number=" + number +
                ", depart=" + depart +
                ", begin=" + (begin.getTime() - dt.getTime()) / 1000 / 60 / 60 / 24 +
                ", end=" + (end.getTime() - dt.getTime()) / 1000 / 60 / 60 / 24 +
                ", value=" + value +
                '}';
    }
}
