package com.khalti.widget.basic;


import com.khalti.form.api.Config;

interface PayContract {
    interface View {
        void setButtonText(String text);

        void openForm();

        void setListener(Listener listener);
    }

    interface Listener {
        void setButtonText(String text);

        void setConfig(Config config);

        void openForm();
    }
}
