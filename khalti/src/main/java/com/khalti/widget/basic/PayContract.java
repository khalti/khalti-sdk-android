package com.khalti.widget.basic;


interface PayContract {
    interface View {
        void setButtonText(String text);

        void openForm();

        void setListener(Listener listener);
    }

    interface Listener {
        void setButtonText(String text);

        void setAmount(Double value);

        void openForm();
    }
}
