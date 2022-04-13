package org.palladiosimulator.kubernetes.allocationscheduler.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.palladiosimulator.kubernetes.allocationscheduler.SchedulerLoaderUtils;
import org.palladiosimulator.kubernetes.allocationscheduler.SchedulerUtils;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;

import kubernetesModel.repository.Pod;
import kubernetesModel.resourceenvironment.KubernetesNode;
import tools.mdsd.junit5utils.extensions.PlatformStandaloneExtension;

/**
 * Tests for SchedulerUtils.
 * 
 * @author Nathan
 *
 */
@ExtendWith(PlatformStandaloneExtension.class)
public class SchedulerUtilsTest {

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

    @BeforeEach
    public void loadSystems() {
        system = SchedulerLoaderUtils.loadSystem(k8sSchedulerTestModelSystem);
        // deployment = SchedulerLoaderUtils.loadDeployment(k8sSchedulerTestModelDeployment);
        repository = SchedulerLoaderUtils.loadRepository(k8sSchedulerTestModelRepository);
        cluster = SchedulerLoaderUtils.loadResourceEnvironment(k8sSchedulerTestModelResourceEnvironment);
        allocation = SchedulerLoaderUtils.loadAllocation(k8sSchedulerTestModelAllocation);
    }

    /**
     * The testmodel has only one Pod "Image".
     * This image is assembled twice.
     * Still as the referenced component (Pod) is the same, they got the same Ids.
     */
    @Test
    public void testGetAssembledPodsFromSystem() {
        List<Pod> assembledPods = SchedulerUtils.getAssembledPodsFromSystem(system);
        assert (assembledPods.size() == 2);
        List<Pod> podsInRepository = repository.getComponents__Repository()
            .stream()
            .filter(Pod.class::isInstance)
            .map(Pod.class::cast)
            .collect(Collectors.toList());
        // In the repository there is only one pod "image" which is assembled twice.
        assert (podsInRepository.size() == 1);
        assertEquals(podsInRepository.get(0)
            .getId(),
                assembledPods.get(0)
                    .getId());
        assertEquals(podsInRepository.get(0)
            .getId(),
                assembledPods.get(1)
                    .getId());
    }

    /**
     * The Testmodel contains one unallocated Pod which is assembled in the system model, but
     * unallocated in the allocation model. This test doesn't test for the Pod itself, as this is a
     * reusable repository component, but for the assembly meaning the instance of the pod.
     */
    @Test
    public void testGetUnallocatedPodsAssemblies() {
        List<AssemblyContext> unallocatedPods = SchedulerUtils.getUnallocatedPodAssemblies(system, allocation);
        assert (unallocatedPods.size() == 1);
        assertEquals(unallocatedPods.get(0)
            .getId(), "_5QsIULrCEeysTOpsKumbeA");
    }

    /**
     * The Testmodel contains two KubernetesNodes and one ResourceContainer which is not a
     * KubernetesNode.
     */
    @Test
    public void testGetNodes() {
        List<KubernetesNode> nodes = SchedulerUtils.getNodes(cluster);
        assert (nodes.size() == 2);
        assertTrue(nodes.stream()
            .anyMatch(node -> node.getId()
                .equals("_GUTXYreIEey_eo6N0lN0NQ")));
        assertTrue(nodes.stream()
            .anyMatch(node -> node.getId()
                .equals("_R13WEreIEey_eo6N0lN0NQ")));
    }

    /**
     * The Testmodel contains two assemblies with pods and one with a loadbalancer component.
     */
    @Test
    public void testGetPodAssemblies() {
        List<AssemblyContext> assemblies = SchedulerUtils.getPodAssemblies(system);
        assert (assemblies.size() == 2);
        assertTrue(assemblies.stream()
            .anyMatch(node -> node.getId()
                .equals("_LAGrMLhkEeyjjf2HilH6cg")));
        assertTrue(assemblies.stream()
            .anyMatch(node -> node.getId()
                .equals("_5QsIULrCEeysTOpsKumbeA")));
    }

    /**
     * The CPU Request of the Container allocated on the chose node is 1000. The Node has 2000
     * mCPUs.
     */
    @Test
    public void testCalculateNodesUnrequestedCPUShare() {
        List<KubernetesNode> nodes = SchedulerUtils.getNodes(cluster);
        nodes = nodes.stream()
            .filter(node -> (node.getId()
                .equals("_GUTXYreIEey_eo6N0lN0NQ")))
            .collect(Collectors.toList());
        KubernetesNode workerNode = nodes.get(0);
        int cpushare = SchedulerUtils.calculateNodesUnrequestedCPUShare(workerNode, allocation);
        assert (cpushare == 1000);
    }

    /**
     * The memory Request of the Container allocated on the chosen node is 2048 The Node has Memory
     * of 4096
     */
    @Test
    public void testCalculateNodesUnrequestedMemory() {
        List<KubernetesNode> nodes = SchedulerUtils.getNodes(cluster);
        nodes = nodes.stream()
            .filter(node -> (node.getId()
                .equals("_GUTXYreIEey_eo6N0lN0NQ")))
            .collect(Collectors.toList());
        KubernetesNode workerNode = nodes.get(0);
        int memory = SchedulerUtils.calculateNodesUnrequestedMemory(workerNode, allocation);
        java.lang.System.out.println("Memory left:" + memory);
        assert (memory == 2048);
    }

}
