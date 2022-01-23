package com.ecommerce.paymentservice.model

class Mail {
     lateinit var mailFrom: String

     lateinit var mailTo: String

     lateinit var mailCc: String

     lateinit var mailBcc: String

     lateinit var mailSubject: String

     lateinit var mailContent: String

      var contentType: String = "text/plain"

     var attachments: List<Any?>? = null


    constructor(
        mailFrom: String,
        mailTo: String,
        mailCc: String,
        mailBcc: String,
        mailSubject: String,
        mailContent: String,
        contentType: String,
        attachments: List<Any?>?
    ) {
        this.mailFrom = mailFrom
        this.mailTo = mailTo
        this.mailCc = mailCc
        this.mailBcc = mailBcc
        this.mailSubject = mailSubject
        this.mailContent = mailContent
        this.contentType = contentType
        this.attachments = attachments
    }

    constructor()
}