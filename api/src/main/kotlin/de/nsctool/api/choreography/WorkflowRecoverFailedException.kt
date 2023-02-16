package de.nsctool.api.choreography

/**
 * Thrown if a workflow failed and wasn't recovered successfully.
 */
class WorkflowRecoverFailedException(
    private val status: WorkflowStatus,
    private val step: WorkflowStep<*>,
    cause: Throwable,
): RuntimeException(cause) {
    override val message: String
    get() = "Failed to recover ${step.javaClass.simpleName} at step '$status': ${cause?.message}"
}