package jp.co.careritz.inmane.service;


import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jp.co.careritz.inmane.dao.MessageReplyDao;
import jp.co.careritz.inmane.dto.MessageReplyDto;

/**
 * メッセージ返信サービス.
 *
 */
@Service
public class MessageReplyService {

  @Autowired
  private MessageReplyDao dao;


  /**
   * メッセージIDをもとに１つのリプライを表示する.
   *
   * @param id メッセージID
   * @return リプライメッセージを格納したDTO
   */
  public MessageReplyDto findByPk(String id) {

    return dao.selectOne(id);

  }

  /**
   * ユーザ情報を更新する.
   *
   * @param dto ユーザを格納したリスト
   *
   */
  @Transactional
  public int updateByPk(MessageReplyDto dto) {
    dao.selectOneForUpadate(dto.getId());
    return dao.update(dto);
  }

  /**
   * メッセージ投稿.
   *
   * @param dto 内容格納用dto
   * @return
   */
  public int create(MessageReplyDto dto) {
    return dao.create(dto);
  }

  /**
   * メッセージ削除.
   *
   * @param id メッセージID
   */
  public int deleteByPk(String id) {

    return dao.setDeleteFlag(id);
  }

  /**
   * 全件表示.
   *
   * @return deleteフラグが０（１が削除扱い）のメッセージ
   */
  public List<MessageReplyDto> findAll(String id) {

    return dao.selectAll(id);
  }


  /**
   * メッセージIDに対するコメント数をカウント.
   *
   * @param id メッセージID
   * @return コメントの数
   */
  public int countReply(String id) {
    return dao.countReply(id);
  }


  /**
   * 自分宛のメッセージ一覧表示.
   *
   * @param id 作成者ID
   * @return 自分宛のメッセージ一覧
   */
  public List<MessageReplyDto> findReplyToMe(String id) {
    return dao.findReplyToMe(id);
  }
}


