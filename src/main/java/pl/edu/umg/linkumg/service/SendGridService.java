package pl.edu.umg.linkumg.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.edu.umg.linkumg.models.tokens.ActivationToken;
import pl.edu.umg.linkumg.models.tokens.Token;

import java.io.IOException;
import java.net.URL;

@Service
public class SendGridService {

    private static final Logger LOGGER = LogManager.getLogger(SendGridService.class);
    private static final String EMAIL_FROM_STRING = "umglink@gmail.com";
    private static final String ACTIVATION_EMAIL_TEMPLATE_ID = "d-acabfe4565e8431f83500137af6a8375";
    private static final String PASSWORD_REMIND_EMAIL_TEMPLATE_ID = "d-ffbc23d4ad184bec8880e0a1fafabe72";
    private static final Email EMAIL_FROM = new Email(EMAIL_FROM_STRING);

    @Value("${environment.url}")
    private String url;
    private SendGrid sendGrid;

    @Autowired
    public SendGridService(SendGrid sendGrid) {
        this.sendGrid = sendGrid;
    }

    public void sendEmail(Token token) {
        Email emailTo = new Email(token.getUser()
                                       .getEmail());

        Mail mail = new Mail();
        mail.setFrom(EMAIL_FROM);
        String path;
        if (token instanceof ActivationToken) {
            mail.setTemplateId(ACTIVATION_EMAIL_TEMPLATE_ID);
            path = "/activate";
        } else {
            mail.setTemplateId(PASSWORD_REMIND_EMAIL_TEMPLATE_ID);
            path = "/changePassword";
        }

        try {
            Personalization personalization = new Personalization();
            personalization.addDynamicTemplateData("name", token.getUser()
                                                                .getName());
            URL url = new URL(
                    ServletUriComponentsBuilder
                            .fromUriString(this.url)
                            .path(path)
                            .queryParam("token", token.getToken())
                            .build()
                            .toUriString()
            );

            personalization.addDynamicTemplateData("link", url);
            personalization.addTo(emailTo);
            mail.addPersonalization(personalization);

            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            sendGrid.api(request);

        } catch (IOException e) {
            LOGGER.error(e);
        }
    }
}
