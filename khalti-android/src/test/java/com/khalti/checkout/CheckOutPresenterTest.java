package com.khalti.checkout;

import com.khalti.checkout.helper.CheckoutEventListener;
import com.khalti.checkout.helper.Config;
import com.khalti.checkout.helper.PaymentPreference;
import com.khalti.signal.Signal;
import com.khalti.utils.Constant;
import com.khalti.utils.Store;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

@RunWith(PowerMockRunner.class)
@PrepareForTest({CheckOutPresenter.class, Store.class})
public class CheckOutPresenterTest {

    private CheckOutPresenter presenter;

    @Mock
    private CheckOutContract.View view;
    private Config config;
    private Config.Builder builder;
    private List<PaymentPreference> defaultPaymentPreferences;
    private Map<String, Object> tabSelectedMap = new HashMap<>();

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(Store.class);

        presenter = new CheckOutPresenter(view);

        builder = new Config.Builder(Constant.pub, "Product ID", "Product Name", 1100L);

        config = builder
                .onSuccess(System.out::println)
                .onError((action, message) -> System.out.println(action + " " + message))
                .build();

        defaultPaymentPreferences = new ArrayList<PaymentPreference>() {{
            add(PaymentPreference.KHALTI);
            add(PaymentPreference.EBANKING);
            add(PaymentPreference.MOBILE_BANKING);
            add(PaymentPreference.SCT);
            add(PaymentPreference.CONNECT_IPS);
        }};
        PowerMockito.when(Store.getConfig()).thenReturn(config);

        PowerMockito.when(view.setUpTabLayout(defaultPaymentPreferences)).thenReturn(new Signal<>());

        tabSelectedMap.put("position", 0);
        tabSelectedMap.put("selected", true);
        tabSelectedMap.put("id", "");
    }

    @Test
    public void onCreate() {
        presenter.onCreate();

        Mockito.verify(view).setStatusBarColor();
        Store.setCheckoutEventListener(any(CheckoutEventListener.class));

        Mockito.verify(view).setupViewPager(presenter.onGetPreferenceList(config));
        Mockito.verify(view).toggleTitle(presenter.onGetPreferenceList(config).size() > 1);

        Mockito.verify(view).setUpTabLayout(defaultPaymentPreferences);
    }

    @Test
    public void check_onGetPreferenceList_withNullPreferenceList() {
        Config config = builder
                .onSuccess(System.out::println)
                .onError((action, message) -> System.out.println(action + " " + message))
                .build();

        Assert.assertEquals(5, presenter.onGetPreferenceList(config).size());
    }

    @Test
    public void check_onGetPreferenceList_withEmptyPreferenceList() {
        Config config = builder
                .onSuccess(System.out::println)
                .onError((action, message) -> System.out.println(action + " " + message))
                .paymentPreferences(new ArrayList<>())
                .build();

        Assert.assertEquals(5, presenter.onGetPreferenceList(config).size());
    }

    @Test
    public void check_onGetPreferenceList_withFilledPreferenceList() {
        Config config = builder
                .onSuccess(System.out::println)
                .onError((action, message) -> System.out.println(action + " " + message))
                .paymentPreferences(defaultPaymentPreferences)
                .build();

        Assert.assertEquals(5, presenter.onGetPreferenceList(config).size());
    }

    @Test
    public void check_OnTabSelected() {
        presenter.onTabSelected(defaultPaymentPreferences, tabSelectedMap, 0);
        Mockito.verify(view).toggleTab(anyInt(), anyBoolean(), anyString());
        Mockito.verify(view).setIndicatorBarPosition(anyInt());
        Mockito.verify(view).toggleSearch(anyString(), anyBoolean());
    }
}