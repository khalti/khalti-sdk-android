package com.khalti.widget.basic;


import com.khalti.form.api.Config;

interface PayContract {
    interface View {

        void setCustomButtonView();

        void setButtonText(String text);

        void setButtonClick();

        void openForm();

        void setListener(Listener listener);
    }

    interface Listener {

        void setCustomButtonView();

        void setButtonText(String text);

        void setButtonClick();

        void setConfig(Config config);

        void openForm();
    }
}
