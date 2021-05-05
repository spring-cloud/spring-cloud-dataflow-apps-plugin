package org.springframework.jenkins.springclouddataflowappsmavenplugins.ci

import org.springframework.jenkins.common.job.JdkConfig
import org.springframework.jenkins.springclouddataflowappsmavenplugins.ci.common.SpringCloudAppDataFlowAppsMavenPluginsJobs
import javaposse.jobdsl.dsl.DslFactory
import org.springframework.jenkins.common.job.Cron
import org.springframework.jenkins.common.job.Maven
import org.springframework.jenkins.common.job.TestPublisher

/**
 * @author Soby Chacko
 */
class SpringCloudDataFlowAppsMavenPluginsBuildMaker implements JdkConfig, TestPublisher,
        Cron, SpringCloudAppDataFlowAppsMavenPluginsJobs, Maven {

    private final DslFactory dsl
    final String organization
    final String repo

    String branchToBuild = "main"

    Map<String, Object> envVariables = new HashMap<>();

    SpringCloudDataFlowAppsMavenPluginsBuildMaker(DslFactory dsl, String organization, String repo) {
        this.dsl = dsl
        this.organization = organization
        this.repo = repo
    }

    void deploy(boolean checkTests = false, boolean isGaRelease = false) {

        dsl.job("${prefixJob(repo)}-${branchToBuild}-ci") {
            triggers {
                if (!isGaRelease) {
                    githubPush()
                }
            }
            jdk jdk8()
            wrappers {
                colorizeOutput()
                environmentVariables(envVariables)
                credentialsBinding {
                    file('FOO_SEC', "spring-signing-secring.gpg")
                    file('FOO_PUB', "spring-signing-pubring.gpg")
                    string('FOO_PASSPHRASE', "spring-gpg-passphrase")
                    usernamePassword('SONATYPE_USER', 'SONATYPE_PASSWORD', "oss-token")
                }
            }
            scm {
                git {
                    remote {
                        url "https://github.com/${organization}/${repo}"
                        branch branchToBuild
                    }
                }
            }
            steps {
                shell(cleanAndDeploy(isGaRelease))
            }
            if (checkTests) {
                publishers {
                    archiveJunit mavenJUnitResults()
                }
            }
        }
    }
}
