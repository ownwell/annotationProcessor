package com.cyning.aptcore;

/**
 * //todo 描述
 *
 * @author lixingyun
 * @since 2017-03-14
 */

public  interface ViewBinder<T> {
    void bindView(T host, Object object, ViewFinder finder);

    void unBindView(T host);
}
