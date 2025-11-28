package org.autosalon;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        System.out.println(new BCryptPasswordEncoder().matches(
                "user123",
                "$2a$12$swUt2JyiiCWszichQC1nvu25b.rBmZxldHzzln2QwVG.gB5tb83MC"
        ));
    }
}