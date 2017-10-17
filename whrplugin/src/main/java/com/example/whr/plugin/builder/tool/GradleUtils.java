package com.example.whr.plugin.builder.tool;

import com.android.build.gradle.AppExtension;
import com.android.build.gradle.AppPlugin;
import com.android.build.gradle.BasePlugin;
import com.android.build.gradle.api.ApplicationVariant;
import com.android.build.gradle.internal.dsl.ProductFlavor;
import com.android.builder.core.AndroidBuilder;

import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.gradle.api.DomainObjectSet;
import org.gradle.api.Project;

import java.util.Collection;
import java.util.List;

/**
 * Created by whrwhr446 on 20/07/2017.
 */

public class GradleUtils {
    public static List<String> getInputTaskNames(Project project){
        return project.getGradle().getStartParameter().getTaskNames();
    }
    public static AppExtension getAppExtension(Project project){
        return DefaultGroovyMethods.asType(DefaultGroovyMethods.getAt(project.getExtensions(),
                "android"),AppExtension.class);
    }
    public static DomainObjectSet<ApplicationVariant> getVariants(Project project){
        return getAppExtension(project).getApplicationVariants();
    }
    public static Collection<ProductFlavor> getProductFlavors(Project project){
        return getAppExtension(project).getProductFlavors();
    }
    public static AndroidBuilder getAndroidBuilder(Project project){
        BasePlugin basePlugin =project.getPlugins().findPlugin(AppPlugin.class);
        return (AndroidBuilder) ReflectUtils.getField(BasePlugin.class,basePlugin,"androidBuilder");
    }
}
