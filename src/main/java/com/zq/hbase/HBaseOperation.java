package com.zq.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.FirstKeyOnlyFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;

import net.sf.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangqiang on 2017/3/13.
 */
public class HBaseOperation {

    private static Logger log = Logger.getLogger(HBaseOperation.class);
    private static Configuration configuration;

    static {
        configuration = HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.property.clientPort", "2181");
        configuration.set("hbase.zookeeper.quorum", "s12181,s12191,s12192");
    }

    public static Connection getHBaseConn() throws IOException {
        Connection connection = ConnectionFactory.createConnection(configuration);
        return connection;
    }

    public static void releaseConnection(Connection connection) {
        try {
            if (connection != null)
                connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改表结构,增加列族
     *
     * @param tableName 表名
     * @param family    列族
     */
    public static void putFamily(String tableName, String family) {
        Connection connection = null;
        try {
            connection = getHBaseConn();
            Admin admin = connection.getAdmin();
            TableName tblName = TableName.valueOf(tableName);
            if (admin.tableExists(tblName)) {
                admin.disableTable(tblName);
                HColumnDescriptor cf = new HColumnDescriptor(family);
                admin.addColumn(TableName.valueOf(tableName), cf);
                admin.enableTable(tblName);
            } else {
                log.warn(tableName + " not exist.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            releaseConnection(connection);
        }
    }

    /**
     * 删除表
     *
     * @param tableName 表名
     */
    public static void dropTable(String tableName) {
        Connection connection = null;
        try {
            connection = getHBaseConn();
            Admin admin = connection.getAdmin();
            TableName table = TableName.valueOf(tableName);
            if (admin.tableExists(table)) {
                admin.disableTable(table);
                admin.deleteTable(table);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            releaseConnection(connection);
        }
    }

    /**
     * 统计行数
     *
     * @param tableName 表名
     * @return 行数
     */
    public static long count(String tableName) {
        final long[] rowCount = {0};
        Connection connection = null;
        try {
            connection = ConnectionFactory.createConnection(configuration);
            Table table = connection.getTable(TableName.valueOf(tableName));
            Scan scan = new Scan();
            scan.setFilter(new FirstKeyOnlyFilter());
            ResultScanner resultScanner = table.getScanner(scan);
            resultScanner.forEach(result -> rowCount[0] += result.size());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            releaseConnection(connection);
        }
        System.out.println("行数: " + rowCount[0]);
        return rowCount[0];
    }

    /**
     * 删除指定数据
     * <p>
     * columns为空, 删除指定列族的全部数据;
     * family为空时, 删除指定行键的全部数据;
     * </p>
     *
     * @param tableName 表名
     * @param rowKey    行键
     * @param family    列族
     * @param columns   列集合
     */
    public static void deleteData(String tableName, String rowKey, String family, String[] columns) {
        Connection connection = null;
        try {
            connection = getHBaseConn();
            Table table = connection.getTable(TableName.valueOf(tableName));
            Delete delete = new Delete(Bytes.toBytes(rowKey));
            if (null != family && !"".equals(family)) {
                if (null != columns && columns.length > 0) { // 删除指定列
                    for (String column : columns) {
                        delete.addColumn(Bytes.toBytes(family), Bytes.toBytes(column));
                    }
                } else { // 删除指定列族
                    delete.addFamily(Bytes.toBytes(family));
                }
            } else { // 删除指定行
                // empty, nothing to do
            }
            table.delete(delete);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            releaseConnection(connection);
        }
    }

    /**
     * 获取指定数据
     * <p>
     * column为空, 检索指定列族的全部数据;
     * family为空时, 检索指定行键的全部数据;
     * </p>
     *
     * @param tableName 表名
     * @param rowKey    行键
     * @param family    列族
     * @param columns   列名集合
     */
    public static Result getData(String tableName, String rowKey, String family, String[] columns) {
        Connection connection = null;
        try {
            connection = getHBaseConn();
            Table table = connection.getTable(TableName.valueOf(tableName));
            Get get = new Get(Bytes.toBytes(rowKey));
            Result result = table.get(get);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            releaseConnection(connection);
        }
        return null;
    }

    /**
     * 插入数据
     *
     * @param tableName 表名
     * @param rowKey    行键
     * @param familys   列族信息(Key: 列族; value: (列名, 列值))
     */
    public static void putData(String tableName, String rowKey, Map<String, Map<String, String>> familys) {
        Connection connection = null;
        try {
            connection = getHBaseConn();
            Table table = connection.getTable(TableName.valueOf(tableName));
            Put put = new Put(Bytes.toBytes(rowKey));

            for (Map.Entry<String, Map<String, String>> family : familys.entrySet()) {
                for (Map.Entry<String, String> column : family.getValue().entrySet()) {
                    put.addColumn(Bytes.toBytes(family.getKey()),
                            Bytes.toBytes(column.getKey()), Bytes.toBytes(column.getValue()));
                }
            }
            table.put(put);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            releaseConnection(connection);
        }
    }

    /**
     * 全表扫描
     *
     * @param tableName
     * @param startRowKey
     * @param stopRowKey
     * @return
     */
    public static JSONArray scan(String tableName, String startRowKey, String stopRowKey) {
        Connection connection = null;
        List<Map<String, String>> resultsList = null;
        try {
            connection = getHBaseConn();
            Table table = connection.getTable(TableName.valueOf(tableName));
            Scan scan = new Scan();
            if (!"".equals(startRowKey) && startRowKey != null)
                scan.setStartRow(Bytes.toBytes(startRowKey));
            if (!"".equals(stopRowKey) && stopRowKey != null)
                scan.setStopRow(Bytes.toBytes(stopRowKey));
            ResultScanner resultScanner = table.getScanner(scan);
            resultsList = new ArrayList();
            for (Result result : resultScanner) {
                Map<String, String> map = new HashMap();
                map.put("userId", Bytes.toString(result.getRow()));
                for (Cell cell : result.rawCells()) {
                    map.put(Bytes.toString(CellUtil.cloneQualifier(cell)), Bytes.toString(result.getValue(Bytes.toBytes("result"), CellUtil.cloneQualifier(cell))));
                }
                resultsList.add(map);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            releaseConnection(connection);
        }
        return JSONArray.fromObject(resultsList);
    }

    public static void main(String[] args) {
        scan("home_visit_reality_prediction", null, null);
    }
}
