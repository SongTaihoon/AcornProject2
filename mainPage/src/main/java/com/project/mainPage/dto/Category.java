package com.project.mainPage.dto;
import java.util.List;

import lombok.Data;
/*
 desc category;
+---------------+-------------+------+-----+---------+-------+
| Field         | Type        | Null | Key | Default | Extra |
+---------------+-------------+------+-----+---------+-------+
| category_id   | varchar(45) | NO   | PRI | NULL    |       |
| category_name | varchar(45) | NO   |     | NULL    |       |
+---------------+-------------+------+-----+---------+-------+  
*/
@Data
public class Category {
	private String category_id;
	private String category_name;
	private List<Product> products; 
	private List<ProductImg> productImgs;
}
