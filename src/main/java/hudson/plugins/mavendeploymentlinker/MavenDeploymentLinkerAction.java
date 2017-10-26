package hudson.plugins.mavendeploymentlinker;

import hudson.model.Action;
import hudson.model.Run;
import jenkins.tasks.SimpleBuildStep;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.export.Exported;

public class MavenDeploymentLinkerAction implements Action, SimpleBuildStep.LastBuildAction {

    private List<MavenDeploymentProjectLinkerAction> projectActions;

    public MavenDeploymentLinkerAction(Run<?,?> build) {
        List<MavenDeploymentProjectLinkerAction> projectActions = new ArrayList<>();
        projectActions.add(new MavenDeploymentProjectLinkerAction(build.getParent()));
        this.projectActions = projectActions;
    }

    public static class ArtifactVersion {
        private static final String SNAPSHOT_PATTERN = ".*-SNAPSHOT.*";
        private static final Pattern p = Pattern.compile(SNAPSHOT_PATTERN);

        private ArtifactVersion(String url) {
            this.url = normalize(url);
            snapshot = p.matcher(url).matches();
        }

        private final String url;
        private boolean snapshot;

        private String normalize(String url) {
        // JENKINS-9114 : Remove "dav:" when Maven uses webdav deployment
            return StringUtils.removeStart(url, "dav:");
        }

        public boolean isSnapshot() {
            return snapshot;
        }

        public String getUrl() {
            return url;
        }


        public String getArtifactName() {
            return url.substring(url.lastIndexOf('/') + 1, url.length());
        }

        /**
         * @deprecated use {@link #getUrl} and {@link #getArtifactName()} instead.
         */
        @Deprecated
        public String getText() {
            StringBuilder textBuilder = new StringBuilder();
            textBuilder.append("\n<li>");
            textBuilder.append("<a href=\"" + url + "\">");
            textBuilder.append(url.substring(url.lastIndexOf('/') + 1, url.length()));
            textBuilder.append("</a>");
            textBuilder.append("</li>\n");
            return textBuilder.toString();
        }
    }

    private List<ArtifactVersion> deployments = new ArrayList<ArtifactVersion>();

    private transient String text;

    @Deprecated
    private transient boolean snapshot;

    public boolean isRelease() {
        for (ArtifactVersion artifactVersion : deployments) {
          if (!artifactVersion.isSnapshot()) return true;
        }
        return false;
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

    /**
     * @deprecated use {@link #getDeployments()} instead.
     */
    @Deprecated
    @Exported
    public String getText() {
        if (text == null) {
            StringBuilder textBuilder = new StringBuilder();
            textBuilder.append("<ul>");
            for (ArtifactVersion artifact : deployments) {
                textBuilder.append(artifact.getText());
            }
            textBuilder.append("</ul>");
            text = textBuilder.toString();
        }
        return text;
    }

    public void addDeployment(String url) {
        ArtifactVersion artifactVersion = new ArtifactVersion(url);
        deployments.add(artifactVersion);
    }

    /**
     * @return list of all linked deployments.
     */
    public List<ArtifactVersion> getDeployments() {
        return deployments;
    }

    @Override
    public Collection<? extends Action> getProjectActions() {
        return this.projectActions;
    }
}
