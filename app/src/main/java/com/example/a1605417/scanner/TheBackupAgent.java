package com.example.a1605417.scanner;

import android.app.backup.BackupAgentHelper;
import android.app.backup.SharedPreferencesBackupHelper;

public class TheBackupAgent extends BackupAgentHelper {

    static final String TEST = "testcase";
    static final String MY_PREFS_BACKUP_KEY = "testcase";

    public void onCreate() {
        SharedPreferencesBackupHelper helper =
                new SharedPreferencesBackupHelper(this, TEST);
        addHelper(MY_PREFS_BACKUP_KEY, helper);
    }


}