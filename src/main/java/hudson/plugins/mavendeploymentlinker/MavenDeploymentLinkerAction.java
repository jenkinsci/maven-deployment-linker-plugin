package hudson.plugins.mavendeploymentlinker;

import hudson.model.Action;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class MavenDeploymentLinkerAction implements Action {
  
    public static class ArtifactVersion {
        private static final String SNAPSHOT_PATTERN = ".*-SNAPSHOT.*";
        private static final Pattern p = Pattern.compile(SNAPSHOT_PATTERN);
        
        private ArtifactVersion(String url) {
            this.name = extractName(url);
            this.url = normalize(url);
            this.snapshot = p.matcher(url).matches();
        }
        
        private final String url;
        private boolean snapshot;
        private String name;
        
        private String extractName(String s) {
            return s.substring(s.lastIndexOf('/') + 1, s.length());
        }
        private String normalize(String url) {
            // JENKINS-9114 : Remove "dav:" when Maven uses webdav deployment
            return StringUtils.removeStart(url, "dav:");
        }
        
        public boolean isSnapshot() {
            return snapshot;
        }
        public String getName() {
            return name;
        }
        public String getUrl() {
            return url;
        }
        
        protected Object readResolve() {
            if (name == null) {
                name = extractName(url);
            }
            return this;
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
