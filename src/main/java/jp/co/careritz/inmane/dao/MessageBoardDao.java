package jp.co.careritz.inmane.dao;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import jp.co.careritz.inmane.config.PropertyConfig;
import jp.co.careritz.inmane.dto.MessageBoardDto;
import jp.co.careritz.inmane.dto.MessageViewDto;
import jp.co.careritz.inmane.util.AppUtil;

/**
 * メッセージ機能用Dao.
 *
 */
@Repository
public class MessageBoardDao {

  // ロガーは一番上に記述し、private static finalとする
  private static final Logger log = LoggerFactory.getLogger(MessageBoardDao.class);

  // jdbcTempleteはDataSourceを含んでいるので@Autowired private DataSource dataSource;はいらなくなる
  // publicメソッド -> protected -> default -> privateメソッド
  private JdbcTemplate jdbcTemplate;



  // @Autowiredは非推奨の流れなので、コンストラクタを作成する？

  @Autowired
  public MessageBoardDao(PropertyConfig propertyConfig, JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;

  }

  /**
   * deleteFlagがない(0:削除済み扱いでない)メッセージを全件取得.
   *
   * @return メッセージリスト
   */
  public List<MessageViewDto> selectAll() {

    // SQL文
    StringBuilder sqlBuilder = new StringBuilder();
    sqlBuilder.append("SELECT *");
    sqlBuilder.append(",( SELECT count(*)");
    sqlBuilder.append("    FROM message_reply_board AS reply");
    sqlBuilder.append("    WHERE message.id = reply.message_id) AS countReply ");
    sqlBuilder.append("FROM  ");
    sqlBuilder.append("message_board AS  message ");
    sqlBuilder.append(" WHERE delete_flag = 0");



    // BeanPropertyRowMapperの宣言：DBからデータ1件を取得するタイミングで独自の処理が行われる
    RowMapper<MessageViewDto> rowMapper = new BeanPropertyRowMapper<>(MessageViewDto.class);

    // ｊｄｂｃTempleteの実行：query して結果を格納
    List<MessageViewDto> resultDtoList = jdbcTemplate.query(sqlBuilder.toString(), rowMapper);


    AppUtil.throwLogInfo(sqlBuilder, log);

    // 呼び出し元に取得結果を返却
    return resultDtoList;
  }



  /**
   * メッセージを取得する.
   *
   * @param id messegeId
   * @return 1つのメッセージを格納したDTO
   */
  public MessageBoardDto selectOne(final String id) {
    MessageBoardDto resultDto = null;

    // SQL文
    // 権限がADMIN、または作成者かどうかみる
    StringBuilder sqlBuilder = new StringBuilder();
    sqlBuilder.append("SELECT ");
    sqlBuilder.append("   message.* ");
    sqlBuilder.append("FROM ");
    sqlBuilder.append("  message_board AS message ");
    sqlBuilder.append("  LEFT JOIN USERS AS users ");
    sqlBuilder.append("    ON users.user_id = message.created_id ");
    sqlBuilder.append("WHERE ");
    sqlBuilder.append("  id =  ? ");
    sqlBuilder.append("  AND ( ");
    sqlBuilder.append("    users.user_id = message.created_id ");
    sqlBuilder.append("    OR users.role_name = 'ADMIN' ");
    sqlBuilder.append("  ); ");



    // ｊｄｂｃTempleteの実行：queryForObjectは１つの時のみ利用？
    try {
      resultDto = jdbcTemplate.queryForObject(sqlBuilder.toString(), new Object[] {id},
          new BeanPropertyRowMapper<MessageBoardDto>(MessageBoardDto.class));
      AppUtil.throwLogInfo(sqlBuilder, log);
      // ▲データベースとDtoのフィールド名が同じものが自動で取得される
    } catch (EmptyResultDataAccessException e) {
      log.debug("nullです");
    }

    // 呼び出し元に取得結果を返却
    return resultDto;

  }


  /**
   * メッセージを登録する.
   *
   * @param dto UsersDto
   *
   * @return 処理結果（0: %s 正常終了）
   */
  public int create(final MessageBoardDto dto) {

    // SQL文
    StringBuilder sqlBuilder = new StringBuilder();
    sqlBuilder.append("INSERT INTO message_board (");

    sqlBuilder.append("name ");
    sqlBuilder.append(",message ");
    sqlBuilder.append(",created_id ");
    sqlBuilder.append(",updated_id ");
    sqlBuilder.append(") VALUES (");
    sqlBuilder.append(" ? ");
    sqlBuilder.append(",? ");
    sqlBuilder.append(",? ");
    sqlBuilder.append(",? ");
    sqlBuilder.append(") ");

    AppUtil.throwLogInfo(sqlBuilder, log);

    // Objectに 各項目値をバインド
    Object[] params = {dto.getName(), dto.getMessage(), dto.getCreatedId(), dto.getUpdatedId()};

    // jdbcTemplateの実行：update
    jdbcTemplate.update(sqlBuilder.toString(), params);

    return 0;
  }



