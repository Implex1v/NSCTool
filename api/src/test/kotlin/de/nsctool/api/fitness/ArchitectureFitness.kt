package de.nsctool.api.fitness

import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import com.tngtech.archunit.library.Architectures

@AnalyzeClasses(packages = ["de.nsctool.api"], importOptions = [ImportOption.DoNotIncludeTests::class])
class ArchitectureFitness {
    companion object {
        @ArchTest
        val `Repositories should only be accessed by Services` = classes()
            .that()
            .resideInAPackage("..repository..")
            .should()
            .onlyBeAccessed()
            .byClassesThat()
            .resideInAPackage("..service..")

        @ArchTest
        val `LayeredArchitecture should not be violated` = Architectures
            .layeredArchitecture()
            .consideringOnlyDependenciesInAnyPackage("de.nsctool.api..")
            .layer("Controller").definedBy("..controller")
            .layer("Service").definedBy("..service..")
            .layer("Persistence").definedBy("..repository..")

            .whereLayer("Controller").mayNotBeAccessedByAnyLayer()
            .whereLayer("Service").mayOnlyBeAccessedByLayers("Controller")
            .whereLayer("Persistence").mayOnlyBeAccessedByLayers("Service")
    }
}