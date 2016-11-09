package blue.aodev.materialkeylines.utils;

import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.NonNull;

public class ColorUtil {

    public static int getColor(@NonNull Resources resource, @NonNull Resources.Theme theme,
                               int colorRes) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return resource.getColor(colorRes);
        } else {
            return resource.getColor(colorRes, theme);
        }
    }
}
