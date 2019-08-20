# Silver Bars Marketplace - Live Order Board

an implementation of a Live Order Board for Silver Bars Marketplace has the following functionality:

* register an order. 
    * `register(order: Order) : userId`
* cancel an order. 
    * `cancel(id: String)`
* get summary information of live orders.
    * `summary : List<Order>`
    * `summaryBuy : List<Order>`
    * `summarySell : List<Order>`
## Tech Stack
* [Kotlin (1.3.41)](https://kotlinlang.org/)
* [SL4J](https://www.slf4j.org/) 
* [JUnit 4](https://junit.org/junit4/)

## Technical decisions

* both price and quantity in kg is modeled as a double and no units/measurement system
* different order summary information for BUYs and SELLs and ALL as aggregated
* TDD Applied
* gradle has been used for assembly
* implementation is not synchronized
* orders modeled as final classes
* YAGNI applied

## Prerequisites
- [Gradle](https://gradle.org/)
- [JDK8+](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html/)

## Download the app
* `git clone git@github.com:eevirgen/sbmp-live-order-board.git`

## Build the app
* gradle clean build (runs the **Unit Tests** and and creates the distribution)
    * `build/libs/sbmp-live-order-board.jar`

## Run the app
* `gradle run`
* or can be done in an IDE 
* or can be run by using jar which has been created by build : 
    * `java -jar build/libs/sbmp-live-order-board.jar`

## Run the tests only
* to run **with** the console outputs
    * `gradle test`
* to run **without** the console outputs 
    * `gradle cleanTest`    
    


