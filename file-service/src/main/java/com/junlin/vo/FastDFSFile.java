package com.junlin.vo;

import lombok.Data;

@Data
public class FastDFSFile {

  public static final String FILE_DEFAULT_WIDTH 	= "120";
  public static final String FILE_DEFAULT_HEIGHT 	= "120";
  public static final String FILE_DEFAULT_AUTHOR 	= "dada";

  public static final String PROTOCOL = "http://";
  public static final String SEPARATOR = "/";

  private static final long serialVersionUID = -996760121932438618L;

  private String name;
  
  private byte[] content;
  
  private String ext;
  
  private String height = FILE_DEFAULT_HEIGHT;
  
  private String width = FILE_DEFAULT_WIDTH;
  
  private String author = FILE_DEFAULT_AUTHOR;
  
  public FastDFSFile(String name, byte[] content, String ext, String height,
                     String width, String author) {
    super();
    this.name = name;
    this.content = content;
    this.ext = ext;
    this.height = height;
    this.width = width;
    this.author = author;
  }
  
  public FastDFSFile(String name, byte[] content, String ext, String height,
                     String width) {
	    super();
	    this.name = name;
	    this.content = content;
	    this.ext = ext;
	    this.height = height;
	    this.width = width;
	  }
  
  public FastDFSFile(String name, byte[] content, String ext) {
    super();
    this.name = name;
    this.content = content;
    this.ext = ext;
  }

  
}