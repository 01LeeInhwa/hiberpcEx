package shop.mtcoding.hiberpc.model.board;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;

import org.assertj.core.api.Assertions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import shop.mtcoding.hiberpc.config.dummy.MyDummyEntity;
import shop.mtcoding.hiberpc.model.Board.Board;
import shop.mtcoding.hiberpc.model.Board.BoardRepository;
import shop.mtcoding.hiberpc.model.User.User;
import shop.mtcoding.hiberpc.model.User.UserRepository;


@Import({UserRepository.class,BoardRepository.class})
@DataJpaTest
public class BoardRepositoryTest extends MyDummyEntity {
   
    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager em;

    @BeforeEach
    public void setUp(){
        em.createNativeQuery("ALTER TABLE user_tb ALTER COLUMN id RESTART WITH 1").executeUpdate();
        em.createNativeQuery("ALTER TABLE board_tb ALTER COLUMN id RESTART WITH 1").executeUpdate();
    }

    @Test
    public void save_test(){

        // given 1
       User user = newUser("ssar");
       User userPS = userRepository.save(user);
                
       // given 2
       Board board = newBoard("제목1", userPS);
        // when
        Board boardPS = boardRepository.save(board);
        System.out.println("테스트 : " + boardPS);

        // then
        assertThat(boardPS.getId()).isEqualTo(1);
        assertThat(boardPS.getUser().getId()).isEqualTo(1);
    }
    @Test
    public void update_test(){

        // given 1 - DB에 영속화
       User user = newUser("ssar");
       User userPS = userRepository.save(user);
       Board board = newBoard("제목1", userPS);
       Board boardPS = boardRepository.save(board);

         // given 2 - request 데이터
        String title = "제목12";
        String content = "내용12";
       

        // when
        boardPS.update(title, content);
        em.flush(); // 트랜잭션 종료 시 자동 발동됨
                        
        // then
        Board findBoardPS = boardRepository.findById(1);
        assertThat(findBoardPS.getContent()).isEqualTo("내용12");
    }
    
    @Test
    public void delete_test(){

        // given 1 - DB에 영속화
        User user = newUser("ssar");
        User userPS = userRepository.save(user);
        Board board = newBoard("제목1", userPS);
        Board boardPS = boardRepository.save(board);

       // given 2 - request 데이터
        // em.clear();
        int id = 1;
        Board findBoardPS = boardRepository.findById(id);

        // when
        boardRepository.delete(findBoardPS);
    
                        
        // then
        Board deleteBoardPS = boardRepository.findById(1);
        Assertions.assertThat(deleteBoardPS).isNull();
    }

    @Test
    public void findById_test(){
        // given 1 - DB에 영속화
        User user = newUser("ssar");
        User userPS = userRepository.save(user);
        Board board = newBoard("제목1", userPS);
        Board boardPS = boardRepository.save(board);

       // given 2
       int id = 1;

       // when
       Board findBoardPS = boardRepository.findById(id);

       // then
       assertThat(findBoardPS.getUser().getUsername()).isEqualTo("ssar");
       assertThat(findBoardPS.getTitle()).isEqualTo("제목1");

    }
    
    @Test
    public void findByAll_test(){
        User user = newUser("ssar");
        User userPS = userRepository.save(user);
        List<Board> boardList = Arrays.asList(newBoard("ssar", userPS),newBoard("cos",userPS));
        boardList.stream().forEach((board)->{
            boardRepository.save(board);
        });

        // when
        List<Board> boardListPS = boardRepository.findAll();
        // System.out.println("테스트 : " + userListPS);

        // then
        assertThat(boardListPS.size()).isEqualTo(2);
    }


}
