package liveorders.service

import com.silverbarsmp.liveorders.domain.Order
import com.silverbarsmp.liveorders.domain.OrderType
import com.silverbarsmp.liveorders.service.LiveOrderBoard
import com.silverbarsmp.liveorders.util.OrderNotExisted
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import silverbarsmp.liveorderboard.util.log
import test.BaseTest
import org.junit.rules.ExpectedException



class LiveOrderBoardTest: BaseTest() {
        override fun reset() { }
        override fun before() { super.before() }
        override fun finish() {}
        override fun init() {
    }

    @Test
    fun shouldRegisterOrder() {
        val liveOrderBoard = LiveOrderBoard()
        val order = Order(quantityInKg = 13.0, pricePerKg =  180.0, orderType =  OrderType.BUY)
        val userId = order.userId
        assertEquals(userId, liveOrderBoard.register(order))
    }

    @Test
    fun shouldCancelOrder() {
        val liveOrderBoard = LiveOrderBoard()
        val order1 = Order(quantityInKg = 44.0, pricePerKg =  180.0, orderType =  OrderType.BUY)
        val order2 = Order(quantityInKg = 46.0, pricePerKg =  200.0, orderType =  OrderType.BUY)
        val order3 = Order(quantityInKg = 18.0, pricePerKg =  500.0, orderType =  OrderType.SELL)

        val userId1 = order1.userId
        val userId2 = order2.userId
        val userId3 = order3.userId

        assertEquals(userId1, liveOrderBoard.register(order1))
        assertEquals(userId2, liveOrderBoard.register(order2))
        assertEquals(userId3, liveOrderBoard.register(order3))

        liveOrderBoard.cancel(order2)
        assertEquals(2, liveOrderBoard.summary.size)
    }


    @Test(expected = OrderNotExisted::class)
    fun shouldThrowOrderIsNotExisted() {
        val liveOrderBoard = LiveOrderBoard()
        val order1 = Order(quantityInKg = 44.0, pricePerKg =  180.0, orderType =  OrderType.BUY)
        val order2 = Order(quantityInKg = 45.0, pricePerKg =  180.0, orderType =  OrderType.BUY)

        val userId1 = order1.userId
        assertEquals(userId1, liveOrderBoard.register(order1))
        liveOrderBoard.cancel(order2)
    }

    @Test
    fun shouldSortBuyOrders() {
        val liveOrderBoard = LiveOrderBoard()
        val orderType = OrderType.BUY
        val order1 = Order(quantityInKg = 13.0, pricePerKg =  400.0, orderType = orderType)
        val order2 = Order(quantityInKg = 11.0, pricePerKg =  200.0, orderType =  orderType)
        val order3 = Order(quantityInKg = 9.0, pricePerKg =  300.0, orderType = orderType)

        liveOrderBoard.register(order1)
        liveOrderBoard.register(order2)
        liveOrderBoard.register(order3)

        val orderSummaryInformations = liveOrderBoard.summary

        assertEquals(400.0,orderSummaryInformations[0].pricePerKg,  0.001)
        assertEquals(300.0,orderSummaryInformations[1].pricePerKg, 0.001)
        assertEquals(200.0,orderSummaryInformations[2].pricePerKg,  0.001)
    }

    @Test
    fun shouldSortSellOrders() {
        val liveOrderBoard = LiveOrderBoard()
        val orderType = OrderType.SELL
        val order1 = Order(quantityInKg = 13.0, pricePerKg =  400.0, orderType = orderType)
        val order2 = Order(quantityInKg = 11.0, pricePerKg =  200.0, orderType =  orderType)
        val order3 = Order(quantityInKg = 9.0, pricePerKg =  300.0, orderType = orderType)

        liveOrderBoard.register(order1)
        liveOrderBoard.register(order2)
        liveOrderBoard.register(order3)

        val orderSummaryInformations = liveOrderBoard.summary

        assertEquals(200.0,orderSummaryInformations[0].pricePerKg,  0.001)
        assertEquals(300.0,orderSummaryInformations[1].pricePerKg, 0.001)
        assertEquals(400.0,orderSummaryInformations[2].pricePerKg,  0.001)
    }

    @Test
    fun shouldMergeOrders() {

        val liveOrderBoard = LiveOrderBoard()
        val orderType = OrderType.SELL
        val order1 = Order(quantityInKg = 13.0, pricePerKg =  400.0, orderType = orderType)
        val order2 = Order(quantityInKg = 11.0, pricePerKg =  400.0, orderType =  orderType)
        val order3 = Order(quantityInKg = 9.0, pricePerKg =  300.0, orderType = orderType)

        liveOrderBoard.register(order1)
        liveOrderBoard.register(order2)
        liveOrderBoard.register(order3)

        val orderSummaryInformations = liveOrderBoard.summary

        assertEquals(9.0, orderSummaryInformations[0].quantityInKg,  0.001)
        assertEquals(24.0, orderSummaryInformations[1].quantityInKg, 0.001)
    }

