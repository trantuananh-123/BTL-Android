package tran.tuananh.btl.Util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class FnCommon {

    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

    public static String generateUId() {
        LocalDateTime currentTime = null;
        String randomString = "";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            currentTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            String timeString = currentTime.format(formatter);
            randomString = generateRandomString(timeString, 10);
        }
        return randomString;
    }

    private static String generateRandomString(String seed, int length) {
        StringBuilder sb = new StringBuilder();

        long seedNumber = Long.parseLong(seed);
        Random random = new Random(seedNumber);

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(randomIndex));
        }

        return sb.toString();
    }
}
