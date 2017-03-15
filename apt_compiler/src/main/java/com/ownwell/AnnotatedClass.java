package com.ownwell;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * //todo 描述
 *
 * @author lixingyun
 * @since 2017-03-14
 */

public class AnnotatedClass {

    private static class TypeUtil {
        static final ClassName BINDER = ClassName.get("com.cyning.aptcore", "ViewBinder");
        static final ClassName PROVIDER = ClassName.get("com.cyning.aptcore", "ViewFinder");
    }

    private TypeElement mTypeElement;
    private ArrayList<BindViewField> mFields;
    private  ContentViewField mContentViewField;
    private Elements mElements;

    AnnotatedClass(TypeElement typeElement, Elements elements) {
        mTypeElement = typeElement;
        mElements = elements;
        mFields = new ArrayList<>();
    }

    void addField(BindViewField field) {
        mFields.add(field);
    }

    public void setContentViewField(ContentViewField contentViewField) {
        this.mContentViewField = contentViewField;
    }

    JavaFile generateFile() {
        //generateMethod
        // add BindView
        MethodSpec.Builder bindViewMethod = MethodSpec.methodBuilder("bindView")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(TypeName.get(mTypeElement.asType()), "host")
                .addParameter(TypeName.OBJECT, "view")
                .addParameter(TypeUtil.PROVIDER, "finder");

        for (BindViewField field : mFields) {
            // find views
            Log.log("------", ClassName.get(field.getFieldType())+"");
            bindViewMethod.addStatement(
                    "host.$N = ($T)(finder.findView(view, $L))",
                    field.getFieldName(),
                    ClassName.get(field.getFieldType()),
                    field.getResId());


        }

        // add BindView
        MethodSpec.Builder unBindViewMethod = MethodSpec.methodBuilder("unBindView")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(TypeName.get(mTypeElement.asType()), "host")
                .addAnnotation(Override.class);
        for (BindViewField field : mFields) {
            unBindViewMethod.addStatement("host.$N = null", field.getFieldName());
        }

        // add setContentView
        if (mContentViewField != null) {
            MethodSpec.Builder setContentBuilder = MethodSpec.methodBuilder("setKSContentView")
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(TypeName.LONG, "contentViewId");
//                .addAnnotation(Override.class)


//            setContentBuilder.addStatement("host.$N = null", field.getFieldName());

        }


        //generaClass
        TypeSpec injectClass = TypeSpec.classBuilder(mTypeElement.getSimpleName() + "$ViewBinder")
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ParameterizedTypeName.get(TypeUtil.BINDER, TypeName.get(mTypeElement.asType())))
                .addMethod(bindViewMethod.build())
                .addMethod(unBindViewMethod.build())
                .build();

        String packageName = mElements.getPackageOf(mTypeElement).getQualifiedName().toString();
        Log.log("AnnotatedClass, generateFile",packageName );

        return JavaFile.builder(packageName, injectClass).build();
    }
}
