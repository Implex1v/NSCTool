package de.nsctool.api.fitness

import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.ArchRule
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes

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

//        @ArchTest
//        val `LayeredArchitecture should not be violated` = Architectures
//            .layeredArchitecture()
//            .consideringOnlyDependenciesInAnyPackage("de.nsctool.api..")
//            .layer("Controller").
//            .layer("Service").definedBy("..service..")
//            .layer("Persistence").definedBy("..repository..")
//
//            .whereLayer("Controller").mayNotBeAccessedByAnyLayer()
//            .whereLayer("Service").mayOnlyBeAccessedByLayers("Controller")
//            .whereLayer("Persistence").mayOnlyBeAccessedByLayers("Service")
    }
}