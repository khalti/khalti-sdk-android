package khalti.eBanking;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import khalti.checkOut.EBanking.EBankingContract;
import khalti.checkOut.EBanking.EBankingModel;
import khalti.checkOut.EBanking.EBankingPresenter;
import khalti.checkOut.api.Config;
import khalti.checkOut.api.OnCheckOutListener;
import khalti.utils.Store;
import rx.Subscription;

@RunWith(PowerMockRunner.class)
@PrepareForTest(EBankingPresenter.class)
public class EBankingPresenterTest {
    private EBankingPresenter eBankingPresenter;

    @Mock
    private EBankingContract.View mEBankingView;
    @Mock
    private EBankingModel eBankingModel;
    @Mock
    private Config config;
    @Mock
    private OnCheckOutListener onCheckOutListener;
    //    @Captor
//    private ArgumentCaptor<EBankingModel.BankAction> bankArgument;
    @Mock
    private Subscription subscription;

    @Before
    public void setEBankingPresenter() throws Exception {
        MockitoAnnotations.initMocks(this);

        PowerMockito.whenNew(EBankingModel.class).withNoArguments().thenReturn(eBankingModel);
        PowerMockito.mockStatic(Store.class);

        eBankingPresenter = new EBankingPresenter(mEBankingView);
        config = new Config("public_key", "product_id", "product_name", "product_url", 1L, onCheckOutListener);

        PowerMockito.when(Store.getConfig()).thenReturn(config);
//        Mockito.when(eBankingModel.fetchBankList(bankArgument.capture())).thenReturn(subscription);

        eBankingPresenter.injectModel(eBankingModel);
        eBankingPresenter.injectConfig(config);
    }

    @Test
    public void setUpLayout() {
//        eBankingPresenter.onCreate(true);
//        verify(mEBankingView).toggleButton(false);
    }
}
