package com.example.model;

import java.util.ArrayList;

public class Questions {

Long q_id;
public Long getQ_id() {
	return q_id;
}
public void setQ_id(Long q_id) {
	this.q_id = q_id;
}
public Long getQuestion_number() {
	return question_number;
}
public void setQuestion_number(Long question_number) {
	this.question_number = question_number;
}
public Long getQ_seq_num() {
	return q_seq_num;
}
public void setQ_seq_num(Long q_seq_num) {
	this.q_seq_num = q_seq_num;
}
public String getAnswer_type() {
	return answer_type;
}
public void setAnswer_type(String answer_type) {
	this.answer_type = answer_type;
}
public String getQuestion_expl() {
	return question_expl;
}
public void setQuestion_expl(String question_expl) {
	this.question_expl = question_expl;
}
public ArrayList<Answers> getAnswers() {
	return answers;
}
public void setAnswers(ArrayList<Answers> answers) {
	this.answers = answers;
}
Long question_number;
Long q_seq_num;
String answer_type;
String question_expl;
ArrayList<Answers> answers;





}