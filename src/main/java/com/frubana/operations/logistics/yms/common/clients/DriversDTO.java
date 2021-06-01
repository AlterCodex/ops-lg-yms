package com.frubana.operations.logistics.yms.common.clients;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.joda.time.DateTime;

/**
 * InnerDriversClient
 */
public class DriversDTO {
    private String id;
     private String phone;
     private LocalDateTime arrivalTime;
     private VehicleDTO vehicle;

     public DriversDTO(){};

     public void setId(String id){
        this.id=id;
    }

    public void setPhone(String phone){
        this.phone=phone;
    }
    public void setArrival_time(String arrivalTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        this.arrivalTime = LocalDateTime.parse(arrivalTime, formatter);
    }
    public void setVehicle(VehicleDTO vehicle){
        this.vehicle=vehicle;
    }

    public String getId(){
        return this.id;
    }
    public String getPhone(){
        return this.phone;
    }
    public LocalDateTime getArravalTime(){
        return this.arrivalTime;
    }
    public VehicleDTO getVehicleDTO(){
        return this.vehicle;
    }

    
}