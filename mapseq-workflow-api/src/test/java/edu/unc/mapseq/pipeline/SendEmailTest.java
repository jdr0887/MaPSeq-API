package edu.unc.mapseq.pipeline;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.junit.Test;

public class SendEmailTest {

    @Test
    public void testSendEmail() {

        String toEmailAddress = "jdr0887@gmail.com";
        String fromEmailAddress = "jreilly@unc.edu";
        Properties properties = System.getProperties();
        properties.setProperty("mail.user", "jreilly");
        properties.setProperty("mail.smtps.host", "smtp.unc.edu");
        properties.setProperty("mail.smtps.port", "465");
        properties.setProperty("mail.smtps.auth", "true");
        Session session = Session.getDefaultInstance(properties);
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmailAddress));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmailAddress));
            message.setSubject(String.format("The %s pipeline has finished.", "Test"));
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("WorkflowRun Name: %s%n", "Test"));
            sb.append(String.format("SequencerRun Name: %s%n", "Test"));
            message.setText(sb.toString());
            Transport.send(message);
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }

    }
}
