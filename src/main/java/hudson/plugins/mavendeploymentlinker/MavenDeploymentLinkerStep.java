package hudson.plugins.mavendeploymentlinker;

import hudson.Extension;
import hudson.Util;

import javax.annotation.Nonnull;

import org.jenkinsci.plugins.workflow.steps.AbstractStepDescriptorImpl;
import org.jenkinsci.plugins.workflow.steps.AbstractStepImpl;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

/**
 * @author Matthias Balke <matthias.balke@googlemail.com>
 */
public class MavenDeploymentLinkerStep extends AbstractStepImpl {

    private String regexp;

    @DataBoundConstructor
    public MavenDeploymentLinkerStep() {
    }

    public String getRegexp() {
        return regexp;
    }

    @DataBoundSetter
    public void setRegexp(String regexp) {
        this.regexp = Util.fixEmptyAndTrim(regexp);
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
