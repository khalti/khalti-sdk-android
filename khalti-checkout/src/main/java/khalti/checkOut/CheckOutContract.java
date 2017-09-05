package khalti.checkOut;

import java.util.HashMap;

import khalti.checkOut.api.Config;

interface CheckOutContract {
    interface View {

        Config getConfig();

        void setupViewPager(HashMap<String, Config> data);

        void setUpTabLayout();

        void setUpToolbar();

        void toggleTab(int position, boolean selected);

        void setStatusBarColor();

        void dismissAllDialogs();

        void setListener(Listener listener);
    }

    interface Listener {

        void setUpLayout();

        void toggleTab(int position, boolean selected);

        void dismissAllDialogs();
    }
}
