package com.ecommerce.paymentservice.service

import com.ecommerce.paymentservice.model.Mail

interface IMailService {
    fun sendMail(mail:Mail)
}