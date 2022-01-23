package com.ecommerce.paymentservice.service

import com.ecommerce.paymentservice.client.ProductClient
import com.ecommerce.paymentservice.dao.IUserRepository
import com.ecommerce.paymentservice.dao.OrdersRepository
import com.ecommerce.paymentservice.model.OrderResponse
import com.ecommerce.paymentservice.model.Orders
import com.ecommerce.paymentservice.model.Product
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.net.URI

@Service
class OrderServiceImp:IOrderService {

    @Autowired
    lateinit var ordersRepository: OrdersRepository

    @Autowired
    lateinit var iUserRepository: IUserRepository


    override fun getOrder(id: ObjectId,email:String): Any {
      val order :Orders= ordersRepository.findById(id).get()
        if(order.email==email || iUserRepository.findByEmail(email)?.role=="admin") {
            return order
        }
        return "You are not authorize to see the order"
    }

    override fun showAllOrders(): Any {
        var orders:MutableList<Orders> = ordersRepository.findAll()
        return if(orders.size<1){
             "No Orders Exist"
        }
        else
            orders
    }

    override fun showMyOrders(email: String): Any {
       var orders:MutableList<Orders> = ordersRepository.findAllOrderByEmail(email)
        return if(orders.size<1){
             "No Orders Exist"
        }
        else {
            val orderResponse:MutableList<OrderResponse> = mutableListOf<OrderResponse>()
//            orders.forEach { i ->
//                i.products.forEach{t
//                    productDetails.add(it.toString())
//                }
//            }
                orders.forEach{ it ->
                    val productDetails:MutableList<String> = mutableListOf<String>()
                    it.products.forEach{
                    productDetails.add(it.toString())
                }
                    orderResponse.add(OrderResponse(it.orderId.toString(),it.email,it.amount,it.txnId,
                        it.orderedDate,productDetails,it.razorpayOrderId,it.paymentStatus,it.paymentId))
//                    productDetails.clear()
                }
            return orderResponse
//            return orders;
        }
    }
}