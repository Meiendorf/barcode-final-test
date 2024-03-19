package com.example.barcodefinaltest.controller;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@RestController
public class TestController {

    @PostMapping("/decode-barcode")
    public String decodeBarcode(@RequestParam("barcodeImage") MultipartFile imageFile) {
        if (imageFile.isEmpty()) {
            return "No file selected";
        }
        try {
            String decodedData = decodeBarcode(imageFile.getInputStream());
            return decodedData != null ? decodedData : "Decoding failed or barcode not found.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error processing the image.";
        }
    }
    private String decodeBarcode(InputStream stream) throws IOException, NotFoundException {
        MultiFormatReader reader = new MultiFormatReader();
        BufferedImage image = ImageIO.read(stream);
        LuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Map<DecodeHintType, Object> hints = new HashMap<>();
        hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);

        Result result = reader.decode(bitmap, hints);

        return result.getText();
    }

}
