import  frame.BrandViewFrame;
import frame.LaptopInputFrame;
import frame.LaptopViewFrame;
import helpers.Koneksi;

public class Main {
    public static void main(String[] args) {
//        Koneksi.getConnection();
//        BrandViewFrame viewFrame = new BrandViewFrame();
        LaptopViewFrame viewFrame = new LaptopViewFrame();
        //LaptopInputFrame viewFrame = new LaptopInputFrame();
        viewFrame.setVisible(true);
    }
}
