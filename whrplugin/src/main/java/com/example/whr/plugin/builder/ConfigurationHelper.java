package com.example.whr.plugin.builder;

import com.android.build.gradle.api.ApplicationVariant;
import com.android.build.gradle.api.BaseVariantOutput;
import com.example.whr.plugin.builder.hook.AppPluginHook;
import com.example.whr.plugin.builder.tool.AaptUtls;
import com.example.whr.plugin.builder.tool.GradleUtils;

import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.Dependency;

import org.gradle.api.file.ConfigurableFileTree;
import org.gradle.api.file.CopySpec;
import org.gradle.api.internal.artifacts.dependencies.DefaultProjectDependency;

import java.io.File;

import java.util.List;
import java.util.function.Consumer;


/**
 * Created by whrwhr446 on 11/07/2017.
 */

public class ConfigurationHelper {
    private static final String PLUGIN_COMPILE = "pluginCompile";
    private static final String DEBUG_INJECTASKNAME = "prePackageMarkerFor";
    private static final String RELEASE_INJECTASKNAME = "transformClassesAndResourcesWithProguardFor";
    private Project project;
    private String PLUGIN_ASSETS_PATH = "/assets/plugins/";
    private String creator;
    private AppPluginHook appPluginHook;

    public ConfigurationHelper(Project mProject,  String creator) {
        this.project = mProject;
        this.creator = creator;
        this.appPluginHook = new AppPluginHook(mProject);
        this.PLUGIN_ASSETS_PATH = project.getProjectDir()+PLUGIN_ASSETS_PATH;
    }
    public void createPluginConfiguration(){
        Configuration pluginConfiguration = project.getConfigurations().findByName(PLUGIN_COMPILE);
        if(null == pluginConfiguration){
            Configuration config = project.getConfigurations().create(PLUGIN_COMPILE);
            project.afterEvaluate(new Action<Project>() {
                @Override
                public void execute(Project project) {
                    List<String> taskNames = GradleUtils.getInputTaskNames(project);
                    for(String taskName : taskNames){
                        config.getDependencies().forEach(new Consumer<Dependency>() {
                            @Override
                            public void accept(Dependency dependency) {
                                if(dependency instanceof DefaultProjectDependency){
                                    DefaultProjectDependency projectDependency = (DefaultProjectDependency) dependency;
                                    Project pluginProject = projectDependency.getDependencyProject();
                                    Task injectTask = getInjectTask();
                                    BaseVariantOutput appVariantOutput = getVariantOutputByTaskName(project,taskName);
                                    BaseVariantOutput pluginVariantOutput = getVariantOutputByTaskName(pluginProject,taskName);
                                    if(null != pluginVariantOutput && null != appVariantOutput){
                                        Task assembleTask = pluginVariantOutput.getAssemble();
                                        injectTask.doLast(new Action<Task>() {
                                            @Override
                                            public void execute(Task task) {
                                                //混淆任务完成之后，将mapping文件copy到app项目的更目录
                                                copyMapping(getVariantByTaskName(project,taskName));
                                            }
                                        });
                                        //注入plugin的assemble任务
                                        injectTask.finalizedBy(assembleTask);

                                        //plugin任务执行完之后，将资源
                                        assembleTask.doLast(new Action<Task>() {
                                            @Override
                                            public void execute(Task task) {

                                                //这里copy一次，原因aapt add 没找到能指定添加路径的参数。
                                                clearFile(pluginProject,PLUGIN_ASSETS_PATH);
                                                String pluginDistPath =copyPluginApkToAssets(pluginVariantOutput);

                                                //这是签名之前的apk的路径
                                                String packagedProcessAp = appVariantOutput.getProcessResources().getPackageOutputFile().getAbsolutePath();
                                                pluginDistPath = pluginDistPath.replaceAll(project.getProjectDir().getAbsolutePath()+"/","");
                                                AaptUtls.aaptAddRes(project,pluginDistPath,packagedProcessAp,project.getProjectDir().getAbsolutePath());
                                            }
                                        });
                                    }

                                }
                            }
                        });
                    }


                }
            });

        }

    }
    public void hookPluginDependencyMananger(){
        appPluginHook.replaceTaskManager();
    }
    private Task getInjectTask(){
        final Task[] task = new Task[1];
        project.getTasks().forEach(new Consumer<Task>() {
            @Override
            public void accept(Task tk) {
                if(tk.getName().indexOf(RELEASE_INJECTASKNAME) != -1){
                    task[0] = tk;
                }
                if(tk.getName().indexOf(DEBUG_INJECTASKNAME) != -1){
                    task[0] = tk;
                }
            }
        });
        return task[0];
    }
    private void copyFile(String dist,File src){
        project.copy(new Action<CopySpec>() {
            @Override
            public void execute(CopySpec copySpec) {
                File parentDir =src.getParentFile();
                copySpec.from(parentDir.getAbsolutePath());
                copySpec.include(src.getName());
                copySpec.into(dist);
            }
        });
    }
    private void clearFile(Project project,String path){
        File file = new File(path);


        ConfigurableFileTree fileTree =project.fileTree(new File(path));
        fileTree.include(project.getName()+"*.apk");
        project.delete(fileTree);
    }
    private String copyPluginApkToAssets(BaseVariantOutput output){
        File pluginApkOutFile = output.getOutputFile();
        copyFile(PLUGIN_ASSETS_PATH,pluginApkOutFile);
        return PLUGIN_ASSETS_PATH+pluginApkOutFile.getName();
    }
    private void copyMapping(ApplicationVariant variant){
        File mappingFile = variant.getMappingFile();
        if(null != mappingFile){
            copyFile(project.getProjectDir().getAbsolutePath(),mappingFile);
        }

    }
    private BaseVariantOutput getVariantOutputByTaskName(Project pt,String taskName){
        if(taskName == null) return null;
        final BaseVariantOutput[] baseVariantOutput = new BaseVariantOutput[1];
        ApplicationVariant variant = getVariantByTaskName(pt,taskName);
        if(variant == null) return null;
        variant.getOutputs().forEach(new Consumer<BaseVariantOutput>() {
            @Override
            public void accept(BaseVariantOutput variantOutput) {
                Task assembleTask = variantOutput.getAssemble();
                if(taskName.indexOf(assembleTask.getName()) != -1){
                    baseVariantOutput[0] = variantOutput;
                }
            }
        });
        return baseVariantOutput[0];
    }
    private ApplicationVariant  getVariantByTaskName(Project pt, String taskName){
        if(taskName == null) return null;
        final ApplicationVariant[] variants = new ApplicationVariant[1];
        GradleUtils.getVariants(pt).forEach(new Consumer<ApplicationVariant>() {
            @Override
            public void accept(ApplicationVariant variant) {
                Task assembleTask = variant.getAssemble();
                if(taskName.indexOf(assembleTask.getName()) != -1){
                    variants[0] = variant;
                }
            }
        });
        return variants[0];
    }
}
