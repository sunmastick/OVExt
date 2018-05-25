package com.github.sunmastick.ovext.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

public final class Utils {

	private static final int BUFFER_SIZE = 1024;
	private static final int EOF = -1;

	private Utils() {
	}

	public static boolean pastTo(IEditorPart editorPart, String content) {
		if (editorPart == null || content == null) {
			return false;
		}
		IEditorInput input = editorPart.getEditorInput();
		if (!(input instanceof FileEditorInput)) {
			return false;
		}

		IDocument doc = (((ITextEditor) editorPart).getDocumentProvider()).getDocument(input);
		doc.set(content);
		return true;
	}

	public final static String getFromClipboard() {
		Clipboard cb = new Clipboard(Display.getDefault());
		TextTransfer textTransfer = TextTransfer.getInstance();
		String content = (String) cb.getContents(textTransfer);
		cb.dispose();
		return content;
	}

	public final static String getContent(IEditorPart editorPart) {
		if (editorPart == null) {
			return null;
		}
		IEditorInput input = editorPart.getEditorInput();
		if (!(input instanceof FileEditorInput)) {
			return null;
		}

		IFile file = ((FileEditorInput) input).getFile();
		InputStream contentStream;
		try {
			contentStream = file.getContents();
		} catch (CoreException e) {
			contentStream = null;
		}

		if (contentStream == null) {
			return null;
		}

		String content = null;
		try (BufferedInputStream bis = new BufferedInputStream(contentStream, BUFFER_SIZE)) {
			ByteArrayOutputStream outArr = new ByteArrayOutputStream();
			int numRead;
			byte[] buffer = new byte[BUFFER_SIZE];
			while ((numRead = bis.read(buffer)) != EOF) {
				outArr.write(buffer, 0, numRead);
			}
			content = new String(outArr.toByteArray(), StandardCharsets.UTF_8);
			outArr.close();
			outArr = null;
		} catch (IOException ignored) {
		}
		return content;
	}

	public final static void copyToClipboard(String content) {
		if (content == null) {
			return;
		}
		Clipboard cb = new Clipboard(Display.getDefault());
		TextTransfer textTransfer = TextTransfer.getInstance();
		cb.setContents(new Object[] { content }, new Transfer[] { textTransfer });
		cb.dispose();
	}

	public final static String getCurrentRowOrSelectedText(IWorkbenchWindow win) {
		IWorkbenchPage page = win.getActivePage();
		IEditorPart editorPart = page.getActiveEditor();
		ITextEditor textEditor = (editorPart instanceof ITextEditor) ? (ITextEditor) editorPart : null;
		ISelection sel = textEditor != null ? textEditor.getSelectionProvider().getSelection() : null;
		ITextSelection selection = sel != null && (sel instanceof ITextSelection) ? (ITextSelection) sel : null;
		IDocumentProvider docProvider = textEditor != null ? textEditor.getDocumentProvider() : null;
		IDocument doc = textEditor != null ? docProvider.getDocument(textEditor.getEditorInput()) : null;

		String text = null;
		if (selection != null && selection.getLength() > 0) {
			text = selection.getText();
		} else if (selection != null && doc != null) {
			text = getCurrentRow(doc, selection);
		}
		return text;
	}

	private final static String getCurrentRow(IDocument doc, ITextSelection selection) {
		IRegion line;
		try {
			line = doc.getLineInformation(selection.getStartLine());
		} catch (BadLocationException ignore) {
			line = null;
		}

		String text = null;
		if (line != null) {
			try {
				text = doc.get(line.getOffset(), line.getLength());
			} catch (BadLocationException e) {
				text = null;
			}
		}
		return text;
	}

}
