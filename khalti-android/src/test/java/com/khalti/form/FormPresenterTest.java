package com.khalti.form;

//@RunWith(PowerMockRunner.class)
//@PrepareForTest({FormPresenter.class, Store.class})
public class FormPresenterTest {

    /*private FormPresenter formPresenter;

    @Mock
    private FormContract.View view;
    @Mock
    FormModel model;
    @Mock
    Config config;*/


    /*private WalletPresenter walletPresenter;

    @Mock
    private WalletContract.View mWalletView;
    @Mock
    private WalletModel walletModel;
    @Mock
    private Config config;
    @Mock
    private OnCheckOutListener onCheckOutListener;
    @Captor
    private ArgumentCaptor<WalletModel.WalletAction> walletArgument;
    @Mock
    private Subscription subscription;
    @Mock
    private WalletConfirmPojo walletConfirmPojo;

    private String mobile = "9800000000";
    private String confirmationCode = "123";
    private String pin = "0000";

    @Before
    public void setWalletPresenter() throws Exception {
        MockitoAnnotations.initMocks(this);

        PowerMockito.whenNew(WalletModel.class).withNoArguments().thenReturn(walletModel);
        PowerMockito.mockStatic(Store.class);

        walletPresenter = new WalletPresenter(mWalletView);
        config = new Config("public_key", "product_id", "product_name", "product_url", 1L, onCheckOutListener);

        PowerMockito.when(Store.getConfig()).thenReturn(config);
        Mockito.when(walletModel.onInitiatePayment(eq(mobile), eq(config), walletArgument.capture())).thenReturn(subscription);
        Mockito.when(walletModel.onConfirmPayment(eq(confirmationCode), eq(pin), walletArgument.capture())).thenReturn(subscription);

        walletPresenter.injectModel(walletModel);
        walletPresenter.injectConfig(config);
    }

    @Test
    public void setUpLayout() {
        walletPresenter.setUpLayout();
        verify(mWalletView).onSetButtonText(Mockito.anyString());
        verify(mWalletView).setButtonClickListener();
    }

    @Test
    public void initiatePaymentWithoutNetwork() {
        walletPresenter.onInitiatePayment(false, mobile);
        verify(mWalletView).showNetworkError();
    }

    @Test
    public void successfulPaymentInitiation() {
        walletPresenter.onInitiatePayment(true, mobile);
        verify(mWalletView).toggleProgressDialog("init", true);
        verify(walletModel).onInitiatePayment(eq(mobile), eq(config), walletArgument.capture());
        walletArgument.getValue().onCompleted(null);
        verify(mWalletView).toggleSmsListener(true);
        verify(mWalletView).toggleProgressDialog("init", false);
        verify(mWalletView).toggleConfirmationLayout(true);
    }

    @Test
    public void failedPaymentInitiationWithNoPIN() {
        walletPresenter.onInitiatePayment(true, mobile);
        verify(mWalletView).toggleProgressDialog("init", true);
        verify(walletModel).onInitiatePayment(eq(mobile), eq(config), walletArgument.capture());
        walletArgument.getValue().onError("</a>");
        verify(mWalletView).showPINDialog(anyString(), anyString());
        verify(config.getOnCheckOutListener()).onError(anyString(), anyString());
    }

    @Test
    public void failedPaymentInitiation() {
        walletPresenter.onInitiatePayment(true, mobile);
        verify(mWalletView).toggleProgressDialog("init", true);
        verify(walletModel).onInitiatePayment(eq(mobile), eq(config), walletArgument.capture());
        walletArgument.getValue().onError("");
        verify(mWalletView).showMessageDialog(Mockito.anyString(), Mockito.anyString());
        verify(config.getOnCheckOutListener()).onError(eq(ErrorAction.WALLET_INITIATE.getAction()), anyString());
    }

    @Test
    public void confirmPaymentWithoutNetwork() {
        walletPresenter.onConfirmPayment(false, confirmationCode, pin);
        verify(mWalletView).showNetworkError();
    }

    @Test
    public void failedPaymentConfirmation() {
        walletPresenter.onConfirmPayment(true, confirmationCode, pin);
        verify(mWalletView).toggleProgressDialog("confirm", true);
        verify(walletModel).onConfirmPayment(eq(confirmationCode), eq(pin), walletArgument.capture());
        walletArgument.getValue().onError(eq(anyString()));
        verify(mWalletView).toggleProgressDialog("confirm", false);
        verify(mWalletView).showMessageDialog(eq("Error"), anyString());
        verify(config.getOnCheckOutListener()).onError(eq(ErrorAction.WALLET_CONFIRM.getAction()), anyString());
    }

    @Test
    public void successfulPaymentConfirmation() {
        walletPresenter.onConfirmPayment(true, confirmationCode, pin);
        verify(mWalletView).toggleProgressDialog("confirm", true);
        verify(walletModel).onConfirmPayment(eq(confirmationCode), eq(pin), walletArgument.capture());
        walletArgument.getValue().onCompleted(walletConfirmPojo);
        verify(mWalletView).toggleProgressDialog("confirm", false);
        verify(onCheckOutListener).onSuccess(any());
        verify(mWalletView).closeWidget();
    }*/
}
