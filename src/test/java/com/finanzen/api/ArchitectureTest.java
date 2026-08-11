package com.finanzen.api;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@AnalyzeClasses(packages = "com.finanzen.api")
public class ArchitectureTest {

    // Dominio isolado
    @ArchTest
    static final ArchRule domain_must_be_pure = noClasses()
            .that().resideInAPackage("..domain..")
            .should().dependOnClassesThat()
            .resideInAnyPackage("..application..", "..adapters..", "org.springframework..", "jakarta.persistence..");

    // Application independente dos adapters
    @ArchTest
    static final ArchRule application_must_not_depend_on_adapters = noClasses()
            .that().resideInAPackage("..application..")
            .should().dependOnClassesThat()
            .resideInAnyPackage("..adapters..", "org.springframework..");

    @ArchTest
    static final ArchRule inbound_adapters_must_not_access_outbound_adapters = noClasses()
            .that().resideInAPackage("..adapters.in..")
            .should().dependOnClassesThat()
            .resideInAnyPackage("..adapters.out..");
}
