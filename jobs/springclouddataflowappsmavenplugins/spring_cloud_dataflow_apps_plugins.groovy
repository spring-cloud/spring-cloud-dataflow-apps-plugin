package springclouddataflowappsmavenplugins

import org.springframework.jenkins.springclouddataflowappsmavenplugins.ci.SpringCloudDataFlowAppsMavenPluginsBuildMaker
import javaposse.jobdsl.dsl.DslFactory

DslFactory dsl = this

// CI. Toggle second parameter on the call to deploy with true/false to enable/disable GA release
new SpringCloudDataFlowAppsMavenPluginsBuildMaker(dsl, "spring-cloud", "spring-cloud-dataflow-apps-plugin").deploy(false, false)
