package com.khalti.checkout;

import static org.mockito.ArgumentMatchers.any;

//@RunWith(PowerMockRunner.class)
//@PrepareForTest({CheckOutPresenter.class, Store.class})
public class CheckOutPresenterTest {

    /*private CheckOutPresenter presenter;

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

        builder = new Config.Builder(Constant.pub, "Product ID", "Product Name", 1100L, new OnCheckOutListener() {
            @Override
            public void onSuccess(@NotNull Map<String, ?> data) {
                System.out.println(data + "");
            }

            @Override
            public void onError(@NotNull String action, @NotNull String message) {
                System.out.println(action + " " + message);
            }
        });

        config = builder
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
                .build();

        Assert.assertEquals(5, presenter.onGetPreferenceList(config).size());
    }

    @Test
    public void check_onGetPreferenceList_withEmptyPreferenceList() {
        Config config = builder
                .paymentPreferences(new ArrayList<>())
                .build();

        Assert.assertEquals(5, presenter.onGetPreferenceList(config).size());
    }

    @Test
    public void check_onGetPreferenceList_withFilledPreferenceList() {
        Config config = builder
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
    }*/
}