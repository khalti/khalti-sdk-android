package com.khalti.checkOut.service;

import androidx.annotation.Nullable;

import com.khalti.checkOut.helper.Config;

import java.io.Serializable;

public interface ConfigServiceComm extends Serializable {

    interface GetConfig extends Serializable{
        @Nullable
        public Config getConfig();
    }

    interface StopService extends Serializable{
        public void stopService();
    }
}
