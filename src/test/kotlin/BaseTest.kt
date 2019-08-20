package test

import com.silverbarsmp.liveorders.service.LiveOrderBoard
import silverbarsmp.liveorderboard.util.initLog
import org.junit.After
import org.junit.Before

abstract class BaseTest() {

    companion object {
        init {
            initLog()
        }
    }

    @Before
    open fun before() {
        init()
    }

    abstract fun reset()

    abstract fun init()

    open fun finish() {}

    @After
    fun after() {
        finish()
    }


}