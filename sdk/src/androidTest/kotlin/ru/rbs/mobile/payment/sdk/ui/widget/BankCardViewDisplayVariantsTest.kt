package ru.rbs.mobile.payment.sdk.ui.widget

import android.content.Context
import org.junit.Test
import ru.rbs.mobile.payment.sdk.test.SleepEmulator.sleep
import ru.rbs.mobile.payment.sdk.test.core.CoreUIViewTest

class BankCardViewDisplayVariantsTest : CoreUIViewTest<BankCardView>() {

    override fun prepareView(context: Context): BankCardView {
        return BankCardView(context)
    }

    @Suppress("LongMethod", "ComplexMethod")
    @Test
    fun shouldCardDisplayVariants() {
        takeScreen("Empty")

        activityTestRule.runOnUiThread {
            testedView.apply {
                setBankLogoUrl("https://mrbin.io/bins/logo/invert/811f1cbe-3f2e-465f-bbe0-6554db83396f/1.svg")
                setBackground("#ff0000", "#ba0e0a")
                setHolderName("Mabel Fergie")
                setTextColor("#fff")
                setExpiry("12/25")
                setNumber("404269")
                setPaymentSystem("amex", true)
            }
        }
        sleep()
        takeScreen("MTS Bank")

        activityTestRule.runOnUiThread {
            testedView.apply {
                setBankLogoUrl("https://mrbin.io/bins/logo/invert/508c9017-1fd8-4d48-9e9b-0922f818bd59/1.svg")
                setBackground("#1a9f29", "#0d7518")
                setHolderName("MARK WATNEY")
                setTextColor("#ffffff")
                setExpiry("12/25")
                setNumber("417398")
                setPaymentSystem("jcb", true)
            }
        }
        sleep()
        takeScreen("Sberbank")

        activityTestRule.runOnUiThread {
            testedView.apply {
                setBankLogoUrl("https://mrbin.io/bins/logo/invert/a559252b-3772-4b7e-817d-27b16db17580/1.svg")
                setBackground("#00bcf2", "#004e90")
                setHolderName("RICHARD HENDRICKS")
                setTextColor("#ffffff")
                setExpiry("12/25")
                setNumber("419349")
                setPaymentSystem("maestro", true)
            }
        }
        sleep()
        takeScreen("Citibank")

        activityTestRule.runOnUiThread {
            testedView.apply {
                setBankLogoUrl("https://mrbin.io/bins/logo/main/364b8b2f-64f1-4268-b1df-9b19575c68e1/1.svg")
                setBackground("#eeeeee", "#efe6a2")
                setHolderName("JOHN DOE")
                setTextColor("#000000")
                setExpiry("12/25")
                setNumber("447624")
                setPaymentSystem("mastercard")
            }
        }
        sleep()
        takeScreen("Raiffeisenbank")

        activityTestRule.runOnUiThread {
            testedView.apply {
                setBankLogoUrl("https://mrbin.io/bins/logo/invert/b2f14c8d-f01a-43eb-bf08-0b737fb4f364/1.svg")
                setBackground("#444444", "#222222")
                setHolderName("MARTIN VAIL")
                setTextColor("#ffffff")
                setExpiry("12/25")
                setNumber("437772")
                setPaymentSystem("mir", true)
            }
        }
        sleep()
        takeScreen("Tinkoff Bank")

        activityTestRule.runOnUiThread {
            testedView.apply {
                setBankLogoUrl("https://mrbin.io/bins/logo/main/51a2f9f4-3936-4099-8ee4-aec7bb349f0d/1.svg")
                setBackground("#e0eaf7", "#f7dfdf")
                setHolderName("MARIA SALOMEA")
                setTextColor("#1c297b")
                setExpiry("12/25")
                setNumber("411648")
                setPaymentSystem("visa")
            }
        }
        sleep()
        takeScreen("Credit Europe Bank (Russia) Ltd.")

        activityTestRule.runOnUiThread {
            testedView.apply {
                setBankLogoUrl("https://mrbin.io/bins/logo/invert/bc567911-f89c-420f-87fc-8ce80472634e/1.svg")
                setBackground("#3fc2ce", "#008c99")
                setHolderName("Diana Anika")
                setTextColor("#ffffff")
                setExpiry("12/25")
                setNumber("414658")
                setPaymentSystem("cup")
            }
        }
        sleep()
        takeScreen("Zenit")

        activityTestRule.runOnUiThread {
            testedView.apply {
                setBankLogoUrl("https://mrbin.io/bins/logo/invert/acdaad5e-5919-4ee2-9139-3b598fd6c5b6/1.svg")
                setBackground("#9fe5ff", "#5ea6d6")
                setHolderName("Reshmi Kuro")
                setTextColor("#005288")
                setExpiry("12/25")
                setNumber("425695")
                setPaymentSystem("maestro", true)
            }
        }
        sleep()
        takeScreen("SMP Bank")

        activityTestRule.runOnUiThread {
            testedView.apply {
                setBankLogoUrl("https://mrbin.io/bins/logo/main/6f4e7579-cda7-4f35-b26e-a08aa2412f0f/1.svg")
                setBackground("#eeeeee", "#98c2dd")
                setHolderName("Jógvan Haraldr")
                setTextColor("#07476e")
                setExpiry("12/25")
                setNumber("426812")
                setPaymentSystem("amex")
            }
        }
        sleep()
        takeScreen("Rossiya")

        activityTestRule.runOnUiThread {
            testedView.apply {
                setBankLogoUrl("https://mrbin.io/bins/logo/main/55ba24e7-76ab-4305-a718-295093dd9865/1.svg")
                setBackground("#ffffff", "#86c5ec")
                setHolderName("Menodora Nina")
                setTextColor("#00446d")
                setExpiry("12/25")
                setNumber("425884")
                setPaymentSystem("jcb")
            }
        }
        sleep()
        takeScreen("Standard Chartered")

        activityTestRule.runOnUiThread {
            testedView.apply {
                setBankLogoUrl("https://mrbin.io/bins/logo/main/9c65cda6-95bb-4a68-ace1-1417f1e3bf3b/1.svg")
                setBackground("#9bdaff", "#ffd2a2")
                setHolderName("Friorika Bruce")
                setTextColor("#072761")
                setExpiry("12/25")
                setNumber("439245")
                setPaymentSystem("cup", false)
            }
        }
        sleep()
        takeScreen("Globexbank")

        activityTestRule.runOnUiThread {
            testedView.apply {
                setBankLogoUrl("https://mrbin.io/bins/logo/main/75fe7383-5361-45da-922e-edb6bc7680e2/1.svg")
                setBackground("#ceecb7", "#8bbb75")
                setHolderName("Gudmundur Halima")
                setTextColor("#167158")
                setExpiry("12/25")
                setNumber("532326")
                setPaymentSystem("belcard")
            }
        }
        sleep()
        takeScreen("Cetelem Bank")

        activityTestRule.runOnUiThread {
            testedView.apply {
                setBankLogoUrl("https://mrbin.io/bins/logo/invert/d84c4add-3f28-4250-aa07-2d87bb055f38/1.svg")
                setBackground("#31a899", "#006b5a")
                setHolderName("Yannig Erastus")
                setTextColor("#ffffff")
                setExpiry("12/25")
                setNumber("410696")
                setPaymentSystem("mir", true)
            }
        }
        sleep()
        takeScreen("SKB-Bank")

        activityTestRule.runOnUiThread {
            testedView.apply {
                setBankLogoUrl("https://mrbin.io/bins/logo/invert/4cb1063d-bc5b-46d4-952f-99d53d1a1399/1.svg")
                setBackground("#29c9f3", "#00b3e1")
                setHolderName("Taffy Johnna")
                setTextColor("#ffffff")
                setExpiry("12/25")
                setNumber("429039")
                setPaymentSystem("dinersclub", true)
            }
        }
        sleep()
        takeScreen("Otkritie FC")

        activityTestRule.runOnUiThread {
            testedView.apply {
                setBankLogoUrl("https://mrbin.io/bins/logo/invert/958c913e-ce40-4658-9a7a-62cb09a16b5a/1.svg")
                setBackground("#ef3124", "#d6180b")
                setHolderName("Radoslav Miroslava")
                setTextColor("#fff")
                setExpiry("12/25")
                setNumber("415482")
                setPaymentSystem("mir", true)
            }
        }
        sleep()
        takeScreen("Alfa-Bank")

        activityTestRule.runOnUiThread {
            testedView.apply {
                setBankLogoUrl("https://mrbin.io/bins/logo/invert/2650be84-bbcc-40f2-bb90-cce3ebb7bc1f/1.svg")
                setBackground("#264489", "#1d2d70")
                setHolderName("Dzintars Marlena")
                setTextColor("#fff")
                setExpiry("12/25")
                setNumber("418262")
                setPaymentSystem("jcb", true)
            }
        }
        sleep()
        takeScreen("VTB Bank")

        activityTestRule.runOnUiThread {
            testedView.apply {
                setBankLogoUrl("https://mrbin.io/bins/logo/main/58ab1767-1b42-45c7-aaa2-1daf0aba597d/1.svg")
                setBackground("#d2e0ec", "#caecd8")
                setHolderName("Margit Sibusiso")
                setTextColor("#165a9a")
                setExpiry("12/25")
                setNumber("439055")
                setPaymentSystem("dinersclub")
            }
        }
        sleep()
        takeScreen("Sviaz-Bank")

        activityTestRule.runOnUiThread {
            testedView.apply {
                setBankLogoUrl("https://mrbin.io/bins/logo/main/f1feda55-299f-47f4-a5c2-545dd4da534c/1.svg")
                setBackground("#cedae6", "#a4abb3")
                setHolderName("Jelka Avtandil")
                setTextColor("#13427b")
                setExpiry("12/25")
                setNumber("410731")
                setPaymentSystem("dinersclub")
            }
        }
        sleep()
        takeScreen("Bank Vozrozhdenie")

        activityTestRule.runOnUiThread {
            testedView.apply {
                setBankLogoUrl("https://mrbin.io/bins/logo/main/e7e4a047-fcd4-47bc-9067-a831e5f7ca26/1.svg")
                setBackground("#efefef", "#dbe1ff")
                setHolderName("Babur Hitomi")
                setTextColor("#001689")
                setExpiry("12/25")
                setNumber("405992")
                setPaymentSystem("mastercard")
            }
        }
        sleep()
        takeScreen("Pochtabank")

        activityTestRule.runOnUiThread {
            testedView.apply {
                setBankLogoUrl("https://mrbin.io/bins/logo/invert/49bfde01-f805-4b52-a7e0-d975dc61e2b6/1.svg")
                setBackground("#6a656f", "#414042")
                setHolderName("Triin Aditi")
                setTextColor("#ffffff")
                setExpiry("12/25")
                setNumber("411790")
                setPaymentSystem("mastercard", true)
            }
        }
        sleep()
        takeScreen("Russian Standard Bank")

        activityTestRule.runOnUiThread {
            testedView.apply {
                setBankLogoUrl("https://mrbin.io/bins/logo/invert/9d489d47-113c-4f32-b6f0-2e5c01ff6911/1.svg")
                setBackground("#00529b", "#0a4477")
                setHolderName("Hadriana Ejder")
                setTextColor("#ffffff")
                setExpiry("12/25")
                setNumber("402911")
                setPaymentSystem("mir", true)
            }
        }
        sleep()
        takeScreen("Novikombank")

        activityTestRule.runOnUiThread {
            testedView.apply {
                setBankLogoUrl("https://mrbin.io/bins/logo/invert/7ed09aa5-e0d5-4af8-805b-89a6bd189b35/1.svg")
                setBackground("#007f2b", "#005026")
                setHolderName("Magomet Thorstein")
                setTextColor("#ffcd00")
                setExpiry("12/25")
                setNumber("418388")
                setPaymentSystem("visa", true)
            }
        }
        sleep()
        takeScreen("Rosselkhozbank")

        activityTestRule.runOnUiThread {
            testedView.apply {
                setBankLogoUrl("https://mrbin.io/bins/logo/invert/86de642a-2943-4d76-9cc7-8032e1d0667c/1.svg")
                setBackground("#8b2d8e", "#4b1650")
                setHolderName("Ujarak Bahman")
                setTextColor("#ffffff")
                setExpiry("12/25")
                setNumber("418909")
                setPaymentSystem("visa", true)
            }
        }
        sleep()
        takeScreen("Rosevrobank")
    }
}
