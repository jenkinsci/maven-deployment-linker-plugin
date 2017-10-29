package hudson.plugins.mavendeploymentlinker;

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.hamcrest.CoreMatchers;
import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition;
import org.jenkinsci.plugins.workflow.job.WorkflowJob;
import org.jenkinsci.plugins.workflow.job.WorkflowRun;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class MavenDeploymentLinkerStepTest {

    @Rule
    public JenkinsRule j = new JenkinsRule();

    /**
     * Test collecting maven deployment links.
     */
    @Test
    public void deploymentLinkerFoundNoDeployments()
            throws Exception {
        // job setup
        WorkflowJob foo = j.jenkins.createProject(WorkflowJob.class, "foo");
        foo.setDefinition(new CpsFlowDefinition(StringUtils.join(Arrays.asList(
                "node {",
                "  echo 'test'",
                "  deploymentLinker regexp: '.*'",
                "}"), "\n")));

        // get the build going, and wait until workflow pauses
        WorkflowRun b = j.assertBuildStatusSuccess(foo.scheduleBuild2(0).get());

        j.assertLogContains("Recording Maven Deployment Links ...", b);
        j.assertLogContains("No Maven Deployment Links where found.", b);
    }

    /**
     * Test collecting maven deployment links.
     */
    @Test
    public void deploymentLinkerFoundOneDeployment()
            throws Exception {
        // job setup
        String artifactName = "maven-deployment-linker-1.6-20171026.194553-11.pom";
        String url = "http://localhost:8081/nexus/content/repositories/snapshots/org/" +
                     "jvnet/hudson/plugins/maven-deployment-linker/1.6-SNAPSHOT/" + artifactName;

        WorkflowJob foo = j.jenkins.createProject(WorkflowJob.class, "foo");
        foo.setDefinition(new CpsFlowDefinition(StringUtils.join(Arrays.asList(
                "node {",
                "  echo 'test'",
                "  echo '''" + getSampleDeploymentLog() + "'''",
                "  deploymentLinker regexp: '.*'",
                "}"), "\n")));

        // get the build going, and wait until workflow pauses
        WorkflowRun b = j.assertBuildStatusSuccess(foo.scheduleBuild2(0).get());
        j.assertLogContains("Recording Maven Deployment Links ...", b);


        MavenDeploymentLinkerAction deploymentLinkerAction = b.getAction(MavenDeploymentLinkerAction.class);
        String log = j.getLog(b);
        System.out.println(log);


        assertThat(deploymentLinkerAction, notNullValue());
        j.assertLogContains("2 Maven Deployment Links where found.", b);
    }


    private String getSampleDeploymentLog() {
        return "Uploading: http://localhost:8081/nexus/content/repositories/snapshots/org/jvnet/hudson/plugins/maven-deployment-linker/1.6-SNAPSHOT/maven-deployment-linker-1.6-20171026.194553-11.hpi\n" +
               "2/32 KB\n" +
               "4/32 KB\n" +
               "6/32 KB\n" +
               "8/32 KB\n" +
               "10/32 KB\n" +
               "12/32 KB\n" +
               "14/32 KB\n" +
               "16/32 KB\n" +
               "18/32 KB\n" +
               "20/32 KB\n" +
               "22/32 KB\n" +
               "24/32 KB\n" +
               "26/32 KB\n" +
               "28/32 KB\n" +
               "30/32 KB\n" +
               "32/32 KB\n" +
               "\n" +
               "Uploaded: http://localhost:8081/nexus/content/repositories/snapshots/org/jvnet/hudson/plugins/maven-deployment-linker/1.6-SNAPSHOT/maven-deployment-linker-1.6-20171026.194553-11.hpi (32 KB at 702.6 KB/sec)\n" +
               "Uploading: http://localhost:8081/nexus/content/repositories/snapshots/org/jvnet/hudson/plugins/maven-deployment-linker/1.6-SNAPSHOT/maven-deployment-linker-1.6-20171026.194553-11.pom\n" +
               "2/6 KB\n" +
               "4/6 KB\n" +
               "6/6 KB\n" +
               "\n" +
               "Uploaded: http://localhost:8081/nexus/content/repositories/snapshots/org/jvnet/hudson/plugins/maven-deployment-linker/1.6-SNAPSHOT/maven-deployment-linker-1.6-20171026.194553-11.pom (6 KB at 148.0 KB/sec)\n" +
               "Downloading: http://localhost:8081/nexus/content/repositories/snapshots/org/jvnet/hudson/plugins/maven-deployment-linker/maven-metadata.xml\n" +
               "305/305 B\n" +
               "\n" +
               "Downloaded: http://localhost:8081/nexus/content/repositories/snapshots/org/jvnet/hudson/plugins/maven-deployment-linker/maven-metadata.xml (305 B at 10.6 KB/sec)\n" +
               "Uploading: http://localhost:8081/nexus/content/repositories/snapshots/org/jvnet/hudson/plugins/maven-deployment-linker/1.6-SNAPSHOT/maven-metadata.xml\n" +
               "966/966 B\n" +
               "\n" +
               "Uploaded: http://localhost:8081/nexus/content/repositories/snapshots/org/jvnet/hudson/plugins/maven-deployment-linker/1.6-SNAPSHOT/maven-metadata.xml (966 B at 32.5 KB/sec)\n" +
               "Uploading: http://localhost:8081/nexus/content/repositories/snapshots/org/jvnet/hudson/plugins/maven-deployment-linker/maven-metadata.xml\n" +
               "305/305 B\n" +
               "\n" +
               "Uploaded: http://localhost:8081/nexus/content/repositories/snapshots/org/jvnet/hudson/plugins/maven-deployment-linker/maven-metadata.xml (305 B at 9.0 KB/sec)\n";
    }

    //        List<MavenDeploymentLinkerAction> foundActions = new ArrayList<>();
    //        for (Action action : b.getAllActions()) {
    //            if (action instanceof MavenDeploymentLinkerAction) {
    //                foundActions.add((MavenDeploymentLinkerAction)action);
    //            }
    //        }
    //
    //        assertThat("Should have found one MavenDeploymentLinkerAction", foundActions, hasSize(1));
    //
    //        List<MavenDeploymentLinkerAction.ArtifactVersion> deployments = foundActions.get(0).getDeployments();
    //
    //        assertThat("Should have recorded one Deployment", deployments, hasSize(1));
    //        assertThat("Should match the artifact name", deployments.get(0).getArtifactName(), equalTo(artifactName));
    //        assertThat("Should match the artifact url", deployments.get(0).getUrl(), equalTo(url));
}
