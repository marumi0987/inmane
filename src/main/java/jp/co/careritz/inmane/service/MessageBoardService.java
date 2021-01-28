package jp.co.careritz.inmane.service;


import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jp.co.careritz.inmane.dao.MessageBoardDao;
import jp.co.careritz.inmane.dto.MessageBoardDto;
import jp.co.careritz.inmane.dto.MessageViewDto;

/**
 *メッセージ機能用サービス.
 *
 */
@Service
public class MessageBoardService {

  @Autowired
  private MessageBoardDao dao;


  /**
   * １件の情報を取得する.
   *
   * @param id メッセージID
   * @return メッセージを格納したDTO
   */
  public MessageBoardDto findByPk(String id) {

    return dao.selectOne(id);

  }



  /**
   * メッセージの更新.
   *
   * @param dto メッセージDto
   * @return
   */
  @Transactional
  public int updateByPk(MessageBoardDto dto) {
    dao.selectOneForUpadate(dto.getId());
    return dao.update(dto);
  }

  /**
   * メッセージ投稿.
   *
   * @param dto 内容格納用dto
   * @return
   */
  public int create(MessageBoardDto dto) {
    return dao.create(dto);
  }



  /**
   * メッセージ 論理削除.
   *
   * @param dto メッセージIDと削除者情報と日付格納
   * @return
   */
  public int deleteByPk(MessageBoardDto dto) {

    return dao.setDeleteFlag(dto);
  }

  /**
   * メッセージの全件表示.
   *
   * @return
   */
  public List<MessageViewDto> findAll() {

    return dao.selectAll();
  }

  /**
   * 【 id＋固定の文字列】のハッシュ値を作成する.
   *
   * @param id メッセージID
   * @return id＋固定の文字列のハッシュ値
   */
  public String makeHashCode(String id) {
    String secretKey = "これは秘密のカギです";
    String value = (id + secretKey);
    String sha1 = "";

    // ハッシュ値を作成
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-1");
      digest.reset();
      digest.update(value.getBytes("utf8"));
      sha1 = String.format("%040x", new BigInteger(1, digest.digest()));
    } catch (Exception e) {
      e.printStackTrace();
    }

    return sha1;
  }


}


