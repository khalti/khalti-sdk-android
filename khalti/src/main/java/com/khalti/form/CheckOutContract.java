package com.khalti.form;

interface CheckOutContract {
    interface View {
        void setupViewPager();

        void attachListenerToTabLayout();

        void setListener(Listener listener);
    }

    interface Listener {
        void setUpLayout();
    }
}
