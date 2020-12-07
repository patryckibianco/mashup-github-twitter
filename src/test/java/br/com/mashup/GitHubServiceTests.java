package br.com.mashup;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import br.com.mashup.models.GitHubProject;
import br.com.mashup.models.GitSearchResult;
import br.com.mashup.services.GitHubService;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = { GitHubService.class })
@TestPropertySource("/test-application.properties")
public class GitHubServiceTests {

	private static final String QUERY_PARAM = "Internet of Things Test";

	@Autowired
	private GitHubService gitHubService;

	@MockBean
	private RestTemplate restTemplate;

	@Value("${mashup.github.search.uri}")
	private String gitHubUri;

	@Test
	void shouldReturnValidGitHubProjectList() {

		GitHubProject gitHubProject = new GitHubProject();
		List<GitHubProject> gitHubProjects = new ArrayList<>();
		List<GitHubProject> gitHubProjectsResult = new ArrayList<>();
		GitSearchResult gitSearchResult = new GitSearchResult();

		gitHubProject.setName("GitHub Service Test");
		gitHubProjects.add(gitHubProject);
		gitSearchResult.setProjects(gitHubProjects);
		Mockito.when(restTemplate.getForObject(gitHubUri + QUERY_PARAM, GitSearchResult.class))
				.thenReturn(gitSearchResult);

		gitHubProjectsResult = gitHubService.search(QUERY_PARAM);

		Assertions.assertEquals(true, !gitHubProjectsResult.isEmpty());
		Assertions.assertEquals(true, gitHubProjectsResult.get(0).getName().equals("GitHub Service Test"));
	}

	@Test
	void shoudReturnNullFromGitHubService() {
		Mockito.when(restTemplate.getForObject(gitHubUri + QUERY_PARAM, GitSearchResult.class)).thenReturn(null);
		Assertions.assertNull(gitHubService.search(QUERY_PARAM));
	}
}
