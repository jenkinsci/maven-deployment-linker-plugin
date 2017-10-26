package hudson.plugins.mavendeploymentlinker;

import hudson.model.TaskListener;

import org.jenkinsci.plugins.workflow.steps.AbstractSynchronousNonBlockingStepExecution;
import org.jenkinsci.plugins.workflow.steps.StepContextParameter;

public class MavenDeploymentLinkerStepExecution extends AbstractSynchronousNonBlockingStepExecution<Void> {

    @StepContextParameter
    private transient TaskListener listener;

    @Override
    protected Void run()
            throws Exception {
        listener.getLogger().println("Running Maven Deployment Linker step");
        return null;
    }
}
