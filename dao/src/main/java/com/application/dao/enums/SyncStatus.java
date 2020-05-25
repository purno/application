package com.application.dao.enums;

import java.util.Arrays;
import java.util.List;

public enum SyncStatus {
    PENDING,
    SUCCESS,
    VERIFIED;

    public static List<SyncStatus> getListOfStatusForSyncSuccess(){
        return Arrays.asList(SUCCESS);
    }

    public static List<SyncStatus> getListOfStatusForSyncPending(){
        return Arrays.asList(PENDING);
    }



}
