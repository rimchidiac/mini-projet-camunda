package com.example.workflow;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import javax.inject.Named;

@Named
public class SendResponseEmail implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) throws Exception {

        //variables from camunda modeler
        String recipient = (String) execution.getVariable("email");
        String status = (String) execution.getVariable("status");

        String user_name = "miniprojetcamunda";  // GMail user name (just the part before "@gmail.com")
        String password = "IAEcamunda2021"; // GMail password

        String[] to={recipient};
        String subject = "fyp response email";
        String body="";

        if (status.equals("rejected")){
            body="We're sorry to announce that your project has been rejected from the fyp process :(";
        }
        else{
            body= "Congratulations, your project is one of the few projects that has been accepted into the fyp process!!";
        }

        sendFromGMail(user_name, password, to, subject, body);

    }

    private static void sendFromGMail(String from, String pass, String[] to, String subject, String body) {
        Properties props = System.getProperties();
        String host = "smtp.gmail.com";

        props.put("mail.smtp.starttls.enable", "true");

        props.put("mail.smtp.ssl.trust", host);
        props.put("mail.smtp.user", from);
        props.put("mail.smtp.password", pass);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");


        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);

        try {


            message.setFrom(new InternetAddress(from));
            InternetAddress[] toAddress = new InternetAddress[to.length];

            // To get the array of addresses
            for( int i = 0; i < to.length; i++ ) {
                toAddress[i] = new InternetAddress(to[i]);
            }

            for( int i = 0; i < toAddress.length; i++) {
                message.addRecipient(Message.RecipientType.TO, toAddress[i]);
            }



            message.setSubject(subject);
            message.setText(body);


            Transport transport = session.getTransport("smtp");


            transport.connect(host, from, pass);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();

        }
        catch (AddressException ae) {
            ae.printStackTrace();
        }
        catch (MessagingException me) {
            me.printStackTrace();
        }
    }
}
