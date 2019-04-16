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
	@RequestMapping(value = "/quiz/questionsId/{q_id}", method = RequestMethod.GET)
	public ResponseEntity <QuizMetadata> getQuestions1 (@PathVariable Long q_id){ 
			
		
		QuizMetadata qm = new QuizMetadata();
		Connection c = null;
		try {
			c = DBConnection.getDBConnection();
			c.setAutoCommit(false);
			String sql = "{? = call retrieve_questions4()}";
			CallableStatement proc = c.prepareCall(sql);
			proc.registerOutParameter(1, Types.OTHER);
			proc.execute();
			ResultSet res = (ResultSet) proc.getObject(1);
			
	
			Boolean meta_found = false;
			Long p_questionNumber = 0L;
			ArrayList<Questions> aq = new ArrayList<Questions>();
			ArrayList<Answers> an = new ArrayList<Answers>();
			Questions qn = new Questions();
			
			Boolean firstTime = true; 
			Integer i = 0;
			
			while (res.next()) {
			i = i + 1;
			System.out.println("Coming to the value");
			
			if (!meta_found) {
				meta_found = true;
				qm.setTitle(res.getString("title"));
				qm.setDescription(res.getString("description"));
				qm.setQuiz_type(res.getString("quiz_type"));
				qm.setStart_date(res.getString("start_date"));
				qm.setEnd_date(res.getString("end_date"));
				qm.setDuration(res.getLong("duration"));
				qm.setAtmpts_allowed(res.getLong("atmpts_all"));
				qm.setRandomize(res.getBoolean("randomize"));
				
			}
				Long c_questionNumber = res.getLong("question_number");
				qn = new Questions();
				
				if (c_questionNumber != p_questionNumber) {
					
					
					qn.setQuestion_number(res.getLong("question_number"));
					qn.setAnswer_type(res.getString("answer_type"));
					qn.setQuestion_expl(res.getString("question_expl"));
					
					if (!firstTime) {
						qn.setAnswers(an);
						aq.add(qn);
						qm.setQuestions(aq);
						firstTime = false;
					}
					p_questionNumber = c_questionNumber;
				
			}
				Answers answr = new Answers();	
				answr.setAnswer(res.getString("answer"));
				answr.setIs_right_answer(res.getBoolean("is_right_answer"));
				an.add(answr);
		   }
			
			qn.setAnswers(an);
			aq.add(qn);
			qm.setQuestions(aq);
			
			proc.close();
			c.close();

			return new ResponseEntity <QuizMetadata>(qm, HttpStatus.OK);
		}catch (Exception e) {
				throw new GenericException("Getting standard failed:" + e.getMessage());
		}
	}


	@CrossOrigin()
	@RequestMapping(value = "/quiz/questionId/{q_id}", method = RequestMethod.GET)
