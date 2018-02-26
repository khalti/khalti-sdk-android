package khalti.widget;


import khalti.checkOut.api.Config;

interface PayContract {
    interface View {

        void setCustomButtonView();

        void setButtonStyle(int id);

        void setButtonText(String text);

        void setButtonClick();

        void openForm();

        void destroyCheckOut();

        void setPresenter(Presenter presenter);
    }

    interface Presenter {

        String checkConfig(Config config);

        void setCustomButtonView();

        void setButtonStyle(int id);

        void setButtonText(String text);

        void setButtonClick();

        void openForm();

        void destroyCheckOut();
    }
}
