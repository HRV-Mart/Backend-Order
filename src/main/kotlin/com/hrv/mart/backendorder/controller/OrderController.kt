package com.hrv.mart.backendorder.controller

import com.hrv.mart.backendorder.service.OrderService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/order")
class OrderController (
    @Autowired
    private val orderService: OrderService
) {
    @GetMapping("/{userID}")
    fun getUserOrders(@PathVariable userID: String) =
        orderService.getUserOrders(userID)

}