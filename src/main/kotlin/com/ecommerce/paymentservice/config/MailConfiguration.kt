package com.ecommerce.paymentservice.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import java.util.*


@Configuration
class MailConfiguration {
    @Autowired
    private lateinit var env: Environment

    @Bean
    fun getMailSender(): JavaMailSender? {
        val mailSender = JavaMailSenderImpl()
        mailSender.host = env.getProperty("spring.mail.host")
        mailSender.port = Integer.valueOf(env.getProperty("spring.mail.port"))
        mailSender.username = env.getProperty("spring.mail.username")
        mailSender.password = env.getProperty("spring.mail.password")

        val javaMailProperties = Properties()
        javaMailProperties.put("mail.smtp.starttls.enable", "true")
        javaMailProperties.put("mail.smtp.auth", "true")
        javaMailProperties.put("mail.transport.protocol", "smtp")
        javaMailProperties.put("mail.debug", "true")
        mailSender.javaMailProperties = javaMailProperties
        return mailSender
    }
}