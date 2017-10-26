package hudson.plugins.mavendeploymentlinker;

import hudson.FilePath;
import hudson.Launcher;
import hudson.model.Run;
import hudson.model.TaskListener;

import javax.inject.Inject;

import org.jenkinsci.plugins.workflow.steps.AbstractSynchronousNonBlockingStepExecution;
import org.jenkinsci.plugins.workflow.steps.StepContextParameter;

/**
 * @author Matthias Balke <matthias.balke@googlemail.com>
 */
public class MavenDeploymentLinkerStepExecution extends AbstractSynchronousNonBlockingStepExecution<Void> {

    @StepContextParameter
    private transient TaskListener listener;

    @StepContextParameter
    private transient FilePath ws;

    @StepContextParameter
    private transient Run build;

    @StepContextParameter
    private transient Launcher launcher;

    @Inject
    MavenDeploymentLinkerStep mavenDeploymentLinkerStep;

    @Override
    protected Void run() throws Exception {
        listener.getLogger().println("Recording Maven Deployment Links ...");

        MavenDeploymentLinkerRecorder recorder = new MavenDeploymentLinkerRecorder(mavenDeploymentLinkerStep.getRegexp());
        recorder.perform(build, ws, launcher, listener);

        return null;
    }
}
