package com.hrv.mart.backendorder.fixture

import com.hrv.mart.cartresponse.model.CartResponse
import com.hrv.mart.orderlibrary.model.OrderResponse
import com.hrv.mart.orderlibrary.model.order.Order
import com.hrv.mart.orderlibrary.model.order.ProductOrdered
import java.util.Random
import kotlin.math.absoluteValue

object TestFixture {
    val users = listOf(
        "userId1",
        "userId2"
    )
    private val product = listOf(
        "productId1",
        "productId2"
    )
    val orderResponses = users
        .map {user ->
            OrderResponse(
                orderId = getRandomLong().toString(),
                userId = user,
                products = product.map {
                    CartResponse(
                        it,
                        getRandomLong()
                    )
                },
                price  = Random().nextLong(),

            )
        }
    val orders = orderResponses
        .map {
            Order(
                orderId = it.orderId,
                userId = it.userId,
                price = it.price,
                status = it.status,
                dateTimeOfOrder = it.dateTimeOfOrder
            )
        }
    val productOrders = orderResponses
        .map {orderResponse ->
            orderResponse
                .products
                .map {
                    ProductOrdered(
                        userId = orderResponse.userId,
                        orderId = orderResponse.orderId,
                        productId = it.productId,
                        quantity = it.quantity
                    )
                }
        }
        .flatten()

    private fun getRandomLong() =
        Random().nextLong().absoluteValue
}