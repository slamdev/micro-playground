package com.github.slamdev.micro.playground.services.mailer.business.control;

import com.github.slamdev.micro.playground.libs.spring.conventions.Base64DecodedResource;
import com.github.slamdev.micro.playground.services.mailer.business.entity.MailData;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

import javax.mail.internet.InternetAddress;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

@Component
@RequiredArgsConstructor
public class EmailSender {

    private final JavaMailSender javaMailSender;

    public void sendMail(MailData mailData) {
        MimeMessagePreparator preparator = toPreparedMessage(mailData);
        javaMailSender.send(preparator);
    }

    private static MimeMessagePreparator toPreparedMessage(MailData mailData) {
        return message -> {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(mailData.getFromPerson().getEmail(), mailData.getFromPerson().getName());
            helper.setText(mailData.getText(), mailData.isHtml());
            helper.setSubject(mailData.getSubject());
            helper.setTo(toInternetAddresses(mailData.getToPersons()));
            helper.setCc(toInternetAddresses(mailData.getCarbonCopies()));
            helper.setBcc(toInternetAddresses(mailData.getBlindCarbonCopies()));
            mailData.getAttachments().forEach(a -> addAttachment(helper, a));
        };
    }

    private static InternetAddress[] toInternetAddresses(List<MailData.Person> people) {
        return people.stream().map(EmailSender::toInternetAddress).toArray(InternetAddress[]::new);
    }

    @SneakyThrows
    private static InternetAddress toInternetAddress(MailData.Person person) {
        return new InternetAddress(person.getEmail(), person.getName(), UTF_8.displayName());
    }

    @SneakyThrows
    private static void addAttachment(MimeMessageHelper helper, MailData.Attachment attachment) {
        helper.addAttachment(attachment.getName(), new Base64DecodedResource(attachment.getContent()));
    }
}
