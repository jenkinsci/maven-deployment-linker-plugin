package hudson.plugins.mavendeploymentlinker;

import org.jenkinsci.plugins.workflow.steps.AbstractSynchronousNonBlockingStepExecution;

public class MavenDeploymentLinkerStepExecution extends AbstractSynchronousNonBlockingStepExecution<Void> {

    @Override
    protected Void run() throws Exception {
        System.out.println("Running Maven Deployment Linker Step");
        return null;
    }
}
