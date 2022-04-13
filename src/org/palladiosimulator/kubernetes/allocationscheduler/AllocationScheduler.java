package org.palladiosimulator.kubernetes.allocationscheduler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.palladiosimulator.kubernetes.*;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.system.System;

import kubernetesModel.repository.Pod;
import kubernetesModel.resourceenvironment.Deployment;
import tools.mdsd.junit5utils.extensions.PlatformStandaloneExtension;

@ExtendWith(PlatformStandaloneExtension.class)
public class AllocationScheduler implements IScheduleAllocation {
    
 // paths to the used model files.
    private String k8sSchedulerTestModelSystem = "testmodels/k8sSchedulerTestModel/default.system";
    private String k8sSchedulerTestModelDeployment = "testmodels/k8sSchedulerTestModel/Deployment.resourceenvironment";
    private String k8sSchedulerTestModelRepository = "testmodels/k8sSchedulerTestModel/default.repository";
    private String k8sSchedulerTestModelResourceEnvironment = "testmodels/k8sSchedulerTestModel/My.resourceenvironment";
    private String k8sSchedulerTestModelAllocation = "testmodels/k8sSchedulerTestModel/default.allocation";
    
    private org.palladiosimulator.pcm.system.System system;
    private ResourceEnvironment deployment;
    private Repository repository;
    private ResourceEnvironment cluster;
    private Allocation allocation;
    
    @BeforeEach
    public void loadSystems() {
        system = SchedulerLoaderUtils.loadSystem(k8sSchedulerTestModelSystem);
        deployment = SchedulerLoaderUtils.loadDeployment(k8sSchedulerTestModelDeployment);
        repository = SchedulerLoaderUtils.loadRepository(k8sSchedulerTestModelRepository);
        cluster = SchedulerLoaderUtils.loadResourceEnvironment(k8sSchedulerTestModelResourceEnvironment);
        allocation = SchedulerLoaderUtils.loadAllocation(k8sSchedulerTestModelAllocation);
    }
    
    
    
    
    @Override
    public Pod identifyUnscheduledPods(Deployment deployment, System system, Allocation allocation) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public ResourceEnvironment schedulePod(Pod podToSchedule, ResourceEnvironment resourceenvironment,
            Allocation allocation) {
        
        
        
        return null;
    }
    @Override
    public Pod schedule(System system, ResourceEnvironment resourceenvironment, Allocation allocation,
            Repository repository, Deployment deployment) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Test
    public void testScheduling() {
        
        
        
    }
}
