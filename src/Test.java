import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class Test {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        PasswordGenerator passwordGenerator = new PasswordGenerator.PasswordGeneratorBuilder()
                .useDigits(true)
                .useLower(true)
                .useUpper(true)
                .build();
        String password = passwordGenerator.generate(4);
        //System.out.println(password);
        PasswordSha256 ps = new PasswordSha256();
        ps.hashing("asd");



    }
}
