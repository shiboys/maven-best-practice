package com.swj.ics.canal;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.google.protobuf.InvalidProtocolBufferException;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2022/08/19 21:41
 */
public class CanalClientDemo {
  public static void main(String[] args) {
    CanalConnector canalConnector =
        CanalConnectors.newSingleConnector(new InetSocketAddress("192.168.0.103", 11111), "example", "", "");
    int batchSize = 1000;
    int emptyCount = 0;
    try {
      canalConnector.connect();
      canalConnector.subscribe("metadata\\..*");
      canalConnector.rollback();
      int emptyTotalCount = 120;
      while (emptyCount < emptyTotalCount) {
        Message message = canalConnector.getWithoutAck(batchSize);
        int msgSize = message.getEntries().size();
        long batchId = message.getId();
        if (message.getId() == -1 || msgSize == 0) {
          emptyCount++;
          try {
            Thread.sleep(1000);
          } catch (InterruptedException e) {

          }
        } else {
          emptyCount = 0;
          printMsgEntry(message.getEntries());
        }
        // 提交确认
        canalConnector.ack(batchId);
        // 处理失败，回滚数据
        // canalConnector.rollback(batchId);
      }
      System.out.println("empty total counter is expire and exit.");
    } finally {
      canalConnector.disconnect();
    }
  }

  /**
   * EntryProtocol.proto (https://github.com/alibaba/canal/blob/master/protocol/src/main/java/com/alibaba/otter/canal/protocol/EntryProtocol.proto)
   * Entry
   * Header
   * logfileName [binlog文件名]
   * logfileOffset [binlog position]
   * executeTime [binlog里记录变更发生的时间戳,精确到秒]
   * schemaName
   * tableName
   * eventType [insert/update/delete类型]
   * entryType   [事务头BEGIN/事务尾END/数据ROWDATA]
   * storeValue  [byte数据,可展开，对应的类型为RowChange]
   * RowChange
   * <p>
   * isDdl       [是否是ddl变更操作，比如create table/drop table]
   * <p>
   * sql         [具体的ddl sql]
   * <p>
   * rowDatas    [具体insert/update/delete的变更数据，可为多条，1个binlog event事件可对应多条变更，比如批处理]
   * <p>
   * beforeColumns [Column类型的数组，变更前的数据字段]
   * <p>
   * afterColumns [Column类型的数组，变更后的数据字段]
   * <p>
   * <p>
   * Column
   * <p>
   * index
   * <p>
   * sqlType     [jdbc type]
   * <p>
   * name        [column name]
   * <p>
   * isKey       [是否为主键]
   * <p>
   * updated     [是否发生过变更]
   * <p>
   * isNull      [值是否为null]
   * <p>
   * value       [具体的内容，注意为string文本]
   */
  private static void printMsgEntry(List<CanalEntry.Entry> entries) {
    for (CanalEntry.Entry entry : entries) {
      CanalEntry.EntryType entryType = entry.getEntryType();
      if (entryType == CanalEntry.EntryType.TRANSACTIONBEGIN || entryType == CanalEntry.EntryType.TRANSACTIONEND) {
        continue;
      }
      CanalEntry.RowChange rowChange = null;
      try {
        rowChange = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
      } catch (InvalidProtocolBufferException e) {
        throw new RuntimeException("ERROR ## parser of binlog has an error.data=" + entry.toString(), e);
      }
      CanalEntry.EventType eventType = rowChange.getEventType();
      System.out.println(String.format("=============> binlog[%s:%s] , name[%s:%s], eventType:%s",
          entry.getHeader().getLogfileName(), entry.getHeader().getLogfileOffset(),
          entry.getHeader().getSchemaName(), entry.getHeader().getTableName(), eventType));
      for (CanalEntry.RowData rowData : rowChange.getRowDatasList()) {
        if (eventType == CanalEntry.EventType.DELETE) {
          printColumns(rowData.getBeforeColumnsList());
        } else if (eventType == CanalEntry.EventType.INSERT) {
          printColumns(rowData.getAfterColumnsList());
        } else {
          System.out.println("=========> before");
          printColumns(rowData.getBeforeColumnsList());
          System.out.println("===========> after");
          printColumns(rowData.getAfterColumnsList());
        }
      }
    }
  }

  private static void printColumns(List<CanalEntry.Column> columns) {
    for (CanalEntry.Column column : columns) {
      System.out.println(column.getName() + ":" + column.getValue() + ", update=" + column.getUpdated());
    }
  }
}
