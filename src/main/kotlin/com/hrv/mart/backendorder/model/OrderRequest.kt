package com.hrv.mart.backendorder.model

import com.hrv.mart.cartresponse.model.CartResponse

data class OrderRequest (
    val userId: String,
    val products: List<CartResponse>,
) {
    fun getOrderResponse() =
        OrderResponse(
            userId=userId,
            products=products
        )
}