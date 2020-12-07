package br.com.mashup.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import br.com.mashup.models.TwitterMention;
import br.com.mashup.models.TwitterSearchResult;
import br.com.mashup.utils.HttpUtilities;

@Service
public class TwitterService {

	private static final Logger log = (Logger) LoggerFactory.getLogger(TwitterService.class);

	@Autowired
	private RestTemplate restTemplate;

	@Value("${mashup.twitter.search.uri}")
	private String twitterUri;

	@Value("${mashup.twitter.access.token}")
	private String accessToken;

	@Value("${mashup.twitter.token.type}")
	private String tokenType;

	public List<TwitterMention> search(String queryParam) {

		HttpEntity<String> entityRequest = null;
		ResponseEntity<TwitterSearchResult> twitterSearchResult = null;
		List<TwitterMention> twitterMentions = null;

		try {
			HttpUtilities.getInstance().getHeaderParamsInstance().put("Authorization", tokenType + " " + accessToken);

			entityRequest = new HttpEntity<String>(HttpUtilities.getInstance().buildHeadersInstance());

			twitterSearchResult = restTemplate.exchange(twitterUri + queryParam, HttpMethod.GET, entityRequest,
					TwitterSearchResult.class);

			if (twitterSearchResult != null && twitterSearchResult.getBody() != null)
				twitterMentions = twitterSearchResult.getBody().getTwitterMentions();

		} catch (ResourceAccessException | HttpClientErrorException e) {
			log.error("Twitter Service Error", e);
		}

		return twitterMentions;
	}
}
