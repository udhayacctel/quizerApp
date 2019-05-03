package com.example.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.model.QuizQuestions;
import com.example.model.QuizMetadata;
import com.example.model.Answers;
import com.example.model.Questions;
import com.example.model.Questions;
import com.example.commons.DBConnection;
import com.example.exception.GenericException;

@RestController
public class QuizController {

	@CrossOrigin()
	@RequestMapping(value = "/quizzer/questionId", method = RequestMethod.GET)
	public ResponseEntity <QuizMetadata> getQuestions (@RequestParam ("subject") String sub,@RequestParam ("id") Long id) { 
			
		System.out.println("the value of  subject" + sub);
		System.out.println("the value of  id" + id); 
		
		ArrayList<QuizQuestions> quizQuestions = new ArrayList<QuizQuestions>();
		QuizMetadata qm = new QuizMetadata();
		
		Connection c = null;
		
		try {
			c = DBConnection.getDBConnection();
			c.setAutoCommit(false);
			String sql = "{? = call retrieve_questions(?, ?)}";
			CallableStatement proc = c.prepareCall(sql);
			proc.registerOutParameter(1, Types.OTHER);
			proc.setString(2, sub);
			proc.setLong(3, id);
			proc.execute();
			ResultSet res = (ResultSet) proc.getObject(1);
			while (res.next()) {
			System.out.println("Coming to the vslue");
			QuizQuestions q = new QuizQuestions();
			q.setTitle(res.getString("title"));
			q.setDescription(res.getString("description"));
			q.setQuiz_type(res.getString("quiz_type"));
			q.setStart_date(res.getString("start_date"));
			q.setEnd_date(res.getString("end_date"));
			q.setAtmpts_allowed(res.getLong("atmpts_allowed"));
			q.setRandomize(res.getBoolean("randomize"));
			q.setQuestion_number(res.getLong("question_number"));
			q.setQuestion_expl(res.getString("question_expl"));
			q.setSubject(res.getString("subject"));
			q.setQ_seq_num(res.getLong("q_seq_num"));
			q.setAnswer_type(res.getString("Answer_type"));
			q.setAnswer(res.getString("Answer"));
			q.setIs_right_answer(res.getBoolean("is_right_answer"));
			q.setQ_id(res.getLong("q_id"));
			q.setUser_id(res.getLong("user_id"));
			q.setAnswer_id(res.getLong("answer_id"));
			quizQuestions.add(q);
			
			}
			proc.close();
			c.close();
			qm = this.getQuizToPortal(quizQuestions);
			
			return new ResponseEntity<QuizMetadata>(qm, HttpStatus.OK);
	//		return new ResponseEntity<List<QuizQuestions>>(quizQuestions, HttpStatus.OK);
			
	
		}
		catch (Exception e) {
			throw new GenericException("Getting standard failed:" + e.getMessage());
		}
		 
	}

	
	
	public QuizMetadata getQuizToPortal(ArrayList<QuizQuestions> qq) {
		
		QuizMetadata qm = new QuizMetadata();
	
		Integer j = 0;
		Integer i = 0;
		Integer k = 0;
		Long c_q_num = 0l;
		Long p_q_num = 0l;
		Boolean firstFound = false;
		Boolean nextQuestion = false;
		
		
		ArrayList<Questions> qn = new ArrayList<Questions>();
		System.out.println("size of the array" + qq.size());
		for(i=j; i < qq.size(); i++) {
			System.out.println("I am looping value" + i);
			Questions qt = new Questions();
			ArrayList<Answers> an = new ArrayList<Answers>();
	 		firstFound = false;
	 		nextQuestion = false;
	 		
	 		
		 	if(i == 0) {
		 		qm.setTitle(qq.get(i).getTitle());
		 		qm.setDescription(qq.get(i).getDescription());
		 		qm.setQuiz_type(qq.get(i).getQuiz_type());
		 		qm.setStart_date(qq.get(i).getStart_date());
		 		qm.setEnd_date(qq.get(i).getEnd_date());
		 		qm.setAtmpts_allowed(qq.get(i).getAtmpts_allowed());
		 		qm.setRandomize(qq.get(i).getRandomize());
		 	}	

		 	if(i > 0) {
		 	 
		 	    i = i -1;
		 	}

		 		qt.setQuestion_number(qq.get(i).getQuestion_number());
		 		qt.setAnswer_type(qq.get(i).getAnswer_type());
		 		qt.setQuestion_expl(qq.get(i).getQuestion_expl());
		 		qt.setSubject(qq.get(i).getSubject());
		 		
		 		
		 		for(k =i; !nextQuestion; k++) {
		 			c_q_num = qq.get(k).getQuestion_number();  
		 			System.out.println("looping inside the inner loop" + k);

		 			if((c_q_num != p_q_num && firstFound) || (k == qq.size() - 1)) {
		 				p_q_num = c_q_num;
		 				nextQuestion = true;	
		 			}else {
		 				p_q_num = c_q_num;
		 				firstFound = true;
		 			}
		 			
		 			if((!nextQuestion) || k==(qq.size()-1)) {
		 			  
		 				Answers answr = new Answers();
		 				answr.setAnswer(qq.get(k).getAnswer());
		 				answr.setIs_right_answer(qq.get(k).getIs_right_answer());
		 				an.add(answr);
		 			
		 			}		
			 	
		 		} 	 	
		 		qt.setAnswers(an);	
		 		qn.add(qt);
		 		k = k - 1;
		 		i = k;
		 	}
		 			 			 		
					qm.setQuestions(qn);
					System.out.println("Coming before return");
					return qm;		

	}

}


