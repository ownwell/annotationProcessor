package com.cyning.aptcore;

import android.app.Activity;
import android.view.View;

/**
 * //todo 描述
 *
 * @author lixingyun
 * @since 2017-03-14
 */

public class ActivityViewFinder implements ViewFinder {
    @Override
    public View findView(Object object, int id) {
        return ((Activity) object).findViewById(id);
    }
}