package ru.rbs.mobile.payment.sample.java;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import ru.rbs.mobile.payment.sdk.PaymentConfigBuilder;
import ru.rbs.mobile.payment.sdk.ResultCallback;
import ru.rbs.mobile.payment.sdk.SDKException;
import ru.rbs.mobile.payment.sdk.SDKPayment;
import ru.rbs.mobile.payment.sdk.model.CameraScannerOptions;
import ru.rbs.mobile.payment.sdk.model.Card;
import ru.rbs.mobile.payment.sdk.model.CardSaveOptions;
import ru.rbs.mobile.payment.sdk.model.ExpiryDate;
import ru.rbs.mobile.payment.sdk.model.HolderInputOptions;
import ru.rbs.mobile.payment.sdk.model.PaymentConfig;
import ru.rbs.mobile.payment.sdk.model.PaymentData;
import ru.rbs.mobile.payment.sdk.model.PaymentInfo;
import ru.rbs.mobile.payment.sdk.model.PaymentInfoBindCard;
import ru.rbs.mobile.payment.sdk.model.PaymentInfoGooglePay;
import ru.rbs.mobile.payment.sdk.model.PaymentInfoNewCard;


public class MainActivity extends AppCompatActivity {

    private Locale launchLocale = Locale.getDefault();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.executeCheckoutButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executeCheckout();
            }
        });
    }

    private void executeCheckout() {
        // Список связанных карт.
        Set<Card> cards = new HashSet();
        cards.add(new Card("492980xxxxxx7724", "ee199a55-cf16-41b2-ac9e-cc1c731edd19", new ExpiryDate(2025, 12)));
        cards.add(new Card("558620xxxxxx6614", "6617c0b1-9976-45d9-b659-364ecac099e2", new ExpiryDate(2024, 6)));
        cards.add(new Card("415482xxxxxx0000", "3d2d320f-ca9a-4713-977c-c852accf8a7b", new ExpiryDate(2019, 1)));
        cards.add(new Card("411790xxxxxx123456", "ceae68c1-cb02-4804-9526-6d6b2f1f2793", null));

        // Идентификатор заказа обязателен.
        String order = "00210bac-0ed1-474b-8ec2-5648cdfc4212";
        PaymentConfig paymentConfig = new PaymentConfigBuilder(order)
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
                .locale(launchLocale)
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
                    }else if (info instanceof PaymentInfoGooglePay) {
                        PaymentInfoGooglePay googlePay = (PaymentInfoGooglePay) info;
                        log("Google pay " + googlePay);
                    }
                    log(result.toString());
                } else if (result.getStatus().isCanceled()) {
                    log("canceled");
                }
            }

            @Override
            public void onFail(SDKException e) {
                // Возникла ошибка.
                log(e.getMessage() + " " + e.getCause());
            }
        });
    }

    private void log(String message) {
        Log.d("LOG_TAG", message);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
