package kz.hts.ce.entity;

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
public class Admin extends BaseEntity {

    public static final String NAME_REGEX = "^[a-zA-Z\u0430-\u044f\u0410-\u042f\u0451\u0401]" +
            "[a-zA-Z-\u0430-\u044f\u0410-\u042f\u0451\u0401]+[a-zA-Z\u0430-\u044f\u0410-\u042f\u0451\u0401]?$";

    @NotEmpty
    @Size(min = 3, max = 14)
    @Pattern(regexp = "^[a-z0-9_-]+[a-z0-9_-]$")
    private String username;

    @NotEmpty
    @Email
    @Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
    private String email;

    @NotEmpty
    private String password;

    @NotEmpty
    @Size(min = 2, max = 15)
    @Pattern(regexp = NAME_REGEX)
    private String name;

    @NotEmpty
    @Size(min = 2, max = 15)
    @Pattern(regexp = NAME_REGEX)
    private String surname;

    @Size(min = 2, max = 15)
    @Pattern(regexp = NAME_REGEX)
    private String patronymic;

    @Pattern(regexp = "\\(?([0-9]{3})\\)?([ .-]?)([0-9]{3})\\2([0-9]{4})|")
    @Column(nullable = true)
    private String phone;

    @ManyToOne
    @JoinColumn(name = "gender_id", nullable = false)
    private Gender gender;

    @Column(name = "start_work_date", nullable = false)
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date startWorkDate;

    @Column(name = "end_work_date", nullable = true)
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date endWorkDate;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Column(name = "is_blocked")
    private boolean blocked;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", phone='" + phone + '\'' +
                ", gender=" + gender +
                ", startWorkDate=" + startWorkDate +
                ", endWorkDate=" + endWorkDate +
                ", role=" + role +
                ", blocked=" + blocked +
                '}';
    }
}
