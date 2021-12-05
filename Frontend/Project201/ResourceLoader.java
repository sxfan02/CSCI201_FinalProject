package Project201;
import java.net.URI;
import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;

public class ResourceLoader
{
    static ResourceLoader rl = new ResourceLoader();

   public static Image loadImage(String imageName)
   {
      URI uri;
	  String uriString = "";
      try
      {
          uri =  rl.getClass().getResource("images/"+imageName).toURI();
          uriString = uri.toString();
      }
      catch(Exception e) { System.out.println("image load error e="+e); }
      return new Image(uriString);
   }


}
