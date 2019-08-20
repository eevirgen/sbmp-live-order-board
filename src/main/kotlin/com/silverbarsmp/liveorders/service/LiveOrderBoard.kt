package com.silverbarsmp.liveorders.service

import com.silverbarsmp.liveorders.domain.Order
import com.silverbarsmp.liveorders.domain.OrderType
import com.silverbarsmp.liveorders.util.OrderNotExisted


class LiveOrderBoard {

    private val orders = mutableListOf<Order>()

    /**
    * Register an order and return with it's id
    *
    * @param order the order to register
    * @return the user id
    */
    fun register(order: Order): String {
        orders.add(order)
        return order.userId
    }

    /**
     * Cancels an order by an order Object
     *
     * @param order the Order object to cancel
     * @throws OrderNotExisted in case there is no item with the given Order object
     */
    fun cancel(order: Order)  {
        try {
            if (!orders.contains(order)) throw OrderNotExisted("Order is not existed")
            orders.remove(order)
        } catch (e:Exception) {
            throw OrderNotExisted("Order is not existed")
        }
    }

    /**
     * Get ALL Orders order summary information
     *
     * @return the order summary as Lisf of Order Object
     */
    val summary: List<Order>
        get() {
            val summary = mutableListOf<Order>()
            summary.addAll(sorted(OrderType.SELL))
            summary.addAll(sorted(OrderType.BUY))
            return summary
        }

    /**
     * Get BUY Orders order summary information
     *
     * @return the order summary as List of Order Object includes BUY's only
     */
    val summaryBuy: List<Order>
        get() {
            return sorted(OrderType.BUY)
        }

    /**
     * Get SELL Orders order summary information
     *
     * @return the order summary as List of Order Object includes SELL's only
     */
    val summarySell: List<Order>
        get() {
            return sorted(OrderType.SELL)
        }


    /**
     * Sorts - Group - Merge by Order Type
     *
     * @param type as Order Type
     *
     * @return the order summary as List of Order Objects by @param
     */
    private fun sorted(type: OrderType): List<Order> = if(type== OrderType.BUY)
        orders.filter{ it.orderType == type }.map { it }.toList().sortedByDescending{ it.pricePerKg }.groupBy { it.pricePerKg }
                .values
                .map {
                    it.reduce{
                        acc, order -> Order(order.userId, acc.quantityInKg + order.quantityInKg, order.pricePerKg, order.orderType)
                    }
                }
    else
        orders.filter{ it.orderType == type }.map { it }.toList().sortedBy{ it.pricePerKg }.groupBy { it.pricePerKg }
                .values
                .map {
                    it.reduce{
                        acc, order -> Order(order.userId, acc.quantityInKg + order.quantityInKg, order.pricePerKg, order.orderType)
                    }
                }

    }