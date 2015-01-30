package com.example.testar.model;

public class Message {
	private String content;
	private String author;
	
	public Message(String content, String author) {
		super();
		this.content = content;
		this.author = author;
	}

	public String getContent() {
		return content;
	}

	public String getAuthor() {
		return author;
	}
	
}
