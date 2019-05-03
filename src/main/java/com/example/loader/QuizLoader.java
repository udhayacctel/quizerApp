package com.example.loader;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.commons.DBConnection;
import com.example.model.QuizQuestions;

public class QuizLoader {
	
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
