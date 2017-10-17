package com.example.whr.plugin.builder.configuration;

import com.android.build.gradle.api.ApplicationVariant;
import com.android.build.gradle.internal.dsl.ProductFlavor;
import com.example.whr.plugin.builder.tool.GradleUtils;
import com.example.whr.plugin.builder.tool.StringUtils;

import org.gradle.api.Action;
import org.gradle.api.DomainObjectSet;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ConfigurationContainer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by whrwhr446 on 25/09/2017.
 * 使用pluginCompile支持flavor的定义
 */

public class BaseConfiguration {
    private Project mProject;
    private String mName;
    private Configuration generalConfiguration;
    private ConfigurationContainer configurations;
    private HashMap<String,Configuration> configurationMap;
    public BaseConfiguration(Project project,String name){
        this.mProject = project;
        this.mName = name;
        this.configurations = project.getConfigurations();
        init();
    }

    private void init(){
        this.generalConfiguration = create(mName);
        createAllWithFlavors();
    }

    private void createAllWithFlavors(){
        if(configurationMap != null) return;
        configurationMap = new HashMap<>();
        configurations.whenObjectAdded(new Action<Configuration>() {
            @Override
            public void execute(Configuration files) {
                List<String> flavorNames = getFlavorNameList(getProductFlavors());

                for(String flavorName:flavorNames){
                    configurationMap.put(flavorName,create(getFlavorConfigurationName(flavorName)));

                }
            }
        });

    }
    public Configuration  getConfigurationByTaskName(String taskName){
        List<String> flavorsNames = getFlavorNameList(getProductFlavors());
        for(String flavorName:flavorsNames){
            if(StringUtils.lowercaseContains(taskName,flavorName)){
                Configuration tempConfig = configurationMap.get(flavorName);
                if(tempConfig != null && tempConfig.getDependencies().size() > 0){
                    return tempConfig;
                }
            }
        }
        return generalConfiguration;
    }


    private Configuration create(String name){
        Configuration config = configurations.findByName(name);
        if(config == null){
            config = configurations.create(name);
        }
        return config;
    }

    private List<String> getFlavorNameList(Collection<ProductFlavor> productFlavors){
        List<String> flavors = new ArrayList<>();
        productFlavors.forEach(new Consumer<ProductFlavor>() {
            @Override
            public void accept(ProductFlavor productFlavor) {
                flavors.add(productFlavor.getName());
            }
        });
        return flavors;
    }

    private Collection<ProductFlavor> getProductFlavors(){
        return GradleUtils.getProductFlavors(mProject);
    }

    /**
     *
     * @param fName
     * @return
     */

    private String  getFlavorConfigurationName(String fName){
        return fName + StringUtils.upperCase(mName);
    }
}
