package com.example.chiemela.election_waka.Model;

/**
 * Created by iduma on 3/28/18.
 */

public class CamModel {
    private String uid, reporterName, Coordinates,elecionType, helpReports;

    public CamModel() {
        // empty constructor needed
    }

    public CamModel(String uid, String reporterName, String Coordinates, String elecionType) {
        this.uid = uid;
        this.reporterName = reporterName;
        this.Coordinates = Coordinates;
        this.elecionType = elecionType;
        this.helpReports = helpReports;

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public void setCoordinates(String Coordinates) {
        this.Coordinates = Coordinates;
    }

    public String getElecionType() {
        return elecionType;
    }

    public void setElecionType(String elecionType) {
        this.elecionType = elecionType;
    }

    public String getHelpReports() {
        return helpReports;
    }

    public void setHelpReports(String helpReports) {
        this.helpReports = helpReports;
    }
}
