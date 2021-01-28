package jp.co.careritz.inmane.service.security;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import jp.co.careritz.inmane.config.PropertyConfig;
import jp.co.careritz.inmane.dto.UsersDto;
import jp.co.careritz.inmane.model.security.SecurityUserModel;
import jp.co.careritz.inmane.service.UsersService;


/**
 * ログイン認証サービス.
 */
@Service
public class SecurityLoginService implements UserDetailsService {

  // ----------------------------------------------------------------------
  // インスタンス変数
  // ----------------------------------------------------------------------
  @Autowired
  private PropertyConfig propertyConfig;
  @Autowired
  private UsersService usersService;
  // ロガー
  static Logger log = LoggerFactory.getLogger(SecurityLoginService.class);

  // ----------------------------------------------------------------------
  // インスタンスメソッド
  // ----------------------------------------------------------------------
  @Override
  public UserDetails loadUserByUsername(String userid) throws UsernameNotFoundException {

    /* ---------------------------------------------------- */
    /* ローカル変数の宣言と初期化 */
    /* ---------------------------------------------------- */
    // ログイン認証情報
    SecurityUserModel user = new SecurityUserModel();

    // アカウントロック対象失敗件数
    Integer lockAttemptCount = Integer.valueOf(propertyConfig.get("login.lock.attempt.count"));

    if (log.isDebugEnabled()) {
      log.info(String.format("propertyConfig:%s", propertyConfig.get("login.lock.attempt.count")));
    }
    // アカウントロック時間（秒）
    Long lockSecond = Long.valueOf(propertyConfig.get("login.lock.second"));
    // 社員情報の取得
    UsersDto usersDto = usersService.findByPk(userid);

    /* ---------------------------------------------------- */
    /* 認証処理 */
    /* ---------------------------------------------------- */
    if (usersDto != null) {

      if (log.isDebugEnabled()) {
        log.info(String.format("userDto.userId :%s", usersDto.getUserId()));
        log.info(String.format("userDto.roleName:%s", usersDto.getRoleName()));
      }

      // ログイン認証情報に社員情報をコピー
      BeanUtils.copyProperties(usersDto, user);

      SimpleGrantedAuthority authority = new SimpleGrantedAuthority(usersDto.getRoleName());
      List<SimpleGrantedAuthority> authorities = new ArrayList<>();
      authorities.add(authority);
      user.setAuthorities(authorities);

      // アカウントロック中かどうかの検証
      if (lockAttemptCount.compareTo(user.getLoginFailureCount() + 1) <= 0
          && lockSecond.compareTo(getDiffDateSecond(user.getLoginDeniedAt(), new Date())) >= 0) {
        user.setAccountLocked(true);
      }

      return user;
    }

    throw new UsernameNotFoundException("user not found");

  }

  /**
   * 引数で指定された日付型オブジェクトの秒差を返します.
   *
   * @param from 検証対象日付(FROM)
   * @param to 検証対象日付(TO)
   * @return 検証対象日付(FROM)と検証対象日付(TO)の秒差
   */
  public static Long getDiffDateSecond(Date from, Date to) {

    // 入力検証
    if (from == null || to == null) {
      return null;
    }
    // 日付差
    long dayDif = (to.getTime() - from.getTime()) / 1000;

    return Long.valueOf(dayDif);
  }
}
