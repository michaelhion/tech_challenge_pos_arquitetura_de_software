package com.techchallenger.oficina360.arch;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import jakarta.persistence.Entity;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(packages = "com.techchallenger.oficina360")
class ArchitectureTest {

	@ArchTest
	static final ArchRule dominioNaoDeveDependerDeCamadasExternas =
			noClasses()
					.that().resideInAPackage("..dominio..")
					.should().dependOnClassesThat()
					.resideInAnyPackage(
							"..frameworks..",
							"..usecases..",
							"..dtos.."
					);

	@ArchTest
	static final ArchRule useCasesNaoDevemDependerDeFrameworks =
			noClasses()
					.that().resideInAPackage("..usecases..")
					.should().dependOnClassesThat()
					.resideInAnyPackage(
							"..frameworks..",
							"org.springframework..",
							"jakarta.persistence.."
					);

	@ArchTest
	static final ArchRule entidadesJpaDevemFicarNaPersistencia =
			classes()
					.that().areAnnotatedWith(Entity.class)
					.should().resideInAPackage(
							"..frameworks.persistence.entities.."
					);

	@ArchTest
	static final ArchRule useCasesNaoDevemDependerDeCamadasExternas =
			noClasses()
					.that().resideInAPackage("..usecases..")
					.should().dependOnClassesThat()
					.resideInAnyPackage(
							"..frameworks..",
							"..dtos..",
							"org.springframework..",
							"jakarta.persistence.."
					);
}
