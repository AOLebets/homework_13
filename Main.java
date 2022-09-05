package org.example;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Homework13 test = new Homework13();

        String jsonForTestPosting = test.jsonMaker(null, "sam", "sam", "email", "street",
                "suite", "city", "zip", "langtitude", "longtitude",
                "phone", "website", "company name", "yahhoo", "bs");
        String jsonForTestUpdating = test.jsonMaker("9", "sam", "sam", "email", "street",
                "suite", "city", "zip", "langtitude", "longtitude",
                "phone", "website", "company name", "yahhoo", "bs");

        System.out.println(test.getAllUsersInfo());
        System.out.println(test.getUserInfoByID(2));
        System.out.println(test.getInfoByUsername("Samantha"));
        test.allCommentsToLastPostOfUserByID(3);
        test.create(jsonForTestPosting);
        test.update(jsonForTestUpdating);
        test.delete(5);
        test.allUnfinishedTodosForUser(7);

    }
}