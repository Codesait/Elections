package com.example.chiemela.election_waka.Model;

public class VideoModel {

    private String reporterName, Coordinates, typeOfElection;

    public VideoModel() {
    }

    public VideoModel(String reporterName, String coordinates, String typeOfElection) {
        this.reporterName = reporterName;
        this.Coordinates = coordinates;
        this.typeOfElection = typeOfElection;
    }

    public String getReporterName() {
        return reporterName;
    }

    public void setReporterName(String reporterName) {
        this.reporterName = reporterName;
    }

    public String getCoordinates() {
        return Coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.Coordinates = coordinates;
    }

    public String getTypeOfElection() {
        return typeOfElection;
    }

    public void setTypeOfElection(String typeOfElection) {
        this.typeOfElection = typeOfElection;
    }

}
