import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class test {
    public static void main(String[] args) {
        String key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnCDzUxnMVEWv2N1/GhQr9Op3L7wHF1OBae60uj04YxQvjRS5cP1NErGNFDjzOcl8sDQMNKz4oHSV8SE5eBi9gNiX7UiAY8lnn9Wx/UQKFwXoKH0MSyo7VNGFzFuDe9FNAnq7SthjkFTP8z/gb17SAF3nnZrRuLwb9YcBtYY/njejU90XQW1gL1wo2y72hZwjcXxkGcxrboHwYv/HwEzFHqnfsoeZEEifWPYhvlMNOfFIegrIvST+N0ILBfgrTnl8UB85Nj3o4s5JDlkvlhEJiT4eglD5e6tO6n6zUX1HBwExxxe5nHXyE85JmiHBCfrBwMr8jhSbkhYwzSnySQlvdwIDAQAB";
        String text = "0iBZJ5HHu9k8AUc3BqNBTdXijyNoTVBRMDdTnHCtAolCz/edi3erlrOoxKvKgmwVlzBqqkZz6+bigiuGJhgMVGKGvUmvwTjwn9kd+uWvqwP8Ft1Zx/Ci+BF7Mx5k4A8ehXHaL18G5kUsDBXVLmxIy2u+8e7VprYMTNd2lFDtrG6mT+c5US9ad0jf1qmzZ2baIcgecOEYJACzgUS4+95/tZFZn0E85LWqy7WoKw2gRH1Y+uAFRc2Lztyj40oGT7oa7UpakmyktZfJdUVWAQ4iVLVJpekiYbvb2JgPSIeRm+7viBFONHXgQxDj+9HdZXeZHAfDHKjFLeFgVeOn+o4kCA==";
        byte[] bytes = Base64.getDecoder().decode(key);
//        bytes[0] = 'M';
//        bytes[1] = 'a';
//        bytes[2] = 'n';
//        System.out.println(bytes);
        printByteArray(bytes);

        byte[] enbytes = Base64.getDecoder().decode(text);
        printByteArray(enbytes);
        System.out.println("text length: " + enbytes.length);

//        byte[] debytes = Base64.getDecoder().decode(enbytes);
//        printByteArray(debytes);
    }


    public static void printByteArray(byte[] bs) {
        for (int i = 0; i < bs.length; i ++) {
            System.out.print(byteToBit(bs[i]) + " ");
        }
        System.out.println();
    }

    public static String byteToBit(byte b) {
        return ""
                + (byte) ((b >> 7) & 0x1) + (byte) ((b >> 6) & 0x1)
                + (byte) ((b >> 5) & 0x1) + (byte) ((b >> 4) & 0x1)
                + (byte) ((b >> 3) & 0x1) + (byte) ((b >> 2) & 0x1)
                + (byte) ((b >> 1) & 0x1) + (byte) ((b >> 0) & 0x1);
    }
}
