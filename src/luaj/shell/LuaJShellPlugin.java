
/**
 * (C) 2017 Zigmantas Kryzius <zigmas.kr@gmail.com>
 * class LuaJShellPlugin
 */

package luaj.shell;

//{{{ Imports
import console.Shell;
import org.gjt.sp.jedit.EditPlugin;
//}}}

public class LuaJShellPlugin extends EditPlugin {
	public void start() {}
	public void stop() {
		try {
			LuaJShell shell = (LuaJShell) Shell.getShell("LuaJ");
			shell.stop();
		} catch (Exception e) {}
	}
}

/* :folding=explicit:collapseFolds=1:tabSize=4:indentSize=4:noTabs=false: */
