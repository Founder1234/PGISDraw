package com.draw.drawMapOnline.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import net.sf.json.JSONObject;

public class CommonDao {
	/**
	 * 
	 * @param orginParams ArrayList  查询需要的参数 查询字段，表名，查询条件 
	 * @return  JSONObject  json对象  将查询结果转成了json对象
	 * @throws IOException 
	 */
	public JSONObject getInfoByParam (JSONObject orginParams) throws IOException{
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		conn = DBUtil.getConn();//获取连接
		
		String sql = pickQuerySql(orginParams.get("fields").toString(),orginParams.get("tableName").toString(),orginParams.get("where").toString());
		JSONObject root=new JSONObject();
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			ArrayList<JSONObject> resultList = new ArrayList<JSONObject>();
			ResultSetMetaData rsmData = rs.getMetaData();
			int nCols = rsmData.getColumnCount();
			while (rs.next()) {
				JSONObject result=new JSONObject();
				for(int k = 0; k < nCols; k++){
					String columnName = rsmData.getColumnName(k+1);
					String cloumnType=rsmData.getColumnTypeName(k+1);
					if(cloumnType.equalsIgnoreCase("CLOB")){
						Clob clob = rs.getClob(columnName);
						if (clob != null&&clob.length()>0) {
							result.put(columnName,this.clobToString(clob));
						}else{
							result.put(columnName,"");
						}
					}else{
						String columnsValue = "";
						if(rs.getString(columnName)==null){
							columnsValue = "";
						}else{
							columnsValue = rs.getString(columnName);
						}
						result.put(columnName,columnsValue);
					}
				}
				resultList.add(result);
			}
			root.put("resultList", resultList);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try{
				if(rs!=null){
					rs.close();
				}
				if(ps !=null){
					ps.close();
				}
				DBUtil.closeConn(conn);
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		return root;
	}
	/**
	 * 将"Clob"型数据转换成"String"型数据
	 */
	protected  String clobToString(Clob clob) throws SQLException, IOException
	{
		 String reString = "";
        if(clob!=null){
        	  Reader is = clob.getCharacterStream();// 得到流
              BufferedReader br = new BufferedReader(is);
              String s = br.readLine();
              StringBuffer sb = new StringBuffer();
              while (s != null) {// 执行循环将字符串全部取出付值给StringBuffer由StringBuffer转成STRING
                  sb.append(s);
                  s = br.readLine();
              }
              reString = sb.toString();
              
        }
        return reString;		
	}
	/**
	 * 通过直接传入sql语句进行查询  用于复杂查询**
	 * @param orginParams
	 * @return
	 */
	public JSONObject getInfoBySql(JSONObject orginParams){
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		conn = DBUtil.getConn();//获取连接
		String sql = orginParams.get("where").toString();
		String[] columns = orginParams.get("fields").toString().split(",");//列的集合
		JSONObject root=new JSONObject();
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			ArrayList<JSONObject> resultList = new ArrayList<JSONObject>();
			while (rs.next()) {
				JSONObject result=new JSONObject();
				for(int i=0;i<columns.length;i++){
					String columnName= columns[i].trim();
					String columnsValue = "";
					if(rs.getString(columnName)==null){
						columnsValue = "";
					}else{
						columnsValue = rs.getString(columnName);
					}
					result.put(columnName,columnsValue);
				}
				resultList.add(result);
			}
			root.put("resultList", resultList);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try{
				if(rs!=null){
					rs.close();
				}
				if(ps !=null){
					ps.close();
				}
				DBUtil.closeConn(conn);
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		return root;
	}
	/**
	 * 通过传入的参数，插入一条数据
	 * @param orginParams  参数集合
	 * @return  JSONObject
	 */
	public JSONObject insertInfoByParam (JSONObject orginParams){
		Connection conn = null;
		int rs = -1;
		PreparedStatement ps = null;
		conn = DBUtil.getConn();//获取连接
		
		String sql = pickInsertSql(orginParams.get("fields").toString(),orginParams.get("tableName").toString(),orginParams.get("where").toString());
		JSONObject root=new JSONObject();
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeUpdate();
			Boolean resultState = false;
			if(rs>=0){
				resultState = true;
			}
			root.put("state", resultState);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try{
				if(ps !=null){
					ps.close();
				}
				DBUtil.closeConn(conn);
			}catch(SQLException e){
				e.printStackTrace();
			}finally{
				
			}
		}
		return root;
	}
	/**
	 * 
	 * @param fields
	 * @param tableName
	 * @param values
	 * @return
	 */
	private String pickInsertSql(String fields, String tableName, String values) {
		String sql = "insert into  "+tableName+"( "+fields+ ") values (" +values+")"; 
		System.out.println(sql);
		return sql;
	}
	/**
	 * 传入三个参数查询数据
	 * @param fields  要查询的字段 多个字段格式----" field1,field2,field3 " ;全部------  " * " 
	 * @param tableName  要查询的表名
	 * @param where  查询条件
	 * @return  sql语句
	 */
	public String pickQuerySql (String fields,String tableName,String where){
 		if(!where.equals("") && null!=where){
			where +=" ";
		}else{
			where ="1=1";
		}
 		if(fields.equals("") || fields==null){
 			fields = "*";
 		}
		String sql = "select "+fields+" from "+tableName+ " where " +where; 
		System.out.println(sql);
		return sql;
		
	}
	private String pickdeleteSql(String fields, String tableName, String where) {
		String sql = "delete from "+tableName+ " where " +where; 
		System.out.println(sql);
		return sql;
	}
	private String pickUpdateSql(String fields, String tableName, String where) {
		String sql = "update "+tableName+ " set "+fields+" where " +where; 
		System.out.println(sql);
		return sql;
	}
	/**
	 * 根据传入的参数删除一条数据
	 * @param orginParams
	 * @return JSONObject
	 */
	public JSONObject deleteInfoByParam (JSONObject orginParams){
		Connection conn = null;
		int rs = -1;
		PreparedStatement ps = null;
		conn = DBUtil.getConn();//获取连接
		
		String sql = pickdeleteSql(orginParams.get("fields").toString(),orginParams.get("tableName").toString(),orginParams.get("where").toString());
	    JSONObject root=new JSONObject();
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeUpdate();
			Boolean resultState = false;
			if(rs>=0){
				resultState = true;
			}
			root.put("state", resultState);
		} catch (SQLException e) {
		}finally{
			try{
				if(ps !=null){
					ps.close();
				}
				DBUtil.closeConn(conn);
			}catch(SQLException e){
				e.printStackTrace();
			}finally{
				
			}
		}
		return root;
	}
	/**
	 * 根据传入的参数修改一条数据
	 * @param orginParams
	 * @return JSONObject
	 */
	public JSONObject updateInfoByParam (JSONObject orginParams){
		Connection conn = null;
		int rs = -1;
		PreparedStatement ps = null;
		conn = DBUtil.getConn();//获取连接
		
		String sql = pickUpdateSql(orginParams.get("fields").toString(),orginParams.get("tableName").toString(),orginParams.get("where").toString());
	    JSONObject root=new JSONObject();
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeUpdate();
			Boolean resultState = false;
			if(rs>=0){
				resultState = true;
			}
			root.put("state", resultState);
		} catch (SQLException e) {
		}finally{
			try{
				if(ps !=null){
					ps.close();
				}
				DBUtil.closeConn(conn);
			}catch(SQLException e){
				e.printStackTrace();
			}finally{
				
			}
		}
		return root;
	}
	/**
	 * 通过传入的参数，插入一条数据
	 * @param orginParams  参数集合
	 * @return  JSONObject
	 */
	public JSONObject insertInfoByClobParam (JSONObject orginParams){
		Connection conn = null;
		int rs = -1;
		PreparedStatement ps = null;
		conn = DBUtil.getConn();//获取连接
		JSONObject jsonParams=(JSONObject) orginParams.get("jsonParams");
		String[] field=orginParams.get("fields").toString().split(",");
		String sql = pickInsertSql(orginParams.get("fields").toString(),orginParams.get("tableName").toString(),orginParams.get("where").toString());
		JSONObject root=new JSONObject();
		try {
			ps = conn.prepareStatement(sql);
			for(int i=0;i<field.length;i++){
				String col=field[i].trim().toUpperCase();
				String colType=getColumnsType(orginParams.get("tableName").toString(),col.toUpperCase());
				if(colType.equals("CLOB")){
					ps.setCharacterStream(i+1, new StringReader(jsonParams.get(col)==null?"":jsonParams.get(col).toString()));  
				}else{
					ps.setString(i+1, jsonParams.get(col)==null?"":jsonParams.get(col).toString());
				}
				
			}
			rs = ps.executeUpdate();
			Boolean resultState = false;
			if(rs>=0){
				resultState = true;
			}
			root.put("state", resultState);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try{
				if(ps !=null){
					ps.close();
				}
				DBUtil.closeConn(conn);
			}catch(SQLException e){
				e.printStackTrace();
			}finally{
				
			}
		}
		return root;
	}
	
	
	/**
	 * 根据传入的参数修改一条数据,特别注意，字段中包含Clob字段时使用该方法。
	 * @param orginParams
	 * 调用方法，fields字段中每个字段以@隔开！如： MAPSERVICELIST='"+allInfoStr+" @ objectid=2 
	 * @return {"state":true}
	 */
	public JSONObject updateClobInfoByParam (JSONObject orginParams){
		Connection conn = null;
		int rs = -1;
		PreparedStatement ps = null;
		String tableName = orginParams.get("tableName").toString();
		String fields = orginParams.get("fields").toString();//取到前端传回来的string
		JSONObject fieldsObj = JSONObject.fromObject(fields);//转成对象
		Set<?> keySet = fieldsObj.keySet();
		Iterator<?> iter = keySet.iterator();
		ArrayList<String> keysArray = new ArrayList<String>();
		while(iter.hasNext()){//循环key
			Object x = iter.next();
			String tempKey = x.toString();
			keysArray.add(tempKey);
		}
		conn = DBUtil.getConn();//获取连接
		String sqlforFields = "";
		for(int j=0;j<keysArray.size();j++){
			String tempCol=((String) keysArray.get(j)).trim();
			sqlforFields+=tempCol+"= ? ,";
		}	
		sqlforFields = sqlforFields.substring(0, sqlforFields.length()-1);
		String sql = pickUpdateSql(sqlforFields,tableName,orginParams.get("where").toString());
		JSONObject root=new JSONObject();
		try {
			ps = conn.prepareStatement(sql);
			for(int i=0;i<keysArray.size();i++){
				String col=((String) keysArray.get(i)).trim();
				String colType=getColumnsType(orginParams.get("tableName").toString(),col.toUpperCase());
				if(colType.equals("CLOB")){
					ps.setCharacterStream(i+1, new StringReader(fieldsObj.getString(col).toString()));  
				}else{
					ps.setString(i+1, fieldsObj.getString(col).toString());
				}
				
			}
			rs = ps.executeUpdate();
			Boolean resultState = false;
			if(rs>=0){
				resultState = true;
			}
			root.put("state", resultState);
		} catch (SQLException e) {
		}finally{
			try{
				if(ps !=null){
					ps.close();
				}
				DBUtil.closeConn(conn);
			}catch(SQLException e){
				e.printStackTrace();
			}finally{
				
			}
		}
		return root;
	}
	/**
	 * 根据传入的参数修改一条数据
	 * @param orginParams
	 * @return JSONObject
	 */
	public JSONObject updateInfoByClobParam (JSONObject orginParams){
		Connection conn = null;
		int rs = -1;
		PreparedStatement ps = null;
		conn = DBUtil.getConn();//获取连接
		JSONObject jsonParams=(JSONObject) orginParams.get("jsonParams");
		String[] field=orginParams.get("fields").toString().split(",");
		String sql = pickUpdateSql(orginParams.get("fields").toString(),orginParams.get("tableName").toString(),orginParams.get("where").toString());
	    JSONObject root=new JSONObject();
		try {
			ps = conn.prepareStatement(sql);
			for(int i=0;i<field.length;i++){
				String col=field[i].split("=")[0].trim();
				String colType=getColumnsType(orginParams.get("tableName").toString(),col.toUpperCase());
				if(colType.equals("CLOB")){
					ps.setCharacterStream(i+1, new StringReader(jsonParams.get(col)==null?"":jsonParams.get(col).toString()));  
				}else{
					ps.setString(i+1, jsonParams.get(col)==null?"":jsonParams.get(col).toString());
				}
				
			}
			rs = ps.executeUpdate();
			Boolean resultState = false;
			if(rs>=0){
				resultState = true;
			}
			root.put("state", resultState);
		} catch (SQLException e) {
		}finally{
			try{
				if(ps !=null){
					ps.close();
				}
				DBUtil.closeConn(conn);
			}catch(SQLException e){
				e.printStackTrace();
			}finally{
				
			}
		}
		return root;
	}
	/**
	 * select * from dba_tab_columns where table_name='JJYZQ_SHZQ_PG' and owner='GIS340000000000'
	 * 
	 */
	public ArrayList<String> getColumns(String tableName){
		Connection conn=null;
	    PreparedStatement ps = null;
	    ResultSet rs = null;
	    String sql;
	    ArrayList<String> list=new ArrayList<String>();
		try {
			conn = DBUtil.getConn();//获取连接
//			String[] dbNameList=conn.getMetaData().getUserName().split("@");
//			String dbusername = dbNameList[0].toUpperCase();
//			String dbuserDriver = conn.getMetaData().getDriverName().toUpperCase();
			Properties pro=PropertiesUtil.getPro();
			String dbusername = pro.getProperty(GeneralMarker.ORACLE_USER).trim().toUpperCase();
			String dbDataSource = pro.getProperty(GeneralMarker.ORACLE_DATASOURCE).trim().toUpperCase();
			System.out.println("数据源"+dbDataSource);
			if (conn != null) {
				if("MYSQL".equals(dbDataSource) && !dbDataSource.equals("")){
					String db_url=pro.getProperty(GeneralMarker.ORACLE_URL).trim();
					String dbName=db_url.substring(db_url.lastIndexOf("/")+1,db_url.length());
					sql="select COLUMN_NAME from information_schema.COLUMNS where table_name = '"+ tableName + "' and table_schema = '"+dbName+"'";
				}else if("ORACLE".equals(dbDataSource) && !dbDataSource.equals("")){
					sql="select column_name from dba_tab_columns where table_name='"+tableName+"' and owner='"+dbusername+"'";
				}else{
					throw new Exception("不支持当前数据库！！！");
				}
//				if(dbuserDriver.indexOf("MYSQL")>-1){
//					String db_url = conn.getMetaData().getURL();
//					String dbName=db_url.substring(db_url.lastIndexOf("/")+1,db_url.length());
//						sql="SELECT  DATA_TYPE as '字段类型'  FROM information_schema.`COLUMNS` where TABLE_NAME = '"+tableName+"' and column_name='"+ColumnName+"' and  table_schema ='"+dbName+"'";
//					}else if(dbuserDriver.indexOf("ORACLE")>-1){
//						sql="select data_type from dba_tab_columns where table_name='"+tableName+"' and column_name='"+ColumnName+"' and  owner='"+dbusername+"'";
//					}else{
//						throw new Exception("不支持当前数据库！！！");
//					}
				//sql="select column_name from user_tab_columns where table_name='"+tableName+"'";
				ps = conn.prepareStatement(sql);
				rs = ps.executeQuery();
				
				while (rs.next()) {
					String cname=rs.getString(1);
					list.add(cname);
				}
				
				rs.close();
				ps.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try{
				if(rs!=null){
					rs.close();
				}
				if(ps !=null){
					ps.close();
				}
				DBUtil.closeConn(conn);
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		return list;
	}
	/**
	 * select * from dba_tab_columns where table_name='JJYZQ_SHZQ_PG' and owner='GIS340000000000'
	 * 
	 */
	public JSONObject getColumns(JSONObject params){
		Connection conn=null;
	    PreparedStatement ps = null;
	    ResultSet rs = null;
	    String sql;
	    ArrayList<String> list=new ArrayList<String>();
	    JSONObject root=new JSONObject();
		try {
			conn = DBUtil.getConn(
					params.get("driver").toString()
					,params.get("url").toString()
					,params.get("username").toString()
					,params.get("pwd").toString());//获取连接
			String dbusername = params.get("owner").toString().trim().toUpperCase();
			if (conn != null) {
				sql="select column_name from all_tab_columns where table_name='"+params.get("layername").toString()+"' and owner='"+dbusername+"'";
				System.out.println(sql);
				ps = conn.prepareStatement(sql);
				rs = ps.executeQuery();
				
				while (rs.next()) {
					String cname=rs.getString(1);
					list.add(cname);
				}
				
				rs.close();
				ps.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try{
				if(rs!=null){
					rs.close();
				}
				if(ps !=null){
					ps.close();
				}
				DBUtil.closeConn(conn);
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		root.put("resultList", list);
		return root;
	}
	/**
	 * select * from dba_tab_columns where table_name='JJYZQ_SHZQ_PG' and owner='GIS340000000000'
	 * 
	 */
	public String getColumnsType(String tableName,String ColumnName){
		Connection conn=null;
	    PreparedStatement ps = null;
	    ResultSet rs = null;
	    String sql;
	    String dataType="";
		try {
			conn = DBUtil.getConn();//获取连接
			Properties pro=PropertiesUtil.getPro();
			String dbusername = pro.getProperty(GeneralMarker.ORACLE_USER).trim();
			String dbDataSource = pro.getProperty(GeneralMarker.ORACLE_DATASOURCE).trim().toUpperCase();
			if (conn != null) {
				if("MYSQL".equals(dbDataSource) && !dbDataSource.equals("")){
					String db_url=pro.getProperty(GeneralMarker.ORACLE_URL).trim();
					String dbName=db_url.substring(db_url.lastIndexOf("/")+1,db_url.length());
					sql="SELECT  DATA_TYPE as '字段类型'  FROM information_schema.`COLUMNS` where TABLE_NAME = '"+tableName+"' and column_name='"+ColumnName+"' and  table_schema ='"+dbName+"'";
				}else if("ORACLE".equals(dbDataSource) && !dbDataSource.equals("")){
					sql="select data_type from dba_tab_columns where table_name='"+tableName+"' and column_name='"+ColumnName+"' and  owner='"+dbusername+"'";
				}else{
					throw new Exception("不支持当前数据库！！！");
				}
				ps = conn.prepareStatement(sql);
				rs = ps.executeQuery();
				
				while (rs.next()) {
					dataType=rs.getString(1);
				}
				
				rs.close();
				ps.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try{
				if(rs!=null){
					rs.close();
				}
				if(ps !=null){
					ps.close();
				}
				DBUtil.closeConn(conn);
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		return dataType;
	}
	public static void main(String[] args) {
//		CommonDao com = new CommonDao();
//		ArrayList<String> s = new ArrayList<String>();
//		s.add("*");
//		s.add("MH_XTC_PGIS");
//		s.add("");
//		JSONObject JS =com.getInfoByParam(s);
//		s = com.getColumns("MH_XTC_PGIS");
//		s.add("id,Userid,Flag,Objectid,owner,Tablename");
//		s.add("MH_PO_POI");
//		s.add("23,'admin','fav',45,'GIS340000000000','AH_CS_HDCS_PT'");
//		com.insertInfoByParam(s);
		//insert into MH_PO_POI(id,Userid,Flag,Objectid,owner,Tablename) values(2323,'admin','fav',45,'GIS340000000000','AH_CS_HDCS_PT');
	}
}
