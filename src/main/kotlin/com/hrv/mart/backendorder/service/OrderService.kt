package com.hrv.mart.backendorder.service

import com.hrv.mart.backendorder.repository.OrderRepository
import com.hrv.mart.orderlibrary.model.OrderRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class OrderService(
    @Autowired
    private val orderRepository: OrderRepository
)
{
    fun getUserOrders(userId: String) =
        orderRepository.findOrdersByUserId(userId)
    fun addOrder(order: OrderRequest) =
        orderRepository.insert(order.getOrderResponse())
}