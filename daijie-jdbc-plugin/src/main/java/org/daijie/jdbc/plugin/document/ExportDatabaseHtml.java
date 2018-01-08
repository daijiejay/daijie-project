package org.daijie.jdbc.plugin.document;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExportDatabaseHtml {

	public String getHtml(String host, String post, String dbName, String user, String password, String path) throws SQLException{
		String url = "jdbc:mysql://"+host+":"+post+"/"+dbName+"?user="+user+"&password="+password+"&useUnicode=true&characterEncoding=UTF8&serverTimezone=GMT";
		return getHtml(url, path);
	}
	
	public String getHtml(String url, String path) throws SQLException{
		Connection conn = null;
		String sql;
		try {
			String dbName = url.substring(url.lastIndexOf("/")+1, url.indexOf("?"));;
			Class.forName("com.mysql.jdbc.Driver");// 动态加载mysql驱动
			System.out.println("成功加载MySQL驱动程序");
			conn = DriverManager.getConnection(url);
			Statement stmt = conn.createStatement();
			sql = "select c.TABLE_NAME,c.COLUMN_NAME,c.COLUMN_TYPE,c.COLUMN_COMMENT,t.TABLE_COMMENT from "
					+ "information_schema.`COLUMNS` c, information_schema.`TABLES` t where c.TABLE_NAME = "
					+ "t.TABLE_NAME and c.TABLE_SCHEMA = t.TABLE_SCHEMA and c.TABLE_SCHEMA = '"+dbName+"'";
			System.out.println(sql);
			ResultSet rs = stmt.executeQuery(sql);
			List<List<Map<String, String>>> tableList = new ArrayList<List<Map<String, String>>>();
			List<Map<String, String>> columnList = null;
			Map<String, String> columnNameMap = null;
			StringBuffer buffer = new StringBuffer();
			buffer.append("<!DOCTYPE html>                                                                 \n");
			buffer.append("<html>                                                                          \n");
			buffer.append("	<head>                                                                         \n");
			buffer.append("		<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">          \n");
			buffer.append("		<title>数据结构</title>                                                      \n");
			buffer.append("		<style>                                                                      \n");
			buffer.append("			.body{                                                                     \n");
			buffer.append("				width: 95%;                                                          \n");
			buffer.append("				height: 600px;                                                          \n");
			buffer.append("				padding: 10px 20px 10px 20px;                                            \n");
			buffer.append("				border: 2px solid #000;                                                  \n");
			buffer.append("			}                                                                          \n");
			buffer.append("			.body>div{                                                                 \n");
			buffer.append("				float: left;                                                             \n");
			buffer.append("			}                                                                          \n");
			buffer.append("			.table-left{                                                               \n");
			buffer.append("				width: 32%;                                                            \n");
			buffer.append("				height: 100%;                                                            \n");
			buffer.append("				font-size: 14px;                                                         \n");
			buffer.append("				border-right: 2px solid #000;                                            \n");
			buffer.append("				overflow-x :hidden;                                                       \n");
			buffer.append("				overflow-y :auto;                                                       \n");
			buffer.append("			}                                                                          \n");
			buffer.append("			.table-left>input{                                                            \n");
			buffer.append("				height: 20px;                                                            \n");
			buffer.append("				font-size: 16px;                                                            \n");
			buffer.append("				width: 80%;                                                            \n");
			buffer.append("				margin-bottom: 5px;                                                            \n");
			buffer.append("			}                                                            \n");
			buffer.append("			.table-left>ul{                                                            \n");
			buffer.append("				list-style-type: none;                                                   \n");
			buffer.append("				padding-left: 0;                                                         \n");
			buffer.append("				margin-top: 0;                                                         \n");
			buffer.append("			}                                                                          \n");
			buffer.append("			.table-left>ul>li.background{                                                                          \n");
			buffer.append("				background: #888;                                                                          \n");
			buffer.append("			}                                                                          \n");
			buffer.append("			.table-left>ul>li:hover, .table-left>ul>li:focus{                          \n");
			buffer.append("				cursor:pointer;                                                          \n");
			buffer.append("				background-color:#A7C942;                                                \n");
			buffer.append("			}                                                                          \n");
			buffer.append("			.table-right{                                                              \n");
			buffer.append("				width: 66%;                                                           \n");
			buffer.append("				font-size: 14px;                                                         \n");
			buffer.append("				padding: 2px 0 0 10px;                                                   \n");
			buffer.append("				overflow:scroll;                                                   \n");
			buffer.append("				height: 100%;                                                   \n");
			buffer.append("			}                                                                          \n");
			buffer.append("			.table-right>table{                                                        \n");
			buffer.append("				font-family:\"Trebuchet MS\", Arial, Helvetica, sans-serif;                \n");
			buffer.append("				width:100%;                                                              \n");
			buffer.append("  				border-collapse:collapse;                                              \n");
			buffer.append("			}                                                                          \n");
			buffer.append("			.table-right>table th, .table-right>table td{                              \n");
			buffer.append("				font-size:1em;                                                           \n");
			buffer.append("				border:1px solid #98bf21;                                                \n");
			buffer.append("				padding:3px 7px 2px 7px;                                                 \n");
			buffer.append("			}                                                                          \n");
			buffer.append("			.table-right>table th{                                                     \n");
			buffer.append("				font-size:1.1em;                                                         \n");
			buffer.append("				text-align:left;                                                         \n");
			buffer.append("				padding-top:5px;                                                         \n");
			buffer.append("				padding-bottom:4px;                                                      \n");
			buffer.append("				background-color:#A7C942;                                                \n");
			buffer.append("				color:#ffffff;                                                           \n");
			buffer.append("			}                                                                          \n");
			buffer.append("			.table-left>ul>li.show, .table-right>table.open{                                                   \n");
			buffer.append("				display: '';                                                             \n");
			buffer.append("			}                                                                          \n");
			buffer.append("			.table-left>ul>li.hide, .table-right>table.close{                                                  \n");
			buffer.append("				display: none;                                                           \n");
			buffer.append("			}                                                                          \n");
			buffer.append("		</style>                                                                     \n");
			buffer.append("		<script type=\"text/javascript\">                                              \n");
			buffer.append("			window.onload = function(){                                                \n");
			buffer.append("				var menu = document.getElementById(\"table-menu\");                        \n");
			buffer.append("				menu.addEventListener(\"click\",function(event){                           \n");
			buffer.append("					var nodes = document.getElementsByClassName(\"background\");                    \n");
			buffer.append("					for(var i = 0; i < nodes.length; i ++){                                        \n");
			buffer.append("						nodes[i].className = nodes[i].className.replace(\"background\",\"\");        \n");
			buffer.append("					}                                                                        \n");
			buffer.append("					event.target.className = \"background\";                                   \n");
			buffer.append("					var open = document.getElementsByClassName(\"open\");                    \n");
			buffer.append("					for(var i = 0; i < open.length; i ++){                                  \n");
			buffer.append("						open[i].className = open[i].className.replace(\"open\",\"close\");       \n");
			buffer.append("					}                                                                        \n");
			buffer.append("					var element =  document.getElementsByClassName(event.target.innerHTML);  \n");
			buffer.append("					for(var i = 0; i < element.length; i ++){                                   \n");
			buffer.append("						element[i].className = element[i].className.replace(\"close\",\"open\"); \n");
			buffer.append("					}                                                                            \n");
			buffer.append("				});                                                                             \n");
			buffer.append("				var doctables = menu.childNodes;                                                  \n");
			buffer.append("				var search = document.getElementById(\"search\");                                 \n");
			buffer.append("				search.onkeyup = function(event){                                                    \n");
			buffer.append("				    var value = search.value;                                                         \n");
			buffer.append("				    for(var i = 0; i < doctables.length; i ++){                                       \n");
			buffer.append("				        if(value.trim() == \"\" || doctables[i].innerHTML.indexOf(value) >= 0){       \n");
			buffer.append("				         	doctables[i].className = \"show\"                                       \n");
			buffer.append("				        }else{                                                                         \n");
			buffer.append("				        	doctables[i].className = \"hide\"                                       \n");
			buffer.append("				        }                                                                        \n");
			buffer.append("				    }                                                                          \n");
			buffer.append("				}                                                                      \n");
			buffer.append("			}                                                                          \n");
			buffer.append("		</script>                                                                    \n");
			buffer.append("	</head>                                                                        \n");
			buffer.append("	<body>                                                                         \n");
			buffer.append("		<div class=\"body\">                                                           \n");
			buffer.append("			<div class=\"table-left\">                                                   \n");
			buffer.append("				<input id=\"search\" type=\"text\" placeholder=\"搜索\"/>                               \n");
			buffer.append("<ul id=\"table-menu\">");
			
			String tableName = "";
			String table = "";
			while (rs.next()) {
				if(columnList != null && columnList.size() > 0 && !tableName.equals(rs.getString(1))){
					tableList.add(columnList);
					columnList = null;
				}                           
				if(!tableName.equals(rs.getString(1))){
					columnList = new ArrayList<Map<String, String>>();
					tableName = rs.getString(1);
					if("".equals(rs.getString(5)) || rs.getString(5) == null){
						table = tableName;
					}else{
						table = tableName+"("+rs.getString(5)+")";
					}
					
					buffer.append("<li>"+table+"</li>");
					columnNameMap = new HashMap<String, String>();
					columnNameMap.put("tableName", table);
					columnNameMap.put("columnName", rs.getString(2));
					columnNameMap.put("columnType", rs.getString(3));
					columnNameMap.put("columnComment", rs.getString(4));
					columnList.add(columnNameMap);
				}else{
					columnNameMap = new HashMap<String, String>();
					columnNameMap.put("tableName", table);
					columnNameMap.put("columnName", rs.getString(2));
					columnNameMap.put("columnType", rs.getString(3));
					columnNameMap.put("columnComment", rs.getString(4));
					columnList.add(columnNameMap);
				}
			}
			tableList.add(columnList);
			
			buffer.append("</ul>\n");
			buffer.append("			</div>                                                                     \n");
			buffer.append("			<div class=\"table-right\">                                                  \n");
			for (int i = 0; i < tableList.size(); i++) {
				buffer.append("				<table class=\"close "+tableList.get(i).get(0).get("tableName")+"\">                                                \n");
				buffer.append("					<thead>                                                                \n");
				buffer.append("						<tr>                                                                 \n");
				buffer.append("							<th width=\"20%\">名称</th>                                          \n");
				buffer.append("							<th width=\"70%\">注释</th>                                          \n");
				buffer.append("							<th width=\"10%\">类型</th>                                          \n");
				buffer.append("						</tr>                                                                \n");
				buffer.append("					</thead>                                                               \n");
				buffer.append("					<tbody>                                                                \n");
				for(int j = 0; j < tableList.get(i).size(); j++){
					buffer.append("						<tr>                                                                 \n");
					buffer.append("							<td>"+tableList.get(i).get(j).get("columnName")+"</td>                                                    \n");
					buffer.append("							<td>"+tableList.get(i).get(j).get("columnComment")+"</td>                                                    \n");
					buffer.append("							<td>"+tableList.get(i).get(j).get("columnType")+"</td>                                                   \n");
					buffer.append("						</tr>                                                                \n");
				}
				buffer.append("					</tbody>                                                               \n");
				buffer.append("				</table>                                                                 \n");
			}
			buffer.append("			</div>                                                                     \n");
			buffer.append("		</div>                                                                       \n");
			buffer.append("		                                                                             \n");
			buffer.append("	</body>                                                                        \n");
			buffer.append("</html>                                                                         \n");
			
			File file = new File(path);
			OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file),"UTF-8");
			BufferedWriter bw = new BufferedWriter(osw);
			bw.write(buffer.toString());
			bw.flush();
			bw.close();
			osw.close();
		} catch (SQLException e) {
			System.out.println("MySQL操作错误");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}
		return "";
	}
}
