package com.hrv.mart.backendorder.model

import com.hrv.mart.cartresponse.model.CartResponse

data class OrderRequest (
    val userId: String,
    val products: List<CartResponse>,
    val status: Status
) {
    fun getOrderResponse() =
        OrderResponse(
            userId=userId,
            products=products,
            status=status
        )
}