  /**
   * メッセージを削除する.
   *
   * @param id メッセージID
   * @return 処理結果（0: 正常終了）
   */
  public int delete(final String id) {

    // SQL文
    StringBuilder sqlBuilder = new StringBuilder();
    sqlBuilder.append("DELETE FROM message_board ");
    sqlBuilder.append(" WHERE ID = ? ");

    AppUtil.throwLogInfo(sqlBuilder, log);

    // ｊｄｂｃTempleteの実行：update
    jdbcTemplate.update(sqlBuilder.toString(), id);

    return 0;
  }


  /**
   * update用取得.
   *
   * @param id メッセージID
   * @return メッセージ
   */
  public MessageBoardDto selectOneForUpadate(final String id) {

    // SQL文
    StringBuilder sqlBuilder = new StringBuilder();
    sqlBuilder.append("SELECT * FROM  message_board WHERE ID = ? FOR UPDATE");

    // jdbcTemplateの実行：queryForObjectは１つの時のみ利用？
    MessageBoardDto resultDto = jdbcTemplate.queryForObject(sqlBuilder.toString(),
        new Object[] {id}, new BeanPropertyRowMapper<MessageBoardDto>(MessageBoardDto.class));
    // データベースとDtoのフィールド名が同じものが自動で取得される

    AppUtil.throwLogInfo(sqlBuilder, log);

    // 呼び出し元に取得結果を返却
    return resultDto;
  }


  /**
   * メッセージを更新する.
   *
   * @param dto UsersDto
   *
   * @return 処理結果（0: 正常終了）
   *
   */
  public int update(final MessageBoardDto dto) {
    // SQL文
    StringBuilder sqlBuilder = new StringBuilder();
    sqlBuilder.append("UPDATE message_board ");
    sqlBuilder.append("SET ");

    sqlBuilder.append(" updated_at = ? ");
    sqlBuilder.append(" ,updated_id = ? ");
    if (AppUtil.isNotEmptyStr(dto.getName())) {
      sqlBuilder.append(",name = ?");
    }
    if (AppUtil.isNotEmptyStr(dto.getMessage())) {
      sqlBuilder.append(",message = ? ");
    }

    sqlBuilder.append(" WHERE id = ? ");


    // ?に記述される各項目値をListに格納
    List<Object> paramList = new ArrayList<>();

    paramList.add(dto.getUpdatedAt());
    if (dto.getUpdatedId() != null) {
      paramList.add(dto.getUpdatedId());
    }
    if (AppUtil.isNotEmptyStr(dto.getName())) {
      paramList.add(dto.getName());
    }
    if (AppUtil.isNotEmptyStr(dto.getMessage())) {
      paramList.add(dto.getMessage());
    }
    paramList.add(dto.getId());


    AppUtil.throwLogInfo(sqlBuilder, log);

    // jdbcTempleteの実行：update( listで呼び出すとlistのリンクだけわたってくるので、toArrayに変換)
    jdbcTemplate.update(sqlBuilder.toString(), paramList.toArray());


    return 0;
  }


  /**
   * メッセージを論理削除. delete_flagに１をつけて削除扱いにする.
   *
   * @param dto MessageDto
   * @return 処理結果（0: 正常終了）
   */
  public int setDeleteFlag(MessageBoardDto dto) {

    // SQL文
    StringBuilder sqlBuilder = new StringBuilder();
    sqlBuilder.append("UPDATE message_board ");
    sqlBuilder.append("SET delete_flag = !delete_flag");
    sqlBuilder.append(", deleted_Id = ?");
    sqlBuilder.append(", deleted_At = ?");
    sqlBuilder.append(" WHERE id = ? ");

    // Objectに 各項目値をバインド
    Object[] params = {dto.getDeletedId(), dto.getDeletedAt(), dto.getId()};

    // jdbcTempleteの実行：update
    jdbcTemplate.update(sqlBuilder.toString(), params);

    AppUtil.throwLogInfo(sqlBuilder, log);

    return 0;
  }


}
