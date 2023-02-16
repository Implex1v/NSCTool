package de.nsctool.api.choreography

/**
 * Thrown if a workflow failed but was recovered successful.
 */
class WorkflowRecoverSuccessException(
    private val status: WorkflowStatus,
    private val step: WorkflowStep<*>,
    cause: Throwable,
): RuntimeException(cause) {
    override val message: String
        get() = "Successfully recovered ${step.javaClass.simpleName} at step '$status': ${cause?.message}"
}