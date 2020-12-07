package br.com.mashup;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.mashup.models.GitHubProject;
import br.com.mashup.models.TwitterMention;
import br.com.mashup.services.GitHubService;
import br.com.mashup.services.MashupService;
import br.com.mashup.services.TwitterService;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = { MashupService.class })
@TestPropertySource("/test-application.properties")
class MashupServiceTests {

	private static final String QUERY_PARAM_SUCCESS = "Internet of Things";
	private static final String QUERY_PARAM_GITHUB_FAIL = "Empty from GitHub";
	private static final String QUERY_PARAM_TWITTER_FAIL = "Empty from Twitter";
	private static final String EXPECTED_CONTENT_PROJECT = "project/IoT";
	private static final String EXPECTED_CONTENT_TWEET = "project/IoT is amazing!";

	@Autowired
	private MashupService mashupService;

	@MockBean
	private GitHubService gitHubService;

	@MockBean
	private TwitterService twitterService;
	
	private GitHubProject gitHubProject;
	private List<GitHubProject> projects;
	private TwitterMention twitterMention;
	private List<TwitterMention> twitterMentions;

	@Test
	void shouldReturnValidJson() {

		gitHubProject = new GitHubProject();
		projects = new ArrayList<>();
		gitHubProject.setName("project/IoT");
		projects.add(gitHubProject);

		twitterMention = new TwitterMention();
		twitterMentions = new ArrayList<>();
		twitterMention.setId(123L);
		twitterMention.setText("project/IoT is amazing!");
		twitterMentions.add(twitterMention);

		Mockito.when(gitHubService.search(QUERY_PARAM_SUCCESS)).thenReturn(projects);
		Mockito.when(twitterService.search(gitHubProject.getName())).thenReturn(twitterMentions);

		String json = mashupService.consolidate(QUERY_PARAM_SUCCESS);
		Assertions.assertNotNull(json);
		Assertions.assertEquals(true, json.contains(EXPECTED_CONTENT_PROJECT));
		Assertions.assertEquals(true, json.contains(EXPECTED_CONTENT_TWEET));
	}

	@Test
	void shouldReturnNullJsonFromGitHub() {

		Mockito.when(gitHubService.search(QUERY_PARAM_GITHUB_FAIL)).thenReturn(null);

		String json = mashupService.consolidate(QUERY_PARAM_GITHUB_FAIL);
		Assertions.assertNull(json);
	}

	@Test
	void shouldReturnNullJsonFromTwitter() {

		gitHubProject = new GitHubProject();
		projects = new ArrayList<>();
		gitHubProject.setName("Twitter not found");
		projects.add(gitHubProject);

		Mockito.when(gitHubService.search(QUERY_PARAM_TWITTER_FAIL)).thenReturn(projects);
		Mockito.when(twitterService.search(gitHubProject.getName())).thenReturn(null);

		String json = mashupService.consolidate(QUERY_PARAM_TWITTER_FAIL);
		Assertions.assertNull(json);
	}
}
