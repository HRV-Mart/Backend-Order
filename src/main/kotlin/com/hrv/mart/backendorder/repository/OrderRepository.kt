package com.hrv.mart.backendorder.repository

import com.hrv.mart.orderlibrary.model.OrderResponse
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
interface OrderRepository: ReactiveMongoRepository<OrderResponse, String> {
    fun findOrdersByUserId(userId: String): Flux<OrderResponse>
}