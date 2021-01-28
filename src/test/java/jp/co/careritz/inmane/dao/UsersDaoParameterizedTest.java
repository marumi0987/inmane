package jp.co.careritz.inmane.dao;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;
import static org.junit.jupiter.params.provider.Arguments.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import jp.co.careritz.inmane.dto.UsersDto;
import jp.co.careritz.inmane.service.UsersService;



/**
 * Mockとは、簡単に言うとクラスの動作をシミュレートするためのオブジェクトです。 テスト対象クラスが呼び出している（＝依存している）クラスをMockで差し替え、
 * Mockの動作内容を定義することで、望むテスト条件を容易に作ることができます.
 */
@ExtendWith(MockitoExtension.class)
class UsersDaoparameterizedTest {

  // モック化するクラスのインスタンスを生成します。
  @Mock
  public JdbcTemplate mockJdbc;
  @Mock
  UsersService usersService = new UsersService();

  // モック化するクラスのインスタンス（テスト対象）
  @InjectMocks

  private UsersDao usersDao;

  // this(dao)を初期化します。
  @BeforeEach
  private void initMocks() {
    MockitoAnnotations.initMocks(this);
  }
  // @Before
  // public void beforeeach() {
  // // 事前に検索対象となるユーザを追加
  // }
  //
  // public void after() {
  // // テスト実施後に追加されたユーザを削除
  // }

  /**
   * カバレッジ網羅率 if文のtrueとfalseを両方テストする。<br>
   * UsersDtoに任意にいれた値(期待値) と <br>
   * usersDao.selectOneの固定の値があっているか確認？<br>
   * 両方うまくとれていたら＝になり成功する？<br>
   */
  @ParameterizedTest
  @ValueSource(ints = {1, 2, 100}) // それぞれの値をいれて実行される・引数１個のみ
  public void selectOneTest(int i) {
    UsersDao dao = Mockito.mock(UsersDao.class);
    UsersDto dto = new UsersDto();

    // ここでモックを作成しています。実際のDbにはアクセスしない。
    Mockito.when(usersService.findByPk(String.valueOf(i))).thenReturn(dto);
    Mockito.when(dao.selectOne(String.valueOf(i))).thenReturn(dto);

    UsersDto resultDto = usersService.findByPk(String.valueOf(i));
    assertEquals(dto.getUserId(), is(resultDto.getUserId()));
    assertNotEquals(dto.getUserId(), is(resultDto.getUserId()));
    assertThat(dto.getUserId(), is(resultDto.getUserId()));



    // selectOneのてすと
    UsersDto dto2 = new UsersDto();
    // 期待値のセット？
    dto2.setUserId("1");
    dto2.setUserName("てきとう");

    // UsersDaoの値を取得？
    UsersDto resultDto2 = usersDao.selectOne("1");

    // 両方を比較
    assertEquals(dto2.getUserId(), resultDto2.getUserId());
    // assertEquals(dto.getUserName(), resultDto.getUserName());
  }


  class SelectTest {

    @ParameterizedTest
    @MethodSource("testprovider")
    public void selectTest(String inputUserId, String inputUserName, String inputRoleName,
        String outUserId) {
      System.out.println("inputUserId:" + inputUserId + "inputUserName:" + inputUserName
          + "inputRoleName:" + inputRoleName + "outUserId:" + outUserId);
      UsersDto inputDto = new UsersDto();

      // Dtoに格納
      inputDto.setUserId(inputUserId);
      inputDto.setUserName(inputUserName);
      inputDto.setRoleName(inputRoleName);
      UsersDto resultDto = new UsersDto();
      resultDto.setUserId(outUserId);
      List<UsersDto> resultList = new ArrayList<>();
      resultList.add(resultDto);
      Mockito.when(usersDao.select(inputDto)).thenReturn(resultList);

      StringBuilder sqlbuilder = new StringBuilder();
      sqlbuilder.append("SELECT * FROM USERS ");
      sqlbuilder.append(" WHERE USER_ID LIKE ? ");
      sqlbuilder.append("        AND USER_NAME LIKE ? ");
      sqlbuilder.append("        AND ROLE_NAME LIKE ? ");


      // when(mockJdbc.query(eq(sqlbuilder), any(RowMapper.class))).thenReturn(expected);
      List<UsersDto> returnList = usersDao.select(inputDto);

      // 取得した値を比較
      assertThat(returnList.get(0), is(resultList.get(0)));


      // selectOneのてすと
      UsersDto dto = new UsersDto();
      // 期待値のセット？
      dto.setUserId("1");
      dto.setUserName("てきとう");

      // UsersDaoの値を取得？
      UsersDto serchDto = new UsersDto();
      serchDto.setUserName("");
      List<UsersDto> resultList2 = usersDao.select(serchDto);

      // 両方を比較
      assertEquals(dto.getUserId(), ((UsersDto) resultList).getUserId());
      // // assertEquals(dto.getUserName(), resultDto.getUserName());

    }

