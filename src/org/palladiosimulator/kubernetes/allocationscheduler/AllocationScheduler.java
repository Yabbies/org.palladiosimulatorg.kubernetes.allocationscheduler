package org.palladiosimulator.kubernetes.allocationscheduler;

import org.junit.jupiter.api.extension.ExtendWith;
import org.palladiosimulator.kubernetes.*;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;

import kubernetesModel.resourceenvironment.Deployment;
import tools.mdsd.junit5utils.extensions.PlatformStandaloneExtension;

@ExtendWith(PlatformStandaloneExtension.class)
public class AllocationScheduler {
    
    private org.palladiosimulator.pcm.system.System system;
    private Deployment deployment;
    private Repository repository;
    private ResourceEnvironment cluster;
}
