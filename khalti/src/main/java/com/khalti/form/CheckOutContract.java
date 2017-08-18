package com.khalti.form;

interface CheckOutContract {
    interface View {
        void setupViewPager();

        void setUpTabLayout();

        void toggleTab(int position, boolean selected);

        void closeForm();

        void setListener(Listener listener);
    }

    interface Listener {
        void setUpLayout();

        void toggleTab(int position, boolean selected);

        void closeForm();
    }
}
