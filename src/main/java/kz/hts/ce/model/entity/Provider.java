package kz.hts.ce.model.entity;

import org.hibernate.annotations.Proxy;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Audited
@Proxy(lazy = false)
public class Provider extends BaseEntity {

    @Size(min = 3, max = 14)
    @Pattern(regexp = "^[a-z0-9_-]+[a-z0-9_-]$")
    private String username;

    private String password;

    @ManyToOne
    @JoinColumn(name = "city_id", nullable = false)
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private City city;

    @Size(max = 100)
    private String address;

    @NotEmpty
    @Email
    @Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
    private String email;

    @Size(max = 100)
    @Column(name = "contact_person", nullable = false)
    private String contactPerson;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private Role role;

    @Size(max = 30)
    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(name = "start_work_date", nullable = false)
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date startWorkDate;

    @Column(name = "end_work_date")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date endWorkDate;

    @Column(name = "is_blocked", nullable = false)
    private boolean blocked;

    @Size(max = 20)
    private String iin;

    @Size(max = 20)
    private String bin;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getStartWorkDate() {
        return startWorkDate;
    }

    public void setStartWorkDate(Date startWorkDate) {
        this.startWorkDate = startWorkDate;
    }

    public Date getEndWorkDate() {
        return endWorkDate;
    }

    public void setEndWorkDate(Date endWorkDate) {
        this.endWorkDate = endWorkDate;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public String getIin() {
        return iin;
    }

    public void setIin(String iin) {
        this.iin = iin;
    }

    public String getBin() {
        return bin;
    }

    public void setBin(String bin) {
        this.bin = bin;
    }

    @Override
    public String toString() {
        return "Provider{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", city=" + city +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", contactPerson='" + contactPerson + '\'' +
                ", role=" + role +
                ", companyName='" + companyName + '\'' +
                ", startWorkDate=" + startWorkDate +
                ", endWorkDate=" + endWorkDate +
                ", blocked=" + blocked +
                ", iin='" + iin + '\'' +
                ", bin='" + bin + '\'' +
                '}';
    }
}
