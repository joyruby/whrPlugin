package com.example.whr.plugin.builder.hook;
import com.android.build.gradle.AppPlugin;
import com.android.build.gradle.BasePlugin;
import com.android.build.gradle.internal.DependencyManager;
import com.android.build.gradle.internal.ExtraModelInfo;
import com.android.build.gradle.internal.WhrDependencyManager;
import com.android.build.gradle.internal.TaskManager;
import com.example.whr.plugin.builder.tool.ReflectUtils;

import org.apache.tools.ant.util.ReflectUtil;
import org.gradle.api.Project;

/**
 * Created by whrwhr446 on 17/07/2017.
 */

public class AppPluginHook {
    private Project project;
    public AppPluginHook(Project project){
        this.project = project;
    }

    public void replaceTaskManager(){
        AppPlugin appPlugin = project.getPlugins().findPlugin(AppPlugin.class);
        if(null == appPlugin) return;
        TaskManager taskManager = (TaskManager) ReflectUtils.getField(BasePlugin.class,appPlugin,"taskManager");
        DependencyManager dependencyManager = (DependencyManager) ReflectUtils.getField(TaskManager.class,taskManager,"dependencyManager");
        WhrDependencyManager newDependencyManager = new WhrDependencyManager(project,
                (ExtraModelInfo) ReflectUtil.getField(dependencyManager,"extraModelInfo"));
        ReflectUtils.updateField(TaskManager.class,taskManager,"dependencyManager",newDependencyManager);
    }
}
