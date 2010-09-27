package hudson.plugins.mavendeploymentlinker;

import hudson.model.Action;
import org.kohsuke.stapler.export.Exported;

public class MavenDeploymentLinkerAction implements Action {
    private final StringBuilder textBuilder = new StringBuilder();

    public String getIconFileName() {
        return "package.gif";
    }

    public String getDisplayName() {
        return "Maven Deployments";
    }

    public String getUrlName() {
        return "";
    }

    @Exported
    public String getText() {

        return "<ul>" + textBuilder.toString() + "</ul>";
    }

    public void addDeployment(String url) {
        textBuilder.append("\n<li>");
        textBuilder.append("<a href=\"" + url + "\">");
        textBuilder.append(url.substring(url.lastIndexOf('/') + 1, url.length()));
        textBuilder.append("</a>");
        textBuilder.append("</li>\n");
    }

}
