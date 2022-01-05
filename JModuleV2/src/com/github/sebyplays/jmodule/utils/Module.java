package com.github.sebyplays.jmodule.utils;

import com.github.sebyplays.jmodule.JModule;
import com.github.sebyplays.logmanager.api.LogManager;
import com.github.sebyplays.logmanager.api.LogType;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.File;

public class Module {

    @Getter private ModuleInfo info;
    @Getter private File path;
    @Getter private boolean isLoaded = false;
    private final JModule jModule;
    private Module dependency;
    private Reflection reflection = new Reflection();
    public Module(JModule jmodule, File file){
        info = new ModuleInfo(file);
        path = file;
        jModule = jmodule;
    }

    @SneakyThrows
    public void load(){
        if(!isLoaded){
            if(info.dependingOnModule != null){
                if(!jModule.moduleIsLoaded(info.dependingOnModule)){
                    dependency = jModule.getModuleWithName(info.dependingOnModule);
                    if(dependency != null){
                        if(!dependency.isLoaded)
                            dependency.load();
                    } else {
                        throw new RuntimeException("Module " + info.getModuleName() + " depends on " + info.dependingOnModule + " but it is not loaded!");
                    }
                }
            }
            invokeOnLoad();
            isLoaded = true;
            invokeOnEnable();
            return;
        }
        LogManager.getLogManager("JModule").log(LogType.ERROR, "Module " + info.getModuleName() + " is already loaded!", false, false, true, true);
    }

    public void invokeOnLoad(){
        reflection.invokeMethod(this, "onLoad");
    }

    public void invokeOnEnable(){
        reflection.invokeMethod(this, "onEnable");
    }

    public void invokeOnDisable(){
        reflection.invokeMethod(this, "onDisable");
    }

}
