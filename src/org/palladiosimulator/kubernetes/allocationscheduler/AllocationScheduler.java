package org.palladiosimulator.kubernetes.allocationscheduler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.extension.ExtendWith;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.system.System;

import kubernetesModel.repository.Pod;
import kubernetesModel.resourceenvironment.KubernetesNode;
import tools.mdsd.junit5utils.extensions.PlatformStandaloneExtension;

@ExtendWith(PlatformStandaloneExtension.class)
public class AllocationScheduler implements IScheduleAllocation {

    @Override
    public Optional<AssemblyContext> identifyUnscheduledPod(System system, Allocation allocation) {
        List<AssemblyContext> unallocatedAsseblyPods = SchedulerUtils.getUnallocatedPodAssemblies(system, allocation);
        return Optional.ofNullable(unallocatedAsseblyPods.get(0));
    }

    @Override
    public Optional<KubernetesNode> schedulePod(Pod podToSchedule, ResourceEnvironment resourceenvironment,
            Allocation allocation) {
        int podsCPURequest = SchedulerUtils.calculatePodsCPURequests(podToSchedule);
        long podsMemoryRequest = SchedulerUtils.calculatePodsMemoryRequests(podToSchedule);
        List<KubernetesNode> nodes = SchedulerUtils.getNodes(resourceenvironment);

        for (KubernetesNode node : nodes) {
            int freeCPU = SchedulerUtils.calculateNodesUnrequestedCPUShare(node, allocation);
            long freeMemory = SchedulerUtils.calculateNodesUnrequestedMemory(node, allocation);
            if ((freeCPU < podsCPURequest) || (freeMemory < podsMemoryRequest)) {
                nodes.remove(node);
            }
        }
        return Optional.ofNullable(nodes.get(0));
    }

    @Override
    public Optional<Map<AssemblyContext, KubernetesNode>> schedule(System system, ResourceEnvironment resourceenvironment, Allocation allocation,
            Repository repository) {
        Optional<AssemblyContext> toSchedule = identifyUnscheduledPod(system, allocation);
        if (toSchedule.isEmpty()) {
            return Optional.ofNullable(null);
        }
        //Cast is here safely possible, as identifyUnscheduledPod can only return AssemblyContext with Pod as EncapsulatedComponent.
        Optional<KubernetesNode> nodeToAllocateOn = schedulePod((Pod)toSchedule.get().getEncapsulatedComponent__AssemblyContext(), resourceenvironment, allocation);
        HashMap<AssemblyContext, KubernetesNode> schedulingResult = new HashMap<AssemblyContext, KubernetesNode>();
        schedulingResult.put(toSchedule.get(), nodeToAllocateOn.get());
        return Optional.ofNullable(schedulingResult);
    }

    @Override
    public List<AssemblyContext> identifyUnscheduledPods(System system,
            Allocation allocation) {
        return SchedulerUtils.getUnallocatedPodAssemblies(system, allocation);
    }

}
