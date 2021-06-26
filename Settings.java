import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Settings {
	private static Connection conn = null;

	/*  [Подключение к БД]  */
	protected static void login(String dbName, String login, String password) throws SQLException {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (Exception ex) {
			return;
		}
		conn = DriverManager.getConnection(String.format("jdbc:postgresql:%s", dbName), login, password);
	}
	protected static ArrayList<String> getHeaders() {
		Statement stmt = null;
		ResultSet rs = null;
		ArrayList<String> headers = new ArrayList<String>();
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select column_name\r\n" + "from INFORMATION_SCHEMA.COLUMNS\r\n" + "where TABLE_NAME='products'");

			while (rs.next()) { headers.add(rs.getString(1)); }
		} catch (SQLException ex) {}
		finally { try { if (rs != null) rs.close(); if (stmt != null) stmt.close(); } catch (SQLException ex) {} }
		return headers;
	}
	
	/*  [Создание БД]  */
	protected static int createDB(String dbName, String login, String password) throws SQLException {
		conn = null;
		Statement stmt = null;
		try {
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection(("jdbc:postgresql://localhost/"), login, password);
			stmt = conn.createStatement();
			stmt.executeUpdate("CREATE DATABASE " + dbName); // 123456
			
			conn = DriverManager.getConnection(String.format("jdbc:postgresql:%s", dbName), login, password);
			stmt = conn.createStatement();
			
			stmt.execute("CREATE TABLE products(sell_id integer PRIMARY KEY, product_name varchar(32), product_info varchar(12), product_price integer)");
			// -------------------- Получение информации --------------------
			stmt.execute("CREATE OR REPLACE FUNCTION getInfo(field text, val character varying) "+
			"RETURNS SETOF products " +
			"AS $$ " +
				" DECLARE r products%rowtype; " +
				" BEGIN" +
					" for r in" +
						" execute format('select * from products where %s=''%s''',$1,$2)" +
					" loop" +
						" return next r;" +
					" end loop;" +
					" if not found" + 
						" then raise exception 'Not found';" +
					" end if;" +
					" return;" +
				" END" +
			" $$ LANGUAGE plpgsql;");
			
			// -------------------- Обновление значения по ID --------------------
			stmt.execute("CREATE OR REPLACE PROCEDURE textUpdate(field character varying, newValue character varying, id integer) " + 
			" LANGUAGE plpgsql " +
			" AS $$ " +
				" BEGIN " +
					" execute format('UPDATE products set %s=''%s'' where sell_id=%L', field, newValue, id); " +
				" END " +
			" $$;");
			
			// -------------------- Удаление строки по ID --------------------
			stmt.execute("CREATE OR REPLACE PROCEDURE deleteRow(id int)\r\n" + 
			"LANGUAGE plpgsql\r\n" + 
			"AS $$\r\n" + 
				"	BEGIN\r\n" + 
				"		DELETE FROM products WHERE sell_id = $1;\r\n" + 
				"	END\r\n" + 
			"$$;");
			
			// -------------------- Очистка таблицы --------------------
			stmt.execute("CREATE OR REPLACE PROCEDURE clearTable()\r\n" + 
			"LANGUAGE plpgsql\r\n" + 
			"AS $$\r\n" + 
				"	BEGIN\r\n" + 
				"		TRUNCATE products;\r\n" + 
				"	END\r\n" + 
			"$$;");
			
			// -------------------- Вставка информации --------------------
			stmt.execute("CREATE OR REPLACE PROCEDURE insertData(id int, name text, pos text, salary int)\r\n" + 
			"LANGUAGE plpgsql\r\n" + 
			"AS $$\r\n" + 
				"	BEGIN\r\n" + 
				"		if id in (SELECT sell_id from products)\r\n" + 
				"			then raise exception '[!]It is already exists!';\r\n" + 
				"		end if;\r\n" + 
				"		EXECUTE format('insert into products VALUES (%L, ''%s'', ''%s'', %L)', id, name, pos, salary);\r\n" + 
				"	END\r\n" + 
			"$$;");
			
			// -------------------- Получить всю инфоррмацию --------------------
			stmt.execute("CREATE OR REPLACE FUNCTION getAllInfo()\r\n" + 
			"RETURNS SETOF products\r\n" + 
			"AS $$\r\n" + 
				"	SELECT * FROM products;\r\n" + 
			"$$ LANGUAGE sql;");
			
			// -------------------- Удалить базу данных --------------------
			stmt.execute("CREATE OR REPLACE PROCEDURE deleteDB(DBname text)\r\n" + 
			"LANGUAGE plpgsql\r\n" + 
			"AS $$\r\n" + 
				"	BEGIN\r\n" + 
				"		DROP DATABASE DBname;\r\n" + 
				"	END\r\n" + 
			"$$;");
			// -----------------------------------------------------------------
		} catch (SQLException ex) {
			return -1;
		} catch (ClassNotFoundException e){e.printStackTrace();}
		finally { try { if (stmt != null) stmt.close(); } catch (SQLException ex) {} }
		return 0;
	}

	/*  [Метод: Получить всю информацию]  */
	protected static Object[][] getInfo() {
		Statement stmt = null;
		ResultSet rs = null;
		List<List<Object>> inf = new ArrayList<List<Object>>();
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select * from products");
			while (rs.next()) {
				List<Object> row = new ArrayList<Object>();
				row.add(rs.getInt(1));
				row.add(rs.getString(2));
				row.add(rs.getString(3));
				row.add(rs.getInt(4));
				inf.add(row);
			}
		} catch (SQLException ex) {}
		finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
			} catch (SQLException ex) {
			}
		}
		Object[][] info = new Object[inf.size()][4];
		for (int i = 0; i < inf.size(); i++) {
			for (int j = 0; j < 4; j++) {
				info[i][j] = inf.get(i).get(j);
			}
		}

		return info;
	}

	/*  [Метод: Найти информацию]  */
	protected static Object[][] search(String field, String value) throws SQLException {
		CallableStatement connect = null;
		ResultSet rs = null;
		if (value.length() == 0) { return getInfo(); }
		List<List<Object>> inf = new ArrayList<List<Object>>();
		connect = conn.prepareCall("select * from getInfo(?,?)");
		connect.clearParameters();
		connect.setString(1, field);
		connect.setString(2, value);
		rs = connect.executeQuery();
		while (rs.next()) {
			List<Object> row = new ArrayList<Object>();
			row.add(rs.getInt(1));
			row.add(rs.getString(2));
			row.add(rs.getString(3));
			row.add(rs.getInt(4));
			inf.add(row);
		}

		try { if (connect != null) connect.close(); } catch (SQLException ex) {}

		Object[][] info = new Object[inf.size()][4];
		for (int i = 0; i < inf.size(); i++) {
			for (int j = 0; j < 4; j++) {
				info[i][j] = inf.get(i).get(j);
			}
		}

		return info;
	}

	/*  [Метод: Обновить значение по ID]  */
	protected static void textUpdate(String field, String newValue, int id) throws SQLException {
		CallableStatement connect = null;
		connect = conn.prepareCall("call textUpdate(?,?,?)");
		connect.clearParameters();
		connect.setString(1, field);
		connect.setString(2, newValue);
		connect.setInt(3, id);
		connect.execute();

		try { if (connect != null) connect.close();} catch (SQLException ex) {}

	}

	/*  [Метод: Добавить данные]  */
	protected static void add(int id, String name, String place, int comm) throws SQLException {
		CallableStatement connect = null;
		connect = conn.prepareCall("call insertData(?,?,?,?)");
		connect.clearParameters();
		connect.setInt(1, id);
		connect.setString(2, name);
		connect.setString(3, place);
		connect.setInt(4, comm);
		boolean result = connect.execute();
		try { if (connect != null) connect.close(); } catch (SQLException ex) {}
	}

	/*  [Метод: Очистить таблицу]  */
	protected static void clearTable() throws SQLException {
		CallableStatement connect = null;

		connect = conn.prepareCall("call clearTable()");
		connect.execute();

		try { if (connect != null) connect.close(); } catch (SQLException ex) {}
	}

	/*  [Метод: Удалить строку]  */
	protected static void deleteRow(int id) throws SQLException {
		CallableStatement connect = null;
		connect = conn.prepareCall("call deleteRow(?)");
		connect.clearParameters();
		connect.setInt(1, id);
		connect.execute();
		try { if (connect != null) connect.close(); } catch (SQLException ex) {}
	}

	/*  [Метод: Удалить БД]  */
	protected static void deleteDb(String dbName) throws SQLException {
		CallableStatement connect = null;
		Statement stmt = null;
		try {
			conn.close();
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection("jdbc:postgresql://localhost/", FirstWindow.login, FirstWindow.pass);
			stmt = conn.createStatement();
			stmt.execute("DROP DATABASE " + dbName);
		}
		catch(SQLException | ClassNotFoundException ex) {}

		try { if (conn != null) conn.close(); if(stmt != null) stmt.close(); } catch (SQLException ex) {}
	}
}
