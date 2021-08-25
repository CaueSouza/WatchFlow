package com.example.watchflow;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class ImageUtil {
    private static final String TAG = ImageUtil.class.getSimpleName();

    public static String saveImage(Context context, String img64) throws IOException {

        try {
            byte[] imageBytes = Base64.decode(img64, Base64.DEFAULT);

            return saveImage(context, imageBytes, img64);
        } catch (IOException e) {
            throw e;
        } catch (Exception e){
            return null;
        }

    }

    private static String saveImage(Context context, byte[] imageBytes, String img64) throws IOException{

        try {
            File folder = new File(getDirectory(context));

            if (!folder.exists()){
                if (!folder.mkdirs()){
                    Log.w(TAG, "Cannot make dir");
                }
            }

            File imageFile = createBase64FileImage(context, imageBytes, img64);

            return imageFile != null ? imageFile.getAbsolutePath() : null;
        } catch (IOException e) {
            throw e;
        } catch (Exception e){
            return null;
        }

    }

    private static File createBase64FileImage(Context context, byte[] imageBytes, String img64) throws IOException{
        File file = new File(getDirectory(context) + "/image.jpeg");

        if (!file.exists() && !file.createNewFile()){
            return null;
        }

        if (img64 != null){
            byte[] sha256base64 = compareSHA256(img64, file);
            if (sha256base64 == null){
                return file;
            }

            storeSHA256File(sha256base64, file.getAbsolutePath());
        }

        FileOutputStream stream = null;

        try {
            stream = new FileOutputStream(file);
            stream.write(imageBytes);
        } finally {
            try {
                if (stream != null){
                    stream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return file;
    }

    private static void storeSHA256File(@NonNull byte[] sha256, String originalFileAbsPath){
        File sha256File = new File(originalFileAbsPath + ".sha256");

        try (OutputStream os = new FileOutputStream(sha256File)){
            os.write(sha256);
        } catch (FileNotFoundException e) {
            if (createFile(sha256File)){
                storeSHA256File(sha256, originalFileAbsPath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean createFile(File file){
        try {
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    private static String getDirectory(Context context){
        return context.getFilesDir().getPath() + "/" + "WATCH_FLOW";
    }

    private static byte[] compareSHA256(@NonNull String newFileContent, File originalFile){
        byte[] sha256base64 = generateSHA256(newFileContent);

        if (sha256base64 == null){
            return null;
        }

        if (!originalFile.exists() || !Arrays.equals(sha256base64, getSHA256forFile(originalFile))){
            return sha256base64;
        }

        return null;
    }

    private static byte[] getSHA256forFile(@NonNull File originalFile){
        File sha256File = new File(originalFile.getAbsolutePath() + ".sha256");

        if (!sha256File.exists()){
            return generateSHA256(readFileAsString(originalFile));
        }

        try {
            return readFileAsBytes(sha256File);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static byte[] generateSHA256(@NonNull String content){
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(content.getBytes(StandardCharsets.UTF_8));
            return messageDigest.digest();
        }catch (NoSuchAlgorithmException e){
            return null;
        }
    }

    private static String readFileAsString(@NonNull File file){
        StringBuilder builder = new StringBuilder();
        boolean firstLine = true;
        String line;

        try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))){
            while ((line = in.readLine()) != null){
                if (firstLine){
                    builder.append(line);
                    firstLine = false;
                }
                else {
                    builder.append("\n").append(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return builder.toString();
    }

    private static byte[] readFileAsBytes(File file) throws IOException{
        if (file.length() > Integer.MAX_VALUE){
            throw new IOException("File too large");
        }

        try (FileInputStream in = new FileInputStream(file)){
            byte[] fileBytes = new byte[(int) file.length()];
            if (in.read(fileBytes) == 0){
                return null;
            }

            return fileBytes;
        } catch (IOException e){
            e.printStackTrace();
        }

        return null;
    }
}
