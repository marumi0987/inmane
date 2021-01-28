package jp.co.careritz.inmane.dto;

import org.springframework.web.multipart.MultipartFile;
import lombok.Data;


/**
 * 画像データDTO.
 */
@Data
public class MessageFileDto {

  /** ID. */
  private String id;

  /** メッセージId. */
  private String messageId;

  /** データファイル. */
  private MultipartFile fileData;

}
