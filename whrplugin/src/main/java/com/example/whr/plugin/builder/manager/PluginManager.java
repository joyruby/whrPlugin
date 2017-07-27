package com.example.whr.plugin.builder.manager;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.PluginContainer;

/**
 * Created by whrwhr446 on 11/07/2017.
 */

public class PluginManager {

    public static void addPlugins(Project project, Class<? extends Plugin> clazz){
        PluginContainer pluginContainer = project.getPlugins();
        if(pluginContainer.hasPlugin(clazz)){
            return;
        }
        pluginContainer.apply(clazz);
    }
}
