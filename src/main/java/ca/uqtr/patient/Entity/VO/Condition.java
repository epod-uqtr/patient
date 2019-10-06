package ca.uqtr.patient.Entity.VO;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Condition {

    private String value;
}
