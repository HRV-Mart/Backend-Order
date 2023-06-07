package com.hrv.mart.backendorder.service

import com.hrv.mart.backendorder.repository.OrderRepository
import com.hrv.mart.backendorder.repository.ProductOrderedRepository
import com.hrv.mart.orderlibrary.model.OrderRequest
import com.hrv.mart.orderlibrary.model.OrderResponse
import com.hrv.mart.orderlibrary.model.order.Order
import com.hrv.mart.orderlibrary.model.order.ProductOrdered
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class OrderService(
    @Autowired
    private val orderRepository: OrderRepository,
    @Autowired
    private val productOrderedRepository: ProductOrderedRepository
)
{
    fun getUserOrders(userId: String) =
        orderRepository
            .findOrdersByUserId(userId)
            .flatMap { order ->
                productOrderedRepository
                    .findProductOrderedByOrderId(order.orderId)
                    .collectList()
                    .map {
                        OrderResponse.parseFrom(
                            order = order,
                            productOrdered = it
                        )
                    }
            }
    fun addOrder(orderRequest: OrderRequest): Mono<String> {
        val order = Order.parseFrom(orderRequest)
        val productOrdered = ProductOrdered.parseFrom(orderRequest, order)

        return productOrdered
            .map {
                productOrderedRepository.insert(it)
            }
            .reduce { acc, mono ->  acc.flatMap { mono }}
            .flatMap { orderRepository.insert(order) }
            .then (Mono.just("Order places successfully"))

    }
}