package com.ownwell;

import com.example.ContentView;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;

/**
 * //todo 描述
 *
 * @author lixingyun
 * @since 2017-03-14
 */

public class ContentViewField {
    private int mResId;
    TypeElement typeElement;

    public ContentViewField(TypeElement typeElement) {
        if (typeElement.getKind() != ElementKind.CLASS) {
            throw new IllegalArgumentException(String.format("Only class can be annotated with @%s",
                    ContentView.class.getSimpleName()));
        }
        this.typeElement = typeElement;


        ContentView contentView = typeElement.getAnnotation(ContentView.class);
        mResId = contentView.value();
        if (mResId < 0) {
            throw new IllegalArgumentException(
                    String.format("value() in %s for field %s is not valid !", ContentView.class.getSimpleName(),
                            typeElement.getSimpleName()));
        }
    }

    int getResId() {
        return mResId;
    }
}
