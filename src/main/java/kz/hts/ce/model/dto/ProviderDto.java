package kz.hts.ce.model.dto;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ProviderDto {

    private StringProperty companyName;
    private StringProperty contactPerson;
    private StringProperty email;
    private StringProperty cityName;
    private StringProperty address;
    private IntegerProperty iin;
    private IntegerProperty bin;

    public String getCompanyName() {
        return companyName.get();
    }

    public StringProperty companyNameProperty() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        if (this.companyName == null) this.companyName = new SimpleStringProperty();
        this.companyName.set(companyName);
    }

    public String getContactPerson() {
        return contactPerson.get();
    }

    public StringProperty contactPersonProperty() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        if (this.contactPerson == null) this.contactPerson = new SimpleStringProperty();
        this.contactPerson.set(contactPerson);
    }

    public String getEmail() {
        return email.get();
    }

    public StringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        if (this.email == null) this.email = new SimpleStringProperty();
        this.email.set(email);
    }

    public String getCityName() {
        return cityName.get();
    }

    public StringProperty cityNameProperty() {
        return cityName;
    }

    public void setCityName(String cityName) {
        if (this.cityName == null) this.cityName = new SimpleStringProperty();
        this.cityName.set(cityName);
    }

    public String getAddress() {
        return address.get();
    }

    public StringProperty addressProperty() {
        return address;
    }

    public void setAddress(String address) {
        if (this.address == null) this.address = new SimpleStringProperty();
        this.address.set(address);
    }

    public int getIin() {
        return iin.get();
    }

    public IntegerProperty iinProperty() {
        return iin;
    }

    public void setIin(int iin) {
        if (this.iin == null) this.iin = new SimpleIntegerProperty();
        this.iin.set(iin);
    }

    public int getBin() {
        return bin.get();
    }

    public IntegerProperty binProperty() {
        return bin;
    }

    public void setBin(int bin) {
        if (this.bin == null) this.bin = new SimpleIntegerProperty();
        this.bin.set(bin);
    }
}
