package ${rest.packageName};

import auto.grpc.${proto.protoName}.${proto.protoClass}Proto;
import auto.grpc.${proto.protoName}.${proto.protoClass}ServiceGrpc;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.Grpc;
import util.Json;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

/**
 * time: ${common.time}
 * ${common.name}
 * @author ${common.author}
 */
@Path("${rest.rootPath}")
public class ${rest.className} {
  public final static Logger Log = LogManager.getLogger();

  private ${proto.protoClass}ServiceGrpc.${proto.protoClass}ServiceBlockingStub stub() {
    return ${proto.protoClass}ServiceGrpc.newBlockingStub(Grpc.getInstance().getManagedChannel());
  }

  @POST
  @Path("add")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response add(Map<String, Object> m) {
    try {
      ${proto.protoClass}Proto.Info.Builder builder = ${proto.protoClass}Proto.Info.newBuilder();
      Json.objectToMessage(m, builder);
      return Response.ok(Json.toHashMap(stub().add(builder.build()))).build();
    } catch (Exception e) {
      Log.catching(e);
    }
    return Response.serverError().build();
  }

  @POST
  @Path("delete")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response del(Map<String, Object> m) {
    try {
      ${proto.protoClass}Proto.Id.Builder builder = ${proto.protoClass}Proto.Id.newBuilder();
      Json.objectToMessage(m, builder);
      return Response.ok(Json.toHashMap(stub().del(builder.build()))).build();
    } catch (Exception e) {
      Log.catching(e);
    }
    return Response.serverError().build();
  }

  @POST
  @Path("update")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response update(Map<String, Object> m) {
    try {
      ${proto.protoClass}Proto.Info.Builder builder = ${proto.protoClass}Proto.Info.newBuilder();
      Json.objectToMessage(m, builder);
      return Response.ok(Json.toHashMap(stub().update(builder.build()))).build();
    } catch (Exception e) {
      Log.catching(e);
    }
    return Response.serverError().build();
  }

  @POST
  @Path("find")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response find(Map<String, Object> m) {
    try {
      ${proto.protoClass}Proto.Search.Builder builder = ${proto.protoClass}Proto.Search.newBuilder();
      Json.objectToMessage(m, builder);
      return Response.ok(Json.toHashMap(stub().find(builder.build()))).build();
    } catch (Exception e) {
      Log.catching(e);
    }
    return Response.serverError().build();
  }

}
