package de.nsctool.api.choreography

import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Tag
import org.slf4j.LoggerFactory

abstract class WorkflowManager<T>(
    protected val meterRegistry: MeterRegistry,
    private val steps: List<WorkflowStep<T>>,
) {
    private val logger by lazy { LoggerFactory.getLogger(this::class.java) }
    protected open val recoverCount: Int = 3

    fun run(data: T) {
        var update = data
        steps.forEach { step ->
            try {
                update = step.process(update)
                countWorkflowStep(step)
            } catch (ex: Exception) {
                countWorkflowStep(step, false)
                recoverWorkflow(ex, data, step)
                throw WorkflowRecoverSuccessException(step.status(), step, ex)
            }
        }
    }

    private fun recoverWorkflow(ex: Exception, data: T, step: WorkflowStep<T>) {
        (0 until recoverCount + 1).forEach {  n ->
            if(recoverCount == n) {
                throw WorkflowRecoverFailedException(step.status(), step, ex)
            }

            try {
                recover(data, step.status())
                return
            }  catch (ex: Exception) {
                logger.warn("failed to recover workflow '${this.javaClass.simpleName}' at step '${step.javaClass.simpleName}'", ex)
                return@forEach
            }
        }
    }

    private fun recover(data: T, status: WorkflowStatus) {
        steps
            .takeWhileInclusive { it.status() != status }
            .reversed()
            .forEach { step ->
                try {
                    step.revert(data)
                    countWorkflowRecover(step)
                } catch (ex: Exception) {
                    countWorkflowRecover(step, false)
                    throw ex
                }

            }
    }

    private fun countWorkflowStep(step: WorkflowStep<T>, success: Boolean = true) {
        meterRegistry
            .counter(
                "saga_process",
                listOf(
                    Tag.of("name", this.javaClass.simpleName),
                    Tag.of("step", step.javaClass.simpleName),
                    Tag.of("success", if(success) "true" else "false")
                )
            )
            .increment()
    }

    private fun countWorkflowRecover(step: WorkflowStep<T>, success: Boolean = true) {
        meterRegistry
            .counter(
                "saga_recover",
                listOf(
                    Tag.of("name", this.javaClass.simpleName),
                    Tag.of("step", step.javaClass.simpleName),
                    Tag.of("success", if(success) "true" else "false")
                )
            )
            .increment()
    }

    private fun <T> Collection<T>.takeWhileInclusive(pred: (T) -> Boolean): Collection<T> {
        var shouldContinue = true
        return takeWhile {
            val result = shouldContinue
            shouldContinue = pred(it)
            result
        }
    }
}