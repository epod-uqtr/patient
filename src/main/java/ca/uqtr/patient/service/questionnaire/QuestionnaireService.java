package ca.uqtr.patient.service.questionnaire;

import ca.uqtr.patient.dto.PatientDto;
import ca.uqtr.patient.dto.QuestionnaireDto;
import ca.uqtr.patient.dto.Response;
import ca.uqtr.patient.entity.Questionnaire;

import java.util.List;

public interface QuestionnaireService {


    Response addQuestionnaire(QuestionnaireDto questionnaireDto);
    void sendQuestionnaire(PatientDto patientDto);

    Response getQuestionnairesByPatient(String patientId);
}
