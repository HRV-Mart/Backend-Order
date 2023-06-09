package com.hrv.mart.backendorder.controller

import com.hrv.mart.backendorder.model.OrderQuery
import com.hrv.mart.backendorder.service.OrderService
import com.hrv.mart.custompageable.CustomPageRequest
import com.hrv.mart.orderlibrary.model.OrderRequest
import com.hrv.mart.orderlibrary.model.OrderTopics
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import java.util.*

@RestController
@RequestMapping("/order")
class OrderController (
    @Autowired
    private val orderService: OrderService
) {
    @GetMapping("/{userID}")
    fun getUserOrders(
        @PathVariable userID: String,
        @RequestParam size: Optional<Int>,
        @RequestParam page: Optional<Int>
    ) =
        orderService
            .getUserOrders(
                userID,
                CustomPageRequest
                    .getPageRequest(
                        optionalPage = page,
                        optionalSize = size
                    )
            )
    @GetMapping("/{userId}/{orderId}")
    fun getOrderByUserIdAndOrderId(
        @PathVariable userId: String,
        @PathVariable orderId: String,
        response: ServerHttpResponse
    ) =
        orderService
            .getOrderByOrderIdAndUserId(
                orderId = orderId,
                userId = userId,
                response = response
            )
    @PostMapping
    fun applyFilteringAndSorting(
        @RequestBody orderQuery: OrderQuery,
        @RequestParam size: Optional<Int>,
        @RequestParam page: Optional<Int>
    ) =
        orderService.applyFilterOnOrder(
            orderQuery,
            CustomPageRequest
                .getPageRequest(
                    optionalSize = size,
                    optionalPage = page
                )
        )
    @KafkaListener(
        topics = [OrderTopics.createOrderTopic],
        groupId = "\${spring.kafka.consumer.group-id}"
    )
    fun addCartToOrder(orderRequest: OrderRequest) =
        orderService
            .addOrder(orderRequest)
            .then(Mono.empty<Void>())
}