//	public ResponseEntity <List<QuizQuestions>> getQuestions(@PathVariable Long q_id) {
	public ResponseEntity <QuizMetadata> getQuestions (@PathVariable Long q_id){ 
			
		ArrayList<QuizQuestions> quizQuestions = new ArrayList<QuizQuestions>();
		QuizMetadata qm = new QuizMetadata();
		
		Connection c = null;
		
		try {
			c = DBConnection.getDBConnection();
			c.setAutoCommit(false);
			String sql = "{? = call retrieve_questions4()}";
			CallableStatement proc = c.prepareCall(sql);
			//proc.setLong(2, q_id);
			proc.registerOutParameter(1, Types.OTHER);
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
	
	
	
	@GetMapping("/helloWorld")
	public String helloWorld() { 
		
		String outPut = this.loadQuestions();
		System.out.println(outPut);
		System.out.println("Hello World");
		return "Hello World";
		
	}
	
	@SuppressWarnings("deprecation")
	public String loadQuestions()   {
		    System.out.println("Coming to load quesstion");
		    Long t_seq_num = null;
		    String q_sub_topic = null;
		    String q_question = null;
		    String q_answer = null;
		    Boolean q_right_answer = null;
		    
		    ArrayList<QuizQuestions> qn = new ArrayList<QuizQuestions>();
		    
			String fileLocation = "C:\\Users\\udhaya\\Desktop\\Neet_Questionnaire.xlsx";
			
			try {
				FileInputStream file = new FileInputStream(new File(fileLocation));
				Workbook workbook = new XSSFWorkbook(file);
				Sheet sheet = workbook.getSheetAt(0); 
				Iterator<Row> rowIterator = sheet.iterator(); 
				while (rowIterator.hasNext()) { 
					
					Row row = rowIterator.next(); 
				    Iterator<Cell> cellIterator = row.cellIterator(); 
				
				    while (cellIterator.hasNext()) { 
	                    Cell cell = cellIterator.next(); 
	                    System.out.println("Reading the Cell value");
	                    if (cell.getColumnIndex() == 0) {
	                    	t_seq_num = (long)cell.getNumericCellValue();

	                    }
	                    if (cell.getColumnIndex() == 1) {
	                    	String b = (cell.getStringCellValue().toLowerCase());
	                    	q_sub_topic = b.substring(0,1).toUpperCase()+ b.substring(1);
	                    	
	                    }
	                
	                    if (cell.getColumnIndex() == 2 ) {
	                    	q_question = null;
	                    	String c = (cell.getStringCellValue().toLowerCase());
	                    	q_question = c.substring(0,1).toUpperCase()+ c.substring(1);
	                    }
	                   
	                    if (cell.getColumnIndex() == 3 || cell.getColumnIndex() == 5 || cell.getColumnIndex() == 7 || cell.getColumnIndex() == 9 ) {
	                    	q_answer = null;
	                    	String d;
	                    	if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
	                    	    d = NumberToTextConverter.toText(cell.getNumericCellValue());
	                    	}else {
	                    		d =(cell.getStringCellValue());
	                    	}
	                    	q_answer = d.substring(0,1).toUpperCase()+ d.substring(1);
	    
	                    }
	                    if (cell.getColumnIndex() == 4 || cell.getColumnIndex() == 6 || cell.getColumnIndex() == 8 || cell.getColumnIndex() == 10) {
	                    	q_right_answer = null;
	                    	String e = (cell.getStringCellValue());
	                    	if (e == "true") {
	                    		q_right_answer = true; 
	                    	}else {
	                    		q_right_answer = false;
	                    	}
	                    
	                    }
	                    
	                    /* Call only when the Boolean Column is read */
	                    if (cell.getColumnIndex() == 4 || cell.getColumnIndex() == 6 || cell.getColumnIndex() == 8 || cell.getColumnIndex() == 10) {	
	                    	System.out.println(" value of questions " + q_question);
	                    	QuizQuestions q = new QuizQuestions();
	                    	q.setAnswer(q_answer);
	                    	q.setAnswer_type("Multi Choice");
	                    	q.setAtmpts_allowed((long) 9999);
	                    	q.setDescription("Neet practice to cover Physics");
	                    	q.setEnd_date("2019-12-31");
	                    	q.setStart_date("2019-01-01");
	                    	q.setIs_right_answer(q_right_answer);
	                    	q.setQuestion_number(t_seq_num);
	                    	q.setQuestion_expl(q_question);
	                    	q.setQuiz_type("Open");
	                    	q.setRandomize(true);
	                    	q.setStart_time("0800");
	                    	q.setSubject("Physics");
	                    	q.setTitle("Neet Practice Test");
	                    	q.setSubject_topic(q_sub_topic);
	                    	q.setDuration_mins((long) 60);
	                    	qn.add(q);
	                    	
	                    } 	
	                    	
				    }
				} 
				workbook.close();
				file.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.out.println("the value of Length of newQUizset" + qn.size());	  
			this.newQuizset(qn);
		
		return "Loaded";
		
		
		
	}
			
	public void newQuizset(ArrayList<QuizQuestions> questions)  {
		
		System.out.println("the value of Length of newQUizset" + questions.size());
		ArrayList<String> qn = new ArrayList<String>();
		ArrayList<String> answr = new ArrayList<String>();
		ArrayList<Boolean> isRightanswr = new ArrayList<Boolean>();
		ArrayList<String> subjectTopic = new ArrayList<String>();
		ArrayList<Long> questionNumber = new ArrayList<Long>();
		
		
		questions.forEach(question -> {
			
			qn.add(question.getQuestion_expl());
			answr.add(question.getAnswer());
			isRightanswr.add(question.getIs_right_answer());
			subjectTopic.add(question.getSubject_topic());
			questionNumber.add(question.getQuestion_number());			
			System.out.println("value of question number" + question.getQuestion_number() );
			
		});
			Connection c = null;
			try {
			
			   c = DBConnection.getDBConnection();
		
			String sql = "select from insert_questions (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement pstmt = c.prepareStatement(sql);
			Array q = c.createArrayOf("TEXT", qn.toArray());
			Array a = c.createArrayOf("TEXT", answr.toArray());
			Array is = c.createArrayOf("BOOLEAN",isRightanswr.toArray());
			Array st = c.createArrayOf("TEXT", subjectTopic.toArray());
			Array qnbr = c.createArrayOf("BIGINT", questionNumber.toArray());
		
			
        	pstmt.setArray(1, q);
        	pstmt.setArray(2, a);
			pstmt.setArray(3, is);
			pstmt.setArray(4, st);
			pstmt.setArray(5, qnbr);
			pstmt.setString(6, questions.get(0).getAnswer_type());
			pstmt.setLong(7, questions.get(0).getAtmpts_allowed());			
			pstmt.setString(8, questions.get(0).getDescription());
			pstmt.setString(9, questions.get(0).getStart_date());
			pstmt.setString(10, questions.get(0).getEnd_date());
			pstmt.setString(11, questions.get(0).getQuiz_type());
			pstmt.setBoolean(12, questions.get(0).getRandomize());
			pstmt.setString(13, questions.get(0).getSubject());
			pstmt.setString(14, questions.get(0).getTitle());
			pstmt.setLong(15, questions.get(0).getDuration_mins());	
			pstmt.execute();
			pstmt.close();
			c.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
}


