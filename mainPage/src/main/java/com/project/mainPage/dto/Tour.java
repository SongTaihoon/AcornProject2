package com.project.mainPage.dto;

import lombok.Data;

/*
 * desc tour;
+-----------+--------------+------+-----+---------+-------+
| Field     | Type         | Null | Key | Default | Extra |
+-----------+--------------+------+-----+---------+-------+
| tour_rank | varchar(45)  | NO   | PRI | NULL    |       |
| tourist   | varchar(255) | NO   |     | NULL    |       |
| province  | varchar(255) | NO   |     | NULL    |       |
| city      | varchar(255) | NO   |     | NULL    |       |
| address3  | varchar(255) | NO   |     | NULL    |       |
| tours     | varchar(255) | NO   |     | NULL    |       |
| category  | varchar(255) | NO   |     | NULL    |       |
| search    | int          | NO   |     | NULL    |       |
+-----------+--------------+------+-----+---------+-------+
 * */
@Data
public class Tour {
	 private String tour_rank;
	 private String tourist;
	 private String province;
	 private String city;
	 private String address3;
	 private String tours;
	 private String category;
	 private int search;
}
