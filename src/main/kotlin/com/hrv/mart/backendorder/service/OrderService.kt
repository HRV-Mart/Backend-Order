package com.hrv.mart.backendorder.service

import com.hrv.mart.backendorder.repository.OrderRepository
import com.hrv.mart.backendorder.repository.ProductOrderedRepository
import com.hrv.mart.orderlibrary.model.OrderRequest
import com.hrv.mart.orderlibrary.model.OrderResponse
import com.hrv.mart.orderlibrary.model.Status
import com.hrv.mart.orderlibrary.model.order.Order
import com.hrv.mart.orderlibrary.model.order.ProductOrdered
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@Service
class OrderService(
    @Autowired
    private val orderRepository: OrderRepository,
    @Autowired
    private val productOrderedRepository: ProductOrderedRepository
)
{
    fun getUserOrders(
        userId: String,
        pageRequest: PageRequest
    ) =
        orderRepository
            .findOrdersByUserId(
                userId,
            )
            .flatMap { order ->
                productOrderedRepository
                    .findProductOrderedByOrderId(
                        order.orderId,
                        pageRequest.withSort(
                            Sort.by(
                                Sort.Direction.DESC,
                                "dateTimeOfOrder"
                            )
                        )
                    )
                    .collectList()
                    .map {
                        OrderResponse.parseFrom(
                            order = order,
                            productOrdered = it
                        )
                    }
            }
//    fun temp() =
//        orderRepository.findOrderByStatusIn(listOf(Status.PROCESS))
    fun temp() : Flux<Order>{
        val pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd:HH:mm:ss", Locale.ROOT)
        return orderRepository.findOrderByStatusInAndDateTimeOfOrderBetween(
            status = listOf(Status.PROCESS),
            start = LocalDateTime.parse("2023-06-10:12:46:41", pattern),
            end = LocalDateTime.parse("2023-06-11:14:46:41", pattern)
        )
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