package jp.co.careritz.inmane.service.security;

import java.sql.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import jp.co.careritz.inmane.config.PropertyConfig;
import jp.co.careritz.inmane.dto.UsersDto;
import jp.co.careritz.inmane.model.security.SecurityUserModel;
import jp.co.careritz.inmane.service.UsersService;

/**
 * ログインプロバイダー.
 */
public class SecurityLoginProvider extends DaoAuthenticationProvider {

  // ----------------------------------------------------------------------
  // インスタンス変数
  // ----------------------------------------------------------------------
  @Autowired
  private PropertyConfig propertyConfig;
  @Autowired
  private UsersService usersService;
  // ロガー
  static Logger log = LoggerFactory.getLogger(SecurityLoginProvider.class);

  // ----------------------------------------------------------------------
  // インスタンスメソッド
  // ----------------------------------------------------------------------
  @Override
  public Authentication authenticate(Authentication authentication) {

    try {
      // 認証処理
      Authentication auth = super.authenticate(authentication);

      // 認証情報の取得
      SecurityUserModel user = (SecurityUserModel) auth.getPrincipal();

      // 認証失敗情報の初期化
      UsersDto result = usersService.findByPk(user.getUserId());

      UsersDto dto = new UsersDto();
      dto.setUserId(result.getUserId());
      dto.setLoginFailureCount(0);
      dto.setLoginDeniedAt(null);
      dto.setUpdaterId(result.getUpdaterId());

      // USERデータの更新
      usersService.updateByPk(dto);

      return auth;
    } catch (BadCredentialsException e) { // 認証失敗

      // 認証失敗情報の登録
      UsersDto result = usersService.findByPk(authentication.getName());

      if (result != null) {
        Date now = new Date(System.currentTimeMillis());
        UsersDto dto = new UsersDto();
        dto.setUserId(result.getUserId());
        dto.setLoginFailureCount(result.getLoginFailureCount() + 1);
        dto.setLoginDeniedAt(now);
        dto.setUpdaterId(result.getUpdaterId());
        dto.setUpdatedAt(now);

        // USERデータの更新
        usersService.updateByPk(dto);
      }

      throw new BadCredentialsException(propertyConfig.get("error.app.login.auth"));

    } catch (DisabledException e) { // 退会
      throw new BadCredentialsException(propertyConfig.get("error.app.login.auth"));

    } catch (LockedException e) { // アカウントロック
      throw new BadCredentialsException(propertyConfig.get("error.app.login.lock"));
    }
  }

}
