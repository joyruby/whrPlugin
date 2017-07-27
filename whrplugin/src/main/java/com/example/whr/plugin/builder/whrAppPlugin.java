package com.example.whr.plugin.builder;

import com.android.build.gradle.AppPlugin;
import com.example.whr.plugin.builder.manager.PluginManager;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

/**
 * Created by whrwhr446 on 17/07/2017.
 */

public class whrAppPlugin implements Plugin<Project>{
    @Override
    public void apply(Project project) {
        PluginManager.addPlugins(project, AppPlugin.class);
        PluginManager.addPlugins(project,whrPlugin.class);
    }
}
