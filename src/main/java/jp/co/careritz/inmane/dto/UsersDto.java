package jp.co.careritz.inmane.dto;

import java.io.Serializable;
import java.sql.Date;
import lombok.Data;

/**
 * ユーザ情報DTO.
 */
@Data
public class UsersDto implements Serializable {

  /** シリアルバージョンUID. */
  private static final long serialVersionUID = 1L;

  /** ユーザID. */
  private String userId;
  /** パスワード. */
  private String password;
  /** ユーザ名. */
  private String userName;
  /** ロール名. */
  private String roleName;
  /** ログイン失敗回数. */
  private int loginFailureCount;
  /** ログイン拒否時間. */
  private Date loginDeniedAt;

  /** 更新者ID. */
  private String updaterId;
  /** 更新日時. */
  private Date updatedAt;
  /** 作成者ID. */
  private String createrId;
  /** 作成日時. */
  private Date createdAt;

}
