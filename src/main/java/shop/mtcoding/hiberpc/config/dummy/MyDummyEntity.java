package shop.mtcoding.hiberpc.config.dummy;

import shop.mtcoding.hiberpc.model.Board.Board;
import shop.mtcoding.hiberpc.model.User.User;

public class MyDummyEntity {

    protected User newUser(String username) {
        return User.builder()
                .username(username)
                .password("1234")
                .email(username + "@nate.com")
                .build();

    }


    protected Board newBoard(String title, User userPS) { // user DB에서 셀렉트해서 넣어줘야함
       
        if (userPS.getId() == null) {
            System.out.println("영속화해서 넣어라!");
            return null;
        }

        return Board.builder()
                .title(title)
                .content(title)
                .user(userPS)
                .build();

    }
}