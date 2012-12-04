package com.u2p.db;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DbDataSource {
	private DbGroups groups;
	private SQLiteDatabase database;
	private DbU2P dbHelper;
	private String[] allColumnsUsers={DbU2P.COLUM_ID,DbU2P.COLUM_USER,DbU2P.COLUM_GROUP,
			DbU2P.COLUM_HASH};
	private String[] allColumnsGroup={DbU2P.GROUP_COLUM_ID,DbU2P.GROUP_COLUM_NAME
			,DbU2P.GROUP_COLUM_URI,DbU2P.GROUP_COLUM_POSITIVE,DbU2P.GROUP_COLUM_NEGATIVE};
	
	public DbDataSource(Context context){
		dbHelper=new DbU2P(context);
		groups=new DbGroups();
	}
	
	public void open()throws SQLException{
		database=dbHelper.getWritableDatabase();
	}
	
	public void close(){
		dbHelper.close();
	}
	
	public boolean addUser(String name,String group,String pass){
		if(!this.tableExist(group)){
			String hash=this.sha1(pass);
			ContentValues values=new ContentValues();
			values.put(DbU2P.COLUM_USER,name);
			values.put(DbU2P.COLUM_GROUP,group);
			values.put(DbU2P.COLUM_HASH,hash);
			long insertId=database.insert(DbU2P.TABLE_USERS, null, values);
			Log.d(DbDataSource.class.getName(),"User add with id: "+insertId);
			this.createGroup(group);
			return true;
		}
		return false;
	}
	
	private boolean createGroup(String name){
		//Comprobamos si la tabla del grupo ya existe
		if(this.tableExist(name)){
			Log.d(DbDataSource.class.getName(), "Table "+name+" already exists");
			return false;
		}
		//Creamos la tabla
		groups.addGroup(name);
		String CREATE_TABLE_GROUP=String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY," +
				" %s TEXT NOT NULL, %s TEXT NOT NULL, %s INTEGER, %s INTEGER);",name,
				DbU2P.GROUP_COLUM_ID,DbU2P.GROUP_COLUM_NAME,DbU2P.GROUP_COLUM_URI,
				DbU2P.GROUP_COLUM_POSITIVE,DbU2P.GROUP_COLUM_NEGATIVE);
		
		database.execSQL(CREATE_TABLE_GROUP);
		Log.d(DbDataSource.class.getName(),"New group created: "+name);
		return true;
	}
	
	public boolean addFileToGroup(String group,String fileName,String uri,int positive,int negative){
		if(this.tableExist(group)){
			ContentValues values=new ContentValues();
			values.put(DbU2P.GROUP_COLUM_NAME,fileName);
			values.put(DbU2P.GROUP_COLUM_URI,uri);
			values.put(DbU2P.GROUP_COLUM_POSITIVE, positive);
			values.put(DbU2P.GROUP_COLUM_NEGATIVE, negative);
			long insertId=database.insert(group, null, values);
			Log.d(DbDataSource.class.getName(),"New file add with id: "+insertId+" to "+group);
			
			Cursor cursor=database.query(group,
					allColumnsGroup, DbU2P.GROUP_COLUM_ID+" = "+ insertId,null,
					null,null,null);
			
			cursor.moveToFirst();
			DbFile newFile=cursorToFile(cursor);
			cursor.close();			
			newFile.setGroup(group);
			groups.addFileToGroup(group, newFile);
			return true;
		}
		return false;
	}
	
	public void deleteUser(DbUser user){
		long id=user.getId();
		Log.d(DbDataSource.class.getName(),"User deleted with id: "+id);
		database.delete(DbU2P.TABLE_USERS,DbU2P.COLUM_ID+" = "+id,null);
	}
	
	public boolean deleteGroup(String group){
		if(this.tableExist(group)){
			database.execSQL("DROP TABLE IF EXISTS "+group+";");
			Log.d(DbDataSource.class.getName(),"Deleted table "+group);
			return true;
		}
		return false;
	}
	
	public boolean deleteFileGroup(String group,DbFile file){
		if(this.tableExist(group)){
			database.delete(group, DbU2P.GROUP_COLUM_ID+" = "+file.getId(),null);
			Log.d(DbDataSource.class.getName(),"Deleted file with id: "+file.getId()+" from group "+group);
			groups.deleteFile(group, file);
			return true;
		}
		return false;
	}
	
	public List<String> getAllGroups(){
		List<String> listsGroups=new ArrayList<String>();
		
		Cursor cursor=database.query(DbU2P.TABLE_USERS,
				new String[] {DbU2P.COLUM_GROUP},null,null,null,null,null);
		
		cursor.moveToFirst();
		while(!cursor.isAfterLast()){
			listsGroups.add(cursor.getString(0));
			cursor.moveToNext();
		}
		cursor.close();
		return listsGroups;
	}
	
	public List<DbFile> getGroup(String group){
		if(this.tableExist(group)){
			List<DbFile> files=new ArrayList<DbFile>();
			
			Cursor cursor=database.query(group,
					allColumnsGroup,null,null,null,null,null);
			cursor.moveToFirst();
			while(!cursor.isAfterLast()){
				DbFile file=cursorToFile(cursor);
				files.add(file);
				cursor.moveToNext();
			}
			cursor.close();
			return files;
		}
		return null;
	}
	
	public List<DbUser> getAllUsers(){
		List<DbUser> users=new ArrayList<DbUser>();
		
		Cursor cursor=database.query(DbU2P.TABLE_USERS,
				allColumnsUsers,null,null,null,null,null);
		
		cursor.moveToFirst();
		while(!cursor.isAfterLast()){
			DbUser user=cursorToUser(cursor);
			users.add(user);
			cursor.moveToNext();
		}
		cursor.close();
		return users;
	}
	
	public DbUser getUser(int id){
		DbUser user;
		Cursor cursor=database.query(DbU2P.TABLE_USERS,allColumnsUsers,
				DbU2P.COLUM_ID+" = "+id, null,null,null,null);
		
		cursor.moveToFirst();
		user=cursorToUser(cursor);
		cursor.close();
		return user;
	}
	
	public DbUser getUser(String name){
		DbUser user;
		Cursor cursor=database.query(DbU2P.TABLE_USERS,allColumnsUsers,
				DbU2P.COLUM_USER+" = "+name, null,null,null,null);
		
		cursor.moveToFirst();
		user=cursorToUser(cursor);
		cursor.close();
		return user;
	}
	
	public List<DbFile> getFilesGroup(String group){
		if(this.tableExist(group)){
			List<DbFile> files=new ArrayList<DbFile>();
			Cursor cursor=database.query(group,
					allColumnsGroup,null,null,null,null,null);
			
			cursor.moveToFirst();
			while(!cursor.isAfterLast()){
				DbFile file=cursorToFile(cursor);
				files.add(file);
				cursor.moveToNext();
			}
			cursor.close();
			return files;
		}
		return null;
	}
	
	private String sha1(String pass){
		try{
			MessageDigest digest=java.security.MessageDigest.getInstance("SHA-1");
			digest.update(pass.getBytes());
			
			byte messageDigest[]=digest.digest();
			StringBuffer hexString=new StringBuffer();
			for(int i=0;i<messageDigest.length; i++){
				hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
			}
			return hexString.toString();
			
		}catch(NoSuchAlgorithmException e){
			Log.e(DbDataSource.class.getName(),"NoSuchAlgorithm Exception");
		}
		return null;
	}
	
	public boolean usersExist(){
		if(database.isOpen()){
			Cursor cursor = database.rawQuery("SELECT COUNT(*) FROM "+DbU2P.TABLE_USERS,null);
			if(!cursor.moveToFirst()){
				return false;
			}
			int count = cursor.getInt(0);
			cursor.close();
			return count > 0;
		}
		return false;
	}
	
	private boolean tableExist(String tablename){
		
		if(tablename==null || !database.isOpen()){
			return false;
		}
		
		Cursor cursor = database.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type=? AND name=?",
				new String[] {"table",tablename});
		if(!cursor.moveToFirst()){
			return false;
		}
		int count = cursor.getInt(0);
		cursor.close();
		return count > 0;
	}
	
	private DbUser cursorToUser(Cursor cursor){
		DbUser user=new DbUser(cursor.getString(1),cursor.getString(2),cursor.getString(3));
		user.setId(cursor.getLong(0));
		return user;
	}
	
	private DbFile cursorToFile(Cursor cursor){
		DbFile file=new DbFile(cursor.getString(1),cursor.getString(2),cursor.getInt(3),cursor.getInt(4));
		file.setId(cursor.getLong(0));
		return file;
	}	
}