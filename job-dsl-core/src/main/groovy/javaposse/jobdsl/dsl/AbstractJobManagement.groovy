package javaposse.jobdsl.dsl

/**
 * Abstract base class providing common functionality for all {@link JobManagement} implementations.
 */
abstract class AbstractJobManagement implements JobManagement {
    final PrintStream outputStream

    protected AbstractJobManagement(PrintStream out) {
        this.outputStream = out
    }

    @Override
    boolean createOrUpdateConfig(String path, String config, boolean ignoreExisting) {
         Item item = new Item(this) {
            @Override
            String getName() {
                path
            }

            @Override
            String getXml() {
                config
            }

            @Override
            Node getNode() {
                throw new UnsupportedOperationException()
            }
        }
        createOrUpdateConfig(item, ignoreExisting)
    }

    @Override
    void logDeprecationWarning() {
        List<StackTraceElement> currentStackTrack = DslScriptHelper.stackTrace
        String details = DslScriptHelper.getSourceDetails(currentStackTrack)
        logDeprecationWarning(currentStackTrack[0].methodName, details)
    }

    @Override
    void logDeprecationWarning(String subject) {
        logDeprecationWarning(subject, DslScriptHelper.sourceDetails)
    }

    @Override
    void logDeprecationWarning(String subject, String scriptName, int lineNumber) {
        logDeprecationWarning(subject, DslScriptHelper.getSourceDetails(scriptName, lineNumber))
    }

    protected void logDeprecationWarning(String subject, String details) {
        logWarning('%s is deprecated (%s)', subject, details)
    }

    protected static void validateUpdateArgs(String jobName, String config) {
        validateNameArg(jobName)
        validateConfigArg(config)
    }

    protected static void validateConfigArg(String config) {
        if (config == null || config.empty) {
            throw new ConfigurationMissingException()
        }
    }

    protected static void validateNameArg(String name) {
        if (name == null || name.empty) {
            throw new NameNotProvidedException()
        }
    }

    @Deprecated
    protected static List<StackTraceElement> getStackTrace() {
        DslScriptHelper.stackTrace
    }

    @Deprecated
    protected static String getSourceDetails(List<StackTraceElement> stackTrace) {
        DslScriptHelper.getSourceDetails(stackTrace)
    }

    @Deprecated
    protected static String getSourceDetails(String scriptName, int lineNumber) {
        DslScriptHelper.getSourceDetails(scriptName, lineNumber)
    }

    protected void logWarning(String message, Object... args) {
        outputStream.printf("Warning: $message\n", args)
    }
}
