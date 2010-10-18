package hudson.plugins.mavendeploymentlinker;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.Action;
import hudson.model.BuildListener;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Project;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import hudson.tasks.Recorder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.DataBoundConstructor;

public class MavenDeploymentLinkerRecorder extends Recorder {
    
    private static final String IGNORED_RESOURCES="^.*maven-metadata.xml$";
    
    private String regexp;
    
    @DataBoundConstructor
    public MavenDeploymentLinkerRecorder(final String regexp) {
        this.regexp = StringUtils.trimToEmpty(regexp);
    }

    public String getRegexp() {
        return regexp;
    }

    public void setRegexp(String regexp) {
        this.regexp = regexp;
    }

    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.NONE;
    }

    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener) throws InterruptedException, IOException {
        File logFile = build.getLogFile();
        BufferedReader in = new BufferedReader(new FileReader(logFile));
        Pattern pattern = Pattern.compile("^.*?Uploading: (.*?)$");
        Pattern filterPattern = Pattern.compile(StringUtils.isNotBlank(regexp) ? regexp : ".*");
        Pattern ignoredPattern = Pattern.compile(IGNORED_RESOURCES);
        String line;
        List<String> matches = new ArrayList<String>();
        while ((line = in.readLine()) != null) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.matches()) {
                String candidate = matcher.group(1);
                Matcher filterMatcher = filterPattern.matcher(candidate);
                Matcher ignoredMatcher = ignoredPattern.matcher(candidate);
                if (filterMatcher.matches() && !ignoredMatcher.matches()) {
                    matches.add(candidate);
                }
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
        return true;
    }
    
    @Override
    public Action getProjectAction(AbstractProject<?, ?> project) {
        return new MavenDeploymentProjectLinkerAction(project);
    }

    @Extension
    public static class DescriptorImpl extends BuildStepDescriptor<Publisher> {

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            return true;
        }

        @Override
        public String getDisplayName() {
            return Messages.MavenDeploymentLinkerRecorder_DisplayName();
        }
    }

}
