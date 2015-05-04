
quartz {
    autoStartup = true
    jdbcStore = false
    waitForJobsToCompleteOnShutdown = true
    exposeSchedulerInRepository = false

    props {
        scheduler.skipUpdateCheck = true
		concurrent=false
    }
}

environments {
    test {
        quartz {
            autoStartup = false
        }
    }
}
