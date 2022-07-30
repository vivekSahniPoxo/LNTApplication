package com.example.lntapplication;

public class ReportDatabase {
    String ProductId,SerialNo,DrawingNo,SapNo,SpoolNo,Weight,Contractor,Location,RfidNo,Remarks,CreatedAt,UpdatedAt,Status;

    public ReportDatabase(String productId, String serialNo, String drawingNo, String sapNo, String spoolNo, String weight, String contractor, String location, String rfidNo, String remarks, String createdAt, String updatedAt, String status) {
        ProductId = productId;
        SerialNo = serialNo;
        DrawingNo = drawingNo;
        SapNo = sapNo;
        SpoolNo = spoolNo;
        Weight = weight;
        Contractor = contractor;
        Location = location;
        RfidNo = rfidNo;
        Remarks = remarks;
        CreatedAt = createdAt;
        UpdatedAt = updatedAt;
        Status = status;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public ReportDatabase() {
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public String getSerialNo() {
        return SerialNo;
    }

    public void setSerialNo(String serialNo) {
        SerialNo = serialNo;
    }

    public String getDrawingNo() {
        return DrawingNo;
    }

    public void setDrawingNo(String drawingNo) {
        DrawingNo = drawingNo;
    }

    public String getSapNo() {
        return SapNo;
    }

    public void setSapNo(String sapNo) {
        SapNo = sapNo;
    }

    public String getSpoolNo() {
        return SpoolNo;
    }

    public void setSpoolNo(String spoolNo) {
        SpoolNo = spoolNo;
    }

    public String getWeight() {
        return Weight;
    }

    public void setWeight(String weight) {
        Weight = weight;
    }

    public String getContractor() {
        return Contractor;
    }

    public void setContractor(String contractor) {
        Contractor = contractor;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getRfidNo() {
        return RfidNo;
    }

    public void setRfidNo(String rfidNo) {
        RfidNo = rfidNo;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public String getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        CreatedAt = createdAt;
    }

    public String getUpdatedAt() {
        return UpdatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        UpdatedAt = updatedAt;
    }
}
