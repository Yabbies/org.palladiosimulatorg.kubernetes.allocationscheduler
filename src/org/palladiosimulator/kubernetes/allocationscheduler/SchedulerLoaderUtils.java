package org.palladiosimulator.kubernetes.allocationscheduler;

import java.io.File;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.system.System;

/**
 * This class provides utility methods to load models from files.
 * @author Nathan
 *
 */
public class SchedulerLoaderUtils {

    public static System loadSystem(String path) {
        Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
        Map<String, Object> m = reg.getExtensionToFactoryMap();
        m.put("system", new XMIResourceFactoryImpl());

        ResourceSet resSet = new ResourceSetImpl();
        Resource resource = resSet.getResource(URI.createFileURI(new File(path).getAbsolutePath()), true);
        System system = (System) resource.getContents()
            .get(0);
        return system;
    }
    
    public static Repository loadRepository(String path) {
        Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
        Map<String, Object> m = reg.getExtensionToFactoryMap();
        m.put("repository", new XMIResourceFactoryImpl());

        ResourceSet resSet = new ResourceSetImpl();
        Resource resource = resSet.getResource(URI.createFileURI(new File(path).getAbsolutePath()), true);
        Repository repository = (Repository) resource.getContents()
            .get(0);
        return repository;
    }
    
    public static ResourceEnvironment loadDeployment(String path) {
        Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
        Map<String, Object> m = reg.getExtensionToFactoryMap();
        m.put("resourceenvironment", new XMIResourceFactoryImpl());

        ResourceSet resSet = new ResourceSetImpl();
        Resource resource = resSet.getResource(URI.createFileURI(new File(path).getAbsolutePath()), true);
        ResourceEnvironment deployment = (ResourceEnvironment) resource.getContents()
            .get(0);
        return deployment;
    }
    
    public static ResourceEnvironment loadResourceEnvironment(String path) {
        Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
        Map<String, Object> m = reg.getExtensionToFactoryMap();
        m.put("resourceenvironment", new XMIResourceFactoryImpl());

        ResourceSet resSet = new ResourceSetImpl();
        Resource resource = resSet.getResource(URI.createFileURI(new File(path).getAbsolutePath()), true);
        ResourceEnvironment resourceEnvironment = (ResourceEnvironment) resource.getContents()
            .get(0);
        return resourceEnvironment;
    }
    
    public static Allocation loadAllocation(String path) {
        Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
        Map<String, Object> m = reg.getExtensionToFactoryMap();
        m.put("resourceenvironment", new XMIResourceFactoryImpl());

        ResourceSet resSet = new ResourceSetImpl();
        Resource resource = resSet.getResource(URI.createFileURI(new File(path).getAbsolutePath()), true);
        Allocation allocation = (Allocation) resource.getContents()
            .get(0);
        return allocation;
    }
    
    

}
