package com.example.lntapplication;

public class Data_Model_Search {
    public String productId;
    public String serialNo;
    public String drawingNo;
    public String sapNo;
    public String spoolNo;
    public String weight;
    public String contractor;
    public String location;
    public String rfidNo;
    public String remarks;
    public String createdAt;
    public String updatedAt;
    String  StatusF="false";
    String Color="Normal";

    public Data_Model_Search(String productId, String serialNo, String drawingNo, String sapNo, String spoolNo, String weight, String contractor, String location, String rfidNo, String remarks, String createdAt, String updatedAt) {
        this.productId = productId;
        this.serialNo = serialNo;
        this.drawingNo = drawingNo;
        this.sapNo = sapNo;
        this.spoolNo = spoolNo;
        this.weight = weight;
        this.contractor = contractor;
        this.location = location;
        this.rfidNo = rfidNo;
        this.remarks = remarks;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Data_Model_Search(String productId, String serialNo, String drawingNo, String sapNo, String spoolNo, String weight, String contractor, String location, String rfidNo, String remarks, String createdAt, String updatedAt, String statusF, String color) {
        this.productId = productId;
        this.serialNo = serialNo;
        this.drawingNo = drawingNo;
        this.sapNo = sapNo;
        this.spoolNo = spoolNo;
        this.weight = weight;
        this.contractor = contractor;
        this.location = location;
        this.rfidNo = rfidNo;
        this.remarks = remarks;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        StatusF = statusF;
        Color = color;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getDrawingNo() {
        return drawingNo;
    }

    public void setDrawingNo(String drawingNo) {
        this.drawingNo = drawingNo;
    }

    public String getSapNo() {
        return sapNo;
    }

    public void setSapNo(String sapNo) {
        this.sapNo = sapNo;
    }

    public String getSpoolNo() {
        return spoolNo;
    }

    public void setSpoolNo(String spoolNo) {
        this.spoolNo = spoolNo;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getContractor() {
        return contractor;
    }

    public void setContractor(String contractor) {
        this.contractor = contractor;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRfidNo() {
        return rfidNo;
    }

    public void setRfidNo(String rfidNo) {
        this.rfidNo = rfidNo;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getStatusF() {
        return StatusF;
    }

    public void setStatusF(String statusF) {
        StatusF = statusF;
    }

    public String getColor() {
        return Color;
    }

    public void setColor(String color) {
        Color = color;
    }
}
