package khalti.widget;


import khalti.form.api.Config;

interface ButtonContract {
    interface View {

        void setCustomButtonView();

        void setButtonStyle(int id);

        void setButtonText(String text);

        void setButtonClick();

        void openForm();

        void setListener(Listener listener);
    }

    interface Listener {

        void setCustomButtonView();

        void setButtonStyle(int id);

        void setButtonText(String text);

        void setButtonClick();

        void setConfig(Config config);

        void openForm();
    }
}
