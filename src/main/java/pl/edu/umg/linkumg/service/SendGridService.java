package pl.edu.umg.linkumg.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;

@Service
@AllArgsConstructor
public class SendGridService {

    private SendGrid sendGrid;

    public void sendEmail() throws IOException {
        Email from = new Email("umglink@gmail.com");
        String subject = "Sending with SendGrid is Fun";
        Email to = new Email("radek.paradecki@gmail.com");
        Content content = new Content("text/plain", "and easy to do anywhere, even with Java");
        var templateId = "d-acabfe4565e8431f83500137af6a8375";
//        Mail mail = new Mail(from, subject, to, content);
        Mail mail = new Mail();
        mail.setFrom(from);
        mail.setTemplateId(templateId);
        Personalization personalization = new Personalization();
        personalization.addDynamicTemplateData("name", "Radek");
        URL url = new URL("http://localhost:8080");
        personalization.addDynamicTemplateData("link", url);
        personalization.addTo(to);
        mail.addPersonalization(personalization);
        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());
        Response response = sendGrid.api(request);
        System.out.println(response.getStatusCode());
        System.out.println(response.getBody());
        System.out.println(response.getHeaders());
    }
}
