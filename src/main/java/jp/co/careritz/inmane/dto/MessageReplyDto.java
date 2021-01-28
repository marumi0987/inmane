package jp.co.careritz.inmane.dto;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;


/**
 * メッセージ返信用DTO.
 */
@Data
public class MessageReplyDto implements Serializable {


  /** シリアルバージョンUID. */
  private static final long serialVersionUID = 1L;// 本来はユニークの値をセットする必要がある。コンパイルエラーを防ぐために書いている


  /** ID. */
  private String id;

  /** メッセージID. */
  private String messageId;

  /** ユーザ名. */
  private String name;

  /** メッセージ. */
  private String message;

  /** 作成者ID. */
  private String createdId;

  /** 作成日時. */
  private Date createdAt;


}
