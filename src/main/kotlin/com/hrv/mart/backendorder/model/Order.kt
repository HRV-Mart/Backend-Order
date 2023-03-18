package com.hrv.mart.backendorder.model

import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document("Data")
data class Order (
    val userId: String,
    val products: List<String>,
    val status: Status,
    val dateTimeOfOrder: LocalDateTime? = LocalDateTime.now()
)