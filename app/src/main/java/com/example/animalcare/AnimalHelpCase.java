package com.example.animalcare;

public class AnimalHelpCase {

    private String userName;
    private String animalType;
    private String userLocation;
    private double latitude;
    private double longitude;


    public AnimalHelpCase( String userName, String animalType, String userLocation, double latitude, double longitude) {
        this.animalType = animalType;
        this.userName = userName;
        this.userLocation = userLocation;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getUserName() {
        return userName;
    }

    public String getAnimalType() {
        return animalType;
    }

    public String getUserLocation() {
        return userLocation;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
