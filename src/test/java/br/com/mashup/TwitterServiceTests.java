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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import br.com.mashup.models.TwitterMention;
import br.com.mashup.models.TwitterSearchResult;
import br.com.mashup.services.TwitterService;
import br.com.mashup.utils.HttpUtilities;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = { TwitterService.class })
@TestPropertySource("/test-application.properties")
public class TwitterServiceTests {

	private static final String QUERY_PARAM = "Internet of Things Test";

	@Autowired
	private TwitterService twitterService;

	@MockBean
	private RestTemplate restTemplate;

	@Value("${mashup.twitter.search.uri}")
	private String twitterUri;

	@Value("${mashup.twitter.access.token}")
	private String accessToken;

	@Value("${mashup.twitter.token.type}")
	private String tokenType;

	@Test
	void shouldReturnValidTwitterMentionsList() {

		HttpEntity<String> entityRequest = null;
		TwitterMention twitterMention = new TwitterMention();
		List<TwitterMention> twitterMentions = new ArrayList<>();
		List<TwitterMention> twitterMentionsResult = new ArrayList<>();
		TwitterSearchResult twitterSearchResult = new TwitterSearchResult();
		ResponseEntity<TwitterSearchResult> responseTwitterSearchResult = new ResponseEntity<TwitterSearchResult>(
				twitterSearchResult, HttpStatus.OK);

		twitterMention.setId(123L);
		twitterMention.setText("Twitter Service Test");
		twitterMentions.add(twitterMention);
		twitterSearchResult.setTwitterMentions(twitterMentions);

		HttpUtilities.getInstance().getHeaderParamsInstance().put("Authorization", tokenType + " " + accessToken);
		entityRequest = new HttpEntity<String>(HttpUtilities.getInstance().buildHeadersInstance());

		Mockito.when(restTemplate.exchange(twitterUri + QUERY_PARAM, HttpMethod.GET, entityRequest,
				TwitterSearchResult.class)).thenReturn(responseTwitterSearchResult);

		twitterMentionsResult = twitterService.search(QUERY_PARAM);

		Assertions.assertEquals(true, !twitterMentionsResult.isEmpty());
		Assertions.assertEquals(true, twitterMentionsResult.get(0).getId() == 123L);
		Assertions.assertEquals(true, twitterMentionsResult.get(0).getText().equals("Twitter Service Test"));
	}

	@Test
	void shoudReturnNullFromTwitterService() {

		HttpEntity<String> entityRequest = null;
		HttpUtilities.getInstance().getHeaderParamsInstance().put("Authorization", tokenType + " " + accessToken);
		entityRequest = new HttpEntity<String>(HttpUtilities.getInstance().buildHeadersInstance());

		Mockito.when(restTemplate.exchange(twitterUri + QUERY_PARAM, HttpMethod.GET, entityRequest,
				TwitterSearchResult.class)).thenReturn(null);

		Assertions.assertNull(twitterService.search(QUERY_PARAM));
	}
}
