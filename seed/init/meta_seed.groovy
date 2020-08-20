
job('meta-seed') {
    triggers {
        githubPush()
    }
    scm {
        git {
            remote {
                github('spring-cloud/spring-cloud-dataflow-apps-plugin')
            }
            branch('jenkins-build')
        }
    }
    steps {
        gradle("clean build")
        dsl {
            external('projects/*.groovy')
            removeAction('DISABLE')
            removeViewAction('DELETE')
            ignoreExisting(false)
            additionalClasspath([
                    'src/main/groovy', 'src/main/resources', 'build/lib/*.jar'
            ].join("\n"))
        }
    }
}
