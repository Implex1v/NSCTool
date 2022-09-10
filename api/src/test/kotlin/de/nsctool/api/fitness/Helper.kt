package de.nsctool.api.fitness

import com.tngtech.archunit.core.importer.ClassFileImporter

internal fun allClasses() = ClassFileImporter().importPackages("de.nsctool.api")