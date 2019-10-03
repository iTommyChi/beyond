package cn.duan.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.IOUtils;

/**
 * java 操作hbase  未测试
 * CRUD Operations
 * 
 */
public class HBaseOperation {

	public static Table getHTableByTableName(String tableName) throws Exception {
		// Get instance of Default Configuration
		Configuration configuration = HBaseConfiguration.create();

		Connection connection = ConnectionFactory.createConnection(configuration);

		Table table = connection.getTable(TableName.valueOf(tableName));

		// Get table instance  过时
		//HTable table = new HTable(configuration, tableName);

		return table;

	}

	public void getData() throws Exception {
		String tableName = "user"; // default.user / hbase:meta

		Table table = getHTableByTableName(tableName);

		// Create Get with rowkey
		Get get = new Get(Bytes.toBytes("10002")); // "10002".toBytes()

		// ==========================================================================
		// add column
		get.addColumn(//
				Bytes.toBytes("info"), //
				Bytes.toBytes("name"));
		get.addColumn(//
				Bytes.toBytes("info"), //
				Bytes.toBytes("age"));

		// Get Data
		Result result = table.get(get);

		// Key : rowkey + cf + c + version
		// Value: value
		for (Cell cell : result.rawCells()) {
			System.out.println(//
					Bytes.toString(CellUtil.cloneFamily(cell)) + ":" //
							+ Bytes.toString(CellUtil.cloneQualifier(cell)) + " ->" //
							+ Bytes.toString(CellUtil.cloneValue(cell)));
		}

		// Table Close
		table.close();

	}

	/**
	 * 建议 tablename & column family -> 常量 , HBaseTableContent
	 * 
	 * Map<String,Obejct>
	 * 
	 * @throws Exception
	 */
	public void putData() throws Exception {
		String tableName = "user"; // default.user / hbase:meta

		Table table = getHTableByTableName(tableName);

		Put put = new Put(Bytes.toBytes("10004"));

		// Add a column with value
		put.addColumn(//
				Bytes.toBytes("info"), //
				Bytes.toBytes("name"), //
				Bytes.toBytes("zhaoliu")//
		);

		put.addColumn(//
				Bytes.toBytes("info"), //
				Bytes.toBytes("age"), //
				Bytes.toBytes(25)//
		);
		put.addColumn(//
				Bytes.toBytes("info"), //
				Bytes.toBytes("address"), //
				Bytes.toBytes("shanghai")//
		);

		table.put(put);

		table.close();
	}

	public void delete() throws Exception {
		String tableName = "user"; // default.user / hbase:meta

		Table table = getHTableByTableName(tableName);

		Delete delete = new Delete(Bytes.toBytes("10004"));
		/*
		 * delete.deleteColumn(Bytes.toBytes("info"),//
		 * Bytes.toBytes("address"));
		 */
		//delete.deleteFamily(Bytes.toBytes("info")); //过时
		delete.addFamily(Bytes.toBytes("info"));

		table.delete(delete);

		table.close();
	}

	public static void main(String[] args) throws Exception {
		String tableName = "user"; // default.user / hbase:meta

		Table table = null;
		ResultScanner resultScanner = null;

		try {
			table = getHTableByTableName(tableName);

			Scan scan = new Scan();
			
// Range
			scan.setStartRow(Bytes.toBytes("10001"));
			scan.setStopRow(Bytes.toBytes("10003")) ;
			
//			Scan scan2 = new Scan(Bytes.toBytes("10001"),Bytes.toBytes("10003"));
			
			// PrefixFilter
			// PageFilter
//			scan.setFilter(filter) ;
			
			
//			scan.setCacheBlocks(cacheBlocks);
//			scan.setCaching(caching);
			
			
	//		scan.addColumn(family, qualifier)
	//		scan.addFamily(family)

			resultScanner = table.getScanner(scan);

			for (Result result : resultScanner) {
				System.out.println(Bytes.toString(result.getRow()));
//				System.out.println(result);
				for (Cell cell : result.rawCells()) {
					System.out.println(//
							Bytes.toString(CellUtil.cloneFamily(cell)) + ":" //
									+ Bytes.toString(CellUtil.cloneQualifier(cell)) + " ->" //
									+ Bytes.toString(CellUtil.cloneValue(cell)));
				}
				System.out.println("---------------------------------------");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeStream(resultScanner);
			IOUtils.closeStream(table);
		}

	}

}
