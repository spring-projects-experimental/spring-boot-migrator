package org.springframework.sbm.engine.precondition;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.Resource;

import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class PreconditionCheckVerifierTest {

	@Test
	void shouldApplyAllPreconditionChecks() {
		PreconditionCheck p1 = mock(PreconditionCheck.class);
		PreconditionCheck p2 = mock(PreconditionCheck.class);
		PreconditionCheck p3 = mock(PreconditionCheck.class);
		List<PreconditionCheck> preconditions = List.of(p1, p2, p3);
		PreconditionVerifier sut = new PreconditionVerifier(preconditions);

		List<Resource> resources = List.of();

		Path projectRoot = Path.of(".");
		when(p1.verify(projectRoot, resources)).thenReturn(new PreconditionCheckResult(PreconditionCheck.ResultState.FAILED, "message 1"));
		when(p2.verify(projectRoot, resources)).thenReturn(new PreconditionCheckResult(PreconditionCheck.ResultState.PASSED, "passed"));
		when(p3.verify(projectRoot, resources)).thenReturn(new PreconditionCheckResult(PreconditionCheck.ResultState.WARN, "message 3"));

		PreconditionVerificationResult preconditionVerificationResult = sut.verifyPreconditions(projectRoot, resources);

		assertThat(preconditionVerificationResult.getResults()).hasSize(3);
		PreconditionCheckResult result1 = preconditionVerificationResult.getResults().get(0);
		assertThat(result1.getState()).isEqualTo(PreconditionCheck.ResultState.FAILED);
		assertThat(result1.getMessage()).isEqualTo("message 1");

		PreconditionCheckResult result2 = preconditionVerificationResult.getResults().get(1);
		assertThat(result2.getState()).isEqualTo(PreconditionCheck.ResultState.PASSED);
		assertThat(result2.getMessage()).isEqualTo("passed");

		PreconditionCheckResult result3 = preconditionVerificationResult.getResults().get(2);
		assertThat(result3.getState()).isEqualTo(PreconditionCheck.ResultState.WARN);
		assertThat(result3.getMessage()).isEqualTo("message 3");

		verify(p1).verify(projectRoot, resources);
		verify(p2).verify(projectRoot, resources);
		verify(p3).verify(projectRoot, resources);
	}

}