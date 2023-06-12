package com.hrv.mart.backendorder.repository

import com.hrv.mart.orderlibrary.model.order.ProductOrdered
import org.springframework.data.domain.PageRequest
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
interface ProductOrderedRepository: ReactiveMongoRepository<ProductOrdered, String> {
    fun findProductOrderedByOrderId(orderId: String, pageRequest: PageRequest): Flux<ProductOrdered>
}
