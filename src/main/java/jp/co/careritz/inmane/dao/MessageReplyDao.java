package jp.co.careritz.inmane.dao;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import jp.co.careritz.inmane.config.PropertyConfig;
import jp.co.careritz.inmane.dto.MessageBoardDto;
import jp.co.careritz.inmane.dto.MessageReplyDto;
import jp.co.careritz.inmane.util.AppUtil;


/**
 * MessageReply用Dao.
 */
@Repository
public class MessageReplyDao {

  // ロガーは一番上に記述し、private static finalとする
  private static final Logger log = LoggerFactory.getLogger(MessageReplyDao.class);

  // jdbcTempleteはDataSourceを含んでいるので@Autowired private DataSource dataSource;はいらなくなる
  // publicメソッド -> protected -> default -> privateメソッド
  private JdbcTemplate jdbcTemplate;


  // @Autowiredは非推奨の流れなので、コンストラクタを作成する？
  @Autowired
  public MessageReplyDao(PropertyConfig propertyConfig, JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;

  }

  /**
   * １つのメッセージを取得する.
   *
   * @param id メッセージID
   * @return １つのメッセージを格納したdto
   *
   */
  public MessageReplyDto selectOne(final String id) {

    // SQL文
    StringBuilder sqlBuilder = new StringBuilder();
    sqlBuilder.append("SELECT * FROM message_reply_board WHERE ID = ?");

    AppUtil.throwLogInfo(sqlBuilder, log);

    // jdbcTempleteの実行：queryForObjectは１つの時のみ利用？
    MessageReplyDto resultDto = jdbcTemplate.queryForObject(sqlBuilder.toString(),
        new Object[] {id}, new BeanPropertyRowMapper<MessageReplyDto>(MessageReplyDto.class));
    // ▲データベースとDtoのフィールド名が同じものが自動で取得される


    // 呼び出し元に取得結果を返却
    return resultDto;

  }


  /**
   * メッセージIDに対するリプライを全件表示.
   *
   * @param id メッセージID
   * @return リプライメッセージを全件格納したdto
   */
  public MessageBoardDto selectReply(final String id) {
    // SQL文
    StringBuilder sqlBuilder = new StringBuilder();


    AppUtil.throwLogInfo(sqlBuilder, log);
    // ｊｄｂｃTempleteの実行：queryForObjectは１つの時のみ利用？
    MessageBoardDto resultDto = jdbcTemplate.queryForObject(sqlBuilder.toString(), new Object[] {id},
        new BeanPropertyRowMapper<MessageBoardDto>(MessageBoardDto.class));// データベースとDtoのフィールド名が同じものが自動で取得される


    // 呼び出し元に取得結果を返却
    return resultDto;

  }


  /**
   * 編集する.
   *
   * @param dto MessageReplyDto
   *
   * @return 処理結果（0: 正常終了）
   *
   */
  public int update(final MessageReplyDto dto) {
    // SQL文
    StringBuilder sqlBuilder = new StringBuilder();
    sqlBuilder.append("UPDATE message_reply_board ");
    sqlBuilder.append("SET ");


    if (AppUtil.isNotEmptyStr(dto.getName())) {
      sqlBuilder.append("NAME = ? ,");
    }
    if (AppUtil.isNotEmptyStr(dto.getMessage())) {
      sqlBuilder.append("MESSAGE = ? ,");
    }
    sqlBuilder.append("CREATED_ID = ? ,");
    sqlBuilder.append("CREATED_AT = ? ");
    sqlBuilder.append("WHERE ID = ? ");


    // ?に記述される各項目値をListに格納
    List<Object> paramList = new ArrayList<>();

    if (AppUtil.isNotEmptyStr(dto.getName())) {
      paramList.add(dto.getName());
    }
    if (AppUtil.isNotEmptyStr(dto.getMessage())) {
      paramList.add(dto.getMessage());
    }
    if (dto.getCreatedId() != null) {
      paramList.add(dto.getCreatedId());
    }
    paramList.add(dto.getCreatedAt());
    paramList.add(dto.getId());

    if (log.isDebugEnabled()) {
      log.debug(String.format(dto.getName(), dto.getMessage(), dto.getId()));
    }

    AppUtil.throwLogInfo(sqlBuilder, log);


    // jdbcTempleteの実行：update( listで呼び出すとlistのリンクだけわたってくるので、toArrayに変換)
    jdbcTemplate.update(sqlBuilder.toString(), paramList.toArray());


    return 0;
  }

  /**
   * リプライ内容を投稿する.
   *
   * @param dto MessageReplyDto
   *
   * @return 処理結果（0: 正常終了）
   */
  public int create(final MessageReplyDto dto) {

    // SQL文
    StringBuilder sqlBuilder = new StringBuilder();
    sqlBuilder.append("INSERT INTO message_reply_board (");
    sqlBuilder.append(" ID ");
    sqlBuilder.append(",MESSAGE_ID");
    sqlBuilder.append(",NAME ");
    sqlBuilder.append(",MESSAGE ");
    sqlBuilder.append(",CREATED_ID ");
    sqlBuilder.append(",CREATED_AT ");
    sqlBuilder.append(") VALUES (");
    sqlBuilder.append(" ? ");
    sqlBuilder.append(",? ");
    sqlBuilder.append(",? ");
    sqlBuilder.append(",? ");
    sqlBuilder.append(",? ");
    sqlBuilder.append(",? ");
    sqlBuilder.append(") ");

    // Objectに 各項目値をバインド
    Object[] selectParams = {dto.getId(), dto.getMessageId(), dto.getName(), dto.getMessage(),
        dto.getCreatedId(), dto.getCreatedAt()};

    AppUtil.throwLogInfo(sqlBuilder, log);

    // ｊｄｂｃTempleteの実行：update
    jdbcTemplate.update(sqlBuilder.toString(), selectParams);

    return 0;
  }



