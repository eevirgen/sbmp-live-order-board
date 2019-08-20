package com.silverbarsmp.liveorders.domain

import java.util.*

data class Order (
        val userId : String = UUID.randomUUID().toString(),
        val quantityInKg : Double,
        val pricePerKg : Double,
        val orderType: OrderType
)