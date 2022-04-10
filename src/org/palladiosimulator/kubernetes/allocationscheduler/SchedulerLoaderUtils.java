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

public class SchedulerLoaderUtils {
    
    private final static String k8sSchedulerTestModelSystem = "testmodels/k8sSchedulerTestModel/default.system";
    private final static String k8sSchedulerTestModelDeployment = "testmodels/k8sSchedulerTestModel/Deployment.resourceenvironment";
    private final static String k8sSchedulerTestModelRepository = "testmodels/k8sSchedulerTestModel/default.repository";
    private final static String k8sSchedulerTestModelResourceEnvironment = "testmodels/k8sSchedulerTestModel/My.resourceenvironment";
    private final static String k8sSchedulerTestModelAllocation = "testmodels/k8sSchedulerTestModel/default.allocation";
    
    protected static System loadSystem() {
        Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
        Map<String, Object> m = reg.getExtensionToFactoryMap();
        m.put("system", new XMIResourceFactoryImpl());

        ResourceSet resSet = new ResourceSetImpl();
        Resource resource = resSet.getResource(URI.createFileURI(new File(k8sSchedulerTestModelSystem).getAbsolutePath()), true);
        System system = (System) resource.getContents()
            .get(0);
        return system;
    }
    
    protected static Repository loadRepository() {
        Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
        Map<String, Object> m = reg.getExtensionToFactoryMap();
        m.put("repository", new XMIResourceFactoryImpl());

        ResourceSet resSet = new ResourceSetImpl();
        Resource resource = resSet.getResource(URI.createFileURI(new File(k8sSchedulerTestModelRepository).getAbsolutePath()), true);
        Repository repository = (Repository) resource.getContents()
            .get(0);
        return repository;
    }
    
    protected static ResourceEnvironment loadDeployment() {
        Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
        Map<String, Object> m = reg.getExtensionToFactoryMap();
        m.put("resourceenvironment", new XMIResourceFactoryImpl());

        ResourceSet resSet = new ResourceSetImpl();
        Resource resource = resSet.getResource(URI.createFileURI(new File(k8sSchedulerTestModelDeployment).getAbsolutePath()), true);
        ResourceEnvironment deployment = (ResourceEnvironment) resource.getContents()
            .get(0);
        return deployment;
    }
    
    protected static ResourceEnvironment loadResourceEnvironment() {
        Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
        Map<String, Object> m = reg.getExtensionToFactoryMap();
        m.put("resourceenvironment", new XMIResourceFactoryImpl());

        ResourceSet resSet = new ResourceSetImpl();
        Resource resource = resSet.getResource(URI.createFileURI(new File(k8sSchedulerTestModelResourceEnvironment).getAbsolutePath()), true);
        ResourceEnvironment resourceEnvironment = (ResourceEnvironment) resource.getContents()
            .get(0);
        return resourceEnvironment;
    }
    
    protected static Allocation loadAllocation() {
        Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
        Map<String, Object> m = reg.getExtensionToFactoryMap();
        m.put("resourceenvironment", new XMIResourceFactoryImpl());

        ResourceSet resSet = new ResourceSetImpl();
        Resource resource = resSet.getResource(URI.createFileURI(new File(k8sSchedulerTestModelAllocation).getAbsolutePath()), true);
        Allocation allocation = (Allocation) resource.getContents()
            .get(0);
        return allocation;
    }
    
    

}