  /**
   * デリートフラグがない(0:削除済み扱いでない)リプライを取得.
   *
   * @param id メッセージID
   * @return メッセージリストを格納したDto
   *
   */
  public List<MessageReplyDto> selectAll(String id) {
    // SQL文
    StringBuilder sqlBuilder = new StringBuilder();
    sqlBuilder.append("SELECT * FROM message_reply_board WHERE delete_flag = 0 AND message_id = ?");

    // BeanPropertyRowMapperの宣言：DBからデータ1件を取得するタイミングで独自の処理が行われる
    RowMapper<MessageReplyDto> rowMapper = new BeanPropertyRowMapper<>(MessageReplyDto.class);

    // 掲示板のidをメッセージidとしてObjectに格納
    Object[] messageId = {id};

    // jdbcTemplateの実行：query して結果を格納
    List<MessageReplyDto> resultDtoList =
        jdbcTemplate.query(sqlBuilder.toString(), messageId, rowMapper);

    AppUtil.throwLogInfo(sqlBuilder, log);

    // 呼び出し元に取得結果を返却
    return resultDtoList;
  }


  /**
   * メッセージに対する返信数をカウントする.
   *
   * @param id メッセージID
   * @return メッセージIDに対する返信数
   */
  public int countReply(String id) {
    // SQL文
    String sql = "SELECT COUNT(*)  FROM  message_reply_board WHERE message_id = ?  ";

    // 掲示板のidをメッセージidとしてObjectに格納
    Object[] messageId = {id};

    // ｊｄｂｃTempleteの実行：query して結果を格納
    int count = jdbcTemplate.queryForObject(sql, messageId, Integer.class);

    if (log.isDebugEnabled()) {
      log.info(String.format("id: %s  ", id));
      log.info(String.format("count: %s  ", count));
    }

    // 呼び出し元に取得結果を返却

    return count;
  }


  /**
   * リプライを削除する.
   *
   * @param id リプライID
   * @return 処理結果（0: 正常終了）
   */
  public int delete(final String id) {

    // SQL文
    StringBuilder sqlBuilder = new StringBuilder();
    sqlBuilder.append("DELETE FROM message_board_reply ");
    sqlBuilder.append(" WHERE ID = ? ");

    AppUtil.throwLogInfo(sqlBuilder, log);

    // ｊｄｂｃTempleteの実行：update
    jdbcTemplate.update(sqlBuilder.toString(), id);

    return 0;
  }

  /**
   * アップデート用にセレクト.
   *
   * @param id メッセージID
   * @return
   */
  public MessageReplyDto selectOneForUpadate(final String id) {


    // SQL文
    StringBuilder sqlBuilder = new StringBuilder();
    sqlBuilder.append("SELECT * FROM  message_reply_board WHERE id = ? FOR UPDATE");


    // ｊｄｂｃTempleteの実行：queryForObjectは１つの時のみ利用？
    MessageReplyDto resultDto = jdbcTemplate.queryForObject(sqlBuilder.toString(),
        new Object[] {id}, new BeanPropertyRowMapper<MessageReplyDto>(MessageReplyDto.class));
    // データベースとDtoのフィールド名が同じものが自動で取得される

    AppUtil.throwLogInfo(sqlBuilder, log);


    // 呼び出し元に取得結果を返却
    return resultDto;

  }


  /**
   * メッセージにデリートフラグ(1)付与.
   *
   * @param id メッセージID
   * @return 処理結果（0: 正常終了）
   */
  public int setDeleteFlag(final String id) {

    // SQL文
    StringBuilder sqlBuilder = new StringBuilder();
    sqlBuilder.append("UPDATE message_reply_board ");
    sqlBuilder.append("SET delete_flag = 1");
    sqlBuilder.append(" WHERE id = ? ");

    AppUtil.throwLogInfo(sqlBuilder, log);

    // ｊｄｂｃTempleteの実行：update
    jdbcTemplate.update(sqlBuilder.toString(), id);

    return 0;
  }

  /**
   * ログインしているユーザー宛のメッセージ一覧表示.
   *
   * @param id メッセージ作成者のID
   * @return 自分宛のメッセージ一覧
   */
  public List<MessageReplyDto> findReplyToMe(String id) {

    // SQL文
    StringBuilder sqlBuilder = new StringBuilder();
    sqlBuilder.append("SELECT * ");
    sqlBuilder.append(" FROM message_reply_board ");
    sqlBuilder.append(" WHERE delete_flag =0 ");
    sqlBuilder.append(" AND created_id = ? ");
    sqlBuilder.append(" ORDER BY created_at");

    // BeanPropertyRowMapperの宣言：DBからデータ1件を取得するタイミングで独自の処理が行われる
    RowMapper<MessageReplyDto> rowMapper = new BeanPropertyRowMapper<>(MessageReplyDto.class);

    // 掲示板のidをメッセージidとしてObjectに格納
    Object[] messageId = {id};

    // jdbcTempleteの実行：query して結果を格納
    List<MessageReplyDto> resultDtoList =
        jdbcTemplate.query(sqlBuilder.toString(), messageId, rowMapper);

    AppUtil.throwLogInfo(sqlBuilder, log);

    // 呼び出し元に取得結果を返却
    return resultDtoList;


  }



}

