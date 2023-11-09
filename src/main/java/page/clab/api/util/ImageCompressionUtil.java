package page.clab.api.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

@Component
public class ImageCompressionUtil {

    private static String imageUrl;
    private static String imageDirectory;

    @Value("${resource.file.url}")
    public void setImageUrl(String imageUrl) {
        ImageCompressionUtil.imageUrl = imageUrl;
    }

    @Value("${resource.file.path}")
    public void setImageDirectory(String imageDirectory) {
        ImageCompressionUtil.imageDirectory = imageDirectory;
    }

    public static void compressImage(String filePath, float quality) {
        try {
            String inputImagePath = imageDirectory + extractFilePath(filePath);
            System.out.println(inputImagePath);
            File inputFile = new File(inputImagePath);
            BufferedImage image = ImageIO.read(inputFile);

            OutputStream os = new FileOutputStream(inputFile);

            String formatName = getFileExtension(inputFile.getName());
            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(formatName);
            ImageWriter writer = writers.next();

            ImageWriteParam param = writer.getDefaultWriteParam();

            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(quality);

            ImageIO.write(image, formatName, os);

            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String extractFilePath(String imagePath) {
        if (imagePath.startsWith(imageUrl)) {
            return imagePath.substring(imageUrl.length());
        } else {
            return imagePath;
        }
    }

    private static String getFileExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < filename.length() - 1) {
            return filename.substring(dotIndex + 1).toLowerCase();
        }
        return "";
    }

}
