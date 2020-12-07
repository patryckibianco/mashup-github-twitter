package br.com.mashup.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TwitterSearchResult {

	@JsonProperty(value = "statuses")
	private List<TwitterMention> twitterMentions;

}
