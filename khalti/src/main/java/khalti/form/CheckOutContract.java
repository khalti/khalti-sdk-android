package khalti.form;

interface CheckOutContract {
    interface View {
        void setupViewPager();

        void setUpTabLayout();

        void setUpToolbar();

        void toggleTab(int position, boolean selected);

        void setListener(Listener listener);
    }

    interface Listener {
        void setUpLayout();

        void toggleTab(int position, boolean selected);
    }
}
