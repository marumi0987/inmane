package jp.co.careritz.inmane.dto;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;


/**
 * メッセージ機能用DTO.
 */
@Data


public class MessageBoardDto implements Serializable {


  /** シリアルバージョンUID. */
  private static final long serialVersionUID = 1L;// 本来はユニークの値をセットする必要がある。コンパイルエラーを防ぐために書いている


  /** ID. */
  private String id;

  /** ユーザ名. */
  private String name;
  /** メッセージ. */
  private String message;

  /** 作成者ID. */
  private String createdId;
  /** 作成日時. */
  private Date createdAt;

  /** 更新者ID. */
  private String updatedId;
  /** 更新日時. */
  private Date updatedAt;

  /** 削除フラグ. */
  private boolean deleteFlag;
  /** 削除者ID. */
  private String deletedId;
  /** 削除日時. */
  private Date deletedAt;


}
