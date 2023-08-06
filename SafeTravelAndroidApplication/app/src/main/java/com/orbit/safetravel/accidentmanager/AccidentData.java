package com.orbit.safetravel.accidentmanager;

import com.google.gson.annotations.SerializedName;

public class AccidentData {
    @SerializedName("_id")
    private String id;

    @SerializedName("Accident_Index")
    private String accidentIndex;

    @SerializedName("Accident Date")
    private String accidentDate;

    @SerializedName("Day_of_Week")
    private String dayOfWeek;

    @SerializedName("Junction_Control")
    private String junctionControl;

    @SerializedName("Junction_Detail")
    private String junctionDetail;

    @SerializedName("Accident_Severity")
    private String accidentSeverity;

    @SerializedName("Latitude")
    private double latitude;

    @SerializedName("Light_Conditions")
    private String lightConditions;

    @SerializedName("Local_Authority_(District)")
    private String localAuthorityDistrict;

    @SerializedName("Carriageway_Hazards")
    private String carriagewayHazards;

    @SerializedName("Longitude")
    private double longitude;

    @SerializedName("Number_of_Casualties")
    private int numberOfCasualties;

    @SerializedName("Number_of_Vehicles")
    private int numberOfVehicles;

    @SerializedName("Police_Force")
    private String policeForce;

    @SerializedName("Road_Surface_Conditions")
    private String roadSurfaceConditions;

    @SerializedName("Road_Type")
    private String roadType;

    @SerializedName("Speed_limit")
    private int speedLimit;

    @SerializedName("Time")
    private String time;

    @SerializedName("Urban_or_Rural_Area")
    private String urbanOrRuralArea;

    @SerializedName("Weather_Conditions")
    private String weatherConditions;

    @SerializedName("Vehicle_Type")
    private String vehicleType;

    public AccidentData() {

    }

    public String getAccidentIndex() {
        return accidentIndex;
    }

    public void setAccidentIndex(String accidentIndex) {
        this.accidentIndex = accidentIndex;
    }

    public String getAccidentDate() {
        return accidentDate;
    }

    public void setAccidentDate(String accidentDate) {
        this.accidentDate = accidentDate;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getJunctionControl() {
        return junctionControl;
    }

    public void setJunctionControl(String junctionControl) {
        this.junctionControl = junctionControl;
    }

    public String getJunctionDetail() {
        return junctionDetail;
    }

    public void setJunctionDetail(String junctionDetail) {
        this.junctionDetail = junctionDetail;
    }

    public String getAccidentSeverity() {
        return accidentSeverity;
    }

    public void setAccidentSeverity(String accidentSeverity) {
        this.accidentSeverity = accidentSeverity;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getLightConditions() {
        return lightConditions;
    }

    public void setLightConditions(String lightConditions) {
        this.lightConditions = lightConditions;
    }

    public String getLocalAuthorityDistrict() {
        return localAuthorityDistrict;
    }

    public void setLocalAuthorityDistrict(String localAuthorityDistrict) {
        this.localAuthorityDistrict = localAuthorityDistrict;
    }

    public String getCarriagewayHazards() {
        return carriagewayHazards;
    }

    public void setCarriagewayHazards(String carriagewayHazards) {
        this.carriagewayHazards = carriagewayHazards;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getNumberOfCasualties() {
        return numberOfCasualties;
    }

    public void setNumberOfCasualties(int numberOfCasualties) {
        this.numberOfCasualties = numberOfCasualties;
    }

    public int getNumberOfVehicles() {
        return numberOfVehicles;
    }

    public void setNumberOfVehicles(int numberOfVehicles) {
        this.numberOfVehicles = numberOfVehicles;
    }

    public String getPoliceForce() {
        return policeForce;
    }

    public void setPoliceForce(String policeForce) {
        this.policeForce = policeForce;
    }

    public String getRoadSurfaceConditions() {
        return roadSurfaceConditions;
    }

    public void setRoadSurfaceConditions(String roadSurfaceConditions) {
        this.roadSurfaceConditions = roadSurfaceConditions;
    }

    public String getRoadType() {
        return roadType;
    }

    public void setRoadType(String roadType) {
        this.roadType = roadType;
    }

    public int getSpeedLimit() {
        return speedLimit;
    }

    public void setSpeedLimit(int speedLimit) {
        this.speedLimit = speedLimit;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUrbanOrRuralArea() {
        return urbanOrRuralArea;
    }

    public void setUrbanOrRuralArea(String urbanOrRuralArea) {
        this.urbanOrRuralArea = urbanOrRuralArea;
    }

    public String getWeatherConditions() {
        return weatherConditions;
    }

    public void setWeatherConditions(String weatherConditions) {
        this.weatherConditions = weatherConditions;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Add getters and setters for all the fields
    @Override
    public String toString() {
        return "AccidentData{" +
                "id='" + id + '\'' +
                ", accidentIndex='" + accidentIndex + '\'' +
                ", accidentDate='" + accidentDate + '\'' +
                ", dayOfWeek='" + dayOfWeek + '\'' +
                ", junctionControl='" + junctionControl + '\'' +
                ", junctionDetail='" + junctionDetail + '\'' +
                ", accidentSeverity='" + accidentSeverity + '\'' +
                ", latitude=" + latitude +
                ", lightConditions='" + lightConditions + '\'' +
                ", localAuthorityDistrict='" + localAuthorityDistrict + '\'' +
                ", carriagewayHazards='" + carriagewayHazards + '\'' +
                ", longitude=" + longitude +
                ", numberOfCasualties=" + numberOfCasualties +
                ", numberOfVehicles=" + numberOfVehicles +
                ", policeForce='" + policeForce + '\'' +
                ", roadSurfaceConditions='" + roadSurfaceConditions + '\'' +
                ", roadType='" + roadType + '\'' +
                ", speedLimit=" + speedLimit +
                ", time='" + time + '\'' +
                ", urbanOrRuralArea='" + urbanOrRuralArea + '\'' +
                ", weatherConditions='" + weatherConditions + '\'' +
                ", vehicleType='" + vehicleType + '\'' +
                '}';
    }
}

