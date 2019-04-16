package com.example.model;

import java.util.ArrayList;

public class QuizMetadata {
	
Long id;
public Long getId() {
	return id;
}
public void setId(Long id) {
	this.id = id;
}
public String getTitle() {
	return title;
}
public void setTitle(String title) {
	this.title = title;
}
public String getDescription() {
	return description;
}
public void setDescription(String description) {
	this.description = description;
}
public String getStart_date() {
	return start_date;
}
public void setStart_date(String start_date) {
	this.start_date = start_date;
}
public String getEnd_date() {
	return end_date;
}
public void setEnd_date(String end_date) {
	this.end_date = end_date;
}
public Long getDuration() {
	return duration;
}
public void setDuration(Long duration) {
	this.duration = duration;
}
public Long getAtmpts_allowed() {
	return atmpts_allowed;
}
public void setAtmpts_allowed(Long atmpts_allowed) {
	this.atmpts_allowed = atmpts_allowed;
}
public Boolean getRandomize() {
	return randomize;
}
public void setRandomize(Boolean randomize) {
	this.randomize = randomize;
}
public Long getUser_id() {
	return user_id;
}
public void setUser_id(Long user_id) {
	this.user_id = user_id;
}
public ArrayList<Questions> getQuestions() {
	return questions;
}
public void setQuestions(ArrayList<Questions> questions) {
	this.questions = questions;
}
public String getQuiz_type() {
	return quiz_type;
}
public void setQuiz_type(String quiz_type) {
	this.quiz_type = quiz_type;
}

String title;
String description;
String quiz_type;
String start_date;
String end_date;
Long duration;
Long atmpts_allowed;
Boolean randomize;
Long user_id;
ArrayList<Questions> questions;

}
