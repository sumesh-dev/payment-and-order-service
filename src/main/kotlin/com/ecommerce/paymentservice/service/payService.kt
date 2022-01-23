package com.ecommerce.paymentservice.service

import com.ecommerce.paymentservice.client.ProductClient
import com.ecommerce.paymentservice.client.UserClient
import com.ecommerce.paymentservice.dao.ISellerRepository
import com.ecommerce.paymentservice.dao.OrdersRepository
import com.ecommerce.paymentservice.model.Mail
import com.ecommerce.paymentservice.model.Orders
import com.ecommerce.paymentservice.model.Product
import com.ecommerce.paymentservice.model.UserAmount
import com.razorpay.RazorpayClient
import org.bson.types.ObjectId
import org.json.JSONObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.mongodb.core.mapping.MongoId
import org.springframework.stereotype.Service
import java.net.URI
import java.time.LocalDateTime
import java.util.*

@Service
class payService: IPayService{

    @Autowired
    lateinit var userClient: UserClient

    @Autowired
    lateinit var ordersRepository: OrdersRepository

    @Autowired
    lateinit var productClient: ProductClient

    @Value("\${Feign.Product_url}")
    lateinit var productUrl: URI


    @Value("\${razorPay.key}")
    private val key: String? = null

    @Value("\${razorpay.secret}")
    private val secret: String? = null

    @Value("\${Feign.User_url}")
    lateinit var userUrl: URI

    @Autowired
    lateinit var iMailService: IMailService

    @Autowired
    lateinit var iSellerRepository: ISellerRepository

    @Value("\${jwt.secret}")
    private lateinit var secretCode: String

    override fun createOrder(email: String, userAmount: UserAmount): String {

        val products: MutableList<ObjectId> = userClient.getProductsFromCart(userUrl,secretCode,email)
        if(products.size > 0) {
            val client = RazorpayClient(key, secret)
            val txnid = UUID.randomUUID().toString().replace("-", "")
//            println(txnid)
            val options = JSONObject()
            options.put("amount", userAmount.amount * 100)
            options.put("currency", "INR")
            options.put("receipt", txnid)

            // creating order
            try {
                val order = client.Orders.create(options)
//            println(order.toString())
                // save the order info in database
                ordersRepository.save(
                    Orders(
                        email, userAmount.amount, txnid, LocalDateTime.now(), products,
                        order.get("id") as String, "created", null
                    )
                )
//                userClient.deleteProductsFromCart(userUrl,secretCode,email)
                return order.toString()
            }
            catch(e:Exception){
                println(e)
               return "error occured"
            }
        }
        else{
            return "no Product in cart"
        }
    }

    override fun updateOrder(orderId: String, paymentId: String, status: String,email:String): String {
        println("update order ${orderId}, ${paymentId}, ${status}")
        val order = ordersRepository.findByRazorpayOrderId(orderId)
        order.paymentId = paymentId
        order.paymentStatus = status
        ordersRepository.save(order)
        userClient.deleteProductsFromCart(userUrl,secretCode,email)
        val product:MutableList<Product> = mutableListOf()
//        order.products.forEach { println(productClient.getProductDetail(productUrl,it)) }
        order.products.forEach { product.add(productClient.getProductDetail(productUrl,it)) }
        product.forEach {
            val mail = Mail()
            mail.mailFrom = "sumeshkuchya2@gmail.com"
            mail.mailTo = iSellerRepository.findById(ObjectId(it.addBy)).get().email
            mail.mailSubject = "${it.name} product sold"
            mail.mailContent = "you get an order from ${order.email} for ${it.name}\n\nThanks\nshoppingSite"
            iMailService.sendMail(mail)
        }

        return "updated successfully"
    }

}