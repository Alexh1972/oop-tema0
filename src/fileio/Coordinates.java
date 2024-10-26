package fileio;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public final class Coordinates {
   private int x, y;
   private ObjectMapper objectMapper = new ObjectMapper();

   public Coordinates() {
   }

   public ObjectNode toObjectNode() {
      ObjectNode objectNodeCoord = objectMapper.createObjectNode();

      objectNodeCoord.put("x", x);
      objectNodeCoord.put("y", y);

      return objectNodeCoord;
   }

   public int getX() {
      return x;
   }

   public void setX(final int x) {
      this.x = x;
   }

   public int getY() {
      return y;
   }

   public void setY(final int y) {
      this.y = y;
   }

   @Override
   public String toString() {
      return "Coordinates{"
              + "x="
              + x
              + ", y="
              + y
              + '}';
   }
}
