package com.github.slamdev.micro.playground.services.mailer.business.boundary;

import com.github.slamdev.micro.playground.services.mailer.api.MailDto;
import com.github.slamdev.micro.playground.services.mailer.api.MailerApi;
import com.github.slamdev.micro.playground.services.mailer.business.control.EmailSender;
import com.github.slamdev.micro.playground.services.mailer.business.control.MailRepository;
import com.github.slamdev.micro.playground.services.mailer.business.entity.MailData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.mail.MailSendException;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MailController implements MailerApi {

    private final ModelMapper mapper = new ModelMapper();

    private final EmailSender sender;

    private final MailRepository repository;

    @Override
    public void sendMail(MailDto mailDto) {
        MailData entity = mapper.map(mailDto, MailData.class);
        try {
            sender.sendMail(entity);
        } catch (MailSendException e) {
            entity.setError(e.getMessage());
            log.error("", e);
        }
        repository.save(entity);
    }
}
