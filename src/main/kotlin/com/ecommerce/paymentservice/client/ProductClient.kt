package com.ecommerce.paymentservice.client

import com.ecommerce.paymentservice.model.Product
import org.bson.types.ObjectId
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import java.net.URI

@FeignClient(url = "http://inventory-service.eba-2kbtwhiu.us-east-1.elasticbeanstalk.com/",name="ProductClient")
interface ProductClient {

    @GetMapping("product/getProduct/{productId}")
    fun getProductDetail(@PathVariable(name = "productId")productId:ObjectId):Product

}