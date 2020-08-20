package springclouddataflowappsmavenplugins

import org.springframework.jenkins.common.view.DashboardViewBuilder
import javaposse.jobdsl.dsl.DslFactory

DslFactory dsl = this

new DashboardViewBuilder(this).buildDashboard()

dsl.listView('Seeds') {
    jobs {
        regex('.*-seed')
    }
    columns defaultColumns()
}

dsl.nestedView('SpringCloudDataFlowAppsMavenPlugins') {
    views {
        listView('CI') {
            jobs {
                regex('spring-cloud-dataflow-apps-plugin.*-ci')
            }
            columns defaultColumns()
        }
        listView('Spring Cloud Data Flow Apps Maven Plugin') {
            jobs {
                regex('spring-cloud-dataflow-apps-plugin.*')
            }
            columns defaultColumns()
        }
    }
}

private Closure defaultColumns() {
    return {
        status()
        name()
        lastSuccess()
        lastFailure()
        lastBuildConsole()
        buildButton()
    }
}