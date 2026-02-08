package com.softwarearchitect.orderplatform.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

class ModuleBoundariesArchTest {

    private static final String BASE = "com.softwarearchitect.orderplatform";
    private static final String MODULES = BASE + ".modules..";
    private static final String SHARED  = BASE + ".shared..";

    private final JavaClasses classes =
            new ClassFileImporter().importPackages(BASE);

    /**
     * Domain rule: domain packages must not depend on other modules' domain packages.
     * Allowed: depend on shared domain types (Money, IDs, DomainException, etc.)
     */
    @Test
    void domain_should_not_depend_on_other_modules_domain() {
        ArchRule rule = noClasses()
                .that().resideInAnyPackage(BASE + ".modules..domain..")
                .should().dependOnClassesThat()
                .resideInAnyPackage(BASE + ".modules..domain..")
                // allow within same module domain (ordering.domain -> ordering.domain etc.)
                // We'll enforce module separation with the rule below:
                .because("Domain must not couple to other modules' domain; use IDs/shared or application ports.");

        // This rule by itself would also flag same-package dependencies,
        // so we apply a better module-slice rule below; keep this method for clarity.
        // We'll do the enforcement via slice rule:
        slices()
                .matching(BASE + ".modules.(*)..")
                .should().notDependOnEachOther()
                .check(classes);
    }

    /**
     * Stronger rule: no module depends on another module (any package), except shared.
     * This prevents accidental imports like ordering -> payments domain, etc.
     */
    @Test
    void modules_should_not_depend_on_each_other_except_shared() {
        // Slice rule prevents module-to-module dependencies.
        // shared is outside modules, so it's allowed by default.
        slices()
                .matching(BASE + ".modules.(*)..")
                .should().notDependOnEachOther()
                .because("Modules must communicate via application ports, not direct imports.")
                .check(classes);
    }

    /**
     * Optional hygiene: no cyclic dependencies inside your codebase.
     */
    @Test
    void no_cycles_in_base_package() {
        slices()
                .matching(BASE + ".(*)..")
                .should().beFreeOfCycles()
                .because("Cyclic dependencies are a maintainability and extraction risk.")
                .check(classes);
    }
}