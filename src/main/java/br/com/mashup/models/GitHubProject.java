package br.com.mashup.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GitHubProject {

	@JsonProperty(value = "full_name")
	String name;
}
