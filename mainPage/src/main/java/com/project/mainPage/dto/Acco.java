package com.project.mainPage.dto;

import lombok.Data;

/*
 desc accommodate;
+-----------+--------------+------+-----+---------+-------+
| Field     | Type         | Null | Key | Default | Extra |
+-----------+--------------+------+-----+---------+-------+
| acco_rank | int          | NO   | PRI | NULL    |       |
| tourist   | varchar(255) | NO   |     | NULL    |       |
| province  | varchar(255) | NO   |     | NULL    |       |
| city      | varchar(255) | NO   |     | NULL    |       |
| address1  | varchar(255) | NO   |     | NULL    |       |
| acco      | varchar(255) | NO   |     | NULL    |       |
| category  | varchar(255) | NO   |     | NULL    |       |
| search    | int          | NO   |     | NULL    |       |
+-----------+--------------+------+-----+---------+-------+
 * */
@Data
public class Acco {
	 private int acco_rank;
	 private String tourist;
	 private String province;
	 private String city;
	 private String address1;
	 private String acco;
	 private String category;
	 //private int good;
	 //private int bad;
	 //private int views;
	 private int search;
}
