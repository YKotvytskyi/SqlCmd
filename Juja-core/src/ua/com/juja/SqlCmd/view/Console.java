package ua.com.juja.SqlCmd.view;

import java.util.Scanner;

public class Console implements View {
    @Override
    public String read() {
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }

    @Override
    public void write(String message) {
        System.out.println(message);
    }
}
