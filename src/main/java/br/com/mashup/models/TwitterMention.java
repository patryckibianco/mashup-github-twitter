package br.com.mashup.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TwitterMention {

	@JsonProperty(value = "id")
	private Long id;

	@JsonProperty(value = "text")
	private String text;

}
