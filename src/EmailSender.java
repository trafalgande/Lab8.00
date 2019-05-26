import javax.mail.*;
import javax.mail.internet.*;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailSender {

    final String username = "developem.net@mail.ru";
    final String password = "asdfghjkl123";

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }



    public void send(String toSend, String email) {

        //Отправить E-Mail

        //Скачать javamail api (javax.mail.jar) отсюда http://www.oracle.com/technetwork/java/javamail/index.html
        //В Intellij IDEA в меню File->Project Structure...->Libraries нажать плюсик и добавить этот файл к проекту

        //Тто же самое сделать для JAF (activation.jar): http://www.oracle.com/technetwork/java/javase/jaf-136260.html

        //Если что-нибудь не получается, возможно сам почтовик блокирует авторизацию через ненадёжные приложения. (так по-умалчанию делает gmail.com и это отключается в личном кабинета)

        if(validate(email)) {


            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.mail.ru");
            props.put("mail.smtp.port", "587");

            Session session = Session.getInstance(props,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(username, password);
                        }
                    });

            try {

                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("developem.net@mail.ru"));
                message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(email));
                message.setSubject("Your password");
                message.setText(toSend);

                Transport.send(message);

                System.out.println("Check email");

            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            System.out.println("Incorrect email");
            send(toSend, email);
        }
    }
}