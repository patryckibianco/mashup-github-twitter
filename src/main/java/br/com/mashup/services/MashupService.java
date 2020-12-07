package br.com.mashup.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.mashup.models.GitHubProject;
import br.com.mashup.models.TwitterMention;

@Service
public class MashupService {

	private static final Logger log = (Logger) LoggerFactory.getLogger(MashupService.class);

	@Autowired
	private GitHubService gitHubService;

	@Autowired
	private TwitterService twitterService;
	
	@Value("${mashup.search.query.limit}")
	Integer queryLimit;
	
	public String consolidate(String queryParam) {

		String jsonResult = null;
		List<GitHubProject> gitHubProjects = null;
		List<TwitterMention> twitterMentions = null;
		Integer currentIndex = 0;

		ObjectMapper mapper = new ObjectMapper();
		Map<String, List<TwitterMention>> mashupResult = new HashMap<>();

		try {

			gitHubProjects = gitHubService.search(queryParam);

			if (gitHubProjects != null && !gitHubProjects.isEmpty()) {
				for (GitHubProject project : gitHubProjects) {

					twitterMentions = twitterService.search(project.getName());

					if (twitterMentions != null && !twitterMentions.isEmpty())
						mashupResult.put(project.getName(), twitterMentions);
					
					if(++currentIndex == queryLimit)
						break;
				}
			}

			if (!mashupResult.isEmpty())
				jsonResult = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(mashupResult);

		} catch (JsonProcessingException e) {
			log.error("Mashup Service Error", e);
		}
		return jsonResult;
	}

}
