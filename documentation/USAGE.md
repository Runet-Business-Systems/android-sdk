# RBS Payment SDK

## Подключение к Gradle проекту, добавлением файла .aar библиотеки

Необходимо добавить в папку `libs` файл библиотеки `sdk-release.aar`, затем указать зависимость от
добавленной библиотеки.

### build.gradle.kts

```kotlin
    allprojects {
        repositories {
            // ...
            flatDir {
                dirs("libs")
            }
        }
    }
    
    dependencies {
        implementation(group = "", name = "sdk-release", ext = "aar")
        
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.7")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.7")
        implementation("com.caverock:androidsvg-aar:1.4")
        implementation("com.google.android.material:material:1.2.0-beta01")
        implementation("io.card:android-sdk:5.5.1")
    }
```

### build.gradle

```groovy
    allprojects {
       repositories {
          // ...
          flatDir {
            dirs 'libs'
          }
       }
    }

    dependencies {
        implementation(group:'', name:'sdk-release', ext:'aar')

        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.7")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.7")
        implementation("com.caverock:androidsvg-aar:1.4")
        implementation("com.google.android.material:material:1.2.0-beta01")
        implementation("io.card:android-sdk:5.5.1")
    }
```

## Пример формирования криптограммы

### Пример Kotlin

```kotlin

import ru.rbs.mobile.payment.sdk.SDKPayment

class MarketApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        SDKPayment.init(this)
    }
}

import ru.rbs.mobile.payment.sdk.PaymentConfigBuilder
import ru.rbs.mobile.payment.sdk.ResultCallback
import ru.rbs.mobile.payment.sdk.SDKPayment
import ru.rbs.mobile.payment.sdk.model.CameraScannerOptions
import ru.rbs.mobile.payment.sdk.model.Card
import ru.rbs.mobile.payment.sdk.model.CardSaveOptions
import ru.rbs.mobile.payment.sdk.model.HolderInputOptions
import ru.rbs.mobile.payment.sdk.model.PaymentData
import ru.rbs.mobile.payment.sdk.ui.helper.Locales.english

class MainActivity : AppCompatActivity() {

    private fun executeCheckout() {
        // Список связанных карт.
        val cards = setOf(
            Card("492980xxxxxx7724", "aa199a55-cf16-41b2-ac9e-cddc731edd19", ExpiryDate(2025, 12)),
            Card("558620xxxxxx6614", "6617c0b1-9976-45d9-b659-364ecac099e2", ExpiryDate(2024, 6)),
            Card("415482xxxxxx0000", "3d2d320f-ca9a-4713-977c-c852accf8a7b", ExpiryDate(2019, 1)),
            Card("411790xxxxxx123456", "ceae68c1-cb02-4804-9526-6d6b2f1f2793")
        )

        // Идентификатор заказа обязателен.
        val order = "00210bac-0ed1-474b-8ec2-5648cdfc4212"
        val paymentConfig = PaymentConfigBuilder(order)
            // Опционально, по умолчанию локализованный перевод "Оплатить".
            .buttonText("Оплатить 200 Ꝑ")
            // Опционально, по умолчанию HIDE.
            .cardSaveOptions(CardSaveOptions.YES_BY_DEFAULT)
            // Опционально, по умолчанию HIDE.
            .holderInputOptions(HolderInputOptions.VISIBLE)
            // Опционально, по умолчанию true.
            .bindingCVCRequired(false)
            // Опционально, по умолчанию ENABLED.
            .cameraScannerOptions(CameraScannerOptions.ENABLED)
            // Опционально, локаль формы полаты, определяется автоматически.
            .locale(english())
            // Опционально, по умолчанию пустой список.
            .cards(cards)
            // Опционально, уникальный идентификатор платежа, генерируется автоматически.
            .uuid("27fb1ebf-895e-4b15-bfeb-6ecae378fe8e")
            // Опционально, время формирования платежа, устанавливается автоматически.
            .timestamp(System.currentTimeMillis())
            .build()

        // Вызов экрана оплаты.
        SDKPayment.cryptogram(this, paymentConfig)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Обработка результата.
        SDKPayment.handleResult(requestCode, data, object : ResultCallback<PaymentData> {

            override fun onSuccess(result: PaymentData) {
                // Результат формирования криптограммы.
                when {
                    result.status.isSucceeded() -> {
                        val info = result.info
                        if (info is PaymentInfoNewCard) {
                            log("New card ${info.holder} ${info.saveCard}")
                        } else if (info is PaymentInfoBindCard) {
                            log("Saved card ${info.bindingId}")
                        }
                        log("$result")
                    }
                    result.status.isCanceled() -> {
                        log("canceled")
                    }
                }
            }

            override fun onFail(e: Exception) {
                // Возникла ошибка.
                log(e.toString())
            }
        })
    }
}
```

### Пример Java

