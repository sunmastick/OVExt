package com.github.sunmastick.ovext.handlers.textfile;

import java.util.HashMap;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.editors.text.EditorsUI;
import org.eclipse.ui.ide.IDE;

import com.github.sunmastick.ovext.textfile.TextFile;

public class OpenTextFile extends TextFileHandler {

	@Override
	protected IEditorPart execTextFile(IWorkbenchWindow win, TextFile textFile) {
		if (textFile.isEmptyPath()) {
			showTextFilePathNotFound(win, textFile.getPath());
			return null;
		}

		IEditorPart editorPart = win.getActivePage().getActiveEditor();
		IFile file = null;
		if (editorPart != null) {
			IFileEditorInput input = (IFileEditorInput) editorPart.getEditorInput();
			IProject project = input.getFile().getProject();
			file = project.getFile(PROJECT_RESOURCES_PATH + textFile.getPath());
		}
		if (file == null) {
			return null;
		}

		IEditorPart sqlEditorPart = null;
		if (file.exists()) {
			IMarker marker = createDefTextEditorMarker(file);
			if (marker != null) {
				try {
					sqlEditorPart = IDE.openEditor(win.getActivePage(), marker, true);
				} catch (PartInitException e) {
					sqlEditorPart = null;
				}
			} else {
				try {
					sqlEditorPart = IDE.openEditor(win.getActivePage(), file, true);
				} catch (PartInitException e) {
					sqlEditorPart = null;
				}
			}
		} else {
			MessageDialog.openInformation(win.getShell(), TEXT_FILE_MSG_TITLE,
					"File [" + file.getLocation().toString() + "] is not found");
		}
		return sqlEditorPart;
	}

	private IMarker createDefTextEditorMarker(IFile file) {
		HashMap<String, String> map = new HashMap<>();
		map.put(IDE.EDITOR_ID_ATTR, EditorsUI.DEFAULT_TEXT_EDITOR_ID);
		IMarker marker;
		try {
			marker = file.createMarker(IMarker.TEXT);
		} catch (CoreException e1) {
			marker = null;
		}
		if (marker != null) {
			try {
				marker.setAttributes(map);
			} catch (CoreException e) {
			}
		}
		return marker;
	}
}
