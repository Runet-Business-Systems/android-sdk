package ru.rbs.mobile.payment.sdk.test

object SleepEmulator{

    fun sleep() { Thread.sleep(SLEEP_TIME) }

    private const val SLEEP_TIME = 2_000L
}
