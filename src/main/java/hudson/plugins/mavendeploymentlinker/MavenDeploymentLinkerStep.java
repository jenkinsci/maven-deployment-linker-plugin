package hudson.plugins.mavendeploymentlinker;

import hudson.Extension;

import javax.annotation.Nonnull;

import org.jenkinsci.plugins.workflow.steps.AbstractStepDescriptorImpl;
import org.jenkinsci.plugins.workflow.steps.AbstractStepImpl;
import org.kohsuke.stapler.DataBoundConstructor;

public class MavenDeploymentLinkerStep extends AbstractStepImpl {

    @DataBoundConstructor
    public MavenDeploymentLinkerStep() {
    }

    @Extension
    public static class DescriptorImpl extends AbstractStepDescriptorImpl {

        public DescriptorImpl() {
            super(MavenDeploymentLinkerStepExecution.class);
        }

        @Override
        public String getFunctionName() {
            return "deploymentLinker";
        }

        @Nonnull
        @Override
        public String getDisplayName() {
            return "Link Maven Deployments";
        }
    }
}
