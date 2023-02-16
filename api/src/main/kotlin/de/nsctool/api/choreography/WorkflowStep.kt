package de.nsctool.api.choreography

interface WorkflowStep<T> {
    fun status(): WorkflowStatus

    /**
     * Executes a step of the saga.
     */
    fun process(data: T): T

    /**
     * Reverts the operation done with [process]. The code has to be idempotent.
     */
    fun revert(data: T): T
}
