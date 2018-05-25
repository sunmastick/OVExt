package com.github.sunmastick.ovext.handlers.textfile;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;

import com.github.sunmastick.ovext.textfile.TextFile;
import com.github.sunmastick.ovext.utils.Utils;

public final class OpenCopyTextFile extends OpenTextFile {

	@Override
	protected IEditorPart execTextFile(IWorkbenchWindow win, TextFile textFile) {
		IEditorPart editorPart = super.execTextFile(win, textFile);
		String content = Utils.getContent(editorPart);
		Utils.copyToClipboard(content);
		return null;
	}

}
