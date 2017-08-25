package khalti.checkOut;

interface CheckOutContract {
    interface View {
        void setupViewPager();

        void setUpTabLayout();

        void setUpToolbar();

        void toggleTab(int position, boolean selected);

        void dismissAllDialogs();

        void setListener(Listener listener);
    }

    interface Listener {
        void setUpLayout();

        void toggleTab(int position, boolean selected);

        void dismissAllDialogs();
    }
}
