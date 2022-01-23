package com.ecommerce.paymentservice.service

import com.ecommerce.paymentservice.model.Mail
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import java.io.UnsupportedEncodingException
import javax.mail.MessagingException
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

@Service
class MailServiceImpl:IMailService {

    @Autowired
    lateinit var mailSender: JavaMailSender

    override fun sendMail(mail: Mail) {
       var mimeMessage:MimeMessage  = mailSender.createMimeMessage();

        try {

            var mimeMessageHelper:MimeMessageHelper  = MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.setSubject(mail.mailSubject);
            mimeMessageHelper.setFrom(InternetAddress(mail.mailFrom, "ecommerce.com"));
            mimeMessageHelper.setTo(mail.mailTo);
            mimeMessageHelper.setText(mail.mailContent);

            mailSender.send(mimeMessageHelper.mimeMessage);

        } catch (e:MessagingException) {
            e.printStackTrace();
        } catch (e:UnsupportedEncodingException) {
            e.printStackTrace();
        }
    }
}