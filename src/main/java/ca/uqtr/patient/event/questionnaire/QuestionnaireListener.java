package ca.uqtr.patient.event.questionnaire;

import ca.uqtr.patient.dto.PatientDto;
import ca.uqtr.patient.service.patient.PatientService;
import ca.uqtr.patient.service.questionnaire.QuestionnaireService;
import com.sendgrid.*;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
public class QuestionnaireListener implements
        ApplicationListener<OnQuestionnaireSendEvent> {

    @Value("${spring.profiles.active}")
    private String mailService;
    private final JavaMailSender mailSender;
    private PatientService patientService;
    @Value("${patient-service.questionnaire.url}")
    private String QUESTIONNAIRE_URL;

    @Autowired
    public QuestionnaireListener(JavaMailSender mailSender, PatientService patientService) {
        this.mailSender = mailSender;
        this.patientService = patientService;
    }

    @SneakyThrows
    @Override
    public void onApplicationEvent(OnQuestionnaireSendEvent event) {
        if (mailService.equals("dev"))
            this.confirmQuestionnaireSendGmail(event);
        else
            this.confirmQuestionnaireSendGrid(event);
        /*this.confirmRegistrationSendGrid(event);*/
    }

    private void confirmQuestionnaireSendGrid(OnQuestionnaireSendEvent event) throws IOException {
        PatientDto patient = event.getPatient();
        int rdv = event.getRdv();
        String token = UUID.randomUUID().toString();
        patientService.createQuestionnaireToken(patient.getId().toString(), token);
        String recipientAddress = patient.getContact().getEmail();
        String subject;
        if (rdv == 1)
            subject = "POD iSante - Personal informations and BREQ questionnaire!";
        else
            subject = "POD iSante - JPAQ questionnaire and recommendations!";
        System.out.println(subject);
        Email from = new Email("zinnour@uqtr.ca");
        Email to = new Email(recipientAddress);
        /*@Value("${mail.uri}")*/
        //String URI_HEROKU = "https://epod-zuul.herokuapp.com/api/v1/auth-service/update/password?token=";
        String URI_HEROKU = "http://localhost:4200/patient/questionnaire?token=";
        String confirmationUrl
                = QUESTIONNAIRE_URL + token;
        String message = "PIN : "+patient.getLoginCode()+". To fill your questionnaire click here : ";
        Content content = new Content("text/plain", message+confirmationUrl);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
        Request request = new Request();
        try {
            request.method = Method.POST;
            request.endpoint = "mail/send";
            request.body = mail.build();
            sg.api(request);
        } catch (IOException ex) {
            throw ex;
        }
    }


    private void confirmQuestionnaireSendGmail(OnQuestionnaireSendEvent event) {
        PatientDto patient = event.getPatient();
        int rdv = event.getRdv();
        String token = UUID.randomUUID().toString();
        patientService.createQuestionnaireToken(patient.getId().toString(), token);
        assert patient.getContact() != null;
        String recipientAddress = patient.getContact().getEmail();
        String subject;
        if (rdv == 1)
            subject = "POD iSante - Informations personal and BREQ questionnaire!";
        else
            subject = "POD iSante - JPAQ questionnaire and recommendations!";
        String URI_GMAIL = "http://localhost:8762/api/v1/patient-service/questionnaire?token=";
        String confirmationUrl
                = QUESTIONNAIRE_URL + token;
        String message = "PIN : "+patient.getQuestionnaireToken()+". To fill your questionnaire click here : ";

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message+confirmationUrl);
        mailSender.send(email);
    }

}


