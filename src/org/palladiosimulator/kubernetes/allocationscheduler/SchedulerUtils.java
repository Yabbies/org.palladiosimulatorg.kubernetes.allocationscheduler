package org.palladiosimulator.kubernetes.allocationscheduler;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.system.System;

import kubernetesModel.k8sconcepts.K8sStandardRequestLimit;
import kubernetesModel.k8sconcepts.K8sconceptsFactory;
import kubernetesModel.k8sconcepts.util.K8sconceptsAdapterFactory;
import kubernetesModel.repository.Container;
import kubernetesModel.repository.Pod;
import kubernetesModel.resourceenvironment.KubernetesNode;

/**
 * This class provides utility methods needed for the K8sAllocationScheduler.
 * 
 * @author Nathan
 *
 */
public class SchedulerUtils {

    /**
     * Returns a collection of pods from the system.
     * 
     * @param system
     *            The system with the pods.
     * @return Collection of Pods that exist in the system.
     */
    public static List<Pod> getAssembledPodsFromSystem(System system) {
        List<AssemblyContext> contexts = system.getAssemblyContexts__ComposedStructure();

        List<Pod> pods = contexts.stream()
            .map(AssemblyContext::getEncapsulatedComponent__AssemblyContext)
            .filter(Pod.class::isInstance)
            .map(Pod.class::cast)
            .collect(Collectors.toList());
        return pods;
    }

    /**
     * Returns all the KubernetesNodes in the given ResourceEnvironment. Usually the
     * ResourceEnvironment is meant to be a cluster.
     * 
     * @param cluster
     *            ResourceEnvironment representing the K8s Cluster with the KubernetesNodes
     * @return Collection of KubernetesNodes in the ResourceEnvironment/Cluster
     */
    public static List<KubernetesNode> getNodes(ResourceEnvironment cluster) {
        List<ResourceContainer> containers = cluster.getResourceContainer_ResourceEnvironment();

        List<KubernetesNode> nodes = containers.stream()
            .filter(KubernetesNode.class::isInstance)
            .map(KubernetesNode.class::cast)
            .collect(Collectors.toList());
        return nodes;
    }

    /**
     * Returns all unscheduled pods by comparing the assembled pods from the system - desired state
     * of instances - with the current state of the cluster - the current allocation - and returns
     * the not allocated pods.
     * 
     * @param system
     * @param allocation
     * @return unallocated pods from the system.
     */
    public static List<Pod> getUnallocatedPods(System system, Allocation allocation) {
        List<Pod> pods = getAssembledPodsFromSystem(system);
        List<AllocationContext> allocationContexts = allocation.getAllocationContexts_Allocation();
        List<Pod> scheduledPods = allocationContexts.stream()
            .map(context -> context.getAssemblyContext_AllocationContext())
            .map(assembly -> assembly.getEncapsulatedComponent__AssemblyContext())
            .filter(Pod.class::isInstance)
            .map(Pod.class::cast)
            .collect(Collectors.toList());
        pods.removeAll(scheduledPods);
        return pods;
    }

    /**
     * Returns all the Unallocated AssemblyContexts that assemble Pods.
     * 
     * @param system
     * @param alloaction
     * @return List of AssemblyContexts that assemble Pods.
     */
    public static List<AssemblyContext> getUnallocatedPodAssemblies(System system, Allocation alloaction) {
        List<AssemblyContext> podAssemblies = getPodAssemblies(system);
        List<AllocationContext> allocations = alloaction.getAllocationContexts_Allocation();
        List<AllocationContext> allocatedPods = allocations.stream()
            .filter(alloc -> (alloc.getAssemblyContext_AllocationContext()
                .getEncapsulatedComponent__AssemblyContext() instanceof Pod))
            .collect(Collectors.toList());

        List<AssemblyContext> toRemove = new ArrayList();
        for (AssemblyContext assembledPod : podAssemblies) {
            for (AllocationContext alloc : allocatedPods) {
                if (assembledPod.getId()
                    .equals(alloc.getAssemblyContext_AllocationContext()
                        .getId())) {
                    toRemove.add(assembledPod);
                }
            }
        }
        podAssemblies.removeAll(toRemove);
        return podAssemblies;
    }

    /**
     * This method returns all AssemblyContexts that contain a Pod in a given System.
     * 
     * @param system
     *            The System with potentially assembled Pods.
     * @return List of AssemblyContexts containing Pods.
     */
    public static List<AssemblyContext> getPodAssemblies(System system) {
        List<AssemblyContext> assemblies = system.getAssemblyContexts__ComposedStructure();
        return assemblies.stream()
            .filter(assembly -> (assembly.getEncapsulatedComponent__AssemblyContext() instanceof Pod))
            .collect(Collectors.toList()); // TODO bessere Lösung ohne instanceof?
    }

