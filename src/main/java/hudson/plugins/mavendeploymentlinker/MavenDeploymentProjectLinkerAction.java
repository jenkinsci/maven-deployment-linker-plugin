package hudson.plugins.mavendeploymentlinker;

import hudson.model.Action;
import hudson.model.Project;
import hudson.model.Run;

import org.kohsuke.stapler.export.Exported;

public class MavenDeploymentProjectLinkerAction implements Action {
    private final Project project;

    public MavenDeploymentProjectLinkerAction(Project project) {
        this.project = project;
    }

    public String getIconFileName() {
        return null;
    }

    public String getDisplayName() {
        return null;
    }

    public String getUrlName() {
        return "";
    }
    
    @Exported
    public boolean hasLatestDeployments() {
        return getLatestDeployments() != null;
    }
    
    @Exported
    public Action getLatestDeployments() {
        Run lastSuccessfulBuild = project.getLastSuccessfulBuild();
        if (lastSuccessfulBuild == null) {
            return null;
        }
        return lastSuccessfulBuild.getAction(MavenDeploymentLinkerAction.class);
    }

}
