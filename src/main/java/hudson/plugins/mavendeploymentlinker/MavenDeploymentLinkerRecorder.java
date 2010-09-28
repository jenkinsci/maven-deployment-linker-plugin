package hudson.plugins.mavendeploymentlinker;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import hudson.tasks.Recorder;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MavenDeploymentLinkerRecorder extends Recorder {

    @DataBoundConstructor
    public MavenDeploymentLinkerRecorder() {

    }

    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.NONE;
    }

    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener) throws InterruptedException, IOException {
        File logFile = build.getLogFile();
        BufferedReader in = new BufferedReader(new FileReader(logFile));
        Pattern pattern = Pattern.compile("^.*?Uploading: (.*?)$");
        String line;
        List<String> matches = new ArrayList<String>();
        while ((line = in.readLine()) != null) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.matches()) {
                matches.add(matcher.group(1));
            }
        }
        if (matches.size() > 0) {
            MavenDeploymentLinkerAction action = new MavenDeploymentLinkerAction();
            for (String url : matches) {
                action.addDeployment(url);
            }
            build.getActions().add(action);
            build.save();
        }
        return super.perform(build, launcher, listener);
    }

    @Extension
    public static class DescriptorImpl extends BuildStepDescriptor<Publisher> {

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            return true;
        }

        @Override
        public String getDisplayName() {
            return "Link to maven deployments";
        }
    }

}