    /**
     * This method calculates for a given KubernetesNode and an Allocation, how much free cpu share
     * is left on this node.
     * 
     * @param node
     *            for which the free share needs to be calculated.
     * @param allocation
     *            Allocation for which the left free cpu share is calculated.
     * @return int that represents the free cpu share of the node.
     */
    public static int calculateNodesUnrequestedCPUShare(KubernetesNode node, Allocation allocation) {
        int cpuSpecification = node.getMillicores();
        List<AllocationContext> allocationContexts = allocation.getAllocationContexts_Allocation();
        List<ResourceContainer> nodeWithNestedContainers = node.getNestedResourceContainers__ResourceContainer();
        nodeWithNestedContainers.add(node);
        // Filter Allocations on this node with Allocations on NestedResourceContainers.

        List<AllocationContext> contextsAllocatedOnNode = new ArrayList<AllocationContext>();
        for (ResourceContainer container : nodeWithNestedContainers) {
            List<AllocationContext> contextsToAdd = allocationContexts.stream()
                .filter(ac -> ac.getResourceContainer_AllocationContext()
                    .getId()
                    .equals(container.getId()))
                .collect(Collectors.toList());
            contextsAllocatedOnNode.addAll(contextsToAdd);
        }
        // Only containers can have requests and limits. => Remove all not containers.
        List<Container> containerAllocatedOnNode = contextsAllocatedOnNode.stream()
            .map(context -> context.getAssemblyContext_AllocationContext())
            .map(assembly -> assembly.getEncapsulatedComponent__AssemblyContext())
            .filter(Container.class::isInstance)
            .map(Container.class::cast)
            .collect(Collectors.toList());

        int allocatedCPUShare = 0;

        for (Container container : containerAllocatedOnNode) {
            // Check whether the container has a request as this is an optional attribute.
            if (container.getStandardRequest() != null) {
                allocatedCPUShare += container.getStandardRequest()
                    .getCpu();
            }
        }
        return cpuSpecification - allocatedCPUShare;
    }

    /**
     * This method calculates for a given KubernetesNode and an Allocation, how much free memory is
     * left on this node.
     * 
     * @param node
     *            for which the free share needs to be calculated.
     * @param allocation
     *            for which the left free memory share is calculated.
     * @return int that represents the free memory share of the node.
     */
    public static long calculateNodesUnrequestedMemory(KubernetesNode node, Allocation allocation) {
        long memorySpecification = node.getMemory();
        List<AllocationContext> allocationContexts = allocation.getAllocationContexts_Allocation();
        List<ResourceContainer> nodeWithNestedContainers = node.getNestedResourceContainers__ResourceContainer();
        nodeWithNestedContainers.add(node);
        // Filter Allocations on this node with Allocations on NestedResourceContainers.

        List<AllocationContext> contextsAllocatedOnNode = new ArrayList<AllocationContext>();
        for (ResourceContainer container : nodeWithNestedContainers) {
            List<AllocationContext> contextsToAdd = allocationContexts.stream()
                .filter(ac -> ac.getResourceContainer_AllocationContext()
                    .getId()
                    .equals(container.getId()))
                .collect(Collectors.toList());
            contextsAllocatedOnNode.addAll(contextsToAdd);
        }
        // Only containers can have requests and limits. => Remove all not container objects.
        List<Container> containerAllocatedOnNode = contextsAllocatedOnNode.stream()
            .map(context -> context.getAssemblyContext_AllocationContext())
            .map(assembly -> assembly.getEncapsulatedComponent__AssemblyContext())
            .filter(Container.class::isInstance)
            .map(Container.class::cast)
            .collect(Collectors.toList());

        long allocatedMemory = 0;

        for (Container container : containerAllocatedOnNode) {
            // Check whether the container has a request as this is an optional attribute.
            if (container.getStandardRequest() != null) {
                allocatedMemory += container.getStandardRequest()
                    .getMemory();
            }
        }
        return memorySpecification - allocatedMemory;
    }

    public static int calculatePodsCPURequests(Pod pod) {
        List<Container> encapsulatedContainers = pod.getAssemblyContexts__ComposedStructure()
            .stream()
            .map(ac -> ac.getEncapsulatedComponent__AssemblyContext())
            .filter(Container.class::isInstance)
            .map(Container.class::cast)
            .collect(Collectors.toList());
        int cpuRequest = 0;
        for (Container container : encapsulatedContainers) {
            cpuRequest += container.getStandardRequest().getCpu();
        }
        return cpuRequest;
    }
    
    public static long calculatePodsMemoryRequests(Pod pod) {
        List<Container> encapsulatedContainers = pod.getAssemblyContexts__ComposedStructure()
            .stream()
            .map(ac -> ac.getEncapsulatedComponent__AssemblyContext())
            .filter(Container.class::isInstance)
            .map(Container.class::cast)
            .collect(Collectors.toList());
        long memoryRequest = 0;
        for (Container container : encapsulatedContainers) {
            memoryRequest += container.getStandardRequest().getMemory();
        }
        return memoryRequest;
    }

}
