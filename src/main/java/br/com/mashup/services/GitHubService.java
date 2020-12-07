package br.com.mashup.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import br.com.mashup.models.GitHubProject;
import br.com.mashup.models.GitSearchResult;

@Service
public class GitHubService {

	private static final Logger log = (Logger) LoggerFactory.getLogger(GitHubService.class);

	@Autowired
	private RestTemplate restTemplate;

	@Value("${mashup.github.search.uri}")
	String gitHubUri;

	public List<GitHubProject> search(String queryParam) {

		GitSearchResult gitSearchResult = null;
		List<GitHubProject> gitHubProjects = null;

		try {
			gitSearchResult = restTemplate.getForObject(gitHubUri + queryParam, GitSearchResult.class);

			if (gitSearchResult != null)
				gitHubProjects = gitSearchResult.getProjects();
		} catch (ResourceAccessException | HttpClientErrorException e) {
			log.error("GitHub Service Error", e);
		}

		return gitHubProjects;
	}
}
