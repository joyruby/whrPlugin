package com.example.whr.plugin.builder;


import com.example.whr.plugin.builder.manager.Version;

import org.gradle.api.Plugin;
import org.gradle.api.Project;


/**
 * Created by whrwhr446 on 11/07/2017.
 */

public class whrPlugin implements Plugin<Project> {
    protected Project project;
    public static String creator = "whrPlugin"+ Version.ANDROID_GRADLE_PLUGIN_VERSION;
    @Override
    public void apply(Project project) {
        this.project = project;
        ConfigurationHelper mConfigHelper = new ConfigurationHelper(project,creator);
        mConfigHelper.createPluginConfiguration();
        mConfigHelper.hookPluginDependencyMananger();
    }
}
