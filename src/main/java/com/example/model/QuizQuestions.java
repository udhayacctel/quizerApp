package com.example.model;

public class QuizQuestions {
	
	
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
	public String getQuiz_type() {
		return quiz_type;
	}
	public void setQuiz_type(String quiz_type) {
		this.quiz_type = quiz_type;
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
	public Long getQuestion_number() {
		return question_number;
	}
	public void setQuestion_number(Long question_number) {
		this.question_number = question_number;
	}
	public String getQuestion_expl() {
		return question_expl;
	}
	public void setQuestion_expl(String question_expl) {
		this.question_expl = question_expl;
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
	public Long getCorrect_answer_count() {
		return correct_answer_count;
	}
	public void setCorrect_answer_count(Long correct_answer_count) {
		this.correct_answer_count = correct_answer_count;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public Boolean getIs_right_answer() {
		return is_right_answer;
	}
	public void setIs_right_answer(Boolean is_right_answer) {
		this.is_right_answer = is_right_answer;
	}
	public Long getQ_id() {
		return q_id;
	}
	public void setQ_id(Long q_id) {
		this.q_id = q_id;
	}
	public Long getUser_id() {
		return user_id;
	}
	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}
	public String getStart_time() {
		return start_time;
	}
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	public Long getScore() {
		return score;
	}
	public void setScore(Long score) {
		this.score = score;
	}
	public Long getAttempt_id() {
		return attempt_id;
	}
	public void setAttempt_id(Long attempt_id) {
		this.attempt_id = attempt_id;
	}
	public Long getAnswer_id() {
		return answer_id;
	}
	public void setAnswer_id(Long answer_id) {
		this.answer_id = answer_id;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getSubject_topic() {
		return subject_topic;
	}
	public void setSubject_topic(String subject_topic) {
		this.subject_topic = subject_topic;
	}
	public Long getDuration_mins() {
		return duration_mins;
	}
	public void setDuration_mins(Long duration_mins) {
		this.duration_mins = duration_mins;
	}

	String question_expl;
	String subject;
	String title;
	String description;
	String quiz_type;
	String start_date;
	String end_date;
	Long atmpts_allowed;
	Boolean randomize;
	Long question_number;
	String subject_topic;
	Long q_seq_num;
	String answer_type;
	Long correct_answer_count;
	String answer;
	Boolean is_right_answer;
	Long q_id;
	Long user_id;
	String start_time;
	String end_time;
	Long score;
	Long attempt_id;
	Long answer_id;
	Long duration_mins;
	
	
}
