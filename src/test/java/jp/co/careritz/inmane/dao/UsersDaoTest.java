package jp.co.careritz.inmane.dao;

import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import jp.co.careritz.inmane.dto.UsersDto;
import jp.co.careritz.inmane.service.UsersService;



@ExtendWith(SpringExtension.class)
@RunWith(SpringRunner.class)
@SpringBootTest


/**
 * Mockとは、簡単に言うとクラスの動作をシミュレートするためのオブジェクトです。 テスト対象クラスが呼び出している（＝依存している）クラスをMockで差し替え、
 * Mockの動作内容を定義することで、望むテスト条件を容易に作ることができます。
 */
class UsersDaoTest {



  @Mock
  UsersService usersService = new UsersService();

  // モック化するクラスのインスタンス（テスト対象）
  @InjectMocks
  private UsersDao usersDao;

  // this(dao)を初期化します。


  @Before
  public void beforeeach() {
    // 事前に検索対象となるユーザを追加
  }

  public void after() {
    // テスト実施後に追加されたユーザを削除
  }

  /**
   * カバレッジ網羅率 if文のtrueとfalseを両方テストする。<br>
   * UsersDtoに任意にいれた値(期待値) と <br>
   * usersDao.selectOneの固定の値があっているか確認？<br>
   * 両方うまくとれていたら＝になり成功する？<br>
   */
  @ParameterizedTest
  @ValueSource(ints = {1, 2, 100}) // それぞれの値をいれて実行される・引数１個のみ
  public void selectOneTest(int i) {
    // UsersDao dao = Mockito.mock(UsersDao.class);
    // UsersDto dto = new UsersDto();
    //
    // // ここでモックを作成しています。実際のDbにはアクセスしない。
    // Mockito.when(usersService.findByPk(String.valueOf(i))).thenReturn(dto);
    // Mockito.when(dao.selectOne(String.valueOf(i))).thenReturn(dto);
    //
    // UsersDto resultDto = usersService.findByPk(String.valueOf(i));
    // assertEquals(dto.getUserId(), is(resultDto.getUserId()));
    // assertNotEquals(dto.getUserId(), is(resultDto.getUserId()));
    // assertThat(dto.getUserId(), is(resultDto.getUserId()));
    //


    // selectOneのてすと
    UsersDto dto = new UsersDto();
    // 期待値のセット？
    dto.setUserId("1");
    dto.setUserName("てきとう");

    // UsersDaoの値を取得？
    UsersDto resultDto = usersDao.selectOne("1");

    // 両方を比較
    assertEquals(dto.getUserId(), resultDto.getUserId());
    // assertEquals(dto.getUserName(), resultDto.getUserName());
  }


  class SelectTest {

    @ParameterizedTest
    @MethodSource("testprovider")
    public void select(final UsersDto dto) {

      // UsersDto inputDto = new UsersDto();
      //
      // // Dtoに格納 inputDto.setUserId(inputUserId);
      // inputDto.setUserName("inputUserName");
      // inputDto.setRoleName("inputRoleName");
      // resultDto.setUserId("outUserId");
      // resultList.add(resultDto);
      // Mockito.when(usersDao.select(inputDto)).thenReturn(resultList);
      //
      // StringBuilder sqlbuilder = new StringBuilder();
      // sqlbuilder.append("SELECT * FROM USERS ");
      // sqlbuilder.append(" WHERE USER_ID LIKE ? ");
      // sqlbuilder.append(" AND USER_NAME LIKE ? ");
      // sqlbuilder.append(" AND ROLE_NAME LIKE ? ");
      //
      //
      // Object expected;
      // when(mockJdbc.query(eq(sqlbuilder), any(RowMapper.class))).thenReturn(expected);
      // List<UsersDto> returnList = usersDao.select(inputDto);
      //
      // // 取得した値を比較
      // assertThat(returnList.get(0), is(resultList.get(0)));


      // selectOneのてすと
      UsersDto dto1 = new UsersDto();
      // 期待値のセット？
      dto1.setUserId("1");
      dto1.setUserName("てきとう");

      // // UsersDaoの値を取得？
      UsersDto serchDto = new UsersDto();
      serchDto.setUserName("");
      List<UsersDto> resultList = usersDao.select(serchDto);


    }

    @Test
    public void test_isEmpty() {
      UsersDto dto = new UsersDto();
      UsersDto usersDto = new UsersDto();

      // isEmptyStr(dto.getUserId())の値のテスト
      // dto.setUserId("");
      usersDto.setUserId("");
      List<UsersDto> resultListId = usersDao.select(usersDto);

      // dto.setUserId("1");
      usersDto.setUserId("1");
      List<UsersDto> resultListEmpId = usersDao.select(usersDto);


      // isEmptyStr(dto.getUserName())の値のテス
      // dto.setUserName("");
      usersDto.setUserName("");
      List<UsersDto> resultListName = usersDao.select(usersDto);

      // dto.setUserName("AAAAA");
      usersDto.setUserName("AAAAA");
      List<UsersDto> resultListEmpName = usersDao.select(usersDto);


      // isEmptyStr(dto.getUserId())の値のテスト
      // dto.setRoleName("");
      usersDto.setRoleName("");
      List<UsersDto> resultListRoleName = usersDao.select(usersDto);

      // dto.setRoleName("AAAAA");
      usersDto.setRoleName("AAAAA");
      List<UsersDto> resultListEmpRoleName = usersDao.select(usersDto);

    }
  }

  class UpdateTest {
    @Test
    public void testUpdate() {
      UsersDto dto = new UsersDto();
      UsersDto usersDto = new UsersDto();


      dto.setPassword("");
      int resultListPass = usersDao.update(dto);
      usersDto.setPassword("aaaa");
      int resultListEmpPass = usersDao.update(usersDto);


      dto.setUserName("");
      int resultListName = usersDao.update(dto);
      usersDto.setUserName("aaaa");
      int resultListEmpName = usersDao.update(usersDto);

      dto.setRoleName("");
      int resultListRoleName = usersDao.update(dto);
      usersDto.setRoleName("aaaa");
      int resultListEmpRoleName = usersDao.update(usersDto);



    }
  }

  @ParameterizedTest
  @MethodSource("testProvider")
  void sumTest(int a, int b, int c) {
    System.out.println(a + ":" + b + ":" + c);
    assertEquals(c, a + b);
  }



  static Stream<Arguments> testProvider() {
    return Arrays.stream(new Arguments[] {Arguments.of(1, 2, 3), Arguments.of(4, 5, 9)});
  }

}