    @Test
    public void test_isEmpty() {
      UsersDto dto = new UsersDto();
      UsersDto usersDto = new UsersDto();

      // isEmptyStr(dto.getUserId())の値のテスト
      // dto.setUserId("");
      dto.setUserId("");
      List<UsersDto> resultListId = usersDao.select(usersDto);

      // dto.setUserId("1");
      usersDto.setUserId("1");
      List<UsersDto> resultListEmpId = usersDao.select(usersDto);
      assertThat(resultListId, is(resultListEmpId));

      // isEmptyStr(dto.getUserName())の値のテス
      dto.setUserName("");
      List<UsersDto> resultListName = usersDao.select(usersDto);

      // dto.setUserName("AAAAA");
      usersDto.setUserName("AAAAA");
      List<UsersDto> resultListEmpName = usersDao.select(usersDto);
      assertThat(resultListName, is(resultListEmpName));

      // isEmptyStr(dto.getUserId())の値のテスト
      dto.setRoleName("");
      List<UsersDto> resultListRoleName = usersDao.select(usersDto);

      // dto.setRoleName("AAAAA");
      usersDto.setRoleName("AAAAA");
      List<UsersDto> resultListEmpRoleName = usersDao.select(usersDto);
      assertThat(resultListRoleName, is(resultListEmpRoleName));
    }
  }


  @ParameterizedTest
  @MethodSource("testUpdateProvider")
  void testUpdate(String sql, UsersDto usersDto, int expected) {
    ArgumentCaptor<String> captorSql = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<MapSqlParameterSource> captorParamSource =
        ArgumentCaptor.forClass(MapSqlParameterSource.class);

    when(mockJdbc.update(anyString(), any(MapSqlParameterSource.class))).thenReturn(expected);

    int result = usersDao.update(usersDto);

    verify(mockJdbc, times(1)).update(captorSql.capture(), captorParamSource.capture());
    assertEquals(sql, captorSql.getValue());
    assertEquals(usersDto.getUserId(),
        usersDto.getUserId() == null ? null : captorParamSource.getValue().getValue("userId"));
    assertEquals(usersDto.getPassword(),
        usersDto.getPassword() == null ? null : captorParamSource.getValue().getValue("password"));
    assertEquals(usersDto.getUserName(),
        usersDto.getUserName() == null ? null : captorParamSource.getValue().getValue("userName"));
    assertEquals(usersDto.getRoleName(),
        usersDto.getRoleName() == null ? null : captorParamSource.getValue().getValue("roleName"));
    assertEquals(usersDto.getLoginDeniedAt(), usersDto.getLoginDeniedAt() == null ? null
        : captorParamSource.getValue().getValue("loginDeniedAt"));
    assertTrue(expected == result);
  }

  static Stream<Arguments> testUpdateProvider() {
    StringBuilder sql1Builder = new StringBuilder();
    sql1Builder.append("UPDATE USERS ");
    sql1Builder.append("SET ");
    sql1Builder.append("PASSWORD = :password ,");
    sql1Builder.append("USER_NAME = :userName ,");
    sql1Builder.append("ROLE_NAME = :roleName ,");
    sql1Builder.append("LOGIN_FAILURE_COUNT = :loginFailureCount ,");
    sql1Builder.append("LOGIN_DENIED_AT = :loginDeniedAt ,");
    sql1Builder.append("UPDATER_ID = :updaterId ,");
    sql1Builder.append("UPDATED_AT = :updatedAt ");
    sql1Builder.append("WHERE USER_ID = :userId ");



    StringBuilder sql2Builder = new StringBuilder();
    sql2Builder.append("UPDATE USERS ");
    sql2Builder.append("SET ");
    sql2Builder.append("LOGIN_FAILURE_COUNT = :loginFailureCount ,");
    sql2Builder.append("UPDATER_ID = :updaterId ,");
    sql2Builder.append("UPDATED_AT = :updatedAt ");
    sql2Builder.append("WHERE USER_ID = :userId ");



    String userId = "admin";
    String password = "passwd";
    String userName = "管理者太郎";
    String roleName = "ADMIN";
    UsersDto usersDto = new UsersDto();
    usersDto.setUserId(userId);
    usersDto.setPassword(password);
    usersDto.setUserName(userName);
    usersDto.setRoleName(roleName);
    usersDto.setLoginDeniedAt((java.sql.Date) Date.from(ZonedDateTime.now().toInstant()));
    int expected = 1;


    String sql1 = sql1Builder.toString();
    String sql2 = sql2Builder.toString();
    return Stream.of(arguments(sql1, usersDto, expected),
        arguments(sql2, new UsersDto(), expected));
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