```java

import ru.rbs.mobile.payment.sdk.SDKPayment;

public class MarketApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SDKPayment.INSTANCE.init(this);
    }
}

import ru.rbs.mobile.payment.sdk.PaymentConfigBuilder;
import ru.rbs.mobile.payment.sdk.SDKPayment;
import ru.rbs.mobile.payment.sdk.model.Card;
import ru.rbs.mobile.payment.sdk.model.CardSaveOptions;
import ru.rbs.mobile.payment.sdk.model.PaymentConfig;

public class MainActivity extends AppCompatActivity {

    private void executeCheckout() {
        // Список связанных карт.
        Set<Card> cards = new HashSet();
        cards.add(new Card("492980xxxxxx7724", "ee199a55-cf16-41b2-ac9e-cc1c731edd19"));

        // Идентификатор заказа обязателен.
        String order = "00210bac-0ed1-474b-8ec2-5648cdfc4212";
        PaymentConfig paymentConfig = new PaymentConfigBuilder(order)
                // Опционально, по умолчанию локализованный перевод "Оплатить".
                .buttonText("Оплатить 200 Ꝑ")
                // Опционально, по умолчанию HIDE.
                .cardSaveOptions(CardSaveOptions.YES_BY_DEFAULT)
                // Опционально, по умолчанию пустой список.
                .cards(cards)
                // Опционально, уникальный идентификатор платежа, генерируется автоматически.
                .uuid("27fb1ebf-895e-4b15-bfeb-6ecae378fe8e")
                // Опционально, время формирования платежа, устанавливается автоматически.
                .timestamp(System.currentTimeMillis())
                .build();

        // Вызов экрана оплаты.
        SDKPayment.INSTANCE.cryptogram(MainActivity.this, paymentConfig);
    }

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Обработка результата.
        SDKPayment.INSTANCE.handleResult(requestCode, data, new ResultCallback<PaymentData>() {
            @Override
            public void onSuccess(PaymentData result) {
                // Результат формирования криптограммы.
                if (result.getStatus().isSucceeded()) {
                    PaymentInfo info = result.getInfo();
                    if (info instanceof PaymentInfoNewCard) {
                        PaymentInfoNewCard newCardInfo = (PaymentInfoNewCard) info;
                        log("New card " + newCardInfo.getHolder() + " " + newCardInfo.getSaveCard());
                    } else if (info instanceof PaymentInfoBindCard) {
                        PaymentInfoBindCard bindCard = (PaymentInfoBindCard) info;
                        log("Saved card " + bindCard);
                    }
                    log(result.toString());
                } else if (result.getStatus().isCanceled()) {
                    log("canceled");
                }
            }

            @Override
            public void onFail(Exception e) {
                // Возникла ошибка.
                log(e.toString());
            }
        });
    }
}
```

### Конфигурация SDK

При необходимости, можно переопределить источники получения ключа и информации о типе карты. Можно
использовать готовые решения RemoteKeyProvider, RemoteCardInfoProvider. Так же можно использовать
свой провайдер, реализующий интерфейс KeyProvider или CardInfoProvider соответственно.

```kotlin
    SDKPayment.init(
        SDKConfigBuilder(this)
            .keyProvider(
                RemoteKeyProvider("https://securepayments.sberbank.ru/payment/se/keys.do")
            )
            .cardInfoProvider(
                RemoteCardInfoProvider(
                    url = "https://mrbin.io/bins/display",
                    urlBin = "https://mrbin.io/bins/"
                )
            )
            .build()
    )
```

### Получение ключа с удаленного сервиса

```kotlin
    // Сервис должен отвечать в следующем формате:
    // {
    //     "keys": [
    //     {
    //         "keyValue": "-----BEGIN PUBLIC KEY-----****-----END PUBLIC KEY-----",
    //             "protocolVersion": "RSA",
    //             "keyExpiration": 1598527672000
    //     }
    //  ]
    // }
    SDKPayment.init(
        SDKConfigBuilder(this)
            .keyProvider(
                RemoteKeyProvider("https://securepayments.sberbank.ru/payment/se/keys.do")
            ).build()
    )
```

### Получение информации о карте с удаленного сервиса

```kotlin
    // Сервис должен отвечать в следующем формате:
    // {
    //     "backgroundColor": "#008bd0",
    //     "backgroundGradient": [
    //         "#00bcf2",
    //         "#004e90"
    //     ],
    //     "supportedInvertTheme": true,
    //     "textColor": "#fff",
    //     "logo": "logo/main/a559252b-3772-4b7e-817d-27b16db17580/1.svg",
    //     "logoInvert": "logo/invert/a559252b-3772-4b7e-817d-27b16db17580/1.svg",
    //     "paymentSystem": "mastercard",
    //     "status": "SUCCESS"
    // }
    SDKPayment.init(
        SDKConfigBuilder(this)
            .cardInfoProvider(
                RemoteCardInfoProvider(
                    url = "https://mrbin.io/bins/display",
                    urlBin = "https://mrbin.io/bins/"
                )
            ).build()
    )
```

### Реализация собственных провайдеров

Можно использовать собственные реализации провайдеров для предоставления ключа шифрования и 
информации о карте.

```kotlin
	SDKPayment.init(
            SDKConfigBuilder(this)
                .keyProvider(
                    KeyProvider() {
                    // TODO
                    }
                )
                .cardInfoProvider(
                    CardInfoProvider() {
                    // TODO
                    }                       
                )
                .build()
        )
```

## Стилизация

Для изменения внешнего вида нужно переопределить следующие ресурсы:

```
    // основной цвет
    <color name="rbs_color_primary"></color>
    // основной темный цвет 
    <color name="rbs_color_primaryDark"></color>
    // цвет акцента (выбранного поля ввода)
    <color name="rbs_color_accent"></color>
    // цвет фона формы
    <color name="rbs_color_main_background"></color>
    // цвет текста кнопки
    <color name="rbs_color_button_text"></color>
    // цвет текста формы
    <color name="rbs_color_text"></color>
    // цвет тени
    <color name="rbs_color_shadow"></color>
    // цвет карты по умолчанию
    <color name="rbs_color_card_background"></color>
```