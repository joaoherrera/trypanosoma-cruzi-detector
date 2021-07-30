package Main_Package;

/**
 * @date 10/03/2012
 * @author joao
 */
import Adaptacao.Cell;
import java.awt.image.BufferedImage;
import java.awt.image.renderable.ParameterBlock;
import javax.media.jai.Histogram;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;

public class ImageTools{
    
    private DataStructure imageDataStructure;
    private ImageProcessing imageProcessing;
    private ImageData imageData;
    private Histogram imageHistograma;
    private PlanarImage planarImage; 
    
    int largura;

    int[] bins = {256};
    double[] low = {0.0};
    double[] high = {256.0};
    
    public ImageTools(BufferedImage imageBuffer){
        this.imageDataStructure = new DataStructure(imageBuffer);
        this.imageProcessing    = new ImageProcessing(this.imageDataStructure); 
        this.imageData          = new ImageData();
    }
    
    public ImageTools(){
 
    }
    
    /*  Livrando-se da exceção: java.lang.NoClassDefFoundError: com/sun/medialib/mlib/Image
     *  Consultada em: "http://www.java.net/node/666373"
     *  @date 10/23/12
     */
    static{ 
        System.setProperty("com.sun.media.jai.disableMediaLib", "true");
    }
    
    //Determinando as propriedades dos Histogramas usados
    public Histogram createHistogram(String path){
        planarImage = JAI.create("fileload", path);
        ParameterBlock parameter = new ParameterBlock();
        parameter.addSource(planarImage);
        parameter.add(null);
        parameter.add(1); parameter.add(1);
        parameter.add(bins);
        parameter.add(low); 
        parameter.add(high);
        PlanarImage dummyImage1 = JAI.create("histogram",parameter);
        imageHistograma = (Histogram) dummyImage1.getProperty("histogram");
        return imageHistograma;
    }
    
    public BufferedImage zoomIn(int scale){
        return null;
    }
    
    public ImageProcessing getImageProcessing(){
        return this.imageProcessing;
    }

    public DataStructure getImageDataStructure() {
        return imageDataStructure;
    }

    public ImageData getImageData() {
        return imageData;
    }

}
