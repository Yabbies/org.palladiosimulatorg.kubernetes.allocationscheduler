package org.palladiosimulator.kubernetes.allocationscheduler;

import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;

import kubernetesModel.repository.Pod;
import kubernetesModel.resourceenvironment.Deployment;
import org.palladiosimulator.pcm.system.System;

/**
 * This Interface provides methods to implement an AllocationScheduler for a PCM Kubernetes System.
 * @author Nathan
 *
 */
public interface IScheduleAllocation {
    
    /**
     * This method identifies the unscheduled pods.
     * This means the method compares whether the desired state specified in the @Deployment differs from the current state.
     * @return a Pod that needs to be scheduled.
     */
    public Pod identifyUnscheduledPods(Deployment deployment, System system, Allocation allocation);
    
    /**
     * This method returns a recommendation based on basic Kubernetes scheduler rules, where an unscheduled pod should be allocated.
     * @param podToSchedule
     * @param resourceenvironment
     * @param allocation
     * @return the ResourceEnvironment the podToSchedule should be allocated.
     */
    public ResourceEnvironment schedulePod(Pod podToSchedule, ResourceEnvironment resourceenvironment, Allocation allocation);
    
    /**
     * This method is the entry point from the transformation into the scheduling result.
     * @param system The current system.
     * @param resourceenvironment The resourceenvironment representing the cluster.
     * @param allocation The current allocation to calculate the free space per node.
     * @param repository The repository with the pod templates which are scheduled.
     * @param deployment The deployment which describes the desired state.
     * @return
     */
    public Pod schedule(System system, ResourceEnvironment resourceenvironment, Allocation allocation, Repository repository, Deployment deployment);
    
    

}
