package ca.uqtr.patient.entity;

import ca.uqtr.patient.entity.vo.Contact;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.sql.Date;
import java.util.*;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "patient", schema = "public")
@TypeDef(
        name = "jsonb",
        typeClass = JsonBinaryType.class
)
public class Patient extends BaseEntity{

    @NaturalId
    @Column(name = "file_number", nullable = false, updatable = false, unique = true)
    private String fileNumber;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "birthday")
    private Date birthday;
    @Column(name = "mother_name")
    private String motherName;
    @Column(name = "gender")
    private String gender;
    @JsonManagedReference
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "patient")
    private Contact contact;
    @Type(type = "jsonb")
    @Column(name = "family_doctor", columnDefinition = "jsonb")
    private String familyDoctor;
    @Type(type = "jsonb")
    @Column(name = "pharmacy", columnDefinition = "jsonb")
    private String pharmacy;
    @Column(name = "is_active")
    private Boolean isActive;
    @Column(name = "questionnaire_token")
    private String questionnaireToken ;
    @Column(name = "questionnaire_token_expiration_date")
    private Date questionnaireTokenExpirationDate ;
    @Column(name = "login_code")
    private String loginCode;
    @Type(type = "jsonb")
    @Column(name = "socio_demographic_variables", columnDefinition = "jsonb")
    private String socioDemographicVariables;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "patient_professional", joinColumns = {
            @JoinColumn(name = "patient_id", referencedColumnName = "id")}, inverseJoinColumns = {
            @JoinColumn(name = "professional_id", referencedColumnName = "id")})
    private Set<Professional> professionals = new HashSet<>();
/*
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "patient_id")
*/
    @JsonManagedReference
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Questionnaire> questionnaires;

    @JsonManagedReference
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appointment> appointments;
    @JsonManagedReference
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Recommendation> recommendations;


    public Patient(String firstName, String lastName, Date birthday, Boolean isActive) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.isActive = isActive;
    }

    public Patient(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public void setFileNumber() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getBirthday());
        int day = cal.get(Calendar.DAY_OF_MONTH) ;
        int month = (cal.get(Calendar.MONTH) + 1);
        int year = cal.get(Calendar.YEAR);
        this.fileNumber = getFirstName().toUpperCase().substring(0, 3) +
                getLastName().toUpperCase().substring(0, 3) +
                (day<10?("0"+day):(day))+ (month<10?("0"+month):(month)) + year +
                UUID.randomUUID().toString().substring(0, 4).toUpperCase();
    }


    public void setContact(Contact contact) {
        if (contact == null) {
            if (this.contact != null) {
                this.contact.setPatient(null);
            }
        }
        else {
            contact.setPatient(this);
        }
        this.contact = contact;
    }

    public void setLoginCode() {
        this.loginCode = UUID.randomUUID().toString().substring(0, 4).toUpperCase();
    }

}
