package com.hrv.mart.backendorder.repository

import com.hrv.mart.orderlibrary.model.order.Order
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
interface OrderRepository: ReactiveMongoRepository<Order, String> {
    fun findOrdersByUserId(userId: String): Flux<Order>
}