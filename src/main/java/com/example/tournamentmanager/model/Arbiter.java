package com.example.tournamentmanager.model;

public class Arbiter {
    String fullName;
    ArbiterTitle title;
    String license;

    String workDescription;

    public Arbiter(String fullName) {
        this(fullName, ArbiterTitle.NA);
    }

    public Arbiter(String fullName, ArbiterTitle title) {
        this(fullName, title, "");
    }

    public Arbiter(String fullName, ArbiterTitle title, String license) {
        this(fullName, title, license, "Sędzia wypełniał bardzo dobrze swoje obowiązki");
    }

    public Arbiter(String fullName, ArbiterTitle title, String license, String workDescription) {
        setFullName(fullName);
        setTitle(title);
        setLicense(license);
        setWorkDescription(workDescription);
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public ArbiterTitle getTitle() {
        return title;
    }

    public void setTitle(ArbiterTitle title) {
        this.title = title;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getWorkDescription() {
        return workDescription;
    }

    public void setWorkDescription(String workDescription) {
        this.workDescription = workDescription;
    }


}
