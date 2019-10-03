package Assignment5;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import java.lang.*;
import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;


public class SignInServlet extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		HttpSession session = request.getSession(true);
		session.setAttribute("username", username);
		getOpinions(session);
		response.sendRedirect("LoggedIn.jsp");

		
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		
		/*If posting new opinion then
			get the assertion and justification from the request
			call writeNewOpinion(), which writes the opinion and adds opinion object to session
			redirect to LoggedIn.jsp
		Else if voting
			Get the option they voted for the from the request
			Get the opinion object from the session
			update the opinion object
			add updated opinion object to the session
			redirect to LoggedIn.jsp
		*/
		
		HttpSession session = request.getSession(true);
		String username = (String) session.getAttribute("username");
		String assertion = request.getParameter("assertion");
		String justification = request.getParameter("justification");
		String vote = request.getParameter("vote");
		String opinionNumberStr = request.getParameter("opinionNumber");
		String deleteOpinion = request.getParameter("deleteOpinion");
		String reset = request.getParameter("reset");
		
		if (assertion != null && justification != null) {
			writeNewOpinion(username, assertion, justification);
			getOpinions(session);
			response.sendRedirect("LoggedIn.jsp");
		} else if (vote != null) {
			int opinionNum = Integer.parseInt(opinionNumberStr);
			
			@SuppressWarnings("unchecked")
			ArrayList<OpinionBean> opinions = (ArrayList<OpinionBean>) session.getAttribute("opinions");
			OpinionBean opinion = (OpinionBean) opinions.get(opinionNum);
			
			int agreed = opinion.getAgreed();
			int neutral = opinion.getNeutral();
			int disagreed = opinion.getDisagreed();
			if (vote.equals("Agreed"))
				opinion.setAgreed(agreed + 1);
			if (vote.equals("Neutral"))
				opinion.setNeutral(neutral + 1);
			if (vote.equals("Disagreed"))
				opinion.setDisagreed(disagreed + 1);
			rewriteOpinions(opinions);
			response.sendRedirect("LoggedIn.jsp");
		} else if (deleteOpinion != null) {
			int opinionNum = Integer.parseInt(deleteOpinion);
			deleteOpinion(session, opinionNum);
			response.sendRedirect("LoggedIn.jsp");
		} else if (reset != null) {
			session.invalidate();
			resetSystem();
			System.out.println("The reset button was pressed");
			response.sendRedirect("start.jsp");
		}
			
		
		
	}
	
	private static void rewriteOpinions(ArrayList<OpinionBean> opinions) throws IOException {
		String fileName = "C:\\Program Files\\Apache Software Foundation\\Tomcat 8.5\\webapps\\assignment5\\WEB-INF\\data\\opinions.txt";
		File fileObject = new File(fileName);
		FileOutputStream overwriteStream = new FileOutputStream(fileObject, false);

		String overwrite = "";
		
		for (int i = 0; i < opinions.size(); i++) {
			String username = opinions.get(i).getUsername();
			String assertion = opinions.get(i).getAssertion();
			String justification = opinions.get(i).getJustification();
			int agreed = opinions.get(i).getAgreed();
			int neutral = opinions.get(i).getNeutral();
			int disagreed = opinions.get(i).getDisagreed();
			overwrite += username + ";;" + assertion + ";;" + justification + ";;" + agreed + ";;" + neutral + ";;" + disagreed + "\r\n";
		}
		
		byte[] overwriteData = overwrite.getBytes();
		overwriteStream.write(overwriteData);
		overwriteStream.close();
	}
	
	private static void writeNewOpinion(String username, String assertion, String justification)  {
		FileWriter fw = null;
		PrintWriter pw = null;
	 	String fileName = "C:\\Program Files\\Apache Software Foundation\\Tomcat 8.5\\webapps\\assignment5\\WEB-INF\\data\\opinions.txt";

		try {
			fw = new FileWriter(fileName, true);      
			pw = new PrintWriter(fw);
			pw.println("\n");
			pw.println(username + ";;" + assertion + ";;" + justification + ";;0;;0;;0" + "\r\n");
		} catch (Exception e) {
			System.out.println("ERROR while writing new opinion " + e );
		} finally {
			try {
				pw.flush();     // flush output stream to file
				pw.close();     // close print writer
			} catch (Exception ignored)    { }
			try {
				fw.close();
			} catch (Exception ignored)    { }
		}
		
	}
	
	public static void deleteOpinion(HttpSession session, int opinionNum) throws IOException {
		/*Delete the opinion from the array list of opinions
		Then delete the text file containing the opinions
		Then rewrite the text file to include the remaining opinions
		*/
		
		String fileName = "C:\\Program Files\\Apache Software Foundation\\Tomcat 8.5\\webapps\\assignment5\\WEB-INF\\data\\opinions.txt";
		File fileObject = new File(fileName);
		FileOutputStream overwriteStream = new FileOutputStream(fileObject, false);
		
		@SuppressWarnings("unchecked")
		ArrayList<OpinionBean> opinions = (ArrayList<OpinionBean>) session.getAttribute("opinions");
		opinions.remove(opinionNum);
		
		String overwrite = "";
		
		for (int i = 0; i < opinions.size(); i++) {
			String username = opinions.get(i).getUsername();
			String assertion = opinions.get(i).getAssertion();
			String justification = opinions.get(i).getJustification();
			int agreed = opinions.get(i).getAgreed();
			int neutral = opinions.get(i).getNeutral();
			int disagreed = opinions.get(i).getDisagreed();
			overwrite += username + ";;" + assertion + ";;" + justification + ";;" + agreed + ";;" + neutral + ";;" + disagreed + "\r\n";
		}
		
		byte[] overwriteData = overwrite.getBytes();
		overwriteStream.write(overwriteData);
		overwriteStream.close();
	}
	
	private static void resetSystem() throws IOException {
		String fileName = "C:\\Program Files\\Apache Software Foundation\\Tomcat 8.5\\webapps\\assignment5\\WEB-INF\\data\\opinions.txt";
		File fileObject = new File(fileName);
		FileOutputStream overwriteStream = new FileOutputStream(fileObject, false);
		String overwrite = "";
		byte[] overwriteData = overwrite.getBytes();
		overwriteStream.write(overwriteData);
		overwriteStream.close();
	}
	
	//Read from opinion.txt and store the opinions into the session by using an OpinionBean object
	private void getOpinions(HttpSession session) throws FileNotFoundException{
		String fileName = "C:\\Program Files\\Apache Software Foundation\\Tomcat 8.5\\webapps\\assignment5\\WEB-INF\\data\\opinions.txt";
		Scanner scanner = new Scanner(new FileReader(fileName));
		ArrayList<OpinionBean> opinions = new ArrayList<OpinionBean>();
		while (scanner.hasNext()) {
			OpinionBean opinion = new OpinionBean();
			String[] line = {scanner.nextLine()};
			System.out.println(line[0]);
			if (line[0] == null || line[0].equals("") || line[0].indexOf(';') == -1)
				continue;
			line = line[0].split(";;");
			if (line.length != 6)
				continue;
			String username = line[0];
			String assertion = line[1];
			String justification = line[2];
			int agreed = Integer.parseInt(line[3]);
			int neutral = Integer.parseInt(line[4]);
			int disagreed = Integer.parseInt(line[5]);
			opinion.setUsername(username);
			opinion.setAssertion(assertion);
			opinion.setJustification(justification);
			opinion.setAgreed(agreed);
			opinion.setNeutral(neutral);
			opinion.setDisagreed(disagreed);
			opinions.add(opinion);
		}
		session.setAttribute("opinions", opinions);
		for (int i = 0; i < opinions.size(); i++) {
			OpinionBean test = (OpinionBean) opinions.get(i);
			System.out.println(test.getUsername() + " " + test.getAssertion() + " " + test.getJustification());
		}
	}
	
} 