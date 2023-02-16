package de.nsctool.api.choreography

import io.micrometer.core.instrument.Metrics
import io.mockk.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class WorkflowManagerTest {
    private val meterRegistry = Metrics.globalRegistry
    private val data = "data"

    @Test
    fun shouldRun() {
        val step = mockk<WorkflowStep<Any>>()
        val manager = object: WorkflowManager<Any>(meterRegistry, listOf(step)) {}
        justRun { step.process(any()) }
        every { step.status() } returns object: WorkflowStatus {}

        manager.run(data)

        verify { step.process(data) }
    }

    @Test
    fun shouldRunMultiple() {
        val step1 = mockk<WorkflowStep<Any>>()
        every { step1.process(any()) } returns "data2"
        every { step1.status() } returns object: WorkflowStatus {}
        val step2 = mockk<WorkflowStep<Any>>()
        every { step2.process(any()) } returns "data3"
        every { step2.status() } returns object: WorkflowStatus {}
        val manager = object: WorkflowManager<Any>(meterRegistry, listOf(step1, step2)) {}

        manager.run(data)

        verify { step1.process(data) }
        verify { step2.process("data2") }
    }

    @Test
    fun shouldRunAndRevert() {
        val step1 = mockk<WorkflowStep<Any>>()
        every { step1.process(any()) } returns "data2"
        every { step1.status() } returns object: WorkflowStatus {}
        justRun { step1.revert(any()) }
        val step2 = mockk<WorkflowStep<Any>>()
        every { step2.process(any()) } throws Exception()
        every { step2.status() } returns object: WorkflowStatus {}
        justRun { step2.revert(any()) }
        val manager = object: WorkflowManager<Any>(meterRegistry, listOf(step1, step2)) {}

        assertThrows<WorkflowRecoverSuccessException> {
            manager.run(data)
        }

        verify { step1.process(data) }
        verify { step2.process("data2") }
        verify { step1.revert("data") }
        verify { step2.revert("data") }
    }

    @Test
    fun shouldRunAndFailRevert() {
        val step1 = mockk<WorkflowStep<Any>>()
        every { step1.process(any()) } returns "data2"
        every { step1.status() } returns object: WorkflowStatus {}
        every { step1.revert(any()) } throws Exception()
        val step2 = mockk<WorkflowStep<Any>>()
        every { step2.process(any()) } throws Exception()
        every { step2.status() } returns object: WorkflowStatus {}
        justRun { step2.revert(any()) }
        val manager = object: WorkflowManager<Any>(meterRegistry, listOf(step1, step2)) {}

        assertThrows<WorkflowRecoverFailedException> {
            manager.run(data)
        }

        verifyOrder {
            step1.process(data)
            step2.process("data2")
            step2.revert("data")
            step1.revert("data")
            step1.revert("data")
            step1.revert("data")
        }
    }
}