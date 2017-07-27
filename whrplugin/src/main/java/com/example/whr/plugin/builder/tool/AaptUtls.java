package com.example.whr.plugin.builder.tool;

import com.android.sdklib.BuildToolInfo;

import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.process.ExecSpec;


/**
 * Created by whrwhr446 on 21/07/2017.
 */

public class AaptUtls {
    private static final String ADD = "a";
    private static  void exec(Project project,String workingDir,Object... comandLine){
        project.exec(new Action<ExecSpec>() {
            @Override
            public void execute(ExecSpec execSpec) {
                execSpec.setWorkingDir(workingDir);
                execSpec.commandLine(comandLine);
                System.out.println(execSpec.getCommandLine()+"++==++"+execSpec.getWorkingDir()+"-====-"+workingDir);

            }
        });
    }
    private static String aaptPath(Project project){
        BuildToolInfo buildToolInfo = GradleUtils.getAndroidBuilder(project).getTargetInfo().getBuildTools();
        return buildToolInfo.getPath(BuildToolInfo.PathId.AAPT);
    }
    public static void aaptAddRes(Project project,String resPath,String apkPath,String workingDir){
        String [] commandLine = new String[]{aaptPath(project),ADD,apkPath,resPath};
        exec(project,workingDir,commandLine);
    }
}
