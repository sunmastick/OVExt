package com.github.sunmastick.ovext.textfile;

public final class TextFile {

	private String path;
	private String text;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean isEmpty() {
		return text == null || text.trim().isEmpty();
	}

	public boolean isNotEmpty() {
		return !isEmpty();
	}

	public boolean isEmptyPath() {
		return path == null || path.trim().isEmpty();
	}

	public boolean isNotEmptyPath() {
		return !isEmptyPath();
	}
}
