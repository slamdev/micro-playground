package com.github.slamdev.micro.playground.services.mailer.business.control;

import com.github.slamdev.micro.playground.services.mailer.business.entity.MailData;
import org.springframework.data.repository.CrudRepository;

public interface MailRepository extends CrudRepository<MailData, String> {
}
