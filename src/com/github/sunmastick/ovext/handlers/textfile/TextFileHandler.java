package com.github.sunmastick.ovext.handlers.textfile;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.github.sunmastick.ovext.textfile.TextFile;
import com.github.sunmastick.ovext.textfile.TextFileUtils;
import com.github.sunmastick.ovext.utils.Utils;

public abstract class TextFileHandler extends AbstractHandler {

	protected static final String TEXT_FILE_MSG_TITLE = "TextFile Manager";
	protected static final String PROJECT_RESOURCES_PATH = "src/main/resources/";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow win = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		String text = Utils.getCurrentRowOrSelectedText(win);
		TextFile textFile = TextFileUtils.parseTextFilePath(text);
		execTextFile(win, textFile);
		return null;
	}

	protected abstract IEditorPart execTextFile(IWorkbenchWindow win, TextFile textFile);
	
	protected void showTextFilePathNotFound(IWorkbenchWindow win, String path) {
		MessageDialog.openInformation(win.getShell(), TEXT_FILE_MSG_TITLE,
				"TextFile path [" + (path != null ? path : "") + "] is not found");
	}
}
