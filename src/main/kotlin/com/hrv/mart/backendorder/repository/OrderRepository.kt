package com.hrv.mart.backendorder.repository

import com.hrv.mart.orderlibrary.model.Status
import com.hrv.mart.orderlibrary.model.order.Order
import org.springframework.data.domain.PageRequest
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDateTime

@Repository
interface OrderRepository: ReactiveMongoRepository<Order, String> {
    fun findOrderByOrderIdAndUserId(orderId: String, userId: String): Mono<Order>
    fun existsByOrderIdAndUserId(orderId: String, userId: String): Mono<Boolean>
    fun findOrdersByUserId(userId: String): Flux<Order>
    fun countOrdersByUserId(userId: String): Mono<Long>
    fun findOrderByStatusInAndDateTimeOfOrderBetween(
        status: List<Status>,
        start: LocalDateTime,
        end: LocalDateTime,
        pageRequest: PageRequest
    ): Flux<Order>
    fun countOrderByStatusInAndDateTimeOfOrderBetween(
        status: List<Status>,
        start: LocalDateTime,
        end: LocalDateTime,
    ): Mono<Long>
}