    @Test
    fun shouldMergeOrdersForBuyAndSellAndSellFirstBuyLastAndOrderedGroupByOrderType() {

        val liveOrderBoard = LiveOrderBoard()
        val order1 = Order(quantityInKg = 13.0, pricePerKg =  400.0, orderType = OrderType.SELL)
        val order2 = Order(quantityInKg = 11.0, pricePerKg =  400.0, orderType =  OrderType.SELL)
        val order3 = Order(quantityInKg = 9.0, pricePerKg =  300.0, orderType = OrderType.SELL)

        liveOrderBoard.register(order1)
        liveOrderBoard.register(order2)
        liveOrderBoard.register(order3)

        val order4 = Order(quantityInKg = 13.0, pricePerKg =  400.0, orderType = OrderType.BUY)
        val order5 = Order(quantityInKg = 11.0, pricePerKg =  400.0, orderType =  OrderType.BUY)
        val order6 = Order(quantityInKg = 9.0, pricePerKg =  300.0, orderType = OrderType.BUY)

        liveOrderBoard.register(order4)
        liveOrderBoard.register(order5)
        liveOrderBoard.register(order6)

        val orderSummaryInformations = liveOrderBoard.summary

        assertEquals(9.0, orderSummaryInformations[0].quantityInKg,  0.001)
        assertEquals(24.0, orderSummaryInformations[1].quantityInKg, 0.001)
        assertEquals(OrderType.SELL, orderSummaryInformations[0].orderType)
        assertEquals(OrderType.SELL, orderSummaryInformations[1].orderType)

        assertEquals(24.0, orderSummaryInformations[2].quantityInKg,  0.001)
        assertEquals(9.0, orderSummaryInformations[3].quantityInKg, 0.001)
        assertEquals(OrderType.BUY, orderSummaryInformations[2].orderType)
        assertEquals(OrderType.BUY, orderSummaryInformations[3].orderType)
    }

    @Test
    fun shouldNotMergeSamePriceOfDifferentTypesInSummary() {
        val liveOrderBoard = LiveOrderBoard()
        val order1 = Order(quantityInKg = 17.0, pricePerKg =  400.0, orderType = OrderType.SELL)
        val order2 = Order(quantityInKg = 9.0, pricePerKg =  400.0, orderType =  OrderType.BUY)

        liveOrderBoard.register(order1)
        liveOrderBoard.register(order2)

        val orderSummaryInformations = liveOrderBoard.summary

        assertEquals(OrderType.SELL, orderSummaryInformations[0].orderType)
        assertEquals(OrderType.BUY, orderSummaryInformations[1].orderType)
    }

    @Test
    fun shouldNotMergeSamePriceOfDifferentTypesInSummaryAndFirstBuySecondSells() {
        val liveOrderBoard = LiveOrderBoard()
        val order1 = Order(quantityInKg = 17.0, pricePerKg =  400.0, orderType = OrderType.BUY)
        val order2 = Order(quantityInKg = 9.0, pricePerKg =  400.0, orderType =  OrderType.SELL)

        liveOrderBoard.register(order1)
        liveOrderBoard.register(order2)

        val orderSummaryInformations = liveOrderBoard.summary
        assertEquals(OrderType.SELL, orderSummaryInformations[0].orderType)
        assertEquals(OrderType.BUY, orderSummaryInformations[1].orderType)
    }

    @Test
    fun shouldMergedAndOrderedOnlyForBuy() {
        val liveOrderBoard = LiveOrderBoard()
        val order1 = Order(quantityInKg = 13.0, pricePerKg =  400.0, orderType = OrderType.BUY)
        val order2 = Order(quantityInKg = 11.0, pricePerKg =  400.0, orderType =  OrderType.BUY)
        val order3 = Order(quantityInKg = 9.0, pricePerKg =  300.0, orderType = OrderType.BUY)

        liveOrderBoard.register(order1)
        liveOrderBoard.register(order2)
        liveOrderBoard.register(order3)

        val orderSummaryInformations = liveOrderBoard.summaryBuy

        assertEquals(24.0, orderSummaryInformations[0].quantityInKg,  0.001)
        assertEquals(9.0, orderSummaryInformations[1].quantityInKg, 0.001)
        assertEquals(OrderType.BUY, orderSummaryInformations[0].orderType)
        assertEquals(OrderType.BUY, orderSummaryInformations[1].orderType)
        assertEquals(2, orderSummaryInformations.size)
    }

    @Test
    fun shouldMergedAndOrderedOnlyForSell() {
        val liveOrderBoard = LiveOrderBoard()
        val order1 = Order(quantityInKg = 13.0, pricePerKg =  400.0, orderType = OrderType.SELL)
        val order2 = Order(quantityInKg = 11.0, pricePerKg =  400.0, orderType =  OrderType.SELL)
        val order3 = Order(quantityInKg = 9.0, pricePerKg =  300.0, orderType = OrderType.SELL)
        liveOrderBoard.register(order1)
        liveOrderBoard.register(order2)
        liveOrderBoard.register(order3)

        val orderSummaryInformations = liveOrderBoard.summarySell

        assertEquals(9.0, orderSummaryInformations[0].quantityInKg,  0.001)
        assertEquals(24.0, orderSummaryInformations[1].quantityInKg, 0.001)
        assertEquals(OrderType.SELL, orderSummaryInformations[0].orderType)
        assertEquals(OrderType.SELL, orderSummaryInformations[1].orderType)
        assertEquals(2, orderSummaryInformations.size)
    }

}