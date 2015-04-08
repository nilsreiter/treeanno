package de.ustu.creta.textgrid;

public interface TGText {
	String getText();

	String getTitle();

	String getAuthor();

	String getFrom();

	String getTo();

	String getGenre();

	void setText(String t);

	void setAuthor(String a);

	void setFrom(String f);

	void setTo(String t);

	void setTitle(String t);

	void setGenre(String g);

	void setId(int id);

	int getId();
}
