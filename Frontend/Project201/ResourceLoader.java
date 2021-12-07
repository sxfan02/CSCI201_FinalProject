package Project201;
import java.net.URI;
import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;

public class ResourceLoader
{
    static ResourceLoader resourcel = new ResourceLoader();

   public static Image loadImage(String imageName)
   {
      URI uri;
	  String stringForUri = "";
      try
      {
          uri =  resourcel.getClass().getResource("images/"+imageName).toURI();
          stringForUri = uri.toString();
      }
      catch(Exception e) { System.out.println("image load error e="+e); }
      return new Image(stringForUri);
   }
}
