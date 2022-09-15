package de.nsctool.api.fitness

import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.ArchRule
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import org.springframework.stereotype.Controller
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RestController

@AnalyzeClasses(packages = ["de.nsctool.api"], importOptions = [ImportOption.DoNotIncludeTests::class])
class ArchitectureFitness {
    companion object {
        @ArchTest
        val repositoriesShouldOnlyBeAccessedByServices: ArchRule = classes()
            .that()
            .haveSimpleNameContaining("Repository")
            .should()
            .onlyBeAccessed()
            .byClassesThat()
            .haveSimpleNameContaining("Service")

        @ArchTest
        val servicesShouldBeAnnotatedWithSpringsService: ArchRule = classes()
            .that()
            .haveSimpleNameContaining("Service")
            .should()
            .beAnnotatedWith(Service::class.java)

        @ArchTest
        val controllerShouldBeAnnotatedWithRestController: ArchRule = classes()
            .that()
            .haveSimpleNameContaining("Controller")
            .should()
            .beAnnotatedWith(RestController::class.java)
            .orShould()
            .beAnnotatedWith(Controller::class.java)
    }
}