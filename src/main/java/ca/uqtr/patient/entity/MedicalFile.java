package ca.uqtr.patient.entity;

import ca.uqtr.patient.entity.vo.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.hibernate.annotations.*;
import org.jasypt.util.text.AES256TextEncryptor;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
@Table(name = "medical_file", schema = "public")
@TypeDef(
        name = "jsonb",
        typeClass = JsonBinaryType.class
)
public class MedicalFile extends BaseEntity {

    @Column(name = "patient")
    private String patient;
    @Column(name = "creation_date")
    private Date creationDate ;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "medical_file_id")
    private List<MedicalFileHistory> medicalFileHistory;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "medical_file_id")
    /*@OneToMany(
            mappedBy = "medicalFile",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )*/
    private List<ClinicalExamination> clinicalExamination;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "medical_file_id")
    private List<LipidProfile> lipidProfiles;



}
