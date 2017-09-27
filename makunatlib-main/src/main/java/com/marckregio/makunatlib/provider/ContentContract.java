package com.marckregio.makunatlib.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by makregio on 06/09/2017.
 */

public class ContentContract {

    public static final String AUTH = "packagename.provider";

    public static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTH);

    public static interface MainColumns extends BaseColumns {}

}
