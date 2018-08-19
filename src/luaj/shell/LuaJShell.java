
/**
 * (C) 2017 Zigmantas Kryzius <zigmas.kr@gmail.com>
 * class LuaJShellPlugin
 *
 * LuaJShell Plugin files are written following
 * ClojureShell Plugin by Damien Radtke
 */

package luaj.shell;

//{{{ Imports
import classpath.ClasspathPlugin;
import console.Console;
import console.Output;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Hashtable;
import javax.swing.text.AttributeSet;

import java.util.ArrayList;
import java.util.List;

import org.gjt.sp.jedit.Buffer;
import org.gjt.sp.jedit.ServiceManager;
import org.gjt.sp.jedit.View;
import org.gjt.sp.jedit.gui.DockableWindowManager;
import org.gjt.sp.jedit.jEdit;
import org.gjt.sp.jedit.textarea.TextArea;
import org.gjt.sp.util.Log;
import procshell.ProcessShell;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

//}}}
public class LuaJShell extends ProcessShell {

	private String prompt = "";  //

	/*
	 * Constructor for LuaJShell
	 */
	public LuaJShell() {
		super("LuaJ");
	}

	//{{{ init()
	/**
	 * Start up LuaJ
	 */
	protected void init(ConsoleState state, String command) throws IOException {
		String luajJar = jEdit.getProperty("options.luaj.luaj-core-path");

		List<String> cmdLineLuaJ = new ArrayList<String>();
		cmdLineLuaJ.add("java");
		cmdLineLuaJ.add("-classpath");
		cmdLineLuaJ.add(luajJar);
		cmdLineLuaJ.add("lua");

		ProcessBuilder pbLuaJ = new ProcessBuilder(cmdLineLuaJ);
		state.p = pbLuaJ.start();
		Log.log(Log.DEBUG, this, "LuaJ started.");
	}
	//}}}

	//{{{ evalCode()
	/**
	 * Evaluate a code (selection)
	 */
	public void evalCode(Console console, String code) {
		code = stripLuaComments(code);
		code = code.replace('\n', ' ');
		send(console, code);
	} //}}}

	//{{{ evalBuffer()
	/**
	 * Evaluate a buffer
	 */
	public void evalBuffer(Console console, Buffer buffer) {
		String codePath = buffer.getPath();
		String sendString = "dofile[[" + codePath + "]]";
		send(console, sendString);
	} //}}}

	//{{{ evalFile()
	/**
	 * Evaluate a file
	 */
	public void evalFile(Console console, File file) {
		String filePath = file.getPath();
		String sendString = "dofile[[" + filePath + "]]";
		send(console, sendString);
	} //}}}

	protected void onRead(ConsoleState state, String str, Output output) {
		if (str.indexOf("\n") != -1) {
			str = str.substring(str.lastIndexOf("\n") + 1);
		}
		if (str.matches(prompt)) {
			state.waiting = false;
			output.commandDone();
		}
	}

	protected String stripLuaComments(String code) {
		String rgxBlockComment = "\\-\\-\\[\\[.*?\\]\\]";
		String rgxLineComment = "\\-\\-.*?\\n";
		Pattern ptnBlockComment = Pattern.compile(rgxBlockComment, Pattern.DOTALL);
		Pattern ptnLineComment = Pattern.compile(rgxLineComment);
		code = ptnBlockComment.matcher(code).replaceAll(" ");
		code = ptnLineComment.matcher(code).replaceAll(" ");
		return code;
	}

	public void printInfoMessage(Output output) {
		output.print(null, jEdit.getProperty("msg.luajshell.info-message"));
	}

}

/* :folding=explicit:collapseFolds=1:tabSize=4:indentSize=4:noTabs=false: */
