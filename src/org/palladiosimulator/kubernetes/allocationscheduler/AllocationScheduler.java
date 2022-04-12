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
    
    private org.palladiosimulator.pcm.system.System system;
    private ResourceEnvironment deployment;
    private Repository repository;
    private ResourceEnvironment cluster;
    
    @BeforeEach
    public void loadSystems() {
        system = SchedulerLoaderUtils.loadSystem();
        deployment = SchedulerLoaderUtils.loadDeployment();
        repository = SchedulerLoaderUtils.loadRepository();
        cluster = SchedulerLoaderUtils.loadResourceEnvironment();
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
        System testSystem = SchedulerLoaderUtils.loadSystem();
        
    }
}
