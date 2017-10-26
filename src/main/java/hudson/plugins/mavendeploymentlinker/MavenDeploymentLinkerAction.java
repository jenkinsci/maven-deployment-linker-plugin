package hudson.plugins.mavendeploymentlinker;

import hudson.model.Action;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public class MavenDeploymentLinkerAction implements Action {
  
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
    }
    
    private List<ArtifactVersion> deployments = new ArrayList<ArtifactVersion>();
    
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

    public void addDeployment(String url) {
        ArtifactVersion artifactVersion = new ArtifactVersion(url);
        deployments.add(artifactVersion);
    }

    /**
     * @return list of all linked deployments
     */
    public List<ArtifactVersion> getDeployments() {
        return deployments;
    }
}
