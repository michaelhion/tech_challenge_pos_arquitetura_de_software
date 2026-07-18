package com.techchallenger.oficina360.arch;

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
}
