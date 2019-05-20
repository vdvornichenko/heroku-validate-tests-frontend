package project;

import com.google.gson.*;
import org.apache.axis2.databinding.types.soapencoding.Array;
import project.Rules.sObjectRule;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


 public class TaskMappingConverter  {
//     @Override
//    public JsonElement serialize(sObjectRule src, Type typeOfSrc, JsonSerializationContext context) {
////        JsonObject object = new JsonObject();
////        object.addProperty("CLASSNAME", src.getClass().getName());
////        object.add(DATA, JsonSerializationContext.class.);
//        return null;
//    }
////
////
//    public TaskMapping deserialize(JsonElement json, Type typeOfT,
//                              JsonDeserializationContext context) {
//        TaskMapping taskMap = new TaskMapping();
//        System.out.println(json);
//        if(json.isJsonNull()) return null;
//        else if(json.isJsonArray()) return handleArray(json.getAsJsonArray(), context);
//
//            return taskMap;
//
////        if(json.isJsonNull()) return null;
////        else if(json.isJsonPrimitive()) return handlePrimitive(json.getAsJsonPrimitive());
////        else if(json.isJsonArray()) return handleArray(json.getAsJsonArray(), context);
////        else return handleObject(json.getAsJsonObject(), context);
//    }
//
//    private TaskMapping handleArray(JsonArray json, JsonDeserializationContext context) {
//        System.out.println(json.size());
//        System.out.println(json);
////        TaskMapping[] array = new Object[json.size()];
////        for(int i = 0; i < array.length; i++)
////            array[i] = context.deserialize(json.get(i), Object.class);
//        return new TaskMapping();
//    }
//    private Object handleObject(JsonObject json, JsonDeserializationContext context) {
//        Map<String, Object> map = new HashMap<String, Object>();
//        for(Map.Entry<String, JsonElement> entry : json.entrySet())
//            map.put(entry.getKey(), context.deserialize(entry.getValue(), Object.class));
//        return map;
//    }
}


