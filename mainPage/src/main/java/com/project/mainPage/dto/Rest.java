package com.project.mainPage.dto;

import lombok.Data;

/*
 desc restaurant;
+-----------+--------------+------+-----+---------+-------+
| Field     | Type         | Null | Key | Default | Extra |
+-----------+--------------+------+-----+---------+-------+
| rest_rank | int          | NO   | PRI | NULL    |       |
| tourist   | varchar(255) | NO   |     | NULL    |       |
| province  | varchar(255) | NO   |     | NULL    |       |
| city      | varchar(255) | NO   |     | NULL    |       |
| address2  | varchar(255) | NO   |     | NULL    |       |
| rest      | varchar(255) | NO   |     | NULL    |       |
| category  | varchar(255) | NO   |     | NULL    |       |
| search    | int          | NO   |     | NULL    |       |
+-----------+--------------+------+-----+---------+-------+
 * */
@Data
public class Rest {
	 private int rest_rank;
	 private String tourist;
	 private String province;
	 private String city;
	 private String address2;
	 private String tours;
	 private String category;
	 //private int good;
	 //private int bad;
	 //private int views;
	 private int search;
}
