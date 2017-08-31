package khalti.widget;


interface ButtonContract {
    interface View {

        void setCustomButtonView();

        void setButtonStyle(int id);

        void setButtonText(String text);

        void setButtonClick();

        void openForm();

        void destroyCheckOut();

        void setListener(Listener listener);
    }

    interface Listener {

        void setCustomButtonView();

        void setButtonStyle(int id);

        void setButtonText(String text);

        void setButtonClick();

        void openForm();

        void destroyCheckOut();
    }
}
