package com.ownwell;

import com.example.BindView;
import com.example.ContentView;
import com.google.auto.service.AutoService;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

/**
 * //todo 描述
 *
 * @author lixingyun
 * @since 2017-03-14
 */

@AutoService(Processor.class)
public class LCJViewBinderProcessor extends AbstractProcessor {
    private Filer mFiler; //文件相关的辅助类
    private Elements mElementUtils; //元素相关的辅助类
    private Messager mMessager; //日志相关的辅助类
    private Map<String,AnnotatedClass> mAnnotatedClassMap;
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mFiler = processingEnv.getFiler();
        mElementUtils = processingEnv.getElementUtils();
        mMessager = processingEnv.getMessager();
        mAnnotatedClassMap = new TreeMap();
    }


    @Override
    public boolean process(Set annotations, RoundEnvironment roundEnv) {
        mAnnotatedClassMap.clear();
        try {
            processBindView(roundEnv);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            error(e.getMessage());
        }

        for (AnnotatedClass annotatedClass : mAnnotatedClassMap.values()) {
            try {
                annotatedClass.generateFile().writeTo(mFiler);
            } catch (IOException e) {
                error("Generate file failed, reason: %s", e.getMessage());
            }
        }
        return true;
    }

    private void processBindView(RoundEnvironment roundEnv) throws IllegalArgumentException {

        for (Element element : roundEnv.getElementsAnnotatedWith(ContentView.class)) {

            if (element instanceof TypeElement) {
                TypeElement typeElement = (TypeElement) element;
                AnnotatedClass annotatedClass =  getClassFromMap(typeElement);
                ContentViewField contentViewField = new ContentViewField(typeElement);
                annotatedClass.setContentViewField(contentViewField);
            }
            Log.log("LCJViewBinderProcessor,getAnnotatedClass",element.toString() );
        }
        // 查找带有BindView的元素
        for (Element element : roundEnv.getElementsAnnotatedWith(BindView.class)) {
            // 获取外面的Activty
            AnnotatedClass annotatedClass = getAnnotatedClass(element);
            BindViewField bindViewField = new BindViewField(element);
            annotatedClass.addField(bindViewField);
        }


    }


    private AnnotatedClass getAnnotatedClass(Element element) {
        Log.log("LCJViewBinderProcessor:79,getAnnotatedClass",element.toString() );
        TypeElement typeElement = (TypeElement) element.getEnclosingElement();
        AnnotatedClass annotatedClass = getClassFromMap(typeElement);
        return annotatedClass;
    }

    private AnnotatedClass getClassFromMap(TypeElement typeElement) {
        String fullName = typeElement.getQualifiedName().toString();
        Log.log("LCJViewBinderProcessor 99 ,getAnnotatedClass",fullName );
        AnnotatedClass annotatedClass = mAnnotatedClassMap.get(fullName);
        if (annotatedClass == null) {
            annotatedClass = new AnnotatedClass(typeElement, mElementUtils);
            mAnnotatedClassMap.put(fullName, annotatedClass);
        }
        return annotatedClass;
    }

    /**
     * 处理错误
     * @param msg
     * @param args
     */
    private void error(String msg, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args));
    }


    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }


    /**
     * 注解的类型
     * @return
     */
    @Override
    public Set getSupportedAnnotationTypes() {
        Set types = new LinkedHashSet();
        types.add(BindView.class.getCanonicalName());
        return types;
    }
}
