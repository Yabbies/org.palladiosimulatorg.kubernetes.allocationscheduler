package org.palladiosimulator.kubernetes.allocationscheduler;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;

import kubernetesModel.repository.Pod;
import kubernetesModel.resourceenvironment.Deployment;
import kubernetesModel.resourceenvironment.KubernetesNode;

import org.palladiosimulator.pcm.system.System;

/**
 * This Interface provides methods to implement an AllocationScheduler for a PCM Kubernetes System.
 * 
 * @author Nathan
 *
 */
public interface IScheduleAllocation {

    /**
     * This method identifies an unscheduled AssemblyContext containing a Pod. This means the method compares whether the
     * desired state specified in the System through assemblies differs from the current state
     * represented by the current allocation.
     * It is important to note, that this method returns one Pod that should be scheduled.
     * If there are more Pods that need to be scheduled, it still returns only one.
     * 
     * @return an AssemblyContext containing a Pod that needs to be scheduled.
     */
    public Optional<AssemblyContext> identifyUnscheduledPod(System system, Allocation allocation);
    
    /**
     * This method identifies all unscheduled AssemblyContexts containing a Pod. This means the method compares whether the
     * desired state specified in the System through assemblies differs from the current state
     * represented by the current allocation.
     * 
     * @return a List of Assemblies containing Pods that need to be scheduled.
     */
    public List<AssemblyContext> identifyUnscheduledPods(System system, Allocation allocation);

    /**
     * This method returns a recommendation, where an unscheduled pod should be allocated. The
     * Scheduler selects a KubernetesNode which has enough CPU and Memory free to allocate the Pod
     * with it's requested resources.
     * 
     * @param podToSchedule
     * @param resourceenvironment
     * @param allocation
     * @return the ResourceEnvironment the podToSchedule should be allocated.
     */
    public Optional<KubernetesNode> schedulePod(Pod podToSchedule, ResourceEnvironment resourceenvironment,
            Allocation allocation);

    /**
     * This method is the entry point from the transformation into the scheduling result.
     * 
     * @param system
     *            The current system.
     * @param resourceenvironment
     *            The resourceenvironment representing the cluster.
     * @param allocation
     *            The current allocation to calculate the free space per node.
     * @param repository
     *            The repository with the pod templates which are scheduled.
     * @return
     */
    public Optional<Map<AssemblyContext, KubernetesNode>> schedule(System system, ResourceEnvironment resourceenvironment, Allocation allocation,
            Repository repository);
    

}
