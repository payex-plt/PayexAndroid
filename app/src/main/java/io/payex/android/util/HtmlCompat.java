package io.payex.android.util;

import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.TextView;

public class HtmlCompat {

    public static void setSpannedText(View view, String text) {
        Spanned spannedText;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            spannedText = Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY);
        } else {
            spannedText = Html.fromHtml(text);
        }
        if (view instanceof TextView) {
            ((TextView) view).setText(spannedText);
        }
    }
}
