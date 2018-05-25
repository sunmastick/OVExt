package com.github.sunmastick.ovext.textfile;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;

public final class TextFileUtils {

	private static final String TEXT_FILE_PATH_PATTERN = "@TextFile\\(\"(.*?)\"\\)";

	private TextFileUtils() {

	}

	public static TextFile parseTextFilePath(String text) {
		Matcher matcher = Pattern.compile(TEXT_FILE_PATH_PATTERN).matcher(text.trim());
		String path = null;
		while (matcher.find()) {
			path = matcher.group(1);
		}

		TextFile textFile = new TextFile();
		textFile.setPath((path != null ? path.trim() : null));
		return textFile;
	}

	public static boolean isExist(IWorkbenchWindow win, TextFile textFile) {
		// TODO Auto-generated method stub
		return false;
	}

	public static IEditorPart openTextFile(IWorkbenchWindow win, TextFile textFile) throws PartInitException {
		IFile file = null;
		return IDE.openEditor(win.getActivePage(), file, true);
	}

	public static void copyText(TextFile textFile, Object clipboard) {
		// TODO Auto-generated method stub
		
	}

	public static void pasteText(TextFile textFile, Object clipboard) {
		// TODO Auto-generated method stub
		
	}
}
