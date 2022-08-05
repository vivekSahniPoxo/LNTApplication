package com.example.lntapplication;

public class BleDeviceDetails {
    String Address,Name;

    public BleDeviceDetails(String address, String name) {
        Address = address;
        Name = name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
