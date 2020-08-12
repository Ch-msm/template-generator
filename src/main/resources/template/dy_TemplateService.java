package ${java.packageName};

import auto.grpc.${proto.protoName}.${proto.protoClass}Proto;
import auto.grpc.${proto.protoName}.${proto.protoClass}ServiceGrpc;
import com.rethinkdb.gen.ast.Filter;
import com.rethinkdb.net.Connection;
import com.rethinkdb.net.Cursor;
import io.grpc.stub.StreamObserver;
import service.Constant;
import util.Json;
import util.RethinkDb;
import util.log.Log;

import java.util.Map;
import java.util.UUID;

import static com.rethinkdb.RethinkDB.r;

/**
 * time: ${common.time}
 * ${common.name}
 * @author ${common.author}
 */
public class ${java.className} extends ${proto.protoClass}ServiceGrpc.${proto.protoClass}ServiceImplBase {
  private static final RethinkDb RD = RethinkDb.getInstance();
  private static final util.log.Log L = new Log("${common.name}");
  public static final String TABLE_NAME = "${java.tableName}";

  public ${java.className}() {
    try (Connection rc = RD.connect(Constant.RETHINKDB_DB)){
    r.db(Constant.RETHINKDB_DB).tableCreate(TABLE_NAME).run(rc);
    } catch (Exception e) {
    //忽略
    }
  }


  private ${proto.protoClass}Proto.Info mapToProto(Map<String, Object> map) {
    ${proto.protoClass}Proto.Info.Builder info = ${proto.protoClass}Proto.Info.newBuilder();
    Json.objectToMessage(map, info);
    return info.build();
  }

  /**
   * <pre>
   * 新增
   * </pre>
   */
  @Override
  public void add(${proto.protoClass}Proto.Info request, StreamObserver<${proto.protoClass}Proto.Empty> responseObserver) {
    L.start("新增:" + Json.toJson(request));
    try (Connection rc = RD.connect(Constant.RETHINKDB_DB)) {
      ${proto.protoClass}Proto.Info.Builder builder = request.toBuilder();
      builder.setId(UUID.randomUUID().toString());
      r.table(TABLE_NAME).insert(Json.toHashMap(builder)).run(rc);
      responseObserver.onNext(${proto.protoClass}Proto.Empty.getDefaultInstance());
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
      e.printStackTrace();
      L.end("失败");
    }
  }

  /**
   * <pre>
   * 删除
   * </pre>
   */
  @Override
  public void del(${proto.protoClass}Proto.Id request, StreamObserver<${proto.protoClass}Proto.Empty> responseObserver) {
    L.start("删除:" + Json.toJson(request));
    try (Connection rc = RD.connect(Constant.RETHINKDB_DB)) {
      r.table(TABLE_NAME).get(request.getId()).delete().run(rc);
      responseObserver.onNext(${proto.protoClass}Proto.Empty.getDefaultInstance());
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
      e.printStackTrace();
      L.end("失败");
    }
  }

  /**
   * <pre>
   * 修改
   * </pre>
   */
  @Override
  public void update(${proto.protoClass}Proto.Info request, StreamObserver<${proto.protoClass}Proto.Empty> responseObserver) {
    L.start("修改:" + Json.toJson(request));
    try (Connection rc = RD.connect(Constant.RETHINKDB_DB)) {
      r.table(TABLE_NAME).get(request.getId()).update(Json.toHashMap(request)).run(rc);
      responseObserver.onNext(${proto.protoClass}Proto.Empty.getDefaultInstance());
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
      e.printStackTrace();
      L.end("失败");
    }
  }

  /**
   * <pre>
   * 查询
   * </pre>
   */
  @Override
  public void find(${proto.protoClass}Proto.Search request, StreamObserver<${proto.protoClass}Proto.List> responseObserver) {
    L.start("查询:" + Json.toJson(request));
    try (Connection rc = RD.connect(Constant.RETHINKDB_DB)) {
      Filter filter = r.table(TABLE_NAME).filter(row -> row.g("id").eq("").not());
      if (!request.getKeyword().isEmpty()) {
        filter = filter.filter(row -> row.toJson().match(request.getKeyword()));
      }
      ${proto.protoClass}Proto.List.Builder builder = ${proto.protoClass}Proto.List.newBuilder();
      int count = request.getCount(), page = request.getPage();
      Cursor<Map<String, Object>> cursor = count > 0 ?
          filter.skip((page - 1) * count).limit(count).run(rc)
          : filter.run(rc);
      builder.setCount(filter.count().run(rc));
      while (cursor.hasNext()) {
        builder.addInfos(mapToProto(cursor.next()));
      }
      responseObserver.onNext(builder.build());
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
      e.printStackTrace();
      L.end("失败");
    }
  }

  /**
   * <pre>
   * 导出
   * </pre>
   */
  @Override
  public void export(${proto.protoClass}Proto.Search request, StreamObserver<${proto.protoClass}Proto.Id> responseObserver) {
    try (Connection rc = RD.connect(Constant.RETHINKDB_DB)) {


      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
      e.printStackTrace();
    }
  }
}
