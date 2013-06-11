package wpi.edu.tmathmeyer.bannerweb;

import java.io.IOException;
import java.net.MalformedURLException;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;



public class DataReciever {
	public static void main(String[] args) throws FailingHttpStatusCodeException, MalformedURLException, IOException{
		final WebClient webClient = new WebClient();

	    // Get the first page
	    final HtmlPage page1 = webClient.getPage("https://bannerweb.wpi.edu/pls/prod/twbkwbis.P_ValLogin");

	    // Get the form that we are dealing with and within that form, 
	    // find the submit button and the field that we want to change.
	    final HtmlForm form = page1.getFormByName("loginform");

	    final HtmlSubmitInput button = form.getInputByValue("Login");
	    final HtmlTextInput usernameField = form.getInputByName("sid");
	    final HtmlPasswordInput passwordField = form.getInputByName("PIN");

	    // Change the value of the text field
	    usernameField.setValueAttribute("tjmeyer");
	    passwordField.setValueAttribute("in b4 apoc~");

	    // Now submit the form by clicking the button and get back the second page.
	    final HtmlPage page2 = button.click();
	    System.out.println(page2.asText());

	    webClient.closeAllWindows();
	}
	
	public static String convertStreamToString(java.io.InputStream is) {
	    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
	    return s.hasNext() ? s.next() : "";
	}
}
