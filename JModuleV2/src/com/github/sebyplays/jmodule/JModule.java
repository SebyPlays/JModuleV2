package com.github.sebyplays.jmodule;

import com.github.sebyplays.jmodule.utils.Module;
import com.github.sebyplays.logmanager.api.LogManager;
import com.github.sebyplays.logmanager.api.LogType;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.File;
import java.util.ArrayList;

public class JModule {

    @Getter private ArrayList<Module> modules = new ArrayList<>();
    @Getter private File modulesDirectory;

    @SneakyThrows
    public JModule(String path){
        this.modulesDirectory = new File(System.getProperty("user.dir") + "/" + path + "/");
        if(!this.modulesDirectory.exists()){
            this.modulesDirectory.mkdirs();
            LogManager.getLogManager("JModule").log(LogType.INFORMATION, "Module directory has been created..", false, false, true, true);
        }
    }

    public JModule(){
        this(System.getProperty("user.dir") + "/modules/");
    }

    @SneakyThrows
    public void loadModules(){
        for(File file : modulesDirectory.listFiles()){
            if(file.getName().endsWith(".jar")){
                LogManager.getLogManager("JModule").log(LogType.INFORMATION, "Module " + file.getName().toUpperCase() + " is being prepared...", false, false, true, true);
                prepareModule(file);
            }
        }

        for(Module module : modules){
            LogManager.getLogManager("JModule").log(LogType.INFORMATION, "Module " + module.getInfo().getModuleName().toUpperCase() + " is being loaded...", false, false, true, true);
            loadModule(module);
        }
    }

    @SneakyThrows
    public void loadModule(Module module){
        module.load();
        LogManager.getLogManager("JModule").log(LogType.INFORMATION, "Module " + module.getInfo().getModuleName().toUpperCase() + " has been loaded!", false, false, true, true);
    }

    @SneakyThrows
    public void loadModule(File file){
        if(file.getName().endsWith(".jar")){
            prepareModule(file);
            loadModule(getModuleByFile(file));
            LogManager.getLogManager("JModule").log(LogType.INFORMATION, "Module " + file.getName().toUpperCase() + " has been loaded..", false, false, true, true);
            return;
        }
        LogManager.getLogManager("JModule").log(LogType.ERROR, "File " + file.getName().toUpperCase() + " is not a module..", false, false, true, true);
    }

    @SneakyThrows
    public void prepareModule(File file){
        Module module = new Module(this, file);
        modules.add(module);
        LogManager.getLogManager("JModule").log(LogType.INFORMATION, "Module " +
                module.getInfo().getModuleName().toUpperCase() + " has been prepared..", false, false, true, true);
    }

    public boolean moduleIsLoaded(String name){
        for(Module module : modules){
            if(module.getInfo().getModuleName().equalsIgnoreCase(name)){
                return true;
            }
        }
        return false;
    }

    public Module getModuleWithName(String name){
        for(Module module : modules){
            if(module.getInfo().getModuleName().equalsIgnoreCase(name)){
                return module;
            }
        }
        return null;
    }

    public Module getModuleByFile(File file){
        for(Module module : modules){
            if(module.getPath().equals(file)){
                return module;
            }
        }
        return null;
    }

}
