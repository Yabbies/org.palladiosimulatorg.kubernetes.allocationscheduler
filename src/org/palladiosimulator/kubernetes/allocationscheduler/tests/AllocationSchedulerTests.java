package org.palladiosimulator.kubernetes.allocationscheduler.tests;

import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.palladiosimulator.kubernetes.allocationscheduler.AllocationScheduler;
import org.palladiosimulator.kubernetes.allocationscheduler.SchedulerLoaderUtils;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;

import kubernetesModel.resourceenvironment.KubernetesNode;
import tools.mdsd.junit5utils.extensions.PlatformStandaloneExtension;

@ExtendWith(PlatformStandaloneExtension.class)
public class AllocationSchedulerTests {
    
 // paths to the used model files.
    private String k8sSchedulerTestModelSystem = "testmodels/k8sSchedulerTestModelUnallocatedPod/default.system";
    private String k8sSchedulerTestModelDeployment = "testmodels/k8sSchedulerTestModelUnallocatedPod/Deployment.resourceenvironment";
    private String k8sSchedulerTestModelRepository = "testmodels/k8sSchedulerTestModelUnallocatedPod/default.repository";
    private String k8sSchedulerTestModelResourceEnvironment = "testmodels/k8sSchedulerTestModelUnallocatedPod/My.resourceenvironment";
    private String k8sSchedulerTestModelAllocation = "testmodels/k8sSchedulerTestModelUnallocatedPod/default.allocation";

    private org.palladiosimulator.pcm.system.System system;
    private ResourceEnvironment deployment;
    private Repository repository;
    private ResourceEnvironment cluster;
    private Allocation allocation;
    private AllocationScheduler scheduler;
    
    @BeforeEach
    public void loadSystems() {
        system = SchedulerLoaderUtils.loadSystem(k8sSchedulerTestModelSystem);
        // deployment = SchedulerLoaderUtils.loadDeployment(k8sSchedulerTestModelDeployment);
        repository = SchedulerLoaderUtils.loadRepository(k8sSchedulerTestModelRepository);
        cluster = SchedulerLoaderUtils.loadResourceEnvironment(k8sSchedulerTestModelResourceEnvironment);
        allocation = SchedulerLoaderUtils.loadAllocation(k8sSchedulerTestModelAllocation);
        scheduler = new AllocationScheduler();
    }
    
    @Test
    public void testSchedule() {
        Optional<Map<AssemblyContext, KubernetesNode>> result = scheduler.schedule(system, cluster, allocation, repository);
        assert(result.isPresent());
        
        for (AssemblyContext assembly : result.get().keySet()) {
            java.lang.System.out.println("AssemblyContext: " + assembly.getId() + " KubernetesNode: " + result.get().get(assembly).getId());
        }
        
    }

}
