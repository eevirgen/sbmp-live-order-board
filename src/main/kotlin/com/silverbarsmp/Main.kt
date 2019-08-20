package com.silverbarsmp

import com.silverbarsmp.liveorders.domain.Order
import com.silverbarsmp.liveorders.domain.OrderType
import com.silverbarsmp.liveorders.service.LiveOrderBoard
import silverbarsmp.liveorderboard.util.*
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    initLog()
    val log = log()
    try {

        log.info("==================== Starting to run samples  ====================")
        val liveOrderBoard = LiveOrderBoard()

        liveOrderBoard.register(Order(quantityInKg = 3.5, pricePerKg =  306.0, orderType = OrderType.SELL))
        liveOrderBoard.register(Order(quantityInKg = 1.2, pricePerKg =  310.0, orderType =  OrderType.SELL))
        liveOrderBoard.register(Order(quantityInKg = 1.5, pricePerKg =  307.0, orderType = OrderType.SELL))
        liveOrderBoard.register(Order(quantityInKg = 2.0, pricePerKg =  306.0, orderType = OrderType.SELL))

        val orderSummaryInformations = liveOrderBoard.summary

        orderSummaryInformations.forEach  { log.info(it.toString()) }

        log.info("==================== Ready to Implement ====================")

    }
    catch (t: Throwable) {
        log.error("Error", t)
        exitProcess(1)
    }
}


