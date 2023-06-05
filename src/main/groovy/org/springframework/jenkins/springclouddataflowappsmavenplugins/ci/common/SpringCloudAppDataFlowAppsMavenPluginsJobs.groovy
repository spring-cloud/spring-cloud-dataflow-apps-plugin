package org.springframework.jenkins.springclouddataflowappsmavenplugins.ci.common

import org.springframework.jenkins.common.job.BuildAndDeploy

/**
 * @author Soby Chacko
 */
trait SpringCloudAppDataFlowAppsMavenPluginsJobs extends BuildAndDeploy {

    @Override
    String projectSuffix() {
        return 'spring-cloud-dataflow-apps-plugin'
    }

    String cleanAndDeploy(boolean isGaRelease) {
        return isGaRelease ?
                """
                        find . -type d -name "target" -exec rm -rf {} +
                        lines=\$(find . -type f -name pom.xml | xargs egrep "SNAPSHOT|M[0-9]|RC[0-9]" | grep -v http-source-apps | wc -l)
                        if [ \$lines -eq 0 ]; then
                            set +x
                            ./mvnw clean deploy -Dgpg.secretKeyring="\$${gpgSecRing()}" -Dgpg.publicKeyring="\$${
                    gpgPubRing()
                }" -Dgpg.passphrase="\$${gpgPassphrase()}" -DSONATYPE_USER="\$${sonatypeUser()}" -DSONATYPE_PASSWORD="\$${sonatypePassword()}" -Pcentral -U
                            set -x
                        else
                            echo "Non release versions found. Aborting build"
                        fi
                    """ :
                """
                        ./mvnw clean deploy -U
                    """

    }

    String gpgSecRing() {
        return 'FOO_SEC'
    }

    String gpgPubRing() {
        return 'FOO_PUB'
    }

    String gpgPassphrase() {
        return 'FOO_PASSPHRASE'
    }

    String sonatypeUser() {
        return 'SONATYPE_USER'
    }

    String sonatypePassword() {
        return 'SONATYPE_PASSWORD'
    }
    
    String buildUserNameEnvVar() {
        return 'ARTIFACTORY_USERNAME'
    }

    String buildPasswordEnvVar() {
        return 'ARTIFACTORY_PASSWORD'
    }

    String buildCredentialId() {
        return '02bd1690-b54f-4c9f-819d-a77cb7a9822c'
    }
}
